
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;

public class DataUtilTest {

    @Test
    public void testLoad() throws IOException {
        File file = createTestFile("test.html");

        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
        Assertions.assertNotNull(doc);
        // add more assertions for typical use cases, edge cases, and error scenarios
    }

    @Test
    public void testLoadWithInputStream() throws IOException {
        InputStream inputStream = new FileInputStream(createTestFile("test.html"));

        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        Assertions.assertNotNull(doc);
        // add more assertions for typical use cases, edge cases, and error scenarios
    }

    @Test
    public void testLoadWithInputStreamAndParser() throws IOException {
        InputStream inputStream = new FileInputStream(createTestFile("test.xml"));

        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        Assertions.assertNotNull(doc);
        // add more assertions for typical use cases, edge cases, and error scenarios
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());

        DataUtil.crossStreams(inputStream, outputStream);
        Assertions.assertEquals("test data", outputStream.toString());
        // add more assertions for typical use cases, edge cases, and error scenarios
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = new FileInputStream(createTestFile("test.html"));

        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
        Assertions.assertNotNull(doc);
        // add more assertions for typical use cases, edge cases, and error scenarios
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream(createTestFile("test.html"));

        byte[] bytes = new byte[10];
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 10);
        byteBuffer.get(bytes);
        Assertions.assertEquals("test data", new String(bytes));
        // add more assertions for typical use cases, edge cases, and error scenarios
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String charset1 = DataUtil.getCharsetFromContentType(contentType1);
        Assertions.assertEquals("UTF-8", charset1);
        // add more assertions for typical use cases, edge cases, and error scenarios
    }

    @Test
    public void testValidateCharset() {
        String validCharset = "UTF-8";
        String invalidCharset = "InvalidCharset";

        boolean isValid = DataUtil.validateCharset(validCharset) != null;
        boolean isInvalid = DataUtil.validateCharset(invalidCharset) == null;

        Assertions.assertTrue(isValid);
        Assertions.assertTrue(isInvalid);
        // add more assertions for typical use cases, edge cases, and error scenarios
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        Assertions.assertNotNull(mimeBoundary);
        Assertions.assertEquals(32, mimeBoundary.length());
        // add more assertions for typical use cases, edge cases, and error scenarios
    }

    private File createTestFile(String fileName) throws IOException {
        File file = new File(fileName);
        FileWriter writer = new FileWriter(file);
        writer.write("<html><body>This is a test file</body></html>");
        writer.close();
        return file;
    }
}

