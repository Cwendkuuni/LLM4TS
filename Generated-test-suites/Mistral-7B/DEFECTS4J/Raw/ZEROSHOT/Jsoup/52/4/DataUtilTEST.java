package org.jsoup.helper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import org.junit.Test;

/**
 * Tests for the DataUtil class.
 */
public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        String fileContent = "Hello World!";
        File inputFile = new File("test.html");
        Files.write(inputFile.getAbsoluteFile(), fileContent.getBytes());

        Document document = DataUtil.load(inputFile, "UTF-8", null);
        assertNotNull(document);
        assertTrue(document.text().equals(fileContent));

        Files.delete(inputFile.getAbsoluteFile());
    }

    @Test
    public void testLoadDocumentFromInputStream() throws IOException {
        String content = "Hello World!";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());

        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(document);
        assertTrue(document.text().equals(content));

        inputStream.close();
    }

    @Test
    public void testLoadDocumentFromInputStreamWithParser() throws IOException {
        String content = "<html><body><h1>Hello World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());

        Document document = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
        assertNotNull(document);
        assertTrue(document.titleText().equals("Hello World!"));

        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        String testContent = "Test Content";
        String outputContent = "";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(testContent.getBytes());

        DataUtil.crossStreams(inputStream, outputStream);
        outputContent = outputStream.toString();

        assertTrue(outputContent.equals(testContent));

        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testParseByteData() throws IOException {
        String fileContent = "<html><body><h1>Hello World!</h1></body></html>";
        byte[] fileData = fileContent.getBytes();
        ByteBuffer byteData = ByteBuffer.wrap(fileData);

        Document document = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertNotNull(document);
        assertTrue(document.titleText().equals("Hello World!"));

        ByteBuffer emptyByteBuffer = DataUtil.emptyByteBuffer();
        Document emptyDocument = DataUtil.parseByteData(emptyByteBuffer, "UTF-8", null, Parser.htmlParser());
        assertNull(emptyDocument);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testContent = "Test Content";
        InputStream inputStream = new ByteArrayInputStream(testContent.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        byte[] expectedByteArray = testContent.getBytes();
        byte[] actualByteArray = new byte[byteBuffer.limit()];
        byteBuffer.get(actualByteArray);

        assertArrayEquals(expectedByteArray, actualByteArray);

        inputStream.close();
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String fileContent = "Hello World!";
        File inputFile = new File("test.html");
        Files.write(inputFile.getAbsoluteFile(), fileContent.getBytes());

        ByteBuffer byteData = DataUtil.readFileToByteBuffer(inputFile);
        assertNotNull(byteData);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byteData.rewind();
        byte[] expectedByteArray = new byte[byteData.limit()];
        byteData.get(expectedByteArray);
        outputStream.write(expectedByteArray);
        String outputContent = outputStream.toString();

        assertTrue(outputContent.equals(fileContent));

        Files.delete(inputFile.getAbsoluteFile());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=undefined";
        String expectedCharset = "UTF-8"; // default charset

        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String expectedCharset = "UTF-8";

        String validCharset = DataUtil.validateCharset(charset);
        assertEquals(expectedCharset, validCharset);

        String invalidCharset1 = "UTF-16";
        String expectedCharset1 = "UTF-8"; // default charset

        String validCharset1 = DataUtil.validateCharset(invalidCharset1);
        assertEquals(expectedCharset1, validCharset1);

        String invalidCharset2 = "UTF-16BE";
        String expectedCharset2 = "UTF-8"; // default charset

        String validCharset2 = DataUtil.validateCharset(invalidCharset2);
        assertEquals(expectedCharset2, validCharset2);

        String invalidCharset3 = "UTF-16LE";
        String expectedCharset3 = "UTF-8"; // default charset

        String validCharset3 = DataUtil.validateCharset(invalidCharset3);
        assertEquals(expectedCharset3, validCharset3);

        String invalidCharset4 = "UTF-32";
        String expectedCharset4 = "UTF-8"; // default charset

        String validCharset4 = DataUtil.validateCharset(invalidCharset4);
        assertEquals(expectedCharset4, validCharset4);

        String invalidCharset5 = "abc";
        String expectedCharset5 = "UTF-8"; // default charset

        String validCharset5 = DataUtil.validateCharset(invalidCharset5);
        assertEquals(expectedCharset5, validCharset5);
    }

    @Test
    public void testMimeBoundary() {
        String expectedBoundary = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String boundary = DataUtil.mimeBoundary();

        assertEquals(expectedBoundary.length(), boundary.length());
        assertTrue(boundary.matches("^[a-zA-Z0-9_-]{32}$"));
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer utf8Bom = ByteBuffer.wrap(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
        String utf8Charset = "UTF-8";

        String charset = DataUtil.detectCharsetFromBom(utf8Bom, null);
        assertEquals(utf8Charset, charset);

        ByteBuffer utf16BomBE = ByteBuffer.wrap(new byte[] { (byte) 0xFF, (byte) 0xFE, 0x00, 0x00 });
        String utf16CharsetBE = "UTF-16BE";

        String charsetBE = DataUtil.detectCharsetFromBom(utf16BomBE, null);
        assertEquals(utf16CharsetBE, charsetBE);

        ByteBuffer utf16BomLE = ByteBuffer.wrap(new byte[] { 0x00, 0x00, (byte) 0xFE, (byte) 0xFF });
        String utf16CharsetLE = "UTF-16LE";

        String charsetLE = DataUtil.detectCharsetFromBom(utf16BomLE, null);
        assertEquals(utf16CharsetLE, charsetLE);

        ByteBuffer utf32Bom = ByteBuffer.wrap(new byte[] { (byte) 0xFE, (byte) 0xFF, 0x00, 0x00 });
        String utf32Charset = "UTF-32";

        String charsetUTF32 = DataUtil.detectCharsetFromBom(utf32Bom, null);
        assertEquals(utf32Charset, charsetUTF32);

        ByteBuffer noBom = ByteBuffer.wrap(new byte[] { 0x3C, 0x68, 0x65, 0x61, 0x64, 0x65, 0x61, 0x20, 0x4D, 0x79, 0x53, 0x43, 0x6F, 0x6D, 0x70, 0x6C, 0x65, 0x20, 0x74, 0x68, 0x69, 0x73, 0x74, 0x72, 0x61, 0x63, 0x6B, 0x20, 0x73, 0x72, 0x63, 0x73, 0x68, 0x65, 0x6D, 0x65, 0x6D, 0x20, 0x68, 0x6F, 0x6D, 0x65, 0x20, 0x63, 0x61, 0x6E, 0x6E, 0x6F, 0x74, 0x20, 0x63, 0x72, 0x65, 0x61, 0x74, 0x65, 0x20, 0x6C, 0x69, 0x62, 0x65, 0x20, 0x77, 0x72, 0x69, 0x74, 0x65, 0x20, 0x73, 0x61, 0x76, 0x65, 0x20, 0x63, 0x72, 0x63, 0x20, 0x6F, 0x6E, 0x20, 0x66, 0x6F, 0x72, 0x20, 0x74, 0x65, 0x73, 0x74, 0x2F, 0x69, 0x6F, 0x20, 0x73, 0x74, 0x72, 0x61, 0x63, 0x65, 0x70, 0x74, 0x3E, 0x3C, 0x2F, 0x3E, 0x3C, 0x2F, 0x3E });
        String noBomCharset = null;

        String charsetNoBom = DataUtil.detectCharsetFromBom(noBom, noBomCharset);
        assertNull(charsetNoBom);
    }
}