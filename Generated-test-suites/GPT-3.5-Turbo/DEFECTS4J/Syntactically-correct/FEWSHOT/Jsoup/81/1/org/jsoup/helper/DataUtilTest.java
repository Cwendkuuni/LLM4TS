package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {
    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "";

        Document doc = DataUtil.load(file, charsetName, baseUri);

        assertNotNull(doc);
        assertEquals(1, doc.children().size());
        Element html = doc.child(0);
        assertNotNull(html);
        assertEquals("html", html.tagName());
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "";

        Document doc = DataUtil.load(inputStream, charsetName, baseUri);

        assertNotNull(doc);
        assertEquals(1, doc.children().size());
        Element html = doc.child(0);
        assertNotNull(html);
        assertEquals("html", html.tagName());
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.xml");
        String charsetName = "UTF-8";
        String baseUri = "";
        Parser parser = Parser.xmlParser();

        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);

        assertNotNull(doc);
        assertEquals(1, doc.children().size());
        Element root = doc.child(0);
        assertNotNull(root);
        assertEquals("root", root.tagName());
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new FileInputStream("input.txt");
        OutputStream outputStream = new FileOutputStream("output.txt");

        DataUtil.crossStreams(inputStream, outputStream);

        InputStream inputStream2 = new FileInputStream("output.txt");
        byte[] buffer = new byte[1024];
        int len = inputStream2.read(buffer);

        String fileContent = new String(buffer, 0, len);
        assertEquals("Hello World!", fileContent);
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);

        assertNotNull(doc);
        assertEquals(1, doc.children().size());
        Element html = doc.child(0);
        assertNotNull(html);
        assertEquals("html", html.tagName());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        File file = new File("test.html");
        InputStream inputStream = new FileInputStream(file);

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        assertNotNull(byteBuffer);
        assertEquals(140, byteBuffer.capacity());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);

        assertNotNull(byteBuffer);
        assertEquals(140, byteBuffer.capacity());
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();

        assertNotNull(mimeBoundary);
        assertEquals(32, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 'H', 'e', 'l', 'l', 'o'});
        
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);

        assertNotNull(bomCharset);
        assertEquals("UTF-8", bomCharset.charset);
        assertEquals(true, bomCharset.offset);
    }
}