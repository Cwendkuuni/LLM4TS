package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class DataUtilTest {
    @Test
    public void testLoadDocumentFromInputStream() throws IOException {
        String xmlContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, "UTF-8", null);

        assertEquals("UTF-8", document.outputSettings().charset());
        assertNotNull(document.body());
    }

    @Test
    public void testDetectCharsetFromInputStream() throws IOException {
        String xmlContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());

        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testDetectCharsetFromInputStreamWithDefaultCharset() throws IOException {
        String xmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());

        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testDetectCharsetFromInputStreamWithInvalidCharset() throws IOException {
        String xmlContent = "<html><head><meta charset=\"XYZ\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());

        assertEquals("UTF-8", document.outputSettings().charset()); // default charset is UTF-8
    }

    @Test
    public void testDetectCharsetFromInputStreamWithBOM() throws IOException {
        String utf16LEContent = "\uFEFF<html><head><meta charset=\"UTF-16\"></head><body></body></html>";
        String utf16BEContent = "<html><head><meta charset=\"UTF-16\"></head><body></body></html>\uFFFE\uFEFF";
        String utf32LEContent = "\uFEFF\uFEFF\uFEFF<html><head><meta charset=\"UTF-32\"></head><body></body></html>";
        String utf8BOMContent = "\uEFFFB<html><head><meta charset=\"UTF-8\"></head><body></body></html>";

        ByteBuffer utf16LEByteBuffer = ByteBuffer.wrap(utf16LEContent.getBytes(StandardCharsets.UTF_8));
        ByteBuffer utf16BEByteBuffer = ByteBuffer.wrap(utf16BEContent.getBytes(StandardCharsets.UTF_8));
        ByteBuffer utf32LEByteBuffer = ByteBuffer.wrap(utf32LEContent.getBytes(StandardCharsets.UTF_8));
        ByteBuffer utf8BOMByteBuffer = ByteBuffer.wrap(utf8BOMContent.getBytes(StandardCharsets.UTF_8));

        Document utf16LEDocument = DataUtil.parseInputStream(new ByteArrayInputStream(utf16LEByteBuffer.array()), null, null, Parser.htmlParser());
        Document utf16BEDocument = DataUtil.parseInputStream(new ByteArrayInputStream(utf16BEByteBuffer.array()), null, null, Parser.htmlParser());
        Document utf32LEDocument = DataUtil.parseInputStream(new ByteArrayInputStream(utf32LEByteBuffer.array()), null, null, Parser.htmlParser());
        Document utf8BODDocument = DataUtil.parseInputStream(new ByteArrayInputStream(utf8BOMByteBuffer.array()), null, null, Parser.htmlParser());

        assertEquals("UTF-16", utf16LEDocument.outputSettings().charset());
        assertEquals("UTF-16", utf16BEDocument.outputSettings().charset());
        assertEquals("UTF-32", utf32LEDocument.outputSettings().charset());
        assertEquals("UTF-8", utf8BODDocument.outputSettings().charset());
    }

    @Test
    public void testDetectCharsetFromInputStreamWithNoMetaData() throws IOException {
        String xmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());

        assertEquals("UTF-8", document.outputSettings().charset()); // default charset is UTF-8
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        byte[] data = "DataUtil Test".getBytes(StandardCharsets.UTF_8);

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new ByteArrayInputStream(data), 0);
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(data);

        assertEquals(expectedByteBuffer, byteBuffer);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        byte[] data = "DataUtil Test".getBytes(StandardCharsets.UTF_8);

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new ByteArrayInputStream(data), data.length);
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(data);

        assertEquals(expectedByteBuffer, byteBuffer);
    }

    @Test
    public void testReadToByteBufferWithLimitedSize() throws IOException {
        byte[] data = "DataUtil Test".getBytes(StandardCharsets.UTF_8);

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new ByteArrayInputStream(data), data.length / 2);

        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(new byte[data.length / 2]);
        expectedByteBuffer.put(data, 0, data.length / 2);

        byte[] readData = new byte[data.length / 2];
        byteBuffer.get(readData, 0, data.length / 2);

        assertArrayEquals(expectedByteBuffer.array(), readData);
    }

    @Test
    public void testMimeBoundary() {
        String expectedBoundary = "---abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890";
        String generatedBoundary = DataUtil.mimeBoundary();

        assertEquals(expectedBoundary, generatedBoundary);
    }
}