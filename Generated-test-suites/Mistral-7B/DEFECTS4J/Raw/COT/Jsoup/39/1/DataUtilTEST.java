package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    /**
     * Test method for {@link DataUtil#load(File, String, String)}.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadFile() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        File file = new File("testFile.html");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(htmlContent);
        fileWriter.close();

        Document document = DataUtil.load(file, "UTF-8", "");
        assertEquals("Hello World", document.title());

        file.delete();
    }

    /**
     * Test method for {@link DataUtil#load(File, String, String)}.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadFileInvalidCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-16LE\"></head><body><h1>Hello World</h1></body></html>";
        File file = new File("testFile.html");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(htmlContent);
        fileWriter.close();

        try {
            Document document = DataUtil.load(file, "UTF-8", "");
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }

        file.delete();
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}.
     */
    @Test
    public void testLoadInputStream() {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());

        Document document = null;
        try {
            document = DataUtil.load(inputStream, "UTF-8", "");
        } finally {
            inputStream.close();
        }

        assertEquals("Hello World", document.title());
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}.
     */
    @Test
    public void testLoadInputStreamInvalidCharset() {
        String htmlContent = "<html><head><meta charset=\"UTF-16LE\"></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());

        try {
            Document document = DataUtil.load(inputStream, "UTF-8", "");
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        } finally {
            inputStream.close();
        }
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)}.
     */
    @Test
    public void testLoadInputStreamParser() {
        String htmlContent = "<xml><root/></xml>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());

        Document document = null;
        try {
            document = DataUtil.load(inputStream, "UTF-8", "", Parser.xmlParser());
        } finally {
            inputStream.close();
        }

        assertNotNull(document);
        assertEquals("root", document.title());
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)}.
     */
    @Test
    public void testParseByteData() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<xml><root/></xml>".getBytes());
        byteBuffer.flip();

        Document document = null;
        try {
            document = DataUtil.parseByteData(byteBuffer, "UTF-8", "", Parser.xmlParser());
        } finally {
            byteBuffer.clear();
        }

        assertNotNull(document);
        assertEquals("root", document.title());
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)}.
     */
    @Test
    public void testParseByteDataInvalidCharset() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("<?xml version=\"1.0\" encoding=\"UTF-16LE\"?>\n<xml><root/></xml>".getBytes());
        byteBuffer.flip();

        try {
            Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", "", Parser.xmlParser());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        } finally {
            byteBuffer.clear();
        }
    }

    /**
     * Test method for {@link DataUtil#readToByteBuffer(InputStream, int)}.
     */
    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new ByteArrayInputStream("<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>".getBytes()), 0);

        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>", Charset.forName("UTF-8").decode(byteBuffer).toString());
    }

    /**
     * Test method for {@link DataUtil#readToByteBuffer(InputStream, int)}.
     */
    @Test
    public void testReadToByteBufferMaxSize() throws IOException {
        ByteBuffer byteBuffer = null;
        try (InputStream inputStream = new ByteArrayInputStream("<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>".getBytes());) {
            byteBuffer = DataUtil.readToByteBuffer(inputStream, 10);
        }

        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>", Charset.forName("UTF-8").decode(byteBuffer).toString());
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)}.
     */
    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=\"UTF-8\""));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8; charset=gb2312"));
        assertEquals("gb2312", DataUtil.getCharsetFromContentType("text/html; charset=gb2312"));
        assertEquals(null, DataUtil.getCharsetFromContentType("text/html"));
    }

}