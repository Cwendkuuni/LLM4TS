package net.kencochrane.a4j.DAO;

import net.kencochrane.a4j.file.*;
import java.util.*;
import com.wutka.jox.*;
import net.kencochrane.a4j.beans.*;
import java.io.*;

public class Product
{
    public FullProduct getProduct(final String asin, final String offer, final String page) {
        final FileUtil fileUtil = new FileUtil();
        JOXBeanInputStream joxIn = null;
        final FullProduct fullProduct = new FullProduct();
        ProductDetails productDetails = new ProductDetails();
        ProductDetails accessoryProductDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        ArrayList detailsArray = new ArrayList();
        MiniProduct miniProduct = new MiniProduct();
        final Search search = new Search();
        try {
            FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
            if (fileIn == null) {
                return null;
            }
            joxIn = new JOXBeanInputStream((InputStream)fileIn);
            ProductInfo productInfoBean = (ProductInfo)joxIn.readObject((Class)ProductInfo.class);
            joxIn.close();
            fileIn.close();
            if (productInfoBean != null && productInfoBean.getDetails().length > 0) {
                productDetails = productInfoBean.getDetails()[0];
                fullProduct.setDetails(productDetails);
                accessories = productDetails.getAccessories();
                accessoryArray = accessories.getAccessoryArray();
                if (accessoryArray != null && accessoryArray.size() > 0) {
                    fileIn = fileUtil.fetchAccessories(asin, accessoryArray);
                    if (fileIn != null) {
                        joxIn = new JOXBeanInputStream((InputStream)fileIn);
                        productInfoBean = (ProductInfo)joxIn.readObject((Class)ProductInfo.class);
                        joxIn.close();
                        fileIn.close();
                        if (productInfoBean != null && productInfoBean.getProductsArrayList().size() > 0) {
                            detailsArray = productInfoBean.getProductsArrayList();
                            for (int x = 0; x < detailsArray.size(); ++x) {
                                accessoryProductDetails = detailsArray.get(x);
                                if (accessoryProductDetails != null) {
                                    miniProduct = new MiniProduct();
                                    miniProduct.setAsin(accessoryProductDetails.getAsin());
                                    miniProduct.setImageURL(accessoryProductDetails.getImageUrlSmall());
                                    miniProduct.setManufacturer(accessoryProductDetails.getManufacturer());
                                    miniProduct.setName(accessoryProductDetails.getProductName());
                                    miniProduct.setPrice(accessoryProductDetails.getOurPrice());
                                    fullProduct.addAccessory(miniProduct);
                                }
                            }
                        }
                    }
                }
                productInfoBean = search.SimilaritesSearch(asin, "1");
                if (productInfoBean != null && productInfoBean.getProductsArrayList() != null && productInfoBean.getProductsArrayList().size() > 0) {
                    detailsArray = productInfoBean.getProductsArrayList();
                    for (int x = 0; x < detailsArray.size(); ++x) {
                        accessoryProductDetails = detailsArray.get(x);
                        if (accessoryProductDetails != null) {
                            miniProduct = new MiniProduct();
                            miniProduct.setAsin(accessoryProductDetails.getAsin());
                            miniProduct.setImageURL(accessoryProductDetails.getImageUrlSmall());
                            miniProduct.setManufacturer(accessoryProductDetails.getManufacturer());
                            miniProduct.setName(accessoryProductDetails.getProductName());
                            miniProduct.setPrice(accessoryProductDetails.getOurPrice());
                            fullProduct.addSimilarItem(miniProduct);
                        }
                    }
                }
            }
        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
        return fullProduct;
    }
}
