package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;

public class DataUtilTest {

    private static final String SAMPLE_HTML_FILE = "src/test/resources/sample.html";

    @Test
    public void testLoadFromFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";

        File inFile = new File(SAMPLE_HTML_FILE);
        Document doc = DataUtil.load(inFile, charsetName, baseUri);

        assertNotNull(doc);
        // Add more assertions as needed
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";

        InputStream in = new FileInputStream(SAMPLE_HTML_FILE);
        Document doc = DataUtil.load(in, charsetName, baseUri);

        assertNotNull(doc);
        // Add more assertions as needed
    }

    @Test
    public void testLoadFromInputStreamWithCustomParser() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser customParser = Parser.xmlParser();

        InputStream in = new FileInputStream(SAMPLE_HTML_FILE);
        Document doc = DataUtil.load(in, charsetName, baseUri, customParser);

        assertNotNull(doc);
        // Add more assertions as needed
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new FileInputStream(SAMPLE_HTML_FILE);
        OutputStream out = new ByteArrayOutputStream();

        DataUtil.crossStreams(in, out);

        // Check if no exceptions were thrown
    }

    // Add more test cases for other methods in DataUtil class

}