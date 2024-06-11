import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document doc = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(doc);
            // Check if the document was loaded correctly
            // Assert statements
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception thrown");
        }
    }

    @Test
    public void testLoadInputStream() {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            assertNotNull(doc);
            // Check if the document was loaded correctly
            // Assert statements
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception thrown");
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
            assertNotNull(doc);
            // Check if the document was loaded correctly
            // Assert statements
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception thrown");
        }
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
        // Check if the document was parsed correctly
        // Assert statements
    }

    @Test
    public void testReadToByteBufferWithMaxSize() {
        InputStream inStream = new FileInputStream("test.html");
        int maxSize = 1024;
        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(inStream, maxSize);
            assertNotNull(byteData);
            // Check if the byte buffer was read correctly
            // Assert statements
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception thrown");
        }
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() {
        InputStream inStream = new FileInputStream("test.html");
        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
            assertNotNull(byteData);
            // Check if the byte buffer was read correctly
            // Assert statements
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception thrown");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
        // Check if the charset was extracted correctly
        // Assert statements
    }

}