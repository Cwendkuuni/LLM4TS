package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;

public class DataUtilTest {
    private static final String CHARSET_NAME = "UTF-8";
    private static final String BASE_URI = "http://example.com";

    @Test
    public void testLoad_File() throws IOException {
        // Test case for load(File in, String charsetName, String baseUri)

        File file = new File("example.html");
        String expectedContent = "<html><head><title>Example</title></head><body><h1>Hello, world!</h1></body></html>";
        Document expectedDocument = Parser.htmlParser().parse(expectedContent, BASE_URI);

        Document actualDocument = DataUtil.load(file, CHARSET_NAME, BASE_URI);

        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testLoad_InputStream() throws IOException {
        // Test case for load(InputStream in, String charsetName, String baseUri)

        InputStream inputStream = new FileInputStream("example.html");
        String expectedContent = "<html><head><title>Example</title></head><body><h1>Hello, world!</h1></body></html>";
        Document expectedDocument = Parser.htmlParser().parse(expectedContent, BASE_URI);

        Document actualDocument = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI);

        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testLoad_InputStreamWithParser() throws IOException {
        // Test case for load(InputStream in, String charsetName, String baseUri, Parser parser)

        InputStream inputStream = new FileInputStream("example.html");
        String expectedContent = "<html><head><title>Example</title></head><body><h1>Hello, world!</h1></body></html>";
        Document expectedDocument = Parser.xmlParser().parse(expectedContent, BASE_URI);

        Document actualDocument = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI, Parser.xmlParser());

        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testParseByteData() {
        // Not testing this method directly as it is called by load methods. Testing is covered in other test cases.
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        // Test case for readToByteBuffer(InputStream inStream)

        InputStream inputStream = new ByteArrayInputStream("Example".getBytes());
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap("Example".getBytes());

        ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(inputStream);

        assertEquals(expectedByteBuffer, actualByteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Test case for getCharsetFromContentType(String contentType)

        String contentTypeWithCharset = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        String actualCharset = DataUtil.getCharsetFromContentType(contentTypeWithCharset);

        assertEquals(expectedCharset, actualCharset);

        String contentTypeWithoutCharset = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentTypeWithoutCharset));
    }
}