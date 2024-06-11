package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.jsoup.Document;
import org.jsoup.HelperException;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParsedText;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import org.junit.Test;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "This is a test file.";
        String charsetName = StandardCharsets.UTF_8.name();
        File file = new File("testFile.html");
        file.createNewFile();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(fileContent);
        }

        Document document = DataUtil.load(file, charsetName, null);
        assertEquals(fileContent, document.body().text());
        assertNotNull(document.body());

        document = DataUtil.load(file, null, null);
        assertEquals(fileContent, document.body().text());
        assertNotNull(document.body());
        assertNull(document.outputSettings().charset());

        file.delete();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "This is a test file.";
        String charsetName = StandardCharsets.UTF_8.name();

        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, charsetName, null);
        assertEquals(inputStreamContent, document.body().text());
        assertNotNull(document.body());

        document = DataUtil.load(inputStream, null, null);
        assertEquals(inputStreamContent, document.body().text());
        assertNotNull(document.body());
        assertNull(document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputStreamContent = "<html><body>This is a test file.</body></html>";
        String charsetName = StandardCharsets.UTF_8.name();

        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, charsetName, null, Parser.xmlParser());
        assertEquals(inputStreamContent, document.body().text());
        assertNotNull(document.body());
        assertEquals(DocumentType.HTML, document.documentType());

        document = DataUtil.load(inputStream, null, null, Parser.xmlParser());
        assertEquals(inputStreamContent, document.body().text());
        assertNotNull(document.body());
        assertNull(document.outputSettings().charset());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputContent = "This is a test input.";
        String outputContent = "This is the test output.";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputContent.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        assertEquals(outputContent, outputStream.toString(StandardCharsets.UTF_8.name()));
        assertNotNull(outputStream.toString(StandardCharsets.UTF_8.name()));
        assertNull(outputStream.toString(StandardCharsets.ISO_8859_1.name()));
    }

    @Test
    public void testParseInputStream() throws IOException {
        String inputStreamContent = "<html><body>This is a test file.</body></html>";
        String charsetName = StandardCharsets.UTF_8.name();

        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseInputStream(inputStream, charsetName, null, Parser.htmlParser());
        assertEquals(inputStreamContent, document.body().text());
        assertNotNull(document.body());
        assertEquals(DocumentType.HTML, document.documentType());

        document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertEquals(inputStreamContent, document.body().text());
        assertNotNull(document.body());
        assertNull(document.outputSettings().charset());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputContent = "This is a test input.";
        int maxSize = 1024 * 1024;

        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputContent.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        assertEquals(inputContent.length(), byteBuffer.remaining());
        assertEquals(inputContent.getBytes(StandardCharsets.UTF_8).length, byteBuffer.limit());

        byteBuffer.flip();
        String readContent = Charset.forName(StandardCharsets.UTF_8.name()).decode(byteBuffer).toString();
        assertEquals(inputContent, readContent);

        inputStream.close();
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer emptyByteBuffer = DataUtil.emptyByteBuffer();
        assertEquals(0, emptyByteBuffer.limit());
        assertEquals(0, emptyByteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String invalidCharset = "UTF-8;";

        assertEquals(charset, DataUtil.validateCharset(charset));
        assertNull(DataUtil.validateCharset(invalidCharset));
    }

    @Test
    public void testMimeBoundary() {
        String expectedMimeBoundary = "d627b0f8-d28b-424b-9237-35838b5a7d1b";

        String mimeBoundary = DataUtil.mimeBoundary();
        assertEquals(expectedMimeBoundary, mimeBoundary);
    }

    @Test(expected = HelperException.class)
    public void testDetectCharsetFromBOMNullByteBuffer() {
        DataUtil.detectCharsetFromBom(null);
    }

    @Test
    public void testDetectCharsetFromBOMUTF8() throws IOException {
        String expectedCharset = "UTF-8";

        String inputContent = "\uFEFFThis is a test file.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputContent.getBytes(StandardCharsets.UTF_8));

        BomCharset bomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(inputStream, 1024 * 1024 - 1));

        assertEquals(expectedCharset, bomCharset.charset);
        assertEquals(false, bomCharset.offset);

        inputStream.close();
    }

    @Test
    public void testDetectCharsetFromBOMUTF16LE() throws IOException {
        String expectedCharset = "UTF-16";

        String inputContent = "\uFFFE\u0001This is a test file.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputContent.getBytes(StandardCharsets.UTF_16LE));

        BomCharset bomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(inputStream, 1024 * 1024 - 1));

        assertEquals(expectedCharset, bomCharset.charset);
        assertEquals(false, bomCharset.offset);

        inputStream.close();
    }

    @Test
    public void testDetectCharsetFromBOMUTF16BE() throws IOException {
        String expectedCharset = "UTF-16";

        String inputContent = "\uFFFE\u0001This is a test file.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x01, (byte) 'T', (byte) 'h', (byte) 'i', (byte) 's', (byte) ' ', (byte) 'i', (byte) 's', (byte) ' ', (byte) 'a', (byte) ' ', (byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) ' ', (byte) 'f', (byte) 'i', (byte) 'l', (byte) 'e'});

        BomCharset bomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(inputStream, 1024 * 1024 - 1));

        assertEquals(expectedCharset, bomCharset.charset);
        assertEquals(false, bomCharset.offset);

        inputStream.close();
    }

    @Test
    public void testDetectCharsetFromBOMUTF32LE() throws IOException {
        String expectedCharset = "UTF-32";

        String inputContent = "\uFFFE\u0000\u0000\u0000This is a test file.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputContent.getBytes(StandardCharsets.UTF_32LE));

        BomCharset bomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(inputStream, 1024 * 1024 - 1));

        assertEquals(expectedCharset, bomCharset.charset);
        assertEquals(false, bomCharset.offset);

        inputStream.close();
    }

    @Test
    public void testDetectCharsetFromBOMUTF32BE() throws IOException {
        String expectedCharset = "UTF-32";

        String inputContent = "\uFFFE\u0000\u0000\u0000This is a test file.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0xFF, 0xFF, 0xFF, 0xFF, 0x00, 0x00, 0x00, 0x00, 'T', 'h', 'i', 's', ' ', 'i', 's', ' ', 'a', ' ', 't', 'e', 's', 't', ' ', 'f', 'i', 'l', 'e'});

        BomCharset bomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(inputStream, 1024 * 1024 - 1));

        assertEquals(expectedCharset, bomCharset.charset);
        assertEquals(false, bomCharset.offset);

        inputStream.close();
    }
}