package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><div>Hello</div></body></html>";
        ByteArrayInputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(input, "UTF-8", null, DataUtil.Parser.htmlParser());
        assertEquals(html, doc.html());

        // Test with null charset
        doc = DataUtil.parseInputStream(input, null, null, DataUtil.Parser.htmlParser());
        assertEquals(html, doc.html());

        // Test with no BOM or meta charset
        input.reset();
        String noBomHtml = "<html><body><div>Hello</div></body></html>";
        ByteArrayInputStream noBomInput = new ByteArrayInputStream(noBomHtml.getBytes(StandardCharsets.UTF_8));
        doc = DataUtil.parseInputStream(noBomInput, null, null, DataUtil.Parser.htmlParser());
        assertEquals(noBomHtml, doc.html());

        // Test with invalid charset
        String invalidCharsetHtml = "<html><head><meta charset=\"InvalidCharset\"></head><body><div>Hello</div></body></html>";
        ByteArrayInputStream invalidInput = new ByteArrayInputStream(invalidCharsetHtml.getBytes(StandardCharsets.UTF_8));
        doc = DataUtil.parseInputStream(invalidInput, "InvalidCharset", null, DataUtil.Parser.htmlParser());
        assertEquals(html, doc.html()); // The default charset is UTF-8

        // Test with content-type header
        String contentTypeHeaderHtml = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><div>Hello</div></body></html>";
        ByteArrayInputStream contentTypeInput = new ByteArrayInputStream(contentTypeHeaderHtml.getBytes(StandardCharsets.UTF_8));
        doc = DataUtil.parseInputStream(contentTypeInput, null, null, DataUtil.Parser.htmlParser());
        assertEquals(contentTypeHeaderHtml, doc.html());

        // Test with xml declaration
        String xmlDeclarationHtml = "<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xmlns=\"http://www.w3.org/2000/xmlns/\"><head><meta charset=\"UTF-8\"></head><body><div>Hello</div></body></html>";
        ByteArrayInputStream xmlDeclarationInput = new ByteArrayInputStream(xmlDeclarationHtml.getBytes(StandardCharsets.UTF_8));
        doc = DataUtil.parseInputStream(xmlDeclarationInput, null, null, DataUtil.Parser.htmlParser());
        assertEquals(xmlDeclarationHtml, doc.html());

        // Test with invalid charset in content-type header
        String invalidContentTypeHeaderHtml = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=InvalidCharset\"></head><body><div>Hello</div></body></html>";
        ByteArrayInputStream invalidContentTypeInput = new ByteArrayInputStream(invalidContentTypeHeaderHtml.getBytes(StandardCharsets.UTF_8));
        doc = DataUtil.parseInputStream(invalidContentTypeInput, null, null, DataUtil.Parser.htmlParser());
        assertEquals(html, doc.html()); // The default charset is UTF-8

        // Test with valid charset in content-type header
        String validContentTypeHeaderHtml = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><div>Hello</div></body></html>";
        ByteArrayInputStream validContentTypeInput = new ByteArrayInputStream(validContentTypeHeaderHtml.getBytes(StandardCharsets.UTF_8));
        doc = DataUtil.parseInputStream(validContentTypeInput, "UTF-8", null, DataUtil.Parser.htmlParser());
        assertEquals(validContentTypeHeaderHtml, doc.html());

        // Test with invalid charset in xml declaration
        String invalidXmlDeclarationHtml = "<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xmlns=\"http://www.w3.org/2000/xmlns/\"><head><meta charset=\"UTF-8\"></head><body><div>Hello</div></body></html>";
        String invalidXmlDeclarationCharset = "InvalidCharset";
        ByteArrayInputStream invalidXmlDeclarationInput = new ByteArrayInputStream(invalidXmlDeclarationHtml.getBytes(StandardCharsets.UTF_8));
        doc = DataUtil.parseInputStream(invalidXmlDeclarationInput, invalidXmlDeclarationCharset, null, DataUtil.Parser.htmlParser());
        assertEquals(html, doc.html()); // The default charset is UTF-8

        // Test with valid charset in xml declaration
        String validXmlDeclarationHtml = "<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xmlns=\"http://www.w3.org/2000/xmlns/\" xml:encoding=\"UTF-8\"><head><meta charset=\"UTF-8\"></head><body><div>Hello</div></body></html>";
        ByteArrayInputStream validXmlDeclarationInput = new ByteArrayInputStream(validXmlDeclarationHtml.getBytes(StandardCharsets.UTF_8));
        doc = DataUtil.parseInputStream(validXmlDeclarationInput, null, null, DataUtil.Parser.htmlParser());
        assertEquals(validXmlDeclarationHtml, doc.html());

        // Test with UTF-16 BOM
        String utf16BomHtml = "\uFEFF<html><head><meta charset=\"UTF-8\"></head><body><div>Hello</div></body></html>";
        ByteArrayInputStream utf16BomInput = new ByteArrayInputStream(utf16BomHtml.getBytes(StandardCharsets.UTF_16LE));
        doc = DataUtil.parseInputStream(utf16BomInput, null, null, DataUtil.Parser.htmlParser());
        assertEquals(html, doc.html()); // The default charset is UTF-8

        // Test with UTF-32 BOM
        String utf32BomHtml = "\u0000\u0000\u00FE\u00FF<html><head><meta charset=\"UTF-8\"></head><body><div>Hello</div></body></html>";
        ByteArrayInputStream utf32BomInput = new ByteArrayInputStream(utf32BomHtml.getBytes(StandardCharsets.UTF_32BE));
        doc = DataUtil.parseInputStream(utf32BomInput, null, null, DataUtil.Parser.htmlParser());
        assertEquals(html, doc.html()); // The default charset is UTF-8
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><div>Hello</div></body></html>";
        ByteArrayInputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(input, -1);
        assertTrue(byteBuffer.hasRemaining());

        byte[] expectedBytes = html.getBytes(StandardCharsets.UTF_8);
        byte[] actualBytes = new byte[expectedBytes.length];
        int bytesRead = byteBuffer.get(actualBytes);
        assertArrayEquals(expectedBytes, actualBytes);

        // Test reading all bytes
        byteBuffer = DataUtil.readToByteBuffer(input, expectedBytes.length);
        assertTrue(byteBuffer.position() == expectedBytes.length);
        assertTrue(byteBuffer.hasRemaining() == false);

        // Test interruption
        Thread interruptionThread = new Thread(() -> {
            try {
                DataUtil.readToByteBuffer(input, 1000);
            } catch (InterruptedIOException e) {
                // Expected
            }
        });
        interruptionThread.start();
        input.close();
        interruptionThread.join();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String charset1 = DataUtil.getCharsetFromContentType(contentType1);
        assertEquals("UTF-8", charset1);

        String contentType2 = "text/html; charset=gb2312";
        String charset2 = DataUtil.getCharsetFromContentType(contentType2);
        assertEquals("gb2312", charset2);

        String contentType3 = "text/html";
        String charset3 = DataUtil.getCharsetFromContentType(contentType3);
        assertNull(charset3);
    }

    @Test
    public void testValidateCharset() {
        String charset1 = "UTF-8";
        String charset2 = "gb2312";
        String charset3 = "InvalidCharset";
        String charset4 = null;

        String validCharset1 = DataUtil.validateCharset(charset1);
        assertEquals("UTF-8", validCharset1);

        String validCharset2 = DataUtil.validateCharset(charset2);
        assertEquals("gb2312", validCharset2);

        String validCharset3 = DataUtil.validateCharset(charset3);
        assertNull(validCharset3);

        String validCharset4 = DataUtil.validateCharset(charset4);
        assertNull(validCharset4);

        String charset5 = "UTF-8\t";
        String validCharset5 = DataUtil.validateCharset(charset5);
        assertEquals("UTF-8", validCharset5);

        String charset6 = "UTF-8\"";
        String validCharset6 = DataUtil.validateCharset(charset6);
        assertEquals("UTF-8", validCharset6);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertTrue(boundary.length() == 32);
        for (int i = 0; i < boundary.length(); i++) {
            assertTrue(Character.isAlphabetic(boundary.charAt(i)) || Character.isDigit(boundary.charAt(i)) || boundary.charAt(i) == '-');
        }
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer bomUtf8 = ByteBuffer.wrap("\u00EF\u00BB\u00BF".getBytes());
        BomCharset utf8Bom = DataUtil.detectCharsetFromBom(bomUtf8);
        assertEquals("UTF-8", utf8Bom.charset);
        assertFalse(utf8Bom.offset);

        ByteBuffer bomUtf16 = ByteBuffer.wrap("\u00FE\u00FF".getBytes(StandardCharsets.UTF_16LE));
        BomCharset utf16Bom = DataUtil.detectCharsetFromBom(bomUtf16);
        assertEquals("UTF-16", utf16Bom.charset);
        assertFalse(utf16Bom.offset);

        ByteBuffer bomUtf32 = ByteBuffer.wrap("\u0000\u0000\u00FE\u00FF".getBytes(StandardCharsets.UTF_32BE));
        BomCharset utf32Bom = DataUtil.detectCharsetFromBom(bomUtf32);
        assertEquals("UTF-32", utf32Bom.charset);
        assertFalse(utf32Bom.offset);

        ByteBuffer bomInvalid = ByteBuffer.wrap("\u0000\u0001\u0002\u0003".getBytes(StandardCharsets.UTF_8));
        BomCharset invalidBom = DataUtil.detectCharsetFromBom(bomInvalid);
        assertNull(invalidBom);
    }
}