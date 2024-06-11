import static org.junit.Assert.*;
import org.junit.Test;

public class ProductTest {

    @Test
    public void testGetProduct() {
        Product product = new Product();
        FullProduct fullProduct = product.getProduct("ASIN1", "Offer1", "Page1");
        
        assertNotNull(fullProduct);
        assertNotNull(fullProduct.getDetails());
        assertEquals("ASIN1", fullProduct.getDetails().getAsin());
        // Add more assertions for other properties of fullProduct
    }
    
    @Test
    public void testGetProduct_NullFileIn() {
        Product product = new Product();
        FullProduct fullProduct = product.getProduct("ASIN2", "Offer2", "Page2");
        
        assertNull(fullProduct);
    }
    
    @Test
    public void testGetProduct_ExceptionHandling() {
        Product product = new Product();
        FullProduct fullProduct = product.getProduct("ASIN3", "Offer3", "Page3");
        
        assertNull(fullProduct);
        // Add more assertions for exception handling scenarios
    }
    
    // Add more test cases for edge cases and specific scenarios
    
}