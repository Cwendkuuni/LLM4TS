import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.jsoup.helper.DataUtil;

public class DataUtilTest {
    @Test
    public void testLoad() {
        // Test case 1: File is null
        try {
            DataUtil.load(null, "UTF-8", "https://www.example.com");
        } catch (Exception e) {
            assertNull(e.getMessage());
        }

        // Test case 2: Valid file and charset
        try {
            DataUtil.load(new File("test.html"), "UTF-8", "https://www.example.com");
        } catch (Exception e) {
            assertNull(e.getMessage());
        }
    }

    @Test
    public void testParseInputStream() {
        // Test case 1: InputStream is null
        try {
            DataUtil.parseInputStream(null, "UTF-8", "https://www.example.com", Parser.htmlParser());
        } catch (Exception e) {
            assertNull(e.getMessage());
        }

        // Test case 2: Valid InputStream, charset and baseUri
        try {
            DataUtil.parseInputStream(new FileInputStream("test.html"), "UTF-8", "https://www.example.com", Parser.htmlParser());
        } catch (Exception e) {
            assertNull(e.getMessage());
        }
    }

    @Test
    public void testCrossStreams() {
        // Test case 1: InputStream is null
        try {
            DataUtil.crossStreams(null, System.out);
        } catch (Exception e) {
            assertNull(e.getMessage());
        }

        // Test case 2: Valid InputStream and OutputStream
        try {
            DataUtil.crossStreams(new FileInputStream("input.txt"), System.out);
        } catch (Exception e) {
            assertNull(e.getMessage());
        }
    }

    @Test
    public void testReadToByteBuffer() {
        // Test case 1: InputStream is null
        try {
            DataUtil.readToByteBuffer(null, 1024);
        } catch (Exception e) {
            assertNull(e.getMessage());
        }

        // Test case 2: Valid InputStream and maxSize
        try {
            DataUtil.readToByteBuffer(new FileInputStream("input.txt"), 1024);
        } catch (Exception e) {
            assertNull(e.getMessage());
        }
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer buffer = DataUtil.emptyByteBuffer();
        assertEquals(0, buffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Test case 1: Content-Type is null
        String charset = DataUtil.getCharsetFromContentType(null);
        assertNull(charset);

        // Test case 2: Content-Type doesn't have charset
        charset = DataUtil.getCharsetFromContentType("text/html");
        assertNull(charset);

        // Test case 3: Valid Content-Type with charset
        charset = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testValidateCharset() {
        // Test case 1: Charset is null
        String charset = DataUtil.validateCharset(null);
        assertNull(charset);

        // Test case 2: Charset is not supported
        charset = DataUtil.validateCharset("invalidCharset");
        assertNull(charset);

        // Test case 3: Charset is supported
        charset = DataUtil.validateCharset("UTF-8");
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertEquals(DataUtil.boundaryLength, boundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        // Test case 1: ByteBuffer is empty
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(DataUtil.emptyByteBuffer());
        assertNull(bomCharset);

        // Test case 2: Valid ByteBuffer with BOM
        byte[] bom = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        ByteBuffer byteBuffer = ByteBuffer.wrap(bom);
        bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertEquals("UTF-8", bomCharset.charset);
        assertEquals(true, bomCharset.offset);
    }
}