package org.jsoup.helper.test;

import org.jsoup.helper.DataUtil;
import org.jsoup.helper.BomCharset;
import org.junit.Test;
import org.junit.Assert;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, null, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamNullCharset() throws IOException {
        InputStream input = new FileInputStream(new File("test.html"));
        Document doc = DataUtil.load(input, null, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamValidCharset() throws IOException {
        InputStream input = new FileInputStream(new File("test.html"));
        Document doc = DataUtil.load(input, "UTF-8", null);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamInvalidCharset() throws IOException {
        InputStream input = new FileInputStream(new File("test.html"));
        Document doc = DataUtil.load(input, "INVALID_CHARSET", null);
        assertNull(doc);
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream input = new FileInputStream(new File("test.html"));
        Document doc = DataUtil.parseInputStream(input, null, null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamInvalidCharset() throws IOException {
        InputStream input = new FileInputStream(new File("test.html"));
        Document doc = DataUtil.parseInputStream(input, "INVALID_CHARSET", null, Parser.htmlParser());
        assertNull(doc);
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        InputStream input = new FileInputStream(new File("test.html"));
        Document doc = DataUtil.parseInputStream(input, null, null, Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidParser() throws IOException {
        InputStream input = new FileInputStream(new File("test.html"));
        Document doc = DataUtil.parseInputStream(input, null, null, new Parser() {});
        assertNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File file = new File("test.html");
        InputStream input = new FileInputStream(file);
        OutputStream output = new FileOutputStream("output.html");
        DataUtil.crossStreams(input, output);
        assert output.available() > 0;
    }

    @Test(expected = IOException.class)
    public void testCrossStreamsWithNullInput() throws IOException {
        OutputStream output = new FileOutputStream("output.html");
        DataUtil.crossStreams(null, output);
        assertFalse(output.available() > 0);
    }

    @Test(expected = IOException.class)
    public void testCrossStreamsWithNullOutput() throws IOException {
        InputStream input = new FileInputStream(new File("test.html"));
        DataUtil.crossStreams(input, null);
        assertFalse(input.available() > 0);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new FileInputStream(file));
        assertNotNull(byteBuffer);
    }

    @Test
    public void testReadToByteBufferWithMaxSizeZero() throws IOException {
        InputStream input = new FileInputStream(new File("test.html"));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(input);
        assertNotNull(byteBuffer);
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithInvalidMaxSize() throws IOException {
        InputStream input = new FileInputStream(new File("test.html"));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(input, -1);
        assertNull(byteBuffer);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        assertNotNull(byteBuffer);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertNotNull(byteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=EUC-JP";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("EUC-JP", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNullContentType() {
        String contentType = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String cs = "UTF-8";
        String validCharset = DataUtil.validateCharset(cs);
        assertEquals(cs, validCharset);
    }

    @Test
    public void testValidateCharsetNullCharset() {
        String cs = null;
        String validCharset = DataUtil.validateCharset(cs);
        assertNull(validCharset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotEmpty(boundary);
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new FileInputStream(file));
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertEquals("UTF-8", bomCharset.charset);
        assertEquals(false, bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomInvalidBom() throws IOException {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        byteBuffer.put((byte) 0x01);
        byteBuffer.flip();
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertNull(bomCharset);
    }
}