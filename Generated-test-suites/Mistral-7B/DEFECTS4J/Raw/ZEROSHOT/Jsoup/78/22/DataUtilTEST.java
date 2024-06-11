package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;

public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        File file = new File("path/to/your/testfile.html");
        Document document = DataUtil.load(file, null, null);
        assertNotNull(document);
    }

    @Test
    public void testLoadDocumentFromInputStream() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(document);
        inputStream.close();
    }

    @Test
    public void testLoadDocumentFromInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "InvalidCharset", null);
        assertNull(document);
        inputStream.close();
    }

    @Test
    public void testParseInputStream() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        assertNotNull(document);
        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        String sourceContent = "Source content";
        String targetContent = "Target content";
        ByteArrayInputStream sourceInputStream = new ByteArrayInputStream(sourceContent.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream targetOutputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(sourceInputStream, targetOutputStream);
        String targetContentRead = targetOutputStream.toString(StandardCharsets.UTF_8.name());
        assertTrue(sourceContent.equals(targetContentRead));

        sourceInputStream.close();
        targetOutputStream.close();
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertTrue(boundary.length() == 32);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        assertNull(DataUtil.validateCharset(null));
        assertNull(DataUtil.validateCharset(""));
        assertNull(DataUtil.validateCharset(" UTF-8"));
        assertNull(DataUtil.validateCharset("UTF-8 "));

        assertTrue(DataUtil.validateCharset(charset).equalsIgnoreCase(charset));

        String invalidCharset = "INVALID_UTF-8";
        assertNull(DataUtil.validateCharset(invalidCharset));
    }

    @Test
    public void testDetectCharsetFromBOM() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[] { (byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00 });
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertTrue(bomCharset != null);
        assertTrue(bomCharset.charset.equalsIgnoreCase("UTF-16"));
        assertTrue(bomCharset.offset == 0);

        byteData = ByteBuffer.wrap(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 });
        bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertTrue(bomCharset != null);
        assertTrue(bomCharset.charset.equalsIgnoreCase("UTF-8"));
        assertTrue(bomCharset.offset == 3);

        byteData = ByteBuffer.wrap(new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 });
        bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertNull(bomCharset);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String content = "Testing readToByteBuffer test.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        inputStream.close();

        long limit = byteBuffer.limit();
        byteBuffer.position(0);
        String contentFromByteBuffer = StandardCharsets.UTF_8.decode(byteBuffer).toString();
        assertTrue(contentFromByteBuffer.equals(content));
        assertTrue(byteBuffer.limit() == limit);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String fileContent = "Testing readFileToByteBuffer test.";
        File file = new File("path/to/your/testfile.txt");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        String contentFromByteBuffer = StandardCharsets.UTF_8.decode(byteBuffer).toString();
        assertTrue(fileContent.equals(contentFromByteBuffer));

        file.delete();
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertTrue(byteBuffer.capacity() == 0);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertTrue(charset.equalsIgnoreCase("UTF-8"));

        contentType = null;
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}