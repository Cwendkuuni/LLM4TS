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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {
    @Test
    public void testLoad() throws IOException {
        String testData = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head></head><body><h1>Test</h1></body></html>";
        byte[] bytes = testData.getBytes();
        File tempFile = File.createTempFile("jsoup", null);
        try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            outputStream.write(bytes);
        }

        Document document = DataUtil.load(tempFile, "UTF-8", null);
        assertEquals(testData, document.html());

        try (FileInputStream inputStream = new FileInputStream(tempFile)) {
            Document document2 = DataUtil.load(inputStream, null, null);
            assertEquals(testData, document2.html());
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bytes.length)) {
            outputStream.write(bytes);
            InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            Document document3 = DataUtil.load(inputStream, "UTF-8", null, Parser.htmlParser());
            assertEquals(testData, document3.html());
        }

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes())) {
            Document document4 = DataUtil.load(inputStream, "UTF-8", null, Parser.htmlParser());
            assertEquals(testData, document4.html());
        }
    }

    @Test
    public void testLoadWithInvalidCharset() throws IOException {
        String testData = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head></head><body><h1>Test</h1></body></html>";
        byte[] bytes = testData.getBytes();

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes())) {
            Document document = null;
            try {
                document = DataUtil.load(inputStream, "ISO-8859-1", null, Parser.htmlParser());
            } catch (IllegalArgumentException e) {
                // expected
            }
            assertNull(document);
        }

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes())) {
            Document document = DataUtil.load(inputStream, null, null, Parser.htmlParser());
            assertEquals(testData, document.html());
            String charset = DataUtil.getCharsetFromContentType(document.outputSettings().contentType());
            assertEquals("UTF-8", charset);
        }
    }

    @Test
    public void testParseByteData() throws IOException {
        String testData = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head></head><body><h1>Test</h1></body></html>";
        byte[] bytes = testData.getBytes();

        ByteBuffer byteData = ByteBuffer.wrap(bytes);
        Document document = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertEquals(testData, document.html());

        byteData.rewind();
        String charset = DataUtil.getCharsetFromContentType(document.outputSettings().contentType());
        assertEquals("UTF-8", charset);

        byteData = ByteBuffer.wrap(bytes);
        byteData.position(bytes.length - 4);
        byteData.put((byte) 65279); // BOM
        document = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertEquals(testData.substring(1), document.html());

        ByteBuffer byteData2 = ByteBuffer.allocate(bytes.length + 1);
        byteData2.put(bytes);
        byteData2.put((byte) 65279); // BOM
        Document document2 = DataUtil.parseByteData(byteData2, null, null, Parser.htmlParser());
        assertEquals(testData.substring(1), document2.html());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        byte[] expected = testData.getBytes();
        byteData.flip();
        byte[] actual = new byte[byteData.remaining()];
        byteData.get(actual);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);

        contentType = "text/html; charset=ISO-8859-1";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("ISO-8859-1", charset);
    }

    @Test
    public void testCharsetPattern() {
        String contentType = "text/html; charset=UTF-8";
        Pattern pattern = DataUtil.charsetPattern;
        Matcher matcher = pattern.matcher(contentType);
        assertTrue(matcher.find());
        assertEquals("UTF-8", matcher.group(1));

        contentType = "text/html; charset=ISO-8859-1";
        matcher = pattern.matcher(contentType);
        assertTrue(matcher.find());
        assertEquals("ISO-8859-1", matcher.group(1));

        contentType = "text/html; charset=GBK";
        matcher = pattern.matcher(contentType);
        assertTrue(matcher.find());
        assertEquals("GBK", matcher.group(1));

        contentType = "text/html";
        matcher = pattern.matcher(contentType);
        assertFalse(matcher.find());
    }
}