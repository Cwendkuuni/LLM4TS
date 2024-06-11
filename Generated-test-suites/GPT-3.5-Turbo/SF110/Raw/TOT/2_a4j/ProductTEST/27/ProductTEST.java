import org.junit.Test;
import static org.junit.Assert.*;

public class ProductTest {

    @Test
    public void testGetProduct_typicalCase() {
        // Typical Case: all parameters are valid
        Product product = new Product();
        FullProduct result = product.getProduct("ASIN1234", "offer1234", "page1234");

        // Assert the result is not null
        assertNotNull(result);
        // Assert the returned object is an instance of FullProduct
        assertTrue(result instanceof FullProduct);
        // Assert the details are correctly set in the FullProduct object
        assertNotNull(result.getDetails());
        // Assert the accessory list is not null
        assertNotNull(result.getAccessoryList());
        // Assert the similar item list is not null
        assertNotNull(result.getSimilarItemList());
    }

    @Test
    public void testGetProduct_invalidAsin() {
        // Invalid ASIN: null ASIN value
        Product product = new Product();
        FullProduct result = product.getProduct(null, "offer1234", "page1234");

        // Assert the result is null
        assertNull(result);
    }

    @Test
    public void testGetProduct_invalidFile() {
        // Invalid File: file is not found
        Product product = new Product();
        FullProduct result = product.getProduct("ASIN1234", "offer1234", "page1234");

        // Assert the result is null
        assertNull(result);
    }
}