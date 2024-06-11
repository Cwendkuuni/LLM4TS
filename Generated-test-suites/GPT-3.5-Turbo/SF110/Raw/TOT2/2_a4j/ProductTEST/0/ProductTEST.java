import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;

public class ProductTest {

    @Test
    public void testGetProduct() {
        Product testProduct = new Product();
        FullProduct result = testProduct.getProduct("B01LZG4V2I", "offer", "page");
        
        assertNotNull(result);
    }
    
    @Test
    public void testGetProductWithInvalidASIN() {
        Product testProduct = new Product();
        FullProduct result = testProduct.getProduct("InvalidASIN", "offer", "page");
        
        assertNull(result);
    }
    
    @Test
    public void testGetProductWithEmptyAccessories() {
        Product testProduct = new Product();
        FullProduct result = testProduct.getProduct("B01LZG4V2I", "offer", "page");
        
        assertTrue(result.getAccessories().isEmpty());
    }
    
    // Add more edge cases and exception handling scenarios as needed

}