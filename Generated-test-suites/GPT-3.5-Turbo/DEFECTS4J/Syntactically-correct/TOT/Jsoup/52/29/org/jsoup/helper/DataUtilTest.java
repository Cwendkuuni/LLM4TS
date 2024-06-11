package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DataUtilTest {

    private static final String BASE_URI = "https://www.example.com";

    @Test
    public void testLoadFile() {
        File file = new File("test.html");
        try {
            Document document = DataUtil.load(file, "UTF-8", BASE_URI);
            assertEquals("UTF-8", document.outputSettings().charset().name());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadInputStream() {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        try {
            Document document = DataUtil.load(inputStream, "UTF-8", BASE_URI);
            assertEquals("UTF-8", document.outputSettings().charset().name());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        InputStream inputStream = getClass().getResourceAsStream("/test.xml");
        try {
            Document document = DataUtil.load(inputStream, "UTF-8", BASE_URI, Parser.xmlParser());
            assertEquals("UTF-8", document.outputSettings().charset().name());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCrossStreams() {
        InputStream inputStream = getClass().getResourceAsStream("/test.xml");
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            DataUtil.crossStreams(inputStream, outputStream);
            assertEquals(343, outputStream.toByteArray().length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseByteData() {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();

            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", BASE_URI, Parser.htmlParser());
            assertEquals("UTF-8", document.outputSettings().charset().name());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadToByteBuffer() {
        InputStream inputStream = getClass().getResourceAsStream("/test.xml");
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1000);
            assertEquals(1000, byteBuffer.capacity());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadToFile() {
        File file = new File("test.xml");
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");

            ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
            assertEquals((int) randomAccessFile.length(), byteBuffer.capacity());

            randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertNull(validatedCharset);

        charset = "UTF-8";
        validatedCharset = DataUtil.validateCharset(charset);
        assertEquals("UTF-8", validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertEquals(32, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.put((byte) 0xEF);
        byteBuffer.put((byte) 0xBB);
        byteBuffer.put((byte) 0xBF);
        byteBuffer.put((byte) 0x41);

        String charset = DataUtil.detectCharsetFromBom(byteBuffer, null);
        assertEquals("UTF-8", charset);
    }
}