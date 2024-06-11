import org.junit.Test;
import static org.junit.Assert.*;

public class ProductTest {
    
    @Test
    public void testGetProduct() {
        Product product = new Product();
        FullProduct result = product.getProduct("ASIN", "offer", "page");
        assertNotNull(result);
        // Add more assertions as necessary
    }
    
    @Test
    public void testGetProductWithNullInputs() {
        Product product = new Product();
        FullProduct result = product.getProduct(null, null, null);
        assertNull(result);
        // Add more assertions as necessary
    }
    
    @Test
    public void testGetProductWithEmptyInputs() {
        Product product = new Product();
        FullProduct result = product.getProduct("", "", "");
        assertNull(result);
        // Add more assertions as necessary
    }
    
    // Add more test cases for the identified edge cases and exceptions
    
}