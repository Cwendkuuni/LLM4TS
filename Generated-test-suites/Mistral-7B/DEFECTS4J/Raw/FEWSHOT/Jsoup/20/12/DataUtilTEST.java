package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
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
    public void testLoadDocumentFromFileWithDefaultCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></meta></head><body>Test</body></html>";
        File file = new File("test.html");
        file.writeText(fileContent);

        Document document = DataUtil.load(file, null, null);
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
        file.delete(); // clean up
    }

    @Test
    public void testLoadDocumentFromFileWithSpecifiedCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></meta></head><body>Test</body></html>";
        File file = new File("test.html");
        file.writeText(fileContent);

        Document document = DataUtil.load(file, "ISO-8859-1", null);
        assertNotNull(document);
        assertEquals("ISO-8859-1", document.outputSettings().charset());
        file.delete(); // clean up
    }

    @Test
    public void testLoadDocumentFromInputStreamWithDefaultCharset() throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></meta></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());

        Document document = DataUtil.load(inputStream, null, null);
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testLoadDocumentFromInputStreamWithSpecifiedCharset() throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></meta></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());

        Document document = DataUtil.load(inputStream, "ISO-8859-1", null);
        assertNotNull(document);
        assertEquals("ISO-8859-1", document.outputSettings().charset());
    }

    @Test
    public void testDetectCharsetFromInputStream() throws IOException {
        String contentWithUtf8Bom = "\uFEFF<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></meta></head><body>Test</body></html>";
        String contentWithGbkBom = "\uEFBB\uBFBF<?xml version=\"1.0\" encoding=\"GBK\"?>\n<html><head><meta charset=\"GBK\"></meta></head><body>Test</body></html>";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bufferSize);
        ByteBuffer byteData;

        InputStream inputStreamWithUtf8Bom = new ByteArrayInputStream(contentWithUtf8Bom.getBytes());
        byteData = DataUtil.readToByteBuffer(inputStreamWithUtf8Bom);
        String charset = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser()).outputSettings().charset();
        assertEquals("UTF-8", charset);

        InputStream inputStreamWithGbkBom = new ByteArrayInputStream(contentWithGbkBom.getBytes());
        byteData = DataUtil.readToByteBuffer(inputStreamWithGbkBom);
        charset = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser()).outputSettings().charset();
        assertEquals("GBK", charset);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></meta></head><body>Test</body></html>";

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(byteArrayInputStream);

        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></meta></head><body>Test</body></html>";

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(byteArrayInputStream);

        Document document = DataUtil.parseByteData(byteData, "ISO-8859-1", null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("ISO-8859-1", document.outputSettings().charset());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String contentType2 = "text/html; charset=GBK";
        String contentType3 = "text/html";

        String charset1 = DataUtil.getCharsetFromContentType(contentType1);
        assertEquals("UTF-8", charset1);

        String charset2 = DataUtil.getCharsetFromContentType(contentType2);
        assertEquals("GBK", charset2);

        String charset3 = DataUtil.getCharsetFromContentType(contentType3);
        assertNull(charset3);
    }

    @Test
    public void testLoadDocumentWithMalformedContentTypeHeader() throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"text/html; charset=UTF-8\"></meta></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());

        try {
            DataUtil.load(inputStream, null, null);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }
}