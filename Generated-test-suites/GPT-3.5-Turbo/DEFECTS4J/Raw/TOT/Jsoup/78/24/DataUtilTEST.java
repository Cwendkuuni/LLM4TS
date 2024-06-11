import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.charset.Charset;

public class DataUtilTest {

    // Test case for load(File in, String charsetName, String baseUri) method
    @Test
    public void testLoadFile() {
        try {
            File file = new File("test.html");
            Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
            assertNotNull(doc);
            assertEquals("http://example.com", doc.baseUri());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    // Test case for load(InputStream in, String charsetName, String baseUri) method
    @Test
    public void testLoadInputStream() {
        try {
            InputStream in = new FileInputStream("test.html");
            Document doc = DataUtil.load(in, "UTF-8", "http://example.com");
            assertNotNull(doc);
            assertEquals("http://example.com", doc.baseUri());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    // Test case for load(InputStream in, String charsetName, String baseUri, Parser parser) method
    @Test
    public void testLoadInputStreamWithParser() {
        try {
            InputStream in = new FileInputStream("test.xml");
            Document doc = DataUtil.load(in, "UTF-8", "http://example.com", Parser.xmlParser());
            assertNotNull(doc);
            assertEquals("http://example.com", doc.baseUri());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    // Test case for crossStreams(InputStream in, OutputStream out) method
    @Test
    public void testCrossStreams() {
        try {
            InputStream in = new FileInputStream("input.txt");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DataUtil.crossStreams(in, out);
            byte[] expected = "This is a test input.".getBytes(Charset.forName("UTF-8"));
            byte[] actual = out.toByteArray();
            assertArrayEquals(expected, actual);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    // Test case for readToByteBuffer(InputStream inStream, int maxSize) method
    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream in = new FileInputStream("input.txt");
            ByteBuffer buffer = DataUtil.readToByteBuffer(in, 100);
            assertNotNull(buffer);
            assertEquals(19, buffer.capacity());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    // Test case for readFileToByteBuffer(File file) method
    @Test
    public void testReadFileToByteBuffer() {
        try {
            File file = new File("input.txt");
            ByteBuffer buffer = DataUtil.readFileToByteBuffer(file);
            assertNotNull(buffer);
            assertEquals(20, buffer.capacity());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    // Test case for emptyByteBuffer() method
    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer buffer = DataUtil.emptyByteBuffer();
        assertNotNull(buffer);
        assertEquals(0, buffer.capacity());
    }

    // Test case for getCharsetFromContentType(String contentType) method
    @Test
    public void testGetCharsetFromContentType() {
        String charset = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    // Test case for validateCharset(String cs) method
    @Test
    public void testValidateCharset() {
        String validatedCharset = DataUtil.validateCharset("UTF-8");
        assertNotNull(validatedCharset);
        assertEquals("UTF-8", validatedCharset);
    }

    // Test case for mimeBoundary() method
    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(32, boundary.length());
    }

    // Test case for detectCharsetFromBom(ByteBuffer byteData) method
    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.put((byte) 0xEF);
        byteBuffer.put((byte) 0xBB);
        byteBuffer.put((byte) 0xBF);
        byteBuffer.rewind();
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertNotNull(bomCharset);
        assertEquals("UTF-8", bomCharset.charset);
    }
}