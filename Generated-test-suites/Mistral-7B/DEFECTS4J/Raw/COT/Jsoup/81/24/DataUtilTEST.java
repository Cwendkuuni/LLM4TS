package org.jsoup.helper; // Make sure the test file is in the same package as the DataUtil class

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.helper.DataUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.Assert.*;

public class DataUtilTest {

    private InputStream testFileStream;

    @Before
    public void setup() {
        String testFile = "test.html"; // Replace this with the path to your test file
        File file = new File(testFile);
        this.testFileStream = new FileInputStream(file);
    }

    @Test
    public void testLoadFileDocument() throws IOException {
        Document doc = DataUtil.load(new File("test.html"), null, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamDocument_UTF8() throws IOException {
        Document doc = DataUtil.load(this.testFileStream, StandardCharsets.UTF_8.name(), null);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamDocument_InvalidCharset() throws IOException {
        Document doc = null;
        try {
            doc = DataUtil.load(this.testFileStream, "invalidCharset", null);
        } catch (IOException e) {
            assertEquals("Expected IOException when loading document with an invalid charset", e.getMessage());
        }
        assertNull(doc);
    }

    @Test
    public void testParseInputStreamDocument() throws IOException {
        Document doc = DataUtil.parseInputStream(this.testFileStream, null, null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream input = new FileInputStream("test.input");
        OutputStream output = new FileOutputStream("test.output");
        try {
            DataUtil.crossStreams(input, output);
            File testFile = new File("test.output");
            Document doc = DataUtil.parseInputStream(new FileInputStream(testFile), null, null, Parser.htmlParser());
            assertNotNull(doc);
        } finally {
            output.close();
            input.close();
            testFile.delete();
        }
    }

    @Test
    public void testParseInputStreamDocument_InvalidCharset() throws IOException {
        InputStream input = new FileInputStream("test.input");
        Document doc = null;
        try {
            doc = DataUtil.parseInputStream(input, "invalidCharset", null, Parser.htmlParser());
            fail("Expected IOException when loading document with an invalid charset");
        } catch (IOException e) {
            // Expected
        } finally {
            input.close();
        }
        assertNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream input = new FileInputStream("test.input");
        ByteBuffer buffer = DataUtil.readToByteBuffer(input);
        long expectedSize = new File("test.input").length();
        assertEquals("Expected correct byte buffer size", expectedSize, buffer.limit());
        input.close();
    }

    @Test
    public void testReadToByteBuffer_EmptyFile() throws IOException {
        ByteBuffer buffer = DataUtil.readToByteBuffer(new FileInputStream("emptyFile.txt"));
        assertEquals("Expected empty byte buffer", 0, buffer.limit());
        new File("emptyFile.txt").delete();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("Expected 'UTF-8'", "UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull("Expected null when no charset is present", charset);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateCharset_EmptyString() {
        String charset = "";
        String expectedExceptionMessage = "Expected non-empty string";
        try {
            DataUtil.validateCharset(charset);
            fail("Expected IllegalArgumentException when validating an empty charset");
        } catch (IllegalArgumentException e) {
            assertEquals(expectedExceptionMessage, e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateCharset_NullCharset() {
        String charset = null;
        String expectedExceptionMessage = "Expected non-null charset";
        try {
            DataUtil.validateCharset(charset);
            fail("Expected IllegalArgumentException when validating a null charset");
        } catch (IllegalArgumentException e) {
            assertEquals(expectedExceptionMessage, e.getMessage());
        }
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull("Expected a mime boundary string", boundary);
        Random random = new Random();
        String randomBoundary = DataUtil.mimeBoundary();
        assertNotSame("Expected different mime boundary strings", boundary, randomBoundary);
    }
}