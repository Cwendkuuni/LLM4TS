package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.AnnotatedClassLoader;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(PowerMockRunner)
@PrepareForTest(DataUtil.class)
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String filePath = "testData/testFile.html";
        File file = new File(filePath);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInvalidFile() throws IOException {
        String filePath = "testData/nonExistentFile.html";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        assertThrows(FileNotFoundException.class, () -> {
            DataUtil.load(new File(filePath), charsetName, baseUri);
        });
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testData = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String testData = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";

        assertThrows(IllegalArgumentException.class, () -> {
            DataUtil.load(inputStream, charsetName, baseUri);
        });
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String testData = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        String charsetName = null;
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithEmptyCharset() throws IOException {
        String testData = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        String charsetName = "";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithNullBaseUri() throws IOException {
        String testData = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = null;

        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseByteData() {
        String testData = "test data";
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        byteData.put(testData.getBytes());
        byteData.flip();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        String testData = "test data";
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        byteData.put(testData.getBytes());
        byteData.flip();

        assertThrows(IllegalArgumentException.class, () -> {
            DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        });
    }

    @Test
    public void testParseByteDataWithNullCharset() {
        String testData = "test data";
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        byteData.put(testData.getBytes());
        byteData.flip();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithEmptyCharset() {
        String testData = "test data";
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        String charsetName = "";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        byteData.put(testData.getBytes());
        byteData.flip();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithNullBaseUri() {
        String testData = "test data";
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        String charsetName = "UTF-8";
        String baseUri = null;
        Parser parser = Parser.htmlParser();

        byteData.put(testData.getBytes());
        byteData.flip();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputData = "test data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] outputBytes = outputStream.toByteArray();
        String outputData = new String(outputBytes, "UTF-8");

        assertNotNull(outputData);
        assertNotNull(outputData.trim());
        assertTrue(outputData.equals(inputData));
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        byte[] bytes = byteBuffer.array();
        String outputData = new String(bytes, "UTF-8");

        assertNotNull(outputData);
        assertNotNull(outputData.trim());
        assertTrue(outputData.equals(testData));
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String testData = "test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        int maxSize = 10;

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        byte[] bytes = byteBuffer.array();
        String outputData = new String(bytes, "UTF-8");

        assertNotNull(outputData);
        assertNotNull(outputData.trim());
        assertTrue(outputData.length() <= maxSize);
        assertTrue(outputData.equals(testData.substring(0, maxSize)));
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String filePath = "testData/testFile.html";
        File file = new File(filePath);

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);

        byte[] bytes = byteBuffer.array();
        String outputData = new String(bytes, "UTF-8");

        assertNotNull(outputData);
        assertNotNull(outputData.trim());
        assertTrue(outputData.length() > 0);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();

        assertNotNull(boundary);
        assertNotNull(boundary.trim());
        assertTrue(boundary.length() >= 32);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNotNull(charset);
        assertNotNull(charset.trim());
        assertEquals(charset, "UTF-8");
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=InvalidCharset";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithUnsupportedCharset() {
        String contentType = "text/html; charset=EUC-KR";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentType = "text/html";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }
}