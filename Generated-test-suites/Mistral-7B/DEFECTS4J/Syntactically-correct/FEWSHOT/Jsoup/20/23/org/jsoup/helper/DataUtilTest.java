package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser.HtmlParser;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><meta charset='UTF-8'></head><body><div>Hello World</div></body></html>";
        File testFile = new File("testFile.html");
        FileUtils.writeStringToFile(testFile, fileContent, "UTF-8");

        Document doc = DataUtil.load(testFile, "UTF-8", "/testBase");
        assertNotNull(doc);
        assertEquals("<html><head><meta charset='UTF-8'></head><body><div>Hello World</div></body></html>", doc.html());

        FileUtils.deleteQuietly(testFile);
    }

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        String fileContent = "<html><head><meta charset='UTF-8'></head><body><div>Hello World</div></body></html>";
        File testFile = new File("testFile.html");
        FileUtils.writeStringToFile(testFile, fileContent, null);

        Document doc = DataUtil.load(testFile, null, "/testBase");
        assertNotNull(doc);
        assertEquals("<html><head><meta charset='UTF-8'></head><body><div>Hello World</div></body></html>", doc.html());

        FileUtils.deleteQuietly(testFile);
    }

    @Test
    public void testLoadFileWithDifferentCharset() throws IOException {
        String fileContent = "<html><head><meta charset='ISO-8859-1'></head><body><div>Hello World</div></body></html>";
        File testFile = new File("testFile.html");
        FileUtils.writeStringToFile(testFile, fileContent, "UTF-8");

        Document doc = DataUtil.load(testFile, null, "/testBase");
        assertNotNull(doc);
        assertEquals("<html><head><meta charset='ISO-8859-1'></head><body><div>Hello World</div></body></html>", doc.html());

        FileUtils.deleteQuietly(testFile);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><meta charset='UTF-8'></head><body><div>Hello World</div></body></html>";

        InputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        Document doc = DataUtil.load(testInputStream, "UTF-8", "/testBase");
        assertNotNull(doc);
        assertEquals("<html><head><meta charset='UTF-8'></head><body><div>Hello World</div></body></html>", doc.html());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String inputStreamContent = "<html><head><meta charset='UTF-8'></head><body><div>Hello World</div></body></html>";

        InputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        Document doc = DataUtil.load(testInputStream, null, "/testBase");
        assertNotNull(doc);
        assertEquals("<html><head><meta charset='UTF-8'></head><body><div>Hello World</div></body></html>", doc.html());
    }

    @Test
    public void testLoadInputStreamWithDifferentCharset() throws IOException {
        String inputStreamContent = "<html><head><meta charset='ISO-8859-1'></head><body><div>Hello World</div></body></html>";

        InputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        Document doc = DataUtil.load(testInputStream, null, "/testBase");
        assertNotNull(doc);
        assertEquals("<html><head><meta charset='ISO-8859-1'></head><body><div>Hello World</div></body></html>", doc.html());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputStreamContent = "<html xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:xhtml='http://www.w3.org/1999/xhtml' xml:lang='en' xmlns='http://www.w3.org/1999/xhtml'>\n<head>\n<meta http-equiv='Content-Type' content='application/xhtml+xml; charset=UTF-8'>\n<title>Test Page</title>\n</head>\n<body>\n<div>\n<p>Hello World</p>\n</div>\n</body>\n</html>";

        InputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));
        Parser parser = mock(HtmlParser.class);

        when(parser.parseInput(anyString(), anyString())).thenReturn(new Document());

        Document doc = DataUtil.load(testInputStream, "UTF-8", "/testBase", parser);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithIOException() throws IOException {
        File testFile = new File("testFile.html");

        Document doc = DataUtil.load(testFile, null, "/testBase");
        assertNull(doc);
    }

    @Test
    public void testParseByteData() throws IOException {
        String byteDataContent = "<html><head><meta charset='UTF-8'></head><body><div>Hello World</div></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(byteDataContent.getBytes("UTF-8"));

        String expectedCharset = "UTF-8";
        String baseUri = "/testBase";
        Parser parser = mock(HtmlParser.class);

        Document doc = DataUtil.parseByteData(byteData, expectedCharset, baseUri, parser);
        assertNotNull(doc);
        assertEquals("<html><head><meta charset='UTF-8'></head><body><div>Hello World</div></body></html>", doc.html());
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String byteDataContent = "<html><head><meta charset='UTF-8'></head><body><div>Hello World</div></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(byteDataContent.getBytes());

        String expectedCharset = null;
        String baseUri = "/testBase";
        Parser parser = mock(HtmlParser.class);

        Document doc = DataUtil.parseByteData(byteData, expectedCharset, baseUri, parser);
        assertNotNull(doc);
        assertEquals("<html><head><meta charset='UTF-8'></head><body><div>Hello World</div></body></html>", doc.html());
    }

    @Test
    public void testParseByteDataWithDifferentCharset() throws IOException {
        String byteDataContent = "<html><head><meta charset='ISO-8859-1'></head><body><div>Hello World</div></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(byteDataContent.getBytes("UTF-8"));

        String expectedCharset = "ISO-8859-1";
        String baseUri = "/testBase";
        Parser parser = mock(HtmlParser.class);

        Document doc = DataUtil.parseByteData(byteData, expectedCharset, baseUri, parser);
        assertNotNull(doc);
        assertEquals("<html><head><meta charset='ISO-8859-1'></head><body><div>Hello World</div></body></html>", doc.html());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputStreamContent = "Hello World";
        InputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        ByteBuffer byteData = DataUtil.readToByteBuffer(testInputStream);
        byte[] expectedContent = inputStreamContent.getBytes("UTF-8");

        byte[] byteArray = new byte[expectedContent.length];
        byteData.get(byteArray);

        assertArrayEquals(expectedContent, byteArray);
        byteData.rewind();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNull() {
        String contentType = null;
        String expectedCharset = null;

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeInvalid() {
        String contentType = "text/html; charset=invalid";
        String expectedCharset = null;

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, charset);
    }
}