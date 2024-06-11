
package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;

import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() {
        try {
            File file = new File("path/to/file");
            String charset = "UTF-8";
            String baseUri = "http://example.com";
            Document doc = DataUtil.load(file, charset, baseUri);

            assertNotNull(doc);
            // Add assertions to check the validity of the loaded document
            
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testLoadFromInputStream() {
        try {
            InputStream inputStream = new FileInputStream("path/to/file");
            String charset = "UTF-8";
            String baseUri = "http://example.com";
            Document doc = DataUtil.load(inputStream, charset, baseUri);

            assertNotNull(doc);
            // Add assertions to check the validity of the loaded document
            
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testLoadFromInputStreamWithParser() {
        try {
            InputStream inputStream = new FileInputStream("path/to/file");
            String charset = "UTF-8";
            String baseUri = "http://example.com";
            Parser parser = Parser.xmlParser();
            Document doc = DataUtil.load(inputStream, charset, baseUri, parser);

            assertNotNull(doc);
            // Add assertions to check the validity of the loaded document
            
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testParseByteData() {
        try {
            ByteBuffer byteData = ByteBuffer.wrap("Test data".getBytes());
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            Parser parser = Parser.htmlParser();
            Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

            assertNotNull(doc);
            // Add assertions to check the validity of the parsed document
            
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream inputStream = new FileInputStream("path/to/file");
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

            assertNotNull(byteData);
            // Add assertions to check the validity of the byte data
            
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String contentType2 = "text/html";
        String charset1 = DataUtil.getCharsetFromContentType(contentType1);
        String charset2 = DataUtil.getCharsetFromContentType(contentType2);

        assertEquals("UTF-8", charset1);
        assertNull(charset2);
    }
}
