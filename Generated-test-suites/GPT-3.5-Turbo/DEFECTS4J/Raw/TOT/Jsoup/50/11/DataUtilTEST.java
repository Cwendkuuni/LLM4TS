import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("example.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(file, charsetName, baseUri);

        assertEquals("Example Title", document.title());
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("example.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        assertEquals("Example Title", document.title());
        assertEquals("http://example.com", document.baseUri());

        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("example.xml");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();

        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        assertEquals("root", document.select("tag").first().tagName());
        assertEquals("http://example.com", document.baseUri());

        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        assertEquals("Hello World", outputStream.toString());
    }

    @Test
    public void testParseByteDataWithCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("Hello World".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertEquals("Hello World", document.text());
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testParseByteDataWithoutCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("Hello World".getBytes());
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, null, baseUri, parser);

        assertEquals("Hello World", document.text());
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
        int maxSize = 5;

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        assertEquals("Hello", new String(byteBuffer.array(), Charset.forName("UTF-8")));
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        assertEquals("Hello World", new String(byteBuffer.array(), Charset.forName("UTF-8")));
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("example.txt");

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);

        assertEquals("Hello World", new String(byteBuffer.array(), Charset.forName("UTF-8")));
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();

        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }
}