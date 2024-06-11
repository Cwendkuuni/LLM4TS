import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProductTest {
    
    private Product product;
    
    @Before
    public void setUp() {
        product = new Product();
    }
    
    @Test
    public void testGetProduct() {
        // Typical use case
        FullProduct fullProduct = product.getProduct("ASIN123", "offer123", "page123");
        assertNotNull(fullProduct);
        
        // Edge case: ASIN not found
        FullProduct nullProduct = product.getProduct("NonExistingASIN", "offer123", "page123");
        assertNull(nullProduct);
        
        // Error scenario: Exception handling
        try {
            FullProduct exceptionProduct = product.getProduct(null, "offer123", "page123");
            // This line should not be reached
            fail("Exception was not thrown when ASIN was null");
        } catch (Exception e) {
            // Exception was successfully caught
        }
    }
}