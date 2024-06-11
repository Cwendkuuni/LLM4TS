package net.kencochrane.a4j.DAO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import net.kencochrane.a4j.file.FileUtil;
import com.wutka.jox.JOXBeanInputStream;

public class ProductTest {
    
    @Test
    public void testGetProduct() {
        Product product = new Product();
        
        String asin = "ASIN";
        String offer = "offer";
        String page = "page";
        
        FullProduct fullProduct = product.getProduct(asin, offer, page);
        assertNull(fullProduct);
        
        // mock the fetchASINFile() method
        FileUtil fileUtilMock = new FileUtil(){
            public FileInputStream fetchASINFile(String asin, String param2, String offer, String page) {
                // mock return value
                return null;
            }
        };
        product.setFileUtil(fileUtilMock);
        fullProduct = product.getProduct(asin, offer, page);
        assertNull(fullProduct);
        
        // mock the fetchASINFile() method
        fileUtilMock = new FileUtil(){
            public FileInputStream fetchASINFile(String asin, String param2, String offer, String page) {
                // mock return value
                return new FileInputStream();
            }
        };
        product.setFileUtil(fileUtilMock);
        
        // mock the JOXBeanInputStream() constructor
        JOXBeanInputStream joxInMock = new JOXBeanInputStream(){
            public Object readObject(Class clazz) {
                // mock return value
                return new ProductInfo();
            }
        };
        
        // mock the FileInputStream() constructor
        FileInputStream fileInMock = new FileInputStream(){
            public void close() throws IOException {
                // do nothing
            }
        };
        
        // mock the fetchASINFile() method again to return mock FileInputStream
        fileUtilMock = new FileUtil(){
            public FileInputStream fetchASINFile(String asin, String param2, String offer, String page) {
                // mock return value
                return fileInMock;
            }
        };
        product.setFileUtil(fileUtilMock);
        
        fullProduct = product.getProduct(asin, offer, page);
        assertNotNull(fullProduct);
        
        
    }
}