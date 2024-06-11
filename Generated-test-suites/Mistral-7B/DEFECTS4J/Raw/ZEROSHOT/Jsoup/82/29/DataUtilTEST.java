package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import org.junit.Test;

/**
 * Unit tests for {@link DataUtil} class.
 *
 */
public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        String fileContent = "This is a test file.";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";

        File testFile = new File("test.html");
        testFile.createNewFile();
        try (OutputStream outputStream = new FileOutputStream(testFile)) {
            outputStream.write(fileContent.getBytes(charsetName));
        }

        Document document = DataUtil.load(testFile, charsetName, baseUri);
        assertNotNull(document);
        assertEquals(fileContent, document.body().html());
        assertEquals(charsetName, document.outputSettings().charset());

        testFile.delete();
    }

    @Test
    public void testLoadDocumentFromInputStream() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";

        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(charsetName));

        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
        assertEquals(inputStreamContent, document.body().html());
        assertEquals(charsetName, document.outputSettings().charset());

        inputStream.close();
    }

    @Test
    public void testLoadDocumentWithInvalidCharsetName() throws IOException {
        String charsetName = "Xyz";
        String baseUri = "http://example.com/";

        InputStream inputStream = new ByteArrayInputStream("This is a test input stream.".getBytes(Charset.forName("UTF-8").name()));

        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            assertFalse("An exception should be thrown due to invalid charset name", true);
        } catch (IllegalCharsetNameException e) {
            // expected
        } finally {
            inputStream.close();
        }
    }

    @Test
    public void testParseInputStreamWithMissingCharset() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        String baseUri = "http://example.com/";

        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        Document document = DataUtil.parseInputStream(inputStream, null, baseUri, Parser.htmlParser());
        assertNotNull(document);
        assertEquals(inputStreamContent, document.body().html());
        assertEquals("UTF-8", document.outputSettings().charset());

        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";

        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(charsetName));

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
        assertEquals(inputStreamContent, document.body().html());
        assertEquals(charsetName, document.outputSettings().charset());

        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputContent = "This is a test input stream.";
        String outputContent = "This is a test output stream.";

        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes("UTF-8"));
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] outputBytes = outputStream.toByteArray();
        assertEquals(outputContent.getBytes("UTF-8").length, outputBytes.length);
        assertEquals(outputContent, new String(outputBytes, "UTF-8"));

        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testParseInputStreamWithBom() throws IOException {
        String bomHeader = "\uFEFF";
        String inputStreamContent = "This is a test input stream.";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(bomHeader.getBytes("UTF-8"));
        inputStream.read(); // consume the BOM
        inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(charsetName));

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
        assertEquals(inputStreamContent, document.body().html());
        assertEquals(charsetName, document.outputSettings().charset());

        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithMetaTagCharset() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"GBK\"></head><body>This is a test input stream.</body></html>";
        String charsetName = null;
        String baseUri = "http://example.com/";

        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
        assertEquals(inputStreamContent, document.body().html());
        assertEquals("GBK", document.outputSettings().charset());

        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithXmlDeclarationCharset() throws IOException {
        String xmlDeclarationContent = "<xml version=\"1.0\" encoding=\"GBK\">";
        String inputStreamContent = "<html><body>This is a test input stream.</body></html>";
        String charsetName = null;
        String baseUri = "http://example.com/";

        InputStream inputStream = new ByteArrayInputStream(xmlDeclarationContent.getBytes("UTF-8"));
        inputStream.read(); // consume the XML declaration
        inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
        assertEquals(inputStreamContent, document.body().html());
        assertEquals("GBK", document.outputSettings().charset());

        inputStream.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html;charset=UTF-8";
        String contentType2 = "application/xml;charset=ISO-8859-1";
        String contentType3 = "application/octet-stream";

        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType1));
        assertEquals(null, DataUtil.getCharsetFromContentType(contentType2));
        assertEquals(null, DataUtil.getCharsetFromContentType(contentType3));
    }

    @Test
    public void testValidateCharset() {
        String charset1 = "UTF-8";
        String charset2 = "ISO-8859-1";
        String charset3 = "GB18030";
        String charset4 = "unicode";

        assertEquals(charset1, DataUtil.validateCharset(charset1));
        assertEquals(charset2, DataUtil.validateCharset(charset2));
        assertEquals(charset3, DataUtil.validateCharset(charset3));
        assertEquals(null, DataUtil.validateCharset(charset4));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertEquals(boundary.length(), 32);
        for (int i = 0; i < boundary.length(); i++) {
            assertTrue(Character.isAlphabetic(boundary.charAt(i)) || Character.isDigit(boundary.charAt(i)) || boundary.charAt(i) == '-' || boundary.charAt(i) == '_');
        }
    }

    @Test
    public void testDetectCharsetFromBom() {
        byte[] utf32Bom = {0x00, 0x00, (byte) 0xFE, (byte) 0xFF};
        byte[] utf16BeBom = {(byte) 0xFE, (byte) 0xFF, 0x00, 0x00};
        byte[] utf16LeBom = {(byte) 0xFF, (byte) 0xFE, 0x00, 0x00};
        byte[] utf8Bom = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};

        ByteBuffer utf32ByteBuffer = ByteBuffer.wrap(utf32Bom);
        ByteBuffer utf16BeByteBuffer = ByteBuffer.wrap(utf16BeBom);
        ByteBuffer utf16LeByteBuffer = ByteBuffer.wrap(utf16LeBom);
        ByteBuffer utf8ByteBuffer = ByteBuffer.wrap(utf8Bom);

        BomCharset utf32Charset = DataUtil.detectCharsetFromBom(utf32ByteBuffer);
        BomCharset utf16BeCharset = DataUtil.detectCharsetFromBom(utf16BeByteBuffer);
        BomCharset utf16LeCharset = DataUtil.detectCharsetFromBom(utf16LeByteBuffer);
        BomCharset utf8Charset = DataUtil.detectCharsetFromBom(utf8ByteBuffer);

        assertEquals("UTF-32", utf32Charset.charset);
        assertEquals(false, utf32Charset.offset);
        assertEquals("UTF-16", utf16BeCharset.charset);
        assertEquals(false, utf16BeCharset.offset);
        assertEquals("UTF-16", utf16LeCharset.charset);
        assertEquals(true, utf16LeCharset.offset);
        assertEquals("UTF-8", utf8Charset.charset);
        assertEquals(true, utf8Charset.offset);
    }
}

class BomCharset {
    String charset;
    boolean offset;

    public BomCharset(String charset, boolean offset) {
        this.charset = charset;
        this.offset = offset;
    }
}