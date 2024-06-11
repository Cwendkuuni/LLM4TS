
package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ProductTest {

    private Product product;

    @Before
    public void setUp() {
        product = new Product();
    }

    @Test
    public void testGetProduct_returnsFullProduct() {
        FullProduct fullProduct = product.getProduct("ASIN123", "Offer123", "Page123");
        assertNotNull(fullProduct);
        assertEquals(FullProduct.class, fullProduct.getClass());
    }

    @Test
    public void testGetProduct_returnsNullWhenFileUtilReturnsNull() {
        FullProduct fullProduct = product.getProduct("ASIN123", "InvalidOffer", "Page123");
        assertNull(fullProduct);
    }

    @Test
    public void testGetProduct_returnsFullProductWithDetailsFromProductInfo() {
        FullProduct fullProduct = product.getProduct("ASIN123", "Offer123", "Page123");
        assertNotNull(fullProduct);
        assertNotNull(fullProduct.getDetails());
        assertEquals(ProductDetails.class, fullProduct.getDetails().getClass());
    }

    @Test
    public void testGetProduct_returnsFullProductWithAccessories() {
        FullProduct fullProduct = product.getProduct("ASIN123", "Offer123", "Page123");
        assertNotNull(fullProduct);
        assertNotNull(fullProduct.getAccessories());
        assertFalse(fullProduct.getAccessories().isEmpty());
    }

    @Test
    public void testGetProduct_returnsFullProductWithSimilarItems() {
        FullProduct fullProduct = product.getProduct("ASIN123", "Offer123", "Page123");
        assertNotNull(fullProduct);
        assertNotNull(fullProduct.getSimilarItems());
        assertFalse(fullProduct.getSimilarItems().isEmpty());
    }
}
