package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    /**
     * Test method for {@link DataUtil#load(File, String, String)}.
     */
    @Test
    public void testLoadFromFile() throws IOException {
        String htmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<title>Test</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello World!</h1>\n" +
                "</body>\n" +
                "</html>";

        File testFile = new File("test.html");
        Files.write(testFile.getAbsoluteFile().toPath(), htmlContent.getBytes());

        Document document = DataUtil.load(testFile, "UTF-8", null);
        assertNotNull(document);
        assertEquals("Test", document.title());
        assertEquals("Hello World!", document.select("h1").text());

        Files.delete(testFile.getAbsoluteFile().toPath());
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}.
     */
    @Test
    public void testLoadFromInputStream() throws IOException {
        String htmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<title>Test</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello World!</h1>\n" +
                "</body>\n" +
                "</html>";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(document);
        assertEquals("Test", document.title());
        assertEquals("Hello World!", document.select("h1").text());
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)}.
     */
    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        String htmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<title>Test</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<xhtml:div xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">\n" +
                "<xhtml:h1 xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">Hello World!</xhtml:h1>\n" +
                "</xhtml:div>\n" +
                "</body>\n" +
                "</html>";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
        assertNotNull(document);
        assertEquals("Test", document.title());
        assertEquals("Hello World!", document.select("div/h1").text());
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)}.
     */
    @Test
    public void testParseByteData() throws IOException {
        String htmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<title>Test</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello World!</h1>\n" +
                "</body>\n" +
                "</html>";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        Document document = DataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("Test", document.title());
        assertEquals("Hello World!", document.select("h1").text());

        document = DataUtil.parseByteData(byteBuffer, "gb2312", null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("Test", document.title());
        assertEquals("Hello World!", document.select("h1").text());
    }

    /**
     * Test method for {@link DataUtil#readToByteBuffer(InputStream, int)}.
     */
    @Test
    public void testReadToByteBuffer() throws IOException {
        String htmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<title>Test</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello World!</h1>\n" +
                "</body>\n" +
                "</html>";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);

        byte[] bufferContent = new byte[byteBuffer.limit()];
        byteBuffer.get(bufferContent);

        assertEquals(htmlContent.length(), byteBuffer.limit());
        assertTrue(new String(bufferContent).contains("Hello World!"));
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)}.
     */
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=gb2312";
        assertEquals("gb2312", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }
}