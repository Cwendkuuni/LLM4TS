import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("path/to/your/file.html");
        Document document = DataUtil.load(file, StandardCharsets.UTF_8.name(), null);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "Your HTML content";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), null);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithCustomParser() throws IOException {
        String xmlContent = "<?xml version='1.0' encoding='UTF-8'?>\n<root/>";
        InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), null, DataUtil.Parser.xmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String htmlContent = "Your HTML content";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, StandardCharsets.UTF_8.name(), null, DataUtil.Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithCustomParserAndCharset() throws IOException {
        String xmlContent = "<?xml version='1.0' encoding='UTF-8'?>\n<root/>";
        InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, "UTF-16", null, DataUtil.Parser.xmlParser());
        Assert.assertNull(document); // UTF-16 is not supported as the default charset for XML parsing
    }

    @Test
    public void testCrossStreams() throws IOException {
        String sourceContent = "Source content";
        String targetContent = "Target content";
        File sourceFile = new File("path/to/source.txt");
        File targetFile = new File("path/to/target.txt");
        try (InputStream inputStream = new FileInputStream(sourceFile);
             OutputStream outputStream = new FileOutputStream(targetFile)) {
            DataUtil.crossStreams(inputStream, outputStream);
            String sourceContentRead = new String(sourceFile.length() > 0 ? new byte[sourceFile.length()][]: new byte[0], StandardCharsets.UTF_8);
            String targetContentRead = new String(targetFile.length() > 0 ? new byte[targetFile.length()][]: new byte[0], StandardCharsets.UTF_8);
            Assert.assertEquals(sourceContent, sourceContentRead);
            Assert.assertEquals(targetContent, targetContentRead);
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=ISO-8859-15";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String sourceContent = "Source content";
        File sourceFile = new File("path/to/source.txt");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new FileInputStream(sourceFile));
        Assert.assertNotNull(byteBuffer);
        String sourceContentRead = new String(byteBuffer.array(), StandardCharsets.UTF_8);
        Assert.assertEquals(sourceContent, sourceContentRead);
    }

    @Test
    public void testReadToByteBufferWithInvalidCharset() {
        String charset = "ISO-8859-1";
        InputStream inputStream = new ByteArrayInputStream("Source content".getBytes(StandardCharsets.UTF_8));
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, charset);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validCharset = DataUtil.validateCharset(charset);
        Assert.assertEquals(charset, validCharset);

        String invalidCharset = "INVALID_CHARSET";
        String validatedCharset = DataUtil.validateCharset(invalidCharset);
        Assert.assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        int boundaryLength = mimeBoundary.length();
        boolean hasBoundaryChars = (mimeBoundary.length() >= DataUtil.boundaryLength);
        Assert.assertTrue(hasBoundaryChars);
        for (int i = 0; i < boundaryLength; i++) {
            char c = mimeBoundary.charAt(i);
            boolean isBoundaryChar = (Character.isDefinedByte(c) && DataUtil.mimeBoundaryChars[Character. charAt(c)-0x20] != 0);
            Assert.assertTrue(isBoundaryChar);
        }
    }
}