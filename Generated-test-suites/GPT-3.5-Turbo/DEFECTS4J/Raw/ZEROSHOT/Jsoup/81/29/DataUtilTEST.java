import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() {
        File file = new File("test.html");
        try {
            Document document = DataUtil.load(file, "UTF-8", "http://example.com");
            Assert.assertNotNull(document);
        } catch (IOException e) {
            Assert.fail("Failed to load file");
        }
    }

    @Test
    public void testLoadFromInputStream() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
            Assert.assertNotNull(document);
        } catch (IOException e) {
            Assert.fail("Failed to load input stream");
        }
    }

    @Test
    public void testLoadFromInputStreamWithParser() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
            Assert.assertNotNull(document);
        } catch (IOException e) {
            Assert.fail("Failed to load input stream with parser");
        }
    }

    @Test
    public void testCrossStreams() {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream("input.txt");
            outputStream = new FileOutputStream("output.txt");
            DataUtil.crossStreams(inputStream, outputStream);
        } catch (IOException e) {
            Assert.fail("Failed to cross streams");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream inputStream = new FileInputStream("test.txt");
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
            Assert.assertNotNull(byteBuffer);
            Assert.assertTrue(byteBuffer.capacity() > 0);
        } catch (IOException e) {
            Assert.fail("Failed to read to byte buffer");
        }
    }

    @Test
    public void testReadFileToByteBuffer() {
        try {
            File file = new File("test.txt");
            ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
            Assert.assertNotNull(byteBuffer);
            Assert.assertTrue(byteBuffer.capacity() > 0);
        } catch (IOException e) {
            Assert.fail("Failed to read file to byte buffer");
        }
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(boundary);
        Assert.assertEquals(DataUtil.boundaryLength, boundary.length());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String charset = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        Assert.assertNotNull(charset);
        Assert.assertEquals("UTF-8", charset);
    }
}