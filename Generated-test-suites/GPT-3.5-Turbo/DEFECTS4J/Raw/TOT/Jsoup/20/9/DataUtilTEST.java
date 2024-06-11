package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile_typicalCase() {
        try {
            File file = new File("test.html");
            String charsetName = "UTF-8";
            String baseUri = "/";
            Document document = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(document);
            // Additional assertions for verifying the correctness of the loaded document
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }

    @Test
    public void testLoadFile_fileNotFound() {
        try {
            File file = new File("nonexistent.html");
            String charsetName = "UTF-8";
            String baseUri = "/";
            Document document = DataUtil.load(file, charsetName, baseUri);
            fail("Expected IOException not thrown");
        } catch (IOException e) {
            // Expected IOException, test passes
        }
    }

    @Test
    public void testLoadFile_nullFileName() {
        try {
            String charsetName = "UTF-8";
            String baseUri = "/";
            Document document = DataUtil.load(null, charsetName, baseUri);
            fail("Expected NullPointerException not thrown");
        } catch (NullPointerException e) {
            // Expected NullPointerException, test passes
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }

    @Test
    public void testLoadFile_nullCharsetName() {
        try {
            File file = new File("test.html");
            String baseUri = "/";
            Document document = DataUtil.load(file, null, baseUri);
            assertNotNull(document);
            // Additional assertions for verifying the correctness of the loaded document
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }

    @Test
    public void testLoadFile_nullBaseUri() {
        try {
            File file = new File("test.html");
            String charsetName = "UTF-8";
            Document document = DataUtil.load(file, charsetName, null);
            assertNotNull(document);
            // Additional assertions for verifying the correctness of the loaded document
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }

    @Test
    public void testLoadInputStream_typicalCase() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("test.html");
            String charsetName = "UTF-8";
            String baseUri = "/";
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            assertNotNull(document);
            // Additional assertions for verifying the correctness of the loaded document
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }

    @Test
    public void testLoadInputStream_nullInputStream() {
        try {
            String charsetName = "UTF-8";
            String baseUri = "/";
            Document document = DataUtil.load(null, charsetName, baseUri);
            fail("Expected NullPointerException not thrown");
        } catch (NullPointerException e) {
            // Expected NullPointerException, test passes
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }

    // Additional test cases for testing load(InputStream, String, String)

    @Test
    public void testLoadInputStream_nullCharsetName() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("test.html");
            String baseUri = "/";
            Document document = DataUtil.load(inputStream, null, baseUri);
            assertNotNull(document);
            // Additional assertions for verifying the correctness of the loaded document
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }

    @Test
    public void testLoadInputStream_nullBaseUri() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("test.html");
            String charsetName = "UTF-8";
            Document document = DataUtil.load(inputStream, charsetName, null);
            assertNotNull(document);
            // Additional assertions for verifying the correctness of the loaded document
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }

    // Additional test cases for testing load(InputStream, String, String, Parser)

    @Test
    public void testLoadInputStreamWithParser_typicalCase() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("test.html");
            String charsetName = "UTF-8";
            String baseUri = "/";
            Parser parser = Parser.htmlParser();
            Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
            assertNotNull(document);
            // Additional assertions for verifying the correctness of the loaded document
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }

    @Test
    public void testLoadInputStreamWithParser_nullInputStream() {
        try {
            String charsetName = "UTF-8";
            String baseUri = "/";
            Parser parser = Parser.htmlParser();
            Document document = DataUtil.load(null, charsetName, baseUri, parser);
            fail("Expected NullPointerException not thrown");
        } catch (NullPointerException e) {
            // Expected NullPointerException, test passes
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }

    // Additional test cases for testing parseByteData(ByteBuffer, String, String, Parser)

    @Test
    public void testParseByteData_typicalCase() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><body>Hello World!</body></html>".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "/";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        // Additional assertions for verifying the correctness of the parsed document
    }

    @Test
    public void testParseByteData_nullByteData() {
        String charsetName = "UTF-8";
        String baseUri = "/";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(null, charsetName, baseUri, parser);
        assertNull(document);
    }

    // Additional test cases for testing readToByteBuffer(InputStream)

    @Test
    public void testReadToByteBuffer_typicalCase() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("test.html");
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
            assertNotNull(byteData);
            // Additional assertions for verifying the correctness of the read byte data
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }

    @Test
    public void testReadToByteBuffer_nullInputStream() {
        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(null);
            assertNull(byteData);
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }

    // Additional test cases for testing getCharsetFromContentType(String)

    @Test
    public void testGetCharsetFromContentType_typicalCase() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_missingCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType_nullContentType() {
        String contentType = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

}