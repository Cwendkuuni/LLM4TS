package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws IOException {
        String expected = "Expected content of the file.";
        File file = new File("testFile.html");
        Document doc = DataUtil.load(file, null, null);
        assertEquals(expected, doc.body().text());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String expected = "Expected content of the input stream.";
        String inputStreamContent = "Expected content of the input stream.";
        InputStream in = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(in, null, null);
        assertEquals(expected, doc.body().text());
        in.close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String expected = "Expected content of the input stream.";
        String inputStreamContent = "Expected content of the input stream.";
        InputStream in = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        Parser parser = Parser.xmlParser();
        Document doc = DataUtil.load(in, null, null, parser);
        assertEquals(expected, doc.body().text());
        in.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        String expected = "Expected content of the input stream.";
        String inputStreamContent = "Expected content of the input stream.";
        InputStream in = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        OutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out);
        String outputStreamContent = out.toString("UTF-8");
        assertEquals(expected, outputStreamContent);
        out.close();
        in.close();
    }

    @Test
    public void testParseByteData() {
        String expected = "Expected content of the byte buffer.";
        String charsetName = "UTF-8";
        String baseUri = "https://example.com/";
        Parser parser = Parser.htmlParser();

        ByteBuffer byteData = ByteBuffer.wrap("Expected content of the byte buffer".getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertEquals(expected, doc.body().text());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[] { (byte) 0xFF, (byte) 0xFE, 0, 0 });
        String charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-16", charsetName);

        byteData = ByteBuffer.wrap(new byte[] { 0xEF, 0xBB, 0xBF, 0 });
        charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-8", charsetName);

        byteData = ByteBuffer.wrap(new byte[] { 0xFE, 0xFF, 0, 0 });
        charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-32", charsetName);

        byteData = ByteBuffer.wrap(new byte[] { 0x00, 0x00, 0xFE, 0xFF });
        charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-32", charsetName);

        byteData = ByteBuffer.wrap(new byte[] { 0xFE, 0xFF, 0x00, 0x00 });
        charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-32", charsetName);

        byteData = ByteBuffer.wrap(new byte[] { 0xEF, 0xBB, 0xBF });
        charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-8", charsetName);

        byteData = ByteBuffer.wrap(new byte[] { 0x00, 0x00 });
        charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertNull(charsetName);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expected = "UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expected, charset);

        contentType = "text/html; charset=UTF-16";
        expected = "UTF-16";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expected, charset);

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testValidateCharset() {
        String cs = "UTF-8";
        String validCharset = DataUtil.validateCharset(cs);
        assertEquals(cs, validCharset);

        cs = "UTF-16";
        validCharset = DataUtil.validateCharset(cs);
        assertEquals(cs, validCharset);

        cs = "GB2312";
        validCharset = DataUtil.validateCharset(cs);
        assertEquals(cs, validCharset);

        cs = "EUC-JP";
        validCharset = DataUtil.validateCharset(cs);
        assertEquals(cs, validCharset);

        cs = "";
        assertNull(DataUtil.validateCharset(cs));

        cs = "invalid";
        assertNull(DataUtil.validateCharset(cs));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertTrue(boundary.length() > 0);
    }
}