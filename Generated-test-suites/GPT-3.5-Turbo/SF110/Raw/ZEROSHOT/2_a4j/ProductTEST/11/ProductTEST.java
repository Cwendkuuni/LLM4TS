import org.junit.Test;
import static org.junit.Assert.*;

public class ProductTest {

    @Test
    public void testGetProduct() {
        // Create a Product instance
        Product product = new Product();
        
        // Test with valid parameters
        FullProduct result = product.getProduct("ASIN", "offer", "page");
        assertNotNull(result);
        
        // Test with null ASIN
        result = product.getProduct(null, "offer", "page");
        assertNull(result);
        
        // Test with null offer
        result = product.getProduct("ASIN", null, "page");
        assertNotNull(result);
        
        // Test with null page
        result = product.getProduct("ASIN", "offer", null);
        assertNotNull(result);
        
        // Test with null parameters
        result = product.getProduct(null, null, null);
        assertNull(result);
    }
}