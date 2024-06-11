package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Locale;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charset, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charset, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        Document document = DataUtil.load(inputStream, charset, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[0]);
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteBuffer, charset, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
}