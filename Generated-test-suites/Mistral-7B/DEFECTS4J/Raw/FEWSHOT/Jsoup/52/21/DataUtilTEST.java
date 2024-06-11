package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        String xmlData = "<html><head></head><body></body></html>";
        File file = new File("temp.xml");
        FileUtil.writeStringToFile(file, xmlData, "UTF-8");

        Document doc = DataUtil.load(file, null, null);
        assertEquals("<html><head></head><body></body></html>", doc.html());
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        String xmlData = "<html><head></head><body></body></html>";
        File file = new File("temp.xml");
        FileUtil.writeStringToFile(file, xmlData, "UTF-8");

        Document doc = DataUtil.load(file, "UTF-16LE", null);
        assertNull(doc);

        doc = DataUtil.load(file, "UTF-8", null);
        assertEquals("<html><head></head><body></body></html>", doc.html());
    }

    @Test
    public void testLoadStreamWithDefaultCharset() throws IOException {
        String xmlData = "<html><head></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlData.getBytes("UTF-8"));

        Document doc = DataUtil.load(inputStream, null, null);
        assertEquals("<html><head></head><body></body></html>", doc.html());
    }

    @Test
    public void testLoadStreamWithSpecifiedCharset() throws IOException {
        String xmlData = "<html><head></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlData.getBytes("UTF-8"));

        Document doc = DataUtil.load(inputStream, "UTF-16LE", null);
        assertNull(doc);

        doc = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals("<html><head></head><body></body></html>", doc.html());
    }

    @Test
    public void testLoadStreamWithSpecifiedParser() throws IOException {
        String xmlData = "<html><head></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlData.getBytes("UTF-8"));

        Document doc = DataUtil.load(inputStream, null, null, Parser.xmlParser());
        assertEquals("<html><head></head><body></body></html>", doc.xml().asXML());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String xmlData = "<html><head></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlData.getBytes("UTF-8"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals(xmlData, outputStream.toString("UTF-8"));
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{"<", "h", "t", "m", "l"});
        String charsetName = null;

        Document doc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertNull(doc);

        charsetName = "UTF-8";
        doc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertEquals("", doc.html());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{"<", "h", "t", "m", "l"});
        String charsetName = "UTF-16LE";

        Document doc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertNull(doc);

        charsetName = "UTF-8";
        doc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertEquals("", doc.html());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String xmlData = "<html><head></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlData.getBytes("UTF-8"));

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byteData.rewind();
        outputStream.write(byteData.array(), 0, (int) byteData.limit());
        assertEquals(xmlData, outputStream.toString("UTF-8"));
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String xmlData = "<html><head></head><body></body></html>";
        File file = new File("temp.xml");
        FileUtil.writeStringToFile(file, xmlData, "UTF-8");

        ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byteData.rewind();
        outputStream.write(byteData.array(), 0, (int) byteData.limit());
        assertEquals(xmlData, outputStream.toString("UTF-8"));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String xmlData = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(xmlData));

        xmlData = "text/html; charset=UTF-16";
        assertEquals("UTF-16", DataUtil.getCharsetFromContentType(xmlData));

        xmlData = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(xmlData));

        xmlData = "text/html; charset=x-invalid";
        assertNull(DataUtil.getCharsetFromContentType(xmlData));
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData1 = ByteBuffer.wrap(new byte[]{0xFF, 0xFF, 0, 0, 0});
        assertEquals("UTF-32", DataUtil.detectCharsetFromBom(byteData1, null));

        ByteBuffer byteData2 = ByteBuffer.wrap(new byte[]{0xFF, 0xFE, 0, 0});
        assertEquals("UTF-16BE", DataUtil.detectCharsetFromBom(byteData2, null));

        ByteBuffer byteData3 = ByteBuffer.wrap(new byte[]{0, 0xFE, 0xFF, 0});
        assertEquals("UTF-16LE", DataUtil.detectCharsetFromBom(byteData3, null));

        ByteBuffer byteData4 = ByteBuffer.wrap(new byte[]{0xEF, 0xBB, 0xBF});
        assertEquals("UTF-8", DataUtil.detectCharsetFromBom(byteData4, null));

        ByteBuffer byteData5 = ByteBuffer.wrap(new byte[]{0, 0, 0, 0, 0});
        assertNull(DataUtil.detectCharsetFromBom(byteData5, null));
    }

    @Test
    public void testValidateCharset() {
        assertEquals("UTF-32", DataUtil.validateCharset("UTF-32"));
        assertEquals("UTF-16BE", DataUtil.validateCharset("UTF-16BE"));
        assertEquals("UTF-16LE", DataUtil.validateCharset("UTF-16LE"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8"));

        assertNull(DataUtil.validateCharset("UTF-HEX"));
        assertNull(DataUtil.validateCharset("UTF-HEX"));
        assertNull(DataUtil.validateCharset("UTF-HEX"));
    }

    @Test
    public void testMimeBoundary() {
        assertEquals("_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ----1234567890abcd123456", DataUtil.mimeBoundary());
    }
}