package org.jsoup.helper;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", null);
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><body><h1>Test</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testDetectCharset() throws IOException {
        String html1 = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body></body></html>";
        ByteArrayInputStream inputStream1 = new ByteArrayInputStream(html1.getBytes("UTF-8"));
        Document doc1 = DataUtil.load(inputStream1, null, null);
        assertEquals("UTF-8", doc1.outputSettings().charset());

        String html2 = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\"></head><body></body></html>";
        ByteArrayInputStream inputStream2 = new ByteArrayInputStream(html2.getBytes("UTF-8"));
        Document doc2 = DataUtil.load(inputStream2, null, null);
        assertEquals("gb2312", doc2.outputSettings().charset());

        String html3 = "<html><head></head><body></body></html>";
        ByteArrayInputStream inputStream3 = new ByteArrayInputStream(html3.getBytes("UTF-8"));
        Document doc3 = DataUtil.load(inputStream3, null, null);
        assertEquals("UTF-8", doc3.outputSettings().charset());
    }

    @Test
    public void testParseByteData() {
        String html = "<html><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String source = "Source text.";
        String target = "Target text.";
        String input = source + target;

        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes("UTF-8"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        String result = outputStream.toString("UTF-8");
        assertEquals(input, result);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><body><h1>Test</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        byte[] expected = html.getBytes("UTF-8");
        byte[] actual = new byte[byteData.limit()];
        byteData.get(actual);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);

        byte[] expected = Files.readAllBytes(file.toPath());
        byte[] actual = new byte[byteData.limit()];
        byteData.get(actual);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertEquals(0, byteBuffer.limit());
        assertEquals(0, byteBuffer.position());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String charset1 = DataUtil.getCharsetFromContentType(contentType1);
        assertEquals("UTF-8", charset1);

        String contentType2 = "text/html";
        String charset2 = DataUtil.getCharsetFromContentType(contentType2);
        assertNull(charset2);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertEquals(32, boundary.length());
        for (int i = 0; i < boundary.length(); i++) {
            char c = boundary.charAt(i);
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c) || c == '-' || c == '_');
        }
    }
}