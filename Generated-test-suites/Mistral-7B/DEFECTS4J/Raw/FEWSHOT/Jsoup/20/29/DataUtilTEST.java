package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.parser.Parser.htmlParser;
import org.jsoup.parser.Parser.xmlParser;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        File testFile = new File("testFile.xml");
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write(fileContent);
        fileWriter.close();

        Document document = DataUtil.load(testFile, null, null);
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>", document.html());

        testFile.delete();
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        File testFile = new File("testFile.xml");
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write(fileContent);
        fileWriter.close();

        Document document = DataUtil.load(testFile, "ISO-8859-1", null);
        assertNotNull(document);
        assertEquals("ISO-8859-1", document.outputSettings().charset());
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>", document.html());

        testFile.delete();
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, null, null);
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>", document.html());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, "ISO-8859-1", null);
        assertNotNull(document);
        assertEquals("ISO-8859-1", document.outputSettings().charset());
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>", document.html());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedParser() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, null, null, xmlParser());
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>", document.xml());
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String charsetName = "UTF-8";
        String fileContent = "<?xml version=\"1.0\" encoding=\"" + charsetName + "\"?>\n<html><head><meta charset=\"" + charsetName + "\"></head><body><h1>Test</h1></body></html>";
        byte[] byteData = fileContent.getBytes(StandardCharsets.UTF_8);

        Document document = DataUtil.parseByteData(ByteBuffer.wrap(byteData), charsetName, null, htmlParser());
        assertNotNull(document);
        assertEquals(charsetName, document.outputSettings().charset());
        assertEquals("<html><head><meta charset=\"" + charsetName + "\"></head><body><h1>Test</h1></body></html>", document.html());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String charsetName = "UTF-8";
        String fileContent = "<?xml version=\"1.0\" encoding=\"" + charsetName + "\"?>\n<html><head><meta charset=\"" + charsetName + "\"></head><body><h1>Test</h1></body></html>";
        byte[] byteData = fileContent.getBytes(StandardCharsets.UTF_8);

        Document document = DataUtil.parseByteData(ByteBuffer.wrap(byteData), "ISO-8859-1", null, htmlParser());
        assertNotNull(document);
        assertEquals("ISO-8859-1", document.outputSettings().charset());
        assertEquals("<html><head><meta charset=\"" + charsetName + "\"></head><body><h1>Test</h1></body></html>", document.html());
    }

    @Test
    public void testParseByteDataWithDetectedCharset() {
        String charsetName = "ISO-8859-1";
        String fileContent = "<?xml version=\"1.0\" encoding=\"" + charsetName + "\"?>\n<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        byte[] byteData = fileContent.getBytes(StandardCharsets.UTF_8);

        Document document = DataUtil.parseByteData(ByteBuffer.wrap(byteData), null, null, htmlParser());
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>", document.html());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        byte[] byteArray = byteData.array();
        String decodedData = Charset.forName(StandardCharsets.UTF_8.name()).decode(ByteBuffer.wrap(byteArray)).toString();
        assertEquals(fileContent, decodedData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String resultCharset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, resultCharset);
    }

    @Test
    public void testGetCharsetFromContentTypeNull() {
        String contentType = null;

        String resultCharset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(resultCharset);
    }

}