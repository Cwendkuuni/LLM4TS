import static org.junit.Assert.*;
import org.junit.Test;

public class ProductTest {

    @Test
    public void testGetProduct(){
        //typical use case
        Product product = new Product();
        FullProduct fullProduct = product.getProduct("ASIN123", "offer123", "page123");
        assertNotNull(fullProduct);
        
        //edge case - testing for null return when fileIn is null
        FullProduct nullProduct = product.getProduct("ASIN456", "offer456", "page456");
        assertNull(nullProduct);
        
        //error scenario - testing for exception handling
        //unable to mock FileUtil's fetchASINFile method to return null
        
    }
}