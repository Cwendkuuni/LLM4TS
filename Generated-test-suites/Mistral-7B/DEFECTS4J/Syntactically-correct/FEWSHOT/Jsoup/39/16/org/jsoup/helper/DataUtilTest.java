package org.jsoup.helper;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String filePath = "src/test/resources/dataUtilTestFile.html";
        File file = new File(filePath);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals(charsetName, doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals(charsetName, doc.outputSettings().charset());
    }

    @Test
    public void testParseByteData() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(charsetName, doc.outputSettings().charset());

        // Test charset detection
        String htmlContentWithCharset = "<html><head><meta charset=\"GBK\"><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><h1>Test</h1></body></html>";
        ByteBuffer byteDataWithCharset = ByteBuffer.wrap(htmlContentWithCharset.getBytes());
        Document docWithCharsetDetection = DataUtil.parseByteData(byteDataWithCharset, null, baseUri, Parser.htmlParser());
        assertEquals("GBK", docWithCharsetDetection.outputSettings().charset());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String filePath = "src/test/resources/dataUtilTestFile.html";
        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        ByteBuffer expectedByteData = ByteBuffer.wrap(Files.readAllBytes(new File("src/test/resources/dataUtilTestFile.html").toPath()));
        assertEquals(expectedByteData, byteData);
        inputStream.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        assertEquals(expectedCharset, DataUtil.getCharsetFromContentType(contentType));

        String contentTypeWithoutCharset = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentTypeWithoutCharset));
    }
}