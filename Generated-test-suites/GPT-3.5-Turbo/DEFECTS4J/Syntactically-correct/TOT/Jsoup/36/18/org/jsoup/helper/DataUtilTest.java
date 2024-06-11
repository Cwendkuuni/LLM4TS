package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoad_withFile() throws IOException {
        File file = new File("test.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(file, charset, baseUri);

        assertNotNull(doc);
        assertEquals("UTF-8", doc.charset().name());
    }

    @Test
    public void testLoad_withInputStream() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(inputStream, charset, baseUri);

        assertNotNull(doc);
        assertEquals("UTF-8", doc.charset().name());
    }

    @Test
    public void testLoad_withParser() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.load(inputStream, charset, baseUri, parser);

        assertNotNull(doc);
        assertEquals("UTF-8", doc.charset().name());
    }

    @Test
    public void testParseByteData_withDefaultCharset() {
        String htmlData = "<html><head><title>Test</title></head><body></body></html>";
        byte[] byteData = htmlData.getBytes(StandardCharsets.UTF_8);
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseByteData(ByteBuffer.wrap(byteData), charsetName, baseUri, parser);

        assertNotNull(doc);
        assertEquals("UTF-8", doc.charset().name());
    }

    @Test
    public void testParseByteData_withCharsetFromMeta() {
        String htmlData = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=ISO-8859-1\"></head><body></body></html>";
        byte[] byteData = htmlData.getBytes(StandardCharsets.UTF_8);
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseByteData(ByteBuffer.wrap(byteData), charsetName, baseUri, parser);

        assertNotNull(doc);
        assertEquals("ISO-8859-1", doc.charset().name());
    }

    @Test
    public void testParseByteData_withCharsetFromMetaCharset() {
        String htmlData = "<html><head><meta charset=\"UTF-16\"></head><body></body></html>";
        byte[] byteData = htmlData.getBytes(StandardCharsets.UTF_8);
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseByteData(ByteBuffer.wrap(byteData), charsetName, baseUri, parser);

        assertNotNull(doc);
        assertEquals("UTF-16", doc.charset().name());
    }

    @Test
    public void testParseByteData_withCharsetSpecified() {
        String htmlData = "<html><head><title>Test</title></head><body></body></html>";
        byte[] byteData = htmlData.getBytes(StandardCharsets.UTF_8);
        String charsetName = "ISO-8859-1";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseByteData(ByteBuffer.wrap(byteData), charsetName, baseUri, parser);

        assertNotNull(doc);
        assertEquals("ISO-8859-1", doc.charset().name());
    }

    @Test
    public void testReadToByteBuffer_withUnlimitedMaxSize() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.html");

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);

        assertNotNull(byteBuffer);
        assertTrue(byteBuffer.capacity() > 0);
    }

    @Test
    public void testReadToByteBuffer_withLimitedMaxSize() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.html");

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);

        assertNotNull(byteBuffer);
        assertTrue(byteBuffer.capacity() <= 1024);
    }

    @Test
    public void testReadToByteBuffer_withInvalidMaxSize_shouldThrowIllegalArgumentException() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.html");

        try {
            DataUtil.readToByteBuffer(inputStream, -1);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("maxSize must be 0 (unlimited) or larger", e.getMessage());
        }
    }

    @Test
    public void testGetCharsetFromContentType_withValidContentType() {
        String contentType = "text/html; charset=ISO-8859-1";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("ISO-8859-1", charset);
    }

    @Test
    public void testGetCharsetFromContentType_withUnsupportedCharset_shouldReturnNull() {
        String contentType = "text/html; charset=foobar";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType_withUppercaseCharset() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_withInvalidContentType_shouldReturnNull() {
        String contentType = "text/html";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }
}