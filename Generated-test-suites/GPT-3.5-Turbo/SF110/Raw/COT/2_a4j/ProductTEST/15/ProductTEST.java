
import org.junit.Test;
import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.file.FileUtil;

import static org.junit.Assert.*;

public class ProductTest {

    private Product product = new Product();

    @Test
    public void testGetProduct() {
        // Test basic functionality
        String asin = "";
        String offer = "";
        String page = "";
        assertNotNull(product.getProduct(asin, offer, page));
    }
}
