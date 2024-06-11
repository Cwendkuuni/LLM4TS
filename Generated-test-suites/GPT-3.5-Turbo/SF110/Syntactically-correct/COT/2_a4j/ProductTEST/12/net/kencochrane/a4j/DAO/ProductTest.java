
import org.junit.Test;

public class ProductTest {
    @Test
    public void testGetProduct() {
        // Basic test case
        Product product = new Product();
        String asin = "123456";
        String offer = "offer";
        String page = "1";
        FullProduct result = product.getProduct(asin, offer, page);
        // Assert statements
        // Add assertions here
    }
}
