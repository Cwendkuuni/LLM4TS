package org.jsoup.helper;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoad() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertNotNull(doc.head());
        assertEquals("UTF-8", doc.head().outputSettings().charset());
        assertEquals("html", doc.tagName());

        File file = new File("test.html");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(html);
        }
        doc = DataUtil.load(file, "UTF-8", null);
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());

        // Test with null charsetName
        doc = DataUtil.load(file, null, null);
        assertEquals("UTF-8", doc.outputSettings().charset());

        // Test with unsupported charsetName
        try {
            doc = DataUtil.load(file, "GBK", null);
            assertNull(doc);
        } catch (IOException e) {
            // Expected
        }

        // Test with empty file
        File emptyFile = new File("empty.html");
        doc = DataUtil.load(emptyFile, "UTF-8", null);
        assertNull(doc);
    }

    @Test
    public void testParseByteData() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());

        // Test with no charsetName
        byteData = DataUtil.readToByteBuffer(inputStream);
        doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());

        // Test with unsupported charsetName
        String unsupportedHtml = "<html><head><meta charset=\"GBK\"></head><body><h1>Hello World</h1></body></html>";
        ByteArrayInputStream unsupportedInputStream = new ByteArrayInputStream(unsupportedHtml.getBytes());
        byteData = DataUtil.readToByteBuffer(unsupportedInputStream);
        assertNull(DataUtil.parseByteData(byteData, "GBK", null, Parser.htmlParser()));

        // Test with spurious byte order mark
        String bomHtml = "<html>\uFEFF<head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        ByteArrayInputStream bomInputStream = new ByteArrayInputStream(bomHtml.getBytes());
        byteData = DataUtil.readToByteBuffer(bomInputStream);
        Document docWithBom = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertNotNull(docWithBom);
        assertTrue(docWithBom.text().startsWith("Hello World"));
        assertFalse(docWithBom.text().startsWith("\uFEFFHello World"));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=EUC-JP";
        assertEquals("EUC-JP", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=gb2312";
        assertEquals("gb2312", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        byte[] expectedBytes = html.getBytes();
        byte[] actualBytes = new byte[byteData.limit()];
        byteData.get(actualBytes);
        assertArrayEquals(expectedBytes, actualBytes);

        // Test with maxSize > 0
        byteData = DataUtil.readToByteBuffer(inputStream, 10);
        byte[] expectedBytes10 = Arrays.copyOfRange(expectedBytes, 0, 10);
        byte[] actualBytes10 = new byte[byteData.limit()];
        byteData.get(actualBytes10);
        assertArrayEquals(expectedBytes10, actualBytes10);

        // Test with maxSize = 0
        byteData = DataUtil.readToByteBuffer(inputStream, 0);
        byte[] expectedBytes0 = html.getBytes();
        byte[] actualBytes0 = new byte[byteData.limit()];
        byteData.get(actualBytes0);
        assertArrayEquals(expectedBytes0, actualBytes0);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadWithFileNotFound() throws IOException {
        File file = new File("nonExistentFile.html");
        DataUtil.load(file, null, null);
    }
}