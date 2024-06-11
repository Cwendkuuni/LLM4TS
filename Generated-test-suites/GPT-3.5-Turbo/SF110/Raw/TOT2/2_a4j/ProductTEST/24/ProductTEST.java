import static org.junit.Assert.*;
import org.junit.Test;

public class ProductTest {

    @Test
    public void testGetProduct() {
        Product product = new Product();
        FullProduct fullProduct = product.getProduct("ASIN123", "offer", "page");

        assertNotNull(fullProduct);
        // Add more specific assertions here based on the expected behavior of getProduct method
    }
}