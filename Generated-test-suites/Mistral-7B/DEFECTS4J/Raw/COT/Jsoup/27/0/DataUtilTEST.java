package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
import org.jsoup.parser.Parser;

import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        File file = new File("testFile.html");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(fileContent);
        fileWriter.close();

        Document expectedDocument = Jsoup.parse(fileContent);

        Document actualDocument = DataUtil.load(file, "UTF-8", null);
        assertEquals(expectedDocument.toString(), actualDocument.toString());

        file.delete();
    }

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        File file = new File("testFile.html");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(fileContent);
        fileWriter.close();

        Document actualDocument = DataUtil.load(file, null, null);
        assertEquals(expectedDocumentToString(fileContent), actualDocument.toString());

        file.delete();
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        File file = new File("testFile.html");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(fileContent);
        fileWriter.close();

        Document actualDocument = null;
        try {
            actualDocument = DataUtil.load(file, "ISO-8859-1", null);
        } catch (IOException e) {
            //Expected exception
        }

        assertNull(actualDocument);

        file.delete();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        Document expectedDocument = Jsoup.parse(inputStreamContent);

        Document actualDocument = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals(expectedDocument.toString(), actualDocument.toString());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";

        Document actualDocument = DataUtil.load(createInputStreamFromString(inputStreamContent), null, null);
        assertEquals(expectedDocumentToString(inputStreamContent), actualDocument.toString());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";

        Document actualDocument = null;
        try {
            actualDocument = DataUtil.load(createInputStreamFromString(inputStreamContent), "ISO-8859-1", null);
        } catch (IOException e) {
            //Expected exception
        }

        assertNull(actualDocument);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        Parser parser = mock(Parser.class);
        when(parser.parseInput(anyString(), eq(null))).thenReturn(Jsoup.parse(inputStreamContent));

        Document expectedDocument = Jsoup.parse(inputStreamContent);

        Document actualDocument = DataUtil.load(inputStream, "UTF-8", null, parser);
        assertEquals(expectedDocument.toString(), actualDocument.toString());
    }

    @Test
    public void testParseByteData() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        byte[] byteData = fileContent.getBytes();

        Document expectedDocument = Jsoup.parse(fileContent);

        Document actualDocument = DataUtil.parseByteData(ByteBuffer.wrap(byteData), "UTF-8", null, Parser.htmlParser());
        assertEquals(expectedDocument.toString(), actualDocument.toString());
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        byte[] byteData = fileContent.getBytes();

        Document actualDocument = DataUtil.parseByteData(ByteBuffer.wrap(byteData), null, null, Parser.htmlParser());
        assertEquals(expectedDocumentToString(fileContent), actualDocument.toString());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        byte[] byteData = fileContent.getBytes();

        Document actualDocument = null;
        try {
            actualDocument = DataUtil.parseByteData(ByteBuffer.wrap(byteData), "ISO-8859-1", null, Parser.htmlParser());
        } catch (IOException e) {
            //Expected exception
        }

        assertNull(actualDocument);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        InputStream in = inputStream;
        try {
            DataUtil.readToByteBuffer(in);
            ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
            assertEquals(inputStream.available(), byteData.limit());
        } finally {
            in.close();
            outputStream.close();
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
    }

    private InputStream createInputStreamFromString(String string) {
        return new ByteArrayInputStream(string.getBytes());
    }

    private String expectedDocumentToString(String input) {
        return Jsoup.parse(input).toString();
    }
}