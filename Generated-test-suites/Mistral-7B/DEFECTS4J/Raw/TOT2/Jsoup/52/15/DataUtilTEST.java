import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File inputFile = new File("input.html");
        Document doc = DataUtil.load(inputFile, null, null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File inputFile = new File("input.html");
        Document doc = DataUtil.load(inputFile, "UTF-16", null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String inputData = "<html><head><meta charset='UTF-8'></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null, null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String inputData = "<html><head><meta charset='UTF-16'></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_16));
        Document doc = DataUtil.load(inputStream, "UTF-16", null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputData = "<?xml version='1.0' encoding='UTF-8'?>\n<root>\n<element>Test</element>\n</root>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null, null, DataUtil.Parser.xmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("input.html");
        File outputFile = new File("output.html");
        Document inputDocument = DataUtil.load(inputFile, null, null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputDocument.outputStream(), outputStream);
        Document outputDocument = DataUtil.load(new ByteArrayInputStream(outputStream.toByteArray()), null, null);
        Assert.assertEquals(inputDocument.title(), outputDocument.title());
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File inputFile = new File("input.html");
        Document doc = DataUtil.load(inputFile, "InvalidCharset", null);
        Assert.fail("Expected IOException but no exception was thrown");
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        byteData.put(DataUtil.UNICODE_BOM);
        byteData.put("Test".getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseByteData(byteData, null, null, DataUtil.Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        byteData.put("Test\u00E4ut".getBytes(StandardCharsets.UTF_8)); // Test Äut
        String charset = "UTF-8";
        Document doc = DataUtil.parseByteData(byteData, charset, null, DataUtil.Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test Äut", doc.text());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        File inputFile = new File("input.html");
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(inputFile);
        Assert.assertNotEquals(0, byteData.limit());
    }

    @Test
    public void testReadToByteBufferWithZeroMaxSize() throws IOException {
        File inputFile = new File("input.html");
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(inputFile);
        Assert.assertEquals(inputFile.length(), byteData.limit());
    }
}