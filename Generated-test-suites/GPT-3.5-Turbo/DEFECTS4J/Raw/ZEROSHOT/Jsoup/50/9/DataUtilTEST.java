import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class DataUtilTest {

    @Test
    public void testLoad_File_CharsetName_BaseUri() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document document = DataUtil.load(file, charsetName, baseUri);

        // Asserts
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoad_InputStream_CharsetName_BaseUri() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(in, charsetName, baseUri);

        // Asserts
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoad_InputStream_CharsetName_BaseUri_Parser() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.load(in, charsetName, baseUri, parser);

        // Asserts
        Assert.assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = getClass().getResourceAsStream("input.txt");
        OutputStream out = new ByteArrayOutputStream();

        DataUtil.crossStreams(in, out);

        // Asserts
        Assert.assertEquals("Test", out.toString());
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        // Asserts
        Assert.assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer_WithMaxSize() throws IOException {
        InputStream inStream = getClass().getResourceAsStream("input.txt");
        int maxSize = 10;

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream, maxSize);

        // Asserts
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void testReadToByteBuffer_WithoutMaxSize() throws IOException {
        InputStream inStream = getClass().getResourceAsStream("input.txt");

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream);

        // Asserts
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.txt");

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);

        // Asserts
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        // Asserts
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType_WithCharset() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Asserts
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_WithoutCharset() {
        String contentType = "text/html";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Asserts
        Assert.assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();

        // Asserts
        Assert.assertNotNull(mimeBoundary);
        Assert.assertEquals(32, mimeBoundary.length());
    }
}