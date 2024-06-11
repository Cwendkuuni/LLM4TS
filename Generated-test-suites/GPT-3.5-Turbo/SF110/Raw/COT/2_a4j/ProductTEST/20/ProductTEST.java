import org.junit.Test;
import static org.junit.Assert.*;

public class ProductTest {
    
    @Test
    public void testGetProduct() {
        // Basic test case
        Product product = new Product();
        FullProduct fullProduct = product.getProduct("ASIN", "Offer", "Page");
        assertNotNull(fullProduct);
        
        // Test case for null return when fileIn is null
        fullProduct = product.getProduct("InvalidASIN", "Offer", "Page");
        assertNull(fullProduct);
        
        // Test case for null return when productInfoBean is null or productInfoBean.getDetails().length <= 0
        fullProduct = product.getProduct("ASIN", "InvalidOffer", "Page");
        assertNull(fullProduct);
        
        // Test case for null return when accessoryArray or accessoryArray.size() <= 0
        fullProduct = product.getProduct("ASIN", "Offer", "InvalidPage");
        assertNull(fullProduct);
    }   
}