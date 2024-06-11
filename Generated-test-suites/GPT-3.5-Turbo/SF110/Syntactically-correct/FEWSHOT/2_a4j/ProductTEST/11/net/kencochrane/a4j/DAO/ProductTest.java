
package net.kencochrane.a4j.DAO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.wutka.jox.JOXBeanInputStream;

import net.kencochrane.a4j.beans.Accessories;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.beans.ProductInfo;
import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.file.Search;

public class ProductTest {
   
	private Product product;
	private FileUtil fileUtil;
	private JOXBeanInputStream joxIn;
  
	@Before
    public void setUp() {
		product = new Product();
		fileUtil = mock(FileUtil.class);
		joxIn = mock(JOXBeanInputStream.class);
	}
   
    @Test
    public void testGetProductWhenFileInIsNull() {
    	String asin = "";
    	String offer = "";
        String page = "";
        when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(null);
    	
        FullProduct result = product.getProduct(asin, offer, page);

        assertNull(result);
    }
    
    @Test
    public void testGetProductWithValidInputs() {
    	String asin = "ASIN";
    	String offer = "offer";
        String page = "page";
        
        FileInputStream fileIn = mock(FileInputStream.class);
        when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(fileIn);
        when(joxIn.readObject(ProductInfo.class)).thenReturn(new ProductInfo());
        when(joxIn.readObject(ProductInfo.class)).thenReturn(null);
        
        ProductDetails productDetails = new ProductDetails();
        productDetails.setAccessories(new Accessories());
        when(joxIn.readObject(ProductDetails.class)).thenReturn(productDetails);
        
        JOXBeanInputStream joxIn1 = mock(JOXBeanInputStream.class);
        FileIn fileIn1 = mock(FileIn.class);
        ProductInfo productInfo = new ProductInfo();
        productInfo.setDetails(new ProductDetails[1]);
        
        when(fileUtil.fetchAccessories(asin, accessoryArray)).thenReturn(fileIn1);
        when(joxIn.readObject(ProductInfo.class)).thenReturn(productInfo);
        when(productInfo.getProductsArrayList().size()).thenReturn(1);
        when(detailsArray.get(0)).thenReturn(accessoryProductDetails);
  
        FullProduct result = product.getProduct(asin, offer, page);

        assertEquals(expectedValue, result);
    }
}
