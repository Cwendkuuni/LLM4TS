
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.*;
import java.io.*;
import java.nio.charset.Charset;

public class DataUtilTest {

    // Test case for the load(File in, String charsetName, String baseUri) method
    @Test
    public void testLoadFile() {
        // Typical use case: file exists and can be loaded
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document document = DataUtil.load(file, charsetName, baseUri);
            Assert.assertNotNull(document);
        } catch (IOException e) {
            Assert.fail("IOException occurred: " + e.getMessage());
        }

        // Edge case: empty file
        File emptyFile = new File("empty.html");
        try {
            Document document = DataUtil.load(emptyFile, charsetName, baseUri);
            Assert.assertNotNull(document);
        } catch (IOException e) {
            Assert.fail("IOException occurred: " + e.getMessage());
        }

        // Error scenario: file does not exist
        File nonExistingFile = new File("non_existing.html");
        try {
            Document document = DataUtil.load(nonExistingFile, charsetName, baseUri);
            Assert.fail("IOException should have been thrown");
        } catch (IOException e) {
            // Expected exception
        }
    }

    // Test case for the load(InputStream in, String charsetName, String baseUri) method
    @Test
    public void testLoadInputStream() {
        // Typical use case: InputStream can be loaded
        InputStream inputStream = new ByteArrayInputStream("<html><body><p>Hello World</p></body></html>".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            Assert.assertNotNull(document);
        } catch (IOException e) {
            Assert.fail("IOException occurred: " + e.getMessage());
        }

        // Edge case: empty InputStream
        InputStream emptyInputStream = new ByteArrayInputStream("".getBytes());
        try {
            Document document = DataUtil.load(emptyInputStream, charsetName, baseUri);
            Assert.assertNotNull(document);
        } catch (IOException e) {
            Assert.fail("IOException occurred: " + e.getMessage());
        }
    }

    // Test case for the load(InputStream in, String charsetName, String baseUri, Parser parser) method
    @Test
    public void testLoadInputStreamWithParser() {
        // Typical use case: InputStream can be loaded with custom Parser
        InputStream inputStream = new ByteArrayInputStream("<html><body><p>Hello World</p></body></html>".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
            Assert.assertNotNull(document);
        } catch (IOException e) {
            Assert.fail("IOException occurred: " + e.getMessage());
        }

        // Edge case: empty InputStream with custom Parser
        InputStream emptyInputStream = new ByteArrayInputStream("".getBytes());
        try {
            Document document = DataUtil.load(emptyInputStream, charsetName, baseUri, parser);
            Assert.assertNotNull(document);
        } catch (IOException e) {
            Assert.fail("IOException occurred: " + e.getMessage());
        }
    }

    // Test case for the parseByteData(ByteBuffer byteData, String charsetName, String baseUri, Parser parser) method
    @Test
    public void testParseByteData() {
        // Typical use case: ByteBuffer contains valid data
        byte[] testData = "<html><body><p>Hello World</p></body></html>".getBytes();
        ByteBuffer byteData = ByteBuffer.wrap(testData);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        Assert.assertNotNull(document);

        // Edge case: ByteBuffer contains empty data
        byte[] emptyData = "".getBytes();
        ByteBuffer emptyByteData = ByteBuffer.wrap(emptyData);
        document = DataUtil.parseByteData(emptyByteData, charsetName, baseUri, parser);
        Assert.assertNotNull(document);
    }

    // Test case for the readToByteBuffer(InputStream inStream) method
    @Test
    public void testReadToByteBuffer() {
        // Typical use case: InputStream can be read into ByteBuffer
        InputStream inputStream = new ByteArrayInputStream("<html><body><p>Hello World</p></body></html>".getBytes());
        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
            Assert.assertNotNull(byteData);
        } catch (IOException e) {
            Assert.fail("IOException occurred: " + e.getMessage());
        }

        // Edge case: empty InputStream
        InputStream emptyInputStream = new ByteArrayInputStream("".getBytes());
        try {
            ByteBuffer emptyByteData = DataUtil.readToByteBuffer(emptyInputStream);
            Assert.assertNotNull(emptyByteData);
        } catch (IOException e) {
            Assert.fail("IOException occurred: " + e.getMessage());
        }
    }

    // Test case for the getCharsetFromContentType(String contentType) method
    @Test
    public void testGetCharsetFromContentType() {
        // Typical use case: valid content type with charset
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);

        // Edge case: empty content type
        String emptyContentType = "";
        charset = DataUtil.getCharsetFromContentType(emptyContentType);
        Assert.assertNull(charset);

        // Error scenario: unsupported charset
        String unsupportedCharsetType = "text/html; charset=invalidCharset";
        charset = DataUtil.getCharsetFromContentType(unsupportedCharsetType);
        Assert.assertNull(charset);
    }
}

