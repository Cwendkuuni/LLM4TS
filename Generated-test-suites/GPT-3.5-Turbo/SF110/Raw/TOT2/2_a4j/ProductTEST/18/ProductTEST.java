import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.FileInputStream;
import java.util.ArrayList;

public class ProductTest {

    @Mock
    private FileUtil fileUtilMock;

    @Test
    public void testGetProduct_ReturnsFullProduct() {
        Product product = new Product();
        String asin = "123456";
        String offer = "10%";
        String page = "1";

        FullProduct fullProduct = product.getProduct(asin, offer, page);

        assertNotNull(fullProduct);
    }

    @Test
    public void testGetProduct_ReturnsNullIfFileInIsNull() {
        Product product = new Product();
        String asin = "123456";
        String offer = "10%";
        String page = "1";

        when(fileUtilMock.fetchASINFile(eq(asin), eq("heavy"), eq(offer), eq(page))).thenReturn(null);

        FullProduct fullProduct = product.getProduct(asin, offer, page);

        assertNull(fullProduct);
    }

    @Test
    public void testGetProduct_ReturnsFullProductWithAccessoriesAndSimilarItems() {
        Product product = new Product();
        String asin = "123456";
        String offer = "10%";
        String page = "1";

        // Mocking fileUtil method fetchASINFile
        FileInputStream fileInMock = new FileInputStream("mockFile");
        when(fileUtilMock.fetchASINFile(eq(asin), eq("heavy"), eq(offer), eq(page))).thenReturn(fileInMock);

        // Mocking joxIn object creation
        JOXBeanInputStream joxInMock = new JOXBeanInputStream(fileInMock);
        whenNew(JOXBeanInputStream.class).withArguments(any(InputStream.class)).thenReturn(joxInMock);

        // Mocking productInfoBean creation and methods
        ProductInfo productInfoMock = new ProductInfo();
        productInfoMock.setDetails(new ProductDetails[]{new ProductDetails()});
        when(joxInMock.readObject(eq(ProductInfo.class))).thenReturn(productInfoMock);

        // Test getProduct method
        FullProduct fullProduct = product.getProduct(asin, offer, page);

        // Assertions
        assertNotNull(fullProduct.getDetails());
        assertFalse(fullProduct.getAccessories().isEmpty());
        assertFalse(fullProduct.getSimilarItems().isEmpty());
        verify(fileUtilMock, times(1)).fetchAccessories(eq(asin), any(ArrayList.class));
        // Add more assertions if needed
    }

    // Add more test cases for other methods in the Product class as needed

}