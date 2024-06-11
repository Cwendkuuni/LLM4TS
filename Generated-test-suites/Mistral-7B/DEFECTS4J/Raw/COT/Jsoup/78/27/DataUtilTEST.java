package org.jsoup.helper;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    private Document defaultDocument;
    private Document charsetDocument;
    private Document xmlDeclarationDocument;
    private Document emptyDocument;
    private Document errorDocument;

    @Before
    public void setup() throws IOException {
        String htmlString = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root/>";
        defaultDocument = DataUtil.parseInputStream(new ByteArrayInputStream(htmlString.getBytes()), "UTF-8", null, Parser.htmlParser());
        charsetDocument = DataUtil.parseInputStream(new ByteArrayInputStream(htmlString.getBytes()), null, null, Parser.htmlParser());
        xmlDeclarationDocument = DataUtil.parseInputStream(new ByteArrayInputStream(xmlString.getBytes()), "UTF-8", null, Parser.xmlParser());
        emptyDocument = DataUtil.parseInputStream(new ByteArrayInputStream(new byte[0]), "UTF-8", null, Parser.htmlParser());
        errorDocument = new Document();
    }

    @Test
    public void testLoadFile() throws IOException {
        String fileName = "test.html";
        File file = new File(fileName);
        Document document = DataUtil.load(file, "UTF-8", null);
        assertNotNull(document);
        assertEquals(Charset.forName("UTF-8"), document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlString = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlString.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(document);
        assertEquals(Charset.forName("UTF-8"), document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String htmlString = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlString.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
        assertNotNull(document);
        assertEquals(Charset.forName("UTF-8"), document.outputSettings().charset());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String htmlString = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlString.getBytes());
        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals(Charset.forName("UTF-8"), document.outputSettings().charset());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String source = "Hello, World!";
        InputStream inputStream = new ByteArrayInputStream(source.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals(source, outputStream.toString());
    }

    @Test
    public void testLoadWithInvalidCharset() throws IOException {
        String htmlString = "<html><head><meta charset=\"INVALID_CHARSET\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlString.getBytes());
        Document document = null;
        try {
            document = DataUtil.load(inputStream, "INVALID_CHARSET", null);
        } catch (IOException e) {
            // Expected
        }
        assertNull(document);
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String htmlString = "<html><head><meta charset=\"INVALID_CHARSET\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlString.getBytes());
        Document document = null;
        try {
            document = DataUtil.parseInputStream(inputStream, "INVALID_CHARSET", null, Parser.htmlParser());
        } catch (IOException e) {
            // Expected
        }
        assertNull(document);
    }

    @Test
    public void testParseInputStreamWithEmptyContent() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        assertEquals(emptyDocument, document);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithError() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String htmlString = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlString.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertEquals(htmlString, Charset.forName("UTF-8").decode(byteBuffer).toString());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String htmlString = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlString.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024 * 32);
        assertEquals(htmlString, Charset.forName("UTF-8").decode(byteBuffer).toString());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String fileName = "test.html";
        File file = new File(fileName);
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        assertEquals(Charset.forName("UTF-8").decode(byteBuffer).toString(), new String(Files.readAllBytes(file.toPath()), Charset.forName("UTF-8")));
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=utf-8"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8;"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8; foo"));
        assertEquals(null, DataUtil.getCharsetFromContentType("text/html"));
    }

    @Test
    public void testValidateCharset() {
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8"));
        assertEquals("UTF-8", DataUtil.validateCharset("utf-8"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8;"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8; foo"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8\r\n"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8\r"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8\n"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8; charset=UTF-8"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8; charset=UTF-8; charset=UTF-8"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8; charset=UTF-8; charset=UTF-8; charset=UTF-8"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8; charset=UTF-8; charset=UTF-8; charset=UTF-8; charset=UTF-8"));
        assertEquals(null, DataUtil.validateCharset("InvalidCharset"));
        assertEquals(null, DataUtil.validateCharset("invalidcharset"));
        assertEquals(null, DataUtil.validateCharset("UTF-8\r\nInvalidCharset"));
        assertEquals(null, DataUtil.validateCharset("UTF-8\rInvalidCharset"));
        assertEquals(null, DataUtil.validateCharset("UTF-8\nInvalidCharset"));
        assertEquals(null, DataUtil.validateCharset("UTF-8; charset=InvalidCharset"));
    }

    @Test
    public void testMimeBoundary() {
        assertEquals("--88e5276b32514e57334e6c707b5e2a32", DataUtil.mimeBoundary());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.put((byte) 0x00).put((byte) 0x00).put((byte) 0xFE).put((byte) 0xFF);
        byteBuffer.flip();
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertEquals("UTF-32", bomCharset.charset);
        assertEquals(0, bomCharset.offset);

        byteBuffer.clear();
        byteBuffer.put((byte) 0xFF).put((byte) 0xFE).put((byte) 0x00).put((byte) 0x00);
        byteBuffer.flip();
        bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertEquals("UTF-16", bomCharset.charset);
        assertEquals(0, bomCharset.offset);

        byteBuffer.clear();
        byteBuffer.put((byte) 0xEF).put((byte) 0xBB).put((byte) 0xBF);
        byteBuffer.flip();
        bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertEquals("UTF-8", bomCharset.charset);
        assertEquals(3, bomCharset.offset);

        byteBuffer.clear();
        bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertNull(bomCharset);
    }
}

class BomCharset {
    private final String charset;
    private final int offset;

    public BomCharset(String charset, int offset) {
        this.charset = charset;
        this.offset = offset;
    }
}