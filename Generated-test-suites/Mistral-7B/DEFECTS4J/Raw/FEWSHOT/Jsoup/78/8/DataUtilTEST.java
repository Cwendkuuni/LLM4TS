package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoad() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File inFile = new File("test.html");

        Document doc = DataUtil.load(inFile, charsetName, baseUri);
        assertEquals("Expected document to not be null", doc, DataUtil.load(inFile, charsetName, baseUri));
        assertEquals("Expected baseUri to be set", baseUri, doc.baseUri());

        inFile = new File("test_empty.html");
        doc = DataUtil.load(inFile, charsetName, baseUri);
        assertEquals("Expected document to not be null for empty file", doc, DataUtil.load(inFile, charsetName, baseUri));
        assertNull("Expected document body to be empty", doc.body());

        inFile = new File("test_non_existent.html");
        assertThrows("Expected IOException for non-existent file", IOException.class, () -> DataUtil.load(inFile, charsetName, baseUri));
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = new FileInputStream(new File("test.html"));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertEquals("Expected document to not be null", doc, DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser()));
        assertEquals("Expected baseUri to be set", baseUri, doc.baseUri());

        inputStream.close();

        charsetName = null;
        doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertEquals("Expected document to not be null with default charset", doc, DataUtil.parseInputStream(inputStream, null, baseUri, Parser.htmlParser()));
        assertEquals("Expected baseUri to be set", baseUri, doc.baseUri());
        inputStream.close();

        inputStream = new FileInputStream(new File("test_empty.html"));
        doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNull("Expected document body to be empty for empty input stream", doc.body());
        inputStream.close();

        charsetName = "InvalidCharset";
        assertThrows("Expected IOException for unsupported charset", IOException.class, () -> DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser()));
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithXmlParser() throws IOException {
        InputStream inputStream = new FileInputStream(new File("test.xml"));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.xmlParser());
        assertEquals("Expected document to not be null", doc, DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.xmlParser()));
        assertEquals("Expected baseUri to be set", baseUri, doc.baseUri());

        inputStream.close();

        charsetName = null;
        doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.xmlParser());
        assertEquals("Expected document to not be null with default charset", doc, DataUtil.parseInputStream(inputStream, null, baseUri, Parser.xmlParser()));
        assertEquals("Expected baseUri to be set", baseUri, doc.baseUri());
        inputStream.close();

        inputStream = new FileInputStream(new File("test_empty.xml"));
        doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.xmlParser());
        assertNull("Expected document body to be empty for empty input stream", doc.body());
        inputStream.close();

        charsetName = "InvalidCharset";
        assertThrows("Expected IOException for unsupported charset", IOException.class, () -> DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.xmlParser()));
        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("test.html");
        File outputFile = new File("test_output.html");
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);
        assertFalse("Expected input stream not to be closed", inputStream.isClosed());
        assertFalse("Expected output stream not to be closed", outputStream.isClosed());

        InputStream in = new ByteArrayInputStream("test".getBytes());
        OutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out);
        assertFalse("Expected input stream not to be closed", in.isClosed());
        assertFalse("Expected output stream not to be closed", out.isClosed());

        in = new ByteArrayInputStream("test".getBytes());
        out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out, 1024 * 64);
        assertFalse("Expected input stream not to be closed", in.isClosed());
        assertFalse("Expected output stream not to be closed", out.isClosed());

        inputFile.delete();
        outputFile.delete();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        File inputFile = new File("test.html");
        InputStream inputStream = new FileInputStream(inputFile);

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertEquals("Expected byte buffer not to be null", byteBuffer, DataUtil.readToByteBuffer(inputStream));
        assertTrue("Expected byte buffer to have data", byteBuffer.hasRemaining());
        inputStream.close();

        byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024 * 64);
        assertTrue("Expected byte buffer to have capacity", byteBuffer.capacity() >= 1024 * 64);
        assertTrue("Expected byte buffer to have data", byteBuffer.hasRemaining());
        inputStream.close();

        byteBuffer = DataUtil.readToByteBuffer(new ByteArrayInputStream("test".getBytes()));
        assertEquals("Expected byte buffer to have data", "test".length(), byteBuffer.remaining());
        assertTrue("Expected byte buffer to have capacity", byteBuffer.capacity() >= "test".length());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        assertEquals("Expected byte buffer not to be null", byteBuffer, DataUtil.readFileToByteBuffer(file));
        assertTrue("Expected byte buffer to have data", byteBuffer.hasRemaining());
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertTrue("Expected byte buffer to be empty", byteBuffer.position() == 0);
        assertTrue("Expected byte buffer to have capacity 0", byteBuffer.capacity() == 0);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("Expected charset UTF-8", "UTF-8", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=gb2312";
        assertEquals("Expected charset gb2312", "gb2312", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull("Expected null", DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        assertEquals("Expected charset UTF-8", "UTF-8", DataUtil.validateCharset(charset));

        charset = "GB2312";
        assertEquals("Expected charset GB2312", "GB2312", DataUtil.validateCharset(charset));

        charset = "ISO-8859-1";
        assertEquals("Expected charset ISO-8859-1", "ISO-8859-1", DataUtil.validateCharset(charset));

        charset = "InvalidCharset";
        assertNull("Expected null for invalid charset", DataUtil.validateCharset(charset));
    }

    @Test
    public void testMimeBoundary() {
        assertEquals("Expected mime boundary to have expected length", DataUtil.mimeBoundary().length(), DataUtil.boundaryLength);
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new FileInputStream(new File("test_utf32.html")));
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertEquals("Expected charset UTF-32", "UTF-32", bomCharset.charset);
        assertEquals("Expected offset 0", 0, bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBom_InvalidBom() {
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new FileInputStream(new File("test.html")));
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertNull("Expected null for invalid BOM", bomCharset);
    }
}

class BomCharset {
    String charset;
    int offset;

    public BomCharset(String charset, int offset) {
        this.charset = charset;
        this.offset = offset;
    }
}