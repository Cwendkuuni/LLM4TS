import static org.junit.Assert.*;
import org.junit.*;
import java.io.*;

public class ProductTest {

    private Product product;
    private static final String ASIN = "B00ABC123";
    private static final String OFFER = "offer";
    private static final String PAGE = "page";

    @Before
    public void setUp() {
        product = new Product();
    }

    @Test
    public void testGetProductReturnsNotNull() {
        FullProduct fullProduct = product.getProduct(ASIN, OFFER, PAGE);
        assertNotNull(fullProduct);
    }

    // Add more test methods to thoroughly test different scenarios
    
    @After
    public void tearDown() {
        product = null;
    }
}