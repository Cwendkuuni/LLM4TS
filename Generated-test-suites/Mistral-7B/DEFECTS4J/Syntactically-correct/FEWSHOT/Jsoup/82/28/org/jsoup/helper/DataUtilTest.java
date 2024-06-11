package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></meta></head><body><h1>Hello World</h1></body></html>";
        File file = new File("test.html");
        FileUtils.writeStringToFile(file, fileContent, "UTF-8");

        Document doc = DataUtil.load(file, null, null);
        assertEquals("Hello World", doc.select("h1").text());

        FileUtils.deleteQuietly(file);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></meta></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals("Hello World", doc.select("h1").text());

        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"></meta></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        Document doc = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
        assertEquals("Hello World", doc.select("h1").text());

        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithDefaultParser() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"></meta></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        Document doc = DataUtil.load(inputStream, null, null);
        assertEquals("Hello World", doc.select("h1").text());

        inputStream.close();
    }

    @Test
    public void testParseInputStream() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"></meta></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        assertEquals("Hello World", doc.select("h1").text());

        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithDefaultParser() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"></meta></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertEquals("Hello World", doc.select("h1").text());

        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputContent = "This is a test.";
        String outputContent = "This is a test.";

        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes("UTF-8"));
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] outputBytes = outputStream.toByteArray();
        assertEquals(inputContent, new String(outputBytes, "UTF-8"));

        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputContent = "This is a test.";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes("UTF-8"));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);
        byte[] bytes = byteBuffer.array();
        assertEquals(inputContent, new String(bytes, "UTF-8"));

        inputStream.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testValidateCharset() {
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8\t"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8\n"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8\r"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8\r\n"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8\t\r"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8\t\r\n"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8 \t "));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8 \t "));

        assertNull(DataUtil.validateCharset(""));
        assertNull(DataUtil.validateCharset(" "));
        assertNull(DataUtil.validateCharset("UTF-16"));
        assertNull(DataUtil.validateCharset("UTF-16BE"));
        assertNull(DataUtil.validateCharset("UTF-16LE"));
        assertNull(DataUtil.validateCharset("UTF-32BE"));
        assertNull(DataUtil.validateCharset("UTF-32LE"));
        assertNull(DataUtil.validateCharset("UTF-32BE\t"));
        assertNull(DataUtil.validateCharset("UTF-32LE\t"));
        assertNull(DataUtil.validateCharset("UTF-32BE\t\r"));
        assertNull(DataUtil.validateCharset("UTF-32LE\t\r"));
        assertNull(DataUtil.validateCharset("UTF-32BE\t\r\n"));
        assertNull(DataUtil.validateCharset("UTF-32LE\t\r\n"));
        assertNull(DataUtil.validateCharset("UTF-32BE\t\r\n "));
        assertNull(DataUtil.validateCharset("UTF-32LE\t\r\n "));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertFalse(boundary.isEmpty());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.put((byte) 0xFF);
        byteBuffer.put((byte) 0xFB);
        byteBuffer.put((byte) 0xEF);
        byteBuffer.put((byte) 0xBB);
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertEquals("UTF-8", bomCharset.charset);
        assertTrue(bomCharset.offset);

        byteBuffer.clear();
        byteBuffer.put((byte) 0xEF);
        byteBuffer.put((byte) 0xBB);
        byteBuffer.put((byte) 0xFF);
        byteBuffer.put((byte) 0xFE);
        bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertEquals("UTF-8", bomCharset.charset);
        assertFalse(bomCharset.offset);

        byteBuffer.clear();
        byteBuffer.put((byte) 0xFE);
        byteBuffer.put((byte) 0xFF);
        byteBuffer.put((byte) 0xEF);
        byteBuffer.put((byte) 0xBB);
        bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertEquals("UTF-8", bomCharset.charset);
        assertFalse(bomCharset.offset);

        byteBuffer.clear();
        byteBuffer.put((byte) 0x00);
        byteBuffer.put((byte) 0x00);
        byteBuffer.put((byte) 0xFE);
        byteBuffer.put((byte) 0xFF);
        bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertNull(bomCharset);

        byteBuffer.clear();
        byteBuffer.put((byte) 0xFE);
        byteBuffer.put((byte) 0xFF);
        byteBuffer.put((byte) 0x00);
        byteBuffer.put((byte) 0x00);
        bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertNull(bomCharset);
    }
}

class FileUtils {
    public static void writeStringToFile(File file, String str, String charset) throws IOException {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), charset)) {
            writer.write(str);
        }
    }

    public static void deleteQuietly(File file) {
        if (file != null && file.exists() && file.isFile()) {
            file.delete();
        }
    }
}