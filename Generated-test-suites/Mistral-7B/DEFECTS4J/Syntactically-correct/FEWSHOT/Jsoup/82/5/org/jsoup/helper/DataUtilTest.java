package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<html><body><h1>Hello World</h1></body></html>";
        File tempFile = File.createTempFile("testDataUtilLoadFile", null);
        FileWriter writer = new FileWriter(tempFile);
        writer.write(fileContent);
        writer.close();

        Document doc = DataUtil.load(tempFile, null, null);
        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.body().html());

        tempFile.delete();
    }

    @Test(expected = IOException.class)
    public void testLoadFileNullInput() throws IOException {
        DataUtil.load(null, null, null);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputContent = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes());

        Document doc = DataUtil.load(inputStream, null, null);
        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.body().html());

        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamNullInput() throws IOException {
        DataUtil.load(null, null, null);
    }

    @Test
    public void testLoadInputStreamWithCharset() throws IOException {
        String inputContent = "<?xml encoding='UTF-16'?><html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes());

        Document doc = DataUtil.load(inputStream, "UTF-16", null);
        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.body().html());

        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String inputContent = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes());

        try {
            DataUtil.load(inputStream, "invalidCharset", null);
            fail("Should have thrown IOException.");
        } catch (IOException e) {
            // Expected
        }

        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithBaseUri() throws IOException {
        String htmlContent = "<html><head><link rel='stylesheet' href='/styles.css'></head><body><h1>Hello World</h1></body></html>";
        String cssContent = "body { color: red; }";
        File tempFileHtml = File.createTempFile("testDataUtilLoadInputStreamWithBaseUriHtml", null);
        File tempFileCss = File.createTempFile("testDataUtilLoadInputStreamWithBaseUriCss", null);

        FileWriter htmlWriter = new FileWriter(tempFileHtml);
        htmlWriter.write(htmlContent);
        htmlWriter.close();

        FileWriter cssWriter = new FileWriter(tempFileCss);
        cssWriter.write(cssContent);
        cssWriter.close();

        Document htmlDoc = DataUtil.load(new FileInputStream(tempFileHtml), null, "/");
        assertEquals("<html><head><link rel='stylesheet' href='/styles.css'><body><h1 style='color: red;'>Hello World</h1></body></html>", htmlDoc.html());

        Elements links = htmlDoc.select("link");
        assertEquals(1, links.size());
        Document cssDoc = DataUtil.load(new FileInputStream(tempFileCss), null, null);

        tempFileHtml.delete();
        tempFileCss.delete();
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputContent = "Hello World";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] expectedOutput = inputContent.getBytes();
        byte[] output = outputStream.toByteArray();

        assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String inputContent = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes());
        String charsetName = "UTF-8";

        Document doc = DataUtil.parseInputStream(inputStream, charsetName, null, Parser.htmlParser());
        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.body().html());

        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamNullInput() throws IOException {
        Document doc = DataUtil.parseInputStream(null, null, null, Parser.htmlParser());
        fail("Should have thrown IOException.");
    }

    @Test
    public void testParseInputStreamWithCharset() throws IOException {
        String inputContent = "<?xml encoding='UTF-16'?><html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes());
        String charsetName = "UTF-16";

        Document doc = DataUtil.parseInputStream(inputStream, charsetName, null, Parser.htmlParser());
        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.body().html());

        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String inputContent = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes());
        String charsetName = "invalidCharset";

        try {
            Document doc = DataUtil.parseInputStream(inputStream, charsetName, null, Parser.htmlParser());
            fail("Should have thrown IOException.");
        } catch (IOException e) {
            // Expected
        }

        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithBaseUri() throws IOException {
        String htmlContent = "<html><head><link rel='stylesheet' href='/styles.css'></head><body><h1>Hello World</h1></body></html>";
        String cssContent = "body { color: red; }";
        InputStream inputStreamHtml = new ByteArrayInputStream(htmlContent.getBytes());
        InputStream inputStreamCss = new ByteArrayInputStream(cssContent.getBytes());
        String charsetName = null;
        String baseUri = "/";

        Document htmlDoc = DataUtil.parseInputStream(inputStreamHtml, charsetName, baseUri, Parser.htmlParser());
        assertEquals("<html><head><link rel='stylesheet' href='/styles.css'><body><h1>Hello World</h1></body></html>", htmlDoc.html());

        Elements links = htmlDoc.select("link");
        assertEquals(1, links.size());
        Document cssDoc = DataUtil.parseInputStream(inputStreamCss, charsetName, baseUri, Parser.xmlParser());

        inputStreamHtml.close();
        inputStreamCss.close();
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        String inputContent = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes());
        String charsetName = "UTF-8";
        Parser parser = Parser.xmlParser();

        Document doc = DataUtil.parseInputStream(inputStream, charsetName, null, parser);
        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.body().html());

        inputStream.close();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String content = "Hello World";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024 * 5);

        byte[] expectedContent = content.getBytes();

        byte[] buffer = new byte[expectedContent.length];
        byteBuffer.get(buffer);
        assertArrayEquals(expectedContent, buffer);

        inputStream.close();
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertTrue(byteBuffer.capacity() == 0);
        assertTrue(byteBuffer.limit() == 0);
        assertTrue(byteBuffer.position() == 0);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        assertEquals(expectedCharset, DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testGetCharsetFromContentTypeNullContentType() {
        String contentType = null;
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "UTF-16";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "ISO-8859-1";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "invalidCharset";
        assertNull(DataUtil.validateCharset(charset));
    }

    @Test
    public void testValidateCharsetNullCharset() {
        String charset = null;
        assertNull(DataUtil.validateCharset(charset));
    }

    @Test
    public void testMimeBoundary() {
        String expected = "Bo_ndaRy_4d7d59a1-6cc5-435f-812d-6cc04c9d2ca6";
        assertEquals(expected, DataUtil.mimeBoundary());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer bomUtf16 = ByteBuffer.wrap(new byte[]{(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00});
        ByteBuffer bomUtf8 = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
        ByteBuffer bomUtf32 = ByteBuffer.wrap(new byte[]{0x00, 0x00, (byte) 0xFE, (byte) 0xFF});

        BomCharset utf16 = DataUtil.detectCharsetFromBom(bomUtf16);
        BomCharset utf8 = DataUtil.detectCharsetFromBom(bomUtf8);
        BomCharset utf32 = DataUtil.detectCharsetFromBom(bomUtf32);

        assertEquals("UTF-16", utf16.charset);
        assertEquals(false, utf16.offset);
        assertEquals("UTF-8", utf8.charset);
        assertEquals(true, utf8.offset);
        assertEquals(null, utf32);
    }

    @Test
    public void testDetectCharsetFromBomNullByteBuffer() {
        assertNull(DataUtil.detectCharsetFromBom(DataUtil.emptyByteBuffer()));
    }
}

class FileWriter {
    FileWriter(File file) {
        this.file = file;
    }

    void write(String string) throws IOException {
        FileWriter writer = new FileWriter(new File(file.getAbsolutePath() + ".tmp"));
        writer.write(string);
        writer.close();

        File oldFile = new File(file.getAbsolutePath());
        if (oldFile.exists()) {
            oldFile.delete();
        }
        file.renameTo(new File(file.getAbsolutePath() + ".tmp"));
        file.renameTo(new File(file.getAbsolutePath() + ".tmp").getParentFile().newFile(file.getName()));
    }

    void close() throws IOException {
        // Do nothing
    }

    File file;
}