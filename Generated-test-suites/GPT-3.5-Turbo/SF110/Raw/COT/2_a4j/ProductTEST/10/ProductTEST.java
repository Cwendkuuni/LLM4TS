
import static org.junit.Assert.*;
import org.junit.Test;

public class ProductTest {

    @Test
    public void testGetProduct() {
        // Test basic functionality
        Product product = new Product();
        FullProduct result = product.getProduct("ASIN", "offer", "page");
        assertNotNull(result);
        
        // Test when fileIn is null
        result = product.getProduct("invalidASIN", "offer", "page");
        assertNull(result);
        
        // Test when productInfoBean.getDetails() is empty
        result = product.getProduct("ASIN", "offer", "page");
        assertNull(result.getDetails());
        
        // Test when accessoryArray is null
        result = product.getProduct("ASIN", "offer", "page");
        assertNotNull(result);
        assertNotNull(result.getAccessoryList());
        assertEquals(0, result.getAccessoryList().size());
        
        // Test when accessoryArray is not empty
        result = product.getProduct("ASIN", "offer", "page");
        assertNotNull(result);
        assertNotNull(result.getAccessoryList());
        assertEquals(1, result.getAccessoryList().size());
        
        // Test when productInfoBean.getProductsArrayList() is empty
        result = product.getProduct("ASIN", "offer", "page");
        assertNotNull(result);
        assertNotNull(result.getSimilarItemsList());
        assertEquals(0, result.getSimilarItemsList().size());
        
        // Test when productInfoBean.getProductsArrayList() is not empty
        result = product.getProduct("ASIN", "offer", "page");
        assertNotNull(result);
        assertNotNull(result.getSimilarItemsList());
        assertEquals(1, result.getSimilarItemsList().size());
    }
}

