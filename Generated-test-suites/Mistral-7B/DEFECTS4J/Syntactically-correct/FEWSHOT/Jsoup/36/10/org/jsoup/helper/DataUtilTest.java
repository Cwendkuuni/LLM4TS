package org.jsoup.helper;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.nodes.ParsingError;
import org.jsoup.parser.Parser.UrlResolver;
import org.jsoup.parser.Parser.UrlResolverDefault;
import org.jsoup.parser.Parser.ParserConfigurationException;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException, ParserConfigurationException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Hello World</h1></body></html>";
        File tempFile = File.createTempFile("test", null);
        FileOutputStream outputStream = new FileOutputStream(tempFile);
        outputStream.write(fileContent.getBytes());
        outputStream.close();

        Document doc = DataUtil.load(tempFile, "UTF-8", null);
        assertNotNull(doc);
        assertFalse(doc.outputSettings().baseUri().isAbsolute());
        assertEquals("Hello World", doc.title());

        doc = DataUtil.load(tempFile, null, null);
        assertNotNull(doc);
        assertFalse(doc.outputSettings().baseUri().isAbsolute());
        assertEquals("Hello World", doc.title());

        doc = DataUtil.load(tempFile, null, "/path/to/base");
        assertNotNull(doc);
        assertEquals("/path/to/base", doc.outputSettings().baseUri().toString());
        assertEquals("Hello World", doc.title());

        tempFile.delete();
    }

    @Test
    public void testLoadInputStream() throws IOException, ParserConfigurationException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());

        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(doc);
        assertFalse(doc.outputSettings().baseUri().isAbsolute());
        assertEquals("Hello World", doc.title());

        doc = DataUtil.load(inputStream, null, null);
        assertNotNull(doc);
        assertFalse(doc.outputSettings().baseUri().isAbsolute());
        assertEquals("Hello World", doc.title());

        doc = DataUtil.load(inputStream, null, "/path/to/base");
        assertNotNull(doc);
        assertEquals("/path/to/base", doc.outputSettings().baseUri().toString());
        assertEquals("Hello World", doc.title());
    }

    @Test
    public void testParseByteData() throws IOException, ParserConfigurationException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(fileContent.getBytes());

        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertNotNull(doc);
        assertFalse(doc.outputSettings().baseUri().isAbsolute());
        assertEquals("Hello World", doc.title());

        doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertNotNull(doc);
        assertFalse(doc.outputSettings().baseUri().isAbsolute());
        assertEquals("Hello World", doc.title());

        doc = DataUtil.parseByteData(byteData, null, "/path/to/base", Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("/path/to/base", doc.outputSettings().baseUri().toString());
        assertEquals("Hello World", doc.title());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(fileContent.getBytes().length, byteData.limit());

        byteData = DataUtil.readToByteBuffer(inputStream, 10);
        assertEquals(fileContent.getBytes().length, byteData.limit());

        inputStream = new ByteArrayInputStream(new byte[0]);
        byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(0, byteData.limit());

        inputStream = new ByteArrayInputStream(new byte[0]);
        byteData = DataUtil.readToByteBuffer(inputStream, 10);
        assertEquals(0, byteData.limit());

        inputStream = new ByteArrayInputStream(new byte[5]);
        byteData = DataUtil.readToByteBuffer(inputStream, 10);
        assertEquals(5, byteData.limit());

        inputStream = new ByteArrayInputStream(new byte[5]);
        byteData = DataUtil.readToByteBuffer(inputStream, 15);
        assertEquals(5, byteData.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=gb2312";
        assertEquals("gb2312", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=ISO-8859-1";
        assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=euc-jp";
        assertEquals("euc-jp", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=Shift_JIS";
        assertEquals("Shift_JIS", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=Big5";
        assertEquals("Big5", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testLoadWithError() throws IOException, ParserConfigurationException, IOException {
        String fileContent = "<html><body><h1>Hello<script>throw new Error(\"Boom\")</script>World</body></html>";
        File tempFile = File.createTempFile("test", null);
        FileOutputStream outputStream = new FileOutputStream(tempFile);
        outputStream.write(fileContent.getBytes());
        outputStream.close();

        try {
            Document doc = DataUtil.load(tempFile, null, null);
            fail("Expected ParsingError");
        } catch (ParsingError e) {
            // expected
        }

        tempFile.delete();
    }

    @Test
    public void testLoadWithEmptyContent() throws IOException, ParserConfigurationException {
        String fileContent = "";
        File tempFile = File.createTempFile("test", null);
        FileOutputStream outputStream = new FileOutputStream(tempFile);
        outputStream.write(fileContent.getBytes());
        outputStream.close();

        Document doc = DataUtil.load(tempFile, null, null);
        assertNotNull(doc);
        assertFalse(doc.outputSettings().baseUri().isAbsolute());
        assertTrue(doc.bodyElements().isEmpty());

        tempFile.delete();
    }

    @Test
    public void testLoadWithBOM() throws IOException, ParserConfigurationException {
        String fileContent = "\uFEFF<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Hello World</h1></body></html>";
        File tempFile = File.createTempFile("test", null);
        FileOutputStream outputStream = new FileOutputStream(tempFile);
        outputStream.write(fileContent.getBytes());
        outputStream.close();

        Document doc = DataUtil.load(tempFile, null, null);
        assertNotNull(doc);
        assertFalse(doc.outputSettings().baseUri().isAbsolute());
        assertEquals("Hello World", doc.title());

        tempFile.delete();
    }

    @Test
    public void testLoadWithBOMAndCharset() throws IOException, ParserConfigurationException {
        String fileContent = "\uFEFF<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Hello World</h1></body></html>";
        File tempFile = File.createTempFile("test", null);
        FileOutputStream outputStream = new FileOutputStream(tempFile);
        outputStream.write(fileContent.getBytes());
        outputStream.close();

        Document doc = DataUtil.load(tempFile, "UTF-8", null);
        assertNotNull(doc);
        assertFalse(doc.outputSettings().baseUri().isAbsolute());
        assertEquals("Hello World", doc.title());

        tempFile.delete();
    }

    @Test
    public void testLoadWithBOMAndCharsetAndBaseUri() throws IOException, ParserConfigurationException {
        String fileContent = "\uFEFF<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Hello World</h1></body></html>";
        File tempFile = File.createTempFile("test", null);
        FileOutputStream outputStream = new FileOutputStream(tempFile);
        outputStream.write(fileContent.getBytes());
        outputStream.close();

        Document doc = DataUtil.load(tempFile, "UTF-8", "/path/to/base");
        assertNotNull(doc);
        assertEquals("/path/to/base", doc.outputSettings().baseUri().toString());
        assertEquals("Hello World", doc.title());

        tempFile.delete();
    }

    @Test
    public void testLoadWithInvalidCharset() throws IOException, ParserConfigurationException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Hello World</h1></body></html>";
        File tempFile = File.createTempFile("test", null);
        FileOutputStream outputStream = new FileOutputStream(tempFile);
        outputStream.write(fileContent.getBytes());
        outputStream.close();

        try {
            Document doc = DataUtil.load(tempFile, "INVALID_CHARSET", null);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }

        tempFile.delete();
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException, ParserConfigurationException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(fileContent.getBytes());

        try {
            Document doc = DataUtil.parseByteData(byteData, "INVALID_CHARSET", null, Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testReadToByteBufferWithInvalidStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 10);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }

        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 10, new UrlResolverDefault());
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testReadToByteBufferWithNullStream() throws IOException {
        ByteBuffer byteData = DataUtil.readToByteBuffer(null);
        assertEquals(0, byteData.limit());
    }

    @Test
    public void testReadToByteBufferWithNullMaxSize() throws IOException {
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(new byte[10]), 0);
        assertEquals(10, byteData.limit());
    }

    @Test
    public void testReadToByteBufferWithNonZeroMaxSize() throws IOException {
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(new byte[10]), 5);
        assertEquals(5, byteData.limit());
    }
}