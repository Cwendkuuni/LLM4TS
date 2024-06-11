
import org.junit.Test;

public class ProductTest {

    @Test
    public void testGetProduct() {
        // Create test data
        String asin = "123";
        String offer = "offer1";
        String page = "page1";
        
        // Create an instance of the Product class
        Product product = new Product();
        
        // Call the getProduct method
        FullProduct result = product.getProduct(asin, offer, page);
        
        // Assert that the result is not null
        assertNotNull(result);
        
        // Add more assertions to check the functionality of the method
        
    }
}
