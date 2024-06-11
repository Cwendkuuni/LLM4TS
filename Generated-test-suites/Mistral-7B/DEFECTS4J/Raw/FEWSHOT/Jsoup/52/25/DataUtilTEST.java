package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        File testFile = new File("path/to/your/test/file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Document document = DataUtil.load(testFile, charsetName, baseUri);
        assertNotNull(document);
        assertEquals("html", document.tagName());
    }

    @Test
    public void testLoadDocumentFromInputStream() throws IOException {
        String testString = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
        assertEquals("html", document.tagName());
    }

    @Test
    public void testParseByteData() throws IOException {
        String testString = "<html><head></head><body></body></html>";
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new ByteArrayInputStream(testString.getBytes()));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Document document = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("html", document.tagName());
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        byte[] bombUtf32 = {0x00, 0x00, 0xFE, 0xFF};
        byteBuffer.put(bombUtf32);
        String charsetName = DataUtil.detectCharsetFromBom(byteBuffer, null);
        assertEquals("UTF-32", charsetName);

        byteBuffer.clear();
        byte[] bombUtf16Be = {0xFE, 0xFF, 0x00, 0x00};
        byteBuffer.put(bombUtf16Be);
        charsetName = DataUtil.detectCharsetFromBom(byteBuffer, null);
        assertEquals("UTF-16 BE", charsetName);

        byteBuffer.clear();
        byte[] bombUtf16Le = {0xFF, 0xFE, 0x00, 0x00};
        byteBuffer.put(bombUtf16Le);
        charsetName = DataUtil.detectCharsetFromBom(byteBuffer, null);
        assertEquals("UTF-16 LE", charsetName);

        byteBuffer.clear();
        byte[] bombUtf8 = {0xEF, 0xBB, 0xBF};
        byteBuffer.put(bombUtf8);
        charsetName = DataUtil.detectCharsetFromBom(byteBuffer, null);
        assertEquals("UTF-8", charsetName);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charsetName = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charsetName);

        contentType = "text/html";
        charsetName = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charsetName);
    }

    @Test
    public void testValidateCharset() {
        String charsetName = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charsetName);
        assertEquals("UTF-8", validatedCharset);

        charsetName = "UTF-8\t";
        validatedCharset = DataUtil.validateCharset(charsetName);
        assertEquals("UTF-8", validatedCharset);

        charsetName = "UTF-16";
        validatedCharset = DataUtil.validateCharset(charsetName);
        assertEquals("UTF-16", validatedCharset);

        charsetName = "UTF-16\tBE";
        validatedCharset = DataUtil.validateCharset(charsetName);
        assertEquals("UTF-16 BE", validatedCharset);

        charsetName = "UNKNOWN-CHARSET";
        validatedCharset = DataUtil.validateCharset(charsetName);
        assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertTrue(boundary.length() > 0);
        for (int i = 0; i < boundary.length(); i++) {
            assertTrue(DataUtil.mimeBoundaryChars.length > i && DataUtil.mimeBoundaryChars[i] == boundary.charAt(i));
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        DataUtil.crossStreams(inputStream, outputStream);
        byte[] result = outputStream.toByteArray();
        assertEquals("Test".getBytes(), result);
    }
}