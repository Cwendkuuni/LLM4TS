package net.kencochrane.a4j.DAO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class ProductTest {
    @Test
    public void testGetProduct() {
        // Test case 1: Fetching ASIN File returns null
        Product product = new Product();
        FullProduct fullProduct = product.getProduct("asin1", "offer1", "page1");
        assertNull(fullProduct);

        // Test case 2: Fetching ASIN File successful with non-null productInfoBean
        // but empty productInfoBean.getDetails()
        fullProduct = product.getProduct("asin2", "offer2", "page2");
        assertNull(fullProduct.getDetails());

        // Test case 3: Fetching ASIN File successful with non-null productInfoBean
        // and non-empty productInfoBean.getDetails()
        fullProduct = product.getProduct("asin3", "offer3", "page3");
        assertEquals(expected, fullProduct.getDetails());

        // Test case 4: Fetching Accessories returns null
        fullProduct = product.getProduct("asin4", "offer4", "page4");
        assertEquals(expected, fullProduct.getDetails());
        assertEquals(0, fullProduct.getAccessories().size());

        // Test case 5: Fetching Accessories returns non-null productInfoBean
        // and non-empty productInfoBean.getProductsArrayList()
        fullProduct = product.getProduct("asin5", "offer5", "page5");
        assertEquals(expected, fullProduct.getDetails());
        assertEquals(expected, fullProduct.getAccessories().size());

        // Test case 6: SimilaritesSearch returns null
        fullProduct = product.getProduct("asin6", "offer6", "page6");
        assertEquals(expected, fullProduct.getDetails());
        assertEquals(0, fullProduct.getSimilarItems().size());

        // Test case 7: SimilaritesSearch returns non-null productInfoBean
        // and non-empty productInfoBean.getProductsArrayList()
        fullProduct = product.getProduct("asin7", "offer7", "page7");
        assertEquals(expected, fullProduct.getDetails());
        assertEquals(expected, fullProduct.getSimilarItems().size());

        // Test case 8: Exception occurs
        fullProduct = product.getProduct("asin8", "offer8", "page8");
        assertNull(fullProduct);
    }
}