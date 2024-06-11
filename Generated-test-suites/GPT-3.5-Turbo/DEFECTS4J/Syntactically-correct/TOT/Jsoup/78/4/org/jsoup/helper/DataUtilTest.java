import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {

    @Test
    public void testLoad_withValidFile_ReturnsDocument() {
        File file = new File("test.html");

        try {
            Document document = DataUtil.load(file, "UTF-8", "");
            Assert.assertNotNull(document);
        } catch (IOException e) {
            Assert.fail("IOException occurred while loading the file");
        }
    }

    @Test
    public void testLoad_withInvalidFile_ThrowsIOException() {
        File file = new File("nonexistent.html");

        try {
            Document document = DataUtil.load(file, "UTF-8", "");
            Assert.fail("IOException should have been thrown");
        } catch (IOException e) {
            // Exception expected
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testLoad_withNullInputStream_ThrowsIOException() {
        InputStream inputStream = null;

        try {
            Document document = DataUtil.load(inputStream, "UTF-8", "");
            Assert.fail("IOException should have been thrown");
        } catch (IOException e) {
            // Exception expected
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testCrossStreams_withValidStreams_DoesNotThrowException() {
        InputStream inputStream = new FileInputStream("test.html");
        OutputStream outputStream = null; // Output stream implementation is not provided in the code, assuming it exists

        try {
            DataUtil.crossStreams(inputStream, outputStream);
            // No exception thrown, test passes
            Assert.assertTrue(true);
        } catch (IOException e) {
            Assert.fail("IOException occurred while crossing streams");
        }
    }

    @Test
    public void testCrossStreams_withNullInputStream_ThrowsIOException() {
        InputStream inputStream = null;
        OutputStream outputStream = null; // Output stream implementation is not provided in the code, assuming it exists

        try {
            DataUtil.crossStreams(inputStream, outputStream);
            Assert.fail("IOException should have been thrown");
        } catch (IOException e) {
            // Exception expected
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testParseInputStream_withValidInputStream_ReturnsDocument() {
        InputStream inputStream = new FileInputStream("test.html");
        
        try {
            Document document = DataUtil.parseInputStream(inputStream, "UTF-8", "", Jsoup.parser());
            Assert.assertNotNull(document);
        } catch (IOException e) {
            Assert.fail("IOException occurred while parsing the input stream");
        }
    }

    @Test
    public void testParseInputStream_withInvalidInputStream_ThrowsIOException() {
        InputStream inputStream = null;

        try {
            Document document = DataUtil.parseInputStream(inputStream, "UTF-8", "", Jsoup.parser());
            Assert.fail("IOException should have been thrown");
        } catch (IOException e) {
            // Exception expected
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testReadToByteBuffer_withValidInputStream_ReturnsByteBuffer() {
        InputStream inputStream = new FileInputStream("test.html");

        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 100);
            Assert.assertNotNull(byteBuffer);
        } catch (IOException e) {
            Assert.fail("IOException occurred while reading to byte buffer");
        }
    }

    @Test
    public void testReadToByteBuffer_withInvalidInputStream_ThrowsIOException() {
        InputStream inputStream = null;

        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 100);
            Assert.fail("IOException should have been thrown");
        } catch (IOException e) {
            // Exception expected
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testReadToByteBuffer_withNegativeMaxSize_ThrowsIllegalArgumentException() {
        InputStream inputStream = new FileInputStream("test.html");
        int maxSize = -1;

        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
            Assert.assertTrue(true);
        } catch (IOException e) {
            Assert.fail("IOException occurred while reading to byte buffer");
        }
    }

    @Test
    public void testEmptyByteBuffer_ReturnsEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        Assert.assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType_withValidContentType_ReturnsCharset() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_withInvalidContentType_ReturnsNull() {
        String contentType = "text/html;";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testMimeBoundary_ReturnsValidMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(mimeBoundary);
        Assert.assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }
}