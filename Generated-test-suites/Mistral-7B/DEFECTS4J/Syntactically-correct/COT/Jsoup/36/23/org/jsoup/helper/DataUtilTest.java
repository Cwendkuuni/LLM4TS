package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.HelperException;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException, HelperException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File file = new File("test.html");
        Document expectedDocument = Parser.htmlParser().parse(new FileReader("test.html")); // assuming a valid test.html file

        Document actualDocument = DataUtil.load(file, charsetName, baseUri);
        assertEquals("Document content is not as expected", expectedDocument.toString(), actualDocument.toString());

        // Test edge case: Empty file
        file = new File("empty.html");
        actualDocument = DataUtil.load(file, charsetName, baseUri);
        assertTrue("Expected empty document", actualDocument.body().text().isEmpty());

        // Test exception handling: Null file
        file = null;
        try {
            actualDocument = DataUtil.load(file, charsetName, baseUri);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInputStream() throws IOException, HelperException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = new ByteArrayInputStream("valid input".getBytes()); // valid input for testing

        Document expectedDocument = Parser.htmlParser().parse(new ByteArrayInputStream("valid input".getBytes()));

        Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri);
        assertEquals("Document content is not as expected", expectedDocument.toString(), actualDocument.toString());

        // Test edge case: Empty input stream
        inputStream = new ByteArrayInputStream(new byte[0]);
        actualDocument = DataUtil.load(inputStream, charsetName, baseUri);
        assertTrue("Expected empty document", actualDocument.body().text().isEmpty());

        // Test exception handling: Null input stream
        inputStream = null;
        try {
            actualDocument = DataUtil.load(inputStream, charsetName, baseUri);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException, HelperException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = new ByteArrayInputStream("valid input".getBytes()); // valid input for testing
        Parser parser = Parser.xmlParser();

        Document expectedDocument = parser.parseInput(new ByteArrayInputStream("valid input".getBytes()), baseUri);

        Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertEquals("Document content is not as expected", expectedDocument.toString(), actualDocument.toString());
    }

    @Test
    public void testParseByteData() throws IOException, HelperException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        ByteBuffer byteData = ByteBuffer.wrap("valid input".getBytes());

        Document expectedDocument = Parser.htmlParser().parse(new ByteArrayInputStream("valid input".getBytes()));

        Document actualDocument = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertEquals("Document content is not as expected", expectedDocument.toString(), actualDocument.toString());

        // Test edge case: Empty byte buffer
        byteData = ByteBuffer.allocate(0);
        actualDocument = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertTrue("Expected empty document", actualDocument.body().text().isEmpty());

        // Test exception handling: Null byte buffer
        byteData = null;
        try {
            actualDocument = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String actualCharset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("Charset is not as expected", expectedCharset, actualCharset);

        // Test edge case: null content type
        contentType = null;
        assertNull("Expected null", DataUtil.getCharsetFromContentType(contentType));
    }

    @Test(expected = HelperException.class)
    public void testReadToByteBufferWithException() throws IOException {
        InputStream inStream = new ByteArrayInputStream("input".getBytes());
        try {
            DataUtil.readToByteBuffer(null, 0);
            fail("Expected HelperException");
        } catch (HelperException e) {
            // Expected
        }
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inStream = new ByteArrayInputStream("input".getBytes());
        ByteBuffer expectedByteData = ByteBuffer.wrap("input".getBytes());
        ByteBuffer actualByteData = DataUtil.readToByteBuffer(inStream);

        assertEquals("Byte buffer sizes don't match", expectedByteData.limit(), actualByteData.limit());
        byte[] expectedBytes = expectedByteData.array();
        byte[] actualBytes = actualByteData.array();

        // Test edge case: Empty input stream
        inStream = new ByteArrayInputStream(new byte[0]);
        actualByteData = DataUtil.readToByteBuffer(inStream);

        assertTrue("Expected empty byte buffer", actualByteData.array().length == 0);

        // Test exception handling: Null input stream
        inStream = null;
        try {
            actualByteData = DataUtil.readToByteBuffer(inStream);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

}