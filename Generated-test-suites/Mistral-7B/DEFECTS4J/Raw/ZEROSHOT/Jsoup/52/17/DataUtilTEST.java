package org.jsoup.helper;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

import org.junit.Test;

/**
 * Test class for DataUtil.
 *
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullCharset() throws IOException {
        File file = new File("test.html");
        String baseUri = "http://example.com";
        DataUtil.load(file, null, baseUri);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithEmptyCharset() throws IOException {
        File file = new File("test.html");
        String baseUri = "http://example.com";
        DataUtil.load(file, "", baseUri);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        String baseUri = "http://example.com";
        DataUtil.load(inputStream, null, baseUri);
    }

    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = mock(Parser.class);
        Document document = mock(Document.class);
        when(parser.parseInput(Mockito.anyString(), Mockito.eq(baseUri))).thenReturn(document);

        ByteBuffer byteData = ByteBuffer.wrap(new byte[] { 0x00, 0x00, 0xFE, 0xFF });
        String detectedCharset = DataUtil.detectCharsetFromBom(byteData, charsetName);
        String expectedCharset = "UTF-32";
        assertEquals(expectedCharset, detectedCharset);

        byteData = ByteBuffer.wrap(new byte[] { 0xFE, 0xFF, 0x00, 0x00 });
        detectedCharset = DataUtil.detectCharsetFromBom(byteData, charsetName);
        expectedCharset = "UTF-32";
        assertEquals(expectedCharset, detectedCharset);

        byteData = ByteBuffer.wrap(new byte[] { 0xEF, 0xBB, 0xBF, 0x68, 0x65, 0x61, 0x64, 0x72, 0x65, 0x72, 0x61, 0x3B, 0x31, 0x2E, 0x31, 0x2E, 0x32, 0x0D, 0x0A, 0x68, 0x65, 0x61, 0x64, 0x45, 0x6E, 0x63, 0x74, 0x54, 0x79, 0x70, 0x65, 0x3B, 0x31, 0x2E, 0x31, 0x2E, 0x32, 0x0D, 0x0A, 0x63, 0x6F, 0x6D, 0x70, 0x65, 0x6E, 0x64, 0x6C, 0x65, 0x74, 0x20, 0x74, 0x65, 0x78, 0x74, 0x2F, 0x69, 0x6D, 0x70, 0x5C, 0x61, 0x70, 0x70, 0x43, 0x6F, 0x6F, 0x6B, 0x69, 0x65, 0x73, 0x2F, 0x68, 0x69, 0x67, 0x68, 0x61, 0x74, 0x6B, 0x62, 0x65, 0x21, 0x0D, 0x0A, 0x6D, 0x79, 0x63, 0x68, 0x65, 0x6D, 0x65, 0x64, 0x69, 0x6E, 0x67, 0x3B, 0x31, 0x2E, 0x31, 0x2E, 0x32 });
        String detectedCharsetFromBom = DataUtil.detectCharsetFromBom(byteData, charsetName);
        assertEquals("UTF-8", detectedCharsetFromBom);

        String docData = "<html><head></head><body></body></html>";
        document = when(parser.parseInput(Mockito.eq(docData), Mockito.eq(baseUri))).thenReturn(document).getMock();
        ByteBuffer byteDataForParse = ByteBuffer.wrap(new byte[] { 0xEF, 0xBB, 0xBF });
        Document parsedDocument = DataUtil.parseByteData(byteDataForParse, charsetName, baseUri, parser);
        assertNotNull(parsedDocument);
        assertEquals(document, parsedDocument);
    }

    @Test
    public void testParseByteDataWithNullCharset() {
        Parser parser = mock(Parser.class);
        Document document = mock(Document.class);
        when(parser.parseInput(Mockito.anyString(), Mockito.eq("http://example.com"))).thenReturn(document);

        ByteBuffer byteData = ByteBuffer.wrap(new byte[] { 0xEF, 0xBB, 0xBF });
        try {
            DataUtil.parseByteData(byteData, null, "", parser);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseByteDataWithEmptyCharset() {
        Parser parser = mock(Parser.class);
        Document document = mock(Document.class);
        when(parser.parseInput(Mockito.anyString(), Mockito.eq("http://example.com"))).thenReturn(document);

        ByteBuffer byteData = ByteBuffer.wrap(new byte[] { 0xEF, 0xBB, 0xBF });
        try {
            DataUtil.parseByteData(byteData, "", parser);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseByteDataWithNullParser() {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        ByteBuffer byteData = ByteBuffer.wrap(new byte[] { 0xEF, 0xBB, 0xBF });
        try {
            DataUtil.parseByteData(byteData, charsetName, "", null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testString = "test string";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes("UTF-8"));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertEquals(testString, Charset.forName("UTF-8").decode(byteBuffer).toString());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String testString = "test string";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes("UTF-8"));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 10);
        assertEquals(testString.substring(0, 10), Charset.forName("UTF-8").decode(byteBuffer).toString());
    }

    @Test
    public void testReadToByteBufferWithZeroMaxSize() throws IOException {
        String testString = "test string";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes("UTF-8"));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);
        assertEquals("", Charset.forName("UTF-8").decode(byteBuffer).toString());
    }

    @Test
    public void testReadToByteBufferWithNullInputStream() {
        try {
            DataUtil.readToByteBuffer(null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        assertNotNull(byteBuffer);
    }

    @Test(expected = IOException.class)
    public void testReadFileToByteBufferWithNullFile() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(null);
        fail();
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertEquals(0L, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        assertEquals(expectedCharset, DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "UTF-16";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "ISO-8859-1";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "";
        assertNull(DataUtil.validateCharset(charset));

        charset = null;
        assertNull(DataUtil.validateCharset(charset));
    }

    @Test
    public void testMimeBoundary() {
        String expectedMimeBoundary = "random_string_-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        assertEquals(expectedMimeBoundary, DataUtil.mimeBoundary());
    }
}