package net.kencochrane.a4j.DAO;

import org.junit.*;
import static org.junit.Assert.*;

public class ProductTest {

    private Product product;

    @Before
    public void setUp() {
        product = new Product();
    }

    @Test
    public void testGetProduct() {
        // Test case 1: when fileIn is null
        FullProduct result1 = product.getProduct(null, null, null);
        assertNull(result1);

        // Test case 2: when productInfoBean is null
        FullProduct result2 = product.getProduct("asin1", "offer1", "page1");
        assertNotNull(result2);
        assertEquals(0, result2.getAccessories().size());
        assertEquals(0, result2.getSimilarItems().size());

        // Test case 3: when productInfoBean.getDetails() is empty
        FullProduct result3 = product.getProduct("asin2", "offer2", "page2");
        assertNotNull(result3);
        assertEquals(0, result3.getAccessories().size());
        assertEquals(0, result3.getSimilarItems().size());

        // Test case 4: when accessoryArray is empty
        FullProduct result4 = product.getProduct("asin3", "offer3", "page3");
        assertNotNull(result4);
        assertEquals(0, result4.getAccessories().size());
        assertEquals(0, result4.getSimilarItems().size());

        // Test case 5: when fileIn is not null
        // and productInfoBean and productInfoBean.getProductsArrayList() are not null
        FullProduct result5 = product.getProduct("asin4", "offer4", "page4");
        assertNotNull(result5);
        assertEquals(1, result5.getAccessories().size());
    }

}