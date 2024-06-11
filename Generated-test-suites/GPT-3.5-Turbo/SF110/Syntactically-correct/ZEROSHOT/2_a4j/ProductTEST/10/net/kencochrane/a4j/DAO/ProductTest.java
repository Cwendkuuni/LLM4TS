package net.kencochrane.a4j.DAO;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

public class ProductTest {

    @Test
    public void testGetProduct_withValidInput_returnsFullProductObject() {
        // Arrange
        String asin = "ASIN123";
        String offer = "offer123";
        String page = "page123";
        Product product = new Product();

        // Act
        FullProduct result = product.getProduct(asin, offer, page);

        // Assert
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetProduct_withInvalidInput_returnsNull() {
        // Arrange
        String asin = "";
        String offer = "";
        String page = "";
        Product product = new Product();

        // Act
        FullProduct result = product.getProduct(asin, offer, page);

        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void testGetProduct_fetchASINFileCalled() {
        // Arrange
        String asin = "ASIN123";
        String offer = "offer123";
        String page = "page123";
        Product product = Mockito.spy(new Product());
        FileUtil mockFileUtil = Mockito.mock(FileUtil.class);
        Mockito.doReturn(mockFileUtil).when(product).getFileUtil();

        // Act
        product.getProduct(asin, offer, page);

        // Assert
        Mockito.verify(mockFileUtil, Mockito.times(1)).fetchASINFile(asin, "heavy", offer, page);
    }

    @Test
    public void testGetProduct_fetchAccessoriesCalled() {
        // Arrange
        String asin = "ASIN123";
        String offer = "offer123";
        String page = "page123";
        Product product = Mockito.spy(new Product());
        FileUtil mockFileUtil = Mockito.mock(FileUtil.class);
        ArrayList<String> accessoryArray = new ArrayList<>();
        accessoryArray.add("accessory1");
        accessoryArray.add("accessory2");
        Mockito.when(mockFileUtil.fetchAccessories(asin, accessoryArray)).thenReturn(null);
        ProductDetails mockProductDetails = Mockito.mock(ProductDetails.class);
        Accessories mockAccessories = Mockito.mock(Accessories.class);
        Mockito.when(mockProductDetails.getAccessories()).thenReturn(mockAccessories);
        Mockito.when(mockAccessories.getAccessoryArray()).thenReturn(accessoryArray);
        FullProduct mockFullProduct = Mockito.mock(FullProduct.class);
        Mockito.when(product.getFullProduct()).thenReturn(mockFullProduct);
        Mockito.when(mockFullProduct.getDetails()).thenReturn(mockProductDetails);

        // Act
        product.getProduct(asin, offer, page);

        // Assert
        Mockito.verify(mockFileUtil, Mockito.times(1)).fetchAccessories(asin, accessoryArray);
    }

    @Test
    public void testGetProduct_SearchSimilaritiesCalled() {
        // Arrange
        String asin = "ASIN123";
        String offer = "offer123";
        String page = "page123";
        Product product = Mockito.spy(new Product());
        Search mockSearch = Mockito.mock(Search.class);
        Mockito.doReturn(mockSearch).when(product).getSearch();
        ArrayList<ProductDetails> detailsArray = new ArrayList<>();
        detailsArray.add(new ProductDetails());
        ProductInfo mockProductInfo = Mockito.mock(ProductInfo.class);
        Mockito.when(mockProductInfo.getProductsArrayList()).thenReturn(detailsArray);
        Mockito.when(mockSearch.SimilaritesSearch(asin, "1")).thenReturn(mockProductInfo);

        // Act
        product.getProduct(asin, offer, page);

        // Assert
        Mockito.verify(mockSearch, Mockito.times(1)).SimilaritesSearch(asin, "1");
    }
}