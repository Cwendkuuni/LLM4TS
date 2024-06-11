package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    /**
     * Test method for {@link DataUtil#load(File, String, String)}.
     */
    @org.junit.Test
    public void testLoadFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File inputFile = new File("src/test/resources/testFile.html");

        Document document = DataUtil.load(inputFile, charsetName, baseUri);
        assertNotNull(document);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}.
     */
    @org.junit.Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        InputStream inputStream = this.getClass().getResourceAsStream("/testFile.html");
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)}.
     */
    @org.junit.Test
    public void testLoadInputStreamWithParser() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        InputStream inputStream = this.getClass().getResourceAsStream("/testFile.html");
        Document document = DataUtil.load(inputStream, charsetName, baseUri, Parser.xmlParser());
        assertNotNull(document);
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)}.
     */
    @org.junit.Test
    public void testParseByteData() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        byte[] testData = new byte[]{'<', 'h', 't', 'm', 'l', ' ', 'd', 'o', 'c', 'u', 'm', 'e', 'n', 't', ' ', 't', 'e', 's', 't', ' ', 'F', 'i', 'l', 'e', '.', 'h', 't', 'm', 'l', '>', '\n'};
        byteBuffer.put(testData);
        byteBuffer.flip();

        Document document = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    /**
     * Test method for {@link DataUtil#readToByteBuffer(InputStream, int)}.
     */
    @org.junit.Test
    public void testReadToByteBuffer() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        InputStream inputStream = this.getClass().getResourceAsStream("/testFile.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteBuffer);

        // Test if byteBuffer can be converted back to InputStream and read correctly
        InputStream inputStreamFromByteBuffer = DataUtil.getInputStreamFromByteBuffer(byteBuffer);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;

        while ((length = inputStreamFromByteBuffer.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        String expected = new String(this.getClass().getResourceAsStream("/testFile.html").readAllBytes(), Charset.forName(charsetName));
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)}.
     */
    @org.junit.Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expected = "UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expected, charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}