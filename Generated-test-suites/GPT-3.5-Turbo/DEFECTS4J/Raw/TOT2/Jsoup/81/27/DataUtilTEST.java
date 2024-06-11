import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws Exception {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws Exception {
        InputStream inputStream = new FileInputStream("test.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithParser() throws Exception {
        InputStream inputStream = new FileInputStream("test.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() {
        // Test case for crossStreams method (if applicable)
    }

    @Test
    public void testReadToByteBuffer() {
        // Test case for readToByteBuffer method (if applicable)
    }

    @Test
    public void testReadFileToByteBuffer() {
        // Test case for readFileToByteBuffer method (if applicable)
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(32, boundary.length());
    }

    @Test(expected = Throwable.class)
    public void testInvalidCharset() throws Exception {
        Document document = DataUtil.load(new File("test.html"), "invalidCharset", "http://example.com");
        // Expecting an exception to be thrown
    }
}