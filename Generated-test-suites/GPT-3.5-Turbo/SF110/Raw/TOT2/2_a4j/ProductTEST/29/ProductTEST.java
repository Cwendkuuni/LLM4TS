import static org.junit.Assert.*;
import org.junit.Test;

public class ProductTest {

    @Test
    public void testGetProduct() {
        // Typical Use Case
        Product product = new Product();
        FullProduct fullProduct = product.getProduct("ASIN123", "Offer123", "Page123");
        assertNotNull(fullProduct);

        // Edge Case - fileIn is null
        fullProduct = product.getProduct("ASIN123", "InvalidOffer", "Page123");
        assertNull(fullProduct);

        // Edge Case - productInfoBean.getDetails().length is 0
        fullProduct = product.getProduct("ASIN456", "Offer123", "Page123");
        assertNull(fullProduct.getDetails());

        // Exception Handling Scenario
        try {
            fullProduct = product.getProduct(null, "Offer123", "Page123");
            fail("Expected exception not thrown");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

}