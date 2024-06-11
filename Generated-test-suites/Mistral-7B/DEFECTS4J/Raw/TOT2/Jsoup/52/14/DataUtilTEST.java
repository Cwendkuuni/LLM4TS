import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "This is a test file.";
        File inputFile = new File("input.html");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] fileContentBytes = fileContent.getBytes(StandardCharsets.UTF_8);
        outputStream.write(fileContentBytes);
        outputStream.close();
        File testFile = new File(inputFile.getAbsolutePath() + "_test.html");
        Files.write(testFile.toPath(), fileContentBytes);

        Document document = DataUtil.load(testFile, "UTF-8", null);
        Assert.assertNotNull(document);
        Assert.assertEquals(fileContent, document.body().text());

        testFile.delete();
    }

    @Test
    public void testLoadStreamWithDefaultCharset() throws IOException {
        String inputContent = "This is a test stream.";
        byte[] inputBytes = inputContent.getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputBytes);

        Document document = DataUtil.load(inputStream, null, null);
        Assert.assertNotNull(document);
        Assert.assertEquals(inputContent, document.body().text());

        inputStream.close();
    }

    @Test
    public void testLoadStreamWithSpecifiedCharset() throws IOException {
        String inputContent = "This is a test stream.";
        byte[] inputBytes = inputContent.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(inputBytes);

        Document document = DataUtil.load(inputStream, "UTF-8", null);
        Assert.assertNotNull(document);
        Assert.assertEquals(inputContent, document.body().text());

        inputStream.close();
    }

    @Test
    public void testLoadStreamWithParser() throws IOException {
        String inputContent = "This is a test stream.";
        byte[] inputBytes = inputContent.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(inputBytes);

        Document document = DataUtil.load(inputStream, "UTF-8", null, DataUtil.Parser.xmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals(inputContent, document.body().text());

        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputContent = "This is a test input stream.";
        String outputContent = "This is a test output stream.";
        byte[] inputBytes = inputContent.getBytes();
        byte[] outputBytes = outputContent.getBytes();

        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        Assert.assertArrayEquals(outputBytes, outputStream.toByteArray());

        inputStream.close();
        outputStream.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCrossStreamsWithNullInputStream() {
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(null, outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCrossStreamsWithNullOutputStream() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DataUtil.crossStreams(inputStream, null);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String testData = "<html><head></head><body>This is a test.</body></html>";
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        byteData.put(testData.getBytes(StandardCharsets.UTF_8));
        byteData.flip();

        Document document = DataUtil.parseByteData(byteData, null, null, DataUtil.Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals(testData, document.body().text());

        byteData.clear();
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String testData = "<html><head></head><body>This is a test.</body></html>";
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        byteData.put(testData.getBytes(StandardCharsets.UTF_8));
        byteData.flip();

        Document document = DataUtil.parseByteData(byteData, "UTF-8", null, DataUtil.Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals(testData, document.body().text());

        byteData.clear();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseByteDataWithNullByteData() {
        Document document = DataUtil.parseByteData(null, null, null, DataUtil.Parser.htmlParser());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "This is a test.";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        Assert.assertNotNull(byteData);
        Assert.assertEquals(testData.getBytes(StandardCharsets.UTF_8).length, byteData.limit());

        inputStream.close();
    }

    @Test(expected = NullPointerException.class)
    public void testReadToByteBufferWithNullInputStream() {
        ByteBuffer byteData = DataUtil.readToByteBuffer(null);
    }
}