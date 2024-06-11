package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;
import org.junit.Test;

public class ProductTest {

    @Test
    public void testGetProduct() {
        // Test case 1
        String asin1 = "ASIN1";
        String offer1 = "Offer1";
        String page1 = "Page1";
        FullProduct expected1 = null;
        FullProduct actual1 = Product.getProduct(asin1, offer1, page1);
        assertEquals(expected1, actual1);

        // Test case 2
        String asin2 = "ASIN2";
        String offer2 = "Offer2";
        String page2 = "Page2";
        FullProduct expected2 = new FullProduct();
        ProductDetails productDetails2 = new ProductDetails();
        Accessories accessories2 = new Accessories();
        ArrayList<MiniProduct> accessoryArray2 = new ArrayList<>();
        ArrayList<ProductDetails> detailsArray2 = new ArrayList<>();
        MiniProduct miniProduct2 = new MiniProduct();
        ProductInfo productInfo2 = new ProductInfo();
        productInfo2.setDetails(new ProductDetails[] {productDetails2});
        productDetails2.setAccessories(accessories2);
        accessories2.setAccessoryArray(accessoryArray2);
        detailsArray2.add(new ProductDetails());
        productInfo2.setProductsArrayList(detailsArray2);
        productInfo2.setProductsArrayList(detailsArray2);
        productInfo2.setProductsArrayList(detailsArray2);
        FullProduct actual2 = Product.getProduct(asin2, offer2, page2);
        assertEquals(expected2, actual2);
    }

}