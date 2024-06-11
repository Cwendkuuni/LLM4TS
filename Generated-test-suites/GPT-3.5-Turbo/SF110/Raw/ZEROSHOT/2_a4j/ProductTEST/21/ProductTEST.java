import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.*;

public class ProductTest {

    @Mock
    private FileUtil fileUtil;

    private Product product;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        product = new Product();
    }

    @Test
    public void testGetProductReturnsNullWhenFileNotAvailable() {
        String asin = "test_asin";
        String offer = "test_offer";
        String page = "test_page";
        FileInputStream fileIn = null;
        
        when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(fileIn);

        FullProduct result = product.getProduct(asin, offer, page);

        assertNull(result);
    }

    @Test
    public void testGetProductReturnsFullProductWhenFileAvailable() throws Exception {
        String asin = "test_asin";
        String offer = "test_offer";
        String page = "test_page";
        FileInputStream fileIn = new FileInputStream("test_file");

        when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(fileIn);
        when(fileUtil.fetchAccessories(eq(asin), any())).thenReturn(fileIn);
        when(search.SimilaritesSearch(asin, "1")).thenReturn(new ProductInfo());

        FullProduct result = product.getProduct(asin, offer, page);

        assertNotNull(result);
    }
}