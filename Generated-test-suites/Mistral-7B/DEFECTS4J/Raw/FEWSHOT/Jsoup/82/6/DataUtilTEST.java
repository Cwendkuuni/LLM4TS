package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testParseInputStream() throws IOException {
        // Test empty input
        Document doc = DataUtil.parseInputStream(new ByteArrayInputStream(new byte[0][0]), "UTF-8", null, Parser.htmlParser());
        assertTrue(doc.titleText().isEmpty());

        // Test valid UTF-8 input
        String xmlData = "<html><head><meta charset=\"UTF-8\"></head><body><div>Test</div></body></html>";
        InputStream xmlInputStream = new ByteArrayInputStream(xmlData.getBytes("UTF-8"));
        doc = DataUtil.parseInputStream(xmlInputStream, "UTF-8", null, Parser.htmlParser());
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><div>Test</div></body></html>", doc.html());

        // Test invalid charset input
        InputStream invalidInputStream = new ByteArrayInputStream("Invalid data".getBytes("ISO-8859-1"));
        assertThrows(IOException.class, () -> DataUtil.parseInputStream(invalidInputStream, "UTF-8", null, Parser.htmlParser()));

        // Test default charset
        InputStream defaultInputStream = new ByteArrayInputStream("Default data".getBytes());
        doc = DataUtil.parseInputStream(defaultInputStream, null, null, Parser.htmlParser());
        assertEquals("Default data", doc.text());

        // Test meta charset
        String metaData = "<html><head><meta charset=\"ISO-8859-1\"></head><body><div>Meta Test</div></body></html>";
        InputStream metaInputStream = new ByteArrayInputStream(metaData.getBytes("UTF-8"));
        doc = DataUtil.parseInputStream(metaInputStream, null, null, Parser.htmlParser());
        assertEquals("Meta Test", doc.text());

        // Test XML declaration
        String xmlDeclarationData = "<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xml=\"http://www.w3.org/XML/1998/namespace\" xml:lang=\"en\" xml:base=\"https://example.com/\" version=\"1.0\" encoding=\"UTF-8\">";
        InputStream xmlDeclarationInputStream = new ByteArrayInputStream(xmlDeclarationData.getBytes("UTF-8"));
        doc = DataUtil.parseInputStream(xmlDeclarationInputStream, null, null, Parser.xmlParser());
        assertEquals("<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xml=\"http://www.w3.org/XML/1998/namespace\" xml:lang=\"en\" xml:base=\"https://example.com/\" version=\"1.0\" encoding=\"UTF-8\">", doc.html());

        // Test content-type header
        String contentTypeData = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><div>Content Type Test</div></body></html>";
        InputStream contentTypeInputStream = new ByteArrayInputStream(contentTypeData.getBytes("UTF-8"));
        doc = DataUtil.parseInputStream(contentTypeInputStream, null, null, Parser.htmlParser());
        assertEquals("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><div>Content Type Test</div></body></html>", doc.html());
    }

    @Test
    public void testLoad() throws IOException {
        // Test file loading with UTF-8 charset
        File file = new File("test_utf8.html");
        if (!file.exists()) {
            file.createNewFile();
            try (OutputStream outputStream = new FileOutputStream(file)) {
                String xmlData = "<html><head><meta charset=\"UTF-8\"></head><body><div>Test</div></body></html>";
                outputStream.write(xmlData.getBytes("UTF-8"));
            }
        }
        Document doc = DataUtil.load(file, "UTF-8", null);
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><div>Test</div></body></html>", doc.html());

        // Test file loading with invalid charset
        File invalidFile = new File("test_invalid.html");
        if (!invalidFile.exists()) {
            invalidFile.createNewFile();
            try (OutputStream outputStream = new FileOutputStream(invalidFile)) {
                String xmlData = "<html><head><meta charset=\"ISO-8859-1\"></head><body><div>Test</div></body></html>";
                outputStream.write(xmlData.getBytes("UTF-8"));
            }
        }
        assertThrows(IOException.class, () -> DataUtil.load(invalidFile, "UTF-8", null));

        // Test file loading with default charset
        File defaultFile = new File("test_default.html");
        if (!defaultFile.exists()) {
            defaultFile.createNewFile();
            try (OutputStream outputStream = new FileOutputStream(defaultFile)) {
                String xmlData = "<html><head></head><body><div>Test</div></body></html>";
                outputStream.write(xmlData.getBytes());
            }
        }
        doc = DataUtil.load(defaultFile, null, null);
        assertEquals("<html><head></head><body><div>Test</div></body></html>", doc.html());
    }

    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType("text/html; charset=ISO-8859-1"));
        assertEquals(null, DataUtil.getCharsetFromContentType("text/html"));
    }

    @Test
    public void testValidateCharset() {
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8"));
        assertEquals("UTF-16", DataUtil.validateCharset("UTF-16"));
        assertEquals("UTF-32", DataUtil.validateCharset("UTF-32"));
        assertEquals("ISO-8859-1", DataUtil.validateCharset("ISO-8859-1"));
        assertEquals("GB2312", DataUtil.validateCharset("GB2312"));
        assertEquals(null, DataUtil.validateCharset(""));
        assertEquals(null, DataUtil.validateCharset(null));
        assertEquals(null, DataUtil.validateCharset("invalid"));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertFalse(boundary.isEmpty());
        String boundary2 = DataUtil.mimeBoundary();
        assertFalse(boundary.equals(boundary2));
    }
}