import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataUtilTest {

    // Test case for load(File in, String charsetName, String baseUri)
    @Test
    public void testLoadFile() {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        try {
            Document doc = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(doc);
        } catch (Exception e) {
            assertNull(e);
        }
    }

    // Test case for load(InputStream in, String charsetName, String baseUri)
    @Test
    public void testLoadInputStream() {
        InputStream inputStream = null; // Input stream pointing to the test data
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            assertNotNull(doc);
        } catch (Exception e) {
            assertNull(e);
        }
    }

    // Test case for load(InputStream in, String charsetName, String baseUri, Parser parser)
    @Test
    public void testLoadInputStreamWithParser() {
        InputStream inputStream = null; // Input stream pointing to the test data
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.xmlParser(); // Using XML parser for testing
        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
            assertNotNull(doc);
        } catch (Exception e) {
            assertNull(e);
        }
    }

    // Test case for getCharsetFromContentType(String contentType)
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

}