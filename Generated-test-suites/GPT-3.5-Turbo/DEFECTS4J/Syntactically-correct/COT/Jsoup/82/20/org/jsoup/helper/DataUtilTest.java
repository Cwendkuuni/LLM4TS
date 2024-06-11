import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        try {
            Document doc = DataUtil.load(new File("file.html"), "UTF-8", "http://example.com");
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            FileInputStream in = new FileInputStream("file.html");
            Document doc = DataUtil.load(in, "UTF-8", "http://example.com");
            assertNotNull(doc);
            in.close();
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        try {
            FileInputStream in = new FileInputStream("file.xml");
            Document doc = DataUtil.load(in, "UTF-8", "http://example.com", Parser.xmlParser());
            assertNotNull(doc);
            in.close();
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testCrossStreams() {
        try {
            FileInputStream in = new FileInputStream("input.txt");
            FileOutputStream out = new FileOutputStream("output.txt");
            DataUtil.crossStreams(in, out);
            in.close();
            out.close();
            File output = new File("output.txt");
            assertTrue(output.exists());
            output.delete();
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testParseInputStream() {
        try {
            FileInputStream in = new FileInputStream("file.html");
            Document doc = DataUtil.parseInputStream(in, "UTF-8", "http://example.com", Parser.htmlParser());
            assertNotNull(doc);
            in.close();
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            FileInputStream in = new FileInputStream("file.html");
            ByteBuffer buffer = DataUtil.readToByteBuffer(in, 1024);
            assertNotNull(buffer);
            in.close();
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testMimeTypeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(32, boundary.length());
    }
}