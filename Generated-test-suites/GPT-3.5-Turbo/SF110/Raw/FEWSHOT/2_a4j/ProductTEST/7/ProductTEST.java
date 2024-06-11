package net.kencochrane.a4j.DAO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.beans.Accessories;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.beans.Search;
import com.wutka.jox.JOXBeanInputStream;
import com.wutka.jox.JOXBeanOutputStream;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ProductTest {
    private FileUtil fileUtilMock;
    private ProductInfo productInfoMock;
    private JOXBeanInputStream joxInMock;
    private JOXBeanOutputStream joxOutMock;
    private FileInputStream fileInMock;
    private Product product;

    @Before
    public void setUp() {
        fileUtilMock = mock(FileUtil.class);
        productInfoMock = mock(ProductInfo.class);
        joxInMock = mock(JOXBeanInputStream.class);
        joxOutMock = mock(JOXBeanOutputStream.class);
        fileInMock = mock(FileInputStream.class);

        product = new Product();
        product.setFileUtil(fileUtilMock);
        product.setSearch(searchMock);
    }

    @Test
    public void testGetProduct_WithInvalidFile_ReturnsNull() {
        when(fileUtilMock.fetchASINFile(anyString(), anyString(), anyString(), anyString())).thenReturn(null);

        FullProduct result = product.getProduct("asin", "offer", "page");

        assertNull(result);
    }

    @Test
    public void testGetProduct_WithValidFileAndNoDetails_ReturnsFullProductWithNoDetails() throws IOException {
        when(fileUtilMock.fetchASINFile(anyString(), anyString(), anyString(), anyString())).thenReturn(fileInMock);
        when(joxInMock.readObject(ProductInfo.class)).thenReturn(productInfoMock);
        when(productInfoMock.getDetails()).thenReturn(new ProductDetails[0]);

        FullProduct result = product.getProduct("asin", "offer", "page");

        verify(joxInMock).readObject(ProductInfo.class);
        verify(fileInMock).close();
        assertEquals(new FullProduct(), result);
    }

    @Test
    public void testGetProduct_WithValidFileAndEmptyAccessoryArray_ReturnsFullProductWithoutAccessories() throws IOException {
        when(fileUtilMock.fetchASINFile(anyString(), anyString(), anyString(), anyString())).thenReturn(fileInMock);
        when(joxInMock.readObject(ProductInfo.class)).thenReturn(productInfoMock);
        when(productInfoMock.getDetails()).thenReturn(new ProductDetails[1]);

        FullProduct result = product.getProduct("asin", "offer", "page");

        verify(joxInMock).readObject(ProductInfo.class);
        verify(fileInMock).close();
        assertEquals(new FullProduct(), result);
    }

    @Test
    public void testGetProduct_WithValidFileAndValidAccessoryArray_ReturnsFullProductWithAccessories() throws IOException {
        when(fileUtilMock.fetchASINFile(anyString(), anyString(), anyString(), anyString())).thenReturn(fileInMock);
        when(joxInMock.readObject(ProductInfo.class)).thenReturn(productInfoMock);
        when(productInfoMock.getDetails()).thenReturn(new ProductDetails[1]);
        when(productInfoMock.getDetails()[0].getAccessories()).thenReturn(new Accessories());
        when(productInfoMock.getDetails()[0].getAccessories().getAccessoryArray()).thenReturn(new ArrayList<>());
        when(fileUtilMock.fetchAccessories(anyString(), any())).thenReturn(fileInMock);
        when(joxInMock.readObject(ProductInfo.class)).thenReturn(productInfoMock);

        FullProduct result = product.getProduct("asin", "offer", "page");

        verify(joxInMock).readObject(ProductInfo.class);
        verify(fileInMock, times(2)).close();
        assertEquals(new FullProduct(), result);
    }

    @Test
    public void testGetProduct_WithValidFileAndValidAccessoryArrayAndValidSimilarItems_ReturnsFullProductWithAccessoriesAndSimilarItems() throws IOException {
        when(fileUtilMock.fetchASINFile(anyString(), anyString(), anyString(), anyString())).thenReturn(fileInMock);
        when(joxInMock.readObject(ProductInfo.class)).thenReturn(productInfoMock);
        when(productInfoMock.getDetails()).thenReturn(new ProductDetails[1]);
        when(productInfoMock.getDetails()[0].getAccessories()).thenReturn(new Accessories());
        when(productInfoMock.getDetails()[0].getAccessories().getAccessoryArray()).thenReturn(new ArrayList<>());
        when(fileUtilMock.fetchAccessories(anyString(), any())).thenReturn(fileInMock);
        when(joxInMock.readObject(ProductInfo.class)).thenReturn(productInfoMock);
        when(productInfoMock.getProductsArrayList()).thenReturn(new ArrayList<>());
        when(productInfoMock.getProductsArrayList().size()).thenReturn(0);
        when(productInfoMock.getProductsArrayList().get(anyInt())).thenReturn(new ProductDetails());

        FullProduct result = product.getProduct("asin", "offer", "page");

        verify(joxInMock).readObject(ProductInfo.class);
        verify(fileInMock, times(2)).close();
        assertEquals(new FullProduct(), result);
    }
}