package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        try {
            Document doc = DataUtil.load(new File("test.html"), "UTF-8", "http://example.com");
            assertNotNull(doc);
            // Add more assertions as needed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            InputStream input = new FileInputStream("test.html");
            Document doc = DataUtil.load(input, "UTF-8", "http://example.com");
            assertNotNull(doc);
            // Add more assertions as needed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCrossStreams() {
        try {
            InputStream input = new FileInputStream("input.txt");
            OutputStream output = new FileOutputStream("output.txt");
            DataUtil.crossStreams(input, output);
            // Add assertions to check if the streams were crossed properly
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream input = new FileInputStream("test.html");
            ByteBuffer buffer = DataUtil.readToByteBuffer(input, 1024);
            assertNotNull(buffer);
            // Add more assertions as needed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Add more test cases for other methods in DataUtil

}