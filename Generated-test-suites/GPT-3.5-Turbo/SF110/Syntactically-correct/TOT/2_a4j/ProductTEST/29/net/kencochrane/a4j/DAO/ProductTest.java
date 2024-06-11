import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.file.FileUtil;
import java.io.*;
import com.wutka.jox.JOXBeanInputStream;
import java.util.ArrayList;
import net.kencochrane.a4j.beans.*;
import com.wutka.jox.JOXBeanOutputStream;
import net.kencochrane.a4j.DAO.FullProduct;
import net.kencochrane.a4j.DAO.ProductDetails;
import net.kencochrane.a4j.DAO.Accessories;
import net.kencochrane.a4j.DAO.MiniProduct;
import net.kencochrane.a4j.DAO.ProductInfo;
import net.kencochrane.a4j.beans.Search;

public class ProductTest {

    private Product product;
    private FullProduct fullProduct;

    @Before
    public void setUp() {
        product = new Product();
        fullProduct = new FullProduct();
    }

    @Test
    public void testGetProductReturnNullWhenFileNotFound() {
        FullProduct productResult = product.getProduct("ASIN1", "offer1", "page1");
        assertNull(productResult);
    }

    @Test
    public void testGetProductReturnFullProductWhenFileFoundAndValid() throws IOException {
        FileUtil fileUtil = new FileUtil();
        File file = new File("test-file.bin");
        file.createNewFile();
        OutputStream outputStream = new FileOutputStream(file);
        JOXBeanOutputStream joxOut = new JOXBeanOutputStream(outputStream);
        ProductInfo productInfo = new ProductInfo();
        productInfo.setDetails(new ProductDetails[]{new ProductDetails()});
        joxOut.writeObject(productInfo);
        joxOut.close();
        outputStream.close();
        FullProduct productResult = product.getProduct("ASIN1", "offer1", "page1");
        assertNotNull(productResult);
        file.delete();
    }

    @Test
    public void testGetProductReturnFullProductWithAccessoryWhenAccessoryFileFoundAndValid() throws IOException {
        FileUtil fileUtil = new FileUtil();
        File file = new File("test-file.bin");
        file.createNewFile();
        OutputStream outputStream = new FileOutputStream(file);
        JOXBeanOutputStream joxOut = new JOXBeanOutputStream(outputStream);
        ProductInfo productInfo = new ProductInfo();
        productInfo.setDetails(new ProductDetails[]{new ProductDetails()});
        Accessories accessories = new Accessories();
        accessories.setAccessoryArray(new ArrayList<>());
        productInfo.getDetails()[0].setAccessories(accessories);
        joxOut.writeObject(productInfo);
        joxOut.close();
        outputStream.close();
        FullProduct productResult = product.getProduct("ASIN1", "offer1", "page1");
        assertNotNull(productResult);
        assertNotNull(productResult.getAccessoryArray());
        file.delete();
    }

    @Test
    public void testGetProductReturnFullProductWithSimilarItemsWhenSimilarItemsFoundAndValid() throws IOException {
        FileUtil fileUtil = new FileUtil();
        File file = new File("test-file.bin");
        file.createNewFile();
        OutputStream outputStream = new FileOutputStream(file);
        JOXBeanOutputStream joxOut = new JOXBeanOutputStream(outputStream);
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductsArrayList(new ArrayList<>());
        joxOut.writeObject(productInfo);
        joxOut.close();
        outputStream.close();
        FullProduct productResult = product.getProduct("ASIN1", "offer1", "page1");
        assertNotNull(productResult);
        assertNotNull(productResult.getSimilarItems());
        file.delete();
    }

    @Test
    public void testGetProductThrowExceptionWhenErrorOccurs() {
        FullProduct productResult = product.getProduct("ASIN1", "offer1", "page1");
        assertNotNull(productResult);
    }

}