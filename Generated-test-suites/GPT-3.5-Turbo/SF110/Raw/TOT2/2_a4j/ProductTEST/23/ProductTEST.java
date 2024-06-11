import static org.junit.Assert.*;
import org.junit.Test;

public class ProductTest {

    @Test
    public void testGetProduct() {
        // Test typical use case
        Product product = new Product();
        FullProduct fullProduct = product.getProduct("1234567890", "offer", "page");
        assertNotNull(fullProduct);
        
        // Test edge case: ASIN file doesn't exist
        fullProduct = product.getProduct("nonExistingASIN", "offer", "page");
        assertNull(fullProduct);
        
        // Test exception handling: Exception occurs while fetching product details
        fullProduct = product.getProduct("exceptionASIN", "offer", "page");
        assertNull(fullProduct);
    }

}