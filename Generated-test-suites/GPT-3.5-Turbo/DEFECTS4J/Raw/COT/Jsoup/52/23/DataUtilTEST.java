
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {
    @Test
    public void testLoadFile() {
        try {
            File file = new File("test.html");
            Document doc = DataUtil.load(file, "UTF-8", "");
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            InputStream in = new FileInputStream("test.html");
            Document doc = DataUtil.load(in, "UTF-8", "");
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testLoadInputStreamParser() {
        try {
            InputStream in = new FileInputStream("test.html");
            Document doc = DataUtil.load(in, "UTF-8", "", new Parser());
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
        
    @Test
    public void testCrossStreams() {
        try {
            InputStream in = new FileInputStream("input.txt");
            OutputStream out = new FileOutputStream("output.txt");
            DataUtil.crossStreams(in, out);
            assertNotNull(out);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testParseByteData() {
        try {
            ByteBuffer byteData = ByteBuffer.allocate(1024);
            Document doc = DataUtil.parseByteData(byteData, "UTF-8", "", new Parser());
            assertNotNull(doc);
        } catch (IllegalCharsetNameException e) {
            fail("IllegalCharsetNameException occurred");
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream in = new FileInputStream("input.txt");
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in, 1024);
            assertNotNull(byteBuffer);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testReadToByteBufferMaxSize() {
        try {
            InputStream in = new FileInputStream("input.txt");
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in);
            assertNotNull(byteBuffer);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testReadFileToByteBuffer() {
        try {
            File file = new File("test.txt");
            ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
            assertNotNull(byteBuffer);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertNotNull(byteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
    }
    
    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        String charsetName = "UTF-8";
        String charset = DataUtil.detectCharsetFromBom(byteData, charsetName);
        assertEquals("UTF-8", charset);
    }
}
