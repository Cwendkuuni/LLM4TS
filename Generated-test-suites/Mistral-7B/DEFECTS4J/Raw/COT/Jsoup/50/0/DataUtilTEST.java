package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    /**
     * Test for method load(File, String, String).
     * @throws IOException
     */
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // assuming file exists
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
    }

    /**
     * Test for method load(InputStream, String, String).
     * @throws IOException
     */
    @Test
    public void testLoadInputStream() throws IOException {
        String content = "Test content";
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(content.getBytes());
        InputStream input = new ByteArrayInputStream(output.toByteArray());
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Document doc = DataUtil.load(input, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals(content, doc.body().text());
    }

    /**
     * Test for method load(InputStream, String, String, Parser).
     * @throws IOException
     */
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String content = "<html><body>Test content</body></html>";
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(content.getBytes());
        InputStream input = new ByteArrayInputStream(output.toByteArray());
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Parser parser = Parser.xmlParser();
        Document doc = DataUtil.load(input, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals(content, doc.html());
    }

    /**
     * Test for method crossStreams(InputStream, OutputStream).
     * @throws IOException
     */
    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        DataUtil.crossStreams(input, output);
        assertEquals("Test", output.toString());
    }

    /**
     * Test for method parseByteData(ByteBuffer, String, String, Parser).
     */
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("Test <html><body>Test</body></html>".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Parser parser = Parser.xmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals("<html><body>Test</body></html>", doc.html());
    }

    /**
     * Test for method readToByteBuffer(InputStream).
     * @throws IOException
     */
    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test".getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(input);
        assertEquals(6, byteData.limit());
        assertEquals("Test".getBytes(), byteData.array(), 0, 6);
    }

    /**
     * Test for method readToByteBuffer(InputStream, int).
     * @throws IOException
     */
    @Test
    public void testReadToByteBufferWithLimit() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test".getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(input, 3);
        assertEquals(3, byteData.limit());
        assertEquals("Test".substring(0, 3).getBytes(), byteData.array(), 0, 3);
    }

    /**
     * Test for method readFileToByteBuffer(File).
     * @throws IOException
     */
    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html"); // assuming file exists
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);
        assertEquals(file.length(), byteData.limit());
    }

    /**
     * Test for method emptyByteBuffer().
     */
    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        assertEquals(0, byteData.limit());
    }

    /**
     * Test for method getCharsetFromContentType(String).
     */
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    /**
     * Test for method mimeBoundary().
     */
    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertTrue(boundary.length() == 32);
        for (int i = 0; i < boundary.length(); i++) {
            assertTrue(java.util.Arrays.binarySearch(DataUtil.mimeBoundaryChars, boundary.charAt(i)) >= 0);
        }
    }
}