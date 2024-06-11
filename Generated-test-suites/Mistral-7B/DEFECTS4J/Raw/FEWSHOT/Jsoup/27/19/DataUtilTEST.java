package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.nodes.Xml;
import org.jsoup.parser.Parser.htmlParser;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        File file = new File("src/test/resources/test.html");
        Document doc = DataUtil.load(file, charset, baseUri);
        assertNotNull(doc);
        assertEquals("HTML", doc.tagName());

        charset = null;
        doc = DataUtil.load(file, charset, baseUri);
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());

        doc = DataUtil.load(file, "ISO-8859-1", baseUri);
        assertEquals("ISO-8859-1", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        String testString = "<html><body><h1>Hello World</h1></body></html>";
        ByteArrayInputStream in = new ByteArrayInputStream(testString.getBytes());
        Document doc = DataUtil.load(in, charset, baseUri);
        assertNotNull(doc);
        assertEquals("HTML", doc.tagName());

        charset = null;
        doc = DataUtil.load(in, charset, baseUri);
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());

        charset = "ISO-8859-1";
        doc = DataUtil.load(in, charset, baseUri);
        assertEquals("ISO-8859-1", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        String testString = "<html><body><h1>Hello World</h1></body></html>";
        ByteArrayInputStream in = new ByteArrayInputStream(testString.getBytes());
        Document doc = DataUtil.load(in, charset, baseUri, htmlParser());
        assertNotNull(doc);
        assertEquals("HTML", doc.tagName());

        charset = null;
        doc = DataUtil.load(in, charset, baseUri, htmlParser());
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());

        charset = "ISO-8859-1";
        doc = DataUtil.load(in, charset, baseUri, htmlParser());
        assertEquals("ISO-8859-1", doc.outputSettings().charset());

        Parser xmlParser = Parser.xmlParser();
        doc = DataUtil.load(in, charset, baseUri, xmlParser);
        assertEquals("xml", doc.tagName());
    }

    @Test
    public void testParseByteData() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        String testString = "<html><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testString.getBytes());
        Document doc = DataUtil.parseByteData(byteData, charset, baseUri, htmlParser());
        assertNotNull(doc);
        assertEquals("HTML", doc.tagName());

        charset = null;
        doc = DataUtil.parseByteData(byteData, charset, baseUri, htmlParser());
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());

        charset = "ISO-8859-1";
        doc = DataUtil.parseByteData(byteData, charset, baseUri, htmlParser());
        assertEquals("ISO-8859-1", doc.outputSettings().charset());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testString = "Test string";
        InputStream in = new ByteArrayInputStream(testString.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(in);

        byte[] expectedBytes = testString.getBytes();
        assertArrayEquals(expectedBytes, byteData.array());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=ISO-8859-1";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("ISO-8859-1", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}