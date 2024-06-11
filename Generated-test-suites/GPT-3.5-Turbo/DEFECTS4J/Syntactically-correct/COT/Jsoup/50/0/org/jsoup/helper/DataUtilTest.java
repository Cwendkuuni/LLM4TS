
package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class DataUtilTest {
    private static File testFile;
    private static final String charsetName = "UTF-8";
    private static final String baseUri = "https://www.example.com";

    @BeforeAll
    public static void setup() {
        testFile = new File("path/to/test/file");
    }

    @Test
    public void testLoadFromFile() {
        try {
            Document doc = DataUtil.load(testFile, charsetName, baseUri);
            assertNotNull(doc);
            // Additional assertions for the loaded document
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testLoadFromInputStream() {
        try {
            InputStream in = new ByteArrayInputStream("test data".getBytes(StandardCharsets.UTF_8));
            Document doc = DataUtil.load(in, charsetName, baseUri);
            assertNotNull(doc);
            // Additional assertions for the loaded document
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testLoadFromInputStreamWithParser() {
        try {
            InputStream in = new ByteArrayInputStream("test data".getBytes(StandardCharsets.UTF_8));
            Document doc = DataUtil.load(in, charsetName, baseUri, Parser.htmlParser());
            assertNotNull(doc);
            // Additional assertions for the loaded document
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testCrossStreams() {
        try {
            InputStream in = new ByteArrayInputStream("test data".getBytes(StandardCharsets.UTF_8));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DataUtil.crossStreams(in, out);
            // Additional assertions for the output stream
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testParseByteDataWithCharset() {
        try {
            String rawData = "<html><body><h1>Test</h1></body></html>";
            ByteBuffer byteData = ByteBuffer.wrap(rawData.getBytes(StandardCharsets.UTF_8));
            Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
            assertNotNull(doc);
            // Additional assertions for the parsed document
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testParseByteDataWithoutCharset() {
        try {
            String rawData = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
            ByteBuffer byteData = ByteBuffer.wrap(rawData.getBytes(StandardCharsets.UTF_8));
            Document doc = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());
            assertNotNull(doc);
            // Additional assertions for the parsed document
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testReadToByteBufferWithMaxSize() {
        try {
            byte[] testData = "test data".getBytes(StandardCharsets.UTF_8);
            InputStream in = new ByteArrayInputStream(testData);
            int maxSize = testData.length;
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in, maxSize);
            assertEquals(maxSize, byteBuffer.capacity());
            // Additional assertions for the byte buffer
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            byte[] testData = "test data".getBytes(StandardCharsets.UTF_8);
            InputStream in = new ByteArrayInputStream(testData);
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in);
            assertEquals(testData.length, byteBuffer.capacity());
            // Additional assertions for the byte buffer
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testReadFileToByteBuffer() {
        try {
            ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);
            assertNotNull(byteBuffer);
            // Additional assertions for the byte buffer
        } catch (IOException e) {
            fail("IOException should not be thrown");
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
        assertEquals(charsetName, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=InvalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithMissingCharset() {
        String contentType = "text/html";
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
