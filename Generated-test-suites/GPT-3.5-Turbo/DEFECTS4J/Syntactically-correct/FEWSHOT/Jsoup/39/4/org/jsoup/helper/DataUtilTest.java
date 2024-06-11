package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoad_File_CharsetName_BaseUri() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);

        assertNotNull(document);
        // Add assertions for document content
    }

    @Test
    public void testLoad_InputStream_CharsetName_BaseUri() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        assertNotNull(document);
        // Add assertions for document content

        inputStream.close();
    }

    @Test
    public void testLoad_InputStream_CharsetName_BaseUri_Parser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        assertNotNull(document);
        // Add assertions for document content

        inputStream.close();
    }

    @Test
    public void testParseByteData_DefaultCharset() {
        ByteBuffer byteData = ByteBuffer.allocate(0);
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
        // Add assertions for document content
    }

    @Test
    public void testParseByteData_SpecifiedCharset() {
        ByteBuffer byteData = ByteBuffer.allocate(0);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
        // Add assertions for document content
    }

    @Test
    public void testReadToByteBuffer_MaxSize() throws IOException {
        InputStream inStream = new ByteArrayInputStream("test".getBytes());
        int maxSize = 1024;
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream, maxSize);

        // Add assertions for byteBuffer content

        inStream.close();
    }

    @Test
    public void testReadToByteBuffer_NoMaxSize() throws IOException {
        InputStream inStream = new ByteArrayInputStream("test".getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream);

        // Add assertions for byteBuffer content

        inStream.close();
    }

    @Test
    public void testGetCharsetFromContentType_SupportedCharset() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_UnsupportedCharset() {
        String contentType = "text/html; charset=invalidcharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(null, charset);
    }

    @Test
    public void testGetCharsetFromContentType_NoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(null, charset);
    }
}