package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {

    private final String baseUri = "http://example.com";

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", baseUri);
        Document jsoupDoc = Jsoup.parse(file, "UTF-8", baseUri);
        
        Assert.assertEquals(doc.html(), jsoupDoc.html());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");
        Document doc = DataUtil.load(in, "UTF-8", baseUri);
        Document jsoupDoc = Jsoup.parse(in, "UTF-8", baseUri);
        
        Assert.assertEquals(doc.html(), jsoupDoc.html());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream in = new FileInputStream("test.html");
        Document doc = DataUtil.load(in, "UTF-8", baseUri, Parser.htmlParser());
        Document jsoupDoc = Jsoup.parse(in, "UTF-8", baseUri);
        
        Assert.assertEquals(doc.html(), jsoupDoc.html());
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new FileInputStream("test.html");
        OutputStream out = new ByteArrayOutputStream();
        
        DataUtil.crossStreams(in, out);
        
        Assert.assertNotNull(out);
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");
        Document doc = DataUtil.parseInputStream(in, "UTF-8", baseUri, Parser.htmlParser());
        Document jsoupDoc = Jsoup.parse(in, "UTF-8", baseUri);
        
        Assert.assertEquals(doc.html(), jsoupDoc.html());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream in = new FileInputStream("test.html");
        ByteBuffer buffer = DataUtil.readToByteBuffer(in, 0);
        byte[] expectedBytes = Files.readAllBytes(Paths.get("test.html"));
        
        byte[] actualBytes = new byte[buffer.remaining()];
        buffer.get(actualBytes);
        
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer buffer = DataUtil.readFileToByteBuffer(file);
        byte[] expectedBytes = Files.readAllBytes(Paths.get("test.html"));
        
        byte[] actualBytes = new byte[buffer.remaining()];
        buffer.get(actualBytes);
        
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer buffer = DataUtil.emptyByteBuffer();
        
        Assert.assertEquals(0, buffer.remaining());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNull() {
        String charset = DataUtil.getCharsetFromContentType(null);
        
        Assert.assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        
        Assert.assertNotNull(boundary);
        Assert.assertEquals(32, boundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 'T', 'e', 's', 't'});
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        
        Assert.assertEquals("UTF-8", bomCharset.charset);
        Assert.assertTrue(bomCharset.offset);
    }

    @Test
    public void testEmptyTestCase() {
        // Empty test case
    }
}