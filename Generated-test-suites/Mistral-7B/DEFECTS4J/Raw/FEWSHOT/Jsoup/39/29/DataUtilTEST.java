package org.jsoup.helper;

import static org.junit.Assert.*;
import static org.jsoup.parser.Parser.htmlParser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class DataUtilTest {

    @Test
    public void testLoad() throws IOException {
        String filePath = "testFile.html";
        File testFile = new File(filePath);
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        Document document = DataUtil.load(testFile, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadWithNullCharsetName() throws IOException {
        File testFile = new File("testFile.html");
        String baseUri = "http://test.com";

        Document document = DataUtil.load(testFile, null, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadWithInvalidCharsetName() throws IOException {
        File testFile = new File("testFile.html");
        String baseUri = "http://test.com";
        String invalidCharsetName = "InvalidCharset";

        try {
            Document document = DataUtil.load(testFile, invalidCharsetName, baseUri);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testData = "<html><body>Test Data</body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithNullCharsetName() throws IOException {
        String testData = "<html><body>Test Data</body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        String baseUri = "http://test.com";

        Document document = DataUtil.load(inputStream, null, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharsetName() throws IOException {
        String testData = "<html><body>Test Data</body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        String invalidCharsetName = "InvalidCharset";
        String baseUri = "http://test.com";

        try {
            Document document = DataUtil.load(inputStream, invalidCharsetName, baseUri);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String testData = "<html><body>Test Data</body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Parser parser = htmlParser();

        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testParseByteData() throws IOException, UnsupportedEncodingException {
        String testData = "<html><body>Test Data</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithNullCharsetName() throws IOException, UnsupportedEncodingException {
        String testData = "<html><body>Test Data</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes());
        String baseUri = "http://test.com";

        Document document = DataUtil.parseByteData(byteData, null, baseUri, htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithInvalidCharsetName() throws IOException, UnsupportedEncodingException {
        String testData = "<html><body>Test Data</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes());
        String invalidCharsetName = "InvalidCharset";
        String baseUri = "http://test.com";

        try {
            Document document = DataUtil.parseByteData(byteData, invalidCharsetName, baseUri, htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "Test Data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 0);
        assertEquals(testData.length(), byteData.remaining());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String testData = "Test Data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        int maxSize = 5;

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, maxSize);
        assertEquals(maxSize, byteData.remaining());
    }

    @Test
    public void testReadToByteBufferWithUnlimitedMaxSize() throws IOException {
        String testData = "Test Data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, -1);
        assertEquals(testData.length(), byteData.remaining());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNullContentType() {
        String contentType = null;

        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidContentType() {
        String contentType = "text/plain";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}