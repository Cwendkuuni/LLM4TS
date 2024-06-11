package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws Exception {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></meta></head><body><h1>Hello World</h1></body></html>";
        File testFile = new File("testFile.html");
        FileUtils.writeStringToFile(testFile, fileContent, "UTF-8");

        Document doc = DataUtil.load(testFile, null, null);
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());
        FileUtils.deleteQuietly(testFile);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws Exception {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></meta></head><body><h1>Hello World</h1></body></html>";
        File testFile = new File("testFile.html");
        FileUtils.writeStringToFile(testFile, fileContent, "UTF-8");

        Document doc = DataUtil.load(testFile, "ISO-8859-1", null);
        assertNotNull(doc);
        assertEquals("ISO-8859-1", doc.outputSettings().charset());
        FileUtils.deleteQuietly(testFile);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws Exception {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></meta></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes("UTF-8"));

        Document doc = DataUtil.load(inputStream, null, null);
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws Exception {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></meta></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes("UTF-8"));

        Document doc = DataUtil.load(inputStream, "ISO-8859-1", null);
        assertNotNull(doc);
        assertEquals("ISO-8859-1", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedParser() throws Exception {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></meta></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes("UTF-8"));

        Document doc = DataUtil.load(inputStream, null, null, Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws Exception {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></meta></head><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(fileContent.getBytes("UTF-8"));

        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws Exception {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></meta></head><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(fileContent.getBytes("UTF-8"));

        Document doc = DataUtil.parseByteData(byteData, "ISO-8859-1", null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("ISO-8859-1", doc.outputSettings().charset());
    }

    @Test
    public void testDetectCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String detectedCharset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", detectedCharset);
    }

    @Test
    public void testDetectCharsetFromContentTypeNullContentType() {
        String detectedCharset = DataUtil.getCharsetFromContentType(null);
        assertNull(detectedCharset);
    }
}