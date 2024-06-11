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
import java.nio.charset.Charset;

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
        File file = new File("testFile.html");
        Document doc = DataUtil.load(file, null, null);
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}.
     * @throws IOException
     */
    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        Document doc = DataUtil.load(inputStream, null, null);
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)}.
     * @throws IOException
     */
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        Document doc = DataUtil.load(inputStream, null, null, Parser.xmlParser());
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)}.
     */
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        String docData = "<html><head></head><body></body></html>";
        byteData.put(docData.getBytes());
        byteData.flip();

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);

        String unexpectedCharset = "GBK";
        byteData.position(0);
        byteData.put(("Content-Type: text/html; charset=" + unexpectedCharset).getBytes());
        byteData.put(docData.getBytes());
        byteData.flip();

        try {
            Document doc2 = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }

        charsetName = unexpectedCharset;
        try {
            Document doc2 = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    /**
     * Test method for {@link DataUtil#readToByteBuffer(InputStream, int)}.
     * @throws IOException
     */
    @Test
    public void testReadToByteBuffer() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(htmlContent.length(), byteData.limit());
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)}.
     */
    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String charset1 = DataUtil.getCharsetFromContentType(contentType1);
        assertEquals("UTF-8", charset1);

        String contentType2 = "text/html";
        String charset2 = DataUtil.getCharsetFromContentType(contentType2);
        assertNull(charset2);

        String contentType3 = "text/html; charset=GBK";
        String charset3 = DataUtil.getCharsetFromContentType(contentType3);
        assertEquals("GBK", charset3);
    }
}