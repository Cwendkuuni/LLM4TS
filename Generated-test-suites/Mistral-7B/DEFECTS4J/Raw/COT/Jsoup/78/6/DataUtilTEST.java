import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String filePath = "path/to/your/file.html";
        File file = new File(filePath);
        Document document = DataUtil.load(file, "UTF-8", null);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8", null, DataUtil.Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", null, DataUtil.Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        byte[] inputData = "Input data".getBytes(StandardCharsets.UTF_8);
        byte[] outputData = new byte[inputData.length];
        InputStream inputStream = new ByteArrayInputStream(inputData);
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        byte[] outputBytes = outputStream.toByteArray();

        Assert.assertArrayEquals(inputData, outputBytes);
    }

    @Test
    public void testLoadInvalidCharset() throws IOException {
        File file = new File("path/to/your/file.html");
        try {
            Document document = DataUtil.load(file, "INVALID_CHARSET", null);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadNullInputStream() throws IOException {
        try {
            Document document = DataUtil.load(null, "UTF-8", null);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));

        try {
            Document document = DataUtil.parseInputStream(inputStream, "INVALID_CHARSET", null, DataUtil.Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testDetectCharsetFromBomUtf16() throws IOException {
        String bomUtf16 = "FEFF";
        ByteBuffer byteData = ByteBuffer.wrap(bomUtf16.getBytes(StandardCharsets.UTF_8));
        BomCharset detectedCharset = DataUtil.detectCharsetFromBom(byteData);
        String expectedCharset = "UTF-16";
        assertEquals(expectedCharset, detectedCharset.charset);
    }

    @Test
    public void testDetectCharsetFromBomUtf32() throws IOException {
        String bomUtf32 = "FFFE0000";
        ByteBuffer byteData = ByteBuffer.wrap(bomUtf32.getBytes(StandardCharsets.UTF_8));
        BomCharset detectedCharset = DataUtil.detectCharsetFromBom(byteData);
        String expectedCharset = "UTF-32";
        assertEquals(expectedCharset, detectedCharset.charset);
    }

    @Test
    public void testDetectCharsetFromBomUtf8() throws IOException {
        String bomUtf8 = "EFBBBF";
        ByteBuffer byteData = ByteBuffer.wrap(bomUtf8.getBytes(StandardCharsets.UTF_8));
        BomCharset detectedCharset = DataUtil.detectCharsetFromBom(byteData);
        String expectedCharset = "UTF-8";
        assertEquals(expectedCharset, detectedCharset.charset);
    }

    @Test
    public void testDetectCharsetFromBomNull() {
        ByteBuffer byteData = ByteBuffer.allocate(0);
        BomCharset detectedCharset = DataUtil.detectCharsetFromBom(byteData);
        assertNull(detectedCharset);
    }
}