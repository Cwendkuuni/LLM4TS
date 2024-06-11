
package net.kencochrane.a4j.DAO;

import org.junit.Test;

import static org.junit.Assert.*;

public class ProductTest {

    @Test
    public void testGetProduct_nullInput_returnNull() {
        Product product = new Product();
        FullProduct result = product.getProduct(null, null, null);
        assertNull(result);
    }

    @Test
    public void testGetProduct_emptyInput_returnNull() {
        Product product = new Product();
        FullProduct result = product.getProduct("", "", "");
        assertNull(result);
    }

    @Test
    public void testGetProduct_validInput_returnFullProduct() {
        Product product = new Product();
        FullProduct result = product.getProduct("ASIN123", "Offer123", "Page123");

        assertNotNull(result);
        assertNotNull(result.getDetails());
    }

    @Test
    public void testGetProduct_invalidASIN_returnNull() {
        Product product = new Product();
        FullProduct result = product.getProduct("InvalidASIN", "Offer123", "Page123");

        assertNull(result);
    }

    @Test
    public void testGetProduct_invalidOffer_returnNull() {
        Product product = new Product();
        FullProduct result = product.getProduct("ASIN123", "InvalidOffer", "Page123");

        assertNull(result);
    }

    @Test
    public void testGetProduct_invalidPage_returnNull() {
        Product product = new Product();
        FullProduct result = product.getProduct("ASIN123", "Offer123", "InvalidPage");

        assertNull(result);
    }
}
