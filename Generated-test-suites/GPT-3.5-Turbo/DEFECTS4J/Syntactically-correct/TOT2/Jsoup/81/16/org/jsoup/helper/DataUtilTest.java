package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    private String testFilePath;

    @Before
    public void setUp() {
        testFilePath = "testfile.html";
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = new File(testFilePath);
        Document document = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(document);
        // Add more assertions
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream(testFilePath);
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(document);
        // Add more assertions
    }

    @Test
    public void testLoadWithParser() throws IOException {
        InputStream inputStream = new FileInputStream(testFilePath);
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(document);
        // Add more assertions
    }

    // Add more test cases for other methods in DataUtil class

}