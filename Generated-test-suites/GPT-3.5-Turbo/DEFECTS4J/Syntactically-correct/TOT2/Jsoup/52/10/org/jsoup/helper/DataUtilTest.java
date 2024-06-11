package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "");
        assertNotNull(doc);
        // Add more assertions here as needed
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream in = new ByteArrayInputStream("<html><body><h1>Hello, World!</h1></body></html>".getBytes());
        Document doc = DataUtil.load(in, "UTF-8", "");
        assertNotNull(doc);
        // Add more assertions here as needed
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream in = new ByteArrayInputStream("<html><body><h1>Hello, World!</h1></body></html>".getBytes());
        Document doc = DataUtil.load(in, "UTF-8", "", Parser.xmlParser());
        assertNotNull(doc);
        // Add more assertions here as needed
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new ByteArrayInputStream("Test data".getBytes());
        ByteBuffer byteBuffer = ByteBuffer.allocate(100);
        DataUtil.crossStreams(in, new ByteArrayOutputStream());
        // Add more assertions here as needed
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("<html><body><h1>Hello, World!</h1></body></html>".getBytes());
        Document doc = DataUtil.parseByteData(byteBuffer, "UTF-8", "", Parser.xmlParser());
        assertNotNull(doc);
        // Add more assertions here as needed
    }

    // Add more test cases as needed

}