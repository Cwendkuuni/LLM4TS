import org.junit.Test;
import static org.junit.Assert.*;

public class ProductTest {

    @Test
    public void testGetProduct() {
        // Setup
        Product product = new Product();
        String asin = "12345";
        String offer = "offer";
        String page = "page";

        // Exercise
        FullProduct fullProduct = product.getProduct(asin, offer, page);

        // Verify
        assertNotNull(fullProduct);
        // Test more assertions here
    }

    // Add more test methods to test other methods in the Product class

}