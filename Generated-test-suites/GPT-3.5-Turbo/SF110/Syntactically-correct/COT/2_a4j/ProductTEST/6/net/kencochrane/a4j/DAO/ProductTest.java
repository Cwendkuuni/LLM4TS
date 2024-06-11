
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class ProductTest {
    
    @Test
    public void testGetProduct() {
        Product product = new Product();
        
        FullProduct result = product.getProduct("asin", "offer", "page");
        
        assertNull(result);
    }
}
