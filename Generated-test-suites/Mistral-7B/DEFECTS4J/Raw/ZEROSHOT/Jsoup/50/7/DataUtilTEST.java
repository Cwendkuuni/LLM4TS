package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * Test class for DataUtil.java
 *
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File("test.html"); // Replace with the path to your test file
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(testFile, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testData = "Test Data";
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        outStream.write(testData.getBytes(StandardCharsets.UTF_8));
        InputStream inStream = new ByteArrayInputStream(outStream.toByteArray());

        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(inStream, charsetName, baseUri);
        assertNotNull(doc);
        assertTrue(doc.body().text().contains(testData));

        inStream.close();
    }

    @Test
    public void testParseByteData() throws IOException {
        String testData = "Test Data";
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        outStream.write(testData.getBytes(StandardCharsets.UTF_8));
        byte[] data = outStream.toByteArray();

        ByteBuffer byteData = ByteBuffer.wrap(data);
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertNotNull(doc);
        assertTrue(doc.body().text().contains(testData));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=EUC-JP";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertTrue(charset != null && charset.equals("EUC-JP"));

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertTrue(charset == null);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertTrue(boundary.length() == 32);
        assert(boundary.matches("[-_0-9a-zA-Z]+"));
    }

    @Test
    public void testCrossStreams() throws IOException {
        String testData = "Test Data";
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        outStream.write(testData.getBytes(StandardCharsets.UTF_8));
        InputStream inStream = new ByteArrayInputStream(outStream.toByteArray());
        ByteArrayOutputStream outStream2 = new ByteArrayOutputStream();

        DataUtil.crossStreams(inStream, outStream2);
        byte[] result = outStream2.toByteArray();
        assertTrue(new String(result, StandardCharsets.UTF_8).contains(testData));

        inStream.close();
        outStream.close();
        outStream2.close();
    }
}