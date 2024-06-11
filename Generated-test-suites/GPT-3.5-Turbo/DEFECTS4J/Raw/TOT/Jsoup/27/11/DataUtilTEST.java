package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static org.junit.Assert.*;

public class DataUtilTest {

    private static final String CHARSET_NAME = "UTF-8";
    private static final String BASE_URI = "https://www.example.com/";

    private File testFile;
    private Document expectedDocument;

    @Before
    public void setup() throws IOException {
        testFile = createTestFile();

        expectedDocument = Jsoup.parse("<html><head></head><body><div>Hello World</div></body></html>", BASE_URI);
    }

    @Test
    public void testLoadFromFile() throws IOException {
        Document document = DataUtil.load(testFile, CHARSET_NAME, BASE_URI);
        assertNotNull(document);
        assertEquals(expectedDocument.html(), document.html());
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        Document document;
        try (FileInputStream inputStream = new FileInputStream(testFile)) {
            document = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI);
        }
        assertNotNull(document);
        assertEquals(expectedDocument.html(), document.html());
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        Document document;
        try (FileInputStream inputStream = new FileInputStream(testFile)) {
            document = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI, Parser.htmlParser());
        }
        assertNotNull(document);
        assertEquals(expectedDocument.html(), document.html());
    }

    private File createTestFile() throws IOException {
        File file = File.createTempFile("test", ".html");
        FileOutputStream outputStream = new FileOutputStream(file);
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        writer.write("<html><head></head><body><div>Hello World</div></body></html>");
        writer.close();
        outputStream.close();
        return file;
    }
}