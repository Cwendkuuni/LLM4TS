package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("US-ASCII", DataUtil.getCharsetFromContentType("text/plain; charset=US-ASCII"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/plain; charset=UTF-8"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/plain; charset=utf-8"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/plain; charset=UTF-8"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/plain; charset=UTF-8"));
        assertEquals(null, DataUtil.getCharsetFromContentType("text/plain"));
        assertEquals(null, DataUtil.getCharsetFromContentType("text/plain; charset="));
        assertEquals(null, DataUtil.getCharsetFromContentType("text/plain; charset=x-unknown-charset"));
    }

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        String fileName = "testData.html";
        File testFile = new File(fileName);
        String baseUri = "file:///" + testFile.getAbsolutePath();
        Document document = DataUtil.load(testFile, "UTF-8", baseUri);
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testLoadDocumentFromInputStream() throws IOException {
        String testData = "This is a test document.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-8"));
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals(testData, document.body().text());
    }

    @Test
    public void testParseByteData() {
        String testData = "This is a test document.";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes("UTF-8"));
        Document document = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals(testData, document.body().text());

        byteData.rewind();
        document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals(testData, document.body().text());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "This is a test document.";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-8"));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(testData.length(), byteData.limit());
        String decodedData = Charset.forName("UTF-8").decode(byteData).toString();
        assertEquals(testData, decodedData);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String fileName = "testData.html";
        File testFile = new File(fileName);
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(testFile);
        assertEquals(testFile.length(), byteData.limit());
        String decodedData = Charset.forName("UTF-8").decode(byteData).toString();
        String expectedData = new String(Files.readAllBytes(Paths.get(fileName)), Charset.forName("UTF-8"));
        assertEquals(expectedData, decodedData);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertTrue(boundary.length() >= DataUtil.boundaryLength);
        for (int i = 0; i < boundary.length(); i++) {
            assertTrue(DataUtil.mimeBoundaryChars.contains(boundary.charAt(i)));
        }
    }
}