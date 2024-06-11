package org.jsoup.helper;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    private Document doc;
    private static final String TEST_FILE = "test.html";
    private static final String TEST_HTML = "<html><head>" +
            "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\">" +
            "<meta charset=\"UTF-8\">" +
            "</head><body>" +
            "<h1>Test</h1>" +
            "</body></html>";

    @Before
    public void setUp() throws Exception {
        doc = DataUtil.load(new File(TEST_FILE), "UTF-8", null);
    }

    @Test
    public void testLoadFileWithUtf8Charset() throws IOException {
        File file = new File(TEST_FILE);
        Document document = DataUtil.load(file, "UTF-8", null);
        assertEquals("Test", document.title());
    }

    @Test
    public void testLoadFileWithNullCharset() throws IOException {
        File file = new File(TEST_FILE);
        Document document = DataUtil.load(file, null, null);
        assertEquals("Test", document.title());

        Element meta = document.select("meta[http-equiv=content-type], meta[charset]").first();
        String charset = null;
        if (meta != null) {
            if (meta.hasAttr("http-equiv")) {
                charset = getCharsetFromContentType(meta.attr("content"));
            } else {
                charset = meta.attr("charset");
            }
        }
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testLoadInputStreamWithUtf8Charset() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        outStream.write(TEST_HTML.getBytes(StandardCharsets.UTF_8));
        InputStream inStream = new ByteArrayInputStream(outStream.toByteArray());

        Document document = DataUtil.load(inStream, "UTF-8", null);
        assertEquals("Test", document.title());

        Element meta = document.select("meta[http-equiv=content-type], meta[charset]").first();
        String charset = null;
        if (meta != null) {
            if (meta.hasAttr("http-equiv")) {
                charset = getCharsetFromContentType(meta.attr("content"));
            } else {
                charset = meta.attr("charset");
            }
        }
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        outStream.write(TEST_HTML.getBytes(StandardCharsets.UTF_8));
        InputStream inStream = new ByteArrayInputStream(outStream.toByteArray());

        Document document = DataUtil.load(inStream, null, null);
        assertEquals("Test", document.title());

        Element meta = document.select("meta[http-equiv=content-type], meta[charset]").first();
        String charset = null;
        if (meta != null) {
            if (meta.hasAttr("http-equiv")) {
                charset = getCharsetFromContentType(meta.attr("content"));
            } else {
                charset = meta.attr("charset");
            }
        }
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        outStream.write(TEST_HTML.getBytes(StandardCharsets.UTF_8));
        InputStream inStream = new ByteArrayInputStream(outStream.toByteArray());

        try {
            Document document = DataUtil.load(inStream, "invalid-charset", null);
            fail("expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testParseByteDataWithUtf8Charset() {
        String docData = TEST_HTML;
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertEquals("Test", document.title());
    }

    @Test
    public void testParseByteDataWithNullCharset() {
        String docData = TEST_HTML;
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals("Test", document.title());

        Element meta = document.select("meta[http-equiv=content-type], meta[charset]").first();
        String charset = null;
        if (meta != null) {
            if (meta.hasAttr("http-equiv")) {
                charset = getCharsetFromContentType(meta.attr("content"));
            } else {
                charset = meta.attr("charset");
            }
        }
        assertEquals("UTF-8", charset);
    }

    private static String getCharsetFromContentType(String contentType) {
        if (contentType == null) return null;
        String charset = null;
        Matcher m = DataUtil.charsetPattern.matcher(contentType);
        if (m.find()) {
            charset = m.group(1).trim();
        }
        return charset;
    }
}