package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.UncheckedIOException;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest implements Serializable {

    

    /**
     * Test load method with a valid file and default charset.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadValidFileWithDefaultCharset() throws IOException {
        File file = new File("testData/validFile.html");
        Document doc = DataUtil.load(file, null);
        assertNotNull(doc);
    }

    /**
     * Test load method with a valid file and specified charset.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadValidFileWithSpecifiedCharset() throws IOException {
        File file = new File("testData/validFileWithCharset.html");
        Document doc = DataUtil.load(file, "UTF-8");
        assertNotNull(doc);
    }

    /**
     * Test load method with an empty file.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadEmptyFile() throws IOException {
        File file = new File("testData/emptyFile.html");
        Document doc = DataUtil.load(file, null);
        assertNotNull(doc);
        assertEquals(0, doc.childNodeSize());
    }

    /**
     * Test load method with an invalid file.
     * @throws IOException on IO error
     */
    @Test(expected = IOException.class)
    public void testLoadInvalidFile() throws IOException {
        File file = new File("testData/invalidFile.html");
        DataUtil.load(file, null);
    }

    /**
     * Test parseInputStream method with a valid input stream and default charset.
     * @throws IOException on IO error
     */
    @Test
    public void testParseInputStreamValidInputWithDefaultCharset() throws IOException {
        String testData = "Test Data for parseInputStream.";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(testData, doc.body().text());
    }

    /**
     * Test parseInputStream method with a valid input stream and specified charset.
     * @throws IOException on IO error
     */
    @Test
    public void testParseInputStreamValidInputWithSpecifiedCharset() throws IOException {
        String testData = "Test Data for parseInputStream.";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-8"));
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(testData, doc.body().text());
    }

    /**
     * Test parseInputStream method with an empty input stream.
     * @throws IOException on IO error
     */
    @Test
    public void testParseInputStreamEmptyInput() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(0, doc.childNodeSize());
    }

    /**
     * Test parseInputStream method with an invalid input stream.
     * @throws IOException on IO error
     */
    @Test(expected = IOException.class)
    public void testParseInputStreamInvalidInput() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{-1});
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
    }

    /**
     * Test parseInputStream method with a BOM.
     * @throws IOException on IO error
     */
    @Test
    public void testParseInputStreamWithBom() throws IOException {
        String testData = "\ufeffTest Data for parseInputStream.";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(testData.substring(1), doc.body().text()); // Remove BOM
    }

    /**
     * Test getCharsetFromContentType method.
     */
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html; charset=ISO-8859-1";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("ISO-8859-1", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    /**
     * Test mimeBoundary method.
     */
    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(32, boundary.length());
    }

    
}