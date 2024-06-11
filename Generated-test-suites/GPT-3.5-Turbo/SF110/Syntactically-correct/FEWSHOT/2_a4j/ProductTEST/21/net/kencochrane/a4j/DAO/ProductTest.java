import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import java.io.File;
import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.DAO.FullProduct;
import net.kencochrane.a4j.DAO.Product;
import com.wutka.jox.JOXBeanInputStream;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.beans.Accessories;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.beans.ProductInfo;
import net.kencochrane.a4j.beans.Search;
import java.io.FileInputStream;
import java.io.InputStream;

public class ProductTest {
    @Test
    public void testGetProduct() {
        Product product = new Product();
        FullProduct result = product.getProduct("ASIN1", "Offer1", "Page1");
        assertNull(result);
        
        FileUtil fileUtil = new FileUtil();
        File file = fileUtil.fetchASINFile("ASIN2", "heavy", "Offer2", "Page2");
        assertNull(file);
        
        ProductInfo productInfo = new ProductInfo();
        productInfo.setDetails(new ProductDetails[] {new ProductDetails()});
        productInfo.setProductsArrayList(new ArrayList());
        
        FileInputStream fileIn = null;
        JOXBeanInputStream joxIn = null;
        
        file = new File("file.path");
        ProductInfo nullProductInfo = null;
        
        try {
            fileIn = new FileInputStream(file);
            joxIn = new JOXBeanInputStream(fileIn);
            joxIn.writeObject(productInfo);
            joxIn.close();
            fileIn.close();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        result = product.getProduct("ASIN3", "Offer3", "Page3");
        assertEquals(new FullProduct(), result);
        
        result = product.getProduct("ASIN4", "Offer4", "Page4");
        assertNull(result);
        
        productInfo = new ProductInfo();
        productInfo.setDetails(new ProductDetails[] {new ProductDetails()});
        productInfo.setProductsArrayList(new ArrayList());
        
        file = fileUtil.fetchAccessories("ASIN5", new ArrayList());
        assertNull(file);
        
        try {
            fileIn = new FileInputStream(file);
            joxIn = new JOXBeanInputStream(fileIn);
            joxIn.writeObject(productInfo);
            joxIn.close();
            fileIn.close();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        result = product.getProduct("ASIN6", "Offer6", "Page6");
        assertEquals(new FullProduct(), result);
        
        file = null;
        nullProductInfo = null;
        
        result = product.getProduct("ASIN7", "Offer7", "Page7");
        assertEquals(new FullProduct(), result);
        
        productInfo = new ProductInfo();
        productInfo.setDetails(new ProductDetails[] {new ProductDetails()});
        productInfo.setProductsArrayList(new ArrayList());
        
        file = fileUtil.fetchAccessories("ASIN8", new ArrayList());
        assertNull(file);
        
        try {
            fileIn = new FileInputStream(file);
            joxIn = new JOXBeanInputStream(fileIn);
            joxIn.writeObject(productInfo);
            joxIn.close();
            fileIn.close();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        result = product.getProduct("ASIN9", "Offer9", "Page9");
        assertEquals(new FullProduct(), result);
        
        file = fileUtil.fetchAccessories("ASIN10", new ArrayList());
        assertNull(file);
        
        try {
            fileIn = new FileInputStream(file);
            joxIn = new JOXBeanInputStream(fileIn);
            joxIn.writeObject(nullProductInfo);
            joxIn.close();
            fileIn.close();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        result = product.getProduct("ASIN11", "Offer11", "Page11");
        assertNull(result);
        
        result = product.getProduct("ASIN12", "Offer12", "Page12");
        assertNull(result);
        
        result = product.getProduct("ASIN13", "Offer13", "Page13");
        assertNull(result);
        
        productInfo = new ProductInfo();
        productInfo.setDetails(new ProductDetails[0]);
        productInfo.setProductsArrayList(new ArrayList());
        
        try {
            fileIn = new FileInputStream(file);
            joxIn = new JOXBeanInputStream(fileIn);
            joxIn.writeObject(productInfo);
            joxIn.close();
            fileIn.close();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        result = product.getProduct("ASIN14", "Offer14", "Page14");
        assertNull(result);
        
        result = product.getProduct("ASIN15", "Offer15", "Page15");
        assertNull(result);
        
        productInfo = new ProductInfo();
        productInfo.setDetails(new ProductDetails[] {new ProductDetails()});
        productInfo.setProductsArrayList(new ArrayList());
        
        MiniProduct miniProduct = new MiniProduct();
        miniProduct.setAsin("ASIN");
        miniProduct.setImageURL("imageUrl");
        miniProduct.setManufacturer("manufacturer");
        miniProduct.setName("name");
        miniProduct.setPrice("price");
        
        ArrayList<MiniProduct> accessoryArray = new ArrayList<>();
        accessoryArray.add(miniProduct);
        
        accessories = new Accessories();
        accessories.setAccessoryArray(accessoryArray);
        
        productDetails = new ProductDetails();
        productDetails.setAccessories(accessories);
        
        productInfo.setDetails(new ProductDetails[] {productDetails});
        
        file = fileUtil.fetchAccessories("ASIN16", accessoryArray);
        assertNull(file);
        
        try {
            fileIn = new FileInputStream(file);
            joxIn = new JOXBeanInputStream(fileIn);
            joxIn.writeObject(productInfo);
            joxIn.close();
            fileIn.close();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        result = product.getProduct("ASIN17", "Offer17", "Page17");
        assertEquals(new FullProduct(), result);
        
        productInfo = new ProductInfo();
        productInfo.setDetails(new ProductDetails[] {new ProductDetails()});
        productInfo.setProductsArrayList(new ArrayList());
        
        productInfo.setProductsArrayList(detailsArray);
        
        file = null;
        nullProductInfo = null;
        
        result = product.getProduct("ASIN18", "Offer18", "Page18");
        assertEquals(new FullProduct(), result);
        
        try {
            fileIn = new FileInputStream(file);
            joxIn = new JOXBeanInputStream(fileIn);
            joxIn.writeObject(productInfo);
            joxIn.close();
            fileIn.close();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        
        result = product.getProduct("ASIN19", "Offer19", "Page19");
        assertEquals(new FullProduct(), result);
        
        result = product.getProduct("ASIN20", "Offer20", "Page20");
        assertEquals(new FullProduct(), result);
    }
}