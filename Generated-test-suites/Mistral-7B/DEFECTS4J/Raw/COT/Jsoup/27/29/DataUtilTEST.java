package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() {
        File inputFile = new File("test.html"); // replace with the actual file path
        Document document = DataUtil.load(inputFile, "UTF-8", "");
        assertNotNull(document);
    }

    @Test
    public void testLoadFileWithInvalidCharset() {
        File inputFile = new File("test.html"); // replace with the actual file path
        assertThrows(IOException.class, () -> DataUtil.load(inputFile, "", ""));
    }

    @Test
    public void testLoadInputStream() {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8", "");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithNullCharset() {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, null, "");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharsetAndDetect() {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(("UTF-16BE" + htmlContent).getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, null, "");
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithClosedStream() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        DataUtil.load(inputStream, null, "");
        inputStream.close();
        DataUtil.load(inputStream, null, "");
    }

    @Test
    public void testLoadInputStreamWithParser() {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8", "", Parser.xmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteData() {
        String htmlContent = "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        String htmlContent = "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(("UTF-16BE" + htmlContent).getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteData);
    }

    @Test
    public void testReadToByteBufferWithClosedInputStream() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        DataUtil.readToByteBuffer(inputStream);
        inputStream.close();
        assertThrows(IOException.class, () -> DataUtil.readToByteBuffer(inputStream));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithMissingCharset() {
        String contentType = "text/html; charset=missing";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

}