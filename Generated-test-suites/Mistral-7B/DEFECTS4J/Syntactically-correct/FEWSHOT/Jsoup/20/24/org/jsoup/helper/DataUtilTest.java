package org.jsoup.helper;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("testFile.html");
        String expectedContent = "Test file content";
        writeFile(file, expectedContent);

        Document document = DataUtil.load(file, null, "");
        assertEquals(expectedContent, document.body().text());
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("testFile.html");
        String expectedContent = "Test file content";
        writeFile(file, expectedContent);

        Document document = DataUtil.load(file, "UTF-8", "");
        assertEquals(expectedContent, document.body().text());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = "<html><body><p>Test input stream content</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());

        Document document = DataUtil.load(inputStream, null, "");
        assertEquals("Test input stream content", document.body().text());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String htmlContent = "<html><body><p>Test input stream content</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));

        Document document = DataUtil.load(inputStream, "UTF-8", "");
        assertEquals("Test input stream content", document.body().text());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedParser() throws IOException {
        String htmlContent = "<html><body><p>Test input stream content</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));

        Document document = DataUtil.load(inputStream, "UTF-8", "", Parser.xmlParser());
        assertEquals("Test input stream content", document.body().text());
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String htmlContent = "<html><body><p>Test byte data content</p></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes());

        Document document = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        assertEquals("Test byte data content", document.body().text());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String htmlContent = "<html><body><p>Test byte data content</p></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes("UTF-8"));

        Document document = DataUtil.parseByteData(byteData, "UTF-8", "", Parser.htmlParser());
        assertEquals("Test byte data content", document.body().text());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=ISO-8859-1";
        String expectedCharset = "ISO-8859-1";

        assertEquals(expectedCharset, DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testGetCharsetFromContentTypeNull() {
        assertNull(DataUtil.getCharsetFromContentType(null));
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String htmlContent = "<html><body><p>Test readToByteBuffer content</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        byte[] expectedByteData = htmlContent.getBytes();
        byte[] actualByteData = byteData.array();

        assertArrayEquals(expectedByteData, actualByteData);
    }

    private void writeFile(File file, String content) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }
}