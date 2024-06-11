package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        File testFile = new File("test.html");
        FileUtils.writeStringToFile(testFile, fileContent, StandardCharsets.UTF_8);

        Document doc = DataUtil.load(testFile, "UTF-8", "");
        assertNotNull(doc);
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>", doc.html());

        FileUtils.deleteQuietly(testFile);
    }

    @Test
    public void testLoadDocumentFromInputStream() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        assertNotNull(doc);
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>", doc.html());
    }

    @Test
    public void testLoadDocumentFromInputStreamWithMetaCharset() throws IOException {
        String fileContent = "<html><head><meta charset=\"ISO-8859-1\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, null, "");
        assertNotNull(doc);
        assertEquals("<html><head><meta charset=\"ISO-8859-1\"></head><body><h1>Test</h1></body></html>", doc.html());
    }

    @Test
    public void testLoadDocumentFromInputStreamWithXmlDeclaration() throws IOException {
        String fileContent = "<?xml encoding=\"UTF-8\"?><html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        assertNotNull(doc);
        assertEquals("<html><body><h1>Test</h1></body></html>", doc.html());
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadDocumentFromInputStreamWithNoMetaCharsetOrXmlDeclaration() throws IOException {
        String fileContent = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, null, "");
        assertNotNull(doc);
        assertEquals("<html><body><h1>Test</h1></body></html>", doc.html());
    }

    @Test
    public void testLoadDocumentFromInputStreamWithInvalidCharset() throws IOException {
        String fileContent = "<html><head><meta charset=\"x-invalid-charset\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, "x-invalid-charset", "");
        assertNull(doc);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>", doc.html());
    }

    @Test
    public void testParseInputStreamWithXmlDeclaration() throws IOException {
        String fileContent = "<?xml encoding=\"UTF-8\"?><html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("<html><body><h1>Test</h1></body></html>", doc.html());
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testParseInputStreamWithNoMetaCharsetOrXmlDeclaration() throws IOException {
        String fileContent = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(inputStream, null, "", Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("<html><body><h1>Test</h1></body></html>", doc.html());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String fileContent = "<html><head><meta charset=\"x-invalid-charset\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(inputStream, "x-invalid-charset", "", Parser.htmlParser());
        assertNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        byte[] expectedBytes = fileContent.getBytes(StandardCharsets.UTF_8);
        byte[] actualBytes = new byte[byteBuffer.limit()];
        byteBuffer.get(actualBytes);

        assertEquals(expectedBytes.length, byteBuffer.limit());
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024 * 1024);
        byte[] expectedBytes = fileContent.getBytes(StandardCharsets.UTF_8);
        byte[] actualBytes = new byte[byteBuffer.limit()];
        byteBuffer.get(actualBytes);

        assertEquals(expectedBytes.length, byteBuffer.limit());
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=utf-8"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertEquals(null, DataUtil.getCharsetFromContentType("text/html"));
    }

    @Test
    public void testValidateCharset() {
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8 "));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8\r\n"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8\r"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8\n"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8\n\r"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8\n\r "));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8 "));

        assertEquals(null, DataUtil.validateCharset("x-invalid-charset"));
        assertEquals(null, DataUtil.validateCharset("x-invalid-charset "));
        assertEquals(null, DataUtil.validateCharset("x-invalid-charset\r"));
        assertEquals(null, DataUtil.validateCharset("x-invalid-charset\n"));
        assertEquals(null, DataUtil.validateCharset("x-invalid-charset\n\r"));
        assertEquals(null, DataUtil.validateCharset("x-invalid-charset\n\r "));
        assertEquals(null, DataUtil.validateCharset("x-invalid-charset "));
        assertEquals(null, DataUtil.validateCharset(""));
        assertEquals(null, DataUtil.validateCharset(" "));
        assertEquals(null, DataUtil.validateCharset("\r"));
        assertEquals(null, DataUtil.validateCharset("\n"));
        assertEquals(null, DataUtil.validateCharset("x-invalid-charset\r\n"));
        assertEquals(null, DataUtil.validateCharset("\r\n"));
        assertEquals(null, DataUtil.validateCharset("\r\n "));
        assertEquals(null, DataUtil.validateCharset(" "));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertEquals(32, boundary.length());

        for (int i = 0; i < boundary.length(); i++) {
            assertEquals(true, Character.isAlphabetic(boundary.charAt(i)) || Character.isDigit(boundary.charAt(i)) || boundary.charAt(i) == '-');
        }
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0xfe, 0xff, 0});

        BomCharset bomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(inputStream));
        assertEquals("UTF-16", bomCharset.charset);
        assertEquals(false, bomCharset.offset);

        inputStream = new ByteArrayInputStream(new byte[]{0, 0xfe, 0xff, 0});

        bomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(inputStream));
        assertEquals("UTF-16", bomCharset.charset);
        assertEquals(true, bomCharset.offset);

        inputStream = new ByteArrayInputStream(new byte[]{0xff, 0xfe, 0, 0});

        bomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(inputStream));
        assertEquals("UTF-16", bomCharset.charset);
        assertEquals(true, bomCharset.offset);

        inputStream = new ByteArrayInputStream(new byte[]{0xff, 0, 0, 0xfe});

        bomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(inputStream));
        assertEquals("UTF-16", bomCharset.charset);
        assertEquals(false, bomCharset.offset);

        inputStream = new ByteArrayInputStream(new byte[]{0x00, 0x00, 0xfe, 0xff});

        bomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(inputStream));
        assertEquals("UTF-32", bomCharset.charset);
        assertEquals(false, bomCharset.offset);

        inputStream = new ByteArrayInputStream(new byte[]{0xfe, 0xff, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});

        bomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(inputStream));
        assertEquals("UTF-32", bomCharset.charset);
        assertEquals(false, bomCharset.offset);

        inputStream = new ByteArrayInputStream(new byte[]{0xff, 0xfe, 0, 0});

        bomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(inputStream));
        assertEquals(null, bomCharset);

        inputStream = new ByteArrayInputStream(new byte[]{0x00, 0x00});

        bomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(inputStream));
        assertEquals(null, bomCharset);
    }
}