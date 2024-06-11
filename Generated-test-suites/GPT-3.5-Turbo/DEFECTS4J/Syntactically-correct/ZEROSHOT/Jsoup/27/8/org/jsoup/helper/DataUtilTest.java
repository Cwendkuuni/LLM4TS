package org.jsoup.helper;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Locale;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = DataUtil.load(file, charset, baseUri);
        
        assertNotNull(doc);
        assertEquals("UTF-8", doc.charset().displayName());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = DataUtil.load(in, charset, baseUri);
        
        assertNotNull(doc);
        assertEquals("UTF-8", doc.charset().displayName());
        
        in.close();
    }

    @Test
    public void testLoadInputStreamAndParser() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.load(in, charset, baseUri, parser);
        
        assertNotNull(doc);
        assertEquals("UTF-8", doc.charset().displayName());
        
        in.close();
    }
    
    @Test
    public void testParseByteData() {
        //TODO: Implement test case
    }
    
    @Test
    public void testReadToByteBuffer() {
        //TODO: Implement test case
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        //TODO: Implement test case
    }
}