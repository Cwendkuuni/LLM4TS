
package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws Exception {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document document = DataUtil.load(file, charsetName, baseUri);
        
        assertNotNull(document);
    }
    
    @Test
    public void testLoadInputStream() throws Exception {
        InputStream inputStream = new FileInputStream(new File("test.html"));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        
        assertNotNull(document);
    }
    
    @Test
    public void testLoadInputStreamWithParser() throws Exception {
        InputStream inputStream = new FileInputStream(new File("test.html"));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
        
        assertNotNull(document);
    }
    
    @Test
    public void testParseByteData() throws Exception {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document document = DataUtil.parseByteData(null, charsetName, baseUri, Parser.htmlParser());
        
        assertNull(document);
    }
    
    @Test
    public void testGetCharsetFromContentType() throws Exception {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertEquals("UTF-8", charset);
    }
}
