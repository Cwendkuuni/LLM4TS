import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");
        Document doc = DataUtil.load(in, "UTF-8", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream in = new FileInputStream("test.html");
        Document doc = DataUtil.load(in, "UTF-8", "http://example.com", Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new FileInputStream("test.txt");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out);
        assertTrue(out.size()>0);
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("test data".getBytes());
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream in = new FileInputStream("test.txt");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in);
        assertNotNull(byteBuffer);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.txt");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        assertNotNull(byteBuffer);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(32, boundary.length());
    }
}