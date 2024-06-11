package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Test class for DataUtil methods.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test</p>\n" +
                "</body>\n" +
                "</html>";
        File testFile = new File("testFile.html");
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write(fileContent);
        }

        Document doc = DataUtil.load(testFile, "UTF-8", "/");
        assertEquals("Test", doc.body().text());

        testFile.delete(); // clean up the test file
    }

    @Test
    public void testLoadInvalidCharsetFile() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"ISO-8859-1\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test</p>\n" +
                "</body>\n" +
                "</html>";
        File testFile = new File("testFile.html");
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write(fileContent);
        }

        try {
            Document doc = DataUtil.load(testFile, null, "/");
            fail();
        } catch (IOException e) {
            // expected
        }

        testFile.delete(); // clean up the test file
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test</p>\n" +
                "</body>\n" +
                "</html>";
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(testData.getBytes());

        Document doc = DataUtil.load(testInputStream, "UTF-8", "/");
        assertEquals("Test", doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"ISO-8859-1\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test</p>\n" +
                "</body>\n" +
                "</html>";
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(testData.getBytes());

        try {
            Document doc = DataUtil.load(testInputStream, null, "/");
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String testData = "<html lang=\"en\"><head><meta charset=\"UTF-8\"></head><body><p>Test</p></body></html>";
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(testData.getBytes());

        Document doc = DataUtil.load(testInputStream, "UTF-8", "/", DataUtil.Parser.xmlParser());
        assertEquals("Test", doc.body().text());
    }

    @Test
    public void testParseByteDataWithNullCharset() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("<html lang=\"en\"><head><meta charset=\"UTF-8\"></head><body><p>Test</p></body></html>".getBytes());

        String charset = null;
        String baseUri = "/";
        Parser parser = DataUtil.Parser.htmlParser();

        try {
            Document doc = DataUtil.parseByteData(byteBuffer, charset, baseUri, parser);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html lang=\"en\"><head><meta charset=\"ISO-8859-1\"></head><body><p>Test</p></body></html>".getBytes());
        String charset = "UTF-8";
        String baseUri = "/";
        Parser parser = DataUtil.Parser.htmlParser();

        try {
            Document doc = DataUtil.parseByteData(byteBuffer, charset, baseUri, parser);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("<html lang=\"en\"><head><meta charset=\"UTF-8\"></head><body><p>Test</p></body></html>".getBytes());
        String charset = "UTF-8";
        String baseUri = "/";
        Parser parser = DataUtil.Parser.htmlParser();

        Document doc = DataUtil.parseByteData(byteBuffer, charset, baseUri, parser);
        assertEquals("Test", doc.body().text());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test</p>\n" +
                "</body>\n" +
                "</html>";
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(testData.getBytes());

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInputStream);
        assertEquals(testData.length(), byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=ISO-8859-1";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("ISO-8859-1", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=ISO-8859-123";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}