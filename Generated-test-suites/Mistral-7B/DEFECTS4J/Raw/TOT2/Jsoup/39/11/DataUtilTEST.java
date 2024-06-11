package org.example;

import org.junit.Test;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;

public class DataUtilTest {

    // Test cases for each method go here.

    @Test
    public void testLoadFile() throws IOException {
        File inputFile = new File("test.html");
        Document doc = DataUtil.load(inputFile, "UTF-8", null);
        // Perform assertions on the Document object.
    }

    @Test
    public void testLoadInputStream() throws IOException {
        File inputFile = new File("test.html");
        FileInputStream fileInputStream = new FileInputStream(inputFile);
        Document doc = DataUtil.load(fileInputStream, "UTF-8", null);
        // Perform assertions on the Document object.
        fileInputStream.close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        File inputFile = new File("test.html");
        FileInputStream fileInputStream = new FileInputStream(inputFile);
        Document doc = DataUtil.load(fileInputStream, "UTF-8", null, Parser.xmlParser());
        // Perform assertions on the Document object.
        fileInputStream.close();
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        // Perform assertions on the Document object.
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        File inputFile = new File("test.html");
        FileInputStream fileInputStream = new FileInputStream(inputFile);
        ByteBuffer byteData = DataUtil.readToByteBuffer(fileInputStream);
        // Perform assertions on the ByteBuffer object.
        fileInputStream.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=ISO-8859-1";
        String expectedCharset = "ISO-8859-1";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentType_NoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testLoadEmptyFile() throws IOException {
        File inputFile = new File("empty.html");
        Document doc = DataUtil.load(inputFile, "UTF-8", null);
        // Perform assertions on the Document object.
        // The document should be empty.
    }

    @Test
    public void testLoadInvalidCharset() throws IOException {
        File inputFile = new File("test.html");
        Document doc = null;
        try {
            doc = DataUtil.load(inputFile, "invalidCharset", null);
        } catch (IOException e) {
            // Expected IOException
        }
        assertNull(doc);
    }

    @Test
    public void testLoadNonExistentFile() {
        File inputFile = new File("nonExistent.html");
        try {
            DataUtil.load(inputFile, "UTF-8", null);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected IOException
        }
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        File inputFile = new File("test.html");
        FileInputStream fileInputStream = new FileInputStream(inputFile);
        try {
            DataUtil.load(inputFile, null, null);
        } finally {
            fileInputStream.close();
        }
    }

    @Test(expected = IllegalCharsetNameException.class)
    public void testIllegalCharsetNameException() throws IOException, IllegalCharsetNameException {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
        String charsetName = "invalidCharset";
        String baseUri = "http://example.com";
        DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
    }
}