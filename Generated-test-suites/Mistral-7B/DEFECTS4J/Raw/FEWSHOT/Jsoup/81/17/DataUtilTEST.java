package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.jsoup.Document;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        String fileContent = "<html><body><h1>Test</h1></body></html>";
        File testFile = new File("test.html");
        Files.write(testFile.getAbsoluteFile().toPath(), fileContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(testFile, "UTF-8", null);
        assertNotNull(document);
        assertEquals("<html><body><h1>Test</h1></body></html>", document.html());
        Files.delete(testFile.getAbsoluteFile().toPath());
    }

    @Test
    public void testLoadDocumentFromInputStream() throws IOException {
        String inputStreamContent = "<html><body><h1>Test</h1></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(testInputStream, "UTF-8", null);
        assertNotNull(document);
        assertEquals("<html><body><h1>Test</h1></body></html>", document.html());
    }

    @Test
    public void testLoadDocumentFromInputStreamWithParser() throws IOException {
        String inputStreamContent = "<html><body><h1>Test</h1></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(testInputStream, "UTF-8", null, DataUtil.Parser.xmlParser());
        assertNotNull(document);
        assertEquals("<html><body><h1>Test</h1></body></html>", document.xml().body().html());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputContent = "Hello, World!";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        String outputContent = outputStream.toString(StandardCharsets.UTF_8.name());

        assertEquals(inputContent, outputContent);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String inputStreamContent = "<html><body><h1>Test</h1></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        String charset = "UTF-8";

        Document document = DataUtil.parseInputStream(testInputStream, charset, null, DataUtil.Parser.htmlParser());
        assertNotNull(document);
        assertEquals("<html><body><h1>Test</h1></body></html>", document.html());
    }

    @Test
    public void testParseInputStreamWithMetaCharset() throws IOException {
        String inputStreamContent = "<html meta charset=\"ISO-8859-1\"><body><h1>Test</h1></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        String charset = null;

        Document document = DataUtil.parseInputStream(testInputStream, charset, null, DataUtil.Parser.htmlParser());
        assertNotNull(document);
        assertEquals("<html><body><h1>Test</h1></body></html>", document.html());
        assertEquals("ISO-8859-1", document.outputSettings().charset());
    }

    @Test
    public void testParseInputStreamWithCharsetFromContentType() throws IOException {
        String inputStreamContent = "text/html; charset=ISO-8859-1\r\n<html><body><h1>Test</h1></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        String charset = null;

        Document document = DataUtil.parseInputStream(testInputStream, charset, null, DataUtil.Parser.htmlParser());
        assertNotNull(document);
        assertEquals("<html><body><h1>Test</h1></body></html>", document.html());
        assertEquals("ISO-8859-1", document.outputSettings().charset());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputStreamContent = "Test";
        InputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInputStream, 0);
        assertEquals(inputStreamContent.getBytes(StandardCharsets.UTF_8).length, byteBuffer.remaining());
        byteBuffer.flip();
        byte[] expected = inputStreamContent.getBytes(StandardCharsets.UTF_8);
        byte[] buffer = new byte[expected.length];

        byteBuffer.get(buffer);
        assertEquals(expected, buffer);
    }

    @Test
    public void testReadToByteBufferWithInterruptedException() throws IOException, InterruptedException {
        String inputStreamContent = "Test";
        InputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        Random random = new Random();

        Thread thread = new Thread(() -> {
            DataUtil.readToByteBuffer(testInputStream, 0);
        });
        thread.start();
        thread.interrupt();

        assertThrows(InterruptedIOException.class, () -> DataUtil.readToByteBuffer(testInputStream, 0));
    }

    @Test
    public void testDetectCharsetFromBom() {
        byte[] bomUtf32 = {0x00, 0x00, 0xFE, 0xFF};
        byte[] bomUtf16BE = {0xFE, 0xFF, 0x00, 0x00};
        byte[] bomUtf8 = {0xEF, 0xBB, 0xBF};

        assertEquals(DataUtil.BomCharset.UTF_32, DataUtil.detectCharsetFromBom(ByteBuffer.wrap(bomUtf32)));
        assertEquals(DataUtil.BomCharset.UTF_16, DataUtil.detectCharsetFromBom(ByteBuffer.wrap(bomUtf16BE)));
        assertEquals(DataUtil.BomCharset.UTF_8, DataUtil.detectCharsetFromBom(ByteBuffer.wrap(bomUtf8)));
        assertNull(DataUtil.detectCharsetFromBom(ByteBuffer.wrap(new byte[4])));
    }

    @Test
    public void testMimeBoundary() {
        assertEquals("_w5r3t6y7u8i9o1p2a3c4e5d6f7g8h9j0k1l2m3n4o5p6q7r8s9t0u1v2w3x4y5z6a7b8c9d0e", DataUtil.mimeBoundary());
    }
}