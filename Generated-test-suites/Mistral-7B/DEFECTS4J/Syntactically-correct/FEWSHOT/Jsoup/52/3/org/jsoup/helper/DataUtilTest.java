package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Test cases for the DataUtil class.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File file = new File("test.html"); // Replace with the path to a valid HTML file.

        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("<!--html-->".getBytes());
        outputStream.write("<head>".getBytes());
        outputStream.write("<meta charset='UTF-8'>".getBytes());
        outputStream.write("</head>".getBytes());
        outputStream.write("<body>Test</body>".getBytes());
        outputStream.write("-->".getBytes());

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        ByteBuffer byteData = ByteBuffer.wrap("<!--html-->".getBytes()); // Replace with valid byte data.

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        String baseUri = "http://example.com";

        ByteBuffer byteData = ByteBuffer.wrap("<!DOCTYPE html>\n".getBytes()); // Replace with valid byte data.

        Document document = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());
        assertNotNull(document);

        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testCharsetFromContentTypeWithUnsupportedCharset() {
        String contentType = "text/html; charset=X-PASCAL";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertTrue(boundary.length() == 32);
    }
}