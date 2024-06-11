package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;
import org.junit.Test;
import net.kencochrane.a4j.beans.*;
import net.kencochrane.a4j.file.FileUtil;

public class ProductTest {

    @Test
    public void testGetProduct() {
        // Generate test data
        String asin = "testAsin";
        String offer = "testOffer";
        String page = "testPage";

        // Create objects for test
        Product product = new Product();
        FullProduct expectedFullProduct = new FullProduct();
        ProductDetails expectedProductDetails = new ProductDetails();
        Accessories expectedAccessories = new Accessories();
        ArrayList expectedAccessoryArray = new ArrayList();
        ArrayList expectedDetailsArray = new ArrayList();
        MiniProduct expectedMiniProduct = new MiniProduct();
        Search expectedSearch = new Search();

        try {
            // Mock fileUtil
            FileUtil fileUtil = Mockito.mock(FileUtil.class);
            FileInputStream expectedFileIn = new FileInputStream(new File("testFile"));
            Mockito.when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(expectedFileIn);

            // Mock JOXBeanInputStream and ProductInfo
            JOXBeanInputStream expectedJoxIn = Mockito.mock(JOXBeanInputStream.class);
            ProductInfo expectedProductInfoBean = new ProductInfo();
            expectedJoxIn.when(expectedJoxIn.readObject((Class)ProductInfo.class))
                    .thenReturn(expectedProductInfoBean);
            Mockito.whenNew(JOXBeanInputStream.class).withArguments(expectedFileIn)
                    .thenReturn(expectedJoxIn);

            // Mock Search and ProductInfo for similar search
            ProductInfo expectedSimilarProductInfoBean = new ProductInfo();
            expectedSearch.when(expectedSearch.SimilaritesSearch(asin, "1"))
                    .thenReturn(expectedSimilarProductInfoBean);

            // Test method
            FullProduct actualFullProduct = product.getProduct(asin, offer, page);

            // Verify the returned object
            assertEquals(expectedFullProduct, actualFullProduct);

            // Verify the calls in the method
            Mockito.verify(fileUtil).fetchASINFile(asin, "heavy", offer, page);
            Mockito.verify(expectedJoxIn).readObject((Class)ProductInfo.class);
            Mockito.verify(expectedSearch).SimilaritesSearch(asin, "1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}