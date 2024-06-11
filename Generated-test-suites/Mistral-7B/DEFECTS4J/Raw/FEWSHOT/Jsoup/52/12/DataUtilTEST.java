package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        File file = new File("test.html");
        Document doc = DataUtil.load(file, charset, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        String html = "<html><body></body></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(in, charset, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testParseByteData() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        String html = "<html><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());
        Document doc = DataUtil.parseByteData(byteData, charset, baseUri, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithCharsetDetectedFromFile() throws IOException {
        String charset = null;
        String baseUri = "http://example.com";
        String html = "<html><head><meta charset=\"ISO-8859-1\"></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());
        Document doc = DataUtil.parseByteData(byteData, charset, baseUri, Parser.htmlParser());
        assertEquals("ISO-8859-1", doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithCharsetDetectedFromMetaTag() throws IOException {
        String charset = null;
        String baseUri = "http://example.com";
        String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());
        Document doc = DataUtil.parseByteData(byteData, charset, baseUri, Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithCharsetDetectedFromXmlDeclaration() throws IOException {
        String charset = null;
        String baseUri = "http://example.com";
        String html = "<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\" xml:base=\"http://example.com\">" +
                "<head><meta charset=\"UTF-8\"/><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>" +
                "<title>Test Page</title></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());
        Document doc = DataUtil.parseByteData(byteData, charset, baseUri, Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String charset = null;
        String baseUri = "http://example.com";
        String html = "<html><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());
        Document doc = DataUtil.parseByteData(byteData, charset, baseUri, Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputFilePath = "input.txt";
        String outputFilePath = "output.txt";
        File inputFile = new File(inputFilePath);
        File outputFile = new File(outputFilePath);
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        ByteBuffer inputByteBuffer = DataUtil.readFileToByteBuffer(inputFile);
        ByteBuffer outputByteBuffer = DataUtil.readFileToByteBuffer(outputFile);

        assertEquals(inputByteBuffer, outputByteBuffer);

        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputFilePath = "input.txt";
        File inputFile = new File(inputFilePath);
        InputStream inputStream = new FileInputStream(inputFile);

        byte[] expectedBytes = new byte[inputFile.length()];
        inputStream.read(expectedBytes);

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        assertEquals(expectedBytes, byteData.array());

        inputStream.close();
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String inputFilePath = "input.txt";
        File inputFile = new File(inputFilePath);
        InputStream inputStream = new FileInputStream(inputFile);
        int maxSize = 10;

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, maxSize);

        byte[] expectedBytes = new byte[maxSize];
        inputStream.read(expectedBytes);

        assertEquals(expectedBytes, byteData.array());

        inputStream.close();
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
        String contentType = "text/html; charset=invalidCharset";
        String expectedCharset = null;

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String expectedCharset = "UTF-8";

        String validatedCharset = DataUtil.validateCharset(charset);

        assertEquals(expectedCharset, validatedCharset);
    }

    @Test
    public void testValidateCharsetWithInvalidCharset() {
        String charset = "invalidCharset";
        String expectedCharset = null;

        String validatedCharset = DataUtil.validateCharset(charset);

        assertEquals(expectedCharset, validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String expectedBoundary = "7d4d4a3b-86d2-4b3a-80a3-17e69b2b5d75";

        String boundary = DataUtil.mimeBoundary();

        assertEquals(expectedBoundary, boundary);
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        String charset = null;
        String bom = "UTF-32";

        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        byteData.put((byte) 0x00).put((byte) 0x00).put((byte) 0xFE).put((byte) 0xFF);
        byteData.rewind();

        charset = DataUtil.detectCharsetFromBom(byteData, charset);

        assertEquals(bom, charset);
    }

    @Test
    public void testDetectCharsetFromBomWithNoBom() throws IOException {
        String charset = null;

        ByteBuffer byteData = DataUtil.emptyByteBuffer();

        charset = DataUtil.detectCharsetFromBom(byteData, charset);

        assertNull(charset);
    }
}