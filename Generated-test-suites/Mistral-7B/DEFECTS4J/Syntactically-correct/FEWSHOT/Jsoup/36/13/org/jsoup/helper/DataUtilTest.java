package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFile() {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body><p>Hello World</p></body></html>";
        File testFile = new File("test.html");
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Document document = null;
        try {
            document = DataUtil.load(testFile, "UTF-8", "/");
            assertNotNull(document);
            assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><p>Hello World</p></body></html>", document.html());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            testFile.delete();
        }
    }

    @Test
    public void testLoadDocumentFromInputStream() {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body><p>Hello World</p></body></html>";
        byte[] fileContentBytes = fileContent.getBytes(StandardCharsets.UTF_8);

        InputStream inputStream = new ByteArrayInputStream(fileContentBytes);
        Document document = null;
        try {
            document = DataUtil.load(inputStream, "UTF-8", "/");
            assertNotNull(document);
            assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><p>Hello World</p></body></html>", document.html());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadDocumentFromInputStreamWithInvalidCharset() {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body><p>Hello World</p></body></html>";
        byte[] fileContentBytes = fileContent.getBytes(StandardCharsets.UTF_8);

        InputStream inputStream = new ByteArrayInputStream(fileContentBytes);
        try {
            DataUtil.load(inputStream, null, "/");
            assertThrows(IllegalArgumentException.class, () -> DataUtil.load(inputStream, null, "/"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadDocumentFromInputStreamWithAutomaticCharsetDetection() {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body><p>Hello World</p></body></html>";
        String nonUtf8Content = "<html><head><meta charset=\"ISO-8859-1\"></head><body><p>Hello World</p></body></html>";
        byte[] fileContentBytes = fileContent.getBytes(StandardCharsets.UTF_8);
        byte[] nonUtf8ContentBytes = nonUtf8Content.getBytes(StandardCharsets.UTF_8);

        InputStream inputStream = new ByteArrayInputStream(fileContentBytes);
        Document document = null;
        try {
            document = DataUtil.load(inputStream, null, "/");
            assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><p>Hello World</p></body></html>", document.html());
            inputStream = new ByteArrayInputStream(nonUtf8ContentBytes);
            document = DataUtil.load(inputStream, null, "/");
            assertNotNull(document);
            assertEquals("<html><head><meta charset=\"ISO-8859-1\"></head><body><p>Hello World</p></body></html>", document.html());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseByteData() {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body><p>Hello World</p></body></html>";
        byte[] fileContentBytes = fileContent.getBytes(StandardCharsets.UTF_8);

        ByteBuffer byteData = ByteBuffer.wrap(fileContentBytes);
        Document document = null;
        String charsetName = "UTF-8";
        String baseUri = "/";
        Parser parser = Parser.htmlParser();
        try {
            document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
            assertNotNull(document);
            assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><p>Hello World</p></body></html>", document.html());
        } finally {
            byteData.clear();
        }
    }

    @Test
    public void testParseByteDataWithDetectedCharset() {
        String fileContent = "<html><head><meta charset=\"ISO-8859-1\"></head><body><p>Hello World</p></body></html>";
        byte[] fileContentBytes = fileContent.getBytes(StandardCharsets.UTF_8);

        ByteBuffer byteData = ByteBuffer.wrap(fileContentBytes);
        String detectedCharset = "ISO-8859-1";
        Document document = null;
        String baseUri = "/";
        Parser parser = Parser.htmlParser();
        try {
            document = DataUtil.parseByteData(byteData, null, baseUri, parser);
            assertNotNull(document);
            assertEquals("<html><head><meta charset=\"ISO-8859-1\"></head><body><p>Hello World</p></body></html>", document.html());
            assertEquals(detectedCharset, document.outputSettings().charset());
        } finally {
            byteData.clear();
        }
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body><p>Hello World</p></body></html>";
        File testFile = new File("test.html");
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileInputStream inputStream = new FileInputStream(testFile);
        ByteBuffer byteData = null;
        try {
            byteData = DataUtil.readToByteBuffer(inputStream, 0);
            assertNotNull(byteData);
            assertEquals(testFile.length(), byteData.limit());
        } finally {
            inputStream.close();
            testFile.delete();
        }
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body><p>Hello World</p></body></html>";
        File testFile = new File("test.html");
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileInputStream inputStream = new FileInputStream(testFile);
        ByteBuffer byteData = null;
        try {
            byteData = DataUtil.readToByteBuffer(inputStream, testFile.length() / 2);
            assertNotNull(byteData);
            assertEquals(testFile.length() / 2, byteData.limit());
        } finally {
            inputStream.close();
            testFile.delete();
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);

        contentType = "text/html; charset=EUC-JP";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("EUC-JP", charset);

        contentType = "text/html; charset=EUC-JP";
        charset = DataUtil.getCharsetFromContentType(contentType.toUpperCase());
        assertEquals("EUC-JP", charset);
    }
}