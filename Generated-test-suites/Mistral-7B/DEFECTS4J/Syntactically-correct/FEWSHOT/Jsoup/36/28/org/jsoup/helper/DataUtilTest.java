package org.jsoup.helper;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head></head><body><h1>Test</h1></body></html>";
        File inputFile = new File("test.html");
        inputFile.writeText(fileContent);

        Document document = DataUtil.load(inputFile, null);
        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.html());
        inputFile.delete();
    }

    @Test
    public void testLoadFileWithCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head></head><body><h1>Test</h1></body></html>";
        File inputFile = new File("test.html");
        inputFile.writeText(fileContent);

        Document document = DataUtil.load(inputFile, "UTF-8");
        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.html());
        inputFile.delete();
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head></head><body><h1>Test</h1></body></html>";
        File inputFile = new File("test.html");
        inputFile.writeText(fileContent);

        try {
            Document document = DataUtil.load(inputFile, "ISO-8859-1");
            assertFalse(true); // should throw an exception
            inputFile.delete();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        Document document = DataUtil.load(inputStream, null);
        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.html());
    }

    @Test
    public void testLoadInputStreamWithCharset() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        Document document = DataUtil.load(inputStream, "UTF-8");
        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.html());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        try {
            Document document = DataUtil.load(inputStream, "ISO-8859-1");
            assertFalse(true); // should throw an exception
            inputStream.close();
        } catch (IllegalArgumentException e) {
            // expected
            inputStream.close();
        }
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputStreamContent = "<html><head></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        Parser parser = mock(Parser.class);
        when(parser.parseInput(anyString(), anyString())).thenReturn(new Document());

        Document document = DataUtil.load(inputStream, null, "", parser);
        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.html());
        inputStream.close();
    }

    @Test
    public void testParseByteData() throws IOException {
        String charset = "UTF-8";
        String documentContent = "<html><head></head><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(documentContent.getBytes(Charset.forName(charset)));

        Document document = DataUtil.parseByteData(byteData, charset, "", Parser.htmlParser());
        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.html());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String charset = "UTF-8";
        String documentContent = "<html><head></head><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(documentContent.getBytes(Charset.forName("ISO-8859-1")));

        try {
            Document document = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
            assertFalse(true); // should throw an exception
            byteData.rewind();
        } catch (IllegalArgumentException e) {
            // expected
            byteData.rewind();
        }
    }

    @Test
    public void testParseByteDataWithMetaCharset() throws IOException {
        String charset = "GB2312";
        String documentContent = "<html><head><meta charset=\"GB2312\"><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(documentContent.getBytes(Charset.forName(charset)));

        Document document = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        assertEquals("<html><head><meta charset=\"GB2312\"><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><body><h1>Test</h1></body></html>", document.html());
        byteData.rewind();
    }

    @Test
    public void testParseByteDataWithContentTypeCharset() throws IOException {
        String charset = "UTF-8";
        String documentContent = "<html><head><meta charset=\"UTF-8\"><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(documentContent.getBytes(Charset.forName(charset)));

        Document document = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        assertEquals("<html><head><meta charset=\"UTF-8\"><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><body><h1>Test</h1></body></html>", document.html());
        byteData.rewind();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String content = "Test content";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(content, Charset.forName("UTF-8").decode(byteData).toString());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String content = "Test content";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 10);
        assertEquals(content.substring(0, 10), Charset.forName("UTF-8").decode(byteData).toString());
    }

    @Test
    public void testReadToByteBufferWithZeroMaxSize() throws IOException {
        String content = "Test content";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 0);
        assertEquals(content, Charset.forName("UTF-8").decode(byteData).toString());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=UTF-8, GB2312";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithEmptyContentType() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(DataUtil.defaultCharset, charset);
    }
}