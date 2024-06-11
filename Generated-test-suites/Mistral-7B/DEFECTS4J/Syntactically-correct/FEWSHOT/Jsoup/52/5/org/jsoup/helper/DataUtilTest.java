package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        File file = new File("test_file_with_valid_charset.html");
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadFileWithInvalidCharsetInHeader() throws IOException {
        File file = new File("test_file_with_invalid_charset_in_header.html");
        String charsetName = null;
        String baseUri = "http://test.com";

        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);

        // Check that the correct charset was detected
        Element meta = document.select("meta[http-equiv=content-type], meta[charset]").first();
        String foundCharset = null;

        if (meta != null) {
            if (meta.hasAttr("http-equiv")) {
                foundCharset = getCharsetFromContentType(meta.attr("content"));
            }
            if (foundCharset == null && meta.hasAttr("charset")) {
                foundCharset = meta.attr("charset");
            }
        }

        assertEquals("UTF-8", foundCharset);
    }

    @Test
    public void testLoadFileWithInvalidCharsetInXMLDeclaration() throws IOException {
        File file = new File("test_file_with_invalid_charset_in_xml_declaration.xml");
        String charsetName = null;
        String baseUri = "http://test.com";

        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);

        // Check that the correct charset was detected
        XmlDeclaration xmlDeclaration = (XmlDeclaration) document.childNodes().get(0);
        String foundCharset = xmlDeclaration.attr("encoding");

        assertEquals("UTF-8", foundCharset);
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        InputStream inputStream = new ByteArrayInputStream("test_input_stream_with_valid_charset".getBytes(Charset.forName(charsetName)));
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharsetInHeader() throws IOException {
        String charsetName = null;
        String baseUri = "http://test.com";

        InputStream inputStream = new ByteArrayInputStream("--test--\nContent-Type: text/html; charset=ISO-8859-1\n\n<html>Test</html>\n--test--".getBytes());
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);

        // Check that the correct charset was detected
        Element meta = document.select("meta[http-equiv=content-type], meta[charset]").first();
        String foundCharset = null;

        if (meta != null) {
            if (meta.hasAttr("http-equiv")) {
                foundCharset = getCharsetFromContentType(meta.attr("content"));
            }
            if (foundCharset == null && meta.hasAttr("charset")) {
                foundCharset = meta.attr("charset");
            }
        }

        assertEquals("UTF-8", foundCharset);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharsetInXMLDeclaration() throws IOException {
        String charsetName = null;
        String baseUri = "http://test.com";

        InputStream inputStream = new ByteArrayInputStream("<?xml encoding='ISO-8859-1'?>\n<html>Test</html>".getBytes());
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);

        // Check that the correct charset was detected
        XmlDeclaration xmlDeclaration = (XmlDeclaration) document.childNodes().get(0);
        String foundCharset = xmlDeclaration.attr("encoding");

        assertEquals("UTF-8", foundCharset);
    }

    @Test
    public void testParseByteDataWithValidCharset() {
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        ByteBuffer byteData = ByteBuffer.wrap("test_byte_data_with_valid_charset".getBytes(Charset.forName(charsetName)));

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithInvalidCharsetInHeader() {
        String charsetName = null;
        String baseUri = "http://test.com";
        ByteBuffer byteData = ByteBuffer.wrap("--test--\nContent-Type: text/html; charset=ISO-8859-1\n\n<html>Test</html>\n--test--".getBytes());

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);

        // Check that the correct charset was detected
        Element meta = document.select("meta[http-equiv=content-type], meta[charset]").first();
        String foundCharset = null;

        if (meta != null) {
            if (meta.hasAttr("http-equiv")) {
                foundCharset = getCharsetFromContentType(meta.attr("content"));
            }
            if (foundCharset == null && meta.hasAttr("charset")) {
                foundCharset = meta.attr("charset");
            }
        }

        assertEquals("UTF-8", foundCharset);
    }

    @Test
    public void testParseByteDataWithInvalidCharsetInXMLDeclaration() {
        String charsetName = null;
        String baseUri = "http://test.com";
        ByteBuffer byteData = ByteBuffer.wrap("<?xml encoding='ISO-8859-1'?>\n<html>Test</html>".getBytes());

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);

        // Check that the correct charset was detected
        XmlDeclaration xmlDeclaration = (XmlDeclaration) document.childNodes().get(0);
        String foundCharset = xmlDeclaration.attr("encoding");

        assertEquals("UTF-8", foundCharset);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("input_file.txt");
        File outputFile = new File("output_file.txt");

        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] outputFileBytes = new byte[outputFile.length()];
        Files.readAllBytes(outputFile.toPath(), outputFileBytes);

        assertEquals(Files.readAllBytes(inputFile.toPath()), outputFileBytes);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertEquals(ByteBuffer.allocate(0).limit(), byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=ISO-8859-1";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        assertEquals("UTF-8", DataUtil.validateCharset(charset));

        charset = "UTF-8";
        assertEquals("UTF-8", DataUtil.validateCharset(charset.toUpperCase()));

        charset = "ISO-8859-1";
        assertNull(DataUtil.validateCharset(charset));

        charset = null;
        assertNull(DataUtil.validateCharset(charset));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertTrue(boundary.length() > 0);
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        ByteBuffer byteData1 = ByteBuffer.wrap(new byte[] { 0x00, 0x00, (byte) 0xFE, (byte) 0xFF });
        assertEquals("UTF-32", DataUtil.detectCharsetFromBom(byteData1, null));

        ByteBuffer byteData2 = ByteBuffer.wrap(new byte[] { (byte) 0xFE, (byte) 0xFF, 0x00, 0x00 });
        assertEquals("UTF-32", DataUtil.detectCharsetFromBom(byteData2, null));

        ByteBuffer byteData3 = ByteBuffer.wrap(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
        assertEquals("UTF-8", DataUtil.detectCharsetFromBom(byteData3, null));

        ByteBuffer byteData4 = ByteBuffer.wrap(new byte[] { 0x00, 0x01, 0x02, 0x03 });
        assertNull(DataUtil.detectCharsetFromBom(byteData4, null));
    }
}