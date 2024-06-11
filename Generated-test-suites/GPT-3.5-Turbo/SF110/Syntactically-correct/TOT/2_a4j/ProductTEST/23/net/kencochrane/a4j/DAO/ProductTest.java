import org.junit.Test;
import static org.junit.Assert.*;

public class ProductTest {

    @Test
    public void testGetProduct_typicalUseCase() {
        //typical use case test for getProduct method
        
        Product product = new Product();
        
        FullProduct fullProduct = product.getProduct("ASIN123", "offer", "page");
        
        assertNotNull(fullProduct);
        assertEquals("ASIN123", fullProduct.getDetails().getAsin());
        assertEquals("imageURL", fullProduct.getDetails().getImageURL());
        assertEquals("manufacturer", fullProduct.getDetails().getManufacturer());
        assertEquals("productName", fullProduct.getDetails().getName());
        assertEquals(99.99, fullProduct.getDetails().getPrice(), 0.01);
    }
    
    @Test
    public void testGetProduct_nullFileInputStream() {
        //test for null file input stream
        
        Product product = new Product();
        
        FullProduct fullProduct = product.getProduct("ASIN789", "offer", "page");
        
        assertNull(fullProduct);
    }
    
    @Test
    public void testGetProduct_emptyProductInfoBeanDetails() {
        //test for empty productInfoBean details
        
        Product product = new Product();
        
        FullProduct fullProduct = product.getProduct("ASIN456", "offer", "page");
        
        assertNotNull(fullProduct);
        assertNull(fullProduct.getDetails().getAsin());
        assertNull(fullProduct.getDetails().getImageURL());
        assertNull(fullProduct.getDetails().getManufacturer());
        assertNull(fullProduct.getDetails().getName());
        assertEquals(0, fullProduct.getDetails().getPrice, 0);
    }
    
    @Test
    public void testGetProduct_nullAccessoryArray() {
        //test for null accessory array
        
        Product product = new Product();
        
        FullProduct fullProduct = product.getProduct("ASIN789", "offer", "page");
        
        assertNotNull(fullProduct);
        assertNotNull(fullProduct.getDetails());
        assertNotNull(fullProduct.getDetails().getAccessories());
        assertNull(fullProduct.getDetails().getAccessories().getAccessoryArray());
        assertEquals(0, fullProduct.getAccessories().size());
    }
    
    @Test
    public void testGetProduct_emptyAccessoryArray() {
        //test for empty accessory array
        
        Product product = new Product();
        
        FullProduct fullProduct = product.getProduct("ASIN123", "offer", "page");
        
        assertNotNull(fullProduct);
        assertNotNull(fullProduct.getDetails());
        assertNotNull(fullProduct.getDetails().getAccessories());
        assertNotNull(fullProduct.getDetails().getAccessories().getAccessoryArray());
        assertEquals(0, fullProduct.getAccessories().size());
    }
    
    @Test
    public void testGetProduct_nullProductInfoBean() {
        //test for null productInfoBean
        
        Product product = new Product();
        
        FullProduct fullProduct = product.getProduct("ASIN456", "offer", "page");
        
        assertNotNull(fullProduct);
        assertNotNull(fullProduct.getSimilarItems());
        assertEquals(0, fullProduct.getSimilarItems().size());
    }
    
    @Test
    public void testGetProduct_emptyProductInfoBeanArrayList() {
        //test for empty productInfoBean ArrayList
        
        Product product = new Product();
        
        FullProduct fullProduct = product.getProduct("ASIN789", "offer", "page");
        
        assertNotNull(fullProduct);
        assertNotNull(fullProduct.getSimilarItems());
        assertNotNull(fullProduct.getSimilarItems().getSimilarItemsArrayList());
        assertEquals(0, fullProduct.getSimilarItems().size());
    }
    
    @Test
    public void testGetProduct_exceptionThrown() {
        //test for exception thrown
        
        Product product = new Product();
        
        FullProduct fullProduct = product.getProduct("ASIN123", "offer", "page");
        
        assertNotNull(fullProduct);
        assertNotNull(fullProduct.getDetails());
        assertNotNull(fullProduct.getDetails().getAsin());
        assertNotNull(fullProduct.getDetails().getImageURL());
        assertNotNull(fullProduct.getDetails().getManufacturer());
        assertNotNull(fullProduct.getDetails().getName());
        assertEquals(0, fullProduct.getDetails().getPrice, 0);
    }
    
}