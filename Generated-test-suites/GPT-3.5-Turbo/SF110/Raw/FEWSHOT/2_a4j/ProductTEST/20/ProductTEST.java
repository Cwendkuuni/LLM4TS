package net.kencochrane.a4j.DAO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ProductTest {
    
    @Test
    public void testGetProduct() {
        Product product = new Product();
        
        FullProduct result = product.getProduct("ASIN1", "offer1", "page1");
        assertNull(result);
        
        result = product.getProduct("ASIN2", "offer2", "page2");
        assertNull(result);
        
        result = product.getProduct("ASIN3", "offer3", "page3");
        assertNull(result);
    }
}