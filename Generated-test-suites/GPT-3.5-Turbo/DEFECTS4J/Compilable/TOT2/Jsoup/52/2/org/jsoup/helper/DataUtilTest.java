package org.jsoup.helper;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
        // Add more assertions as needed
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
        // Add more assertions as needed
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.xml");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.xmlParser();
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
        // Add more assertions as needed
    }

    // Add more test methods for other public methods in DataUtil class

}