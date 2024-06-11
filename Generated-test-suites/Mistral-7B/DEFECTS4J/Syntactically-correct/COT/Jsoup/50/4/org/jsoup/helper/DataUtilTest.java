package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
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
import java.util.Random;

/**
 * Test class for DataUtil class
 */
public class DataUtilTest {

    /**
     * Test method for {@link DataUtil#load(File, String, String)}
     * @throws IOException if an exception occurs whilst testing
     */
    @Test
    public void testLoadFile() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        File file = new File("testFile.html");
        FileUtils.writeStringToFile(file, html, StandardCharsets.UTF_8);

        Document document = DataUtil.load(file, "UTF-8", null);

        assertEquals("Test", document.body().text());
        FileUtils.deleteQuietly(file);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}
     * @throws IOException if an exception occurs whilst testing
     */
    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, "UTF-8", null);

        assertEquals("Test", document.body().text());
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)}
     * @throws IOException if an exception occurs whilst testing
     */
    @Test
    public void testLoadInputStreamParser() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, "UTF-8", null, Parser.htmlParser());

        assertEquals("Test", document.body().text());
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)}
     * @throws IOException if an exception occurs whilst testing
     */
    @Test
    public void testParseByteData() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", null, Parser.htmlParser());

        assertEquals("Test", document.body().text());
    }

    /**
     * Test method for {@link DataUtil#readToByteBuffer(InputStream)}
     * @throws IOException if an exception occurs whilst testing
     */
    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        byte[] expected = html.getBytes(StandardCharsets.UTF_8);
        byte[] actual = new byte[byteBuffer.limit()];
        byteBuffer.get(actual);
        assertArrayEquals(expected, actual);
    }

    /**
     * Test method for {@link DataUtil#readToByteBuffer(InputStream, int)}
     * @throws IOException if an exception occurs whilst testing
     */
    @Test
    public void testReadToByteBufferMaxSize() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 10);

        byte[] expected = html.substring(0, 10).getBytes(StandardCharsets.UTF_8);
        byte[] actual = new byte[byteBuffer.limit()];
        byteBuffer.get(actual);
        assertArrayEquals(expected, actual);
    }

    /**
     * Test method for {@link DataUtil#readFileToByteBuffer(File)}
     * @throws IOException if an exception occurs whilst testing
     */
    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        File file = new File("testFile.html");
        FileUtils.writeStringToFile(file, html, StandardCharsets.UTF_8);

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);

        byte[] expected = html.getBytes(StandardCharsets.UTF_8);
        byte[] actual = new byte[byteBuffer.limit()];
        byteBuffer.get(actual);
        assertArrayEquals(expected, actual);

        FileUtils.deleteQuietly(file);
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)}
     * @throws IOException if an exception occurs whilst testing
     */
    @Test
    public void testGetCharsetFromContentType() throws IOException {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", charset);
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)} with invalid content type
     * @throws IOException if an exception occurs whilst testing
     */
    @Test (expected = AssertionError.class)
    public void testGetCharsetFromContentTypeInvalidContentType() throws IOException {
        String contentType = "text/html; charset=invalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
    }

    /**
     * Test method for {@link DataUtil#mimeBoundary()}
     */
    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        int length = boundary.length();
        for (int i = 0; i < length; i++) {
            assertTrue(DataUtil.mimeBoundaryChars[new Random().nextInt(DataUtil.mimeBoundaryChars.length)] == boundary.charAt(i));
        }
    }
}