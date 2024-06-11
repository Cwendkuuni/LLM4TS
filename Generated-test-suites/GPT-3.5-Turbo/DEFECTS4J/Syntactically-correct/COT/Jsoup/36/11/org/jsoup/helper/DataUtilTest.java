
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class DataUtilTest {
    @Test
    public void testLoadFile() {
        File file = new File("test.html");
        try {
            Document document = DataUtil.load(file, "UTF-8", "http://example.com");
            Assert.assertNotNull(document);
            // Add more assertions as needed
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException occurred");
        }
    }
    
    @Test
    public void testLoadInputStream() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("test.html");
            Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
            Assert.assertNotNull(document);
            // Add more assertions as needed
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException occurred");
        }
    }
    
    @Test
    public void testLoadInputStreamWithParser() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("test.html");
            Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Jsoup.parser());
            Assert.assertNotNull(document);
            // Add more assertions as needed
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException occurred");
        }
    }
    
    // Edge case: Invalid charset name
    @Test
    public void testLoadFileWithInvalidCharset() {
        File file = new File("test.html");
        try {
            Document document = DataUtil.load(file, "InvalidCharset", "http://example.com");
            Assert.assertNotNull(document);
            // Add more assertions as needed
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException occurred");
        }
    }
    
    // Edge case: File not found
    @Test(expected = IOException.class)
    public void testLoadNonExistentFile() throws IOException {
        File file = new File("nonexistent.html");
        DataUtil.load(file, "UTF-8", "http://example.com");
    }
    
    // Exception handling: IOException
    @Test(expected = IOException.class)
    public void testLoadInputStreamIOException() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("nonexistent.html");
        DataUtil.load(inputStream, "UTF-8", "http://example.com");
    }
}
