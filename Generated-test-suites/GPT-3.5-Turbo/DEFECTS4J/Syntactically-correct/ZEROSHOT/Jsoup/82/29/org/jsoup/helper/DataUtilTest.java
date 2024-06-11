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

    public static final String FILE_PATH = "test.html";

    @Test
    public void testLoad_withFile() throws IOException {
        File file = new File(FILE_PATH);
        Document document = DataUtil.load(file, "UTF-8", FILE_PATH);
        Assert.assertNotNull(document);
        Assert.assertEquals("UTF-8", document.outputSettings().charset().name());
    }

    @Test
    public void testLoad_withInputStream() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(FILE_PATH);
        Document document = DataUtil.load(fileInputStream, "UTF-8", FILE_PATH);
        Assert.assertNotNull(document);
        Assert.assertEquals("UTF-8", document.outputSettings().charset().name());
    }

    @Test
    public void testLoad_withParser() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(FILE_PATH);
        Document document = DataUtil.load(fileInputStream, "UTF-8", FILE_PATH, Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals("UTF-8", document.outputSettings().charset().name());
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream input = new FileInputStream(FILE_PATH);
        // Output stream is not tested here as the test cannot check for correctness of output
        DataUtil.crossStreams(input, System.out);
    }

    @Test
    public void testParseInputStream() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(FILE_PATH);
        Document document = DataUtil.parseInputStream(fileInputStream, "UTF-8", FILE_PATH, Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals("UTF-8", document.outputSettings().charset().name());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(FILE_PATH);
        byte[] bytes = new byte[fileInputStream.available()];
        fileInputStream.read(bytes);

        InputStream inputStream = new FileInputStream(FILE_PATH);
        Document document = Jsoup.parse(inputStream, "UTF-8", "");
        byte[] bytesFromDocument = document.toString().getBytes("UTF-8");

        Assert.assertArrayEquals(bytes, bytesFromDocument);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        Assert.assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(mimeBoundary);
        Assert.assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.put((byte) 0xEF);
        byteBuffer.put((byte) 0xBB);
        byteBuffer.put((byte) 0xBF);
        byteBuffer.put((byte) 0x41);
        byteBuffer.rewind();

        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        Assert.assertNotNull(bomCharset);
        Assert.assertEquals("UTF-8", bomCharset.charset);
        Assert.assertTrue(bomCharset.offset);
    }
}