package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File inputFile = new File("test.html");
        Document document = DataUtil.load(inputFile, "UTF-8", "");
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String htmlContent = "Test content";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(htmlContent.getBytes("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document document = DataUtil.load(inputStream, "UTF-8", "");
        Assert.assertNotNull(document);
        Assert.assertEquals(htmlContent, document.body().html());
    }

    @Test
    public void testLoadFromInputStreamWithCustomParser() throws IOException {
        String htmlContent = "<xml><tag>Test content</tag></xml>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(htmlContent.getBytes("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document document = DataUtil.load(inputStream, "UTF-8", "", DataUtil.Parser.xmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals("<xml><tag>Test content</tag></xml>", document.html());
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't', ' ', 'C', 'o', 'n', 't', 'e', 'n', 't', '\n'});
        DataUtil.crossStreams(inputStream, outputStream);
        Assert.assertEquals("Test content\n", outputStream.toString());
    }

    @Test
    public void testParseByteData() throws IOException {
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("test.html"));
        String defaultCharset = DataUtil.defaultCharset;
        Document document = DataUtil.parseByteData(byteData, defaultCharset, "", Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        byte data[] = {0x00, 0x00, (byte) 0xFE, (byte) 0xFF};
        byteData.put(data);
        String charsetFromBom = DataUtil.detectCharsetFromBom(byteData, null);
        Assert.assertEquals("UTF-32", charsetFromBom);

        byteData.clear();
        byteData.put(new byte[]{(byte) 0xFF, (byte) 0xFE, 0x00, 0x00});
        charsetFromBom = DataUtil.detectCharsetFromBom(byteData, null);
        Assert.assertEquals("UTF-16", charsetFromBom);

        byteData.clear();
        byteData.put(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
        charsetFromBom = DataUtil.detectCharsetFromBom(byteData, null);
        Assert.assertEquals("UTF-8", charsetFromBom);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(boundary);
        Assert.assertEquals(boundary.length(), DataUtil.boundaryLength);
        Assert.assertFalse(boundary.isEmpty());
    }
}