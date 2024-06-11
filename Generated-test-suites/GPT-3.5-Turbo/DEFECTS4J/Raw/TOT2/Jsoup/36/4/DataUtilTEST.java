package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        File file = new File("test.html");
        try {
            Document doc = DataUtil.load(file, "UTF-8", "https://www.example.com");
            // Add assertions here
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadInputStream() {
        InputStream inputStream = new FileInputStream("test.html");
        try {
            Document doc = DataUtil.load(inputStream, "UTF-8", "https://www.example.com");
            // Add assertions here
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadInputStreamWithCharset() {
        InputStream inputStream = new FileInputStream("test.html");
        try {
            Document doc = DataUtil.load(inputStream, "UTF-8", "https://www.example.com", Parser.htmlParser());
            // Add assertions here
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Additional test cases for other methods can be added here

}