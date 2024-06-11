package net.kencochrane.a4j.DAO;
import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.beans.FullProduct;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProductTest {
    private Product product;

    @Test
    public void testGetProduct() {
        product = new Product();
        
        // Test typical use case
        FullProduct fullProduct = product.getProduct("1234567890", "offer", "page");
        assertNotNull(fullProduct);
        
        // Test edge case: when fileIn is null
        FullProduct nullProduct = product.getProduct("0000000000", "offer", "page");
        assertNull(nullProduct);
        
        // Test exception handling scenario
        FullProduct exceptionProduct = product.getProduct("exception", "offer", "page");
        assertEquals(null, exceptionProduct);
    }
}