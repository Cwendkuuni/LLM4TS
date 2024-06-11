package org.jsoup.helper;

import static org.junit.Assert.*;
import org.junit.Test;

import org.jsoup.nodes.Document;
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
    public void testLoad() {
        try {
            File in = new File("test.html");
            Document doc = DataUtil.load(in, "UTF-8", "");
            assertNotNull(doc);
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException when loading file");
        }
    }

    @Test
    public void testLoad_Inputstream() {
        try {
            InputStream in = new FileInputStream("test.html");
            Document doc = DataUtil.load(in, "UTF-8", "");
            assertNotNull(doc);
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException when loading input stream");
        }
    }

    @Test
    public void testLoad_InputstreamParser() {
        try {
            InputStream in = new FileInputStream("test.html");
            Parser parser = Parser.htmlParser();
            Document doc = DataUtil.load(in, "UTF-8", "", parser);
            assertNotNull(doc);
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException when loading input stream with parser");
        }
    }

    @Test
    public void testCrossStreams() {
        try {
            InputStream in = new ByteArrayInputStream("test".getBytes());
            OutputStream out = new ByteArrayOutputStream();
            DataUtil.crossStreams(in, out);
            assertEquals("test", out.toString());
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException when crossing streams");
        }
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream inStream = new ByteArrayInputStream("test".getBytes());
            int maxSize = 0;
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream, maxSize);
            assertEquals("test", new String(byteBuffer.array()));
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException when reading to byte buffer");
        }
    }

    @Test
    public void testReadToByteBuffer_WithMaxSize() {
        try {
            InputStream inStream = new ByteArrayInputStream("test".getBytes());
            int maxSize = 10;
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream, maxSize);
            assertEquals("test", new String(byteBuffer.array()));
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException when reading to byte buffer with max size");
        }
    }

    @Test
    public void testReadToByteBuffer_Inputstream() {
        try {
            InputStream inStream = new ByteArrayInputStream("test".getBytes());
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream);
            assertEquals("test", new String(byteBuffer.array()));
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException when reading to byte buffer from input stream");
        }
    }

    @Test
    public void testReadFileToByteBuffer() {
        try {
            File file = new File("test.txt");
            ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
            assertEquals("test", new String(byteBuffer.array()));
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException when reading file to byte buffer");
        }
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_WithUnsupportedCharset() {
        String contentType = "text/html; charset=unsupportedCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType_WithEmptyCharset() {
        String contentType = "text/html; charset=";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(DataUtil.boundaryLength, boundary.length());
    }
}