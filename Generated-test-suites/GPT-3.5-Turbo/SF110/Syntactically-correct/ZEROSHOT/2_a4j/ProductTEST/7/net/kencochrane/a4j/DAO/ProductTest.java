import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class ProductTest {

    @Test
    public void testGetProduct() {
        Product product = new Product();

        // Test case 1: fetchASINFile returns null
        assertNull(product.getProduct("ASIN1", "offer1", "page1"));

        // Test case 2: fetchASINFile returns valid file
        FullProduct fullProduct = product.getProduct("ASIN2", "offer2", "page2");
        assertNotNull(fullProduct);
        assertTrue(fullProduct.getDetails() instanceof ProductDetails);

        // Test case 3: fetchAccessories returns null
        ArrayList<MiniProduct> accessories = fullProduct.getAccessories();
        assertNotNull(accessories);
        assertEquals(0, accessories.size());

        // Test case 4: fetchAccessories returns valid file
        fullProduct = product.getProduct("ASIN3", "offer3", "page3");
        assertNotNull(fullProduct);
        assertTrue(fullProduct.getDetails() instanceof ProductDetails);
        accessories = fullProduct.getAccessories();
        assertNotNull(accessories);
        assertEquals(2, accessories.size());

        // Test case 5: SimilaritesSearch returns null
        ArrayList<MiniProduct> similarItems = fullProduct.getSimilarItems();
        assertNotNull(similarItems);
        assertEquals(0, similarItems.size());

        // Test case 6: SimilaritesSearch returns valid result
        fullProduct = product.getProduct("ASIN4", "offer4", "page4");
        assertNotNull(fullProduct);
        assertTrue(fullProduct.getDetails() instanceof ProductDetails);
        similarItems = fullProduct.getSimilarItems();
        assertNotNull(similarItems);
        assertEquals(3, similarItems.size());
    }
}