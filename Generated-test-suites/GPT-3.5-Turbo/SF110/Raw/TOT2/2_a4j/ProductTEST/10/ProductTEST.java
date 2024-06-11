import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.beans.FullProduct;

import org.junit.Test;

import static org.junit.Assert.*;

public class ProductTest {

    @Test
    public void testGetProduct() {
        Product product = new Product();
        
        // Typical use case
        FullProduct fullProduct = product.getProduct("ASIN123", "offer123", "page123");
        assertNotNull(fullProduct);
        
        // Edge cases
        // Test when the fileIn is null
        fullProduct = product.getProduct("invalidASIN", "offer123", "page123");
        assertNull(fullProduct);
        
        // Test when the productInfoBean is null
        fullProduct = product.getProduct("ASIN456", "offer123", "page123");
        assertNotNull(fullProduct);
        assertEquals(0, fullProduct.getAccessories().size());
        
        // Test when accessories.getAccessoryArray() is null
        fullProduct = product.getProduct("ASIN789", "offer123", "page123");
        assertNotNull(fullProduct);
        assertEquals(0, fullProduct.getAccessories().size());

        // Error scenarios
        // Test when an exception is thrown
        fullProduct = product.getProduct("errorASIN", "offer123", "page123");
        assertNull(fullProduct);
    }
}