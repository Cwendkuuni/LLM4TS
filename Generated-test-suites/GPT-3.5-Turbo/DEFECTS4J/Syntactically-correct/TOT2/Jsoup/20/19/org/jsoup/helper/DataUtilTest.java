import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        try {
            File file = new File("test.html");
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            Document document = DataUtil.load(file, charsetName, baseUri);
            // Add assertions here to test the functionality of the method
        } catch (IOException e) {
            // Handle exception
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            InputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            // Add assertions here to test the functionality of the method
        } catch (IOException e) {
            // Handle exception
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        try {
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            Parser parser = Parser.htmlParser();
            InputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
            Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
            // Add assertions here to test the functionality of the method
        } catch (IOException e) {
            // Handle exception
        }
    }

    @Test
    public void testParseByteData() {
        // This method is internal and cannot be directly tested
        // Edge cases and exception handling should be verified indirectly through other tests
    }

    @Test
    public void testReadToByteBuffer() {
        // This method is internal and cannot be directly tested
        // Edge cases and exception handling should be verified indirectly through other tests
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        // Add assertions here to test the functionality of the method
    }

}