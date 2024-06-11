import net.kencochrane.a4j.DAO.Product;
import com.wutka.jox.JOXBeanInputStream;
import net.kencochrane.a4j.beans.ProductInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class ProductTest {
    
    private Product product;

    @Before
    public void setUp() {
        product = new Product();
    }

    @Test
    public void testGetProduct_validInputs_returnFullProduct() {
        // Arrange
        String asin = "TESTASIN";
        String offer = "TESTOFFER";
        String page = "TESTPAGE";
        String expectedAsin = "TESTMINIPRODUCT";
        String expectedImageUrl = "https://testimage.com";
        String expectedManufacturer = "Test Manufacturer";
        String expectedName = "Test Product";
        double expectedPrice = 9.99;
        String expectedSimilarAsin = "TESTSIMILARASIN";
        String expectedSimilarImageUrl = "https://testsimilarimage.com";
        String expectedSimilarManufacturer = "Test Similar Manufacturer";
        String expectedSimilarName = "Test Similar Product";
        double expectedSimilarPrice = 19.99;
        
        // Act
        FullProduct actualProduct = product.getProduct(asin, offer, page);
        
        // Assert
        Assert.assertNotNull(actualProduct);
        Assert.assertEquals(expectedAsin, actualProduct.getDetails().getAsin());
        Assert.assertEquals(expectedImageUrl, actualProduct.getDetails().getImageUrlSmall());
        Assert.assertEquals(expectedManufacturer, actualProduct.getDetails().getManufacturer());
        Assert.assertEquals(expectedName, actualProduct.getDetails().getProductName());
        Assert.assertEquals(expectedPrice, actualProduct.getDetails().getOurPrice(), 0.001);
        Assert.assertTrue(actualProduct.getAccessories().getAccessoryArray().size() > 0);
        Assert.assertEquals(expectedAsin, actualProduct.getAccessories().getAccessoryArray().get(0).getAsin());
        Assert.assertEquals(expectedImageUrl, actualProduct.getAccessories().getAccessoryArray().get(0).getImageUrlSmall());
        Assert.assertEquals(expectedManufacturer, actualProduct.getAccessories().getAccessoryArray().get(0).getManufacturer());
        Assert.assertEquals(expectedName, actualProduct.getAccessories().getAccessoryArray().get(0).getProductName());
        Assert.assertEquals(expectedPrice, actualProduct.getAccessories().getAccessoryArray().get(0).getOurPrice(), 0.001);
        Assert.assertTrue(actualProduct.getSimilarItems().size() > 0);
        Assert.assertEquals(expectedSimilarAsin, actualProduct.getSimilarItems().get(0).getAsin());
        Assert.assertEquals(expectedSimilarImageUrl, actualProduct.getSimilarItems().get(0).getImageUrlSmall());
        Assert.assertEquals(expectedSimilarManufacturer, actualProduct.getSimilarItems().get(0).getManufacturer());
        Assert.assertEquals(expectedSimilarName, actualProduct.getSimilarItems().get(0).getProductName());
        Assert.assertEquals(expectedSimilarPrice, actualProduct.getSimilarItems().get(0).getOurPrice(), 0.001);
    }

    @Test
    public void testGetProduct_nullAsin_returnNull() {
        // Arrange
        String asin = null;
        String offer = "TESTOFFER";
        String page = "TESTPAGE";

        // Act
        FullProduct actualProduct = product.getProduct(asin, offer, page);

        // Assert
        Assert.assertNull(actualProduct);
    }

    @Test
    public void testGetProduct_nullOffer_returnNull() {
        // Arrange
        String asin = "TESTASIN";
        String offer = null;
        String page = "TESTPAGE";

        // Act
        FullProduct actualProduct = product.getProduct(asin, offer, page);

        // Assert
        Assert.assertNull(actualProduct);
    }

    @Test
    public void testGetProduct_nullPage_returnNull() {
        // Arrange
        String asin = "TESTASIN";
        String offer = "TESTOFFER";
        String page = null;

        // Act
        FullProduct actualProduct = product.getProduct(asin, offer, page);

        // Assert
        Assert.assertNull(actualProduct);
    }

    @Test
    public void testGetProduct_invalidAsin_returnNull() {
        // Arrange
        String asin = "INVALIDASIN";
        String offer = "TESTOFFER";
        String page = "TESTPAGE";

        // Act
        FullProduct actualProduct = product.getProduct(asin, offer, page);

        // Assert
        Assert.assertNull(actualProduct);
    }

    @Test
    public void testGetProduct_emptyAccessoryArray_returnFullProductWithNoAccessory() {
        // Arrange
        String asin = "EMPTYACCESSORYARRAY";
        String offer = "TESTOFFER";
        String page = "TESTPAGE";

        // Act
        FullProduct actualProduct = product.getProduct(asin, offer, page);

        // Assert
        Assert.assertNotNull(actualProduct);
        Assert.assertTrue(actualProduct.getAccessories().getAccessoryArray().isEmpty());
    }

    @Test
    public void testGetProduct_noSimilarItems_returnFullProductWithNoSimilarItems() {
        // Arrange
        String asin = "NOSIMILARITEMS";
        String offer = "TESTOFFER";
        String page = "TESTPAGE";

        // Act
        FullProduct actualProduct = product.getProduct(asin, offer, page);

        // Assert
        Assert.assertNotNull(actualProduct);
        Assert.assertTrue(actualProduct.getSimilarItems().isEmpty());
    }

    @Test
    public void testGetProduct_exceptionThrown_returnNull() {
        // Arrange
        String asin = "EXCEPTIONTHROWN";
        String offer = "TESTOFFER";
        String page = "TESTPAGE";

        // Mock
        InputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
        FileUtil fileUtilMock = Mockito.mock(FileUtil.class);
        JOXBeanInputStream joxBeanInputStreamMock = Mockito.mock(JOXBeanInputStream.class);
        ProductInfo productInfoMock = Mockito.mock(ProductInfo.class);

        Mockito.when(fileUtilMock.fetchASINFile(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(inputStream);
        Mockito.when(joxBeanInputStreamMock.readObject(Mockito.eq(ProductInfo.class)))
                .thenThrow(new Exception());

        // Replace with Mock
        product.setFileUtil(fileUtilMock);

        // Act
        FullProduct actualProduct = product.getProduct(asin, offer, page);

        // Assert
        Assert.assertNull(actualProduct);
    }
}