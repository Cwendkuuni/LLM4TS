package org.jsoup.helper;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test cases for DataUtil class.
 */
public class DataUtilTest {

    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertEquals("GBK", DataUtil.getCharsetFromContentType("text/html; charset=GBK"));
        assertEquals("EUC-JP", DataUtil.getCharsetFromContentType("text/html; charset=EUC-JP"));
        assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType("text/html; charset=ISO-8859-1"));
        assertEquals("US-ASCII", DataUtil.getCharsetFromContentType("text/html; charset=US-ASCII"));
        assertEquals("UTF-16", DataUtil.getCharsetFromContentType("text/html; charset=UTF-16"));
        assertNull(DataUtil.getCharsetFromContentType("text/html"));
        assertNull(DataUtil.getCharsetFromContentType("application/json"));
    }

    @Test
    public void testParseByteData() {
        String utf8Data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Hello, World!</h1></body></html>";
        Document doc = DataUtil.parseByteData(ByteBuffer.wrap(utf8Data.getBytes(Charset.forName("UTF-8"))), null, null, Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals("<html><body><h1>Hello, World!</h1></body></html>", doc.html());

        String charsetDetectedData = "<?xml version=\"1.0\" encoding=\"gb2312\"?>\n<html><body><h1>你好，世界！</h1></body></html>";
        doc = DataUtil.parseByteData(ByteBuffer.wrap(charsetDetectedData.getBytes(Charset.forName("UTF-8"))), null, null, Parser.htmlParser());
        assertEquals("gb2312", doc.outputSettings().charset());
        assertEquals("<html><body><h1>你好，世界！</h1></body></html>", doc.html());
    }

    @Test
    public void testLoadFile() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        File inputFile = new File("test.html");
        Document doc = DataUtil.load(inputFile, charset, baseUri);
        assertEquals(charset, doc.outputSettings().charset());
        assertNotNull(doc.html());

        inputFile = new File("test_gb2312.html");
        doc = DataUtil.load(inputFile, null, baseUri);
        assertEquals("gb2312", doc.outputSettings().charset());
        assertNotNull(doc.html());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        InputStream inputStream = new FileInputStream(new File("test.html"));
        Document doc = DataUtil.load(inputStream, charset, baseUri);
        assertEquals(charset, doc.outputSettings().charset());
        assertNotNull(doc.html());

        InputStream gb2312InputStream = new FileInputStream(new File("test_gb2312.html"));
        doc = DataUtil.load(gb2312InputStream, null, baseUri);
        assertEquals("gb2312", doc.outputSettings().charset());
        assertNotNull(doc.html());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();

        InputStream inputStream = new FileInputStream(new File("test.html"));
        Document doc = DataUtil.load(inputStream, charset, baseUri, parser);
        assertEquals(charset, doc.outputSettings().charset());
        assertNotNull(doc.xml());

        InputStream gb2312InputStream = new FileInputStream(new File("test_gb2312.html"));
        doc = DataUtil.load(gb2312InputStream, null, baseUri, parser);
        assertEquals("gb2312", doc.outputSettings().charset());
        assertNotNull(doc.xml());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(Charset.forName("UTF-8")));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        byte[] expectedBuffer = inputData.getBytes(Charset.forName("UTF-8"));
        byte[] actualBuffer = new byte[byteData.limit()];
        byteData.get(actualBuffer);
        assertArrayEquals(expectedBuffer, actualBuffer);
    }
}