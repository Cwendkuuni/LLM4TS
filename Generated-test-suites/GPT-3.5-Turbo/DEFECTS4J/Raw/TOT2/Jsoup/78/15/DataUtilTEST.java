package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, "UTF-8", baseUri);
        assertNotNull(document);
        // Add more assertions as needed
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, "UTF-8", baseUri);
        assertNotNull(document);
        // Add more assertions as needed
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.xml");
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, "UTF-8", baseUri, Parser.xmlParser());
        assertNotNull(document);
        // Add more assertions as needed
    }

    // Add more test cases for other methods in DataUtil class

}