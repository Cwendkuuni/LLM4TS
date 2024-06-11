package org.jsoup.helper;
import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(doc);
        // Add more assertions based on the expected output from loading a file
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(doc);
        // Add more assertions based on the expected output from loading an input stream
    }

    @Test
    public void testCrossStreams() throws IOException {
        // Create input stream and output stream
        // Call DataUtil.crossStreams method
        // Add assertions to verify the data is correctly written to the output stream
    }

    // Add more test cases for other methods in DataUtil class

}