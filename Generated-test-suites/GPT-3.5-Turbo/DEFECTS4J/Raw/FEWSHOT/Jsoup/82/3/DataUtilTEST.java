package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import java.io.*;

import static org.junit.Assert.*;

public class DataUtilTest {
    private static final String FILE_PATH = "path/to/file";
    private static final String CHARSET = "UTF-8";
    private static final String BASE_URI = "https://example.com/";

    private static File testFile;

    @Before
    public void setUp() {
        testFile = new File(FILE_PATH);
    }

    @Test
    public void testLoadWithFile() {
        try {
            Document document = DataUtil.load(testFile, CHARSET, BASE_URI);
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testLoadWithInputStream() {
        try {
            InputStream inputStream = new FileInputStream(testFile);
            Document document = DataUtil.load(inputStream, CHARSET, BASE_URI);
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testLoadWithInputStreamAndParser() {
        try {
            InputStream inputStream = new FileInputStream(testFile);
            Document document = DataUtil.load(inputStream, CHARSET, BASE_URI, Parser.htmlParser());
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testCrossStreams() {
        try {
            InputStream inputStream = new FileInputStream(testFile);
            OutputStream outputStream = new ByteArrayOutputStream();
            DataUtil.crossStreams(inputStream, outputStream);
            assertTrue(outputStream.toString().length() > 0);
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream inputStream = new FileInputStream(testFile);
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
            assertNotNull(byteBuffer);
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertTrue(boundary.length() > 0);
    }

    @Test
    public void testDetectCharsetFromBom() {
        try {
            InputStream inputStream = new FileInputStream(testFile);
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
            DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
            assertNull(bomCharset);
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }
}