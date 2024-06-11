package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import net.kencochrane.a4j.file.FileUtil;
import com.wutka.jox.JOXBeanInputStream;
import java.io.*;

public class ProductTest {

    private FileUtil fileUtil;
    private Product product;

    @Before
    public void setUp() {
        fileUtil = new FileUtil();
        product = new Product();
    }

    @Test
    public void testGetProduct_whenFileInIsNull_returnsNull() {
        FullProduct result = product.getProduct("asin", "offer", "page");
        assertNull(result);
    }

    @Test
    public void testGetProduct_whenProductInfoBeanIsNull_returnsNull() throws IOException {
        FileInputStream fileIn = new FileInputStream("test.asin");
        when(fileUtil.fetchASINFile("asin", "heavy", "offer", "page")).thenReturn(fileIn);

        FullProduct result = product.getProduct("asin", "offer", "page");

        assertNull(result);
        fileIn.close();
    }

    @Test
    public void testGetProduct_whenProductInfoBeanDetailsIsEmpty_returnsFullProduct() throws IOException {
        FileInputStream fileIn = new FileInputStream("test.asin");
        when(fileUtil.fetchASINFile("asin", "heavy", "offer", "page")).thenReturn(fileIn);
        JOXBeanInputStream joxIn = mock(JOXBeanInputStream.class);
        whenNew(JOXBeanInputStream.class).withArguments((InputStream) fileIn).thenReturn(joxIn);
        ProductInfo productInfoBean = new ProductInfo();
        when(joxIn.readObject(ProductInfo.class)).thenReturn(productInfoBean);

        FullProduct result = product.getProduct("asin", "offer", "page");

        assertNotNull(result);
        assertEquals(productInfoBean.getDetails()[0], result.getDetails());
        verify(joxIn).close();
        fileIn.close();
    }

    @Test
    public void testGetProduct_whenAccessoryArrayIsEmpty_doesNotFetchAccessories() throws IOException {
        FileInputStream fileIn = new FileInputStream("test.asin");
        when(fileUtil.fetchASINFile("asin", "heavy", "offer", "page")).thenReturn(fileIn);
        JOXBeanInputStream joxIn = mock(JOXBeanInputStream.class);
        whenNew(JOXBeanInputStream.class).withArguments((InputStream) fileIn).thenReturn(joxIn);
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        productDetails.setAccessories(new Accessories());
        productInfoBean.setDetails(new ProductDetails[]{productDetails});
        when(joxIn.readObject(ProductInfo.class)).thenReturn(productInfoBean);

        FullProduct result = product.getProduct("asin", "offer", "page");

        assertNotNull(result);
        assertEquals(productDetails, result.getDetails());
        verify(joxIn).close();
        fileIn.close();
    }

    @Test
    public void testGetProduct_whenAccessoryArraySizeIsGreaterThanZero_fetchesAccessories() throws IOException {
        FileInputStream fileIn = new FileInputStream("test.asin");
        when(fileUtil.fetchASINFile("asin", "heavy", "offer", "page")).thenReturn(fileIn);
        JOXBeanInputStream joxIn = mock(JOXBeanInputStream.class);
        whenNew(JOXBeanInputStream.class).withArguments((InputStream) fileIn).thenReturn(joxIn);
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        accessories.setAccessoryArray(new ArrayList());
        productDetails.setAccessories(accessories);
        productInfoBean.setDetails(new ProductDetails[]{productDetails});
        when(joxIn.readObject(ProductInfo.class)).thenReturn(productInfoBean);

        FileInputStream fileIn1 = new FileInputStream("testAccessories.asin");
        when(fileUtil.fetchAccessories("asin", accessories.getAccessoryArray())).thenReturn(fileIn1);
        JOXBeanInputStream joxIn1 = mock(JOXBeanInputStream.class);
        whenNew(JOXBeanInputStream.class).withArguments((InputStream) fileIn1).thenReturn(joxIn1);
        ProductInfo productInfoBean1 = new ProductInfo();
        when(joxIn1.readObject(ProductInfo.class)).thenReturn(productInfoBean1);

        FullProduct result = product.getProduct("asin", "offer", "page");

        assertNotNull(result);
        assertEquals(productDetails, result.getDetails());
        verify(joxIn).close();
        verify(joxIn1).close();
        fileIn.close();
        fileIn1.close();
    }

    @Test
    public void testGetProduct_whenAccessoryProductDetailsIsNull_doesNotAddAccessory() throws IOException {
        FileInputStream fileIn = new FileInputStream("test.asin");
        when(fileUtil.fetchASINFile("asin", "heavy", "offer", "page")).thenReturn(fileIn);
        JOXBeanInputStream joxIn = mock(JOXBeanInputStream.class);
        whenNew(JOXBeanInputStream.class).withArguments((InputStream) fileIn).thenReturn(joxIn);
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        accessories.setAccessoryArray(new ArrayList());
        productDetails.setAccessories(accessories);
        productInfoBean.setDetails(new ProductDetails[]{productDetails});
        when(joxIn.readObject(ProductInfo.class)).thenReturn(productInfoBean);

        FileInputStream fileIn1 = new FileInputStream("testAccessories.asin");
        when(fileUtil.fetchAccessories("asin", accessories.getAccessoryArray())).thenReturn(fileIn1);
        JOXBeanInputStream joxIn1 = mock(JOXBeanInputStream.class);
        whenNew(JOXBeanInputStream.class).withArguments((InputStream) fileIn1).thenReturn(joxIn1);
        ProductInfo productInfoBean1 = new ProductInfo();
        productInfoBean1.setProductsArrayList(new ArrayList());
        when(joxIn1.readObject(ProductInfo.class)).thenReturn(productInfoBean1);

        FullProduct result = product.getProduct("asin", "offer", "page");

        assertNotNull(result);
        assertEquals(productDetails, result.getDetails());
        verify(joxIn).close();
        verify(joxIn1).close();
        fileIn.close();
        fileIn1.close();
    }

    @Test
    public void testGetProduct_whenAccessoryProductDetailsIsNotNull_addsAccessory() throws IOException {
        FileInputStream fileIn = new FileInputStream("test.asin");
        when(fileUtil.fetchASINFile("asin", "heavy", "offer", "page")).thenReturn(fileIn);
        JOXBeanInputStream joxIn = mock(JOXBeanInputStream.class);
        whenNew(JOXBeanInputStream.class).withArguments((InputStream) fileIn).thenReturn(joxIn);
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        accessories.setAccessoryArray(new ArrayList());
        productDetails.setAccessories(accessories);
        productInfoBean.setDetails(new ProductDetails[]{productDetails});
        when(joxIn.readObject(ProductInfo.class)).thenReturn(productInfoBean);

        FileInputStream fileIn1 = new FileInputStream("testAccessories.asin");
        when(fileUtil.fetchAccessories("asin", accessories.getAccessoryArray())).thenReturn(fileIn1);
        JOXBeanInputStream joxIn1 = mock(JOXBeanInputStream.class);
        whenNew(JOXBeanInputStream.class).withArguments((InputStream) fileIn1).thenReturn(joxIn1);
        ProductInfo productInfoBean1 = new ProductInfo();
        ArrayList<ProductDetails> detailsArray = new ArrayList<>();
        detailsArray.add(new ProductDetails());
        productInfoBean1.setProductsArrayList(detailsArray);
        when(joxIn1.readObject(ProductInfo.class)).thenReturn(productInfoBean1);

        FullProduct result = product.getProduct("asin", "offer", "page");

        assertNotNull(result);
        assertEquals(productDetails, result.getDetails());
        verify(joxIn).close();
        verify(joxIn1).close();
        fileIn.close();
        fileIn1.close();
    }

    @Test
    public void testGetProduct_whenProductInfoBeanIsNotNullAndSimilarItemsSizeIsGreaterThanZero_addsSimilarItems() {
        FileInputStream fileIn = new FileInputStream("test.asin");
        when(fileUtil.fetchASINFile("asin", "heavy", "offer", "page")).thenReturn(fileIn);
        JOXBeanInputStream joxIn = mock(JOXBeanInputStream.class);
        whenNew(JOXBeanInputStream.class).withArguments((InputStream) fileIn).thenReturn(joxIn);
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        accessories.setAccessoryArray(new ArrayList());
        productDetails.setAccessories(accessories);
        productInfoBean.setDetails(new ProductDetails[]{productDetails});
        when(joxIn.readObject(ProductInfo.class)).thenReturn(productInfoBean);

        FileInputStream fileIn1 = new FileInputStream("testAccessories.asin");
        when(fileUtil.fetchAccessories("asin", accessories.getAccessoryArray())).thenReturn(fileIn1);
        JOXBeanInputStream joxIn1 = mock(JOXBeanInputStream.class);
        whenNew(JOXBeanInputStream.class).withArguments((InputStream) fileIn1).thenReturn(joxIn1);
        ProductInfo productInfoBean1 = new ProductInfo();
        ArrayList<ProductDetails> detailsArray = new ArrayList<>();
        detailsArray.add(new ProductDetails());
        productInfoBean1.setProductsArrayList(detailsArray);
        when(joxIn1.readObject(ProductInfo.class)).thenReturn(productInfoBean1);
        
        Search search = mock(Search.class);
        product.setSearch(search);
        ProductInfo similarProductInfoBean = new ProductInfo();
        similarProductInfoBean.setProductsArrayList(detailsArray);
        when(search.SimilaritesSearch("asin", "1")).thenReturn(similarProductInfoBean);

        FullProduct result = product.getProduct("asin", "offer", "page");

        assertNotNull(result);
        assertEquals(productDetails, result.getDetails());
        verify(joxIn).close();
        verify(joxIn1).close();
        verify(search).SimilaritesSearch("asin", "1");
        fileIn.close();
        fileIn1.close();
    }

    @Test
    public void testGetProduct_whenProductInfoBeanIsNotNullAndSimilarItemsIsNull_doesNotAddSimilarItems() {
        FileInputStream fileIn = new FileInputStream("test.asin");
        when(fileUtil.fetchASINFile("asin", "heavy", "offer", "page")).thenReturn(fileIn);
        JOXBeanInputStream joxIn = mock(JOXBeanInputStream.class);
        whenNew(JOXBeanInputStream.class).withArguments((InputStream) fileIn).thenReturn(joxIn);
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        accessories.setAccessoryArray(new ArrayList());
        productDetails.setAccessories(accessories);
        productInfoBean.setDetails(new ProductDetails[]{productDetails});
        when(joxIn.readObject(ProductInfo.class)).thenReturn(productInfoBean);

        FileInputStream fileIn1 = new FileInputStream("testAccessories.asin");
        when(fileUtil.fetchAccessories("asin", accessories.getAccessoryArray())).thenReturn(fileIn1);
        JOXBeanInputStream joxIn1 = mock(JOXBeanInputStream.class);
        whenNew(JOXBeanInputStream.class).withArguments((InputStream) fileIn1).thenReturn(joxIn1);
        ProductInfo productInfoBean1 = new ProductInfo();
        ArrayList<ProductDetails> detailsArray = new ArrayList<>();
        detailsArray.add(new ProductDetails());
        productInfoBean1.setProductsArrayList(detailsArray);
        when(joxIn1.readObject(ProductInfo.class)).thenReturn(productInfoBean1);
        
        Search search = mock(Search.class);
        product.setSearch(search);
        when(search.SimilaritesSearch("asin", "1")).thenReturn(null);

        FullProduct result = product.getProduct("asin", "offer", "page");

        assertNotNull(result);
        assertEquals(productDetails, result.getDetails());
        verify(joxIn).close();
        verify(joxIn1).close();
        verify(search).SimilaritesSearch("asin", "1");
        fileIn.close();
        fileIn1.close();
    }

    @Test
    public void testGetProduct_whenExceptionThrown_printsStackTrace() {
        FileInputStream fileIn = new FileInputStream("test.asin");
        when(fileUtil.fetchASINFile("asin", "heavy", "offer", "page")).thenReturn(fileIn);
        JOXBeanInputStream joxIn = mock(JOXBeanInputStream.class);
        whenNew(JOXBeanInputStream.class).withArguments((InputStream) fileIn).thenReturn(joxIn);
        when(joxIn.readObject(ProductInfo.class)).thenThrow(new IOException());
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        System.setErr(printStream);

        FullProduct result = product.getProduct("asin", "offer", "page");

        assertNull(result);
        assertTrue(baos.toString().length() > 0);
        fileIn.close();
    }
}