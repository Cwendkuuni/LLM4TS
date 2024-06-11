import org.junit.Assert;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFileDocument() throws IOException {
        File file = new File("test.html"); // Replace with an actual file path
        Document document = DataUtil.load(file, "UTF-8", null);
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileDocumentInvalidCharset() throws IOException {
        File file = new File("test.html"); // Replace with an actual file path
        Document document = null;
        try {
            document = DataUtil.load(file, "invalidCharset", null);
        } catch (IOException e) {
        }
        Assert.assertNull(document);
    }

    @Test
    public void testLoadInputStreamDocument() throws IOException {
        String testData = "Test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamDocumentInvalidCharset() throws IOException {
        String testData = "Test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        Document document = null;
        try {
            document = DataUtil.load(inputStream, "invalidCharset", null);
        } catch (IOException e) {
        }
        Assert.assertNull(document);
    }

    @Test
    public void testLoadInputStreamDocumentParser() throws IOException {
        String testData = "Test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferNullInputStream() throws IOException {
        InputStream inputStream = null;
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Assert.assertNull(byteData);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "Test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Assert.assertNotNull(byteData);
    }

    @Test
    public void testReadToByteBufferMaxSize() throws IOException {
        String testData = "Test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 10);
        Assert.assertNotNull(byteData);
        Assert.assertEquals(10, byteData.limit());
    }

    @Test
    public void testParseByteData() throws IOException {
        String testData = "Test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithCharset() throws IOException {
        String testData = "Test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Document document = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String testData = "Test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Document document = DataUtil.parseByteData(byteData, "invalidCharset", null, Parser.htmlParser());
        Assert.assertNull(document);
    }
}