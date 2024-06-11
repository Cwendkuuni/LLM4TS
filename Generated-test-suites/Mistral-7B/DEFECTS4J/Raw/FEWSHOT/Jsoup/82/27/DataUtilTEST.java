package org.jsoup.helper;

import static org.junit.Assert.*;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testParseInputStream() throws IOException {
        String xmlData = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        ByteArrayInputStream xmlInputStream = new ByteArrayInputStream(xmlData.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(xmlInputStream, "UTF-8", null, Parser.htmlParser());

        assertEquals("UTF-8", document.outputSettings().charset().toString());
        assertNotNull(document.head());
        assertEquals("UTF-8", document.head().outputSettings().charset().toString());
        assertEquals("Hello World", document.body().select("h1").text());

        String htmlData = "<html><body><h1>Hello World</h1></body></html>";
        ByteArrayInputStream htmlInputStream = new ByteArrayInputStream(htmlData.getBytes(StandardCharsets.UTF_8));
        Document htmlDocument = DataUtil.parseInputStream(htmlInputStream, null, null, Parser.htmlParser());

        assertEquals("UTF-8", htmlDocument.outputSettings().charset().toString());
        assertNull(htmlDocument.head());
        assertEquals("Hello World", htmlDocument.body().select("h1").text());

        String charsetData = "Content-Type: text/html; charset=UTF-8\n";
        ByteArrayInputStream charsetInputStream = new ByteArrayInputStream(charsetData.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new BufferedInputStream(new SequenceInputStream(charsetInputStream, new ByteArrayInputStream(xmlData.getBytes(StandardCharsets.UTF_8))));
        Document charsetDocument = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());

        assertEquals("UTF-8", charsetDocument.outputSettings().charset().toString());
        assertEquals(xmlData, charsetDocument.toString());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "application/xml; charset=ISO-8859-1";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("ISO-8859-1", charset);

        contentType = "text/plain";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);

        contentType = "text/html; charset=invalid-charset";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String utf8 = "UTF-8";
        String iso8859_1 = "ISO-8859-1";
        String invalidCharset = "invalid-charset";

        assertEquals(utf8, DataUtil.validateCharset(utf8));
        assertEquals(iso8859_1, DataUtil.validateCharset(iso8859_1));
        assertNull(DataUtil.validateCharset(null));
        assertNull(DataUtil.validateCharset(""));
        assertNull(DataUtil.validateCharset(invalidCharset));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertFalse(boundary.isEmpty());
    }

    @Test
    public void testDetectCharsetFromBom() {
        String utf8Content = "EF BBBF"; // UTF-8 BOM
        String utf16LEContent = "FE FF"; // UTF-16 LE BOM
        String utf16BEContent = "FE FF"; // UTF-16 BE BOM
        String utf32LEContent = "00 00 FE FF"; // UTF-32 LE BOM
        String utf32BEContent = "FE FF 00 00"; // UTF-32 BE BOM

        ByteBuffer utf8ByteBuffer = ByteBuffer.wrap(utf8Content.getBytes(StandardCharsets.UTF_8));
        BomCharset utf8BomCharset = DataUtil.detectCharsetFromBom(utf8ByteBuffer);
        assertEquals("UTF-8", utf8BomCharset.charset);
        assertFalse(utf8BomCharset.offset);

        ByteBuffer utf16LEByteBuffer = ByteBuffer.wrap(utf16LEContent.getBytes(StandardCharsets.UTF_8));
        BomCharset utf16LEBomCharset = DataUtil.detectCharsetFromBom(utf16LEByteBuffer);
        assertEquals("UTF-16", utf16LEBomCharset.charset);
        assertFalse(utf16LEBomCharset.offset);

        ByteBuffer utf16BEByteBuffer = ByteBuffer.wrap(utf16BEContent.getBytes(StandardCharsets.UTF_8));
        BomCharset utf16BEBomCharset = DataUtil.detectCharsetFromBom(utf16BEByteBuffer);
        assertEquals("UTF-16", utf16BEBomCharset.charset);
        assertTrue(utf16BEBomCharset.offset);

        ByteBuffer utf32LEByteBuffer = ByteBuffer.wrap(utf32LEContent.getBytes(StandardCharsets.UTF_8));
        BomCharset utf32LEBomCharset = DataUtil.detectCharsetFromBom(utf32LEByteBuffer);
        assertEquals("UTF-32", utf32LEBomCharset.charset);
        assertFalse(utf32LEBomCharset.offset);

        ByteBuffer utf32BEByteBuffer = ByteBuffer.wrap(utf32BEContent.getBytes(StandardCharsets.UTF_8));
        BomCharset utf32BEBomCharset = DataUtil.detectCharsetFromBom(utf32BEByteBuffer);
        assertEquals("UTF-32", utf32BEBomCharset.charset);
        assertTrue(utf32BEBomCharset.offset);

        ByteBuffer emptyByteBuffer = DataUtil.emptyByteBuffer();
        assertNull(DataUtil.detectCharsetFromBom(emptyByteBuffer));
    }
}

class SequenceInputStream extends InputStream {
    InputStream is1;
    InputStream is2;

    public SequenceInputStream(InputStream is1, InputStream is2) {
        this.is1 = is1;
        this.is2 = is2;
    }

    @Override
    public int read() throws IOException {
        int b = is1.read();
        if (b == -1)
            return is2.read();
        return b;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int bytesRead = is1.read(b, off, len);
        if (bytesRead < len) {
            return is2.read(b, off + bytesRead, len - bytesRead);
        }
        return bytesRead;
    }

    @Override
    public long skip(long n) throws IOException {
        return is1.skip(n) + is2.skip(n - is1.skip(n));
    }

    @Override
    public int available() throws IOException {
        return is1.available() + is2.available();
    }

    @Override
    public void close() throws IOException {
        is1.close();
        is2.close();
    }
}