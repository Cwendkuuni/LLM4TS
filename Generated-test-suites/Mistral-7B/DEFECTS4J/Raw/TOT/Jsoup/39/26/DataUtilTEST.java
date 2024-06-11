package org.jsoup.helper.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class DataUtilTest {

    private static final String HTML_WITH_UTF8_CHARSET =
            "<html>\n" +
                    "<head>\n" +
                    "<meta charset=\"UTF-8\">\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<p>Test UTF-8</p>\n" +
                    "</body>\n" +
                    "</html>";

    private static final String HTML_WITH_GB2312_CHARSET =
            "<html>\n" +
                    "<head>\n" +
                    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=GB2312\">\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<p>Test GB2312</p>\n" +
                    "</body>\n" +
                    "</html>";

    private static final String HTML_WITHOUT_CHARSET =
            "<html>\n" +
                    "<head>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<p>Test without charset</p>\n" +
                    "</body>\n" +
                    "</html>";

    private static final String BOM_HTML = "\uFEFF" + HTML_WITH_UTF8_CHARSET;

    private static final String XML_WITH_UTF8_CHARSET =
            "<xml version=\"1.0\" encoding=\"UTF-8\">\n" +
                    "<root>\n" +
                    "<value>Test UTF-8</value>\n" +
                    "</root>\n" +
                    "</xml>";

    private Document utf8HtmlDocument;
    private Document gb2312HtmlDocument;
    private Document noCharsetHtmlDocument;
    private Document bomHtmlDocument;
    private Document xmlUtf8Document;

    @Before
    public void setUp() throws IOException {
        utf8HtmlDocument = DataUtil.load(new File("test_utf8.html"), StandardCharsets.UTF_8.toString(), null);
        gb2312HtmlDocument = DataUtil.load(new File("test_gb2312.html"), null, null);
        noCharsetHtmlDocument = DataUtil.load(new File("test_no_charset.html"), null, null);
        bomHtmlDocument = DataUtil.load(new File("test_bom.html"), null, null);
        xmlUtf8Document = DataUtil.load(new ByteArrayInputStream(XML_WITH_UTF8_CHARSET.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8.toString(), null, Parser.xmlParser());
    }

    @Test
    public void testLoadFileWithUTF8Charset() throws IOException {
        Document document = DataUtil.load(new File("test_utf8.html"), StandardCharsets.UTF_8.toString(), null);
        assertEquals(utf8HtmlDocument, document);
    }

    @Test
    public void testLoadFileWithoutCharset() throws IOException {
        Document document = DataUtil.load(new File("test_no_charset.html"), null, null);
        assertEquals(noCharsetHtmlDocument, document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        Document document = DataUtil.load(new File("test_invalid_charset.html"), "UTF-16BE", null);
        fail("IOException expected");
    }

    @Test
    public void testLoadInputStreamWithUTF8Charset() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        outStream.write(HTML_WITH_UTF8_CHARSET.getBytes(StandardCharsets.UTF_8));
        ByteArrayInputStream inStream = new ByteArrayInputStream(outStream.toByteArray());
        Document document = DataUtil.load(inStream, StandardCharsets.UTF_8.toString(), null);
        assertEquals(utf8HtmlDocument, document);
    }

    @Test
    public void testLoadInputStreamWithGB2312Charset() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        outStream.write(HTML_WITH_GB2312_CHARSET.getBytes(StandardCharsets.UTF_8));
        ByteArrayInputStream inStream = new ByteArrayInputStream(outStream.toByteArray());
        Document document = DataUtil.load(inStream, null, null);
        assertEquals(gb2312HtmlDocument, document);
    }

    @Test
    public void testLoadInputStreamWithXMLParser() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        outStream.write(XML_WITH_UTF8_CHARSET.getBytes(StandardCharsets.UTF_8));
        ByteArrayInputStream inStream = new ByteArrayInputStream(outStream.toByteArray());
        Document document = DataUtil.load(inStream, null, null, Parser.xmlParser());
        assertEquals(xmlUtf8Document, document);
    }

    @Test
    public void testLoadInputStreamWithBOM() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        outStream.write(BOM_HTML.getBytes(StandardCharsets.UTF_8));
        ByteArrayInputStream inStream = new ByteArrayInputStream(outStream.toByteArray());
        Document document = DataUtil.load(inStream, null, null);
        assertEquals(utf8HtmlDocument, document);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharsetName() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        outStream.write(HTML_WITH_UTF8_CHARSET.getBytes(StandardCharsets.UTF_8));
        ByteArrayInputStream inStream = new ByteArrayInputStream(outStream.toByteArray());
        Document document = null;
        try {
            document = DataUtil.load(inStream, "UTF-16BE", null);
            fail("IOException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }

        document = DataUtil.load(inStream, null, null);
        assertEquals(utf8HtmlDocument, document);
    }

    @Test(expected = IOException.class)
    public void testParseByteDataWithInvalidCharsetName() throws IOException {
        String charsetName = "UTF-16BE";
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        outStream.write(HTML_WITH_UTF8_CHARSET.getBytes(StandardCharsets.UTF_8));
        ByteArrayInputStream inStream = new ByteArrayInputStream(outStream.toByteArray());
        Document document = DataUtil.parseByteData(ByteBuffer.wrap(inStream.readAllBytes()), charsetName, null, Parser.htmlParser());
        fail("IOException expected");
    }

    @Test
    public void testParseByteDataWithUTF8Charset() throws IOException {
        String charsetName = StandardCharsets.UTF_8.name();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        outStream.write(HTML_WITH_UTF8_CHARSET.getBytes(StandardCharsets.UTF_8));
        ByteArrayInputStream inStream = new ByteArrayInputStream(outStream.toByteArray());
        Document document = DataUtil.parseByteData(ByteBuffer.wrap(inStream.readAllBytes()), charsetName, null, Parser.htmlParser());
        assertEquals(utf8HtmlDocument, document);
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        outStream.write(HTML_WITH_GB2312_CHARSET.getBytes(StandardCharsets.UTF_8));
        ByteArrayInputStream inStream = new ByteArrayInputStream(outStream.toByteArray());
        Document document = DataUtil.parseByteData(ByteBuffer.wrap(inStream.readAllBytes()), null, null, Parser.htmlParser());
        assertEquals(gb2312HtmlDocument, document);
    }

    @Test
    public void testParseByteDataWithInvalidBOM() throws IOException {
        String charsetName = StandardCharsets.UTF_8.name();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        outStream.write("test".getBytes());
        outStream.write(0xFEFF);
        ByteArrayInputStream inStream = new ByteArrayInputStream(outStream.toByteArray());
        Document document = DataUtil.parseByteData(ByteBuffer.wrap(inStream.readAllBytes()), charsetName, null, Parser.htmlParser());
        assertEquals(utf8HtmlDocument, document);
    }
}