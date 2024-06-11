package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        File tempFile = File.createTempFile("jsoup", null);
        try (RandomAccessFile file = new RandomAccessFile(tempFile, "rw");
             OutputStream os = new BufferedOutputStream(file)) {
            os.write(html.getBytes(StandardCharsets.UTF_8));
        }

        Document doc = DataUtil.load(tempFile, null, null);
        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
        tempFile.delete();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(input, null, null);
        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test
    public void testLoadInputStreamWithCharset() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String charset = "UTF-8";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(input, charset, null);
        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String charset = "UTF-8";
        Parser parser = Parser.xmlParser(); // Use a different parser here for test coverage

        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(input, charset, null, parser);
        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(input, null, null, Parser.htmlParser());
        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test
    public void testParseInputStreamWithCharset() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String charset = "UTF-8";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(input, charset, null, Parser.htmlParser());
        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test
    public void testParseInputStreamWithCustomParser() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String charset = "UTF-8";
        Parser parser = Parser.xmlParser(); // Use a different parser here for test coverage

        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(input, charset, null, parser);
        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String source = "Source Text";
        String target = "Target Text";

        InputStream input = new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8));
        OutputStream output = new ByteArrayOutputStream();

        DataUtil.crossStreams(input, output);

        String result = output.toString(StandardCharsets.UTF_8.name());
        assertEquals(source, result);
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[] {0x00, 0x00, 0xFE, 0xFF}); // BE UTF-32
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("UTF-32", bomCharset.charset);
        assertEquals(false, bomCharset.offset);

        byteData = ByteBuffer.wrap(new byte[] {0xFE, 0xFF, 0x00, 0x00}); // LE UTF-32
        bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("UTF-32", bomCharset.charset);
        assertEquals(false, bomCharset.offset);

        byteData = ByteBuffer.wrap(new byte[] {0xEF, 0xBB, 0xBF}); // UTF-8
        bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("UTF-8", bomCharset.charset);
        assertEquals(true, bomCharset.offset);

        byteData = ByteBuffer.wrap(new byte[0]);
        bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertNull(bomCharset);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String source = "Source Text";
        InputStream input = new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(input, 0);

        byte[] expectedByteArray = source.getBytes(StandardCharsets.UTF_8);
        byte[] actualByteArray = new byte[byteBuffer.remaining()];
        byteBuffer.get(actualByteArray);

        assertArrayEquals(expectedByteArray, actualByteArray);
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
        assertEquals(charset, validatedCharset);

        charset = "UTF-8\t";
        validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validatedCharset);

        charset = "UTF-8\"";
        validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validatedCharset);

        charset = "UTF-8'";
        validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validatedCharset);

        charset = "UTF-8\n";
        validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validatedCharset);

        charset = "GBK";
        validatedCharset = DataUtil.validateCharset(charset);
        assertEquals("GBK", validatedCharset);

        charset = "GBK\t";
        validatedCharset = DataUtil.validateCharset(charset);
        assertEquals("GBK", validatedCharset);

        charset = "GBK\"";
        validatedCharset = DataUtil.validateCharset(charset);
        assertEquals("GBK", validatedCharset);

        charset = "GBK'";
        validatedCharset = DataUtil.validateCharset(charset);
        assertEquals("GBK", validatedCharset);

        charset = "GBK\n";
        validatedCharset = DataUtil.validateCharset(charset);
        assertEquals("GBK", validatedCharset);

        charset = "InvalidCharset";
        validatedCharset = DataUtil.validateCharset(charset);
        assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        assertEquals(boundaryLength, mimeBoundary.length());
    }
}