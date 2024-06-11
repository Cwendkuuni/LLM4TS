package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class DataUtilTest {

    private static final String HTML_CONTENT_TYPE = "text/html";
    private static final String HTML_WITH_GB2312_CHARSET = "text/html; charset=GB2312";

    @BeforeClass
    public static void setUp() {
        // Initialize any static variables or perform any setup that needs to be done before all tests
    }

    @Test
    public void testLoadFile() throws IOException {
        File inputFile = new File("test.html"); // Replace with the actual path to your test file
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(inputFile, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamUTF8() throws IOException {
        String htmlData = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlData.getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamDefaultCharset() throws IOException {
        String htmlData = "<html><head><meta charset=\"gb2312\"></meta></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlData.getBytes(Charset.forName("UTF-8")));

        Document doc = DataUtil.load(inputStream, null, "http://example.com");
        assertNotNull(doc);

        String charset = DataUtil.getCharsetFromContentType(HTML_WITH_GB2312_CHARSET);
        assertNotNull(charset);
        assertNotNull(doc.outputSettings().charset());
        assertEquals(charset, doc.outputSettings().charset());
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamInvalidCharset() throws IOException {
        String htmlData = "<html><head><meta charset=\"InvalidCharset\"></meta></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlData.getBytes(Charset.forName("UTF-8")));

        Document doc = DataUtil.load(inputStream, null, "http://example.com");
        assertNull(doc);
    }

    @Test
    public void testParseByteDataUTF8() throws IOException {
        String htmlData = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlData.getBytes(Charset.forName("UTF-8")));

        Document doc = DataUtil.parseByteData(byteData, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataGB2312() throws IOException {
        String htmlData = "<html><head><meta charset=\"gb2312\"></meta></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlData.getBytes(Charset.forName("UTF-8")));

        Document doc = DataUtil.parseByteData(byteData, null, "http://example.com", Parser.htmlParser());
        assertNotNull(doc);

        String charset = DataUtil.getCharsetFromContentType(HTML_WITH_GB2312_CHARSET);
        assertNotNull(charset);
        assertNotNull(doc.outputSettings().charset());
        assertEquals(charset, doc.outputSettings().charset());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String htmlData = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlData.getBytes(Charset.forName("UTF-8")));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        byte[] expectedBytes = htmlData.getBytes(Charset.forName("UTF-8"));
        byte[] actualBytes = new byte[byteData.limit()];
        byteData.get(actualBytes);

        assertArrayEquals(expectedBytes, actualBytes);
    }
}