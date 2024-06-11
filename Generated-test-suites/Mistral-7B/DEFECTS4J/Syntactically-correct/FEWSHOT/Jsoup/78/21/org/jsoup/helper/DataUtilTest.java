package org.jsoup.helper;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFromFileWithDefaultCharset() throws IOException {
        String fileContent = "This is a test file.";
        File testFile = new File("testFile.html");

        Files.write(testFile.getAbsoluteFile(), fileContent.getBytes("UTF-8"));

        Document document = DataUtil.load(testFile, null);

        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals("This is a test file.", document.body().text());
    }

    @Test
    public void testLoadFromFileWithSpecifiedCharset() throws IOException {
        String fileContent = "This is a test file.";
        File testFile = new File("testFile.html");

        Files.write(testFile.getAbsoluteFile(), fileContent.getBytes("UTF-8"));

        Document document = DataUtil.load(testFile, "ISO-8859-1");

        assertNull(document.outputSettings().charset());
        assertEquals("This is a test file.", document.body().text());
    }

    @Test
    public void testLoadFromInputStreamWithDefaultCharset() throws IOException {
        final String inputStreamContent = "This is a test input stream.";

        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        Document document = DataUtil.load(inputStream, null);

        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals("This is a test input stream.", document.body().text());
    }

    @Test
    public void testLoadFromInputStreamWithSpecifiedCharset() throws IOException {
        final String inputStreamContent = "This is a test input stream.";

        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        Document document = DataUtil.load(inputStream, "ISO-8859-1");

        assertNull(document.outputSettings().charset());
        assertEquals("This is a test input stream.", document.body().text());
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        final String inputStreamContent = "This is a test input stream.";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());

        assertEquals("UTF-8", Charset.forName("UTF-8").decode(outputStream.toByteArray()).toString().split("\n")[0].split(":")[1]);
        assertEquals("This is a test input stream.", document.body().text());
    }

    @Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException {
        final String inputStreamContent = "This is a test input stream.";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        DataUtil.parseInputStream(inputStream, "ISO-8859-1", null, Parser.htmlParser());

        assertNull(document.outputSettings().charset());
        assertEquals("This is a test input stream.", document.body().text());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        inputStream.close();

        assertEquals(inputStreamContent, outputStream.toString());
    }

    @Test
    public void testDetectCharsetFromBOM() throws IOException {
        byte[] bomUtf32 = {0x00, 0x00, (byte) 0xFE, (byte) 0xFF};
        ByteBuffer byteDataUtf32 = ByteBuffer.wrap(bomUtf32);

        BomCharset bomCharsetUtf32 = DataUtil.detectCharsetFromBom(byteDataUtf32);

        assertEquals("UTF-32", bomCharsetUtf32.charset);
        assertEquals(0, bomCharsetUtf32.offset);

        byte[] bomUtf16BE = {(byte) 0xFE, (byte) 0xFF, 0x00, 0x00};
        ByteBuffer byteDataUtf16BE = ByteBuffer.wrap(bomUtf16BE);

        BomCharset bomCharsetUtf16BE = DataUtil.detectCharsetFromBom(byteDataUtf16BE);

        assertEquals("UTF-16", bomCharsetUtf16BE.charset);
        assertEquals(0, bomCharsetUtf16BE.offset);

        byte[] bomUTF8 = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        ByteBuffer byteDataUTF8 = ByteBuffer.wrap(bomUTF8);

        BomCharset bomCharsetUTF8 = DataUtil.detectCharsetFromBom(byteDataUTF8);

        assertEquals("UTF-8", bomCharsetUTF8.charset);
        assertEquals(3, bomCharsetUTF8.offset);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));

        contentType = "application/xml; charset=ISO-8859-1";

        assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/plain";

        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String charsetValidated = DataUtil.validateCharset(charset);

        assertEquals(charset, charsetValidated);

        charset = "UTF-8\n";
        charsetValidated = DataUtil.validateCharset(charset);

        assertEquals(charset, charsetValidated);

        charset = "ISO-8859-1";
        charsetValidated = DataUtil.validateCharset(charset);

        assertEquals(charset, charsetValidated);

        charset = "ISO-8859-1\n";
        charsetValidated = DataUtil.validateCharset(charset);

        assertEquals(charset, charsetValidated);

        charset = "UTF-16";
        charsetValidated = DataUtil.validateCharset(charset);

        assertEquals(charset, charsetValidated);

        charset = "UTF-16\n";
        charsetValidated = DataUtil.validateCharset(charset);

        assertEquals(charset, charsetValidated);

        charset = "UTF-32";
        charsetValidated = DataUtil.validateCharset(charset);

        assertEquals(charset, charsetValidated);

        charset = "UTF-32\n";
        charsetValidated = DataUtil.validateCharset(charset);

        assertEquals(charset, charsetValidated);

        charset = "invalidCharset";
        charsetValidated = DataUtil.validateCharset(charset);

        assertNull(charsetValidated);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();

        assertEquals(32, mimeBoundary.length());
        for (int i = 0; i < mimeBoundary.length(); i++) {
            assertTrue(Character.isAlphabetic(mimeBoundary.charAt(i)) || Character.isDigit(mimeBoundary.charAt(i)) || mimeBoundary.charAt(i) == '-');
        }
    }
}