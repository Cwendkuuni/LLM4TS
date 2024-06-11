
import static org.junit.Assert.*;
import org.junit.Test;

public class ProductTest {

    // Test getProduct method
    @Test
    public void testGetProduct() {
        Product product = new Product();
        FullProduct result = product.getProduct("ASIN123", "Offer123", "Page123");
        
        // Check if the result is not null
        assertNotNull(result);
        
        // Additional test scenarios for edge cases and exceptions can be added here
    }
}
