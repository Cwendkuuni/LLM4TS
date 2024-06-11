import static org.junit.Assert.*;
import org.junit.Test;

public class ProductTest {
    
    @Test
    public void testGetProduct() {
        // Initialize test data
        String asin = "12345";
        String offer = "offer";
        String page = "page";
        
        // Create an instance of the Product class
        Product product = new Product();
        
        // Invoke the getProduct method and get the result
        FullProduct result = product.getProduct(asin, offer, page);
        
        // Assert that the result is not null
        assertNotNull(result);
        
        // Assert that the result contains the expected values
        // ...

        // Add additional assertions to test all possible scenarios
        // ...
    }
}