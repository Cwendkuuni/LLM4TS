package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;

public class DataUtilTest {

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html xmlns:x=\"http://www.w3.org/1999/xhtml\">" +
                "<head><meta charset=\"UTF-8\"/></head>" +
                "<body><xml encoding=\"ISO-8859-1\"></xml></body>" +
                "</html>";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));

        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        assertEquals(html, doc.html());

        String charsetFromMeta = DataUtil.getCharsetFromContentType(doc.head().metaText());
        assertEquals("UTF-8", charsetFromMeta);

        String charsetFromXml = null;
        if (doc.childNodeSize() > 0) {
            Node first = doc.childNode(0);
            if (first instanceof XmlDeclaration) {
                XmlDeclaration decl = (XmlDeclaration) first;
                charsetFromXml = decl.attr("encoding");
            }
        }
        assertEquals("ISO-8859-1", charsetFromXml);

        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8".toUpperCase(Locale.ENGLISH)));
        assertNull(DataUtil.validateCharset("invalidCharset"));

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 0);
        assertSame(inputStream, byteData.asReadOnlyBuffer().asInputStream());
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        ByteBuffer bomUtf32 = ByteBuffer.wrap(new byte[] {(byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF});
        ByteBuffer bomUtf16BE = ByteBuffer.wrap(new byte[] {(byte) 0xFE, (byte) 0xFF, (byte) 0x00, (byte) 0x00});
        ByteBuffer bomUtf16LE = ByteBuffer.wrap(new byte[] {(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00});
        ByteBuffer bomUtf8 = ByteBuffer.wrap(new byte[] {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});

        ByteBuffer byteData = ByteBuffer.wrap(new byte[] {(byte) 0x01, (byte) 0x02, (byte) 0x03});

        BomCharset utf32 = DataUtil.detectCharsetFromBom(bomUtf32);
        assertEquals("UTF-32", utf32.charset);
        assertFalse(utf32.offset);

        BomCharset utf16BE = DataUtil.detectCharsetFromBom(bomUtf16BE);
        assertEquals("UTF-16", utf16BE.charset);
        assertFalse(utf16BE.offset);

        BomCharset utf16LE = DataUtil.detectCharsetFromBom(bomUtf16LE);
        assertEquals("UTF-16", utf16LE.charset);
        assertFalse(utf16LE.offset);

        BomCharset utf8 = DataUtil.detectCharsetFromBom(bomUtf8);
        assertEquals("UTF-8", utf8.charset);
        assertTrue(utf8.offset);

        assertNull(DataUtil.detectCharsetFromBom(byteData));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertEquals(32, boundary.length());
        for (int i = 0; i < boundary.length(); i++) {
            assertTrue(Arrays.binarySearch(DataUtil.mimeBoundaryChars, boundary.charAt(i)) >= 0);
        }
    }

    @Test
    public void testLoadFile() throws IOException {
        Document doc = DataUtil.load(new File("test.html"), null, null);
        assertNotNull(doc);
        assertEquals("text/html", doc.metaCharset());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html xmlns:x=\"http://www.w3.org/1999/xhtml\">" +
                "<head><meta charset=\"UTF-8\"/></head>" +
                "<body><xml encoding=\"ISO-8859-1\"></xml></body>" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));

        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals(html, doc.html());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharsetName() throws IOException {
        String html = "<html xmlns:x=\"http://www.w3.org/1999/xhtml\">" +
                "<head><meta charset=\"UTF-8\"/></head>" +
                "<body><xml encoding=\"ISO-8859-1\"></xml></body>" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));

        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(inputStream, "invalidCharsetName", null));
    }

    @Test
    public void testLoadInputStreamWithNullCharsetName() throws IOException {
        String html = "<html xmlns:x=\"http://www.w3.org/1999/xhtml\">" +
                "<head><meta charset=\"UTF-8\"/></head>" +
                "<body><xml encoding=\"ISO-8859-1\"></xml></body>" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));

        Document doc = DataUtil.load(inputStream, null, null);
        assertEquals(html, doc.html());
        assertEquals("UTF-8", doc.metaCharset());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String input = "input";
        String output = "output";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(input.getBytes("UTF-8"));

        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals(input, outputStream.toString("UTF-8"));
    }
}