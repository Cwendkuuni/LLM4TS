package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.parser.Parser.HtmlParser;

/**
 * Test cases for DataUtil class.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    private static final String TEST_FILE = "testData.html";

    @Test
    public void testLoadFileWithDefaultCharset() throws Exception {
        File file = new File(TEST_FILE);
        Document doc = DataUtil.load(file, null, "/");
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws Exception {
        File file = new File(TEST_FILE);
        Document doc = DataUtil.load(file, "UTF-8", "/");
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws Exception {
        File file = new File(TEST_FILE);
        try {
            Document doc = DataUtil.load(file, "INVALID_CHARSET", "/");
            assertNull(doc);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws Exception {
        String testData = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, null, "/");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws Exception {
        String testData = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-8", "/");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws Exception {
        String testData = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-8"));
        try {
            Document doc = DataUtil.load(inputStream, "INVALID_CHARSET", "/");
            assertNull(doc);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLoadInputStreamWithParser() throws Exception {
        String testData = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-8"));
        Parser parser = Mockito.mock(HtmlParser.class);
        Document doc = DataUtil.load(inputStream, "UTF-8", "/", parser);
        assertNotNull(doc);
        Mockito.verify(parser).parseInput(testData, "/");
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws Exception {
        String testData = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(byteData, null, "/", Mockito.mock(HtmlParser.class));
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws Exception {
        String testData = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        String charsetName = "UTF-8";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(byteData, charsetName, "/", Mockito.mock(HtmlParser.class));
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws Exception {
        String testData = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        String charsetName = "INVALID_CHARSET";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes("UTF-8"));
        try {
            Document doc = DataUtil.parseByteData(byteData, charsetName, "/", Mockito.mock(HtmlParser.class));
            assertNull(doc);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testReadToByteBuffer() throws Exception {
        String testData = "Test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-8"));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(testData.getBytes().length, byteData.limit());
    }

    @Test
    public void testGetCharsetFromContentType() throws Exception {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() throws Exception {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() throws Exception {
        String contentType = "text/html; charset=INVALID_CHARSET";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testLoadFileWithMetaCharset() throws Exception {
        String testData = "<html><head><meta charset=\"UTF-16LE\"><body>Test</body></html>";
        File file = new File(TEST_FILE);
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(testData.getBytes("UTF-8"));
        outputStream.close();

        Document doc = DataUtil.load(file, null, "/");
        assertNotNull(doc);
        assertEquals("UTF-16LE", doc.outputSettings().charset());

        // clean up
        file.delete();
    }

    @Test
    public void testLoadFileWithInvalidMetaCharset() throws Exception {
        String testData = "<html><head><meta charset=\"INVALID_CHARSET\"><body>Test</body></html>";
        File file = new File(TEST_FILE);
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(testData.getBytes("UTF-8"));
        outputStream.close();

        try {
            Document doc = DataUtil.load(file, null, "/");
            assertNull(doc);
        } finally {
            file.delete();
        }
    }

    @Test
    public void testLoadFileWithNoMetaCharset() throws Exception {
        String testData = "<html><body>Test</body></html>";
        File file = new File(TEST_FILE);
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(testData.getBytes("UTF-8"));
        outputStream.close();

        Document doc = DataUtil.load(file, null, "/");
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());

        // clean up
        file.delete();
    }
}