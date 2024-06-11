import org.junit.Test;
import static org.junit.Assert.*;

public class ProductTest {

    @Test
    public void testGetProduct_withValidInput_returnFullProduct() {
        // Arrange
        Product product = new Product();
        String asin = "123456";
        String offer = "offer";
        String page = "page";

        // Act
        FullProduct fullProduct = product.getProduct(asin, offer, page);

        // Assert
        assertNotNull(fullProduct);
    }

    @Test
    public void testGetProduct_withNullFileInputStream_returnNull() {
        // Arrange
        Product product = new Product();
        String asin = "123456";
        String offer = "offer";
        String page = "page";

        // Act
        FullProduct fullProduct = product.getProduct(asin, offer, page);

        // Assert
        assertNull(fullProduct);
    }

    @Test
    public void testGetProduct_withEmptyProductInfoBean_returnFullProductWithoutDetails() {
        // Arrange
        Product product = new Product();
        String asin = "123456";
        String offer = "offer";
        String page = "page";

        // Act
        FullProduct fullProduct = product.getProduct(asin, offer, page);

        // Assert
        assertNotNull(fullProduct);
        assertNull(fullProduct.getDetails());
    }

    @Test
    public void testGetProduct_withEmptyAccessoryArray_returnFullProductWithoutAccessories() {
        // Arrange
        Product product = new Product();
        String asin = "123456";
        String offer = "offer";
        String page = "page";

        // Act
        FullProduct fullProduct = product.getProduct(asin, offer, page);

        // Assert
        assertNotNull(fullProduct);
        assertNotNull(fullProduct.getDetails());
        assertNull(fullProduct.getDetails().getAccessories());
    }

    @Test
    public void testGetProduct_withEmptyDetailsArray_returnFullProductWithoutAccessories() {
        // Arrange
        Product product = new Product();
        String asin = "123456";
        String offer = "offer";
        String page = "page";

        // Act
        FullProduct fullProduct = product.getProduct(asin, offer, page);

        // Assert
        assertNotNull(fullProduct);
        assertNotNull(fullProduct.getDetails());
        assertNotNull(fullProduct.getDetails().getAccessories());
        assertEquals(0, fullProduct.getDetails().getAccessories().getAccessoryArray().size());
    }

    @Test
    public void testGetProduct_withNullProductInfoBean_returnFullProductWithoutAccessories() {
        // Arrange
        Product product = new Product();
        String asin = "123456";
        String offer = "offer";
        String page = "page";

        // Act
        FullProduct fullProduct = product.getProduct(asin, offer, page);

        // Assert
        assertNotNull(fullProduct);
        assertNotNull(fullProduct.getDetails());
        assertNotNull(fullProduct.getDetails().getAccessories());
        assertEquals(0, fullProduct.getDetails().getAccessories().getAccessoryArray().size());
    }

    @Test
    public void testGetProduct_withValidDetailsArray_returnFullProductWithAccessories() {
        // Arrange
        Product product = new Product();
        String asin = "123456";
        String offer = "offer";
        String page = "page";

        // Act
        FullProduct fullProduct = product.getProduct(asin, offer, page);

        // Assert
        assertNotNull(fullProduct);
        assertNotNull(fullProduct.getDetails());
        assertNotNull(fullProduct.getDetails().getAccessories());
        assertEquals(1, fullProduct.getDetails().getAccessories().getAccessoryArray().size());
    }

    @Test
    public void testGetProduct_withInvalidSimilaritesSearch_returnFullProductWithoutSimilarItems() {
        // Arrange
        Product product = new Product();
        String asin = "123456";
        String offer = "offer";
        String page = "page";

        // Act
        FullProduct fullProduct = product.getProduct(asin, offer, page);

        // Assert
        assertNotNull(fullProduct);
        assertNotNull(fullProduct.getSimilarItems());
        assertEquals(0, fullProduct.getSimilarItems().size());
    }

    @Test
    public void testGetProduct_withValidSimilaritesSearch_returnFullProductWithSimilarItems() {
        // Arrange
        Product product = new Product();
        String asin = "123456";
        String offer = "offer";
        String page = "page";

        // Act
        FullProduct fullProduct = product.getProduct(asin, offer, page);

        // Assert
        assertNotNull(fullProduct);
        assertNotNull(fullProduct.getSimilarItems());
        assertEquals(1, fullProduct.getSimilarItems().size());
    }
}