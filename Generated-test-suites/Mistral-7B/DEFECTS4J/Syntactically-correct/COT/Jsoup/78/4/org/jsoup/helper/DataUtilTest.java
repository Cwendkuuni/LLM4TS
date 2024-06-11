package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Test class for DataUtil methods.
 */
public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File file = new File("path/to/file.html");
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFromFileWithNullFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File file = null;
        try {
            DataUtil.load(file, charsetName, baseUri);
        } catch (NullPointerException e) {
            fail("Expected IOException, but got NullPointerException");
        }
        DataUtil.load(new File("path/to/file.html"), charsetName, baseUri);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = new FileInputStream(new File("path/to/file.html"));
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testLoadFromInputStreamWithNullInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = null;
        try {
            DataUtil.load(inputStream, charsetName, baseUri);
        } catch (NullPointerException e) {
            fail("Expected IOException, but got NullPointerException");
        }
        InputStream inputStream1 = new FileInputStream(new File("path/to/file.html"));
        DataUtil.load(inputStream1, charsetName, baseUri);
        inputStream1.close();
    }

    @Test
    public void testLoadFromInputStreamWithWrongCharset() throws IOException {
        String charsetName = "ISO-8859-1";
        String baseUri = "http://example.com";
        InputStream inputStream = new FileInputStream(new File("path/to/file.html"));
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
        // Add assertions to check the document's encoding
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testCrossStreamsWithNullInputStream() throws IOException {
        InputStream input = null;
        OutputStream output = new FileOutputStream("output.txt");
        try {
            DataUtil.crossStreams(input, output);
        } catch (NullPointerException e) {
            fail("Expected IOException, but got NullPointerException");
        }
        InputStream input1 = new FileInputStream(new File("path/to/input.txt"));
        OutputStream output1 = new FileOutputStream("output.txt");
        DataUtil.crossStreams(input1, output1);
        input1.close();
        output1.close();
    }

    @Test
    public void testParseInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = new FileInputStream(new File("path/to/file.html"));
        Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithNullInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = null;
        try {
            DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        } catch (NullPointerException e) {
            fail("Expected IOException, but got NullPointerException");
        }
        InputStream inputStream1 = new FileInputStream(new File("path/to/file.html"));
        Document doc = DataUtil.parseInputStream(inputStream1, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        inputStream1.close();
    }

    @Test
    public void testParseInputStreamWithEmptyInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = new FileInputStream(new File("path/to/empty.txt"));
        Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(0, doc.childNodeSize());
        inputStream.close();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String filePath = "path/to/file.html";
        InputStream inputStream = new FileInputStream(new File(filePath));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertEquals(inputStream.available(), byteBuffer.remaining());
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithNullInputStream() throws IOException {
        InputStream inputStream = null;
        try {
            DataUtil.readToByteBuffer(inputStream);
        } catch (NullPointerException e) {
            fail("Expected IOException, but got NullPointerException");
        }
        InputStream inputStream1 = new FileInputStream(new File("path/to/file.html"));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream1);
        inputStream1.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);

        contentType = "text/html; charset=ISO-8859-1";
        expectedCharset = "ISO-8859-1";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validatedCharset);

        charset = "";
        validatedCharset = DataUtil.validateCharset(charset);
        assertNull(validatedCharset);

        charset = "UTF-8\"";
        validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(charset.substring(0, charset.length() - 1), validatedCharset);

        charset = "UTF-8\t";
        validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validatedCharset);

        charset = "UTF-8\r\n";
        validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validatedCharset);

        charset = "UTF-8\r";
        validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validatedCharset);

        charset = "UTF-8\n";
        validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validatedCharset);

        charset = "UTF-8\r\n\t";
        validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validatedCharset);

        charset = "UTF-8\r\n\tUTF-8";
        validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(charset.substring(0, charset.indexOf("UTF-8")), validatedCharset);

        charset = "UTF-16BE";
        validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validatedCharset);

        charset = "UTF-16LE";
        validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validatedCharset);

        charset = "UTF-32BE";
        validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validatedCharset);

        charset = "UTF-32LE";
        validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validatedCharset);

        charset = "UTF-8\r\n\tUTF-16BE";
        validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(charset.substring(0, charset.indexOf("UTF-16BE")), validatedCharset);

        charset = "UTF-8\r\n\tUTF-16LE";
        validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(charset.substring(0, charset.indexOf("UTF-16LE")), validatedCharset);

        charset = "UTF-8\r\n\tUTF-32BE";
        validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(charset.substring(0, charset.indexOf("UTF-32BE")), validatedCharset);

        charset = "UTF-8\r\n\tUTF-32LE";
        validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(charset.substring(0, charset.indexOf("UTF-32LE")), validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertFalse(boundary.isEmpty());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("path/to/file.html"));
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        if (bomCharset != null) {
            assertEquals("UTF-8", bomCharset.charset);
            assertEquals(3, bomCharset.offset);
        } else {
            assertNull(bomCharset);
        }
    }

    @Test
    public void testDetectCharsetFromBomWithInvalidBom() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertNull(bomCharset);
    }

    @Test
    public void testDetectCharsetFromBomWithUtf16Lebom() throws IOException {
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("path/to/file.html"));
        byteData.position(0);
        byteData.put((byte) 0xFF);
        byteData.put((byte) 0xFE);
        byteData.put((byte) 0x00);
        byteData.put((byte) 0x00);
        byteData.flip();
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("UTF-16LE", bomCharset.charset);
        assertEquals(0, bomCharset.offset);
        byteData.clear();
    }

    @Test
    public void testDetectCharsetFromBomWithUtf16Bebom() throws IOException {
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("path/to/file.html"));
        byteData.position(0);
        byteData.put((byte) 0x00);
        byteData.put((byte) 0x00);
        byteData.put((byte) 0xFF);
        byteData.put((byte) 0xFE);
        byteData.flip();
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("UTF-16BE", bomCharset.charset);
        assertEquals(0, bomCharset.offset);
        byteData.clear();
    }

    @Test
    public void testDetectCharsetFromBomWithUtf32Lebom() throws IOException {
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("path/to/file.html"));
        byteData.position(0);
        byteData.put((byte) 0x00);
        byteData.put((byte) 0x00);
        byteData.put((byte) 0x00);
        byteData.put((byte) 0x00);
        byteData.put((byte) 0xFF);
        byteData.put((byte) 0xFE);
        byteData.flip();
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("UTF-32LE", bomCharset.charset);
        assertEquals(0, bomCharset.offset);
        byteData.clear();
    }

    @Test
    public void testDetectCharsetFromBomWithUtf32Bebom() throws IOException {
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("path/to/file.html"));
        byteData.position(0);
        byteData.put((byte) 0xFF);
        byteData.put((byte) 0xFE);
        byteData.put((byte) 0x00);
        byteData.put((byte) 0x00);
        byteData.put((byte) 0x00);
        byteData.put((byte) 0x00);
        byteData.flip();
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("UTF-32BE", bomCharset.charset);
        assertEquals(0, bomCharset.offset);
        byteData.clear();
    }
}