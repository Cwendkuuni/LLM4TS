
import static org.junit.Assert.*;
import org.junit.Test;

public class ProductTest {
    
    @Test
    public void testGetProduct() {
        // Test functionality of getProduct
        Product product = new Product();
        FullProduct result = product.getProduct("ASIN", "Offer", "Page");
        assertNotNull(result);
        // Test for null input values
        result = product.getProduct(null, null, null);
        assertNull(result);
    }
}
