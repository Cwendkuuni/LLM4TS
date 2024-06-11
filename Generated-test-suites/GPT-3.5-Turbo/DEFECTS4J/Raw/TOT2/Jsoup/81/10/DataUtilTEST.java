package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "https://example.com");
        assertNotNull(document);
        // Add more assertions for the loaded document if needed
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "https://example.com");
        assertNotNull(document);
        // Add more assertions for the loaded document if needed
    }

    @Test
    public void testCrossStreams() throws IOException {
        // Test the crossStreams method here
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        // Test the readToByteBuffer method here
    }

    @Test
    public void testGetCharsetFromContentType() {
        String charset = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        assertEquals("UTF-8", charset);
        // Add more assertions for different content types if needed
    }

    // Add more test cases for other methods in DataUtil class

}