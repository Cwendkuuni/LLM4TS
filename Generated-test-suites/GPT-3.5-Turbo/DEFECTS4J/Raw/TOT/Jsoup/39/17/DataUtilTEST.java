import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import java.io.*;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;

public class DataUtilTest {

    // Test for load(File in, String charsetName, String baseUri) method
    @Test
    public void testLoadFileWithValidData() {
        try {
            File file = new File("testFile.html");
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            Document doc = DataUtil.load(file, charsetName, baseUri);
            assertNotNull("Document should not be null", doc);
            assertEquals("Document base URI should be equal to baseUri", baseUri, doc.baseUri());
        } catch (IOException e) {
            fail("IOException occurred while loading file");
        }
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidFile() throws IOException {
        File file = new File("nonExistingFile.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        DataUtil.load(file, charsetName, baseUri);
    }

    @Test
    public void testLoadFileWithNullParameters() {
        try {
            File file = new File("testFile.html");
            String charsetName = null;
            String baseUri = null;
            Document doc = DataUtil.load(file, charsetName, baseUri);
            assertNotNull("Document should not be null", doc);
            assertEquals("Document base URI should be equal to an empty string", "", doc.baseUri());
        } catch (IOException e) {
            fail("IOException occurred while loading file");
        }
    }

    // Test for load(InputStream in, String charsetName, String baseUri) method
    @Test
    public void testLoadInputStreamWithValidData() {
        try {
            InputStream inputStream = new FileInputStream("testFile.html");
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            assertNotNull("Document should not be null", doc);
            assertEquals("Document base URI should be equal to baseUri", baseUri, doc.baseUri());
        } catch (IOException e) {
            fail("IOException occurred while loading input stream");
        }
    }

    // Test for load(InputStream in, String charsetName, String baseUri, Parser parser) method
    @Test
    public void testLoadInputStreamWithParserWithValidData() {
        try {
            InputStream inputStream = new FileInputStream("testFile.html");
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            Document doc = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
            assertNotNull("Document should not be null", doc);
            assertEquals("Document base URI should be equal to baseUri", baseUri, doc.baseUri());
        } catch (IOException e) {
            fail("IOException occurred while loading input stream with parser");
        }
    }

    // Test for parseByteData(ByteBuffer byteData, String charsetName, String baseUri, Parser parser) method
    @Test
    public void testParseByteDataWithValidData() {
        ByteBuffer byteData = ByteBuffer.wrap("Test data".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull("Document should not be null", doc);
        assertEquals("Document base URI should be equal to baseUri", baseUri, doc.baseUri());
    }

    // Test for readToByteBuffer(InputStream inStream, int maxSize) method
    @Test
    public void testReadToByteBufferWithValidData() {
        try {
            InputStream inputStream = new FileInputStream("testFile.html");
            int maxSize = 100000;
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
            assertNotNull("ByteBuffer should not be null", byteBuffer);
            assertTrue("ByteBuffer capacity should be greater than zero", byteBuffer.capacity() > 0);
        } catch (IOException e) {
            fail("IOException occurred while reading input stream to byte buffer");
        }
    }

    // Test for getCharsetFromContentType(String contentType) method
    @Test
    public void testGetCharsetFromContentTypeWithValidData() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull("Charset should not be null", charset);
        assertEquals("Charset should be equal to UTF-8", "UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidData() {
        String contentType = "text/html; charset=invalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull("Charset should be null for invalid charset", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNullContentType() {
        String charset = DataUtil.getCharsetFromContentType(null);
        assertNull("Charset should be null for null content type", charset);
    }



}