
package net.kencochrane.a4j.DAO;

import org.junit.Test;
import org.junit.Assert;

public class ProductTest {

    @Test
    public void testGetProduct_ReturnsFullProduct() {
        // Arrange
        Product product = new Product();
        String asin = "ASIN123";
        String offer = "Offer123";
        String page = "Page123";

        // Act
        FullProduct result = product.getProduct(asin, offer, page);

        // Assert
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetProduct_ReturnsNull_WhenFileNotFound() {
        // Arrange
        Product product = new Product();
        String asin = "ASIN123";
        String offer = "Offer123";
        String page = "Page123";

        // Act
        FullProduct result = product.getProduct(asin, offer, page);

        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void testGetProduct_ReturnsFullProduct_WithValidASIN() {
        // Arrange
        Product product = new Product();
        String asin = "ASIN123";
        String offer = "Offer123";
        String page = "Page123";

        // Act
        FullProduct result = product.getProduct(asin, offer, page);

        // Assert
        Assert.assertEquals(asin, result.getDetails().getAsin());
    }

    @Test
    public void testGetProduct_ReturnsFullProduct_WithValidOffer() {
        // Arrange
        Product product = new Product();
        String asin = "ASIN123";
        String offer = "Offer123";
        String page = "Page123";

        // Act
        FullProduct result = product.getProduct(asin, offer, page);

        // Assert
        Assert.assertEquals(offer, result.getDetails().getOffer());
    }

    @Test
    public void testGetProduct_ReturnsFullProduct_WithValidPage() {
        // Arrange
        Product product = new Product();
        String asin = "ASIN123";
        String offer = "Offer123";
        String page = "Page123";

        // Act
        FullProduct result = product.getProduct(asin, offer, page);

        // Assert
        Assert.assertEquals(page, result.getDetails().getPage("heavy"));
    }

    // Additional test cases for other methods of the Product class can be added here

}

