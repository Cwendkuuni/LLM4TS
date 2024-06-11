package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.parser.Parser.HtmlParser;
import org.jsoup.parser.Parser.XmlParser;
import org.jsoup.select.Elements;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "This is a test <html> file.";
        File testFile = new File("test.html");
        Files.write(testFile.getAbsoluteFile(), fileContent.getBytes());

        Document doc = DataUtil.load(testFile, null, null);
        assertEquals(doc.body().text(), fileContent);
        Files.deleteIfExists(testFile.getAbsoluteFile());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testString = "This is a test String.";
        InputStream testStream = new ByteArrayInputStream(testString.getBytes());

        Document doc = DataUtil.load(testStream, null, null);
        assertEquals(doc.body().text(), testString);
    }

    @Test
    public void testLoadInputStreamWithCharset() throws IOException {
        String testString = "This is a test String.";
        InputStream testStream = new ByteArrayInputStream(testString.getBytes("UTF-16"));

        Document doc = DataUtil.load(testStream, "UTF-16", null);
        assertEquals(doc.body().text(), testString);
    }

    @Test
    public void testLoadInputStreamWithBaseUri() throws IOException {
        String testString = "<html><head><base href=\"/test\"></base></head><body><p>This is a test String.</p></body></html>";
        InputStream testStream = new ByteArrayInputStream(testString.getBytes());

        Document doc = DataUtil.load(testStream, null, "/test");
        assertEquals(doc.body().select("p").text(), "This is a test String.");
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String testString = "<html><head></head><body><p>This is a test String.</p></body></html>";
        InputStream testStream = new ByteArrayInputStream(testString.getBytes());

        Parser parser = new Parser();
        Document doc = DataUtil.load(testStream, null, null, parser);
        assertEquals(doc.body().select("p").text(), "This is a test String.");
    }

    @Test
    public void testCrossStreams() throws IOException {
        String testString = "This is a test String.";
        File testFile = new File("test.html");
        Files.write(testFile.getAbsoluteFile(), testString.getBytes());

        InputStream input = new FileInputStream(testFile);
        OutputStream output = new ByteArrayOutputStream();

        DataUtil.crossStreams(input, output);
        String result = new String(output.toByteArray());
        assertEquals(testString, result);
        input.close();
        output.close();
        Files.deleteIfExists(testFile.getAbsoluteFile());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String testString = "<html><head></head><body><p>This is a test String.</p></body></html>";
        InputStream testStream = new ByteArrayInputStream(testString.getBytes());

        Document doc = DataUtil.parseInputStream(testStream, null, null, HtmlParser.instance());
        assertEquals(doc.body().select("p").text(), "This is a test String.");
    }

    @Test
    public void testParseInputStreamWithCharset() throws IOException {
        String testString = "<html><head></head><body><p>This is a test String.</p></body></html>";
        InputStream testStream = new ByteArrayInputStream(testString.getBytes("UTF-16"));

        Document doc = DataUtil.parseInputStream(testStream, "UTF-16", null, HtmlParser.instance());
        assertEquals(doc.body().select("p").text(), "This is a test String.");
    }

    @Test
    public void testParseInputStreamWithBaseUri() throws IOException {
        String testString = "<html><head><base href=\"/test\"></base></head><body><p>This is a test String.</p></body></html>";
        InputStream testStream = new ByteArrayInputStream(testString.getBytes());

        Document doc = DataUtil.parseInputStream(testStream, null, "/test", HtmlParser.instance());
        assertEquals(doc.body().select("p").text(), "This is a test String.");
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        String testString = "<html><head></head><body><p>This is a test String.</p></body></html>";
        InputStream testStream = new ByteArrayInputStream(testString.getBytes());

        Parser parser = new Parser();
        Document doc = DataUtil.parseInputStream(testStream, null, null, parser);
        assertEquals(doc.body().select("p").text(), "This is a test String.");
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testString = "This is a test String.";
        InputStream testStream = new ByteArrayInputStream(testString.getBytes());

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testStream, 1024);
        byte[] expectedBytes = testString.getBytes();
        byte[] bufferBytes = new byte[byteBuffer.capacity()];
        byteBuffer.get(bufferBytes);
        assertEquals(expectedBytes, bufferBytes);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String testString = "This is a test String.";
        InputStream testStream = new ByteArrayInputStream(testString.getBytes());

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testStream, testString.length());
        byte[] expectedBytes = testString.getBytes();
        byte[] bufferBytes = new byte[byteBuffer.capacity()];
        byteBuffer.get(bufferBytes);
        assertEquals(expectedBytes, bufferBytes);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(charset, "UTF-8");

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validatedCharset);

        charset = " invalidCharset ";
        validatedCharset = DataUtil.validateCharset(charset);
        assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertTrue(boundary.length() == 32);
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        InputStream testStream = new ByteArrayInputStream(new byte[]{(byte) 0xFF, (byte) 0xFE, (byte) 0, (byte) 0});
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(testStream, 4));
        assertEquals(bomCharset.charset, "UTF-16");
        assertEquals(bomCharset.offset, false);

        testStream = new ByteArrayInputStream(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, (byte) 0});
        bomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(testStream, 4));
        assertEquals(bomCharset.charset, "UTF-8");
        assertEquals(bomCharset.offset, true);

        testStream = new ByteArrayInputStream(new byte[]{});
        bomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(testStream, 0));
        assertNull(bomCharset);
    }
}