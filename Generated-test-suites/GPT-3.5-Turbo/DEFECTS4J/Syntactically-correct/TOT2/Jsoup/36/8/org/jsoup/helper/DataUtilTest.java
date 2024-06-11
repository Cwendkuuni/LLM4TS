package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {
    
    @Test
    public void testLoadFile() {
        File file = new File("testFile.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        
        try {
            Document doc = DataUtil.load(file, charset, baseUri);
            assertNotNull(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLoadInputStream() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testFile.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        
        try {
            Document doc = DataUtil.load(inputStream, charset, baseUri);
            assertNotNull(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLoadInputStreamWithParser() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testFile.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        
        try {
            Document doc = DataUtil.load(inputStream, charset, baseUri, Parser.xmlParser());
            assertNotNull(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}