package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    /**
     * Test loading a file with a specified charset and base URI.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadFile() throws IOException {
        String filePath = "path/to/your/file.html"; // replace with the path to your test file
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";

        File file = new File(filePath);
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
    }

    /**
     * Test loading a file without specifying a charset.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadFileWithoutCharset() throws IOException {
        String filePath = "path/to/your/file.html"; // replace with the path to your test file
        String baseUri = "http://www.example.com";

        File file = new File(filePath);
        Document doc = DataUtil.load(file, null, baseUri);
        assertNotNull(doc);
    }

    /**
     * Test loading a file with an invalid charset.
     * @throws IOException on IO error
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        String filePath = "path/to/your/file.html"; // replace with the path to your test file
        String charsetName = "INVALID_CHARSET"; // replace with an invalid charset name
        String baseUri = "http://www.example.com";

        File file = new File(filePath);
        DataUtil.load(file, charsetName, baseUri);
    }

    /**
     * Test loading an InputStream with a specified charset and base URI.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        String html = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";

        InputStream inputStream = new ByteArrayInputStream(html.getBytes(Charset.forName(charsetName)));
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
    }

    /**
     * Test loading an InputStream without specifying a charset.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadInputStreamWithoutCharset() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(Charset.forName("UTF-8")));
        Document doc = DataUtil.load(inputStream, null, null, Parser.htmlParser());
        assertNotNull(doc);
    }

    /**
     * Test loading an InputStream with an unsupported charset.
     * @throws IOException on IO error
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLoadInputStreamWithUnsupportedCharset() throws IOException {
        String charsetName = "INVALID_CHARSET"; // replace with an invalid charset name
        String html = "<html><head><meta charset=\"" + charsetName + "\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(Charset.forName("UTF-8")));
        DataUtil.load(inputStream, charsetName, null, Parser.htmlParser());
    }

    /**
     * Test parsing a Document from an InputStream with a specified charset and base URI.
     * @throws IOException on IO error
     */
    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        String html = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";

        InputStream inputStream = new ByteArrayInputStream(html.getBytes(Charset.forName(charsetName)));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
    }

    /**
     * Test reading bytes from an InputStream into a byte buffer.
     * @throws IOException on IO error
     */
    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(Charset.forName("UTF-8")));

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        long limit = byteData.limit();
        byteData.position(0);

        byte[] buffer = new byte[limit];
        byteData.get(buffer);
        assertEquals(html, new String(buffer, 0, (int) limit, Charset.forName("UTF-8")));
    }

    /**
     * Test getting a charset from a content type header.
     */
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html; charset=gb2312";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("gb2312", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}