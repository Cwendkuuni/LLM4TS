package org.jsoup.helper;

import org.jjunit.Assert;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;



public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        String fileContent = "Hello World!";
        String expectedCharset = "UTF-8";
        File inputFile = new File("test.html");
        FileUtils.writeStringToFile(inputFile, fileContent, Charset.forName(expectedCharset));

        Document doc = DataUtil.load(inputFile, expectedCharset, "");
        assertNotNull(doc);
        assertEquals(fileContent, doc.body().text());
        FileUtils.deleteQuietly(inputFile);
    }

    @Test
    public void testLoadDocumentFromInputStream() throws IOException {
        String inputStreamContent = "Hello World!";
        String expectedCharset = "UTF-8";

        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(Charset.forName(expectedCharset)));

        Document doc = DataUtil.load(inputStream, expectedCharset, "");
        assertNotNull(doc);
        assertEquals(inputStreamContent, doc.body().text());
    }

    @Test
    public void testLoadDocumentFromInputStreamWithParser() throws IOException {
        String inputStreamContent = "<html><body>Hello World!</body></html>";
        String expectedCharset = "UTF-8";

        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(Charset.forName(expectedCharset)));

        Document doc = DataUtil.load(inputStream, expectedCharset, "", Parser.xmlParser());
        assertNotNull(doc);
        assertEquals(inputStreamContent, doc.html().toString());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputStreamContent = "Hello World!";
        String expectedOutput = "Hello World!";

        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String inputStreamContent = "Hello World!";
        String expectedCharset = "UTF-8";
        String expectedHtmlContent = "<html><body>Hello World!</body></html>";

        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(Charset.forName(expectedCharset)));

        Document doc = DataUtil.parseInputStream(inputStream, expectedCharset, "", Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(expectedHtmlContent, doc.html().toString());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputStreamContent = "Hello World!";
        byte[] expectedByteBuffer = inputStreamContent.getBytes();

        InputStream inputStream = new ByteArrayInputStream(expectedByteBuffer);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);

        assertEquals(expectedByteBuffer.length, byteBuffer.limit());
        byte[] actualByteBuffer = new byte[byteBuffer.limit()];
        byteBuffer.get(actualByteBuffer);
        assertArrayEquals(expectedByteBuffer, actualByteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String actualCharset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, actualCharset);
    }

    @Test
    public void testValidateCharset() {
        String validCharset = "UTF-8";
        String invalidCharset = "InvalidCharset";

        assertEquals(validCharset, DataUtil.validateCharset(validCharset));
        assertNull(DataUtil.validateCharset(null));
        assertNull(DataUtil.validateCharset(""));
        assertNull(DataUtil.validateCharset(invalidCharset));
    }

    @Test
    public void testMimeBoundary() {
        String expectedBoundary = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String actualBoundary = DataUtil.mimeBoundary();

        assertEquals(expectedBoundary.length(), actualBoundary.length());
        assertEquals(expectedBoundary.charAt(i), actualBoundary.charAt(i), i < expectedBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom_UTF32() throws IOException {
        String bomContent = "\u0000\u0000\u00FE\u00FF";
        ByteBuffer bomByteBuffer = ByteBuffer.wrap(bomContent.getBytes());
        BomCharset expectedBomCharset = new BomCharset("UTF-32", false);

        BomCharset actualBomCharset = DataUtil.detectCharsetFromBom(bomByteBuffer);
        assertEquals(expectedBomCharset.charset, actualBomCharset.charset);
        assertEquals(expectedBomCharset.offset, actualBomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBom_UTF16() throws IOException {
        String bomContent = "\u00FE\u00FF";
        ByteBuffer bomByteBuffer = ByteBuffer.wrap(bomContent.getBytes());
        BomCharset expectedBomCharset = new BomCharset("UTF-16", false);

        BomCharset actualBomCharset = DataUtil.detectCharsetFromBom(bomByteBuffer);
        assertEquals(expectedBomCharset.charset, actualBomCharset.charset);
        assertEquals(expectedBomCharset.offset, actualBomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBom_UTF8() throws IOException {
        String bomContent = "\u00EF\u00BB\u00BF";
        ByteBuffer bomByteBuffer = ByteBuffer.wrap(bomContent.getBytes());
        BomCharset expectedBomCharset = new BomCharset("UTF-8", true);

        BomCharset actualBomCharset = DataUtil.detectCharsetFromBom(bomByteBuffer);
        assertEquals(expectedBomCharset.charset, actualBomCharset.charset);
        assertEquals(expectedBomCharset.offset, actualBomCharset.offset);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDetectCharsetFromBom_InvalidBom() throws IOException {
        String bomContent = "InvalidBOM";
        ByteBuffer bomByteBuffer = ByteBuffer.wrap(bomContent.getBytes());

        DataUtil.detectCharsetFromBom(bomByteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType_InvalidContentType() {
        String contentType = "InvalidContentType";
        String expectedCharset = null;

        String actualCharset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, actualCharset);
    }

}
