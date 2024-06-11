
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DataUtilTest {
    private static final String BASE_URI = "http://example.com";

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test-data.html");
        Document doc = DataUtil.load(file, "UTF-8", BASE_URI);
        assertNotNull(doc);
        assertEquals(BASE_URI, doc.baseUri());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test-data.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", BASE_URI);
        assertNotNull(doc);
        assertEquals(BASE_URI, doc.baseUri());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("test-data.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", BASE_URI, Jsoup.parser());
        assertNotNull(doc);
        assertEquals(BASE_URI, doc.baseUri());
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new FileInputStream("test-image.png");
        OutputStream outputStream = new FileOutputStream("test-image-copy.png");
        DataUtil.crossStreams(inputStream, outputStream);
        outputStream.close();

        File file1 = new File("test-image.png");
        File file2 = new File("test-image-copy.png");

        assertTrue(file1.exists());
        assertTrue(file2.exists());
        assertEquals(file1.length(), file2.length());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream("test-data.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteBuffer);
        assertTrue(byteBuffer.capacity() > 0);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test-data.html");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        assertNotNull(byteBuffer);
        assertTrue(byteBuffer.capacity() > 0);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
}
