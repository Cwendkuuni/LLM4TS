import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;

public class ProductTest {

    @Test
    public void testGetProduct() {
        Product product = new Product();
        
        // Typical use case
        FullProduct fullProduct = product.getProduct("ASIN123", "Offer123", "Page123");
        assertNotNull(fullProduct);
        
        // Edge case: fileIn is null
        FullProduct fullProductNull = product.getProduct("ASIN456", "Offer456", "Page456");
        assertNull(fullProductNull);
        
        // Error scenario: Exception in fetching file
        FullProduct fullProductException = product.getProduct("ASIN789", "Offer789", "Page789");
        assertNull(fullProductException);
    }
}