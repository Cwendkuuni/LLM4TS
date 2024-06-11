
import org.jsoup.helper.DataUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoad() throws IOException {
        File in = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Document expected = Jsoup.parse(in, charsetName, baseUri);
        Document actual = DataUtil.load(in, charsetName, baseUri);
        assertEquals(expected, actual);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Document expected = Jsoup.parse(in, charsetName, baseUri);
        Document actual = DataUtil.load(in, charsetName, baseUri);
        assertEquals(expected, actual);
    }

    @Test
    public void testLoadInputStreamParser() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Parser parser = Parser.htmlParser();
        Document expected = Jsoup.parse(in, charsetName, baseUri, parser);
        Document actual = DataUtil.load(in, charsetName, baseUri, parser);
        assertEquals(expected, actual);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new FileInputStream("input.txt");
        OutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out);
        // check if output is correct
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Parser parser = Parser.htmlParser();
        Document actual = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        // check if parsing is correct
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inStream = new FileInputStream("input.txt");
        int maxSize = 0; // unlimited
        ByteBuffer expected = ByteBuffer.allocate(1024); // expected buffer contents
        ByteBuffer actual = DataUtil.readToByteBuffer(inStream, maxSize);
        assertEquals(expected, actual);
    }

    @Test
    public void testReadToByteBufferWithSize() throws IOException {
        InputStream inStream = new FileInputStream("input.txt");
        int maxSize = 1024;
        ByteBuffer expected = ByteBuffer.allocate(maxSize); // expected buffer contents
        ByteBuffer actual = DataUtil.readToByteBuffer(inStream, maxSize);
        assertEquals(expected, actual);
    }

    @Test
    public void testReadToByteBufferFromFile() throws IOException {
        File file = new File("input.txt");
        ByteBuffer expected = ByteBuffer.allocate(1024); // expected buffer contents
        ByteBuffer actual = DataUtil.readFileToByteBuffer(file);
        assertEquals(expected, actual);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer expected = ByteBuffer.allocate(0);
        ByteBuffer actual = DataUtil.emptyByteBuffer();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expected = "UTF-8";
        String actual = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expected, actual);
    }

    @Test
    public void testMimeBoundary() {
        String expected = DataUtil.mimeBoundary();
        assertNotNull(expected);
        assertEquals(DataUtil.boundaryLength, expected.length());
    }
}

