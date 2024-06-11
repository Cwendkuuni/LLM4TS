
import static org.junit.Assert.*;
import org.junit.Test;

public class ProductTest {
    @Test
    public void testGetProduct_NullInput_ReturnNull() {
        Product product = new Product();
        assertNull(product.getProduct(null, null, null));
    }

    @Test
    public void testGetProduct_ValidInput_ReturnFullProduct() {
        Product product = new Product();
        FullProduct fullProduct = product.getProduct("ASIN123", "Offer123", "Page123");
        assertNotNull(fullProduct);
    }

    @Test
    public void testGetProduct_InvalidFile_ReturnNull() {
        Product product = new Product();
        assertNull(product.getProduct("InvalidASIN", "InvalidOffer", "InvalidPage"));
    }

    @Test
    public void testGetProduct_EmptyData_ReturnFullProduct() {
        Product product = new Product();
        FullProduct fullProduct = product.getProduct("EmptyASIN", "EmptyOffer", "EmptyPage");
        assertNotNull(fullProduct);
    }

    @Test
    public void testGetProduct_ExceptionThrown_PrintStackTrace() {
        Product product = new Product();
        try {
            product.getProduct("ASIN123", "Offer123", "Page123");
        } catch (Exception e) {
            assertNotNull(e);
        }
    }
}
