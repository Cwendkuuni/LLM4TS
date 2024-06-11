
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class DataUtilTest {

    @Test
    public void testLoadWithFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "http://example.com");
        Assert.assertEquals("Example Domain", document.title());
    }

    @Test
    public void testLoadWithInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        inputStream.close();
        Assert.assertEquals("Example Domain", document.title());
    }

    @Test
    public void testLoadWithInputStreamAndParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", parser);
        inputStream.close();
        Assert.assertEquals("Example Domain", document.title());
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        inputStream.close();

        byte[] expectedBytes = Jsoup.connect("http://example.com").execute().bodyAsBytes();
        byte[] actualBytes = outputStream.toByteArray();

        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testParseByteDataWithCharset() {
        // TODO: Implement test case after setting up test data
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
        inputStream.close();

        Assert.assertEquals(1024, byteBuffer.capacity());
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        inputStream.close();

        Assert.assertTrue(byteBuffer.capacity() > 0);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);

        Assert.assertTrue(byteBuffer.capacity() > 0);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        Assert.assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();

        Assert.assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }
}
