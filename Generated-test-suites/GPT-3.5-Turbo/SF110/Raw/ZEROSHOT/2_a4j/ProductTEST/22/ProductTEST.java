import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;

public class ProductTest {

    @Test
    public void testGetProduct_NullFile_ReturnsNull() {
        Product product = new Product();
        FullProduct result = product.getProduct(null, null, null);
        assertNull(result);
    }

    @Test
    public void testGetProduct_ValidInputs_ReturnsFullProduct() {
        Product product = new Product();
        
        FullProduct result = product.getProduct("ASIN", "Offer", "Page");
        assertNotNull(result);
    }

    @Test
    public void testGetProduct_EmptyDetailsArray_NoAccessoriesAdded() {
        Product product = new Product();
        FullProduct result = product.getProduct("ASIN", "Offer", "Page");

        ArrayList<MiniProduct> accessories = result.getAccessories();
        assertEquals(0, accessories.size());
    }

    @Test
    public void testGetProduct_EmptySimilarItemsArray_NoSimilarItemsAdded() {
        Product product = new Product();
        FullProduct result = product.getProduct("ASIN", "Offer", "Page");

        ArrayList<MiniProduct> similarItems = result.getSimilarItems();
        assertEquals(0, similarItems.size());
    }

    // Add more individual tests for each method in the Product class

}