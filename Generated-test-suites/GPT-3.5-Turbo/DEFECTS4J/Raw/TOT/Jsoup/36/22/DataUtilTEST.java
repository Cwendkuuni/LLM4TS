package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.*;

public class DataUtilTest {

    private static DataUtil dataUtil;

    @BeforeClass
    public static void setUp() {
        dataUtil = new DataUtil();
    }

    /*
     * Test case for load(File in, String charsetName, String baseUri) method
     * Testing typical use case where the file is successfully loaded and parsed to a document
     */
    @Test
    public void testLoadFile() {
        File file = new File("test.html");
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "https://www.example.com";
        
        try {
            Document document = dataUtil.load(file, charsetName, baseUri);
            assertNotNull(document);
            // validate if the loaded document has the correct base uri
            assertEquals(baseUri, document.baseUri());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    /*
     * Test case for load(InputStream in, String charsetName, String baseUri) method
     * Testing typical use case where the input stream is successfully loaded and parsed to a document
     */
    @Test
    public void testLoadInputStream() {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "https://www.example.com";

        try {
            Document document = dataUtil.load(inputStream, charsetName, baseUri);
            assertNotNull(document);
            // validate if the loaded document has the correct base uri
            assertEquals(baseUri, document.baseUri());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    /*
     * Test case for load(InputStream in, String charsetName, String baseUri, Parser parser) method
     * Testing typical use case where the input stream is successfully loaded and parsed to a document using a custom parser
     */
    @Test
    public void testLoadInputStreamWithParser() {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();

        try {
            Document document = dataUtil.load(inputStream, charsetName, baseUri, parser);
            assertNotNull(document);
            // validate if the loaded document has the correct base uri
            assertEquals(baseUri, document.baseUri());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    /*
     * Test case for parseByteData(ByteBuffer byteData, String charsetName, String baseUri, Parser parser) method
     * Testing typical use case where the byte data is successfully parsed to a document
     */
    @Test
    public void testParseByteData() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();

        Document document = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
        // validate if the loaded document has the correct base uri
        assertEquals(baseUri, document.baseUri());
    }

    /*
     * Test case for readToByteBuffer(InputStream inStream, int maxSize) method
     * Testing typical use case where the input stream is successfully read and converted to a byte buffer
     */
    @Test
    public void testReadToByteBuffer() {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        int maxSize = 0; // unlimited

        try {
            ByteBuffer byteBuffer = dataUtil.readToByteBuffer(inputStream, maxSize);
            assertNotNull(byteBuffer);
            // validate if the byte buffer has data
            assertTrue(byteBuffer.remaining() > 0);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    /*
     * Test case for readToByteBuffer(InputStream inStream) method
     * Testing typical use case where the input stream is successfully read and converted to a byte buffer
     */
    @Test
    public void testReadToByteBufferWithNoLimit() {
        InputStream inputStream = getClass().getResourceAsStream("test.html");

        try {
            ByteBuffer byteBuffer = dataUtil.readToByteBuffer(inputStream);
            assertNotNull(byteBuffer);
            // validate if the byte buffer has data
            assertTrue(byteBuffer.remaining() > 0);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    /*
     * Test case for getCharsetFromContentType(String contentType) method
     * Testing typical use case where the charset is successfully extracted from the content type header
     */
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = StandardCharsets.UTF_8.name();

        String charset = dataUtil.getCharsetFromContentType(contentType);

        assertNotNull(charset);
        assertEquals(expectedCharset, charset);
    }

    /*
     * Test case for getCharsetFromContentType(String contentType) method
     * Testing edge case where an unsupported charset is provided in the content type header
     */
    @Test
    public void testGetCharsetFromContentTypeWithUnsupportedCharset() {
        String contentType = "text/html; charset=INVALID";
        String expectedCharset = null;

        String charset = dataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
        assertEquals(expectedCharset, charset);
    }

    /*
     * Test case for getCharsetFromContentType(String contentType) method
     * Testing edge case where an empty content type header is provided
     */
    @Test
    public void testGetCharsetFromContentTypeWithEmptyContentType() {
        String contentType = "";
        String expectedCharset = null;

        String charset = dataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
        assertEquals(expectedCharset, charset);
    }

}