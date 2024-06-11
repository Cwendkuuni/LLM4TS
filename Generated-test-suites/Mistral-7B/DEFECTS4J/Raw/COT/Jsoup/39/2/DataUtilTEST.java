package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    /**
     * Test method for {@link DataUtil#load(File, String, String)}.
     */
    @Test
    public void testLoadFile() throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        File file = new File("test.html");
        FileUtils.writeStringToFile(file, content, "UTF-8");
        Document document = DataUtil.load(file, "UTF-8", null);
        assertEquals("Test", document.body().text());
        FileUtils.deleteQuietly(file);
    }

    /**
     * Test method for {@link DataUtil#load(File, String, String)}.
     */
    @Test(expected = IOException.class)
    public void testLoadFileInvalidCharset() throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        File file = new File("test.html");
        FileUtils.writeStringToFile(file, content, "UTF-8");
        Document document = DataUtil.load(file, "ISO-8859-1", null);
        fail("Expected IOException but no exception was thrown.");
        FileUtils.deleteQuietly(file);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}.
     */
    @Test
    public void testLoadInputStream() throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals("Test", document.body().text());
        inputStream.close();
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)}.
     */
    @Test
    public void testLoadInputStreamParser() throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));
        Document document = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
        assertEquals("Test", document.body().text());
        inputStream.close();
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)}.
     */
    @Test
    public void testParseByteData() {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(content.getBytes("UTF-8"));
        Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", null, Parser.htmlParser());
        assertEquals("Test", document.body().text());
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)} with invalid charset.
     */
    @Test(expected = IllegalCharsetNameException.class)
    public void testParseByteDataInvalidCharset() {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(content.getBytes("UTF-8"));
        Document document = DataUtil.parseByteData(byteBuffer, "ISO-8859-1", null, Parser.htmlParser());
        fail("Expected IllegalCharsetNameException but no exception was thrown.");
    }

    /**
     * Test method for {@link DataUtil#readToByteBuffer(InputStream, int)}.
     */
    @Test
    public void testReadToByteBuffer() throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);
        byte[] expectedBytes = content.getBytes("UTF-8");
        byte[] actualBytes = new byte[byteBuffer.limit()];
        byteBuffer.get(actualBytes);
        assertArrayEquals(expectedBytes, actualBytes);
        inputStream.close();
    }

    /**
     * Test method for {@link DataUtil#readToByteBuffer(InputStream)}.
     */
    @Test
    public void testReadToByteBufferEmpty() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertEquals(0, byteBuffer.limit());
        inputStream.close();
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)}.
     */
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        String contentType2 = "application/xml; charset=ISO-8859-1";
        charset = DataUtil.getCharsetFromContentType(contentType2);
        assertEquals("ISO-8859-1", charset);

        String contentType3 = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType3);
        assertNull(charset);
    }
}