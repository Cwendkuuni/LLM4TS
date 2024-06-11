package org.jsoup.helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        // Typical use case
        // Test loading a valid file with UTF-8 charset
        String filepath = "path/to/valid/file.html";
        String charset = "UTF-8";
        String baseUri = "http://www.example.com";
        try {
            Document doc = DataUtil.load(new File(filepath), charset, baseUri);
            assertNotNull(doc);
            // Additional assertion statements here
        } catch (IOException e) {
            fail("IOException occurred");
        }

        // Edge case
        // Test loading an empty file
        String emptyFilepath = "path/to/empty/file.html";
        try {
            Document doc = DataUtil.load(new File(emptyFilepath), charset, baseUri);
            assertNotNull(doc);
            // Additional assertion statements here
        } catch (IOException e) {
            fail("IOException occurred");
        }

        // Error scenario
        // Test loading a non-existent file
        String nonExistentFilepath = "path/to/nonexistent/file.html";
        assertThrows(IOException.class, () -> {
            DataUtil.load(new File(nonExistentFilepath), charset, baseUri);
        });
    }

    @Test
    public void testLoadInputStream() {
        // Typical use case
        // Test loading a valid input stream with UTF-8 charset
        InputStream inputStream = new FileInputStream("path/to/input/stream.html");
        String charset = "UTF-8";
        String baseUri = "http://www.example.com";
        try {
            Document doc = DataUtil.load(inputStream, charset, baseUri);
            assertNotNull(doc);
            // Additional assertion statements here
        } catch (IOException e) {
            fail("IOException occurred");
        }

        // Edge case
        // Test loading an empty input stream
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        try {
            Document doc = DataUtil.load(emptyInputStream, charset, baseUri);
            assertNotNull(doc);
            // Additional assertion statements here
        } catch (IOException e) {
            fail("IOException occurred");
        }

        // Error scenario
        // Test loading a null input stream
        InputStream nullInputStream = null;
        assertThrows(IOException.class, () -> {
            DataUtil.load(nullInputStream, charset, baseUri);
        });
    }

    @Test
    public void testLoadInputStreamWithParser() {
        // Typical use case
        // Test loading a valid input stream with UTF-8 charset and custom parser
        InputStream inputStream = new FileInputStream("path/to/input/stream.html");
        String charset = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        try {
            Document doc = DataUtil.load(inputStream, charset, baseUri, parser);
            assertNotNull(doc);
            // Additional assertion statements here
        } catch (IOException e) {
            fail("IOException occurred");
        }

        // Edge case
        // Test loading an empty input stream with XML parser
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        Parser xmlParser = Parser.xmlParser();
        try {
            Document doc = DataUtil.load(emptyInputStream, charset, baseUri, xmlParser);
            assertNotNull(doc);
            // Additional assertion statements here
        } catch (IOException e) {
            fail("IOException occurred");
        }

        // Error scenario
        // Test loading a null input stream with null parser
        InputStream nullInputStream = null;
        Parser nullParser = null;
        assertThrows(IOException.class, () -> {
            DataUtil.load(nullInputStream, charset, baseUri, nullParser);
        });
    }

    @Test
    public void testParseByteData() {
        // Typical use case
        // Test parsing byte data with UTF-8 charset and base URI
        ByteBuffer byteData = ByteBuffer.wrap("test data".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
        // Additional assertion statements here

        // Edge case
        // Test parsing empty byte data with default charset and empty base URI
        ByteBuffer emptyByteData = ByteBuffer.wrap(new byte[0]);
        String defaultCharsetName = DataUtil.defaultCharset;
        String emptyBaseUri = "";
        Document emptyDoc = DataUtil.parseByteData(emptyByteData, defaultCharsetName, emptyBaseUri, parser);
        assertNotNull(emptyDoc);
        // Additional assertion statements here

        // Error scenario
        // Test parsing null byte data with null charset and null base URI
        ByteBuffer nullByteData = null;
        Parser nullParser = null;
        assertThrows(NullPointerException.class, () -> {
            DataUtil.parseByteData(nullByteData, null, null, nullParser);
        });
    }

    @Test
    public void testReadToByteBuffer() {
        // Typical use case
        // Test reading input stream to byte buffer with unlimited maxSize
        InputStream inputStream = new FileInputStream("path/to/input/stream.html");
        int maxSize = 0;
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
            assertNotNull(byteBuffer);
            // Additional assertion statements here
        } catch (IOException e) {
            fail("IOException occurred");
        }

        // Edge case
        // Test reading input stream to byte buffer with capped maxSize
        int cappedMaxSize = 1000;
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, cappedMaxSize);
            assertNotNull(byteBuffer);
            // Additional assertion statements here
        } catch (IOException e) {
            fail("IOException occurred");
        }

        // Error scenario
        // Test reading null input stream with negative maxSize
        InputStream nullInputStream = null;
        int negativeMaxSize = -1;
        assertThrows(IllegalArgumentException.class, () -> {
            DataUtil.readToByteBuffer(nullInputStream, negativeMaxSize);
        });
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Typical use case
        // Test getting charset from content type header
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
        // Additional assertion statements here

        // Edge case
        // Test getting charset from content type header with unsupported charset
        String unsupportedContentType = "text/html; charset=ISO-8859-11";
        String unsupportedCharset = DataUtil.getCharsetFromContentType(unsupportedContentType);
        assertNull(unsupportedCharset);
        // Additional assertion statements here

        // Error scenario
        // Test getting charset from null content type
        String nullContentType = null;
        String nullCharset = DataUtil.getCharsetFromContentType(nullContentType);
        assertNull(nullCharset);
        // Additional assertion statements here
    }
}