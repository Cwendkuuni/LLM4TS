package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.helper.DataUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadDocumentFromInputStream() throws IOException {
        String xmlContent = "<html><head><meta charset='UTF-8'></head><body>Hello World!</body></html>";
        InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(document);
        assertEquals("Hello World!", document.body().text());
    }

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        String fileName = "test.html";
        String fileContent = "<html><head><meta charset='UTF-8'></head><body>Hello World!</body></html>";
        writeFile(fileName, fileContent);

        Document document = DataUtil.load(new File(fileName), "UTF-8", null);
        assertNotNull(document);
        assertEquals("Hello World!", document.body().text());

        deleteFile(fileName);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String xmlContent = "<html><head><meta charset='UTF-8'></head><body>Hello World!</body></html>";
        InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("Hello World!", document.body().text());

        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithCharsetDetectedFromContentType() throws IOException {
        String xmlContent = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'><body>Hello World!</body></html>";
        InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("Hello World!", document.body().text());

        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithCustomParser() throws IOException {
        String xmlContent = "<html xmlns='http://www.w3.org/1999/xhtml'><head><meta charset='UTF-8'></head><body>Hello World!</body></html>";
        InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.xmlParser());
        assertNotNull(document);
        assertEquals("Hello World!", document.body().text());

        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String xmlContent = "<html><head><meta charset='x-invalid'></head><body>Hello World!</body></html>";
        InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseInputStream(inputStream, "x-invalid", null, Parser.htmlParser());
        assertNull(document);

        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithBOM() throws IOException {
        String xmlContent = "\uFEFF<html><head><meta charset='UTF-8'></head><body>Hello World!</body></html>";
        InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("Hello World!", document.body().text());

        inputStream.close();
    }

    private void writeFile(String fileName, String content) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
             FileOutputStream outputStream = new FileOutputStream(fileName)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer, 0, buffer.length)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        }
    }

    private void deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists() && file.delete()) {
            System.out.println("Deleted file: " + fileName);
        } else {
            System.out.println("Failed to delete file: " + fileName);
        }
    }
}