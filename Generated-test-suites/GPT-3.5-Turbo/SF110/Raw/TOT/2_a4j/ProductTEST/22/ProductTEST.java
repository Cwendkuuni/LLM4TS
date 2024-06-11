import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.io.*;

public class ProductTest {

    @Test
    public void testGetProduct() {
        // Typical Use Case
        Product product = new Product();
        assertEquals(null, product.getProduct("12345", "offer1", "page1"));

        // Edge Cases
        try {
            FileUtil fileUtil = new FileUtil();
            FileInputStream fileIn = fileUtil.fetchASINFile("12345", "heavy", "offer1", "page1");
            assertNotNull(fileIn);
            JOXBeanInputStream joxIn = new JOXBeanInputStream((InputStream)fileIn);
            ProductInfo productInfoBean = (ProductInfo)joxIn.readObject((Class)ProductInfo.class);
            joxIn.close();
            fileIn.close();
            MiniProduct miniProduct = new MiniProduct();
            miniProduct.setAsin("ASIN1");
            miniProduct.setImageURL("image1.png");
            miniProduct.setManufacturer("Manufacturer1");
            miniProduct.setName("Product1");
            miniProduct.setPrice(9.99);
            FullProduct fullProduct = new FullProduct();
            fullProduct.setDetails(productInfoBean.getDetails()[0]);
            fullProduct.addAccessory(miniProduct);
            assertSame(fullProduct, product.getProduct("12345", "offer1", "page1"));
        } catch (Exception e) {
            fail(e.getMessage());
        }

        // Error Scenarios
        Product product = new Product();

        // Invalid input parameters
        assertNull(product.getProduct("", "offer1", "page1"));  // empty asin
        assertNull(product.getProduct("12345", "", "page1"));  // empty offer
        assertNull(product.getProduct("12345", "offer1", ""));  // empty page
    }
}