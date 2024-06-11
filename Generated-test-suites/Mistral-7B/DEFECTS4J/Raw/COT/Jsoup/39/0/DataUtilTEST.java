package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;

public class DataUtilTest {

    @Test
    public void testLoadFileDocument() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        File tempFile = File.createTempFile("test", ".html");
        FileWriter writer = new FileWriter(tempFile);
        writer.write(fileContent);
        writer.close();

        Document document = DataUtil.load(tempFile, "UTF-8", null);
        assertNotNull(document);

        tempFile.delete();
    }

    @Test
    public void testLoadInputStreamDocument() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes("UTF-8"));

        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamDocumentWithDefaultCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes("UTF-8"));

        Document document = DataUtil.load(inputStream, null, null);
        assertNotNull(document);
        assertTrue(document.outputSettings().charset().equals(Charset.forName("UTF-8").name()));
    }

    @Test(expected = IOException.class)
    public void testLoadFileDocumentWithInvalidCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        File tempFile = File.createTempFile("test", ".html");
        FileWriter writer = new FileWriter(tempFile);
        writer.write(fileContent);
        writer.close();

        DataUtil.load(tempFile, "X-UTF-8", null);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamDocumentWithInvalidCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes("UTF-8"));

        DataUtil.load(inputStream, "X-UTF-8", null);
    }

    @Test
    public void testParseByteData() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent.getBytes("UTF-8"));

        Document document = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), null, null, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent.getBytes("UTF-8"));

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        byteData.position(byteData.position() + 3); // Move past the BOM

        try {
            Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
            assertNotNull(document);
            fail("Expected an IOException");
        } catch (IOException e) {
            // Expected
        }
    }
}