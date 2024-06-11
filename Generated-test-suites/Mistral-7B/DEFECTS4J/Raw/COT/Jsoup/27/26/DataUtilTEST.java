import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String filePath = "path/to/your/file.html";
        File file = new File(filePath);
        Document document = DataUtil.load(file, "UTF-8", "");
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "Your HTML content here";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8", "");
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "Your HTML content here";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.ISO_8859_1));
        try {
            Document document = DataUtil.load(inputStream, null, "");
            Assert.fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String htmlContent = "Your HTML content here";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        try {
            Document document = DataUtil.load(inputStream, null, "");
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInputStreamWithEmptyContentType() throws IOException {
        String htmlContent = "Your HTML content here";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, null, "");
        String charset = DataUtil.getCharsetFromContentType(null);
        Assert.assertNull(charset);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithInvalidContentType() throws IOException {
        String htmlContent = "text/plain; charset=UTF-8\nYour HTML content here";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = null;
        try {
            document = DataUtil.load(inputStream, null, "");
        } catch (IOException e) {
            // Expected
        }
        Assert.assertNull(document);
    }

    @Test
    public void testLoadInputStreamWithValidContentType() throws IOException {
        String htmlContent = "text/html; charset=UTF-8\nYour HTML content here";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, null, "");
        Assert.assertNotNull(document);
        String charset = DataUtil.getCharsetFromContentType(htmlContent);
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testParseByteDataWithUTF8() throws IOException {
        String htmlContent = "Your HTML content here";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteData, "UTF-8", "", Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithGB2312() throws IOException {
        String htmlContent = "Your HTML content here";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = null;
        try {
            document = DataUtil.parseByteData(byteData, "GB2312", "", Parser.htmlParser());
        } catch (IOException e) {
            // Expected
        }
        Assert.assertNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String htmlContent = "Your HTML content here";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Assert.assertNotNull(byteData);
        byte[] expectedBytes = htmlContent.getBytes(StandardCharsets.UTF_8);
        byte[] actualBytes = new byte[byteData.limit()];
        byteData.get(actualBytes);
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }
}