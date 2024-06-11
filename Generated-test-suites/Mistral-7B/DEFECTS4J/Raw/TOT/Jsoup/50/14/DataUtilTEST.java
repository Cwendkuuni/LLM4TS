package org.jsoup.helper.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.helper.Validate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class DataUtilTest {

    private Parser parser;
    private Document doc;

    @Before
    public void setup() {
        parser = Parser.htmlParser();
    }

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html"); // replace with a valid file path
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Document expectedDocument = parser.parse(file);
        Document actualDocument = DataUtil.load(file, charsetName, baseUri);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String html = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Document expectedDocument = parser.parse(html);
        Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadFromInputStreamWithInvalidCharset() throws IOException {
        String html = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        String charsetName = "INVALID_CHARSET";
        String baseUri = "http://example.com/";
        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(inputStream, charsetName, baseUri));
    }

    @Test
    public void testParseByteData() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap("UTF-8\uFEFF<html><body></body></html>".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Document expectedDocument = parser.parseInput("<html><body></body></html>", baseUri);
        Document actualDocument = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap("<html><body></body></html>".getBytes("UTF-8"));
        String charsetName = "INVALID_CHARSET";
        String baseUri = "http://example.com/";
        Document actualDocument = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNull(actualDocument);
    }

    @Test
    public void testParseByteDataWithBOM() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap("\uFEFF<html><body></body></html>".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Document expectedDocument = parser.parseInput("<html><body></body></html>", baseUri);
        expectedDocument.outputSettings().charset("UTF-8");
        Document actualDocument = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testParseByteDataWithMetaCharset() throws IOException {
        String html = "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\">\n" +
                "</head>\n" +
                "<body></body>\n" +
                "</html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes("UTF-8"));
        String charsetName = null;
        String baseUri = "http://example.com/";
        Document expectedDocument = parser.parseInput(html, baseUri);
        Document actualDocument = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String input = "Test data";
        InputStream in = new ByteArrayInputStream(input.getBytes("UTF-8"));
        OutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out);
        String expectedOutput = input;
        String actualOutput = out.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String actualCharset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", actualCharset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharsetName() {
        String contentType = "text/html; charset=INVALID_CHARSET";
        String actualCharset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(actualCharset);
    }
}