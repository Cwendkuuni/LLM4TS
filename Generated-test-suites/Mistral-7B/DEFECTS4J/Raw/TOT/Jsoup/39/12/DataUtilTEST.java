package org.jsoup.helper.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;

public class DataUtilTest {

    private Parser parser;

    @Before
    public void setup() {
        parser = Parser.htmlParser();
    }

    @After
    public void teardown() {
        parser = null;
    }

    @Test
    public void testLoadFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        File file = new File("test.html");
        Document document = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(document);

        // Test with null charsetName
        Document documentNullCharset = DataUtil.load(file, null, baseUri);
        Assert.assertNotNull(documentNullCharset);

        // Test with unknown charsetName
        Document documentUnknownCharset = DataUtil.load(file, "unknown-charset", baseUri);
        Assert.assertNotNull(documentUnknownCharset);

        // Test with empty file
        File emptyFile = new File("empty.html");
        Document documentEmpty = DataUtil.load(emptyFile, charsetName, baseUri);
        Assert.assertNull(documentEmpty);

        // Test with file not found
        File notFoundFile = new File("nonexistent.html");
        Document documentNotFound = DataUtil.load(notFoundFile, charsetName, baseUri);
        Assert.assertNull(documentNotFound);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(document);

        // Test with null inputStream
        Document documentNullInputStream = DataUtil.load(null, charsetName, baseUri);
        Assert.assertNull(documentNullInputStream);

        // Test with null charsetName
        Document documentNullCharset = DataUtil.load(inputStream, null, baseUri);
        Assert.assertNotNull(documentNullCharset);

        // Test with unknown charsetName
        Document documentUnknownCharset = DataUtil.load(inputStream, "unknown-charset", baseUri);
        Assert.assertNotNull(documentUnknownCharset);

        // Test with empty inputStream
        Document documentEmpty = DataUtil.load(new ByteArrayInputStream(new byte[0]), charsetName, baseUri);
        Assert.assertNull(documentEmpty);
    }

    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        String testData = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-16LE'></head><body>Test</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        Assert.assertNotNull(document);

        // Test with null charsetName
        Document documentNullCharset = DataUtil.parseByteData(byteData, null, baseUri, parser);
        Assert.assertNotNull(documentNullCharset);

        // Test with unknown charsetName
        Document documentUnknownCharset = DataUtil.parseByteData(byteData, "unknown-charset", baseUri, parser);
        Assert.assertNotNull(documentUnknownCharset);

        // Test with null baseUri
        Document documentNullBaseUri = DataUtil.parseByteData(byteData, charsetName, null, parser);
        Assert.assertNotNull(documentNullBaseUri);

        // Test with null parser
        Document documentNullParser = DataUtil.parseByteData(byteData, charsetName, baseUri, null);
        Assert.assertNull(documentNullParser);

        // Test with invalid charsetName
        Document documentInvalidCharset = DataUtil.parseByteData(byteData, "invalid-charset", baseUri, parser);
        Assert.assertNull(documentInvalidCharset);

        // Test with empty byteData
        ByteBuffer emptyByteData = ByteBuffer.allocate(0);
        Document documentEmpty = DataUtil.parseByteData(emptyByteData, charsetName, baseUri, parser);
        Assert.assertNull(documentEmpty);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "Test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Assert.assertEquals(testData.length(), byteData.limit());
        Assert.assertEquals(testData, Charset.forName(StandardCharsets.UTF_8.name()).decode(byteData).toString());

        // Test with null inputStream
        ByteBuffer byteDataNull = DataUtil.readToByteBuffer(null);
        Assert.assertEquals(0, byteDataNull.limit());

        // Test with empty inputStream
        ByteBuffer byteDataEmpty = DataUtil.readToByteBuffer(new ByteArrayInputStream(new byte[0]));
        Assert.assertEquals(0, byteDataEmpty.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);

        // Test with null contentType
        String charsetNull = DataUtil.getCharsetFromContentType(null);
        Assert.assertNull(charsetNull);

        // Test with empty contentType
        String charsetEmpty = DataUtil.getCharsetFromContentType("");
        Assert.assertNull(charsetEmpty);

        // Test with contentType without charset
        String charsetNoCharset = DataUtil.getCharsetFromContentType("text/html");
        Assert.assertEquals(DataUtil.defaultCharset, charsetNoCharset);
    }
}