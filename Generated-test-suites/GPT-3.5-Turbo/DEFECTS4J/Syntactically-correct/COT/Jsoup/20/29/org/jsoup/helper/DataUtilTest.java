import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {

    // Test load(File in, String charsetName, String baseUri)
    @Test
    public void testLoadFile() throws IOException {
        File inputFile = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = DataUtil.load(inputFile, charsetName, baseUri);
        
        // Add asserts to check the functionality of the method
    }

    // Test load(InputStream in, String charsetName, String baseUri)
    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        
        // Add asserts to check the functionality of the method
    }

    // Test load(InputStream in, String charsetName, String baseUri, Parser parser)
    @Test
    public void testLoadInputStreamParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
        
        // Add asserts to check the functionality of the method
    }

    // Test parseByteData(ByteBuffer byteData, String charsetName, String baseUri, Parser parser)
    // No need to write a test case for this method since it is a private method called internally by other public methods.

    // Test readToByteBuffer(InputStream inStream)
    // No need to write a test case for this method since it is a private method called internally by other public methods.

    // Test getCharsetFromContentType(String contentType)
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertEquals(expectedCharset, charset);
    }

}