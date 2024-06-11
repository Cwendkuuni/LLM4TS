package org.jsoup.helper;

import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.helper.DataUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

/**
 * JUnit test cases for DataUtil class.
 */
public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File inputFile = new File("test.html");
        Document document = DataUtil.load(inputFile, null, null);
        assertNotNull(document);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File inputFile = new File("test.html");
        Document document = DataUtil.load(inputFile, "UTF-8", null);
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File inputFile = new File("test.html");
        Document document = DataUtil.load(inputFile, "InvalidCharset", null);
        fail("IOException expected but not thrown");
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String inputData = "test data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        Document document = DataUtil.load(inputStream, null, null);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String inputData = "test data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes("UTF-8"));
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String inputData = "test data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        Document document = DataUtil.load(inputStream, "InvalidCharset", null);
        fail("IOException expected but not thrown");
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullInput() throws IOException {
        Document document = DataUtil.load(null, null, null);
        fail("IOException expected but not thrown");
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String inputData = "test data";
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(inputData.getBytes()));
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String inputData = "test data";
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(inputData.getBytes("UTF-8")));
        Document document = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithEmptyInput() {
        ByteBuffer byteData = ByteBuffer.allocate(0);
        try {
            Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
            fail("IOException expected but not thrown");
        } catch (IOException e) {
            // Expected
        }
        assertNull(null);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputData = "test data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        byte[] bytes = byteData.array();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(bytes, 0, bytes.length);
        String outputData = outputStream.toString();
        assertEquals(inputData, outputData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNullContentType() {
        String contentType = null;
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