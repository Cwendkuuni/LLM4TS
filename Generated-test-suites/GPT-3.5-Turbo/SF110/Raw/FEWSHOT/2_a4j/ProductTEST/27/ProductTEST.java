package net.kencochrane.a4j.DAO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.junit.Test;

import com.wutka.jox.JOXBeanInputStream;

import net.kencochrane.a4j.beans.Accessories;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.beans.ProductInfo;
import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.file.Search;

public class ProductTest {
    @Test
    public void testGetProduct() throws Exception {
        Product product = new Product();
        
        String asin = "ASIN";
        String offer = "Offer";
        String page = "Page";
        
        // Mock dependencies
        FileUtil fileUtil = mock(FileUtil.class);
        JOXBeanInputStream joxIn = mock(JOXBeanInputStream.class);
        FileInputStream fileIn = mock(FileInputStream.class);
        ProductInfo productInfoBean = mock(ProductInfo.class);
        Search search = mock(Search.class);
        ArrayList<ProductDetails> detailsArray = mock(ArrayList.class);
        
        // Mock return values
        
        // Step 1
        assertNull(product.getProduct(asin, offer, page));
        
        // Step 2
        // Mock fileUtil.fetchASINFile
        // Return null
        
        assertNull(product.getProduct(asin, offer, page));
        
        // Step 3
        // Mock fileUtil.fetchASINFile
        fileIn = new FileInputStream(new File("path/to/testFile"));
        
        // Mock joxIn.readObject for productInfoBean
        productDetails = new ProductDetails();
        productDetails.setAccessories(new Accessories());
        productInfoBean.setDetails(new ProductDetails[] {productDetails});
        joxIn = new JOXBeanInputStream((InputStream)fileIn);
        when(joxIn.readObject((Class)ProductInfo.class)).thenReturn(productInfoBean);
        joxIn.close();
        fileIn.close();
        when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(fileIn);

        FullProduct fullProduct = new FullProduct();
        fullProduct.setDetails(productDetails);
        assertNotNull(product.getProduct(asin, offer, page));
        
        // Step 4
        // Mock detailsArray.size
        when(detailsArray.size()).thenReturn(1);
        
        // Mock productInfoBean.getProductsArrayList for accessoryProductDetails
        accessoryProductDetails = new ProductDetails();
        accessoryProductDetails.setAsin("ASIN");
        accessoryProductDetails.setImageUrlSmall("ImageURL");
        accessoryProductDetails.setManufacturer("Manufacturer");
        accessoryProductDetails.setProductName("ProductName");
        accessoryProductDetails.setOurPrice("Price");
        detailsArray.add(accessoryProductDetails);
        when(productInfoBean.getProductsArrayList()).thenReturn(detailsArray);
        
        FullProduct fullProduct = new FullProduct();
        fullProduct.setDetails(productDetails);
        fullProduct.addAccessory(miniProduct);
        assertNotNull(product.getProduct(asin, offer, page));
        
        // Step 5
        // Mock search.SimilaritiesSearch
        // Mock productInfoBean.getProductsArrayList for accessoryProductDetails
        // Mock detailsArray.size
        search = new Search();
        accessoryProductDetails = new ProductDetails();
        accessoryProductDetails.setAsin("ASIN");
        accessoryProductDetails.setImageUrlSmall("ImageURL");
        accessoryProductDetails.setManufacturer("Manufacturer");
        accessoryProductDetails.setProductName("ProductName");
        accessoryProductDetails.setOurPrice("Price");
        detailsArray.add(accessoryProductDetails);
        when(productInfoBean.getProductsArrayList()).thenReturn(detailsArray);

        assertNotNull(product.getProduct(asin, offer, page));
    }
}