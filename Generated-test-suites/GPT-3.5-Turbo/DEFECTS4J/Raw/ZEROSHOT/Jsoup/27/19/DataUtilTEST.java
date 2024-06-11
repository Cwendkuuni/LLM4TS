package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Locale;

public class DataUtilTest {

    private static final String CHARSET_NAME = "UTF-8";
    private static final String BASE_URI = "http://example.com";
    private static final Parser PARSER = Parser.htmlParser();
    
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, CHARSET_NAME, BASE_URI);
        assertNotNull(doc);
    }
    
    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document doc = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI);
        assertNotNull(doc);
        inputStream.close();
    }
    
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document doc = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI, PARSER);
        assertNotNull(doc);
        inputStream.close();
    }
    
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(8192);
        String charsetName = CHARSET_NAME;
        String baseUri = BASE_URI;
        Parser parser = PARSER;
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNull(doc);
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteData);
        inputStream.close();
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(CHARSET_NAME, charset);
    }
    
}