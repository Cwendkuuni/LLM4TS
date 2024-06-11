import static org.junit.Assert.*;
import org.junit.Test;

public class ProductTest {

    @Test
    public void testGetProduct() {
        // Test case 1 - test with valid inputs
        Product product = new Product();
        FullProduct fullProduct = product.getProduct("ASIN123", "Offer123", "Page123");
        assertNotNull(fullProduct);
        // Assert other properties of fullProduct if required

        // Test case 2 - test with null ASIN
        fullProduct = product.getProduct(null, "Offer123", "Page123");
        assertNull(fullProduct);

        // Test case 3 - test with null offer
        fullProduct = product.getProduct("ASIN123", null, "Page123");
        assertNull(fullProduct);

        // Test case 4 - test with null page
        fullProduct = product.getProduct("ASIN123", "Offer123", null);
        assertNull(fullProduct);

        // Add more test cases based on different scenarios and edge cases
    }
}