package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Test class for DataUtil
 */
public class DataUtilTest {

    /**
     * Test method for {@link DataUtil#load(File, String, String)}
     * @throws IOException
     */
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("testFile.html");
        Document doc = DataUtil.load(file, "UTF-8", null);
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}
     * @throws IOException
     */
    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)}
     * @throws IOException
     */
    @Test
    public void testLoadInputStreamParser() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "UTF-8", null, DataUtil.Parser.xmlParser());
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#parseInputStream(InputStream, String, String, Parser)}
     * @throws IOException
     */
    @Test
    public void testParseInputStream() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, DataUtil.Parser.htmlParser());
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#crossStreams(InputStream, OutputStream)}
     * @throws IOException
     */
    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("testInput.html");
        File outputFile = new File("testOutput.html");
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);
        inputStream.close();
        outputStream.close();

        assertTrue(outputFile.length() > 0);
    }

    /**
     * Test method for null input and output streams in {@link DataUtil#crossStreams(InputStream, OutputStream)}
     * @throws IOException
     */
    @Test(expected = NullPointerException.class)
    public void testCrossStreamsNullInputStream() throws IOException {
        OutputStream outputStream = new FileOutputStream(new File("testOutput.html"));
        DataUtil.crossStreams(null, outputStream);
    }

    /**
     * Test method for null output stream in {@link DataUtil#crossStreams(InputStream, OutputStream)}
     * @throws IOException
     */
    @Test(expected = NullPointerException.class)
    public void testCrossStreamsNullOutputStream() throws IOException {
        InputStream inputStream = new FileInputStream(new File("testInput.html"));
        DataUtil.crossStreams(inputStream, null);
    }

    /**
     * Test method for empty input stream in {@link DataUtil#parseInputStream(InputStream, String, String, Parser)}
     * @throws IOException
     */
    @Test
    public void testParseEmptyInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, DataUtil.Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("", doc.html());
    }

    /**
     * Test method for null charset name in {@link DataUtil#load(File, String, String)} and {@link DataUtil#parseInputStream(InputStream, String, String, Parser)}
     * @throws IOException
     */
    @Test
    public void testParseNullCharsetName() throws IOException {
        File file = new File("testFile.html");
        Document doc = DataUtil.load(file, null, null);
        assertNotNull(doc);

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Document docFromInputStream = DataUtil.parseInputStream(inputStream, null, null, DataUtil.Parser.htmlParser());
        assertNotNull(docFromInputStream);
    }

    /**
     * Test method for empty input stream in {@link DataUtil#parseInputStream(InputStream, String, String, Parser)}
     * @throws IOException
     */
    @Test
    public void testParseEmptyInputStreamWithParser() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, DataUtil.Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("", doc.html());
    }

    /**
     * Test method for {@link DataUtil#detectCharsetFromBom(ByteBuffer)}
     */
    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{0x00, 0x00, 0xFE, 0xFF}); // UTF-32
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertEquals("UTF-32", bomCharset.charset);
        assertEquals(0, bomCharset.offset);

        byteBuffer = ByteBuffer.wrap(new byte[]{0xFF, 0xFE, 0x00, 0x00}); // UTF-16
        bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertEquals("UTF-16", bomCharset.charset);
        assertEquals(0, bomCharset.offset);

        byteBuffer = ByteBuffer.wrap(new byte[]{0xEF, 0xBB, 0xBF}); // UTF-8
        bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertEquals("UTF-8", bomCharset.charset);
        assertEquals(3, bomCharset.offset);

        byteBuffer = ByteBuffer.wrap(new byte[]{0x01, 0x02, 0x03, 0x04}); // Not a BOM
        bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertNull(bomCharset);
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)}
     */
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html; charset=GB2312";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("GB2312", charset);

        contentType = "text/html; charset=ISO-8859-1";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("ISO-8859-1", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    /**
     * Test method for {@link DataUtil#validateCharset(String)}
     */
    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validCharset);

        charset = "UTF-8\t";
        validCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validCharset);

        charset = "UTF-8\n";
        validCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validCharset);

        charset = "ISO-8859-1";
        validCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validCharset);

        charset = "ISO-8859-1\t";
        validCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validCharset);

        charset = "ISO-8859-1\n";
        validCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validCharset);

        charset = "invalidCharset";
        validCharset = DataUtil.validateCharset(charset);
        assertNull(validCharset);
    }

    /**
     * Test method for {@link DataUtil#mimeBoundary()}
     */
    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        int length = boundary.length();
        for (int i = 0; i < length; i++) {
            assertEquals(true, Character.isDefinedIsAlphabetic(boundary.charAt(i)));
            assertEquals(true, Character.isDefinedIsDigit(boundary.charAt(i)));
        }
    }
}