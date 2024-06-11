package org.jsoup.helper;

import static org.junit.Assert.*;
import static org.jsoup.helper.DataUtil.getDefaultCharset;
import static org.jsoup.helper.DataUtil.getCharsetFromContentType;
import static org.jsoup.parser.Parser.htmlParser;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        File testFile = new File("path/to/your/testfile.html");
        String charsetName = "UTF-8"; // replace with the actual charset if known
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(testFile, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadDocumentFromInputStream() throws IOException {
        String htmlData = "<html><body><p>Test Data</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlData.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals(htmlData, doc.body().html());
    }

    @Test
    public void testParseByteData() throws IOException {
        String htmlData = "<html><body><p>Test Data</p></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(htmlData.getBytes("UTF-8"));
        ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());

        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = htmlParser();

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals(htmlData, doc.body().html());
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        String htmlData = "<html><head><meta charset=\"gb2312\"></head><body><p>Test Data</p></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(htmlData.getBytes("UTF-8"));
        ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());

        String baseUri = "http://example.com";
        Parser parser = htmlParser();

        Document doc = DataUtil.parseByteData(byteData, null, baseUri, parser);
        assertNotNull(doc);
        assertEquals(htmlData, doc.body().html());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=gb2312";
        String expectedCharset = "gb2312";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNull() {
        String contentType = null;

        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}