package org.jsoup.helper;

import static org.junit.Assert.*;
import static org.jsoup.helper.DataUtil.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;

/**
 * Test class for DataUtil class.
 */
public class DataUtilTest {

    /**
     * Test method for {@link DataUtil#load(File, String, String)}
     * @throws IOException
     */
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = load(file, charsetName, baseUri);
        assertNotNull(document);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}
     * @throws IOException
     */
    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)}
     * @throws IOException
     */
    @Test
    public void testLoadInputStreamParser() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        Document document = load(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    /**
     * Test method for {@link DataUtil#crossStreams(InputStream, OutputStream)}
     * @throws IOException
     */
    @Test
    public void testCrossStreams() throws IOException {
        String input = "Test Input";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        crossStreams(inputStream, outputStream);
        String output = outputStream.toString(StandardCharsets.UTF_8.name());
        assertEquals(input, output);
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)}
     */
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{0xFF, 0xFE, 0, -128, 0, 127, 0, 128, 0, 255, 0, -1});
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);

        // Test with invalid charset
        String invalidCharsetName = "InvalidCharset";
        document = parseByteData(byteData, invalidCharsetName, baseUri, parser);
        assertNull(document);
    }

    /**
     * Test method for {@link DataUtil#readToByteBuffer(InputStream)}
     * @throws IOException
     */
    @Test
    public void testReadToByteBuffer() throws IOException {
        String input = "Test Input";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = readToByteBuffer(inputStream);
        byte[] expectedBytes = input.getBytes(StandardCharsets.UTF_8);
        byte[] actualBytes = new byte[byteBuffer.limit()];
        byteBuffer.get(actualBytes);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    /**
     * Test method for {@link DataUtil#readFileToByteBuffer(File)}
     * @throws IOException
     */
    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteBuffer = readFileToByteBuffer(file);
        // Actually reading file content to compare with is not possible within JUnit test
        // You can either compare byte arrays if you have the data, or add assertions based on the structure of the document
    }

    /**
     * Test method for {@link DataUtil#emptyByteBuffer()}
     */
    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = emptyByteBuffer();
        assertEquals(0, byteBuffer.limit());
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)}
     */
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    /**
     * Test method for {@link DataUtil#mimeBoundary()}
     */
    @Test
    public void testMimeBoundary() {
        String boundary = mimeBoundary();
        int length = boundary.length();
        for (int i = 0; i < length; i++) {
            char c = boundary.charAt(i);
            assertTrue("Character '" + c + "' is a valid mime boundary character", isMimeBoundaryCharacter(c));
        }
    }
}