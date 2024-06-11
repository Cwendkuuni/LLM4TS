package net.kencochrane.a4j.DAO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.ProductDetails;

import org.junit.Before;
import org.junit.Test;

public class ProductTest {
    private Product product;
    
    @Before
    public void setUp() {
        product = new Product();
    }
    
    @Test
    public void testGetProduct() {
        String asin = "testAsin";
        String offer = "testOffer";
        String page = "testPage";
        
        FullProduct result = product.getProduct(asin, offer, page);
        assertNull(result);
    }
}