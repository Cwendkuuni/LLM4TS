import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {

    // Test load(File in, String charsetName, String baseUri)
    @Test
    public void testLoad_File() throws IOException {
        File testFile = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";

        Document doc = DataUtil.load(testFile, charsetName, baseUri);

        assertNotNull(doc);
    }

    // Test load(InputStream in, String charsetName, String baseUri)
    @Test
    public void testLoad_InputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";

        Document doc = DataUtil.load(inputStream, charsetName, baseUri);

        assertNotNull(doc);
    }

    // Test load(InputStream in, String charsetName, String baseUri, Parser parser)
    @Test
    public void testLoad_InputStreamParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.xml");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.xmlParser();

        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);

        assertNotNull(doc);
    }

    // Test parseByteData(ByteBuffer byteData, String charsetName, String baseUri, Parser parser)
    @Test
    public void testParseByteData() {
        // Define test data
        String testData = "<html><head><title>Test</title></head><body><h1>Test</h1></body></html>";
        byte[] testDataBytes = testData.getBytes();
        ByteBuffer byteData = ByteBuffer.wrap(testDataBytes);
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(doc);
        assertEquals(testData, doc.html());
    }

    // Test readToByteBuffer(InputStream inStream)
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        assertNotNull(byteData);
        assertEquals(328, byteData.capacity());
    }

    // Test getCharsetFromContentType(String contentType)
    @Test
    public void testGetCharsetFromContentType() {
        // Define test data
        String contentTypeWithCharset = "text/html; charset=UTF-8";
        String contentTypeWithoutCharset = "text/html";

        String charsetWithCharset = DataUtil.getCharsetFromContentType(contentTypeWithCharset);
        String charsetWithoutCharset = DataUtil.getCharsetFromContentType(contentTypeWithoutCharset);

        assertEquals("UTF-8", charsetWithCharset);
        assertNull(charsetWithoutCharset);
    }

}