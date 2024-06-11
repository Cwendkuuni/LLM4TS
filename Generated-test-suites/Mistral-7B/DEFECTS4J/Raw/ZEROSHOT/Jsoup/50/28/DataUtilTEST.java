package org.jsoup.helper;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil methods.
 */
public class DataUtilTest {

    private static final String TEST_FILE = "test.html";
    private static final String TEST_INPUT_STREAM = "test_input_stream.txt";

    private File testFile;
    private InputStream testInputStream;

    @Before
    public void setup() throws IOException {
        testFile = new File(TEST_FILE);
        testInputStream = new FileInputStream(TEST_FILE);
    }

    @Test
    public void testLoadFile() throws IOException {
        Document doc = DataUtil.load(testFile, null, null);
        assertNotNull(doc);
        testFile.deleteOnExit();
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        Document doc = DataUtil.load(testFile, "InvalidCharset", null);
        fail("Expected IOException was not thrown.");
    }

    @Test
    public void testLoadInputStream() throws IOException {
        Document doc = DataUtil.load(testInputStream, null, null);
        assertNotNull(doc);
        testInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        Document doc = DataUtil.load(testInputStream, "InvalidCharset", null);
        fail("Expected IOException was not thrown.");
    }

    @Test
    public void testParseByteData() {
        File testFile = new File(TEST_FILE);
        InputStream testInputStream = new FileInputStream(testFile);
        ByteBuffer byteData = DataUtil.readToByteBuffer(testInputStream);
        testInputStream.close();
        testFile.deleteOnExit();

        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File testFile = new File("test_output.html");
        InputStream testInputStream = new FileInputStream(TEST_FILE);
        OutputStream testOutputStream = new FileOutputStream(testFile);

        DataUtil.crossStreams(testInputStream, testOutputStream);
        testInputStream.close();
        testOutputStream.close();

        File testFileVerification = new File("test_verification.html");
        Document doc = DataUtil.load(testFileVerification, null, null);
        assertNotNull(doc);
        testFileVerification.deleteOnExit();
    }

    @Test(expected = IOException.class)
    public void testCrossStreamsWithError() throws IOException {
        InputStream testInputStream = new FileInputStream(TEST_FILE);
        OutputStream testOutputStream = new FileOutputStream("test_output_error.html");

        DataUtil.crossStreams(testInputStream, testOutputStream);
        testInputStream.close();
        testOutputStream.close();

        File testFileVerification = new File("test_verification_error.html");
        Document doc = DataUtil.load(testFileVerification, null, null);
        fail("Expected IOException was not thrown.");
        testFileVerification.deleteOnExit();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html; charset=US-ASCII";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("US-ASCII", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        int length = boundary.length();
        for (int i = 0; i < length; i++) {
            char c = boundary.charAt(i);
            assertTrue("Char " + c + " is a valid mime boundary character.",
                    DataUtil.isValidMimeBoundaryCharacter(c));
        }
    }
}