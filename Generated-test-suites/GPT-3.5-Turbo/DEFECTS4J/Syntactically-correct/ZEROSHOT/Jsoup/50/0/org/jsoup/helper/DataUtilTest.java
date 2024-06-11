import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

public class DataUtilTest {
    private static final String BASE_URI = "http://example.com";

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, null, BASE_URI);
        String expectedTitle = "Test Page";
        assertEquals(expectedTitle, doc.title());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Document doc = DataUtil.load(inputStream, null, BASE_URI);
        String expectedTitle = "Test Page";
        assertEquals(expectedTitle, doc.title());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Document doc = DataUtil.load(inputStream, null, BASE_URI, Parser.htmlParser());
        String expectedTitle = "Test Page";
        assertEquals(expectedTitle, doc.title());
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        InputStream newInputStream = DataUtil.crossStreams(byteBuffer, null);
        Document doc = Jsoup.parse(newInputStream, null, BASE_URI);
        String expectedTitle = "Test Page";
        assertEquals(expectedTitle, doc.title());
    }

    @Test
    public void testParseByteDataWithCharset() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("<html><head><title>Test Page</title></head><body><h1>Hello, World!</h1></body></html>".getBytes());
        String charsetName = "UTF-8";
        Document doc = DataUtil.parseByteData(byteBuffer, charsetName, BASE_URI, Parser.htmlParser());
        String expectedTitle = "Test Page";
        assertEquals(expectedTitle, doc.title());
    }

    @Test
    public void testParseByteDataWithoutCharset() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("<html><head><title>Test Page</title></head><body><h1>Hello, World!</h1></body></html>".getBytes());
        Document doc = DataUtil.parseByteData(byteBuffer, null, BASE_URI, Parser.htmlParser());
        String expectedTitle = "Test Page";
        assertEquals(expectedTitle, doc.title());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        String expectedCharset = "UTF-8";
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithoutCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(null, charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }
}