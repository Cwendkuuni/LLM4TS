import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.io.File;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(Charset.forName("UTF-8")));
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testInvalidCharsetLoad() {
        try {
            DataUtil.load(new File("test.html"), "InvalidCharset", "http://example.com");
            fail("Expected an IOException to be thrown");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] inputData = {10, 20, 30, 40, 50};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        DataUtil.crossStreams(inputStream, outputStream);
        byte[] outputData = outputStream.toByteArray();
        assertArrayEquals(inputData, outputData);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String content = "Test content for Byte Buffer";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);
        assertNotNull(byteBuffer);
        assertEquals(content, Charset.forName("UTF-8").decode(byteBuffer).toString());
    }

    @Test(expected = IOException.class)
    public void testIOExceptionOnReadToByteBufferWithNegativeSize() throws IOException {
        DataUtil.readToByteBuffer(new ByteArrayInputStream(new byte[0]), -1);
    }

    @Test(expected = IOException.class)
    public void testIOExceptionOnReadFileToByteBufferNonExistentFile() throws IOException {
        File file = new File("nonexistent.html");
        DataUtil.readFileToByteBuffer(file);
    }

    @Test
    public void testValidateCharset() {
        assertEquals("EUC-JP", DataUtil.validateCharset("EUC-JP"));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(32, boundary.length());
    }

}