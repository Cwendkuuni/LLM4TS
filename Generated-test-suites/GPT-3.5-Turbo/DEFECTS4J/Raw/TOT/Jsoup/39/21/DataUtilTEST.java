import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

public class DataUtilTest {

    // Test load(File in, String charsetName, String baseUri) method

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);

        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        // Additional assertions specific to the loaded file content
    }

    @Test(expected = IOException.class)
    public void testLoadFileIOException() throws IOException {
        File file = new File("nonexistent.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        DataUtil.load(file, charsetName, baseUri);
    }

    // Test load(InputStream in, String charsetName, String baseUri) method

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);

        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        // Additional assertions specific to the loaded input stream
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamIOException() throws IOException {
        InputStream inputStream = new FileInputStream("nonexistent.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        DataUtil.load(inputStream, charsetName, baseUri);
    }

    // Test load(InputStream in, String charsetName, String baseUri, Parser parser) method

    @Test
    public void testLoadWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);

        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        // Additional assertions specific to the loaded input stream with specific parser
    }

    @Test(expected = IOException.class)
    public void testLoadWithParserIOException() throws IOException {
        InputStream inputStream = new FileInputStream("nonexistent.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        DataUtil.load(inputStream, charsetName, baseUri, parser);
    }

    // Test parseByteData(ByteBuffer byteData, String charsetName, String baseUri, Parser parser) method

    // Additional test cases for parseByteData() method can be added to cover different scenarios

    // Test readToByteBuffer(InputStream inStream, int maxSize) method

    // Additional test cases for readToByteBuffer() method can be added to cover different scenarios

    // Test getCharsetFromContentType(String contentType) method

    // Additional test cases for getCharsetFromContentType() method can be added to cover different scenarios

}