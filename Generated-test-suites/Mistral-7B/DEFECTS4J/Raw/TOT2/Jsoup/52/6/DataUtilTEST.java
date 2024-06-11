import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "This is a test file.";
        String charset = "UTF-8";
        File inputFile = new File("test.html");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteBuffer byteData = ByteBuffer.wrap(fileContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputFile, charset, "");
        Assert.assertNotNull(doc);

        ByteArrayOutputStream expectedOutput = new ByteArrayOutputStream();
        DataUtil.crossStreams(new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8)), expectedOutput);
        Document expectedDoc = DataUtil.parseByteData(ByteBuffer.wrap(expectedOutput.toByteArray()), charset, "", DataUtil.Parser.htmlParser());

        Assert.assertEquals(expectedDoc.html(), doc.html());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamData = "This is a test input stream.";
        String charset = "UTF-8";

        InputStream inputStream = new ByteArrayInputStream(inputStreamData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, charset, "");
        Assert.assertNotNull(doc);

        Assert.assertEquals("This is a test input stream.", doc.text());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String inputStreamData = "This is a test input stream.";
        String charset = "INVALID_CHARSET";

        InputStream inputStream = new ByteArrayInputStream(inputStreamData.getBytes(StandardCharsets.UTF_8));
        try {
            Document doc = DataUtil.load(inputStream, charset, "");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testParseByteData() throws IOException {
        String fileContent = "This is a test file.";
        String charset = "UTF-8";
        ByteBuffer byteData = ByteBuffer.wrap(fileContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseByteData(byteData, charset, "", DataUtil.Parser.htmlParser());
        Assert.assertNotNull(doc);

        Assert.assertEquals("This is a test file.", doc.text());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        String fileContent = "This is a test file.";
        String charset = "INVALID_CHARSET";

        ByteBuffer byteData = ByteBuffer.wrap(fileContent.getBytes(StandardCharsets.UTF_8));
        try {
            Document doc = DataUtil.parseByteData(byteData, charset, "", DataUtil.Parser.htmlParser());
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF});
        String charset = DataUtil.detectCharsetFromBom(byteData, "");
        Assert.assertEquals("UTF-32", charset);

        byteData.rewind();
        charset = DataUtil.detectCharsetFromBom(byteData, "");
        Assert.assertEquals("UTF-16 BE", charset);

        byteData.rewind();
        charset = DataUtil.detectCharsetFromBom(byteData, "");
        Assert.assertEquals("UTF-8", charset);

        byteData.rewind();
        charset = DataUtil.detectCharsetFromBom(byteData, "");
        Assert.assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);

        contentType = "text/html; charset=UTF-16";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-16", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset); // Default charset

        contentType = "text/html; charset=INVALID_CHARSET";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertTrue(boundary.length() > 0);
        Assert.assertFalse(boundary.isEmpty());
    }
}