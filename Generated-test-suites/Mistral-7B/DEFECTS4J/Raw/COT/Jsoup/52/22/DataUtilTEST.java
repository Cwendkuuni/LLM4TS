import org.junit.Assert;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
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
    public void testLoadFile() throws IOException {
        String fileContent = "This is a test file.";
        File file = new File("testFile.html");
        file.createNewFile();
        OutputStream outputStream = new RandomAccessFile(file, "rw").getFD().getOutputStream();
        outputStream.write(fileContent.getBytes());
        outputStream.close();

        Document document = DataUtil.load(file, "UTF-8", null);
        Assert.assertEquals("This is a test file.", document.body().text());

        file.delete();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        Document document = DataUtil.load(inputStream, "UTF-8", null);
        Assert.assertEquals("This is a test input stream.", document.body().text());

        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        Document document = DataUtil.load(inputStream, "UTF-8", null, Parser.htmlParser());
        Assert.assertEquals("This is a test input stream.", document.body().text());

        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        inputStream.close();

        Assert.assertEquals(inputStreamContent, outputStream.toString());
    }

    @Test
    public void testParseByteData() throws IOException {
        String bytes = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body>This is a test.</body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes.getBytes());

        Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", null, Parser.htmlParser());
        Assert.assertEquals("This is a test.", document.body().text());
    }

    @Test(expected = IOException.class)
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String bytes = "<?xml version=\"1.0\" encoding=\"invalidCharset\"?>\n<html><body>This is a test.</body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes.getBytes());

        Document document = DataUtil.parseByteData(byteBuffer, "invalidCharset", null, Parser.htmlParser());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        Assert.assertEquals(inputStreamContent, Charset.forName("UTF-8").decode(byteBuffer).toString());

        inputStream.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String contentType2 = "text/html";

        String charset1 = DataUtil.getCharsetFromContentType(contentType1);
        String charset2 = DataUtil.getCharsetFromContentType(contentType2);

        Assert.assertEquals("UTF-8", charset1);
        Assert.assertNull(charset2);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(mimeBoundary);
        Assert.assertFalse(mimeBoundary.isEmpty());
    }

    @Test
    public void testDetectCharsetFromBom() {
        String bomUtf8 = "\uFEFFThis is a test.";
        String bomUtf16 = "\uFEFF\u0000This is a test.";
        String bomUtf32 = "\u0000\u0000\uFEFFThis is a test.";
        String bomUtf8BE = "\u00FE\u00FFThis is a test.";
        String bomUtf8LE = "\u00FF\u00FEThis is a test.";

        ByteBuffer bomUtf8ByteBuffer = ByteBuffer.wrap(bomUtf8.getBytes());
        ByteBuffer bomUtf16ByteBuffer = ByteBuffer.wrap(bomUtf16.getBytes());
        ByteBuffer bomUtf32ByteBuffer = ByteBuffer.wrap(bomUtf32.getBytes());
        ByteBuffer bomUtf8BEByteBuffer = ByteBuffer.wrap(bomUtf8BE.getBytes());
        ByteBuffer bomUtf8LEByteBuffer = ByteBuffer.wrap(bomUtf8LE.getBytes());

        String detectedCharsetUtf8 = DataUtil.detectCharsetFromBom(bomUtf8ByteBuffer, null);
        String detectedCharsetUtf16 = DataUtil.detectCharsetFromBom(bomUtf16ByteBuffer, null);
        String detectedCharsetUtf32 = DataUtil.detectCharsetFromBom(bomUtf32ByteBuffer, null);
        String detectedCharsetUtf8BE = DataUtil.detectCharsetFromBom(bomUtf8BEByteBuffer, null);
        String detectedCharsetUtf8LE = DataUtil.detectCharsetFromBom(bomUtf8LEByteBuffer, null);

        Assert.assertEquals("UTF-8", detectedCharsetUtf8);
        Assert.assertNull(detectedCharsetUtf16);
        Assert.assertNull(detectedCharsetUtf32);
        Assert.assertEquals("UTF-8", detectedCharsetUtf8BE);
        Assert.assertEquals("UTF-8", detectedCharsetUtf8LE);
    }
}