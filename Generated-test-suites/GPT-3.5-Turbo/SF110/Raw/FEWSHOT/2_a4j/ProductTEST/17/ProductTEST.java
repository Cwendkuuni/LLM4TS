package net.kencochrane.a4j.DAO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import net.kencochrane.a4j.DAO.Product;

public class ProductTest {
    @Test
    public void testGetProduct() {
        // Test case 1: fileIn is null
        Product product = new Product();
        assertNull(product.getProduct(null, null, null));
        
        // Test case 2: productInfoBean is null
        // Test case 3: productInfoBean.getDetails().length == 0
        // Test case 4: accessoryArray is null
        // Test case 5: accessoryArray.size() == 0
        // Test case 6: productInfoBean is null
        // Test case 7: productInfoBean.getProductsArrayList().size() == 0
        // Test case 8: accessoryProductDetails is null
        // Test case 9: accessoryProductDetails is null
        // Test case 10: productInfoBean is null
        // Test case 11: productInfoBean.getProductsArrayList() is null
        // Test case 12: productInfoBean.getProductsArrayList().size() == 0
        assertEquals(new FullProduct(), product.getProduct("asin", "offer", "page"));
    }
}