package org.jsoup.helper;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadDocumentFromInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals("UTF-8", document.outputSettings().charset());
        assertNotNull(document.title());
        assertEquals("Test", document.title().text());
    }

    @Test
    public void testDetectCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String detectedCharset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", detectedCharset);

        contentType = "text/html";
        detectedCharset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(detectedCharset);
    }

    @Test
    public void testDetectCharsetFromBom() {
        String utf8Bom = "\uFEFFUTF-8\n";
        ByteBuffer byteBuffer = ByteBuffer.wrap(utf8Bom.getBytes());
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertEquals("UTF-8", bomCharset.charset);
        assertTrue(bomCharset.offset);

        byteBuffer = ByteBuffer.wrap(new byte[]{0xFF, 0xFE, 0, 0});
        bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertEquals("UTF-16", bomCharset.charset);
        assertFalse(bomCharset.offset);

        byteBuffer = ByteBuffer.wrap(new byte[]{0xEF, 0xBB, 0xBF});
        bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertEquals("UTF-8", bomCharset.charset);
        assertFalse(bomCharset.offset);

        byteBuffer = ByteBuffer.wrap(new byte[0]);
        bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertNull(bomCharset);
    }
}