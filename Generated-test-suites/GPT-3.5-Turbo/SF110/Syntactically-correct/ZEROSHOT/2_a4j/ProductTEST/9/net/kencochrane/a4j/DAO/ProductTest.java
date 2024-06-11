import static org.junit.Assert.*;
import org.junit.Test;
import net.kencochrane.a4j.beans.*;
import net.kencochrane.a4j.DAO.*;
import java.util.ArrayList;

public class ProductTest {
   
    @Test
    public void testGetProduct() {
        Product product = new Product();
        FullProduct fullProduct = product.getProduct("ASIN", "Offer", "Page");
        
        assertNotNull("FullProduct should not be null", fullProduct);
    
        ProductDetails productDetails = fullProduct.getDetails();
        assertNotNull("ProductDetails should not be null", productDetails);
    
        Accessories accessories = productDetails.getAccessories();
        assertNotNull("Accessories should not be null", accessories);
        
        ArrayList<MiniProduct> accessoryArray = accessories.getAccessoryArray();
        assertNotNull("AccessoryArray should not be null", accessoryArray);
        
        for (MiniProduct accessory : accessoryArray) {
            assertNotNull("Accessory should not be null", accessory);
            assertNotNull("Accessory ASIN should not be null", accessory.getAsin());
            assertNotNull("Accessory ImageURL should not be null", accessory.getImageURL());
            assertNotNull("Accessory Manufacturer should not be null", accessory.getManufacturer());
            assertNotNull("Accessory Name should not be null", accessory.getName());
            assertNotNull("Accessory Price should not be null", accessory.getPrice());
        }
        
        ArrayList<MiniProduct> similarItems = fullProduct.getSimilarItems();
        assertNotNull("SimilarItems should not be null", similarItems);
        
        for (MiniProduct similarItem : similarItems) {
            assertNotNull("SimilarItem should not be null", similarItem);
            assertNotNull("SimilarItem ASIN should not be null", similarItem.getAsin());
            assertNotNull("SimilarItem ImageURL should not be null", similarItem.getImageURL());
            assertNotNull("SimilarItem Manufacturer should not be null", similarItem.getManufacturer());
            assertNotNull("SimilarItem Name should not be null", similarItem.getName());
            assertNotNull("SimilarItem Price should not be null", similarItem.getPrice());
        }
    }
}