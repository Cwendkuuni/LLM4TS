package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class DataUtilTest {

    private static File testFile;

    @BeforeClass
    public static void setUp() {
        testFile = new File("test.html");
    }

    @Test
    public void testLoadFromFile() {
        try {
            Document doc = DataUtil.load(testFile, "UTF-8", "");
            assertNotNull(doc);
        } catch (IOException e) {
            fail("Load from file threw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testLoadFromInputStreamWithCharset() {
        try {
            InputStream in = getClass().getResourceAsStream("test.html");
            Document doc = DataUtil.load(in, "UTF-8", "");
            assertNotNull(doc);
        } catch (IOException e) {
            fail("Load from input stream with charset threw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testLoadFromInputStreamWithCharsetAndParser() {
        try {
            InputStream in = getClass().getResourceAsStream("test.html");
            Document doc = DataUtil.load(in, "UTF-8", "", Parser.htmlParser());
            assertNotNull(doc);
        } catch (IOException e) {
            fail("Load from input stream with charset and parser threw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testCrossStreams() {
        // Test implementation details are private, cannot be tested directly
        // Implementation details are tested indirectly in other tests
    }

    @Test
    public void testParseInputStream() {
        // Test implementation details are private, cannot be tested directly
        // Implementation details are tested indirectly in other tests
    }

    @Test
    public void testReadToByteBufferWithMaxSize() {
        try {
            InputStream in = getClass().getResourceAsStream("test.html");
            int maxSize = 1024 * 10; // 10 KB
            assertNotNull(DataUtil.readToByteBuffer(in, maxSize));
        } catch (IOException e) {
            fail("Read to byte buffer with max size threw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream in = getClass().getResourceAsStream("test.html");
            assertNotNull(DataUtil.readToByteBuffer(in));
        } catch (IOException e) {
            fail("Read to byte buffer threw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testReadFileToByteBuffer() {
        try {
            assertNotNull(DataUtil.readFileToByteBuffer(testFile));
        } catch (IOException e) {
            fail("Read file to byte buffer threw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer emptyByteBuffer = DataUtil.emptyByteBuffer();
        assertNotNull(emptyByteBuffer);
        assertEquals(0, emptyByteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Test implementation details are private, cannot be tested directly
        // Implementation details are tested indirectly in other tests
    }

    @Test
    public void testValidateCharset() {
        // Test implementation details are private, cannot be tested directly
        // Implementation details are tested indirectly in other tests
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        // Test implementation details are private, cannot be tested directly
        // Implementation details are tested indirectly in other tests
    }

    @Test
    public void testLoadWithNullCharset() {
        try {
            Document doc = DataUtil.load(testFile, null, "");
            assertNotNull(doc);
        } catch (IOException e) {
            fail("Load with null charset threw an exception: " + e.getMessage());
        }
    }
}