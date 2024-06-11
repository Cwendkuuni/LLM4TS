import org.jsoup.helper.DataUtil;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        // typical use case: loading a file with UTF-8 charset
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(document);

        // edge case: loading a non-existent file
        try {
            File nonExistentFile = new File("nonexistent.html");
            Document nonExistentDocument = DataUtil.load(nonExistentFile, "UTF-8", "http://example.com");
            fail("IOException should have been thrown");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("No such file or directory"));
        }

        // error scenario: loading a file with an unsupported charset
        try {
            File unsupportedCharsetFile = new File("test.html");
            Document unsupportedCharsetDocument = DataUtil.load(unsupportedCharsetFile, "invalid", "http://example.com");
            fail("UnsupportedEncodingException should have been thrown");
        } catch (UnsupportedEncodingException e) {
            assertTrue(e.getMessage().contains("invalid"));
        }
    }

    @Test
    public void testLoadInputStream() throws IOException {
        // typical use case: loading an input stream with UTF-8 charset
        InputStream inputStream = new FileInputStream("test.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(document);

        // edge case: loading an empty input stream
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        Document emptyDocument = DataUtil.load(emptyInputStream, "UTF-8", "http://example.com");
        assertNull(emptyDocument);

        // error scenario: loading an input stream with an unsupported charset
        InputStream unsupportedCharsetInputStream = new FileInputStream("test.html");
        try {
            Document unsupportedCharsetDocument = DataUtil.load(unsupportedCharsetInputStream, "invalid", "http://example.com");
            fail("UnsupportedEncodingException should have been thrown");
        } catch (UnsupportedEncodingException e) {
            assertTrue(e.getMessage().contains("invalid"));
        }
    }

    @Test
    public void testLoadInputStreamParser() throws IOException {
        // typical use case: loading an input stream with UTF-8 charset and custom parser
        InputStream inputStream = new FileInputStream("test.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(document);

        // edge case: loading an input stream with an empty base URI
        InputStream emptyBaseUriInputStream = new FileInputStream("test.html");
        Document emptyBaseUriDocument = DataUtil.load(emptyBaseUriInputStream, "UTF-8", "");
        assertNotNull(emptyBaseUriDocument);

        // error scenario: loading an input stream with an unsupported charset and custom parser
        InputStream unsupportedCharsetInputStream = new FileInputStream("test.html");
        try {
            Document unsupportedCharsetDocument = DataUtil.load(unsupportedCharsetInputStream, "invalid", "http://example.com", Parser.htmlParser());
            fail("UnsupportedEncodingException should have been thrown");
        } catch (UnsupportedEncodingException e) {
            assertTrue(e.getMessage().contains("invalid"));
        }
    }

    @Test
    public void testParseByteData() {
        // typical use case: parsing byte data with UTF-8 charset
        String byteData = "test";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(ByteBuffer.wrap(byteData.getBytes()), "UTF-8", "http://example.com", parser);
        assertNotNull(document);

        // edge case: parsing empty byte data
        Document emptyByteDataDocument = DataUtil.parseByteData(ByteBuffer.wrap(new byte[0]), "UTF-8", "http://example.com", parser);
        assertNull(emptyByteDataDocument);

        // error scenario: parsing byte data with null charset
        Document nullCharsetDocument = DataUtil.parseByteData(ByteBuffer.wrap(byteData.getBytes()), null, "http://example.com", parser);
        assertNotNull(nullCharsetDocument);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        // typical use case: reading input stream into a byte buffer
        InputStream inputStream = new FileInputStream("test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteBuffer);

        // edge case: reading an empty input stream into a byte buffer
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        ByteBuffer emptyByteBuffer = DataUtil.readToByteBuffer(emptyInputStream);
        assertNotNull(emptyByteBuffer);

        // error scenario: reading input stream into a byte buffer with a maximum size
        InputStream maxSizeInputStream = new FileInputStream("test.html");
        ByteBuffer maxSizeByteBuffer = DataUtil.readToByteBuffer(maxSizeInputStream, 100);
        assertNotNull(maxSizeByteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        // typical use case: getting the charset from a content type header
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        // edge case: getting the charset from a content type header with no charset
        String noCharsetContentType = "text/html";
        String noCharset = DataUtil.getCharsetFromContentType(noCharsetContentType);
        assertNull(noCharset);

        // error scenario: getting the charset from a content type header with an unsupported charset
        String unsupportedCharsetContentType = "text/html; charset=invalid";
        String unsupportedCharset = DataUtil.getCharsetFromContentType(unsupportedCharsetContentType);
        assertNull(unsupportedCharset);
    }
}