package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.TextNode;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    private static final String testFileName = "testFile.html";

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File(testFileName);
        Document document = DataUtil.load(file, null);
        assertNotNull(document);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File(testFileName);
        Document document = DataUtil.load(file, "UTF-8");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        File file = new File(testFileName);
        FileInputStream inputStream = new FileInputStream(file);
        Document document = DataUtil.load(inputStream, null);
        assertNotNull(document);
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        File file = new File(testFileName);
        FileInputStream inputStream = new FileInputStream(file);
        Document document = DataUtil.load(inputStream, "UTF-8");
        assertNotNull(document);
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithSpecifiedParser() throws IOException {
        File file = new File(testFileName);
        FileInputStream inputStream = new FileInputStream(file);
        Document document = DataUtil.load(inputStream, null, null, Parser.xmlParser());
        assertNotNull(document);
        inputStream.close();
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<body>\n<p>Hello World</p>\n</body>\n</html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes("UTF-8"));
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<body>\n<p>Hello World</p>\n</body>\n</html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes("UTF-8"));
        Document document = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithDetectedCharset() {
        String testData = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n<html>\n<body>\n<p>Hello World</p>\n</body>\n</html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes("UTF-8"));
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals("ISO-8859-1", document.outputSettings().charset());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        File file = new File(testFileName);
        FileInputStream inputStream = new FileInputStream(file);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteData);
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithNullInputStream() {
        ByteBuffer byteData = DataUtil.readToByteBuffer(null);
        fail("Expected IOException");
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));

        String contentTypeNoCharset = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentTypeNoCharset));
    }
}