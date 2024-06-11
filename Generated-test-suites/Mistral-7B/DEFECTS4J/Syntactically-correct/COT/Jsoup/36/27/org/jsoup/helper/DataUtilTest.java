package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

/**
 * Test class for DataUtil class.
 */
public class DataUtilTest {

    /**
     * Test method for {@link DataUtil#load(File, String, String)}.
     * @throws IOException
     */
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // Replace with the actual path to test file
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#load(File, String, String)}.
     * @throws IOException
     */
    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("test.html"); // Replace with the actual path to test file
        String charsetName = "invalidcharset";
        String baseUri = "http://example.com";
        assertThrows(IOException.class, () -> DataUtil.load(file, charsetName, baseUri));
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}.
     * @throws IOException
     */
    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test <html>\n".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)}.
     * @throws IOException
     */
    @Test
    public void testLoadInputStreamWithCustomParser() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test <html>\n".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)}.
     */
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("Test <html>\n".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)}.
     */
    @Test
    public void testParseByteDataWithInvalidCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("Test <html>\n".getBytes());
        String charsetName = "invalidcharset";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        assertThrows(IllegalArgumentException.class, () -> DataUtil.parseByteData(byteData, charsetName, baseUri, parser));
    }

    /**
     * Test method for {@link DataUtil#readToByteBuffer(InputStream, int)}.
     * @throws IOException
     */
    @Test
    public void testReadToByteBufferInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test <html>\n".getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 0);
        assertNotNull(byteData);
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)}.
     */
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals(charset, "UTF-8");
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)}.
     */
    @Test
    public void testGetCharsetFromContentTypeInvalid() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}