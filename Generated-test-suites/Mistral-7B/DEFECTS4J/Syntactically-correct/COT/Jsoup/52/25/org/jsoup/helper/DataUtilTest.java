package org.jsoup.helper;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html"); // replace with actual test file
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamUnsupportedCharset() throws IOException {
        String unsupportedCharset = "X-1234";
        InputStream in = new ByteArrayInputStream("test".getBytes(Charset.forName(unsupportedCharset)));
        String baseUri = "http://example.com";
        try {
            Document doc = DataUtil.load(in, unsupportedCharset, baseUri);
            assertNull(doc);
        } catch (IllegalArgumentException e) {
            assertEquals("Unsupported charset: " + unsupportedCharset, e.getMessage());
        }
    }

    @Test
    public void testLoadInputStreamSupportedCharset() throws IOException {
        String supportedCharset = "UTF-8";
        InputStream in = new ByteArrayInputStream("test".getBytes(Charset.forName(supportedCharset)));
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(in, supportedCharset, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithCustomParser() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        InputStream in = new ByteArrayInputStream("test".getBytes(Charset.forName(charsetName)));
        Document doc = DataUtil.load(in, charsetName, baseUri, parser);
        assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        DataUtil.crossStreams(input, output);
        byte[] expectedOutput = "test".getBytes();
        byte[] outputBytes = output.toByteArray();
        assertArrayEquals(expectedOutput, outputBytes);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        String charsetName = "X-1234";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        try {
            Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
            assertNull(doc);
        } catch (IllegalArgumentException e) {
            assertEquals("Unsupported charset: " + charsetName, e.getMessage());
        }
    }

    @Test
    public void testParseByteDataWithValidCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(input);
        byte[] expectedByteArray = "test".getBytes();
        byte[] byteArray = new byte[byteBuffer.limit()];
        byteBuffer.get(byteArray);
        assertArrayEquals(expectedByteArray, byteArray);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(input, 5);
        assertEquals(5, byteBuffer.limit());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html"); // replace with actual test file
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        byte[] expectedByteArray = new byte[0]; // replace with expected byte array for the test file
        byte[] byteArray = new byte[byteBuffer.limit()];
        byteBuffer.get(byteArray);
        assertArrayEquals(expectedByteArray, byteArray);
    }
}