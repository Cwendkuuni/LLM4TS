import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    private String testHtml;

    @Before
    public void setUp() {
        testHtml = "<html><head><title>Test HTML</title></head><body><h1>Hello World!</h1></body></html>";
    }

    @Test
    public void testLoadFromFile() throws IOException {
        File inputFile = new File("test.html");
        FileWriter fileWriter = new FileWriter(inputFile);
        fileWriter.write(testHtml);
        fileWriter.close();

        Document document = DataUtil.load(inputFile, "UTF-8", "");

        assertNotNull(document);
        assertEquals("Test HTML", document.title());

        inputFile.delete();
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(testHtml.getBytes("UTF-8"));

        Document document = DataUtil.load(inputStream, "UTF-8", "");

        assertNotNull(document);
        assertEquals("Test HTML", document.title());
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(testHtml.getBytes("UTF-8"));

        Document document = DataUtil.load(inputStream, "UTF-8", "", Parser.htmlParser());

        assertNotNull(document);
        assertEquals("Test HTML", document.title());
    }

    @Test
    public void testCrossStreams() throws IOException {
        byte[] inputBytes = testHtml.getBytes("UTF-8");
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] outputBytes = outputStream.toByteArray();

        assertNotNull(outputBytes);
        assertEquals(testHtml, new String(outputBytes, "UTF-8"));
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(testHtml.getBytes("UTF-8"));

        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser());

        assertNotNull(document);
        assertEquals("Test HTML", document.title());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        byte[] inputBytes = testHtml.getBytes("UTF-8");
        InputStream inputStream = new ByteArrayInputStream(inputBytes);

        int maxSize = inputBytes.length;
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        byte[] outputBytes = new byte[byteBuffer.limit()];
        byteBuffer.get(outputBytes);

        assertNotNull(outputBytes);
        assertEquals(testHtml, new String(outputBytes, "UTF-8"));
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File inputFile = new File("test.html");
        FileWriter fileWriter = new FileWriter(inputFile);
        fileWriter.write(testHtml);
        fileWriter.close();

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(inputFile);

        byte[] outputBytes = new byte[byteBuffer.limit()];
        byteBuffer.get(outputBytes);

        assertNotNull(outputBytes);
        assertEquals(testHtml, new String(outputBytes, "UTF-8"));

        inputFile.delete();
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();

        assertNotNull(mimeBoundary);
        assertEquals(32, mimeBoundary.length());

        Pattern pattern = Pattern.compile("[-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ]+");
        assertEquals(true, pattern.matcher(mimeBoundary).matches());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNotNull(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);

        assertNotNull(validatedCharset);
        assertEquals(charset, validatedCharset);
    }
}