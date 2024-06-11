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
    public void testLoad() {
        try {
            File file = new File("test.html");
            Document document = DataUtil.load(file, "UTF-8", file.getAbsolutePath());
            Assert.assertNotNull(document);
        } catch (IOException e) {
            Assert.fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testLoad_InputStream() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            Document document = DataUtil.load(inputStream, "UTF-8", "test.html");
            Assert.assertNotNull(document);
        } catch (IOException e) {
            Assert.fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testLoad_InputStream_Parser() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            Document document = DataUtil.load(inputStream, "UTF-8", "test.html", Parser.htmlParser());
            Assert.assertNotNull(document);
        } catch (IOException e) {
            Assert.fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testCrossStreams() {
        try {
            InputStream inputStream = new FileInputStream("input.txt");
            OutputStream outputStream = new FileOutputStream("output.txt");
            DataUtil.crossStreams(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            Assert.fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testParseInputStream() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            Document document = DataUtil.parseInputStream(inputStream, "UTF-8", "test.html", Parser.htmlParser());
            Assert.assertNotNull(document);
        } catch (IOException e) {
            Assert.fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream inputStream = new FileInputStream("input.txt");
            ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream, 1024);
            Assert.assertNotNull(buffer);
        } catch (IOException e) {
            Assert.fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testReadToByteBuffer_InputStream() {
        try {
            InputStream inputStream = new FileInputStream("input.txt");
            ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream);
            Assert.assertNotNull(buffer);
        } catch (IOException e) {
            Assert.fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testReadFileToByteBuffer() {
        try {
            File file = new File("test.html");
            ByteBuffer buffer = DataUtil.readFileToByteBuffer(file);
            Assert.assertNotNull(buffer);
        } catch (IOException e) {
            Assert.fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer buffer = DataUtil.emptyByteBuffer();
        Assert.assertEquals(0, buffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = DataUtil.validateCharset("UTF-8");
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(mimeBoundary);
        Assert.assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        byte[] bom = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        ByteBuffer buffer = ByteBuffer.wrap(bom);
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(buffer);
        Assert.assertEquals("UTF-8", bomCharset.charset);
        Assert.assertTrue(bomCharset.offset);
    }
}