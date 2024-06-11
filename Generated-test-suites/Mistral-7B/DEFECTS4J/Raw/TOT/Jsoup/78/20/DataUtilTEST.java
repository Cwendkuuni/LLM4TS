package org.jsoup.helper.tests;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.DataUtil.BomCharset;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * Tests for the DataUtil class.
 */
public class DataUtilTest {

    private Parser parser;
    private Charset utf8;
    private Charset invalidCharset;

    @Before
    public void setUp() {
        parser = Parser.htmlParser();
        utf8 = Charset.forName("UTF-8");
        invalidCharset = new Charset("InvalidCharset") {
            @Override
            public String name() {
                return "InvalidCharset";
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof Charset)) return false;
                return getClass() == o.getClass();
            }

            @Override
            public int compareTo(Charset cs) {
                return -1;
            }
        };
    }

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("test.html");
        assertThrows(IOException.class, () -> DataUtil.load(file, "InvalidCharset", ""));
    }

    @Test
    public void testLoadFileWithNoCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, null, "");
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        assertNotNull(doc);
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        assertThrows(IOException.class, () -> DataUtil.load(inputStream, "InvalidCharset", ""));
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithNoCharset() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Document doc = DataUtil.load(inputStream, null, "");
        assertEquals("UTF-8", doc.outputSettings().charset());
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithBomAndValidCharset() throws IOException {
        String testDataUtf8 = "\uFEFF<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testDataUtf8.getBytes(utf8));
        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        assertNotNull(doc);
        assertEquals(testDataUtf8, doc.body().html());
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithBomAndInvalidCharset() throws IOException {
        String testDataUtf8 = "\uFEFF<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testDataUtf8.getBytes(utf8));
        assertThrows(IOException.class, () -> DataUtil.load(inputStream, "InvalidCharset", ""));
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithBomAndNoCharset() throws IOException {
        String testDataUtf8 = "\uFEFF<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testDataUtf8.getBytes(utf8));
        Document doc = DataUtil.load(inputStream, null, "");
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals(testDataUtf8, doc.body().html());
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        String testDataUtf8 = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testDataUtf8.getBytes(utf8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        ByteBuffer expectedByteBuffer = DataUtil.readFileToByteBuffer(new File("expected.html"));
        assertEquals(expectedByteBuffer, byteBuffer);
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String testDataUtf8 = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testDataUtf8.getBytes(utf8));
        assertThrows(IOException.class, () -> DataUtil.parseInputStream(inputStream, null, "", parser));
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithNoCharset() throws IOException {
        String testDataUtf8 = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testDataUtf8.getBytes(utf8));
        Document doc = DataUtil.parseInputStream(inputStream, null, "", parser);
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals(testDataUtf8, doc.body().html());
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithBomAndValidCharset() throws IOException {
        String testDataUtf8WithBom = "\uFEFF<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testDataUtf8WithBom.getBytes(utf8));
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "", parser);
        assertEquals(testDataUtf8WithBom, doc.body().html());
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithBomAndInvalidCharset() throws IOException {
        String testDataUtf8WithBom = "\uFEFF<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testDataUtf8WithBom.getBytes(utf8));
        assertThrows(IOException.class, () -> DataUtil.parseInputStream(inputStream, "InvalidCharset", "", parser));
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithBomAndNoCharset() throws IOException {
        String testDataUtf8WithBom = "\uFEFF<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testDataUtf8WithBom.getBytes(utf8));
        Document doc = DataUtil.parseInputStream(inputStream, null, "", parser);
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals(testDataUtf8WithBom, doc.body().html());
        inputStream.close();
    }

    @Test
    public void testCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));

        String contentTypeWithNoCharset = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentTypeWithNoCharset));
    }

    @Test
    public void testValidateCharset() {
        assertNull(DataUtil.validateCharset(null));
        assertNull(DataUtil.validateCharset(""));
        assertNull(DataUtil.validateCharset("UTF-8"));
        assertNull(DataUtil.validateCharset("UTF-8".toUpperCase()));

        assertNull(DataUtil.validateCharset("InvalidCharset"));
        assertNull(DataUtil.validateCharset("InvalidCharset".toUpperCase()));

        assertNotNull(DataUtil.validateCharset("ISO-8859-1"));
        assertNotNull(DataUtil.validateCharset("ISO-8859-1".toUpperCase()));
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertEquals(boundaryLength, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        ByteBuffer byteDataUtf16 = DataUtil.readFileToByteBuffer(new File("utf16.html"));
        BomCharset utf16BomCharset = DataUtil.detectCharsetFromBom(byteDataUtf16);
        assertEquals("UTF-16", utf16BomCharset.charset);
        assertEquals(0, utf16BomCharset.offset);

        ByteBuffer byteDataUtf8 = DataUtil.readFileToByteBuffer(new File("utf8.html"));
        BomCharset utf8BomCharset = DataUtil.detectCharsetFromBom(byteDataUtf8);
        assertEquals("UTF-8", utf8BomCharset.charset);
        assertEquals(3, utf8BomCharset.offset);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("input.html");
        File outputFile = new File("output.html");

        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] expectedData = DataUtil.readFileToByteArray(inputFile);
        byte[] outputData = DataUtil.readFileToByteArray(outputFile);

        assertArrayEquals(expectedData, outputData);
        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testParseXmlDeclarationWithValidCharset() throws IOException {
        String xmlDataUtf8 = "<?xml encoding=\"UTF-8\"?>\n<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(xmlDataUtf8.getBytes(utf8));
        Document doc = DataUtil.parseInputStream(inputStream, null, "", parser);
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals(xmlDataUtf8, doc.body().html());
        inputStream.close();
    }

    @Test
    public void testParseXmlDeclarationWithInvalidCharset() throws IOException {
        String xmlDataUtf8 = "<?xml encoding=\"UTF-8\"?>\n<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(xmlDataUtf8.getBytes(utf8));
        assertThrows(IOException.class, () -> DataUtil.parseInputStream(inputStream, "InvalidCharset", "", parser));
        inputStream.close();
    }

    @Test
    public void testParseXmlDeclarationWithNoCharset() throws IOException {
        String xmlDataUtf8 = "<?xml?>\n<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(xmlDataUtf8.getBytes(utf8));
        Document doc = DataUtil.parseInputStream(inputStream, null, "", parser);
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals(xmlDataUtf8, doc.body().html());
        inputStream.close();
    }
}