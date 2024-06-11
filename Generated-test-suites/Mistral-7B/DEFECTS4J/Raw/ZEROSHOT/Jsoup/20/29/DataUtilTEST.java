package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(JUnit4.class)
public class DataUtilTest {

    private static File testFile;
    private static InputStream testInputStream;
    private static Document doc;

    @BeforeClass
    public static void setup() throws Exception {
        testFile = new File("test.html"); // replace with path to your test file
        doc = DataUtil.load(testFile, "UTF-8", null); // replace with appropriate charset and baseUri
    }

    @Test
    public void testLoadFile() throws Exception {
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws Exception {
        Document doc = null;
        try {
            doc = DataUtil.load(testFile, null, null);
        } catch (IOException e) {
            // expected
        }
        assertNull(doc);
    }

    @Test
    public void testLoadInputStream() throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("".getBytes()); // replace with content of your input stream
        doc = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("".getBytes()); // replace with content of your input stream
        doc = DataUtil.load(inputStream, null, null);
        fail("Should have thrown an IOException");
    }

    @Test
    public void testParseByteData() throws Exception {
        byte[] byteData = doc.html().getBytes("UTF-8");
        Document doc2 = DataUtil.parseByteData(ByteBuffer.wrap(byteData), "UTF-8", null, Parser.htmlParser());
        assertEquals(doc, doc2);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws Exception {
        byte[] byteData = doc.html().getBytes("UTF-8");
        Document doc2 = DataUtil.parseByteData(ByteBuffer.wrap(byteData), null, null, Parser.htmlParser());
        assertNotEquals(doc, doc2);

        // try to re-decode with a valid charset
        String validCharset = "UTF-16";
        Document doc3 = DataUtil.parseByteData(ByteBuffer.wrap(byteData), validCharset, null, Parser.htmlParser());
        assertEquals(doc, doc3);
    }

    @Test
    public void testReadToByteBuffer() throws Exception {
        FileInputStream inStream = new FileInputStream(testFile);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
        assertNotNull(byteData);
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithInvalidInputStream() throws Exception {
        FileInputStream inStream = null;
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
        fail("Should have thrown an IOException");
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }
}