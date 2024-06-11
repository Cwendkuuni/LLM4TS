package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

public class DataUtilTest {
    private static final String TEST_FILE_PATH = "path/to/your/test/file.html"; // Replace with the actual path to your test file

    @Test
    public void testLoadDocument() throws IOException {
        File file = new File(TEST_FILE_PATH);
        String charsetName = "UTF-8"; // Replace with the actual charset of your test file if known
        String baseUri = "http://test.com"; // Replace with the actual base URI of your test document if necessary

        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadDocumentWithNullCharset() throws IOException {
        File file = new File(TEST_FILE_PATH);
        String baseUri = "http://test.com"; // Replace with the actual base URI of your test document if necessary

        Document document = DataUtil.load(file, null);
        assertNotNull(document);
    }

    @Test
    public void testLoadDocumentWithInvalidCharset() throws IOException {
        File file = new File(TEST_FILE_PATH);
        String charsetName = "InvalidCharsetName"; // Replace with an invalid charset name
        String baseUri = "http://test.com"; // Replace with the actual base URI of your test document if necessary

        try {
            Document document = DataUtil.load(file, charsetName, baseUri);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadStream() throws IOException {
        String testData = "Your test data here"; // Replace with your test data
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        String charsetName = "UTF-8"; // Replace with the actual charset of your test data if known
        String baseUri = "http://test.com"; // Replace with the actual base URI of your test document if necessary

        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithValidCharset() throws IOException {
        String testData = "Your test data here"; // Replace with your test data
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes());
        String charsetName = "UTF-8"; // Replace with the actual charset of your test data if known
        String baseUri = "http://test.com"; // Replace with the actual base URI of your test document if necessary

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithAutoDetectCharset() throws IOException {
        String testData = "Your test data here"; // Replace with your test data
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes());
        String baseUri = "http://test.com"; // Replace with the actual base URI of your test document if necessary

        Document document = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String testData = "Your test data here"; // Replace with your test data
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes());
        String charsetName = "InvalidCharsetName"; // Replace with an invalid charset name
        String baseUri = "http://test.com"; // Replace with the actual base URI of your test document if necessary

        try {
            Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "Your test data here"; // Replace with your test data
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(testData.getBytes().length, byteData.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}