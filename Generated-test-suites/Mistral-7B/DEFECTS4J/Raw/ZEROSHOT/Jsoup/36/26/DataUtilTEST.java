package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {
    private static File testFile;
    private static InputStream testInputStream;

    @BeforeClass
    public static void setUp() throws Exception {
        String testFilePath = "path/to/your/test/file.html";
        testFile = new File(testFilePath);
        testInputStream = new FileInputStream(testFile);
    }

    @Test
    public void testLoadFile() throws Exception {
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document doc = DataUtil.load(testFile, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidFile() throws Exception {
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        File invalidFile = new File("path/to/non-existent/file.html");
        DataUtil.load(invalidFile, charsetName, baseUri);
    }

    @Test
    public void testLoadInputStream() throws Exception {
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document doc = DataUtil.load(testInputStream, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidStream() throws Exception {
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        InputStream invalidStream = new FileInputStream(new File("path/to/non-existent/file.txt"));
        DataUtil.load(invalidStream, charsetName, baseUri);
    }

    @Test
    public void testParseByteData() throws Exception {
        ByteBuffer byteData = DataUtil.readToByteBuffer(testInputStream);
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws Exception {
        ByteBuffer byteData = DataUtil.readToByteBuffer(testInputStream);
        long byteCount = byteData.limit() * byteData.remaining();
        assertEquals(testInputStream.available(), byteCount);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=ISO-8859-1";
        String expectedCharset = "ISO-8859-1";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidContentType() {
        String contentType = "text/plain";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}