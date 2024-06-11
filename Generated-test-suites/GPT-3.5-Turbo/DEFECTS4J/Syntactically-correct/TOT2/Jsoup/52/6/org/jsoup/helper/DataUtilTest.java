import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        // Typical use case
        String baseUri = "https://www.example.com";
        try {
            Document document = DataUtil.load(new File("test.html"), "UTF-8", baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testLoadInputStream() {
        // Edge case: empty input stream
        String baseUri = "https://www.example.com";
        try {
            Document document = DataUtil.load(new ByteArrayInputStream(new byte[0]), "UTF-8", baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        // Error scenario: invalid charset
        String baseUri = "https://www.example.com";
        try {
            Document document = DataUtil.load(new ByteArrayInputStream(new byte[0]), "invalidCharset", baseUri, Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected IOException
        }
    }

    @Test
    public void testCrossStreams() {
        // Typical use case
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            DataUtil.crossStreams(new ByteArrayInputStream("test".getBytes()), out);
            assertEquals("test", out.toString());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testParseByteData() {
        // Edge case: empty byte buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(0);
        String baseUri = "https://www.example.com";
        Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", baseUri, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        // Error scenario: invalid charset
        ByteBuffer byteBuffer = ByteBuffer.allocate(0);
        String baseUri = "https://www.example.com";
        try {
            Document document = DataUtil.parseByteData(byteBuffer, "invalidCharset", baseUri, Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected IOException
        }
    }

    @Test
    public void testReadToByteBuffer() {
        // Typical use case
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new ByteArrayInputStream("test".getBytes()), 0);
            assertNotNull(byteBuffer);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testReadFileToByteBuffer() {
        // Edge case: non-existent file
        try {
            ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(new File("nonExistentFile"));
            assertEquals(0, byteBuffer.capacity());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Typical use case
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        // Edge case: mime boundary generation
        String boundary = DataUtil.mimeBoundary();
        assertEquals(DataUtil.boundaryLength, boundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        // Typical use case
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 't', 'e', 's', 't'});
        String charset = DataUtil.detectCharsetFromBom(byteBuffer, null);
        assertEquals("UTF-8", charset);
    }
}