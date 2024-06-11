import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File inputFile = new File("test.html");
        Document doc = DataUtil.load(inputFile, "UTF-8", null);
        Assert.assertNotNull(doc);
        // add assertions to check the document content here
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        Assert.assertNotNull(doc);
        // add assertions to check the document content here
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("ISO-8859-1"));
        try {
            Document doc = DataUtil.load(inputStream, "ISO-8859-1", null);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected, ignore
        }
    }

    @Test
    public void testLoadInputStreamWithEmptyContent() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Document doc = DataUtil.load(inputStream, null, null);
        Assert.assertNotNull(doc);
        // an empty document should be created in this case
    }

    @Test
    public void testParseInputStream() throws IOException {
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        // add assertions to check the document content here
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("ISO-8859-1"));
        try {
            Document doc = DataUtil.parseInputStream(inputStream, "ISO-8859-1", null, Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // expected, ignore
        }
    }

    @Test
    public void testParseInputStreamWithEmptyContent() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        // an empty document should be created in this case
    }

    @Test
    public void testCrossStreams() throws IOException {
        String sourceContent = "Source content";
        String targetContent = "Target content";

        ByteArrayInputStream inputStreamSource = new ByteArrayInputStream(sourceContent.getBytes("UTF-8"));
        ByteArrayOutputStream outputStreamTarget = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStreamSource, outputStreamTarget);
        String targetContentExpected = "Source content";
        Assert.assertEquals(targetContentExpected, outputStreamTarget.toString());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);

        byte[] expectedContent = htmlContent.getBytes("UTF-8");
        byte[] actualContent = new byte[byteBuffer.limit()];
        byteBuffer.get(actualContent);

        Assert.assertArrayEquals(expectedContent, actualContent);
    }

    @Test
    public void testReadToByteBufferWithEmptyContent() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);

        Assert.assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=GB2312";
        String expectedCharset = null;

        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(expectedCharset, charset);
    }

    @Test
    public void testValidateCharset() {
        String charset1 = "UTF-8";
        String charset2 = "UTF-8 (unsupported)";
        String charset3 = "GB2312";

        String validatedCharset1 = DataUtil.validateCharset(charset1);
        Assert.assertEquals(charset1, validatedCharset1);

        String validatedCharset2 = DataUtil.validateCharset(charset2);
        Assert.assertEquals(null, validatedCharset2);

        String validatedCharset3 = DataUtil.validateCharset(charset3);
        Assert.assertEquals(null, validatedCharset3);
    }

    @Test
    public void testMimeBoundary() {
        String expectedBoundary = "GBQQMH1jKwRv2zvMbQ3pq5uC3pC1zJJ1jqZ6bJQ";
        String boundary = DataUtil.mimeBoundary();
        Assert.assertEquals(expectedBoundary, boundary);
    }

    @Test
    public void testDetectCharsetFromBom() {
        String bomContentUTF8 = "\uFEFF<html>";
        String bomContentUTF16 = "\uFFFE\u0001<html>";
        String bomContentUTF32 = "\u0000\u0000\u0000\u0003<html>";

        ByteBuffer byteBufferUTF8 = Charset.forName("UTF-8").decode(bomContentUTF8);
        BomCharset bomUTF8 = DataUtil.detectCharsetFromBom(byteBufferUTF8);
        Assert.assertEquals("UTF-8", bomUTF8.charset);
        Assert.assertFalse(bomUTF8.offset);

        ByteBuffer byteBufferUTF16 = Charset.forName("UTF-16").decode(bomContentUTF16);
        BomCharset bomUTF16 = DataUtil.detectCharsetFromBom(byteBufferUTF16);
        Assert.assertEquals("UTF-16", bomUTF16.charset);
        Assert.assertFalse(bomUTF16.offset);

        ByteBuffer byteBufferUTF32 = Charset.forName("UTF-32").decode(bomContentUTF32);
        BomCharset bomUTF32 = DataUtil.detectCharsetFromBom(byteBufferUTF32);
        Assert.assertEquals("UTF-32", bomUTF32.charset);
        Assert.assertTrue(bomUTF32.offset);
    }
}