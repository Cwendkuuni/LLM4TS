package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testParseInputStream() throws Exception {
        String html = "<html>\n<head>\n<meta charset=\"UTF-8\"/>\n</head>\n<body>\n<h1>Hello, World!</h1>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        assertEquals(html, doc.html());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws Exception {
        String html = "<html>\n<head>\n<meta charset=\"ISO-12345-6\"/>\n</head>\n<body>\n<h1>Hello, World!</h1>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = null;
        try {
            doc = DataUtil.parseInputStream(inputStream, "ISO-12345-6", null, Parser.htmlParser());
        } catch (IOException e) {
            // Expected
        }
        assertNull(doc);
    }

    @Test
    public void testParseInputStreamWithContentTypeHeader() throws Exception {
        String html = "<html>\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n</head>\n<body>\n<h1>Hello, World!</h1>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertEquals(html, doc.html());
    }

    @Test
    public void testParseInputStreamWithXmlDeclaration() throws Exception {
        String xml = "<?xml encoding=\"UTF-8\"?>\n<root>\n<element>Hello, World!</element>\n</root>";
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.xmlParser());
        assertEquals(xml, doc.xml());
    }

    @Test
    public void testParseInputStreamWithNoCharsetOrContentType() throws Exception {
        String html = "<html>\n<head>\n</head>\n<body>\n<h1>Hello, World!</h1>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertEquals(html, doc.html());
    }

    @Test
    public void testParseInputStreamWithBOM() throws Exception {
        String html = "\ufeff<html>\n<head>\n</head>\n<body>\n<h1>Hello, World!</h1>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        assertEquals(html, doc.html());
    }

    @Test
    public void testParseInputStreamWithBOMAndInvalidCharset() throws Exception {
        String html = "\ufeff<html>\n<head>\n<meta charset=\"ISO-12345-6\"/>\n</head>\n<body>\n<h1>Hello, World!</h1>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = null;
        try {
            doc = DataUtil.parseInputStream(inputStream, "ISO-12345-6", null, Parser.htmlParser());
        } catch (IOException e) {
            // Expected
        }
        assertNull(doc);
    }

    @Test
    public void testLoadFile() throws IOException {
        String filePath = "src/test/resources/testFile.html";
        Document doc = DataUtil.load(new File(filePath), "UTF-8", null);
        assertEquals(FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8), doc.html());
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        String filePath = "src/test/resources/testFile.html";
        Document doc = null;
        try {
            doc = DataUtil.load(new File(filePath), "ISO-12345-6", null);
        } catch (IOException e) {
            // Expected
        }
        assertNull(doc);
    }

    @Test
    public void testLoadFileWithNoCharset() throws IOException {
        String filePath = "src/test/resources/testFile.html";
        Document doc = DataUtil.load(new File(filePath), null, null);
        assertEquals(FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8), doc.html());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputText = "Hello, World!";
        String outputText = "Output: ";
        InputStream inputStream = new ByteArrayInputStream(inputText.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertEquals("Output: Hello, World!", output);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputText = "Hello, World!";
        InputStream inputStream = new ByteArrayInputStream(inputText.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        byte[] expectedByteArray = inputText.getBytes(StandardCharsets.UTF_8);
        byte[] actualByteArray = new byte[byteBuffer.limit()];
        byteBuffer.get(actualByteArray);

        assertArrayEquals(expectedByteArray, actualByteArray);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String filePath = "src/test/resources/testFile.html";
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(new File(filePath));

        String expectedText = FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);
        byte[] expectedByteArray = expectedText.getBytes(StandardCharsets.UTF_8);

        byte[] actualByteArray = new byte[byteBuffer.limit()];
        byteBuffer.get(actualByteArray);

        assertArrayEquals(expectedByteArray, actualByteArray);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertEquals(boundary.length(), 32);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=ISO-12345-6";
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
    public void testValidateCharsetWithInvalidCharset() {
        String charset = "ISO-12345-6";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertNull(validatedCharset);
    }
}