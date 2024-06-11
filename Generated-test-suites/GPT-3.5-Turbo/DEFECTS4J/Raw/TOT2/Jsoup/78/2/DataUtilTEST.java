package org.jsoup.helper;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DataUtilTest {

    private File testFile;
    private String charsetName;
    private String baseUri;

    @Before
    public void setUp() {
        testFile = new File("testFile.html");
        charsetName = "UTF-8";
        baseUri = "http://example.com";
    }

    @Test
    public void testLoadFile() {
        try {
            Document doc = DataUtil.load(testFile, charsetName, baseUri);
            assertNotNull(doc);
            // Add more assertions for the loaded document if needed
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            InputStream inputStream = new FileInputStream("testFile.html");
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            assertNotNull(doc);
            // Add more assertions for the loaded document if needed
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        try {
            InputStream inputStream = new FileInputStream("testFile.html");
            Parser parser = Parser.htmlParser();
            Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
            assertNotNull(doc);
            // Add more assertions for the loaded document if needed
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testCrossStreams() {
        try {
            InputStream inputStream = new FileInputStream("testFile1.txt");
            OutputStream outputStream = new ByteArrayOutputStream();
            DataUtil.crossStreams(inputStream, outputStream);
            // Add assertions for output stream content if needed
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream inputStream = new FileInputStream("testFile.html");
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
            assertNotNull(byteBuffer);
            // Add more assertions for the byte buffer if needed
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    // Add more test cases for other methods in DataUtil class as needed

}