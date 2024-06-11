package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws Exception {
        File file = new File("testFile.html");
        Document document = DataUtil.load(file, null, null);
        assertNotNull(document);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws Exception {
        File file = new File("testFile.html");
        Document document = DataUtil.load(file, "UTF-8", null);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws Exception {
        File file = new File("testFile.html");
        InputStream inputStream = new FileInputStream(file);
        Document document = DataUtil.load(inputStream, null, null);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws Exception {
        File file = new File("testFile.html");
        InputStream inputStream = new FileInputStream(file);
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedParser() throws Exception {
        File file = new File("testFile.html");
        InputStream inputStream = new FileInputStream(file);
        Document document = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String html = "<html>\n<head>\n<meta charset='UTF-8'>\n<title>Test</title>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String html = "<html>\n<head>\n<meta charset='UTF-8'>\n<title>Test</title>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());
        Document document = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=ISO-8859-1";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("ISO-8859-1", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNull() {
        String contentType = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferThrowsIOException() throws IOException {
        File file = new File("testFile.html");
        InputStream inputStream = new FileInputStream(file);
        DataUtil.readToByteBuffer(null);
    }

    @Test
    public void testReadToByteBuffer() throws Exception {
        File file = new File("testFile.html");
        InputStream inputStream = new FileInputStream(file);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteData);
    }
}