import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    // Test loading a file to a Document
    @Test
    public void testLoadFromFile() throws IOException {
        File testFile = new File("test.html");
        Document doc = DataUtil.load(testFile, "UTF-8", "http://www.example.com");
        assertNotNull(doc);

        // Add more assertions here if needed
    }

    // Test loading from an InputStream
    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream in = new ByteArrayInputStream("test data".getBytes());
        Document doc = DataUtil.load(in, "UTF-8", "http://www.example.com");
        assertNotNull(doc);

        // Add more assertions here if needed
    }

    // Test loading from an InputStream with a specific Parser
    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream in = new ByteArrayInputStream("test data".getBytes());
        Document doc = DataUtil.load(in, "UTF-8", "http://www.example.com", Parser.xmlParser());
        assertNotNull(doc);

        // Add more assertions here if needed
    }

    // Test parsing byte data with charset detection
    @Test
    public void testParseByteDataWithCharsetDetection() {
        ByteBuffer byteData = ByteBuffer.wrap("test data".getBytes());
        Document doc = DataUtil.parseByteData(byteData, null, "http://www.example.com", Parser.xmlParser());
        assertNotNull(doc);

        // Add more assertions here if needed
    }

    // Test reading bytes to ByteBuffer from an InputStream
    @Test
    public void testReadToByteBufferFromInputStream() throws IOException {
        InputStream in = new ByteArrayInputStream("test data".getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(in);
        assertNotNull(byteData);

        // Add more assertions here if needed
    }

    // Test getting charset from content type header
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        // Add more assertions here if needed
    }
}