package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.Document;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Test class for DataUtil methods.
 */
public class DataUtilTest {

    private InputStream inputStream;
    private OutputStream outputStream;
    private Parser parser;

    @Before
    public void setup() {
        parser = Mockito.mock(Parser.class);
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", null);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(document);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", null, parser);
        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String input = "Test input";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals(input, new String(outputStream.toByteArray(), StandardCharsets.UTF_8));
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String html = "<html><body><h1>Test</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Mockito.doThrow( new IOException("Invalid charset") ).when(parser).parseInput(Mockito.anyString(), Mockito.anyString());
        try {
            DataUtil.parseInputStream(inputStream, "InvalidCharset", null, parser);
            fail("Expected IOException");
        } catch (IOException e) {
            assertEquals("Invalid charset", e.getMessage());
        }
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024 * 32);
        assertEquals(html.getBytes(StandardCharsets.UTF_8).length, byteBuffer.limit());
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithInterruptedInputStream() throws IOException, InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                InputStream inputStream = new ByteArrayInputStream(new byte[1024 * 32]);
                ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024 * 32);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
        thread.interrupt();
        thread.join();
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String fileContent = "<html><body><h1>Test</h1></body></html>";
        File testFile = new File("test.html");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);
        assertEquals(fileContent.getBytes(StandardCharsets.UTF_8).length, byteBuffer.limit());
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=InvalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validatedCharset);
    }

    @Test
    public void testValidateCharsetWithNullCharset() {
        String charset = null;
        String validatedCharset = DataUtil.validateCharset(charset);
        assertNull(validatedCharset);
    }

    @Test
    public void testValidateCharsetWithInvalidCharset() {
        String charset = "InvalidCharset";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        assertFalse(mimeBoundary.isEmpty());
    }

    @Test
    public void testDetectCharsetFromBomWithUTF16BE() throws IOException {
        String testFileContent = "𠮷";
        File testFile = new File("test.txt");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertEquals("UTF-16", bomCharset.charset);
        assertEquals(2, bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomWithUTF16LE() throws IOException {
        String testFileContent = "𠮷";
        File testFile = new File("test.txt");
        ByteBuffer byteBuffer = ByteBuffer.allocate(2);
        byteBuffer.put((byte) 0xFF);
        byteBuffer.put((byte) 0xFE);
        byteBuffer.flip();
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertEquals("UTF-16", bomCharset.charset);
        assertEquals(0, bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomWithUTF32BE() throws IOException {
        String testFileContent = "🄵";
        File testFile = new File("test.txt");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertEquals("UTF-32", bomCharset.charset);
        assertEquals(0, bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomWithUTF32LE() throws IOException {
        String testFileContent = "🄵";
        File testFile = new File("test.txt");
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.put((byte) 0x00);
        byteBuffer.put((byte) 0x00);
        byteBuffer.put((byte) 0xFE);
        byteBuffer.put((byte) 0xFF);
        byteBuffer.flip();
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertEquals("UTF-32", bomCharset.charset);
        assertEquals(0, bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomWithInvalidBom() throws IOException {
        String testFileContent = "Test";
        File testFile = new File("test.txt");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertNull(bomCharset);
    }
}