import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
        // additional assertions for file loading
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Document doc = DataUtil.load(in, charsetName, baseUri);
        assertNotNull(doc);
        // additional assertions for input stream loading
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.load(in, charsetName, baseUri, parser);
        assertNotNull(doc);
        // additional assertions for input stream loading with parser
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new ByteArrayInputStream("Test Data".getBytes());
        OutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out);
        // additional assertions for cross streams
        assertEquals("Test Data", out.toString());
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("Test Data".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
        // additional assertions for parsing byte data
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream in = new ByteArrayInputStream("Test Data".getBytes());
        int maxSize = 0;
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in, maxSize);
        assertNotNull(byteBuffer);
        // additional assertions for reading to byte buffer with max size
        assertEquals("Test Data", Charset.forName("UTF-8").decode(byteBuffer).toString());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream in = new ByteArrayInputStream("Test Data".getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in);
        assertNotNull(byteBuffer);
        // additional assertions for reading to byte buffer
        assertEquals("Test Data", Charset.forName("UTF-8").decode(byteBuffer).toString());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        assertNotNull(byteBuffer);
        // additional assertions for reading file to byte buffer
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeInvalidCharset() {
        String contentType = "text/html; charset=InvalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 'T', 'e', 's', 't'});
        String charsetName = "UTF-8";
        String detectedCharset = DataUtil.detectCharsetFromBom(byteData, charsetName);
        assertEquals("UTF-8", detectedCharset);
    }

    // additional test cases for other methods

    
}