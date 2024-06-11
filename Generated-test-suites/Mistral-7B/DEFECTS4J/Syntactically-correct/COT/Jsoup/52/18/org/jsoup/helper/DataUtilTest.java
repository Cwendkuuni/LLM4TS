import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File testFile = new File("testFile.html");
        Document document = DataUtil.load(testFile, "UTF-8", "");
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String htmlCode = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlCode.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", "");
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadWithParser() throws IOException {
        String htmlCode = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlCode.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", "", Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("input.html");
        File outputFile = new File("output.html");

        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        InputStream outputInputStream = new FileInputStream(outputFile);
        Document document = DataUtil.load(outputInputStream, "UTF-8", "");
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String htmlCode = "\uFEFF<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlCode.getBytes());
        Document document = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String htmlCode = "\uFEFF<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlCode.getBytes());
        Document document = DataUtil.parseByteData(byteData, "UTF-8", "", Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        String htmlCode = "\uFEFF<html><head><meta charset=\"InvalidCharset\"></meta></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlCode.getBytes());
        Document document = DataUtil.parseByteData(byteData, "InvalidCharset", "", Parser.htmlParser());
        Assert.assertNull(document);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNullContentType() {
        String contentType = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(boundary);
        Assert.assertEquals(boundary.length(), DataUtil.boundaryLength);
    }
}