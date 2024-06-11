package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.junit.Test;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    /**
     * Test method for {@link DataUtil#load(File, String, String)}.
     * @throws IOException
     */
    @Test
    public void testLoadFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File file = new File("path/to/your/file.html");
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}.
     * @throws IOException
     */
    @Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = getClass().getResourceAsStream("/yourfile.html");
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
        inputStream.close();
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)}.
     * @throws IOException
     */
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = getClass().getResourceAsStream("/yourfile.html");
        Parser parser = Parser.xmlParser();
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
        inputStream.close();
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)}.
     * @throws IOException
     */
    @Test
    public void testParseByteData() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'H', 'e', 'l', 'l', 'o', 0});
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    /**
     * Test method for {@link DataUtil#readToByteBuffer(InputStream)}.
     * @throws IOException
     */
    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputFile = "path/to/your/input.txt";
        InputStream inputStream = new FileInputStream(inputFile);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteData);
        inputStream.close();
    }

    /**
     * Test method for {@link DataUtil#readToByteBuffer(InputStream, int)}.
     * @throws IOException
     */
    @Test
    public void testReadToByteBufferLimit() throws IOException {
        String inputFile = "path/to/your/input.txt";
        int maxSize = 10;
        InputStream inputStream = new FileInputStream(inputFile);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, maxSize);
        assertNotNull(byteData);
        inputStream.close();
    }

    /**
     * Test method for {@link DataUtil#emptyByteBuffer()}.
     */
    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertTrue(byteBuffer.capacity() == 0);
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)}.
     */
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    /**
     * Test method for {@link DataUtil#validateCharset(String)}.
     */
    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        assertEquals(charset, DataUtil.validateCharset(charset));
        charset = "invalidCharset";
        assertNull(DataUtil.validateCharset(charset));
    }

    /**
     * Test method for {@link DataUtil#mimeBoundary()}.
     */
    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertTrue(boundary.length() == 32);
    }

    /**
     * Test method for {@link DataUtil#detectCharsetFromBom(ByteBuffer, String)}.
     */
    @Test
    public void testDetectCharsetFromBom() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00});
        String charsetName = "UTF-16";
        String charsetDetected = DataUtil.detectCharsetFromBom(byteData, charsetName);
        assertEquals(charsetDetected, charsetName);
    }
}