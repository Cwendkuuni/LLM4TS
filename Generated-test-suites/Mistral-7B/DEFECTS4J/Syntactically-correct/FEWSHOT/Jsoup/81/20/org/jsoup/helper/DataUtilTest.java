package org.jsoup.helper;

import static org.junit.Assert.*;
import org.jsoup.UncheckedIOException;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com/";
        File file = new File("test.html");
        Document doc = DataUtil.load(file, charset, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com/";
        InputStream input = new FileInputStream("test.html");
        Document doc = DataUtil.load(input, charset, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com/";
        InputStream input = new FileInputStream("test.html");
        Parser parser = DataUtil.Parser.htmlParser();
        Document doc = DataUtil.load(input, charset, baseUri, parser);
        assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream input = new FileInputStream("test.html");
        OutputStream output = new FileOutputStream("test_output.html");
        DataUtil.crossStreams(input, output);
        input.close();
        output.close();
        File testOutputFile = new File("test_output.html");
        assertTrue(testOutputFile.length() > 0);
        testOutputFile.delete();
    }

    @Test
    public void testParseInputStream() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com/";
        InputStream input = new FileInputStream("test.html");
        Document doc = DataUtil.parseInputStream(input, charset, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{0x00, 0x00, (byte) 0xFE, (byte) 0xFF});
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("UTF-32", bomCharset.charset);
        assertTrue(bomCharset.offset);

        byteData = ByteBuffer.wrap(new byte[]{0xFF, 0xFE, 0x00, 0x00});
        bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("UTF-16", bomCharset.charset);
        assertFalse(bomCharset.offset);

        byteData = ByteBuffer.wrap(new byte[]{0xEF, 0xBB, 0xBF});
        bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("UTF-8", bomCharset.charset);
        assertTrue(bomCharset.offset);

        byteData = ByteBuffer.wrap(new byte[]{0xFF, 0xFF, 0x00, 0x00});
        bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertNull(bomCharset);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String cs = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(cs);
        assertEquals(cs, validatedCharset);

        cs = "EUC-JP";
        validatedCharset = DataUtil.validateCharset(cs);
        assertEquals("EUC-JP", validatedCharset);

        cs = "iso-8859-1";
        validatedCharset = DataUtil.validateCharset(cs);
        assertEquals("ISO-8859-1", validatedCharset);

        cs = "invalid_charset";
        validatedCharset = DataUtil.validateCharset(cs);
        assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertFalse(boundary.isEmpty());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteData = DataUtil.readToByteBuffer(file);
        assertNotNull(byteData);
        file.delete();
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteData = DataUtil.readToByteBuffer(file, 1024);
        assertNotNull(byteData);
        file.delete();
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithNegativeMaxSize() throws IOException {
        File file = new File("test.html");
        DataUtil.readToByteBuffer(file, -1);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        assertTrue(byteData.capacity() == 0);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);
        assertNotNull(byteData);
        file.delete();
    }
}