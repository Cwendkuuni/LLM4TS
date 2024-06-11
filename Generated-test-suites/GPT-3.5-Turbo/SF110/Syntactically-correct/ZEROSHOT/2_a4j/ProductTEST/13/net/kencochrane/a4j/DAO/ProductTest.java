package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;
import org.junit.Test;

public class ProductTest {

    @Test
    public void testGetProduct() {
        // Test case 1: when fileIn is null, the method should return null
        // Setup
        Product product = new Product();
        String asin = "";
        String offer = "";
        String page = "";

        // Execute
        FullProduct result = product.getProduct(asin, offer, page);

        // Verify
        assertNull(result);

        // Test case 2: when productInfoBean is not null and productInfoBean.getDetails().length > 0, the method should return a FullProduct object
        // Setup
        asin = "ASIN";
        offer = "Offer";
        page = "Page";

        // Execute
        result = product.getProduct(asin, offer, page);

        // Verify
        assertNotNull(result);
        assertNotNull(result.getDetails());

        // Test case 3: when accessoryArray is not null and accessoryArray.size() > 0, the method should return a FullProduct object with accessories
        // Setup
        asin = "ASIN";
        offer = "Offer";
        page = "Page";

        // Execute
        result = product.getProduct(asin, offer, page);

        // Verify
        assertNotNull(result);
        assertNotNull(result.getAccessories());

        // Test case 4: when productInfoBean is not null, productInfoBean.getProductsArrayList() is not null, and productInfoBean.getProductsArrayList().size() > 0, the method should return a FullProduct object with similar items
        // Setup
        asin = "ASIN";
        offer = "Offer";
        page = "Page";

        // Execute
        result = product.getProduct(asin, offer, page);

        // Verify
        assertNotNull(result);
        assertNotNull(result.getSimilarItems());
    }
}