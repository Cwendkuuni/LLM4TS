package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.jsoup.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Xml;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Test;

/**
 * Test class for DataUtil.
 *
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        File inputFile = new File("input.html");
        Files.write(inputFile.getAbsoluteFile(), fileContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputFile, null, null);
        assertNotNull(doc);
        assertEquals("text/html", doc.contentType());
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals("<html>", doc.body().tagName());

        Files.delete(inputFile.getAbsoluteFile());
    }

    @Test
    public void testLoadInputStreamWithoutCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null, null);
        assertNotNull(doc);
        assertEquals("text/html", doc.contentType());
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals("<html>", doc.body().tagName());

        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(doc);
        assertEquals("text/html", doc.contentType());
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals("<html>", doc.body().tagName());

        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
        assertNotNull(doc);
        assertEquals("application/xml", doc.contentType());
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals(DocumentType.HTML, doc.documentType());
        assertEquals("<html>", doc.body().tagName());

        inputStream.close();
    }

    @Test
    public void testParseInputStream() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("text/html", doc.contentType());
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals("<html>", doc.body().tagName());

        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.xmlParser());
        assertNotNull(doc);
        assertEquals("application/xml", doc.contentType());
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals(DocumentType.HTML, doc.documentType());
        assertEquals("<html>", doc.body().tagName());

        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        String fileContent = "Hello World!";
        File inputFile = new File("input.txt");
        Files.write(inputFile.getAbsoluteFile(), fileContent.getBytes(StandardCharsets.UTF_8));

        InputStream inputStream = new FileInputStream(inputFile);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        assertEquals(fileContent, outputStream.toString(StandardCharsets.UTF_8.name()));

        inputStream.close();
        outputStream.close();

        Files.delete(inputFile.getAbsoluteFile());
    }

    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=utf-8"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8-BOM"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8-without-BOM"));
        assertEquals(null, DataUtil.getCharsetFromContentType("text/html; charset=invalid-charset"));
        assertEquals(null, DataUtil.getCharsetFromContentType("text/html"));
        assertEquals(null, DataUtil.getCharsetFromContentType(null));
    }

    @Test
    public void testValidateCharset() {
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8"));
        assertEquals("UTF-8", DataUtil.validateCharset("utf-8"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8-BOM"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8-without-BOM"));
        assertEquals(null, DataUtil.validateCharset("invalid-charset"));
        assertEquals("UTF-8", DataUtil.validateCharset("ISO-8859-1"));
        assertEquals("UTF-8", DataUtil.validateCharset("gb2312"));
        assertEquals("UTF-8", DataUtil.validateCharset("GB2312"));
        assertEquals("UTF-8", DataUtil.validateCharset("GB2312-BOM"));
        assertEquals("UTF-8", DataUtil.validateCharset("GB2312-without-BOM"));
        assertEquals("UTF-8", DataUtil.validateCharset(""));
        assertEquals(null, DataUtil.validateCharset(null));
    }

    @Test
    public void testMimeBoundary() {
        assertEquals("--3234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", DataUtil.mimeBoundary());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.put((byte) 0xEF);
        byteBuffer.put((byte) 0xBB);
        byteBuffer.put((byte) 0xBF);
        byteBuffer.flip();

        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertEquals("UTF-8", bomCharset.charset);
        assertEquals(true, bomCharset.offset);

        byteBuffer.clear();
        byteBuffer.put((byte) 0xFF);
        byteBuffer.put((byte) 0xFE);
        byteBuffer.flip();

        bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertEquals("UTF-16", bomCharset.charset);
        assertEquals(false, bomCharset.offset);

        byteBuffer.clear();
        byteBuffer.put((byte) 0xFE);
        byteBuffer.put((byte) 0xFF);
        byteBuffer.flip();

        bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertEquals("UTF-16", bomCharset.charset);
        assertEquals(true, bomCharset.offset);

        byteBuffer.clear();
        byteBuffer.put((byte) 0x00);
        byteBuffer.put((byte) 0x00);
        byteBuffer.put((byte) 0xFE);
        byteBuffer.put((byte) 0xFF);
        byteBuffer.flip();

        bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertEquals("UTF-32", bomCharset.charset);
        assertEquals(false, bomCharset.offset);

        byteBuffer.clear();
        byteBuffer.put((byte) 0x00);
        byteBuffer.put((byte) 0x00);
        byteBuffer.put((byte) 0xFF);
        byteBuffer.put((byte) 0xFE);
        byteBuffer.flip();

        bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertEquals("UTF-32", bomCharset.charset);
        assertEquals(true, bomCharset.offset);

        byteBuffer.clear();
        byteBuffer.put((byte) 0xFE);
        byteBuffer.put((byte) 0xFF);
        byteBuffer.put((byte) 0x00);
        byteBuffer.put((byte) 0x00);
        byteBuffer.flip();

        bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertEquals(null, bomCharset);
    }

}