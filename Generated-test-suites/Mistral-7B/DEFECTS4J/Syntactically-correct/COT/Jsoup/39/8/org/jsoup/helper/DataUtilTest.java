package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

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
import java.nio.charset.IllegalCharsetNameException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil class.
 */
public class DataUtilTest {

    /**
     * Test for load(File, String, String) method.
     * @throws IOException
     */
    @Test
    public void testLoadFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File file = new File("test.html"); // assuming test.html is present in classpath
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
    }

    /**
     * Test for load(InputStream, String, String) method.
     * @throws IOException
     */
    @Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("html content".getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
    }

    /**
     * Test for load(InputStream, String, String, Parser) method.
     * @throws IOException
     */
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("html content".getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, Parser.xmlParser());
        assertNotNull(doc);
    }

    /**
     * Test for parseByteData(ByteBuffer, String, String, Parser) method with null charsetName.
     * @throws IOException
     */
    @Test(expected = NullPointerException.class)
    public void testParseByteDataWithNullCharsetName() throws IOException {
        ByteBuffer byteData = ByteBuffer.allocate(10);
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        DataUtil.parseByteData(byteData, null, baseUri, parser);
    }

    /**
     * Test for parseByteData(ByteBuffer, String, String, Parser) method with invalid charsetName.
     * @throws IOException
     */
    @Test(expected = IllegalCharsetNameException.class)
    public void testParseByteDataWithInvalidCharsetName() throws IOException {
        ByteBuffer byteData = ByteBuffer.allocate(10);
        String baseUri = "http://example.com";
        String charsetName = "xyz";
        Parser parser = Parser.htmlParser();
        DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
    }

    /**
     * Test for parseByteData(ByteBuffer, String, String, Parser) method with default charset.
     */
    @Test
    public void testParseByteDataWithDefaultCharset() {
        ByteBuffer byteData = ByteBuffer.allocate(10);
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, null, baseUri, parser);
        assertNotNull(doc);
    }

    /**
     * Test for parseByteData(ByteBuffer, String, String, Parser) method with specified charset.
     */
    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        ByteBuffer byteData = ByteBuffer.allocate(10);
        String baseUri = "http://example.com";
        String charsetName = "UTF-8";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
    }

    /**
     * Test for readToByteBuffer(InputStream, int) method with maxSize > 0.
     */
    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String inputData = "test data";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        int maxSize = 5;
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, maxSize);
        assertNotNull(byteData);
        assertEquals(inputData.length(), byteData.limit());
    }

    /**
     * Test for readToByteBuffer(InputStream) method.
     */
    @Test
    public void testReadToByteBufferWithUnlimitedSize() throws IOException {
        String inputData = "test data";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteData);
        assertEquals(inputData.length(), byteData.limit());
    }

    /**
     * Test for getCharsetFromContentType(String) method with valid content type.
     */
    @Test
    public void testGetCharsetFromContentTypeValid() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    /**
     * Test for getCharsetFromContentType(String) method with invalid content type.
     */
    @Test
    public void testGetCharsetFromContentTypeInvalid() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}