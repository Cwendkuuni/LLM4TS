
package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import static org.junit.Assert.*;

public class DataUtilTest {
    @Test
    public void testLoadFromFileWithValidCharset() throws IOException {
        String basePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources";
        String filePath = basePath + File.separator + "test.html";
        File file = new File(filePath);
        String baseUri = "http://example.com";

        Document document = DataUtil.load(file, "UTF-8", baseUri);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testLoadFromFileWithInvalidCharset() throws IOException {
        String basePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources";
        String filePath = basePath + File.separator + "test.html";
        File file = new File(filePath);
        String baseUri = "http://example.com";

        Document document = DataUtil.load(file, "INVALID_CHARSET", baseUri);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testLoadFromFileWithNullCharset() throws IOException {
        String basePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources";
        String filePath = basePath + File.separator + "test.html";
        File file = new File(filePath);
        String baseUri = "http://example.com";

        Document document = DataUtil.load(file, null, baseUri);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testLoadFromInputStreamWithValidCharset() throws IOException {
        String basePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources";
        String filePath = basePath + File.separator + "test.html";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, "UTF-8", baseUri);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testLoadFromInputStreamWithInvalidCharset() throws IOException {
        String basePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources";
        String filePath = basePath + File.separator + "test.html";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, "INVALID_CHARSET", baseUri);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testLoadFromInputStreamWithNullCharset() throws IOException {
        String basePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources";
        String filePath = basePath + File.separator + "test.html";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, null, baseUri);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testLoadFromInputStreamWithCustomParser() throws IOException {
        String basePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources";
        String filePath = basePath + File.separator + "test.html";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();

        Document document = DataUtil.load(inputStream, null, baseUri, parser);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseByteDataWithValidCharsetFromContentType() {
        String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(Charset.forName("UTF-8")));
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertEquals(Charset.forName("UTF-8").name(), document.outputSettings().charset().name());
    }

    @Test
    public void testParseByteDataWithInvalidCharsetFromContentType() {
        String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=INVALID_CHARSET\"></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(Charset.forName("UTF-8")));
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertEquals(Charset.forName("UTF-8").name(), document.outputSettings().charset().name());
    }

    @Test
    public void testParseByteDataWithNullCharsetFromContentType() {
        String html = "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(Charset.forName("UTF-8")));
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertEquals(Charset.forName("UTF-8").name(), document.outputSettings().charset().name());
    }

    @Test
    public void testParseByteDataWithValidCharsetSpecified() {
        String html = "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(Charset.forName("ISO-8859-1")));
        String charsetName = "ISO-8859-1";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertEquals(Charset.forName("ISO-8859-1").name(), document.outputSettings().charset().name());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String basePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources";
        String filePath = basePath + File.separator + "test.html";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);

        assertNotNull(byteBuffer);
        assertTrue(byteBuffer.capacity() > 0);
    }

    @Test
    public void testGetCharsetFromContentTypeWithValidContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=INVALID_CHARSET";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNullContentType() {
        String contentType = null;

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }
}
