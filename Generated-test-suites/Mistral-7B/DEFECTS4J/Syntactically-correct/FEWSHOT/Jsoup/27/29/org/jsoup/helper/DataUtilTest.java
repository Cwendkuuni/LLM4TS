package org.jsoup.helper;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoad() throws IOException {
        String fileContent = "This is a test file.";
        File file = new File("testFile.html");
        FileInputStream fileInputStream = new FileInputStream(file);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] byteArray = fileContent.getBytes();
        outputStream.write(byteArray);
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document document = DataUtil.load(file, "UTF-8", "/testBase");
        assertEquals(fileContent, document.body().html());

        document = DataUtil.load(inputStream, "UTF-8", "/testBase");
        assertEquals(fileContent, document.body().html());
    }

    @Test
    public void testLoadWithDefaultCharset() throws IOException {
        String fileContent = "This is a test file.";
        File file = new File("testFile.html");
        FileInputStream fileInputStream = new FileInputStream(file);

        Document document = DataUtil.load(file, null, "/testBase");
        assertEquals(fileContent, document.body().html());

        document = DataUtil.load(fileInputStream, null, "/testBase");
        assertEquals(fileContent, document.body().html());
    }

    @Test
    public void testLoadWithInvalidCharset() throws IOException {
        String fileContent = "This is a test file.";
        File file = new File("testFile.html");
        FileInputStream fileInputStream = new FileInputStream(file);

        try {
            Document document = DataUtil.load(file, "INVALID_CHARSET", "/testBase");
            assertNull(document);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Document document = DataUtil.load(fileInputStream, "INVALID_CHARSET", "/testBase");
            assertNull(document);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLoadWithMismatchedCharset() throws IOException {
        String fileContent = "This is a test file.";
        String invalidCharset = "ISO-8859-1";
        File file = new File("testFile.html");
        FileInputStream fileInputStream = new FileInputStream(file);

        Document document = DataUtil.load(file, invalidCharset, "/testBase");
        assertNull(document);

        document = DataUtil.load(fileInputStream, invalidCharset, "/testBase");
        assertNull(document);
    }

    @Test
    public void testParseByteData() throws IOException {
        String fileContent = "This is a test file.";
        String charset = "UTF-8";
        String baseUri = "/testBase";
        Parser parser = mock(Parser.class);

        when(parser.parseInput(eq("This is a test file."), eq("/testBase"))).thenReturn(createTestDocument());

        ByteBuffer byteData = ByteBuffer.wrap(fileContent.getBytes(Charset.forName(charset)));
        Document document = DataUtil.parseByteData(byteData, charset, baseUri, parser);
        assertEquals(fileContent, document.body().html());

        document = DataUtil.parseByteData(byteData, null, baseUri, parser);
        assertEquals(fileContent, document.body().html());
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        String fileContent = "Content-Type: text/html; charset=ISO-8859-1\n" +
                "This is a test file.";
        String baseUri = "/testBase";
        Parser parser = mock(Parser.class);

        when(parser.parseInput(eq("This is a test file."), eq("/testBase"))).thenReturn(createTestDocument());

        ByteBuffer byteData = ByteBuffer.wrap(fileContent.getBytes());
        Document document = DataUtil.parseByteData(byteData, null, baseUri, parser);
        assertEquals(fileContent, document.body().html());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String fileContent = "Content-Type: text/html; charset=UTF-8\n" +
                "This is a test file.";
        String invalidCharset = "ISO-8859-1";
        String baseUri = "/testBase";
        Parser parser = mock(Parser.class);

        when(parser.parseInput(eq("This is a test file."), eq("/testBase"))).thenReturn(createTestDocument());

        ByteBuffer byteData = ByteBuffer.wrap(fileContent.getBytes(Charset.forName("UTF-8")));
        Document document = DataUtil.parseByteData(byteData, invalidCharset, baseUri, parser);
        assertNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String fileContent = "This is a test file.";
        byte[] byteArray = fileContent.getBytes();
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        byte[] byteArrayExpected = new byte[byteData.limit()];
        byteData.get(byteArrayExpected);
        assertArrayEquals(byteArrayExpected, byteArray);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidContentType() {
        String contentType = "text/html";
        String expectedCharset = null;

        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    private Document createTestDocument() {
        Document document = new org.jsoup.nodes.Document();
        Element element = document.createElement("html");
        document.appendChild(element);
        element.appendChild(document.createElement("body"));
        return document;
    }
}