import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class ProductTest {
    
    @Test
    public void testGetProduct() {
        // Typical use case
        Product product = new Product();
        FullProduct fullProduct = product.getProduct("ASIN123", "offer123", "page123");
        
        assertNotNull(fullProduct);
        
        // Edge cases
        // Test with invalid ASIN
        fullProduct = product.getProduct(null, "offer123", "page123");
        assertNull(fullProduct);
        
        // Test with empty offer
        fullProduct = product.getProduct("ASIN123", "", "page123");
        assertNotNull(fullProduct);
        
        // Test with empty page
        fullProduct = product.getProduct("ASIN123", "offer123", "");
        assertNotNull(fullProduct);
        
        // Error scenarios
        // Test when fileUtil.fetchASINFile returns null
        FileUtil fileUtilMock = mock(FileUtil.class);
        when(fileUtilMock.fetchASINFile(anyString(), anyString(), anyString(), anyString())).thenReturn(null);
        
        Product productSpy = spy(new Product());
        doReturn(fileUtilMock).when(productSpy).getFileUtil();
        
        fullProduct = productSpy.getProduct("ASIN123", "offer123", "page123");
        assertNull(fullProduct);
        
    }
}