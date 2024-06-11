import static org.junit.Assert.*;
import org.junit.Test;

public class ProductTest {
    
    @Test
    public void testGetProduct() {
        // typical use case
        Product product = new Product();
        FullProduct fullProduct = product.getProduct("ASIN123", "offer", "page");
        assertNotNull(fullProduct);
        
        // edge cases
        // Testing null ASIN
        FullProduct nullASIN = product.getProduct(null, "offer", "page");
        assertNull(nullASIN);
        
        // Testing empty offer
        FullProduct emptyOffer = product.getProduct("ASIN123", "", "page");
        assertNotNull(emptyOffer);
        
        // Testing page with invalid format
        FullProduct invalidPage = product.getProduct("ASIN123", "offer", "invalidPage");
        assertNull(invalidPage);
        
        // error scenarios
        // Test exception handling when file fetching fails
        FullProduct exceptionHandling = product.getProduct("ASIN123", "invalidOffer", "page");
        assertNotNull(exceptionHandling);
    }
}