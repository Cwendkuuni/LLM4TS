package org.jsoup.helper;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.parser.Parser;
import org.jsoup.helper.DataUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil class.
 */
public class DataUtilTest {

    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream inputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
    }

    @Test
    public void testLoadFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "/path/to/base";
        File file = new File("test.html");
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "/path/to/base";
        File file = new File("not_exists.html");
        DataUtil.load(file, charsetName, baseUri);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "/path/to/base";
        String html = "<html><head></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String baseUri = "/path/to/base";
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        DataUtil.load(inputStream, null, baseUri);
    }

    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "/path/to/base";
        String html = "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithNullCharset() {
        String baseUri = "/path/to/base";
        ByteBuffer byteData = ByteBuffer.wrap("test data".getBytes());
        Document doc = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        String charsetName = "invalidCharset";
        String baseUri = "/path/to/base";
        ByteBuffer byteData = ByteBuffer.wrap("test data".getBytes());
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputData = "test data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidFormat() {
        String contentType = "text/html text/css";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}