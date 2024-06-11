
package net.kencochrane.a4j.DAO;

import net.kencochrane.a4j.file.FileUtil;
import com.wutka.jox.JOXBeanInputStream;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.beans.Accessories;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.beans.Search;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class ProductTest {
    
    @Test
    public void testGetProduct() {
        // Initialize mocks
        FileUtil fileUtil = mock(FileUtil.class);
        JOXBeanInputStream joxIn = mock(JOXBeanInputStream.class);
        FileInputStream fileIn = mock(FileInputStream.class);
        
        // Initialize test data
        String asin = "ASIN123";
        String offer = "Offer123";
        String page = "Page123";
        
        // Initialize expected result
        FullProduct expectedProduct = new FullProduct();
        ProductDetails expectedProductDetails = new ProductDetails();
        Accessories expectedAccessories = new Accessories();
        ArrayList<MiniProduct> expectedAccessoryArray = new ArrayList<>();
        ArrayList<ProductDetails> expectedDetailsArray = new ArrayList<>();
        MiniProduct expectedMiniProduct = new MiniProduct();
        
        expectedProduct.setDetails(expectedProductDetails);
        expectedProductDetails.setAccessories(expectedAccessories);
        expectedAccessories.setAccessoryArray(expectedAccessoryArray);
        expectedAccessoryArray.add(expectedMiniProduct);
        expectedProduct.addAccessory(expectedMiniProduct);
        expectedProduct.addSimilarItem(expectedMiniProduct);
        
        // Mock dependencies
        when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(fileIn);
        when(fileUtil.fetchAccessories(asin, expectedAccessoryArray)).thenReturn(fileIn);
        try {
            when(joxIn.readObject(ProductInfo.class)).thenReturn(new ProductInfo());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create instance of Product for testing
        Product product = new Product(fileUtil);
        
        // Invoke the method to be tested
        FullProduct actualProduct = product.getProduct(asin, offer, page);
        
        // Verify the behavior
        verify(fileUtil, times(1)).fetchASINFile(asin, "heavy", offer, page);
        verify(joxIn, times(2)).close();
        verify(fileIn, times(2)).close();
        verify(joxIn, times(2)).readObject(ProductInfo.class);
        verify(fileUtil, times(1)).fetchAccessories(asin, expectedAccessoryArray);
        verify(fileUtil, never()).fetchAccessories(any(), any());
        
        // Verify the result
        Assert.assertEquals(expectedProduct, actualProduct);
    }
}
