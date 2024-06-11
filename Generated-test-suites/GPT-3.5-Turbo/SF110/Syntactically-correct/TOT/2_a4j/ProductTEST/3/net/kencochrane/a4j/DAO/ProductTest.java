import org.junit.*;
import static org.junit.Assert.*;

public class ProductTest {
    
    @Test
    public void testGetProduct_validParameters_success() {
        Product product = new Product();
        
        FullProduct fullProduct = product.getProduct("B01M0M3PV3", "10", "1");
        
        assertNotNull("FullProduct should not be null", fullProduct);
        assertNotNull("FullProduct details should not be null", fullProduct.getDetails());
        assertTrue("FullProduct accessories should not be empty", fullProduct.getAccessories().size() > 0);
        assertTrue("FullProduct similar items should not be empty", fullProduct.getSimilarItems().size() > 0);
    }
    
    @Test
    public void testGetProduct_invalidParameters_returnNull() {
        Product product = new Product();
        
        FullProduct fullProduct = product.getProduct("", "10", "1");
        
        assertNull("FullProduct should be null", fullProduct);
    }
    
    @Test
    public void testGetProduct_noAccessories_returnEmptyAccessories() {
        Product product = new Product();
        
        FullProduct fullProduct = product.getProduct("B01M0M3PV3", "10", "1");
        
        assertNotNull("FullProduct should not be null", fullProduct);
        assertNotNull("FullProduct details should not be null", fullProduct.getDetails());
        assertNotNull("FullProduct accessories should not be null", fullProduct.getAccessories());
        assertEquals("FullProduct accessories should be empty", 0, fullProduct.getAccessories().size());
    }
    
    @Test
    public void testGetProduct_noSimilarItems_returnEmptySimilarItems() {
        Product product = new Product();
        
        FullProduct fullProduct = product.getProduct("B01M0M3PV3", "10", "1");
        
        assertNotNull("FullProduct should not be null", fullProduct);
        assertNotNull("FullProduct details should not be null", fullProduct.getDetails());
        assertNotNull("FullProduct similar items should not be null", fullProduct.getSimilarItems());
        assertEquals("FullProduct similar items should be empty", 0, fullProduct.getSimilarItems().size());
    }
}