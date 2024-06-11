package org.jsoup.helper;

import static org.junit.Assert.*;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadDocument() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"/></head><body></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(input, "UTF-8", null);
        assertNotNull(document);
        assertEquals("<html><head><meta charset=\"UTF-8\"/></head><body></body></html>", document.html());
    }

    @Test
    public void testLoadDocumentWithInvalidCharset() throws IOException {
        String html = "<html><head><meta charset=\"InvalidCharset\"/></head><body></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(input, "InvalidCharset", null);
        assertNotNull(document);
        assertEquals("<html><head><meta charset=\"UTF-8\"/></head><body></body></html>", document.html());
    }

    @Test
    public void testLoadDocumentWithXmlDeclaration() throws IOException {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html><body></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(input, null, null);
        assertNotNull(document);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><html><body></body></html>", document.html());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"/></head><body></body></html>";
        ByteArrayInputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(input, "UTF-8", null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("<html><head><meta charset=\"UTF-8\"/></head><body></body></html>", document.html());
    }

    @Test
    public void testParseInputStreamWithXmlDeclaration() throws IOException {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html><body></body></html>";
        ByteArrayInputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(input, null, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><html><body></body></html>", document.html());
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"/></head><body></body></html>";
        ByteArrayInputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(input, "UTF-8", null, Parser.xmlParser());
        assertNotNull(document);
        assertEquals("<html><head><meta charset=\"UTF-8\"/></head><body></body></html>", document.html());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String html = "<html><head><meta charset=\"InvalidCharset\"/></head><body></body></html>";
        ByteArrayInputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(input, "InvalidCharset", null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("<html><head><meta charset=\"UTF-8\"/></head><body></body></html>", document.html());
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        byte[] bomUtf8 = {0xEF, 0xBB, 0xBF};
        byte[] bomUtf16BE = {0xFF, 0xFE, 0x00, 0x00};
        byte[] bomUtf16LE = {0x00, 0x00, 0xFF, 0xFE};
        byte[] bomUtf32BE = {0x00, 0x00, 0x00, 0x00, 0xFF, 0xFF, 0xFE, 0xFE};

        ByteBuffer byteDataUtf8 = ByteBuffer.wrap(bomUtf8);
        assertEquals("UTF-8", DataUtil.detectCharsetFromBom(byteDataUtf8).charset);

        ByteBuffer byteDataUtf16BE = ByteBuffer.wrap(bomUtf16BE);
        assertEquals("UTF-16", DataUtil.detectCharsetFromBom(byteDataUtf16BE).charset);

        ByteBuffer byteDataUtf16LE = ByteBuffer.wrap(bomUtf16LE);
        assertEquals("UTF-16", DataUtil.detectCharsetFromBom(byteDataUtf16LE).charset);

        ByteBuffer byteDataUtf32BE = ByteBuffer.wrap(bomUtf32BE);
        assertEquals("UTF-32", DataUtil.detectCharsetFromBom(byteDataUtf32BE).charset);

        ByteBuffer byteDataUnknown = ByteBuffer.wrap(new byte[4]);
        assertNull(DataUtil.detectCharsetFromBom(byteDataUnknown));
    }
}