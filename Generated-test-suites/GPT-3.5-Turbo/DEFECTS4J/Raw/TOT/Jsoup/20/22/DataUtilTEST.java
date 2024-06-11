import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoad_File_Valid() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        // Add additional assertions for the loaded document if necessary
    }

    @Test
    public void testLoad_File_InvalidFile() {
        File file = new File("non_existing_file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        assertThrows(IOException.class, () -> DataUtil.load(file, charsetName, baseUri));
    }

    @Test
    public void testLoad_File_NullCharset() throws IOException {
        File file = new File("test.html");
        String charsetName = null;
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        // Add additional assertions for the loaded document if necessary
    }

    @Test
    public void testLoad_InputStream_Valid() throws IOException {
        String html = "<html><head><title>Test Page</title></head><body><p>Hello, World!</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        // Add additional assertions for the loaded document if necessary
    }

    @Test
    public void testLoad_InputStream_NullCharset() throws IOException {
        String html = "<html><head><title>Test Page</title></head><body><p>Hello, World!</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        // Add additional assertions for the loaded document if necessary
    }

    @Test
    public void testLoad_InputStream_InvalidStream() {
        InputStream inputStream = null;
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        assertThrows(IOException.class, () -> DataUtil.load(inputStream, charsetName, baseUri));
    }

    @Test
    public void testLoad_InputStream_CustomParser() throws IOException {
        String html = "<html><head><title>Test Page</title></head><body><p>Hello, World!</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        // Add additional assertions for the loaded document if necessary
    }

    @Test
    public void testParseByteData_CorrectCharset() {
        String html = "<html><head><title>Test Page</title></head><body><p>Hello, World!</p></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        // Add additional assertions for the parsed document if necessary
    }

    @Test
    public void testParseByteData_NullCharset() {
        String html = "<html><head><title>Test Page</title></head><body><p>Hello, World!</p></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        // Add additional assertions for the parsed document if necessary
    }

    @Test
    public void testParseByteData_InvalidCharset() {
        String html = "<html><head><title>Test Page</title></head><body><p>Hello, World!</p></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "INVALID_CHARSET";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        assertThrows(IllegalCharsetNameException.class, () -> DataUtil.parseByteData(byteData, charsetName, baseUri, parser));
    }

    @Test
    public void testReadToByteBuffer_ValidInputStream() throws IOException {
        String html = "<html><head><title>Test Page</title></head><body><p>Hello, World!</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        assertNotNull(byteData);
        assertEquals(html, new String(byteData.array()));
    }

    @Test
    public void testReadToByteBuffer_NullInputStream() {
        InputStream inputStream = null;

        assertThrows(IOException.class, () -> DataUtil.readToByteBuffer(inputStream));
    }

    @Test
    public void testGetCharsetFromContentType_ValidContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_InvalidContentType() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType_NullContentType() {
        String contentType = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }
}