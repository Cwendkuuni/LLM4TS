package org.jsoup.helper;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(doc);
        // Add more assertions here if needed
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(doc);
        // Add more assertions here if needed
    }

    @Test
    public void testLoadCustomParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Parser customParser = Parser.xmlParser();
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com", customParser);
        assertNotNull(doc);
        // Add more assertions here if needed
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new ByteArrayInputStream("test data".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out);
        assertEquals("test data", new String(out.toByteArray()));
    }

    // Add more test cases for other methods as needed

}