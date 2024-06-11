package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {
    private static final int BUFFER_SIZE = 1024 * 32;
    private static final int FIRST_READ_BUFFER_SIZE = 1024 * 5;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    @Test
    public void testLoadDocumentFromFileWithNullFile() {
        assertThrows(NullPointerException.class, () -> DataUtil.load(null, null, null));
    }

    @Test
    public void testLoadDocumentFromFileWithEmptyFile() {
        File file = new File("");
        Document doc = DataUtil.load(file, null, null);
        assertEquals(file.toURI(), doc.baseUri());
        assertTrue(doc.body().childNodeSize() == 0);
    }

    @Test
    public void testLoadDocumentFromFileWithDefaultCharset() throws IOException {
        File xmlFile = new File("src/test/resources/test.xml");
        Document doc = DataUtil.load(xmlFile, null, null);
        assertEquals(xmlFile.toURI().toString(), doc.baseUri().toString());
        assertEquals("<root><child>Hello</child></root>", doc.body().html());
    }

    @Test
    public void testLoadDocumentFromFileWithSpecifiedCharset() throws IOException {
        File xmlFile = new File("src/test/resources/test.xml");
        Document doc = DataUtil.load(xmlFile, "UTF-16LE", null);
        assertEquals(xmlFile.toURI().toString(), doc.baseUri().toString());
        assertEquals("<root><child>Hello</child></root>", doc.body().html());
    }

    @Test
    public void testLoadDocumentFromInputStreamWithNullInputStream() {
        assertThrows(NullPointerException.class, () -> DataUtil.load((InputStream) null, null, null));
    }

    @Test
    public void testLoadDocumentFromInputStreamWithEmptyStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Document doc = DataUtil.load(inputStream, null, null);
        assertTrue(doc.body().childNodeSize() == 0);
    }

    @Test
    public void testLoadDocumentFromInputStreamWithDefaultCharset() throws IOException {
        String xmlData = "<root><child>Hello</child></root>";
        InputStream inputStream = new ByteArrayInputStream(xmlData.getBytes(DEFAULT_CHARSET));
        Document doc = DataUtil.load(inputStream, null, null);
        assertEquals(xmlData, doc.body().html());
    }

    @Test
    public void testLoadDocumentFromInputStreamWithSpecifiedCharset() throws IOException {
        String xmlData = "<root><child>Hello</child></root>";
        InputStream inputStream = new ByteArrayInputStream(xmlData.getBytes("UTF-16LE"));
        Document doc = DataUtil.load(inputStream, "UTF-16LE", null);
        assertEquals(xmlData, doc.body().html());
    }

    @Test
    public void testLoadDocumentFromInputStreamWithParser() throws IOException {
        String xmlData = "<root><child>Hello</child></root>";
        InputStream inputStream = new ByteArrayInputStream(xmlData.getBytes(DEFAULT_CHARSET));
        Document doc = DataUtil.load(inputStream, null, null, DataUtil.Parser.xmlParser());
        assertEquals(xmlData, doc.body().html());
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("src/test/resources/test.xml");
        File outputFile = new File("src/test/resources/output.xml");

        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        Document docInput = DataUtil.load(inputFile, null, null);
        Document docOutput = DataUtil.load(outputFile, null, null);

        assertEquals(docInput.body().html(), docOutput.body().html());

        outputStream.close();
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithNullInputStream() {
        assertThrows(NullPointerException.class, () -> DataUtil.parseInputStream(null, null, null, null));
    }

    @Test
    public void testParseInputStreamWithFirstReadBufferSizeLessThanFourBytes() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[FIRST_READ_BUFFER_SIZE - 1]);
        ByteBuffer firstBytes = DataUtil.readToByteBuffer(inputStream, FIRST_READ_BUFFER_SIZE - 1);

        Document doc = DataUtil.parseInputStream(inputStream, null, null, null);
        assertNull(doc);
    }

    @Test
    public void testParseInputStreamWithFirstReadBufferSizeGreaterThanFourBytes() throws IOException {
        String xmlData = "<root><child>Hello</child></root>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlData.getBytes(DEFAULT_CHARSET));

        ByteBuffer firstBytes = DataUtil.readToByteBuffer(inputStream, FIRST_READ_BUFFER_SIZE);
        assertEquals(4, firstBytes.remaining());

        Document doc = DataUtil.parseInputStream(inputStream, null, null, null);
        assertEquals(xmlData, doc.body().html());
    }

    @Test
    public void testParseInputStreamWithEmptyInputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Document doc = DataUtil.parseInputStream(inputStream, null, null, null);
        assertTrue(doc.body().childNodeSize() == 0);
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        String xmlData = "<root><child>Hello</child></root>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlData.getBytes(DEFAULT_CHARSET));
        Document doc = DataUtil.parseInputStream(inputStream, null, null, null);
        assertEquals(xmlData, doc.body().html());
    }

    @Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException {
        String xmlData = "<root><child>Hello</child></root>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlData.getBytes("UTF-16LE"));
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-16LE", null, null);
        assertEquals(xmlData, doc.body().html());
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        String xmlData = "<root><child>Hello</child></root>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlData.getBytes(DEFAULT_CHARSET));
        Document doc = DataUtil.parseInputStream(inputStream, null, null, DataUtil.Parser.xmlParser());
        assertEquals(xmlData, doc.body().html());
    }

    @Test
    public void testParseInputStreamWithBomCharset() throws IOException {
        String xmlData = "<root><child>Hello</child></root>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlData.getBytes(DEFAULT_CHARSET));
        ByteBuffer firstBytes = DataUtil.readToByteBuffer(inputStream, FIRST_READ_BUFFER_SIZE);

        Document doc = DataUtil.parseInputStream(inputStream, null, null, null);
        assertEquals(xmlData, doc.body().html());

        BomCharset bomCharset = DataUtil.detectCharsetFromBom(firstBytes);
        assertEquals("UTF-8", bomCharset.charset);
        assertTrue(bomCharset.offset);
    }

    @Test
    public void testParseInputStreamWithBomUtf16() throws IOException {
        String xmlData = "<root><child>Hello</child></root>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlData.getBytes(DEFAULT_CHARSET));
        ByteBuffer firstBytes = DataUtil.readToByteBuffer(inputStream, FIRST_READ_BUFFER_SIZE);

        firstBytes.put(0xEF);
        firstBytes.put(0xBB);
        firstBytes.put(0xBF);

        Document doc = DataUtil.parseInputStream(inputStream, null, null, null);
        assertEquals(xmlData, doc.body().html());

        BomCharset bomCharset = DataUtil.detectCharsetFromBom(firstBytes);
        assertEquals("UTF-8", bomCharset.charset);
        assertFalse(bomCharset.offset);
    }

    @Test
    public void testParseInputStreamWithInvalidBom() throws IOException {
        String xmlData = "<root><child>Hello</child></root>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlData.getBytes(DEFAULT_CHARSET));
        ByteBuffer firstBytes = DataUtil.readToByteBuffer(inputStream, FIRST_READ_BUFFER_SIZE);

        firstBytes.put(0xFF);
        firstBytes.put(0xFF);
        firstBytes.put(0xFF);
        firstBytes.put(0xFF);

        assertThrows(IllegalArgumentException.class, () -> DataUtil.parseInputStream(inputStream, null, null, null));
    }

    @Test
    public void testReadToByteBufferWithZeroSize() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("".getBytes(DEFAULT_CHARSET));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);
        assertTrue(byteBuffer.position() == 0);
    }

    @Test
    public void testReadToByteBufferWithNegativeSize() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("".getBytes(DEFAULT_CHARSET));
        assertThrows(IllegalArgumentException.class, () -> DataUtil.readToByteBuffer(inputStream, -1));
    }

    @Test
    public void testReadToByteBufferWithValidSize() throws IOException {
        String xmlData = "<root><child>Hello</child></root>";
        InputStream inputStream = new ByteArrayInputStream(xmlData.getBytes(DEFAULT_CHARSET));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, FIRST_READ_BUFFER_SIZE);

        assertEquals(xmlData, new String(byteBuffer.array(), 0, (int) byteBuffer.limit(), DEFAULT_CHARSET));
    }

    @Test
    public void testReadToByteBufferWithBufferSizeGreaterThanInputStreamSize() throws IOException {
        String xmlData = "<root><child>Hello</child></root>";
        InputStream inputStream = new ByteArrayInputStream(xmlData.getBytes(DEFAULT_CHARSET));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, BUFFER_SIZE);

        assertEquals(xmlData, new String(byteBuffer.array(), 0, (int) byteBuffer.limit(), DEFAULT_CHARSET));
    }

    @Test
    public void testReadToByteBufferWithInterruptedThread() throws IOException, InterruptedException {
        String xmlData = "<root><child>Hello</child></root>";
        InputStream inputStream = new ByteArrayInputStream(xmlData.getBytes(DEFAULT_CHARSET));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, FIRST_READ_BUFFER_SIZE);

        Thread thread = new Thread(() -> {
            try {
                byteBuffer.limit(FIRST_READ_BUFFER_SIZE);
                byte[] buffer = new byte[FIRST_READ_BUFFER_SIZE];
                int bytesRead = inputStream.read(buffer);
                byteBuffer.put(buffer, 0, bytesRead);
            } catch (IOException e) {
                // ignore
            } finally {
                inputStream.close();
            }
        });

        thread.start();
        thread.interrupt();

        assertEquals(xmlData, new String(byteBuffer.array(), 0, (int) byteBuffer.limit(), DEFAULT_CHARSET));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-16LE";
        assertEquals("UTF-16LE", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "UTF-16LE";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "GBK";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "UTF-16";
        assertEquals("UTF-16", DataUtil.validateCharset(charset));

        charset = "ISO-8859-1";
        assertEquals("ISO-8859-1", DataUtil.validateCharset(charset));

        charset = "invalid";
        assertNull(DataUtil.validateCharset(charset));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertEquals(boundary.length(), DataUtil.boundaryLength);

        for (int i = 0; i < DataUtil.boundaryLength; i++) {
            char c = boundary.charAt(i);
            assertTrue("Character should be one of " + new String(DataUtil.mimeBoundaryChars),
                    DataUtil.mimeBoundaryChars[new Random().nextInt(DataUtil.mimeBoundaryChars.length)] == c);
        }
    }
}