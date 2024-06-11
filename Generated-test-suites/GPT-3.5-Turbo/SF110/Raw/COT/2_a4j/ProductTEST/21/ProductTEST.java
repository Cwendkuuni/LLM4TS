
import org.junit.Test;
import static org.junit.Assert.*;

public class ProductTest {

    @Test
    public void testGetProduct() {
        Product product = new Product();
        
        // Test case 1: Null input for asin
        FullProduct nullAsinResult = product.getProduct(null, "offer", "page");
        assertNull(nullAsinResult);
        
        // Test case 2: Null input for offer
        FullProduct nullOfferResult = product.getProduct("asin", null, "page");
        assertNull(nullOfferResult);
        
        // Test case 3: Null input for page
        FullProduct nullPageResult = product.getProduct("asin", "offer", null);
        assertNull(nullPageResult);
        
        // Test case 4: Empty input for asin
        FullProduct emptyAsinResult = product.getProduct("", "offer", "page");
        assertNull(emptyAsinResult);
        
        // Test case 5: Empty input for offer
        FullProduct emptyOfferResult = product.getProduct("asin", "", "page");
        assertNull(emptyOfferResult);
        
        // Test case 6: Empty input for page
        FullProduct emptyPageResult = product.getProduct("asin", "offer", "");
        assertNull(emptyPageResult);
        
        // Test case 7: Valid inputs
        FullProduct validResult = product.getProduct("asin", "offer", "page");
        assertNotNull(validResult);
    }
}
