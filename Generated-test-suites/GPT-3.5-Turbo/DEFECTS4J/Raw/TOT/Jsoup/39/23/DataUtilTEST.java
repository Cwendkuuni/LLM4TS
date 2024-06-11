
package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() {
        try {
            File file = new File("test.html");
            Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
            assertNotNull(doc);
            // TODO: Add assertions for the expected content of the document
        } catch (IOException e) {
            fail("Failed to load file");
        }
    }

    @Test
    public void testLoadFromInputStream() {
        try {
            String html = "<html><head><title>Test</title></head><body><h1>Hello, world!</h1></body></html>";
            InputStream inputStream = new ByteArrayInputStream(html.getBytes());
            Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
            assertNotNull(doc);
            // TODO: Add assertions for the expected content of the document
        } catch (IOException e) {
            fail("Failed to load input stream");
        }
    }

    @Test
    public void testLoadFromInputStreamWithAlternateParser() {
        try {
            String xml = "<root><element>Test</element></root>";
            InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
            Parser parser = Parser.xmlParser();
            Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com", parser);
            assertNotNull(doc);
            // TODO: Add assertions for the expected content of the document
        } catch (IOException e) {
            fail("Failed to load input stream with alternate parser");
        }
    }

    @Test
    public void testParseByteDataWithCharsetFromMeta() {
        try {
            String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body><h1>Hello, world!</h1></body></html>";
            InputStream inputStream = new ByteArrayInputStream(html.getBytes());
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
            Parser parser = Parser.htmlParser();
            String charsetName = null;
            Document doc = DataUtil.parseByteData(byteData, charsetName, "http://example.com", parser);
            assertNotNull(doc);
            assertEquals("UTF-8", doc.outputSettings().charset().displayName());
            // TODO: Add assertions for the expected content of the document
        } catch (IOException e) {
            fail("Failed to parse byte data with charset from meta");
        }
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        try {
            String html = "<html><head><title>Test</title></head><body><h1>Hello, world!</h1></body></html>";
            InputStream inputStream = new ByteArrayInputStream(html.getBytes());
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
            Parser parser = Parser.htmlParser();
            String charsetName = "UTF-8";
            Document doc = DataUtil.parseByteData(byteData, charsetName, "http://example.com", parser);
            assertNotNull(doc);
            assertEquals("UTF-8", doc.outputSettings().charset().displayName());
            // TODO: Add assertions for the expected content of the document
        } catch (IOException e) {
            fail("Failed to parse byte data with specified charset");
        }
    }

    @Test
    public void testReadToByteBufferUnlimitedSize() {
        try {
            String html = "<html><head><title>Test</title></head><body><h1>Hello, world!</h1></body></html>";
            InputStream inputStream = new ByteArrayInputStream(html.getBytes());
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);
            assertNotNull(byteBuffer);
            String content = new String(byteBuffer.array());
            assertTrue(content.contains("<html>"));
            assertTrue(content.contains("<h1>Hello, world!</h1>"));
        } catch (IOException e) {
            fail("Failed to read input stream to byte buffer with unlimited size");
        }
    }

    @Test
    public void testReadToByteBufferCappedSize() {
        try {
            String html = "<html><head><title>Test</title></head><body><h1>Hello, world!</h1></body></html>";
            InputStream inputStream = new ByteArrayInputStream(html.getBytes());
            int maxSize = 100; // Set a small max size to test the capability of capping the byte buffer
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
            assertNotNull(byteBuffer);
            String content = new String(byteBuffer.array());
            assertTrue(content.contains("<html>"));
            assertTrue(content.contains("<h1>Hello, world!</h1>"));
            assertTrue(content.length() <= maxSize);
        } catch (IOException e) {
            fail("Failed to read input stream to byte buffer with capped size");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html; charset=EUC-JP";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("EUC-JP", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);

        contentType = null;
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}
