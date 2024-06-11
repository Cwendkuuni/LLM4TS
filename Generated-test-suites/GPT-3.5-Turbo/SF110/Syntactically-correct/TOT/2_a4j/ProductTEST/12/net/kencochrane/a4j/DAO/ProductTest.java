import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import net.kencochrane.a4j.DAO.Product;

public class ProductTest {
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @After
    public void tearDown() {
        System.setOut(System.out);
    }
    
    @Test
    public void testGetProduct_ValidInput_ReturnsFullProduct() {
        Product product = new Product();
        FullProduct fullProduct = product.getProduct("ASIN123", "offer01", "page01");
        assertNotNull(fullProduct);
    }
    
    @Test
    public void testGetProduct_NullASIN_ReturnsNull() {
        Product product = new Product();
        FullProduct fullProduct = product.getProduct(null, "offer01", "page01");
        assertNull(fullProduct);
    }
    
    @Test
    public void testGetProduct_NullOffer_ReturnsNull() {
        Product product = new Product();
        FullProduct fullProduct = product.getProduct("ASIN123", null, "page01");
        assertNull(fullProduct);
    }
    
    @Test
    public void testGetProduct_NullPage_ReturnsNull() {
        Product product = new Product();
        FullProduct fullProduct = product.getProduct("ASIN123", "offer01", null);
        assertNull(fullProduct);
    }
    
    @Test
    public void testGetProduct_FileNotFoundException_PrintsStackTrace() {
        Product product = new Product();
        FullProduct fullProduct = product.getProduct("ASIN789", "offer01", "page01");
        assertNotNull(fullProduct);
        assertEquals("java.io.FileNotFoundException", outputStreamCaptor.toString().trim());
    }
    
    @Test
    public void testGetProduct_EmptyProductInfoBean_ReturnsNull() {
        Product product = new Product();
        FullProduct fullProduct = product.getProduct("ASIN456", "offer01", "page01");
        assertNull(fullProduct);
    }
    
    @Test
    public void testGetProduct_EmptyAccessoryArray_ReturnsFullProductWithoutAccessories() {
        Product product = new Product();
        FullProduct fullProduct = product.getProduct("ASIN123", "offer02", "page02");
        assertNotNull(fullProduct);
        assertNull(fullProduct.getAccessory(0));
    }
    
    @Test
    public void testGetProduct_EmptyDetailsArray_ReturnsFullProductWithoutSimilarItems() {
        Product product = new Product();
        FullProduct fullProduct = product.getProduct("ASIN123", "offer03", "page03");
        assertNotNull(fullProduct);
        assertNull(fullProduct.getSimilarItem(0));
    }
    
    @Test
    public void testGetProduct_NullAccessoryProductDetails_ReturnsFullProductWithoutAccessories() {
        Product product = new Product();
        FullProduct fullProduct = product.getProduct("ASIN123", "offer04", "page04");
        assertNotNull(fullProduct);
        assertNull(fullProduct.getAccessory(0));
    }
    
    @Test
    public void testGetProduct_NullSimilarItem_ReturnsFullProductWithoutSimilarItems() {
        Product product = new Product();
        FullProduct fullProduct = product.getProduct("ASIN123", "offer05", "page05");
        assertNotNull(fullProduct);
        assertNull(fullProduct.getSimilarItem(0));
    }
}