package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Locale;

import static org.junit.Assert.*;

public class DataUtilTest {
    private static final String TEST_FILE = "test.html";
    private static final String CHARSET = "UTF-8";
    private static final String BASE_URI = "https://www.example.com/";

    @Test
    public void testLoad_File_CharsetName_BaseUri() throws IOException {
        File file = new File(TEST_FILE);
        Document document = DataUtil.load(file, CHARSET, BASE_URI);
        assertNotNull(document);
        // Add more assertions if needed
    }

    @Test
    public void testLoad_InputStream_CharsetName_BaseUri() throws IOException {
        byte[] data = "Test data".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Document document = DataUtil.load(inputStream, CHARSET, BASE_URI);
        assertNotNull(document);
        // Add more assertions if needed
    }

    @Test
    public void testLoad_InputStream_CharsetName_BaseUri_Parser() throws IOException {
        byte[] data = "Test data".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Document document = DataUtil.load(inputStream, CHARSET, BASE_URI, Parser.htmlParser());
        assertNotNull(document);
        // Add more assertions if needed
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("Test data".getBytes());
        String charsetName = CHARSET;
        String baseUri = BASE_URI;
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);
        assertNotNull(document);
        // Add more assertions if needed
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        byte[] data = "Test data".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteBuffer);
        // Add more assertions if needed
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(CHARSET, charset);
        // Add more assertions if needed
    }
}