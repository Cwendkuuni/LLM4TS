package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {
    private static final String TEST_FILE_PATH = "src/test/resources/test.html";

    private File testFile;

    @Before
    public void setUp() {
        testFile = new File(TEST_FILE_PATH);
    }

    @Test
    public void testLoadFromFile() throws IOException {
        Document document = DataUtil.load(testFile, "UTF-8", "");
        assertNotNull(document);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inputStream = new FileInputStream(testFile);
        Document document = DataUtil.load(inputStream, "UTF-8", "");
        assertNotNull(document);
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream(testFile);
        Document document = DataUtil.load(inputStream, "UTF-8", "", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String expected = "Hello, world!";
        InputStream inputStream = new ByteArrayInputStream(expected.getBytes());
        OutputStream outputStream = new OutputStream() {
            private StringBuilder result = new StringBuilder();

            @Override
            public void write(int b) {
                result.append((char) b);
            }

            @Override
            public String toString() {
                return result.toString();
            }
        };

        DataUtil.crossStreams(inputStream, outputStream);

        assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testParseByteData() throws IOException {
        byte[] bytes = "Hello, world!".getBytes();
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        String charsetName = "UTF-8";
        String baseUri = "";

        Document document = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream(testFile);
        int maxSize = (int) testFile.length();

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        assertNotNull(byteBuffer);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);
        assertNotNull(byteBuffer);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertNotNull(byteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
    }

    @Test
    public void testDetectCharsetFromBom() {
        String charsetName = null;
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
        charsetName = DataUtil.detectCharsetFromBom(byteBuffer, charsetName);
        assertEquals("UTF-8", charsetName);
    }
}