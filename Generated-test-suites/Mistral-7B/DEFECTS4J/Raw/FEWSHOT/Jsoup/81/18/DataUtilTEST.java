package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"><title>Test</title></head><body><p>Hello World</p></body></html>";
        byte[] htmlBytes = html.getBytes("UTF-8");

        InputStream inputStream = new ByteArrayInputStream(htmlBytes);
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals("UTF-8", doc.metaCharset());
        assertEquals("Test", doc.title());
        assertEquals("Hello World", doc.body().text());

        inputStream = new ByteArrayInputStream(new byte[0]);
        doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertNull(doc);

        inputStream = new ByteArrayInputStream(new byte[1]);
        doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertNull(doc);

        inputStream = new ByteArrayInputStream(new byte[]{"x".getBytes("UTF-8")});
        doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        assertNull(doc);

        inputStream = new ByteArrayInputStream(new byte[]{0x00, 0x00, 0xEE, 0xFF, 0x00, 0x00, 0x47, 0x45, 0x54, 0x54, 0x46, 0x2D, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30, 0x30, 0x31, 0x30, 0x32, 0x31, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x2D, 0x33, 0x32, 0x30, 0x31, 0x31, 0x39, 0x32, 0x30, 0x38, 0x31, 0x34, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x2E, 0x33, 0x32, 0x2D, 0x65, 0x6E, 0x63, 0x74, 0x61, 0x74, 0x75, 0x72, 0x65, 0x31, 0x30, 0x31, 0x32, 0x31, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x31, 0x33, 0x31, 0x30, 0x31, 0x34, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30, 0x32, 0x31, 0x33, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x32, 0x33, 0x33});
        doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertEquals("UTF-16BE", doc.outputSettings().charset());
        assertEquals("UTF-16", doc.metaCharset());
        assertEquals("", doc.title());
        assertEquals("", doc.body().text());

        inputStream = new ByteArrayInputStream(new byte[]{0xFF, 0xFE, 0x00, 0x00, 0x47, 0x45, 0x54, 0x54, 0x46, 0x2D, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30, 0x30, 0x31, 0x30, 0x32, 0x31, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x2D, 0x33, 0x32, 0x30, 0x31, 0x31, 0x39, 0x32, 0x30, 0x38, 0x31, 0x34, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x2E, 0x33, 0x32, 0x2D, 0x65, 0x6E, 0x63, 0x74, 0x61, 0x74, 0x75, 0x72, 0x65, 0x31, 0x30, 0x31, 0x32, 0x31, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x31, 0x33, 0x31, 0x30, 0x31, 0x34, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30, 0x32, 0x31, 0x33, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x32, 0x33, 0x33});
        doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertEquals("UTF-16LE", doc.outputSettings().charset());
        assertEquals("UTF-16", doc.metaCharset());
        assertEquals("", doc.title());
        assertEquals("", doc.body().text());

        // Test with an empty byte array
        inputStream = new ByteArrayInputStream(new byte[0]);
        doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"><title>Test</title></head><body><p>Hello World</p></body></html>";
        byte[] htmlBytes = html.getBytes("UTF-8");

        InputStream inputStream = new ByteArrayInputStream(htmlBytes);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertEquals(htmlBytes.length, byteBuffer.limit());
        assertEquals(htmlBytes[0], byteBuffer.get(0));
        assertEquals(htmlBytes[htmlBytes.length - 1], byteBuffer.get(byteBuffer.limit() - 1));

        byteBuffer.clear();
        assertEquals(0, byteBuffer.limit());
        assertEquals(0, byteBuffer.position());
        assertEquals(-1, byteBuffer.remaining());

        byteBuffer.put(htmlBytes);
        byteBuffer.flip();
        assertEquals(htmlBytes.length, byteBuffer.limit());
        assertEquals(htmlBytes[0], byteBuffer.get());
        assertEquals(htmlBytes[htmlBytes.length - 1], byteBuffer.get(byteBuffer.limit() - 1));
        assertEquals(htmlBytes, byteBuffer.array());

        byteBuffer.compact();
        assertEquals(htmlBytes.length, byteBuffer.limit());
        assertEquals(htmlBytes.length, byteBuffer.position());
        assertEquals(0, byteBuffer.remaining());
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertEquals(boundary.length(), 32);
        for (int i = 0; i < boundary.length(); i++) {
            char c = boundary.charAt(i);
            assertTrue(Character.isDefined(c));
            assertTrue(Character.isUpperCase(Character.isDigit(c) || Character.isLetter(c)));
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=ISO-8859-1";
        assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType(contentType));

        contentType = "application/xml; charset=UTF-16";
        assertEquals("UTF-16", DataUtil.getCharsetFromContentType(contentType));

        contentType = "application/xml";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testValidateCharset() {
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8"));
        assertEquals("UTF-16", DataUtil.validateCharset("UTF-16"));
        assertEquals("ISO-8859-1", DataUtil.validateCharset("ISO-8859-1"));
        assertEquals("GBK", DataUtil.validateCharset("GBK"));
        assertEquals("US-ASCII", DataUtil.validateCharset("US-ASCII"));
        assertEquals("UTF-32", DataUtil.validateCharset("UTF-32"));
        assertEquals("UTF-32BE", DataUtil.validateCharset("UTF-32BE"));
        assertEquals("UTF-32LE", DataUtil.validateCharset("UTF-32LE"));
        assertEquals(null, DataUtil.validateCharset("unknown"));
        assertEquals(null, DataUtil.validateCharset("UTF-8 "));
        assertEquals(null, DataUtil.validateCharset(" UTF-8 "));
        assertEquals(null, DataUtil.validateCharset("UTF-8\r\n"));
        assertEquals(null, DataUtil.validateCharset("\r\nUTF-8"));
        assertEquals(null, DataUtil.validateCharset("UTF-8\r\n\r\n"));
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer utf8Bom = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
        assertEquals("UTF-8", DataUtil.detectCharsetFromBom(utf8Bom).charset);
        assertEquals(true, DataUtil.detectCharsetFromBom(utf8Bom).offset);

        ByteBuffer utf16Lebom = ByteBuffer.wrap(new byte[]{0xFF, 0xFE, 0x00, 0x00});
        assertEquals("UTF-16LE", DataUtil.detectCharsetFromBom(utf16Lebom).charset);
        assertEquals(false, DataUtil.detectCharsetFromBom(utf16Lebom).offset);

        ByteBuffer utf16Bebom = ByteBuffer.wrap(new byte[]{0x00, 0x00, 0xFE, 0xFF});
        assertEquals("UTF-16BE", DataUtil.detectCharsetFromBom(utf16Bebom).charset);
        assertEquals(false, DataUtil.detectCharsetFromBom(utf16Bebom).offset);

        ByteBuffer utf32Lebom = ByteBuffer.wrap(new byte[]{0xFF, 0xFF, 0xFF, 0xFF, 0x00, 0x00, 0x00, 0x00});
        assertEquals("UTF-32LE", DataUtil.detectCharsetFromBom(utf32Lebom).charset);
        assertEquals(false, DataUtil.detectCharsetFromBom(utf32Lebom).offset);

        ByteBuffer utf32Bebom = ByteBuffer.wrap(new byte[]{0x00, 0x00, 0x00, 0x00, 0xFF, 0xFF, 0xFF, 0xFF});
        assertEquals("UTF-32BE", DataUtil.detectCharsetFromBom(utf32Bebom).charset);
        assertEquals(false, DataUtil.detectCharsetFromBom(utf32Bebom).offset);

        ByteBuffer noneBom = ByteBuffer.wrap(new byte[0]);
        assertNull(DataUtil.detectCharsetFromBom(noneBom));

        ByteBuffer invalidBom = ByteBuffer.wrap(new byte[]{0x01, 0x01, 0x01, 0x01});
        assertNull(DataUtil.detectCharsetFromBom(invalidBom));
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