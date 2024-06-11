
import org.junit.Test;
import static org.junit.Assert.*;

public class ProductTest {

    @Test
    public void testGetProduct() {
        // Test case 1: Test when asin, offer, and page are valid
        String asin = "ASIN1";
        String offer = "Offer1";
        String page = "Page1";
        Product product = new Product();
        FullProduct fullProduct = product.getProduct(asin, offer, page);
        assertNotNull(fullProduct);
        
        // Test case 2: Test when asin, offer, and page are empty
        asin = "";
        offer = "";
        page = "";
        fullProduct = product.getProduct(asin, offer, page);
        assertNull(fullProduct);
        
        // Test case 3: Test when asin is null
        asin = null;
        offer = "Offer2";
        page = "Page2";
        fullProduct = product.getProduct(asin, offer, page);
        assertNull(fullProduct);
        
        // Test case 4: Test when offer is null
        asin = "ASIN3";
        offer = null;
        page = "Page3";
        fullProduct = product.getProduct(asin, offer, page);
        assertNull(fullProduct);
        
        // Test case 5: Test when page is null
        asin = "ASIN4";
        offer = "Offer4";
        page = null;
        fullProduct = product.getProduct(asin, offer, page);
        assertNull(fullProduct);
        
        // Test case 6: Test when all parameters are null
        asin = null;
        offer = null;
        page = null;
        fullProduct = product.getProduct(asin, offer, page);
        assertNull(fullProduct);
    }
}
