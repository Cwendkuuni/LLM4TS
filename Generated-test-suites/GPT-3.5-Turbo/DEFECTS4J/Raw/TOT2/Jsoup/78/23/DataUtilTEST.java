import org.junit.Test;
import org.junit.Assert;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        try {
            File file = new File("test.html");
            Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
            Assert.assertNotNull(doc);
        } catch (IOException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
            Assert.assertNotNull(doc);
        } catch (IOException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        } 
    }

    @Test
    public void testLoadInputStreamWithParser() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
            Assert.assertNotNull(doc);
        } catch (IOException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        } 
    }

    @Test
    public void testCrossStreams() {
        try {
            InputStream in = new FileInputStream("input.txt");
            OutputStream out = System.out;
            DataUtil.crossStreams(in, out);
        } catch (IOException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            File file = new File("test.html");
            InputStream inputStream = new FileInputStream(file);
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
            Assert.assertNotNull(byteBuffer);
        } catch (IOException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=utf-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("utf-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(mimeBoundary);
    }
}