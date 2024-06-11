
package net.kencochrane.a4j.DAO;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public class ProductTest {

    private Product product;
    private final String asin = "TEST_ASIN";
    private final String offer = "TEST_OFFER";
    private final String page = "TEST_PAGE";

    @Before
    public void setUp(){
        product = new Product();
    }

    @Test
    public void testGetProduct_withValidParameters_shouldReturnFullProduct() {
        FullProduct fullProduct = product.getProduct(asin, offer, page);
        assertNotNull(fullProduct);
    }

    @Test
    public void testGetProduct_withInvalidAsin_shouldReturnNull() {
        FullProduct fullProduct = product.getProduct("", offer, page);
        assertNull(fullProduct);
    }

    @Test
    public void testGetProduct_withInvalidOffer_shouldReturnNull() {
        FullProduct fullProduct = product.getProduct(asin, "", page);
        assertNull(fullProduct);
    }

    @Test
    public void testGetProduct_withInvalidPage_shouldReturnNull() {
        FullProduct fullProduct = product.getProduct(asin, offer, "");
        assertNull(fullProduct);
    }

    @Test
    public void testGetProduct_withNonExistingFile_shouldReturnNull() {
        FullProduct fullProduct = product.getProduct("NON_EXISTING_ASIN", offer, page);
        assertNull(fullProduct);
    }

    @Test
    public void testGetProduct_withInvalidFile_shouldReturnNull() {
        FullProduct fullProduct = product.getProduct("INVALID_FILE_ASIN", offer, page);
        assertNull(fullProduct);
    }

    @Test
    public void testGetProduct_withNoDetails_shouldReturnFullProductWithNullDetails() {
        FullProduct fullProduct = product.getProduct("NO_DETAILS_ASIN", offer, page);
        assertNotNull(fullProduct);
        assertNull(fullProduct.getDetails());
    }

    @Test
    public void testGetProduct_withAccessories_shouldReturnFullProductWithAccessories() {
        FullProduct fullProduct = product.getProduct("ACCESSORIES_ASIN", offer, page);
        assertNotNull(fullProduct);
        assertNotNull(fullProduct.getAccessories());
        ArrayList<MiniProduct> accessories = fullProduct.getAccessories();
        assertFalse(accessories.isEmpty());
    }

    @Test
    public void testGetProduct_withSimilarItems_shouldReturnFullProductWithSimilarItems() {
        FullProduct fullProduct = product.getProduct("SIMILAR_ITEMS_ASIN", offer, page);
        assertNotNull(fullProduct);
        assertNotNull(fullProduct.getSimilarItems());
        ArrayList<MiniProduct> similarItems = fullProduct.getSimilarItems();
        assertFalse(similarItems.isEmpty());
    }

}
