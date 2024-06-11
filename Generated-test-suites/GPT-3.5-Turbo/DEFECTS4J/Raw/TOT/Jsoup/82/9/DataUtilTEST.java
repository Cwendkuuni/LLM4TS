import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull("Document should not be null", document);
        assertEquals("Document base URI should match", "http://example.com", document.baseUri());
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String html = "<html><body><h1>Test</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull("Document should not be null", document);
        assertEquals("Document base URI should match", "http://example.com", document.baseUri());
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        String xml = "<root><element>test</element></root>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xml.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        assertNotNull("Document should not be null", document);
        assertEquals("Document base URI should match", "http://example.com", document.baseUri());
        assertTrue("Document should be parsed as XML", document.isXml());
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals("Output stream should contain the same data as input stream", "Test", outputStream.toString());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><body><h1>Test</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull("Document should not be null", document);
        assertEquals("Document base URI should match", "http://example.com", document.baseUri());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String data = "Test";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes());
        ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream, data.length());
        assertEquals("Buffer size should match data length", data.length(), buffer.capacity());
        byte[] byteArray = new byte[buffer.capacity()];
        buffer.get(byteArray);
        assertEquals("Buffer contents should match data", data, new String(byteArray));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("Charset should match", "UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull("Boundary should not be null", boundary);
        assertEquals("Boundary length should match", DataUtil.boundaryLength, boundary.length());
    }

    @Test
    public void testInvalidCharset() {
        String invalidCharset = "invalidCharset";
        try {
            DataUtil.load(new File("test.html"), invalidCharset, "http://example.com");
            fail("Should throw IllegalCharsetNameException for invalid charset");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception message should contain charset name", e.getMessage().contains(invalidCharset));
        }
    }
}