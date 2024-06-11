
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        File file = new File("example.html");
        try {
            Document document = DataUtil.load(file, "UTF-8", "http://example.com");
            Assert.assertNotNull(document);
            // Additional assertions for the loaded document
        } catch (IOException e) {
            Assert.fail("IOException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/example.html");
            Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
            Assert.assertNotNull(document);
            // Additional assertions for the loaded document
        } catch (IOException e) {
            Assert.fail("IOException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testLoadWithParser() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/example.xml");
            Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
            Assert.assertNotNull(document);
            // Additional assertions for the loaded document
        } catch (IOException e) {
            Assert.fail("IOException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCrossStreams() {
        try {
            // Set up input and output streams for testing
            InputStream inputStream = getClass().getResourceAsStream("/file_input.txt");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            DataUtil.crossStreams(inputStream, outputStream);

            byte[] expectedBytes = "Hello, World!".getBytes(Charset.forName("UTF-8"));
            byte[] actualBytes = outputStream.toByteArray();

            Assert.assertArrayEquals(expectedBytes, actualBytes);
        } catch (IOException e) {
            Assert.fail("IOException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testParseInputStream() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/example.html");
            Document document = DataUtil.parseInputStream(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
            Assert.assertNotNull(document);
            // Additional assertions for the parsed document
        } catch (IOException e) {
            Assert.fail("IOException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/example.txt");
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
            Assert.assertNotNull(byteBuffer);
            // Additional assertions for the read byte buffer
        } catch (IOException e) {
            Assert.fail("IOException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testReadFileToByteBuffer() {
        try {
            File file = new File("example.txt");
            ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
            Assert.assertNotNull(byteBuffer);
            // Additional assertions for the read byte buffer
        } catch (IOException e) {
            Assert.fail("IOException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        Assert.assertNotNull(byteBuffer);
        Assert.assertEquals(0, byteBuffer.remaining());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNotNull(charset);
        Assert.assertEquals("UTF-8", charset);
    }
}
