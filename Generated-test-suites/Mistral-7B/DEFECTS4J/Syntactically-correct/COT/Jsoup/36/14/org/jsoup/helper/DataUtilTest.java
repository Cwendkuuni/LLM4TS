import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head></head><body></body></html>";
        File testFile = new File("test.html");
        Files.write(testFile.getAbsoluteFile(), fileContent.getBytes());

        Document doc = DataUtil.load(testFile, "UTF-8", null);
        Assert.assertNotNull(doc);

        Files.deleteIfExists(testFile.getAbsoluteFile());
    }

    @Test
    public void testLoadFromInputStreamWithDefaultCharset() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head></head><body></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        Document doc = DataUtil.load(testInputStream, null, null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFromInputStreamWithSpecifiedCharset() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head></head><body></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        Document doc = DataUtil.load(testInputStream, "UTF-8", null);
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFromInputStreamWithNullCharset() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head></head><body></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        DataUtil.load(testInputStream, null, null);
    }

    @Test(expected = IOException.class)
    public void testLoadFromInputStreamWithInvalidCharset() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head></head><body></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        DataUtil.load(testInputStream, " invalidCharset ", null);
    }

    @Test(expected = IOException.class)
    public void testLoadFromInputStreamWithNegativeBufferSize() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head></head><body></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        DataUtil.load(testInputStream, null, null, Parser.htmlParser(), -1);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String byteData = "<html><head></head><body></body></html>";
        ByteBuffer testByteBuffer = ByteBuffer.wrap(byteData.getBytes(Charset.forName("UTF-8")));

        Document doc = DataUtil.parseByteData(testByteBuffer, null, null, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String byteData = "<html><head></head><body></body></html>";
        ByteBuffer testByteBuffer = ByteBuffer.wrap(byteData.getBytes(Charset.forName("UTF-8")));

        Document doc = DataUtil.parseByteData(testByteBuffer, "UTF-8", null, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithNullCharset() {
        String byteData = "<html><head></head><body></body></html>";
        ByteBuffer testByteBuffer = ByteBuffer.wrap(byteData.getBytes(Charset.forName("UTF-8")));

        Document doc = DataUtil.parseByteData(testByteBuffer, null, null, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        String byteData = "<html><head></head><body></body></html>";
        ByteBuffer testByteBuffer = ByteBuffer.wrap(byteData.getBytes(Charset.forName("UTF-8")));

        Document doc = DataUtil.parseByteData(testByteBuffer, "invalidCharset", null, Parser.htmlParser());
        Assert.assertNull(doc);
    }

    @Test
    public void testReadToByteBufferWithValidFile() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head></head><body></body></html>";
        File testFile = new File("test.html");
        Files.write(testFile.getAbsoluteFile(), fileContent.getBytes());

        ByteBuffer result = DataUtil.readToByteBuffer(new FileInputStream(testFile), 0);
        Assert.assertNotNull(result);

        Files.deleteIfExists(testFile.getAbsoluteFile());
    }

    @Test
    public void testReadToByteBufferWithNullInputStream() {
        ByteBuffer result = DataUtil.readToByteBuffer(null, 0);
        Assert.assertNull(result);
    }

    @Test
    public void testReadToByteBufferWithValidInputStream() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head></head><body></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        ByteBuffer result = DataUtil.readToByteBuffer(testInputStream, 0);
        Assert.assertNotNull(result);

        testInputStream.close();
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head></head><body></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        ByteBuffer result = DataUtil.readToByteBuffer(testInputStream, 10);
        Assert.assertEquals(10, result.limit());

        testInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithNegativeBufferSize() throws IOException {
        InputStream testInputStream = new ByteArrayInputStream("test".getBytes());

        DataUtil.readToByteBuffer(testInputStream, -1);
    }
}