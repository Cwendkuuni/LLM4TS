import org.jsoup.helper.*;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Locale;

import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoad_file() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testLoad_inputStream() throws IOException {
        File file = new File("test.html");
        FileInputStream inStream = new FileInputStream(file);
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document document = DataUtil.load(inStream, charsetName, baseUri);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testLoad_parser() throws IOException {
        File file = new File("test.html");
        FileInputStream inStream = new FileInputStream(file);
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.load(inStream, charsetName, baseUri, parser);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseByteData_charsetNameNull() {
        ByteBuffer byteData = ByteBuffer.wrap("Test document".getBytes());
        String charsetName = null;
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseByteData_charsetNameNotNull() {
        ByteBuffer byteData = ByteBuffer.wrap("Test document".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testReadToByteBuffer_maxSizeUnlimited() throws IOException {
        FileInputStream inStream = new FileInputStream("test.html");
        int maxSize = 0;
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream, maxSize);

        assertNotNull(byteData);
        assertTrue(byteData.limit() > 0);
    }

    @Test
    public void testReadToByteBuffer_maxSizeCapped() throws IOException {
        FileInputStream inStream = new FileInputStream("test.html");
        int maxSize = 1024;
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream, maxSize);

        assertNotNull(byteData);
        assertTrue(byteData.limit() > 0);
        assertTrue(byteData.limit() <= maxSize);
    }

    @Test
    public void testReadToByteBuffer_noMaxSize() throws IOException {
        FileInputStream inStream = new FileInputStream("test.html");
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);

        assertNotNull(byteData);
        assertTrue(byteData.limit() > 0);
    }

    @Test
    public void testGetCharsetFromContentType_charsetFound() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_charsetNotFound() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType_invalidCharset() {
        String contentType = "text/html; charset=Invalid";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

    // Additional tests can be added to cover all methods and edge cases in the DataUtil class.

}