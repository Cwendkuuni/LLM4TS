import org.junit.Assert;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "This is a test file.<meta charset=\"UTF-8\">";
        File testFile = new File("testFile.html");
        FileUtils.writeStringToFile(testFile, fileContent, "UTF-8");

        Document doc = DataUtil.load(testFile, "UTF-8", null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("This is a test file.", doc.body().text());

        FileUtils.deleteQuietly(testFile);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String fileContent = "This is a test file.<meta charset=\"UTF-8\">";
        InputStream testInput = new ByteArrayInputStream(fileContent.getBytes("UTF-8"));

        Document doc = DataUtil.load(testInput, "UTF-8", null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("This is a test file.", doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String fileContent = "This is a test file.<meta charset=\"UTF-8\">";
        InputStream testInput = new ByteArrayInputStream(fileContent.getBytes("UTF-8"));

        Document doc = DataUtil.load(testInput, null, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("This is a test file.", doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String fileContent = "This is a test file.<meta charset=\"UTF-8\">";
        InputStream testInput = new ByteArrayInputStream(fileContent.getBytes("UTF-8"));

        try {
            Document doc = DataUtil.load(testInput, "invalidCharset", null);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadInputStreamWithEmptyContent() throws IOException {
        InputStream testInput = new ByteArrayInputStream(new byte[0]);

        try {
            Document doc = DataUtil.load(testInput, null, null);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testParseByteData() throws IOException {
        String fileContent = "This is a test file.<meta charset=\"UTF-8\">";
        byte[] testData = fileContent.getBytes("UTF-8");

        ByteBuffer byteData = ByteBuffer.wrap(testData);
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("This is a test file.", doc.body().text());
    }

    @Test
    public void testParseByteDataWithNullCharset() {
        byte[] testData = "This is a test file.<meta charset=\"UTF-8\">".getBytes();
        ByteBuffer byteData = ByteBuffer.wrap(testData);

        try {
            Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String fileContent = "This is a test file.<meta charset=\"UTF-8\">";
        byte[] testData = fileContent.getBytes("UTF-8");

        ByteBuffer byteData = ByteBuffer.wrap(testData);
        byteData.rewind();
        String charset = "invalidCharset";

        try {
            Document doc = DataUtil.parseByteData(byteData, charset, null, Parser.htmlParser());
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String fileContent = "This is a test file.<meta charset=\"UTF-8\">";
        InputStream testInput = new ByteArrayInputStream(fileContent.getBytes("UTF-8"));

        ByteBuffer byteData = DataUtil.readToByteBuffer(testInput);
        byte[] expectedData = fileContent.getBytes("UTF-8");

        // compare byte arrays (not byte buffers) to ensure they are equal
        byte[] buffer = new byte[byteData.limit()];
        byteData.get(buffer);
        Assert.assertArrayEquals(expectedData, buffer);
    }

    @Test
    public void testReadToByteBufferWithEmptyInput() throws IOException {
        InputStream testInput = new ByteArrayInputStream(new byte[0]);

        ByteBuffer byteData = DataUtil.readToByteBuffer(testInput);
        Assert.assertEquals(0, byteData.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentType = "text/html";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }
}