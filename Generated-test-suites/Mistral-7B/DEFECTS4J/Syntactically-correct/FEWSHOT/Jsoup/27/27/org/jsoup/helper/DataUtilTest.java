package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "This is a test <html> file";
        File file = new File("test.html");
        FileUtils.writeStringToFile(file, fileContent, "UTF-8");

        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        Document document = DataUtil.load(file, charsetName, baseUri);
        assertEquals("This is a test <html> file", document.body().html());

        FileUtils.deleteQuietly(file);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "This is a test <html> file";
        byte[] inputStreamBytes = inputStreamContent.getBytes("UTF-8");
        InputStream inputStream = new ByteArrayInputStream(inputStreamBytes);

        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertEquals("This is a test <html> file", document.body().html());

        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithUnknownCharset() throws IOException {
        String inputStreamContent = "This is a test <html> file";
        byte[] inputStreamBytes = inputStreamContent.getBytes("ISO-8859-1");
        InputStream inputStream = new ByteArrayInputStream(inputStreamBytes);

        String charsetName = null; // or any other value, it will be detected automatically
        String baseUri = "http://test.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertEquals("This is a test <html> file", document.body().html());

        inputStream.close();
    }

    @Test
    public void testParseByteData() throws IOException {
        String testData = "This is a test <html> file\uFEFF";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes("UTF-8"));

        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertEquals("This is a test <html> file\uFEFF", document.body().html());

        document = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());
        assertEquals("This is a test <html> file", document.body().html());
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        String testData = "This is a test <html> file\uFEFF";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes("UTF-8"));

        String charsetName = null;
        String baseUri = "http://test.com";

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertEquals("This is a test <html> file", document.body().html());

        // BOM should be removed
        assertEquals("This is a test <html>", document.body().html().substring(0, document.body().html().length() - 1));
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "This is a test <html> file\uFEFF";
        byte[] inputBytes = testData.getBytes("UTF-8");
        InputStream inputStream = new ByteArrayInputStream(inputBytes);

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(inputBytes.length, byteData.limit());

        inputStream.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }
}