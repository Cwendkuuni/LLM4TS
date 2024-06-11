package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File("testFile.html");
        Document doc = DataUtil.load(testFile, null, null);
        assertNotNull(doc);
        assertTrue(doc.body().text().length() > 0);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testString = "<html><body>Test</body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(testString.getBytes("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(doc);
        assertTrue(doc.body().text().equals("Test"));
    }

    @Test
    public void testParseByteData() throws IOException {
        String testString = "<html><body>Test</body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(testString.getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(byteBuffer, "UTF-8", null, Parser.htmlParser());
        assertNotNull(doc);
        assertTrue(doc.body().text().equals("Test"));
    }

    @Test
    public void testParseByteDataDetectCharset() throws IOException {
        String testString = "<html><head><meta charset=\"ISO-8859-1\">";
        testString += "<body>Test</body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(testString.getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("ISO-8859-1", doc.outputSettings().charset());
        assertTrue(doc.body().text().equals("Test"));
    }

    @Test
    public void testParseByteDataDetectCharsetInvalid() throws IOException {
        String testString = "<html><head><meta charset=\"invalidCharset\">";
        testString += "<body>Test</body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(testString.getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());
        assertNull(doc);
    }

    @Test
    public void testParseByteDataDetectCharsetNone() throws IOException {
        String testString = "<html><body>Test</body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(testString.getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertTrue(doc.body().text().equals("Test"));
    }

    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType("text/html; charset=ISO-8859-1"));
        assertEquals("US-ASCII", DataUtil.getCharsetFromContentType("text/plain"));
        assertNull(DataUtil.getCharsetFromContentType("text/plain"));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertEquals(boundary.length(), 32);
        for (int i = 0; i < boundary.length(); i++) {
            assertTrue(Character.isAlphANumeric(boundary.charAt(i)));
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals(0, outputStream.size());

        inputStream = new ByteArrayInputStream("Test".getBytes());
        outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals("Test", outputStream.toString());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertEquals(7, byteBuffer.limit());
        byte[] bytes = new byte[7];
        byteBuffer.get(bytes);
        assertEquals("Test", new String(bytes, "UTF-8"));
    }

    @Test
    public void testReadToByteBufferEmpty() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new ByteArrayInputStream(new byte[0]));
        assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File testFile = new File("testFile.html");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);
        assertFalse(byteBuffer.hasArray());
        assertEquals(testFile.length(), byteBuffer.limit());
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertEquals(0, byteBuffer.limit());
        assertTrue(byteBuffer.hasRemaining());
    }

    @Test
    public void testParseByteDataUnsupportedCharset() {
        String testString = "<html><head><meta charset=\"UTF-16BE\">";
        testString += "<body>Test</body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(testString.getBytes("UTF-8"));
        try {
            DataUtil.parseByteData(byteBuffer, "UTF-16BE", null, Parser.htmlParser());
            fail();
        } catch (UnsupportedEncodingException e) {
            // Expected
        }
    }
}