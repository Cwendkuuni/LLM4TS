import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        try {
            File in = new File("test.html");
            Document doc = DataUtil.load(in, charsetName, baseUri);
            
            // Assert statements
            Assert.assertNotNull(doc);
            Assert.assertTrue(doc.title().equalsIgnoreCase("Test HTML"));
            
        } catch (IOException e) {
            Assert.fail("Exception occurred while loading file");
        }
    }

    @Test
    public void testLoadInputStream() {
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        try {
            InputStream in = getClass().getResourceAsStream("/test.html");
            Document doc = DataUtil.load(in, charsetName, baseUri);

            // Assert statements
            Assert.assertNotNull(doc);
            Assert.assertTrue(doc.title().equalsIgnoreCase("Test HTML"));
            
        } catch (IOException e) {
            Assert.fail("Exception occurred while loading input stream");
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        
        try {
            InputStream in = getClass().getResourceAsStream("/test.html");
            Document doc = DataUtil.load(in, charsetName, baseUri, parser);

            // Assert statement
            Assert.assertNotNull(doc);
            Assert.assertTrue(doc.title().equalsIgnoreCase("Test HTML"));
            
        } catch (IOException e) {
            Assert.fail("Exception occurred while loading input stream with parser");
        }
    }

    @Test
    public void testParseByteData() {
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        
        try {
            InputStream in = getClass().getResourceAsStream("/test.html");
            byte[] byteData = DataUtil.readToByteBuffer(in).array();
            ByteBuffer byteBuffer = ByteBuffer.wrap(byteData);
            
            Document doc = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);
            
            // Assert statements
            Assert.assertNotNull(doc);
            Assert.assertTrue(doc.title().equalsIgnoreCase("Test HTML"));
            
        } catch (IOException e) {
            Assert.fail("Exception occurred while parsing byte data");
        }
    }

    @Test
    public void testReadToByteBuffer() {
        String charsetName = "UTF-8";
        int maxSize = 0;
        
        try {
            InputStream in = getClass().getResourceAsStream("/test.html");
            ByteBuffer byteData = DataUtil.readToByteBuffer(in, maxSize);
            
            // Assert statements
            Assert.assertNotNull(byteData);
            Assert.assertTrue(byteData.capacity() > 0);
            
        } catch (IOException e) {
            Assert.fail("Exception occurred while reading to byte buffer");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        // Assert statement
        Assert.assertEquals("UTF-8", charset);
    }

}