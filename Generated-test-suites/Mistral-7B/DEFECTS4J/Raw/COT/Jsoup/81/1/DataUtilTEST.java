package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import org.junit.Test;

/**
 * Test class for DataUtil methods.
 */
public class DataUtilTest {

    /**
     * Test for DataUtil.load(File) method.
     * @throws IOException
     */
    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"/></head><body>Hello world!</body></html>";
        File testFile = new File("testFile.html");
        Files.write(testFile.getAbsoluteFile().toPath(), fileContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(testFile, "UTF-8", "");
        assertNotNull(doc);
        assertEquals("<html><head><meta charset=\"UTF-8\"/></head><body>Hello world!</body></html>", doc.html());

        Files.delete(testFile.toPath());
    }

    /**
     * Test for DataUtil.load(InputStream) method.
     * @throws IOException
     */
    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"/></head><body>Hello world!</body></html>";

        InputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(testInputStream, "UTF-8", "");
        assertNotNull(doc);
        assertEquals("<html><head><meta charset=\"UTF-8\"/></head><body>Hello world!</body></html>", doc.html());
    }

    /**
     * Test for DataUtil.parseInputStream(InputStream, String, String, Parser) method.
     * @throws IOException
     */
    @Test
    public void testParseInputStream() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"/></head><body>Hello world!</body></html>";

        InputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(testInputStream, "UTF-8", "", Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("<html><head><meta charset=\"UTF-8\"/></head><body>Hello world!</body></html>", doc.html());
    }

    /**
     * Test for DataUtil.parseInputStream(InputStream, String, String, Parser) method with invalid charset.
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseInputStreamInvalidCharset() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"/></head><body>Hello world!</body></html>";

        InputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        DataUtil.parseInputStream(testInputStream, null, "", Parser.htmlParser());
    }

    /**
     * Test for DataUtil.crossStreams(InputStream, OutputStream) method.
     * @throws IOException
     */
    @Test
    public void testCrossStreams() throws IOException {
        String readData = "ReadData";
        String writeData = "WriteData";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(readData.getBytes(StandardCharsets.UTF_8));

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] expectedOutputData = (readData + writeData).getBytes(StandardCharsets.UTF_8);
        byte[] actualOutputData = outputStream.toByteArray();

        assertTrue(new String(actualOutputData, StandardCharsets.UTF_8).equals(new String(expectedOutputData, StandardCharsets.UTF_8)));
    }

    /**
     * Test for DataUtil.readToByteBuffer(InputStream) method.
     * @throws IOException
     */
    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"/></head><body>Hello world!</body></html>";

        InputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInputStream);

        byte[] expectedByteBufferData = inputStreamContent.getBytes(StandardCharsets.UTF_8);
        byte[] actualByteBufferData = byteBuffer.array();

        assertTrue(new String(actualByteBufferData, StandardCharsets.UTF_8).equals(new String(expectedByteBufferData, StandardCharsets.UTF_8)));
    }

    /**
     * Test for DataUtil.readFileToByteBuffer(File) method.
     * @throws IOException
     */
    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"/></head><body>Hello world!</body></html>";
        File testFile = new File("testFile.html");
        Files.write(testFile.getAbsoluteFile().toPath(), fileContent.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);

        byte[] expectedByteBufferData = fileContent.getBytes(StandardCharsets.UTF_8);
        byte[] actualByteBufferData = byteBuffer.array();

        assertTrue(new String(actualByteBufferData, StandardCharsets.UTF_8).equals(new String(expectedByteBufferData, StandardCharsets.UTF_8)));

        Files.delete(testFile.toPath());
    }

    /**
     * Test for DataUtil.getCharsetFromContentType(String) method.
     */
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String result = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", result);
    }

    /**
     * Test for DataUtil.validateCharset(String) method.
     */
    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String result = DataUtil.validateCharset(charset);

        assertEquals("UTF-8", result);

        String invalidCharset = "InvalidCharset";
        assertNull(DataUtil.validateCharset(invalidCharset));
    }

    /**
     * Test for DataUtil.mimeBoundary() method.
     */
    @Test
    public void testMimeBoundary() {
        String result = DataUtil.mimeBoundary();

        int boundaryLength = 32;
        String expectedBoundary = new String(new char[boundaryLength])
                .replace("\0", new String(DataUtil.mimeBoundaryChars, 0, boundaryLength));

        assertEquals(expectedBoundary, result);
    }
}