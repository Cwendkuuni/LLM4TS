import static org.junit.Assert.*;
import org.junit.Test;

public class ProductTest {
    
    @Test
    public void testGetProduct() {
        Product product = new Product();
        
        // Test case 1: Test with valid inputs
        FullProduct result1 = product.getProduct("ASIN123", "offer123", "page123");
        assertNotNull(result1);
        
        // Test case 2: Test with null ASIN
        FullProduct result2 = product.getProduct(null, "offer123", "page123");
        assertNull(result2);
        
        // Test case 3: Test with null offer
        FullProduct result3 = product.getProduct("ASIN123", null, "page123");
        assertNull(result3);
        
        // Test case 4: Test with null page
        FullProduct result4 = product.getProduct("ASIN123", "offer123", null);
        assertNull(result4);
        
        // Test case 5: Test with invalid ASIN, offer, and page
        FullProduct result5 = product.getProduct("INVALID", "INVALID", "INVALID");
        assertNotNull(result5);
    }
}