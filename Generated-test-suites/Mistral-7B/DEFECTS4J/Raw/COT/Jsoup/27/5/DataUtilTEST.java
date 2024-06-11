import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // Replace this with a valid file path
        Document doc = DataUtil.load(file, "UTF-8", "");
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullInput() {
        File file = null;
        try {
            Document doc = DataUtil.load(file, "UTF-8", "");
        } catch (IOException e) {
            // Expected exception, test passes if this block is reached
        }
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testData = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullInput() {
        InputStream inputStream = null;
        try {
            Document doc = DataUtil.load(inputStream, "UTF-8", "");
        } catch (IOException e) {
            // Expected exception, test passes if this block is reached
        }
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String testData = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-16BE"));
        Document doc = null;
        try {
            doc = DataUtil.load(inputStream, "UTF-16BE", "");
        } catch (IOException e) {
            // Expected exception, test passes if this block is reached
        }
        Assert.assertNull(doc);
    }

    @Test
    public void testParseByteData() throws IOException {
        String testData = "<html><head></head><body></body></html>";
        byte[] byteData = testData.getBytes();
        Document doc = null;
        try (InputStream inputStream = new ByteArrayInputStream(byteData)) {
            doc = DataUtil.load(inputStream, "UTF-8", "");
        }
        Document parsedDoc = DataUtil.parseByteData(ByteBuffer.wrap(byteData), null, "", Parser.htmlParser());
        Assert.assertEquals(doc.html(), parsedDoc.html());
    }

    @Test
    public void testParseByteDataWithValidCharset() throws IOException {
        String testData = "<html><head><meta charset=\"UTF-16BE\"></head><body></body></html>";
        byte[] byteData = testData.getBytes("UTF-16BE");
        Document doc = null;
        try (InputStream inputStream = new ByteArrayInputStream(byteData)) {
            doc = DataUtil.load(inputStream, null, "");
        }
        Document parsedDoc = DataUtil.parseByteData(ByteBuffer.wrap(byteData), "UTF-16BE", "", Parser.htmlParser());
        Assert.assertEquals(doc.html(), parsedDoc.html());
    }

    @Test(expected = IOException.class)
    public void testParseByteDataWithEmptyByteBuffer() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(0);
        Document doc = DataUtil.parseByteData(byteBuffer, null, "", Parser.htmlParser());
        Assert.assertNull(doc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseByteDataWithNullParser() {
        byte[] byteData = new byte[0];
        Document doc = null;
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(byteData)) {
            doc = DataUtil.load(inputStream, null, "");
        }
        try {
            Document parsedDoc = DataUtil.parseByteData(ByteBuffer.wrap(byteData), null, "", null);
        } catch (IOException e) {
            // Expected exception from load method, test passes if this block is reached
        } catch (IllegalArgumentException expectedException) {
            // Expected exception, test passes if this block is reached
        }
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        Assert.assertNotNull(byteBuffer);
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithNullInput() {
        InputStream inputStream = null;
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        } catch (IOException e) {
            // Expected exception, test passes if this block is reached
        }
    }
}