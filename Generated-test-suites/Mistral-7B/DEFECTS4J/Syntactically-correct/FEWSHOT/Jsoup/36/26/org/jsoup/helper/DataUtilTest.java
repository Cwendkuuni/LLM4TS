package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFileWithCharset() {
        String charsetName = "UTF-8";
        File inputFile = new File("path/to/input/file.html");
        String baseUri = "http://example.com";

        try {
            Document doc = DataUtil.load(inputFile, charsetName, baseUri);
            assertNotNull(doc);
            assertEquals(charsetName, doc.outputSettings().charset());
        } catch (IOException e) {
            // Test fails if an IOException is thrown
            fail("Unexpected IOException: " + e.getMessage());
        }
    }

    @Test
    public void testLoadDocumentFromFileWithDetectedCharset() {
        File inputFile = new File("path/to/input/file.html");
        String baseUri = "http://example.com";

        try {
            Document doc = DataUtil.load(inputFile, null, baseUri);
            assertEquals("UTF-8", doc.outputSettings().charset());
        } catch (IOException e) {
            // Test fails if an IOException is thrown
            fail("Unexpected IOException: " + e.getMessage());
        }
    }

    @Test
    public void testLoadDocumentFromInputStreamWithCharset() {
        String charsetName = "UTF-8";
        String inputData = "input data for testing";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(Charset.forName(charsetName)));
        String baseUri = "http://example.com";

        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            assertNotNull(doc);
            assertEquals(charsetName, doc.outputSettings().charset());
        } catch (IOException e) {
            // Test fails if an IOException is thrown
            fail("Unexpected IOException: " + e.getMessage());
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                // Ignore close exception
            }
        }
    }

    @Test
    public void testLoadDocumentFromInputStreamWithDetectedCharset() {
        String inputData = "input data for testing<meta http-equiv='Content-Type' content='text/html; charset=gb2312'>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        String baseUri = "http://example.com";

        try {
            Document doc = DataUtil.load(inputStream, null, baseUri);
            assertEquals("gb2312", doc.outputSettings().charset());
        } catch (IOException e) {
            // Test fails if an IOException is thrown
            fail("Unexpected IOException: " + e.getMessage());
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                // Ignore close exception
            }
        }
    }

    @Test
    public void testLoadDocumentFromInputStreamWithParser() {
        String charsetName = "UTF-8";
        String inputData = "input data for testing";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(Charset.forName(charsetName)));
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();

        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
            assertNotNull(doc);
            assertEquals(charsetName, doc.outputSettings().charset());
        } catch (IOException e) {
            // Test fails if an IOException is thrown
            fail("Unexpected IOException: " + e.getMessage());
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                // Ignore close exception
            }
        }
    }

    @Test
    public void testParseByteDataWithCharset() {
        String charsetName = "UTF-8";
        String inputData = "input data for testing<meta http-equiv='Content-Type' content='text/html; charset=gb2312'>";
        ByteBuffer byteData = ByteBuffer.wrap(inputData.getBytes(Charset.forName(charsetName)));
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals(charsetName, doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithDetectedCharset() {
        String inputData = "input data for testing<meta http-equiv='Content-Type' content='text/html; charset=gb2312'>";
        ByteBuffer byteData = ByteBuffer.wrap(inputData.getBytes());
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseByteData(byteData, null, baseUri, parser);
        assertEquals("gb2312", doc.outputSettings().charset());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputData = "input data for testing";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        byte[] buffer = new byte[bufferSize];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bufferSize);

        DataUtil.readToByteBuffer(inputStream, bufferSize, outputStream);

        ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
        assertEquals(inputData.length(), byteData.remaining());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        String contentType2 = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType2));

        String contentType3 = "text/html; charset=gb2312";
        charset = DataUtil.getCharsetFromContentType(contentType3);
        assertEquals("gb2312", charset);
    }
}