import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.util.Arrays;

import static org.junit.Assert.*;

public class DataUtilTest {
    private static final String BASE_URI = "https://example.com";

    @Test
    public void testLoad_fromFile() throws IOException {
        File file = new File("test.html");
        String expected = "<html><head></head><body><h1>Hello, World!</h1></body></html>";
        Files.write(file.toPath(), expected.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(file, StandardCharsets.UTF_8.name(), BASE_URI);

        assertEquals(expected, doc.html());

        file.delete();
    }

    @Test
    public void testLoad_inputStream() throws IOException {
        String html = "<html><head></head><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), BASE_URI);

        assertEquals(html, doc.html());

        inputStream.close();
    }

    @Test
    public void testLoad_inputStream_withParser() throws IOException {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><element>Hello, World!</element></root>";
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), BASE_URI, Parser.xmlParser());

        assertEquals(xml, doc.html());

        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        String input = "Hello, World!";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        assertEquals(input, outputStream.toString(StandardCharsets.UTF_8.name()));

        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testParseByteData_withCharsetFromMeta() {
        Charset expectedCharset = Charset.forName("ISO-8859-1");
        ByteBuffer byteData = ByteBuffer.wrap("<html><head><meta charset=\"ISO-8859-1\"></head><body>Hello, World!</body></html>".getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseByteData(byteData, null, BASE_URI, Parser.htmlParser());

        assertEquals(expectedCharset, doc.outputSettings().charset());
        assertEquals("<html><head><meta charset=\"ISO-8859-1\"></head><body>Hello, World!</body></html>", doc.html());
    }

    @Test
    public void testParseByteData_withCharsetFromHttpEquiv() {
        Charset expectedCharset = Charset.forName("ISO-8859-1");
        ByteBuffer byteData = ByteBuffer.wrap("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\"></head><body>Hello, World!</body></html>".getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseByteData(byteData, null, BASE_URI, Parser.htmlParser());

        assertEquals(expectedCharset, doc.outputSettings().charset());
        assertEquals("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\"></head><body>Hello, World!</body></html>", doc.html());
    }

    @Test
    public void testParseByteData_withDefaultCharset() {
        Charset expectedCharset = Charset.forName(DataUtil.defaultCharset);
        ByteBuffer byteData = ByteBuffer.wrap("<html><head></head><body>Hello, World!</body></html>".getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseByteData(byteData, null, BASE_URI, Parser.htmlParser());

        assertEquals(expectedCharset, doc.outputSettings().charset());
        assertEquals("<html><head></head><body>Hello, World!</body></html>", doc.html());
    }

    @Test
    public void testReadToByteBuffer_withMaxSize() throws IOException {
        String input = "Hello, World!";
        byte[] expected = input.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(expected);

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 5);

        byte[] actual = new byte[byteBuffer.remaining()];
        byteBuffer.get(actual);

        assertTrue(Arrays.equals(expected, actual));

        inputStream.close();
    }

    @Test
    public void testReadToByteBuffer_withoutMaxSize() throws IOException {
        String input = "Hello, World!";
        byte[] expected = input.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(expected);

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        byte[] actual = new byte[byteBuffer.remaining()];
        byteBuffer.get(actual);

        assertTrue(Arrays.equals(expected, actual));

        inputStream.close();
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String input = "Hello, World!";
        File file = new File("test.txt");
        Files.write(file.toPath(), input.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);

        byte[] actual = new byte[byteBuffer.remaining()];
        byteBuffer.get(actual);

        assertTrue(Arrays.equals(input.getBytes(StandardCharsets.UTF_8), actual));

        file.delete();
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.remaining());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_withUnsupportedCharset() {
        String contentType = "text/html; charset=unsupported";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();

        assertEquals(DataUtil.boundaryLength, boundary.length());
    }
}