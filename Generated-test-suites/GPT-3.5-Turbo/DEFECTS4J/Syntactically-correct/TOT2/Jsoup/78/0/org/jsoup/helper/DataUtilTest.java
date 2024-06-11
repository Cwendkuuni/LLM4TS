import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class DataUtilTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File("test.html");
        Document doc = DataUtil.load(testFile, "UTF-8", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");
        Document doc = DataUtil.load(in, "UTF-8", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamParser() throws IOException {
        InputStream in = new FileInputStream("test.html");
        Document doc = DataUtil.load(in, "UTF-8", "http://example.com", Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new FileInputStream("input.txt");
        OutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out);
        assertNotNull(out);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inStream = new FileInputStream("input.txt");
        ByteBuffer buffer = DataUtil.readToByteBuffer(inStream, 100);
        assertNotNull(buffer);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("file.txt");
        ByteBuffer buffer = DataUtil.readFileToByteBuffer(file);
        assertNotNull(buffer);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer buffer = DataUtil.emptyByteBuffer();
        assertEquals(0, buffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(32, boundary.length());
    }

}