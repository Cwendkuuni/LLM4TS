package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithNullCharset() throws IOException {
        File file = new File("test.html");
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, null, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("test.html");
        String charsetName = "INVALID_CHARSET";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals(DataUtil.defaultCharset, doc.outputSettings().charset());
    }

    @Test
    public void testLoadFileWithEmptyCharset() throws IOException {
        File file = new File("test.html");
        String charsetName = "";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals(DataUtil.defaultCharset, doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testString = "Test string";
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        OutputStream writer = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                os.write(b);
            }

            @Override
            public void close() throws IOException {
                // no-op
            }
        };
        os.write(testString.getBytes(DataUtil.defaultCharset));
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(is, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals(charsetName, doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String testString = "Test string";
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        OutputStream writer = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                os.write(b);
            }

            @Override
            public void close() throws IOException {
                // no-op
            }
        };
        os.write(testString.getBytes(DataUtil.defaultCharset));
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(is, null, baseUri);
        assertNotNull(doc);
        assertEquals(DataUtil.defaultCharset, doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String testString = "Test string";
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        OutputStream writer = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                os.write(b);
            }

            @Override
            public void close() throws IOException {
                // no-op
            }
        };
        os.write(testString.getBytes(DataUtil.defaultCharset));
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        String charsetName = "INVALID_CHARSET";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(is, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals(DataUtil.defaultCharset, doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithEmptyCharset() throws IOException {
        String testString = "Test string";
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        OutputStream writer = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                os.write(b);
            }

            @Override
            public void close() throws IOException {
                // no-op
            }
        };
        os.write(testString.getBytes(DataUtil.defaultCharset));
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(is, "", baseUri);
        assertNotNull(doc);
        assertEquals(DataUtil.defaultCharset, doc.outputSettings().charset());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String testString = "Test string";
        byte[] buffer = testString.getBytes();
        InputStream in = new ByteArrayInputStream(buffer);
        OutputStream out = new FileOutputStream("output.txt");
        DataUtil.crossStreams(in, out);
        byte[] outputBuffer = new byte[buffer.length];
        FileInputStream outputIn = new FileInputStream("output.txt");
        outputIn.read(outputBuffer);
        assertEquals(testString, new String(outputBuffer));
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String testString = "Test string";
        ByteBuffer byteData = ByteBuffer.wrap(testString.getBytes(DataUtil.defaultCharset));
        String charsetName = null;
        String baseUri = "http://example.com";
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(DataUtil.defaultCharset, doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String testString = "Test string";
        ByteBuffer byteData = ByteBuffer.wrap(testString.getBytes(DataUtil.defaultCharset));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(charsetName, doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithInvalidCharsetFromContentType() throws IOException {
        String testString = "Test string";
        ByteBuffer byteData = ByteBuffer.wrap(testString.getBytes(DataUtil.defaultCharset));
        String charsetName = "INVALID_CHARSET";
        String baseUri = "http://example.com";
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(DataUtil.defaultCharset, doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithEmptyCharset() throws IOException {
        String testString = "Test string";
        ByteBuffer byteData = ByteBuffer.wrap(testString.getBytes(DataUtil.defaultCharset));
        String charsetName = "";
        String baseUri = "http://example.com";
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(DataUtil.defaultCharset, doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithNoCharsetInContentType() throws IOException {
        String testString = "Test string";
        ByteBuffer byteData = ByteBuffer.wrap(testString.getBytes(DataUtil.defaultCharset));
        String charsetName = null;
        String baseUri = "http://example.com";
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(DataUtil.defaultCharset, doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithCharsetInContentType() throws IOException {
        String testString = "Test string";
        ByteBuffer byteData = ByteBuffer.wrap(testString.getBytes(DataUtil.defaultCharset));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(charsetName, doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithUnicodeBom() throws IOException {
        String testString = "Test string\uFFFE";
        ByteBuffer byteData = ByteBuffer.wrap(testString.getBytes(DataUtil.defaultCharset));
        String charsetName = null;
        String baseUri = "http://example.com";
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(DataUtil.defaultCharset, doc.outputSettings().charset());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testString = "Test string";
        InputStream is = new ByteArrayInputStream(testString.getBytes(DataUtil.defaultCharset));
        ByteBuffer byteData = DataUtil.readToByteBuffer(is);
        assertEquals(testString.getBytes(DataUtil.defaultCharset).length, byteData.limit());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String testString = "Test string";
        InputStream is = new ByteArrayInputStream(testString.getBytes(DataUtil.defaultCharset));
        ByteBuffer byteData = DataUtil.readToByteBuffer(is, 10);
        assertEquals(10, byteData.limit());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);
        long fileSize = file.length();
        assertEquals(fileSize, byteData.limit());
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        assertEquals(0, byteData.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String testContentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(testContentType);
        assertEquals("UTF-8", charset);

        testContentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(testContentType);
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        int boundaryLength = boundary.length();
        for (int i = 0; i < boundaryLength; i++) {
            char c = boundary.charAt(i);
            assertTrue(DataUtil.mimeBoundaryCharsContains(c));
        }
    }

    private static boolean mimeBoundaryCharsContains(char c) {
        for (char mimeBoundaryChar : DataUtil.mimeBoundaryChars) {
            if (mimeBoundaryChar == c) {
                return true;
            }
        }
        return false;
    }
}