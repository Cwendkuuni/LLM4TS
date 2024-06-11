package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></meta></head><body>Hello World</body></html>";
        File tempFile = File.createTempFile("testDataUtil", null);
        FileWriter fileWriter = new FileWriter(tempFile);
        fileWriter.write(fileContent);
        fileWriter.close();

        Document document = DataUtil.load(tempFile, "UTF-8", null);
        assertNotNull(document);
        assertEquals("Hello World", document.body().text());

        tempFile.delete();
    }

    @Test
    public void testLoadFileWithNullCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></meta></head><body>Hello World</body></html>";
        File tempFile = File.createTempFile("testDataUtil", null);
        FileWriter fileWriter = new FileWriter(tempFile);
        fileWriter.write(fileContent);
        fileWriter.close();

        Document document = DataUtil.load(tempFile, null, null);
        assertNotNull(document);
        assertEquals("Hello World", document.body().text());

        tempFile.delete();
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></meta></head><body>Hello World</body></html>";
        File tempFile = File.createTempFile("testDataUtil", null);
        FileWriter fileWriter = new FileWriter(tempFile);
        fileWriter.write(fileContent);
        fileWriter.close();

        Document document = DataUtil.load(tempFile, "ISO-8859-1", null);
        assertNotNull(document);
        assertEquals("Hello World", document.body().text());

        tempFile.delete();
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></meta></head><body>Hello World</body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(document);
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></meta></head><body>Hello World</body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, null, null);
        assertNotNull(document);
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></meta></head><body>Hello World</body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, "ISO-8859-1", null);
        assertNotNull(document);
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></meta></head><body>Hello World</body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
        assertNotNull(document);
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testParseByteDataWithValidCharset() throws IOException {
        String documentContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></meta></head><body>Hello World</body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(documentContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testParseByteDataWithNullCharset() throws IOException {
        String documentContent = "<html><head></head><body>Hello World</body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(documentContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String documentContent = "<html><head></head><body>Hello World</body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(documentContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseByteData(byteBuffer, "ISO-8859-1", null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></meta></head><body>Hello World</body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        byte[] expectedByteArray = inputStreamContent.getBytes(StandardCharsets.UTF_8);
        byte[] actualByteArray = new byte[byteBuffer.limit()];
        byteBuffer.get(actualByteArray);

        assertArrayEquals(expectedByteArray, actualByteArray);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></meta></head><body>Hello World</body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 10);
        byte[] expectedByteArray = new byte[10];
        byteBuffer.get(expectedByteArray);

        assertArrayEquals(expectedByteArray, new byte[10]); // verify the byte array is empty
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);

        contentType = "text/html; charset=UTF-8, foo=bar";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html; charset=ISO-8859-1, foo=bar";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("ISO-8859-1", charset);
    }
}