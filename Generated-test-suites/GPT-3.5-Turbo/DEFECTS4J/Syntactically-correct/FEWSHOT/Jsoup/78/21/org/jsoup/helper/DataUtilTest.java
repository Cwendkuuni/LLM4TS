package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataUtilTest {

    @Test
    public void testLoad_File_Charset_BaseUri() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com/";

        Document doc = DataUtil.load(file, charsetName, baseUri);

        assertNotNull(doc);
        // add more assertions
    }

    @Test
    public void testLoad_InputStream_Charset_BaseUri() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com/";

        Document doc = DataUtil.load(inputStream, charsetName, baseUri);

        assertNotNull(doc);
        // add more assertions
    }

    @Test
    public void testLoad_InputStream_Charset_BaseUri_Parser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com/";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);

        assertNotNull(doc);
        // add more assertions
    }

    @Test
    public void testCrossStreams() throws IOException {
        // add test case for crossStreams method
    }

    @Test
    public void testParseInputStream_ValidInput() throws IOException {
        // add test case for parseInputStream method with valid input
    }

    @Test
    public void testParseInputStream_EmptyInput() throws IOException {
        // add test case for parseInputStream method with empty input
    }

    @Test
    public void testReadToByteBuffer_MaxSize() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        int maxSize = 1024;

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        assertNotNull(byteBuffer);
        // add more assertions
    }

    @Test
    public void testReadToByteBuffer_NoMaxSize() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        assertNotNull(byteBuffer);
        // add more assertions
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);

        assertNotNull(byteBuffer);
        // add more assertions
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType_WithCharset() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNotNull(charset);
        assertEquals("UTF-8", charset.toUpperCase());
    }

    @Test
    public void testGetCharsetFromContentType_NoCharset() {
        String contentType = "text/html";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType_InvalidCharset() {
        String contentType = "text/html; charset=invalid";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

    @Test
    public void testValidateCharset_ValidCharset() {
        String charset = "UTF-8";

        String validatedCharset = DataUtil.validateCharset(charset);

        assertNotNull(validatedCharset);
        assertEquals("UTF-8", validatedCharset.toUpperCase());
    }

    @Test
    public void testValidateCharset_InvalidCharset() {
        String charset = "invalid";

        String validatedCharset = DataUtil.validateCharset(charset);

        assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();

        assertNotNull(boundary);
        assertEquals(DataUtil.boundaryLength, boundary.length());
    }

    @Test
    public void testDetectCharsetFromBom_ValidBom() {
        ByteBuffer bom = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});

        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(bom);

        assertNotNull(bomCharset);
        assertEquals("UTF-8", bomCharset.charset.toUpperCase());
        assertEquals(3, bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBom_InvalidBom() {
        ByteBuffer bom = ByteBuffer.wrap(new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF});

        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(bom);

        assertNull(bomCharset);
    }
}