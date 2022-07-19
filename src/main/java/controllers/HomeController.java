package controllers;

import list.CrudCategory;
import list.CrudProduct;
import models.Category;
import models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Controller
public class HomeController {
    @Autowired
    CrudCategory crudCategory;

    @Autowired
    CrudProduct crudProduct;

    @GetMapping("/home")
    public ModelAndView show(){
        ModelAndView modelAndView = new ModelAndView("Home");
        modelAndView.addObject("products", crudProduct.productList);
        return modelAndView;
    }
    @GetMapping("/create")
    public ModelAndView showCreate(){
        ModelAndView modelAndView = new ModelAndView("create");
        modelAndView.addObject("categories",crudCategory.categoryList);
        return modelAndView;
    }
    @PostMapping ("/create")
    public ModelAndView create(@RequestParam int id,@RequestParam String name,@RequestParam int price,@RequestParam MultipartFile file,@RequestParam int categoryId,@RequestParam boolean status){
        String nameImg = file.getOriginalFilename();
        try {
            FileCopyUtils.copy(file.getBytes(), new File("D:\\00. Codegym\\04. Module 4\\4. Data Binding and Form\\Demo_Thymeleaf_MD4\\src\\main\\webapp\\WEB-INF\\img\\" + nameImg));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String img = "/img/" + nameImg;


        Category category = null;
        for (int i = 0; i < crudCategory.categoryList.size(); i++) {
            if (categoryId == crudCategory.categoryList.get(i).getId()){
                category = crudCategory.categoryList.get(i);
            }
        }


        Product product = new Product(id,name,price,img,category,status);
        crudProduct.create(product);
        ModelAndView modelAndView = new ModelAndView("redirect:/home");
        return modelAndView;
    }


    @GetMapping("/edit")
    public ModelAndView showEdit(@RequestParam int id, Model model) {
        ModelAndView modelAndView = new ModelAndView("edit");
        for (Product p : crudProduct.productList) {
            if (p.getId() == id) {
                modelAndView.addObject("product", p);
                break;
            }
        }
        modelAndView.addObject("categories",crudCategory.categoryList);

        Boolean[] statusT = {true,false};
        modelAndView.addObject("statusT",statusT);

        return modelAndView;
    }

    @PostMapping("/edit")
    public String edit(@RequestParam int id, @RequestParam String name, @RequestParam int price, @RequestParam MultipartFile file, @RequestParam int categoryId, @RequestParam boolean status) {
        String nameImg = file.getOriginalFilename();
            try {
                FileCopyUtils.copy(file.getBytes(), new File("D:\\00. Codegym\\04. Module 4\\4. Data Binding and Form\\Demo_Thymeleaf_MD4\\src\\main\\webapp\\WEB-INF\\img\\" + nameImg));
            } catch (IOException e) {
                e.printStackTrace();
            }

        String img = "/img/" + nameImg;
        Category category = null;
        for (int i = 0; i < crudCategory.categoryList.size(); i++) {
            if (categoryId == crudCategory.categoryList.get(i).getId()){
                category = crudCategory.categoryList.get(i);
            }
        }
        int index = -1;
        for (int i = 0; i < crudProduct.productList.size(); i++) {
            if (id == crudProduct.productList.get(i).getId()){
                index = crudProduct.productList.indexOf(crudProduct.productList.get(i));
            }
        }


        Product product = new Product(id,name,price,img,category,status);
        crudProduct.edit(product,index);
        return "redirect:/home";
    }

    @PostMapping("/Search")
    public String search(@RequestParam String search,Model model) {
        List<Product> findSearch = new ArrayList<>();
        for (Product p : crudProduct.productList) {
            if (p.getName().contains(search)) {
                findSearch.add(p);
            }
        }
        model.addAttribute("products", findSearch);
        return "Home";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam int id) {
        for (Product p : crudProduct.productList) {
            if (p.getId() == id) {
                int index = crudProduct.productList.indexOf(p);
                File file = new File("D:\\00. Codegym\\04. Module 4\\4. Data Binding and Form\\Demo_Thymeleaf_MD4\\src\\main\\webapp\\WEB-INF\\" + p.getImg());
                System.out.println(file.delete());
                crudProduct.productList.remove(index);
                break;
            }
        }
        return "redirect:/home";
    }
}
