package org.jsoup.helper;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

import org.jsoup.Document;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<html><body>Hello World</body></html>";
        File file = new File("testFile.html");
        FileWriter writer = new FileWriter(file);
        writer.write(fileContent);
        writer.close();

        Document doc = DataUtil.load(file, StandardCharsets.UTF_8.name(), null);
        assertEquals("<html><body>Hello World</body></html>", doc.html());

        doc = DataUtil.load(file, null, null);
        assertEquals("<html><body>Hello World</body></html>", doc.html());

        DataUtil.deleteFile(file);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "<html><body>Hello World</body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), null);
        assertEquals("<html><body>Hello World</body></html>", doc.html());

        doc = DataUtil.load(inputStream, null, null);
        assertEquals("<html><body>Hello World</body></html>", doc.html());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String inputStreamContent = "<html><body>Hello World</body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "/base/uri";
        Parser parser = mock(Parser.class);

        Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        verify(parser).parseInput(eq(inputStream), eq(baseUri));
        assertEquals("<html><body>Hello World</body></html>", doc.html());
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test Input Stream".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals("Test Input Stream", outputStream.toString());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String inputStreamContent = "<html><body>Hello World</body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "invalidCharset";
        String baseUri = "/base/uri";
        Parser parser = mock(Parser.class);

        assertThrows(IllegalArgumentException.class, () -> DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser));
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputStreamContent = "Test Input Stream";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        int maxSize = 10;

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        assertEquals(inputStreamContent, Charset.forName("UTF-8").decode(byteBuffer).toString());
    }

    @Test
    public void testReadToByteBufferWithMaxSizeZero() throws IOException {
        String inputStreamContent = "Test Input Stream";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);
        assertEquals(inputStreamContent, Charset.forName("UTF-8").decode(byteBuffer).toString());
    }

    @Test
    public void testReadToByteBufferWithInterruptedThread() throws IOException, InterruptedException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[100]);
        Thread thread = new Thread(() -> {
            try {
                DataUtil.readToByteBuffer(inputStream, 100);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        thread.start();
        thread.interrupt();
        assertTrue(thread.isInterrupted());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String fileContent = "Test File Content";
        File file = new File("testFile.txt");
        FileWriter writer = new FileWriter(file);
        writer.write(fileContent);
        writer.close();

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        assertEquals(fileContent, Charset.forName("UTF-8").decode(byteBuffer).toString());

        DataUtil.deleteFile(file);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertEquals("UTF-8", validatedCharset);

        charset = "unknownCharset";
        validatedCharset = DataUtil.validateCharset(charset);
        assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        assertTrue(mimeBoundary.length() > 0);
    }

    @Test
    public void testDetectCharsetFromBom() {
        byte[] bomUtf32 = {0x00, 0x00, (byte) 0xFE, (byte) 0xFF};
        ByteBuffer byteDataUtf32 = ByteBuffer.wrap(bomUtf32);
        BomCharset bomCharsetUtf32 = DataUtil.detectCharsetFromBom(byteDataUtf32);
        assertEquals("UTF-32", bomCharsetUtf32.charset);
        assertEquals(false, bomCharsetUtf32.offset);

        byte[] bomUtf16BE = {0xFE, 0xFF, 0x00, 0x00};
        ByteBuffer byteDataUtf16BE = ByteBuffer.wrap(bomUtf16BE);
        BomCharset bomCharsetUtf16BE = DataUtil.detectCharsetFromBom(byteDataUtf16BE);
        assertEquals("UTF-16", bomCharsetUtf16BE.charset);
        assertEquals(false, bomCharsetUtf16BE.offset);

        byte[] bomUtf16LE = {0xFF, 0xFE, 0x00, 0x00};
        ByteBuffer byteDataUtf16LE = ByteBuffer.wrap(bomUtf16LE);
        BomCharset bomCharsetUtf16LE = DataUtil.detectCharsetFromBom(byteDataUtf16LE);
        assertEquals("UTF-16", bomCharsetUtf16LE.charset);
        assertEquals(false, bomCharsetUtf16LE.offset);

        byte[] bomUtf8 = {0xEF, 0xBB, 0xBF};
        ByteBuffer byteDataUtf8 = ByteBuffer.wrap(bomUtf8);
        BomCharset bomCharsetUtf8 = DataUtil.detectCharsetFromBom(byteDataUtf8);
        assertEquals("UTF-8", bomCharsetUtf8.charset);
        assertEquals(true, bomCharsetUtf8.offset);

        ByteBuffer byteDataNone = ByteBuffer.wrap(new byte[0]);
        BomCharset bomCharsetNone = DataUtil.detectCharsetFromBom(byteDataNone);
        assertNull(bomCharsetNone);
    }
}