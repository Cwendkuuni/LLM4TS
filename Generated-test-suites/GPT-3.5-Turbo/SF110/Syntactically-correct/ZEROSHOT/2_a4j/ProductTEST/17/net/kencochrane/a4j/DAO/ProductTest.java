import static org.junit.Assert.*;
import org.junit.Test;

public class ProductTest {

    @Test 
    public void testGetProduct() {
        // Test case 1: Testing when file is not found, return null
        Product product = new Product();
        FullProduct result = product.getProduct("ASIN", "Offer", "Page");
        assertNull(result);

        // Test case 2: Testing when file is found and product info is not null
        result = product.getProduct("ASIN", "Offer", "Page");
        assertNotNull(result);

        // Add more test cases to cover different scenarios and edge cases
        
        // TODO: Add more test cases...
    }
}