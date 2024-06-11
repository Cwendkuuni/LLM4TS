package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
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
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"/><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-16\"/><title>Test</title></head><body></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(input, "UTF-8", null, Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals(html, doc.html());

        input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_16));

        doc = DataUtil.parseInputStream(input, null, null, Parser.htmlParser());
        assertEquals("UTF-16", doc.outputSettings().charset());
        assertEquals(html, doc.html());

        input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8), 0, html.length() - 1);

        doc = DataUtil.parseInputStream(input, "UTF-8", null, Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals(html.substring(0, html.length() - 1), doc.html());
    }

    @Test
    public void testParseInputStreamWithContentTypeHeader() throws IOException {
        String html = "<html><head><title>Test</title></head><body></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(input, "text/html; charset=UTF-16", null, Parser.htmlParser());
        assertEquals("UTF-16", doc.outputSettings().charset());
        assertEquals(html, doc.html());
    }

    @Test
    public void testParseInputStreamWithXmlDeclaration() throws IOException {
        String xml = "<?xml encoding=\"UTF-8\"?><root></root>";
        InputStream input = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(input, null, null, Parser.xmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals(xml, doc.xml().toString());
    }

    @Test
    public void testParseInputStreamWithMetaTag() throws IOException {
        String html = "<html><head><meta charset=\"UTF-16\"/><title>Test</title></head><body></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(input, null, null, Parser.htmlParser());
        assertEquals("UTF-16", doc.outputSettings().charset());
        assertEquals(html, doc.html());
    }

    @Test
    public void testParseInputStreamWithBomUtf8() throws IOException {
        String html = "\uFEFF<html><head><title>Test</title></head><body></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(input, null, null, Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals(html, doc.html());
    }

    @Test
    public void testParseInputStreamWithBomUtf16() throws IOException {
        String html = "\uFEFF\uFEFF<html><head><title>Test</title></head><body></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_16));

        Document doc = DataUtil.parseInputStream(input, null, null, Parser.htmlParser());
        assertEquals("UTF-16", doc.outputSettings().charset());
        assertEquals(html, doc.html());
    }

    @Test
    public void testParseInputStreamWithBomUtf32() throws IOException {
        String html = "\u0000\u0000\u0000\u0000<html><head><title>Test</title></head><body></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_32));

        Document doc = DataUtil.parseInputStream(input, null, null, Parser.htmlParser());
        assertEquals("UTF-32", doc.outputSettings().charset());
        assertEquals(html, doc.html());
    }

    @Test
    public void testParseInputStreamWithBomUtf8_InvalidBom() throws IOException {
        String html = "\u0000<html><head><title>Test</title></head><body></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        try {
            DataUtil.parseInputStream(input, null, null, Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testParseInputStreamWithBomUtf8_InvalidBomLength() throws IOException {
        String html = "\uFEFF<html><head><title>Test</title></head><body></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8), 0, 2);

        try {
            DataUtil.parseInputStream(input, null, null, Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><head><title>Test</title></head><body></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(input);

        byte[] expectedBytes = html.getBytes(StandardCharsets.UTF_8);
        byte[] actualBytes = new byte[byteBuffer.limit()];
        byteBuffer.get(actualBytes);

        assertSame(expectedBytes, actualBytes);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String html = "<html><head><title>Test</title></head><body></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        int maxSize = 5;

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(input, maxSize);

        byte[] expectedBytes = html.subSequence(0, Math.min(html.length(), maxSize)).getBytes(StandardCharsets.UTF_8);
        byte[] actualBytes = new byte[byteBuffer.limit()];
        byteBuffer.get(actualBytes);

        assertSame(expectedBytes, actualBytes);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String filePath = "test.html";
        String html = "<html><head><title>Test</title></head><body></body></html>";
        writeFile(filePath, html);

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(new File(filePath));

        byte[] expectedBytes = html.getBytes(StandardCharsets.UTF_8);
        byte[] actualBytes = new byte[byteBuffer.limit()];
        byteBuffer.get(actualBytes);

        assertSame(expectedBytes, actualBytes);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        assertEquals(0, byteBuffer.limit());
        assertEquals(0, byteBuffer.position());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String contentType2 = "text/html; charset=unknown";
        String contentType3 = "text/html";

        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType1));
        assertEquals(null, DataUtil.getCharsetFromContentType(contentType2));
        assertEquals(null, DataUtil.getCharsetFromContentType(contentType3));
    }

    @Test
    public void testValidateCharset() {
        String charset1 = "UTF-8";
        String charset2 = "unicode";
        String charset3 = "UTF-16";
        String charset4 = "UTF-32";
        String charset5 = "UTF-32BE";
        String charset6 = "UTF-32LE";
        String charset7 = "UTF-32BE-FOO";

        assertEquals("UTF-8", DataUtil.validateCharset(charset1));
        assertEquals("UTF-16", DataUtil.validateCharset(charset3));
        assertEquals("UTF-32", DataUtil.validateCharset(charset4));
        assertEquals("UTF-32BE", DataUtil.validateCharset(charset5));
        assertEquals("UTF-32LE", DataUtil.validateCharset(charset6));
        assertEquals(null, DataUtil.validateCharset(charset2));
        assertEquals(null, DataUtil.validateCharset(charset7));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();

        assertTrue(boundary.length() >= boundary.length() / 2);
        for (int i = 0; i < boundary.length(); i++) {
            assertTrue(Character.isAlphabetic(boundary.charAt(i)) || boundary.charAt(i) == '-');
        }
    }

    @Test
    public void testDetectCharsetFromBom() {
        byte[] bomUtf8 = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        byte[] bomUtf16 = {(byte) 0xFE, (byte) 0xFF};
        byte[] bomUtf32BE = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF, (byte) 0x00, (byte) 0x00};
        byte[] bomUtf32LE = {(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};

        ByteBuffer byteBufferUtf8 = ByteBuffer.wrap(bomUtf8);
        ByteBuffer byteBufferUtf16 = ByteBuffer.wrap(bomUtf16);
        ByteBuffer byteBufferUtf32BE = ByteBuffer.wrap(bomUtf32BE);
        ByteBuffer byteBufferUtf32LE = ByteBuffer.wrap(bomUtf32LE);

        BomCharset expectedUtf8 = new BomCharset("UTF-8", true);
        BomCharset expectedUtf16 = new BomCharset("UTF-16", false);
        BomCharset expectedUtf32BE = new BomCharset("UTF-32BE", false);
        BomCharset expectedUtf32LE = new BomCharset("UTF-32LE", false);

        BomCharset actualUtf8 = DataUtil.detectCharsetFromBom(byteBufferUtf8);
        BomCharset actualUtf16 = DataUtil.detectCharsetFromBom(byteBufferUtf16);
        BomCharset actualUtf32BE = DataUtil.detectCharsetFromBom(byteBufferUtf32BE);
        BomCharset actualUtf32LE = DataUtil.detectCharsetFromBom(byteBufferUtf32LE);

        assertEquals(expectedUtf8, actualUtf8);
        assertEquals(expectedUtf16, actualUtf16);
        assertEquals(expectedUtf32BE, actualUtf32BE);
        assertEquals(expectedUtf32LE, actualUtf32LE);
    }

    private void writeFile(String filePath, String content) throws IOException {
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            outputStream.write(content.getBytes(StandardCharsets.UTF_8));
        }
    }
}