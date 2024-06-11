import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProductTest {

    @Test
    public void testGetProduct_ReturnsNull_WhenFileInputStreamIsNull() {
        // Arrange
        String asin = "ASIN";
        String offer = "Offer";
        String page = "Page";
        FileUtil fileUtil = Mockito.mock(FileUtil.class);
        Mockito.when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(null);

        Product product = new Product();
        
        // Act
        FullProduct result = product.getProduct(asin, offer, page);

        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void testGetProduct_ReturnsFullProduct_WhenFileInputStreamIsNotNull() throws IOException {
        // Arrange
        String asin = "ASIN";
        String offer = "Offer";
        String page = "Page";
        FileUtil fileUtil = Mockito.mock(FileUtil.class);
        FileInputStream fileIn = Mockito.mock(FileInputStream.class);
        JOXBeanInputStream joxIn = Mockito.mock(JOXBeanInputStream.class);
        ProductInfo productInfoBean = Mockito.mock(ProductInfo.class);
        ProductDetails productDetails = new ProductDetails();

        Mockito.when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(fileIn);
        Mockito.when(fileIn == null).thenReturn(false);
        Mockito.when(new JOXBeanInputStream((InputStream) fileIn)).thenReturn(joxIn);
        Mockito.when(joxIn.readObject((Class) ProductInfo.class)).thenReturn(productInfoBean);
        Mockito.when(productInfoBean.getDetails().length).thenReturn(1);
        Mockito.when(productInfoBean.getDetails()[0]).thenReturn(productDetails);

        Product product = new Product();
        
        // Act
        FullProduct result = product.getProduct(asin, offer, page);

        // Assert
        Assert.assertNotNull(result);
    }

    // Additional test cases for other methods within the class...
    
    
}