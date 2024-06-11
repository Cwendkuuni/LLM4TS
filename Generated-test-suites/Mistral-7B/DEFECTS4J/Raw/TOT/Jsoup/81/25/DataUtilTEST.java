package org.jsoup.helper.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    private final static String htmlCode = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><div>Test</div></body></html>";
    private final static String xmlCode = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root></root>";

    private Document htmlDocument;
    private Document xmlDocument;

    @Before
    public void setUp() {
        htmlDocument = DataUtil.parseString(htmlCode, StandardCharsets.UTF_8, null, Parser.htmlParser());
        xmlDocument = DataUtil.parseString(xmlCode, StandardCharsets.UTF_8, null, Parser.xmlParser());
    }

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        File testFile = new File("testFile.html");
        String expectedCharset = "UTF-8";
        String baseUri = "/";

        Document document = DataUtil.load(testFile, expectedCharset, baseUri);

        assertEquals("Loaded file with expected charset", expectedCharset, document.outputSettings().charset());
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File testFile = new File("testFile.html");
        String expectedCharset = "ISO-8859-1";
        String baseUri = "/";

        DataUtil.load(testFile, expectedCharset, baseUri);
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        InputStream testInputStream = new ByteArrayInputStream(htmlCode.getBytes(StandardCharsets.UTF_8));
        String expectedCharset = "UTF-8";
        String baseUri = "/";

        Document document = DataUtil.load(testInputStream, expectedCharset, baseUri);

        assertEquals("Loaded inputStream with expected charset", expectedCharset, document.outputSettings().charset());
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        InputStream testInputStream = new ByteArrayInputStream(htmlCode.getBytes(StandardCharsets.UTF_8));
        String expectedCharset = "ISO-8859-1";
        String baseUri = "/";

        DataUtil.load(testInputStream, expectedCharset, baseUri);
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        InputStream testInputStream = new ByteArrayInputStream(htmlCode.getBytes(StandardCharsets.UTF_8));
        String expectedCharset = "UTF-8";
        String baseUri = "/";

        Document document = DataUtil.parseInputStream(testInputStream, expectedCharset, baseUri, Parser.htmlParser());

        assertEquals("Parsed inputStream with expected charset", expectedCharset, document.outputSettings().charset());
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        InputStream testInputStream = new ByteArrayInputStream(htmlCode.getBytes(StandardCharsets.UTF_8));
        String expectedCharset = "ISO-8859-1";
        String baseUri = "/";

        DataUtil.parseInputStream(testInputStream, expectedCharset, baseUri, Parser.htmlParser());
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(htmlCode.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document document = DataUtil.parseInputStream(testInputStream, "UTF-8", null, Parser.htmlParser());

        assertEquals("Output stream content equals input stream content", htmlDocument.toString(), document.toString());
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();

        assertTrue("Generated boundary is at least 32 characters long", boundary.length() >= 32);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String resultCharset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("Returned charset from content type matches expected charset", expectedCharset, resultCharset);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream testInputStream = new ByteArrayInputStream(htmlCode.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInputStream);

        int expectedLength = htmlCode.length();
        int actualLength = (int) byteBuffer.limit();

        assertEquals("Returned byte buffer length matches input string length", expectedLength, actualLength);
    }

    @Test
    public void testParseInputStreamWithMetaData() throws IOException {
        InputStream testInputStream = new ByteArrayInputStream(htmlCode.getBytes(StandardCharsets.UTF_8));
        String expectedCharset = "UTF-8";
        String baseUri = "/";

        Document document = DataUtil.parseInputStream(testInputStream, expectedCharset, baseUri, Parser.htmlParser());

        String metaCharset = document.select("meta[http-equiv=content-type], meta[charset]").get(0).attr("content");
        String actualCharset = getCharsetFromContentType(metaCharset);

        assertEquals("Parsed inputStream with meta charset matches expected charset", expectedCharset, actualCharset);
    }

    @Test
    public void testParseInputStreamWithXmlDeclaration() throws IOException {
        InputStream testInputStream = new ByteArrayInputStream(xmlCode.getBytes(StandardCharsets.UTF_8));
        String expectedCharset = "UTF-8";
        String baseUri = "/";

        Document document = DataUtil.parseInputStream(testInputStream, expectedCharset, baseUri, Parser.xmlParser());

        Element xmlDeclaration = document.childNodes().get(0);
        String actualCharset = xmlDeclaration.attr("encoding");

        assertEquals("Parsed inputStream with xml declaration charset matches expected charset", expectedCharset, actualCharset);
    }

    private String getCharsetFromContentType(String contentType) {
        return DataUtil.getCharsetFromContentType(contentType);
    }
}