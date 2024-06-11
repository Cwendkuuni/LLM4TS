import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class DataUtilTest {

    private File file;
    private File nonExistentFile;
    private InputStream inputStream;
    private String baseUri;

    @Before
    public void setup() {
        file = new File("input.html");
        nonExistentFile = new File("nonexistent.html");
        inputStream = null; // Set input stream to null to test for IOException
        baseUri = "http://example.com";
        
        // Create or set up input.html file for testing

    }

    // Expert 1
    @Test
    public void testLoad_withFileCharsetAndBaseUri_validInputFile_expectDocument() throws IOException {
        Document document = DataUtil.load(file, "UTF-8", baseUri);

        Assert.assertNotNull(document);
        // Additional assertions to validate loaded document
    }

    // Expert 2
    @Test
    public void testLoad_withInputStreamCharsetAndBaseUri_validInputStream_expectDocument() throws IOException {
        InputStream inStream = new FileInputStream(file);
        Document document = DataUtil.load(inStream, "UTF-8", baseUri);

        Assert.assertNotNull(document);
        // Additional assertions to validate loaded document
    }

    // Expert 3
    @Test
    public void testLoad_withInputStreamCharsetBaseUriAndParser_validInputStream_expectDocument() throws IOException {
        InputStream inStream = new FileInputStream(file);
        Document document = DataUtil.load(inStream, "UTF-8", baseUri, Parser.htmlParser());

        Assert.assertNotNull(document);
        // Additional assertions to validate loaded document
    }

    // Expert 1
    @Test(expected = IOException.class)
    public void testLoad_withNonExistentFile_expectIOException() throws IOException {
        DataUtil.load(nonExistentFile, "UTF-8", baseUri);
    }

    // Expert 2
    @Test(expected = IOException.class)
    public void testLoad_withNullInputStream_expectIOException() throws IOException {
        DataUtil.load(inputStream, "UTF-8", baseUri);
    }

    // Expert 3
    @Test(expected = IOException.class)
    public void testLoad_withInvalidInputStream_expectIOException() throws IOException {
        InputStream inStream = new FileInputStream(nonExistentFile);
        DataUtil.load(inStream, "UTF-8", baseUri, Parser.htmlParser());
    }

    // Expert 1
    @Test
    public void testParseByteData_withCharsetNull_expectDocument() {
        ByteBuffer byteData = ByteBuffer.wrap("Test HTML".getBytes());
        Document document = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());

        Assert.assertNotNull(document);
        // Additional assertions to validate parsed document
    }

    // Expert 2
    @Test
    public void testParseByteData_withCharsetAndBaseUri_expectDocument() {
        ByteBuffer byteData = ByteBuffer.wrap("Test HTML".getBytes());
        Document document = DataUtil.parseByteData(byteData, "UTF-8", baseUri, Parser.htmlParser());

        Assert.assertNotNull(document);
        // Additional assertions to validate parsed document
    }

    // Expert 3
    @Test
    public void testParseByteData_withCharsetBaseUriAndParser_expectDocument() {
        ByteBuffer byteData = ByteBuffer.wrap("Test HTML".getBytes());
        Document document = DataUtil.parseByteData(byteData, "UTF-8", baseUri, Parser.htmlParser());

        Assert.assertNotNull(document);
        // Additional assertions to validate parsed document
    }

    // Expert 1
    @Test
    public void testReadToByteBuffer_withInputStreamAndMaxSize_expectByteBuffer() throws IOException {
        InputStream inStream = new FileInputStream(file);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream, 100);

        Assert.assertNotNull(byteBuffer);
        // Additional assertions to validate read byte buffer
    }

    // Expert 2
    @Test
    public void testReadToByteBuffer_withInputStream_expectByteBuffer() throws IOException {
        InputStream inStream = new FileInputStream(file);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream);

        Assert.assertNotNull(byteBuffer);
        // Additional assertions to validate read byte buffer
    }

    // Expert 3
    @Test
    public void testReadToByteBuffer_withInputStreamAndZeroMaxSize_expectByteBuffer() throws IOException {
        InputStream inStream = new FileInputStream(file);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream, 0);

        Assert.assertNotNull(byteBuffer);
        // Additional assertions to validate read byte buffer
    }

    // Expert 1
    @Test
    public void testGetCharsetFromContentType_withValidContentType_expectCharset() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertEquals("UTF-8", charset);
    }

    // Expert 2
    @Test
    public void testGetCharsetFromContentType_withValidContentTypeAndInvalidCharset_expectNull() {
        String contentType = "text/html; charset=InvalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertNull(charset);
    }

    // Expert 3
    @Test
    public void testGetCharsetFromContentType_withInvalidContentType_expectNull() {
        String contentType = "invalid/content; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertNull(charset);
    }

    // Expert 1
    @Test
    public void testGetCharsetFromContentType_withNullContentType_expectNull() {
        String charset = DataUtil.getCharsetFromContentType(null);

        Assert.assertNull(charset);
    }

    // Expert 2
    @Test
    public void testGetCharsetFromContentType_withEmptyContentType_expectNull() {
        String charset = DataUtil.getCharsetFromContentType("");

        Assert.assertNull(charset);
    }

    // Expert 3
    @Test
    public void testGetCharsetFromContentType_withContentTypeWithoutCharset_expectNull() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertNull(charset);
    }

}