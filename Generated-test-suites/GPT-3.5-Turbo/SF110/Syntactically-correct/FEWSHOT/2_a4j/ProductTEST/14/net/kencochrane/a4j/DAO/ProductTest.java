
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.wutka.jox.JOXBeanInputStream;

import net.kencochrane.a4j.beans.Accessories;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.beans.ProductInfo;
import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.file.Search;
import net.kencochrane.a4j.DAO.Product;

public class ProductTest {
    @Mock
    private FileUtil fileUtilMock;
    @Mock
    private FileInputStream fileInputStreamMock;
    @Mock
    private JOXBeanInputStream joxBeanInputStreamMock;

    @Test
    public void testGetProduct_whenFileUtilReturnsNull() {
        Product product = new Product();
        String asin = "1234";
        String offer = "offer";
        String page = "page";

        when(fileUtilMock.fetchASINFile(eq(asin), eq("heavy"), eq(offer), eq(page))).thenReturn(null);

        FullProduct result = product.getProduct(asin, offer, page);

        assertNull(result);
        verify(fileUtilMock).fetchASINFile(eq(asin), eq("heavy"), eq(offer), eq(page));
        verifyNoMoreInteractions(fileUtilMock);
    }

    @Test
    public void testGetProduct_whenFileUtilReturnsInputStreamAndJOXBeanInputStreamIsNull() throws Exception {
        Product product = new Product();
        String asin = "1234";
        String offer = "offer";
        String page = "page";

        when(fileUtilMock.fetchASINFile(eq(asin), eq("heavy"), eq(offer), eq(page))).thenReturn(fileInputStreamMock);
        whenNew(JOXBeanInputStream.class).withArguments((InputStream)fileInputStreamMock).thenReturn(joxBeanInputStreamMock);
        when(joxBeanInputStreamMock.readObject(eq(ProductInfo.class))).thenReturn(null);

        FullProduct result = product.getProduct(asin, offer, page);

        assertNull(result);
        verify(fileUtilMock).fetchASINFile(eq(asin), eq("heavy"), eq(offer), eq(page));
        verify(joxBeanInputStreamMock).close();
        verify(fileInputStreamMock).close();
        verify(joxBeanInputStreamMock).readObject(eq(ProductInfo.class));
        verifyNoMoreInteractions(joxBeanInputStreamMock, fileInputStreamMock);
    }

    @Test
    public void testGetProduct_whenProductInfoBeanIsNull() throws Exception {
        Product product = new Product();
        String asin = "1234";
        String offer = "offer";
        String page = "page";
        FullProduct fullProductMock = mock(FullProduct.class);
        ProductDetails productDetailsMock = mock(ProductDetails.class);
        Accessories accessoriesMock = mock(Accessories.class);
        ArrayList accessoryArrayMock = mock(ArrayList.class);
        ArrayList detailsArrayMock = mock(ArrayList.class);
        MiniProduct miniProductMock = mock(MiniProduct.class);
        Search searchMock = mock(Search.class);
        ProductInfo productInfoBeanMock = mock(ProductInfo.class);

        when(fileUtilMock.fetchASINFile(eq(asin), eq("heavy"), eq(offer), eq(page))).thenReturn(fileInputStreamMock);
        whenNew(JOXBeanInputStream.class).withArguments((InputStream)fileInputStreamMock).thenReturn(joxBeanInputStreamMock);
        when(joxBeanInputStreamMock.readObject(eq(ProductInfo.class))).thenReturn(productInfoBeanMock);
        when(productInfoBeanMock.getDetails()).thenReturn(new ProductDetails[]{productDetailsMock});
        when(productDetailsMock.getAccessories()).thenReturn(accessoriesMock);
        when(accessoriesMock.getAccessoryArray()).thenReturn(accessoryArrayMock);
        when(accessoryArrayMock.size()).thenReturn(1);
        when(fileUtilMock.fetchAccessories(eq(asin), eq(accessoryArrayMock))).thenReturn(fileInputStreamMock);
        whenNew(JOXBeanInputStream.class).withArguments((InputStream)fileInputStreamMock).thenReturn(joxBeanInputStreamMock);
        when(joxBeanInputStreamMock.readObject(eq(ProductInfo.class))).thenReturn(null);
        when(searchMock.SimilaritesSearch(eq(asin), eq("1"))).thenReturn(null);

        FullProduct result = product.getProduct(asin, offer, page);

        assertNull(result);
        verify(fileUtilMock).fetchASINFile(eq(asin), eq("heavy"), eq(offer), eq(page));
        verify(joxBeanInputStreamMock).close();
        verify(fileInputStreamMock).close();
        verify(joxBeanInputStreamMock).readObject(eq(ProductInfo.class));
        verify(productInfoBeanMock).getDetails();
        verify(productDetailsMock, times(2)).getAccessories();
        verify(accessoriesMock).getAccessoryArray();
        verify(accessoryArrayMock).size();
        verify(fileUtilMock).fetchAccessories(eq(asin), eq(accessoryArrayMock));
        verify(joxBeanInputStreamMock, times(2)).close();
        verify(fileInputStreamMock, times(2)).close();
        verify(joxBeanInputStreamMock, times(2)).readObject(eq(ProductInfo.class));
        verify(searchMock).SimilaritesSearch(eq(asin), eq("1"));
        verifyNoMoreInteractions(fileUtilMock, joxBeanInputStreamMock, fileInputStreamMock, productInfoBeanMock,
                productDetailsMock, accessoriesMock, accessoryArrayMock, searchMock);
    }

    @Test
    public void testGetProduct_whenProductInfoBeanIsNotNull() throws Exception {
        Product product = new Product();
        String asin = "1234";
        String offer = "offer";
        String page = "page";
        FullProduct fullProductMock = mock(FullProduct.class);
        ProductDetails productDetailsMock = mock(ProductDetails.class);
        Accessories accessoriesMock = mock(Accessories.class);
        ArrayList accessoryArrayMock = mock(ArrayList.class);
        ArrayList detailsArrayMock = mock(ArrayList.class);
        MiniProduct miniProductMock = mock(MiniProduct.class);
        Search searchMock = mock(Search.class);
        ProductInfo productInfoBeanMock = mock(ProductInfo.class);

        when(fileUtilMock.fetchASINFile(eq(asin), eq("heavy"), eq(offer), eq(page))).thenReturn(fileInputStreamMock);
        whenNew(JOXBeanInputStream.class).withArguments((InputStream)fileInputStreamMock).thenReturn(joxBeanInputStreamMock);
        when(joxBeanInputStreamMock.readObject(eq(ProductInfo.class))).thenReturn(productInfoBeanMock);
        when(productInfoBeanMock.getDetails()).thenReturn(new ProductDetails[]{productDetailsMock});
        when(productDetailsMock.getAccessories()).thenReturn(accessoriesMock);
        when(accessoriesMock.getAccessoryArray()).thenReturn(accessoryArrayMock);
        when(accessoryArrayMock.size()).thenReturn(1);
        when(fileUtilMock.fetchAccessories(eq(asin), eq(accessoryArrayMock))).thenReturn(fileInputStreamMock);
        whenNew(JOXBeanInputStream.class).withArguments((InputStream)fileInputStreamMock).thenReturn(joxBeanInputStreamMock);
        when(joxBeanInputStreamMock.readObject(eq(ProductInfo.class))).thenReturn(productInfoBeanMock);
        when(productInfoBeanMock.getProductsArrayList()).thenReturn(detailsArrayMock);
        when(detailsArrayMock.size()).thenReturn(1);
        when(detailsArrayMock.get(0)).thenReturn(productDetailsMock);
        when(productDetailsMock.getAsin()).thenReturn("accessoryAsin");
        when(productDetailsMock.getImageUrlSmall()).thenReturn("accessoryImageUrlSmall");
        when(productDetailsMock.getManufacturer()).thenReturn("accessoryManufacturer");
        when(productDetailsMock.getProductName()).thenReturn("accessoryProductName");
        when(productDetailsMock.getOurPrice()).thenReturn("accessoryOurPrice");
        when(searchMock.SimilaritesSearch(eq(asin), eq("1"))).thenReturn(productInfoBeanMock);

        FullProduct result = product.getProduct(asin, offer, page);

        assertEquals(fullProductMock, result);
        verify(fileUtilMock).fetchASINFile(eq(asin), eq("heavy"), eq(offer), eq(page));
        verify(joxBeanInputStreamMock).close();
        verify(fileInputStreamMock).close();
        verify(joxBeanInputStreamMock).readObject(eq(ProductInfo.class));
        verify(productInfoBeanMock).getDetails();
        verify(productDetailsMock, times(2)).getAccessories();
        verify(accessoriesMock).getAccessoryArray();
        verify(accessoryArrayMock).size();
        verify(fileUtilMock).fetchAccessories(eq(asin), eq(accessoryArrayMock));
        verify(joxBeanInputStreamMock, times(2)).close();
        verify(fileInputStreamMock, times(2)).close();
        verify(joxBeanInputStreamMock, times(2)).readObject(eq(ProductInfo.class));
        verify(productInfoBeanMock, times(2)).getProductsArrayList();
        verify(detailsArrayMock, times(2)).size();
        verify(detailsArrayMock).get(eq(0));
        verify(productDetailsMock).getAsin();
        verify(productDetailsMock).getImageUrlSmall();
        verify(productDetailsMock).getManufacturer();
        verify(productDetailsMock).getProductName();
        verify(productDetailsMock).getOurPrice();
        verify(searchMock).SimilaritesSearch(eq(asin), eq("1"));
        verify(miniProductMock, times(2)).setAsin(eq("accessoryAsin"));
        verify(miniProductMock, times(2)).setImageURL(eq("accessoryImageUrlSmall"));
        verify(miniProductMock, times(2)).setManufacturer(eq("accessoryManufacturer"));
        verify(miniProductMock, times(2)).setName(eq("accessoryProductName"));
        verify(miniProductMock, times(2)).setPrice(eq("accessoryOurPrice"));
        verify(fullProductMock, times(2)).addAccessory(eq(miniProductMock));
        verifyNoMoreInteractions(fileUtilMock, joxBeanInputStreamMock, fileInputStreamMock, productInfoBeanMock,
                productDetailsMock, accessoriesMock, accessoryArrayMock, detailsArrayMock, searchMock, miniProductMock, fullProductMock);
    }

}
