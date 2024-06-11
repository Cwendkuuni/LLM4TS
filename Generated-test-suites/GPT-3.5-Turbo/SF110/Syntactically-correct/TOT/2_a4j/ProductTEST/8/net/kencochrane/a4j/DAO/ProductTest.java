import net.kencochrane.a4j.DAO.Product;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProductTest {

    private Product product;

    @Before
    public void setUp() {
        product = new Product();
    }

    @Test
    public void testGetProduct_withValidArguments_returnsNonNullFullProduct() {
        String asin = "B000123456";
        String offer = "10% off";
        String page = "1";
        
        FullProduct result = product.getProduct(asin, offer, page);
        
        assertNotNull(result);
    }

    @Test
    public void testGetProduct_withNullAsin_returnsNullFullProduct() {
        String asin = null;
        String offer = "10% off";
        String page = "1";
        
        FullProduct result = product.getProduct(asin, offer, page);
        
        assertNull(result);
    }

    @Test
    public void testGetProduct_withInvalidAsin_returnsNullFullProduct() {
        String asin = "INVALID";
        String offer = "10% off";
        String page = "1";
        
        FullProduct result = product.getProduct(asin, offer, page);
        
        assertNull(result);
    }

    @Test
    public void testGetProduct_withNullOffer_returnsNullFullProduct() {
        String asin = "B000123456";
        String offer = null;
        String page = "1";
        
        FullProduct result = product.getProduct(asin, offer, page);
        
        assertNull(result);
    }

    @Test
    public void testGetProduct_withNullPage_returnsNullFullProduct() {
        String asin = "B000123456";
        String offer = "10% off";
        String page = null;
        
        FullProduct result = product.getProduct(asin, offer, page);
        
        assertNull(result);
    }

    @Test
    public void testGetProduct_withInvalidAsinAndValidOfferAndPage_returnsNullFullProduct() {
        String asin = "INVALID";
        String offer = "10% off";
        String page = "1";
        
        FullProduct result = product.getProduct(asin, offer, page);
        
        assertNull(result);
    }

    // More test cases can be added here for other methods in the class

}