package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoad_File_CharsetName_BaseUri() throws IOException {
        // Test case 1: Test loading a file with UTF-8 charset and base URI
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());

        // Test case 2: Test loading a file with ISO-8859-1 charset and base URI
        charsetName = "ISO-8859-1";
        document = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());

        // Test case 3: Test loading a file with null charset and base URI
        charsetName = null;
        document = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testLoad_InputStream_CharsetName_BaseUri() throws IOException {
        // Test case 1: Test loading an input stream with UTF-8 charset and base URI
        String input = "<html><head><title>Test</title></head><body><h1>Hello world!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());

        // Test case 2: Test loading an input stream with ISO-8859-1 charset and base URI
        inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.ISO_8859_1));
        charsetName = "ISO-8859-1";
        document = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());

        // Test case 3: Test loading an input stream with null charset and base URI
        inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        charsetName = null;
        document = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());

    }

    @Test
    public void testLoad_InputStream_Charset_BaseUri_Parser() throws IOException {
        // Test case 1: Test loading an input stream with UTF-8 charset, base URI, and HTML parser
        String input = "<html><head><title>Test</title></head><body><h1>Hello world!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());

        // Test case 2: Test loading an input stream with ISO-8859-1 charset, base URI, and XML parser
        inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.ISO_8859_1));
        charsetName = "ISO-8859-1";
        parser = Parser.xmlParser();
        document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());

        // Test case 3: Test loading an input stream with null charset, base URI, and HTML parser
        inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        charsetName = null;
        parser = Parser.htmlParser();
        document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseByteData() {
        // Test case 1: Test parsing byte data with UTF-8 charset, base URI, and HTML parser
        String input = "<html><head><title>Test</title></head><body><h1>Hello world!</h1></body></html>";
        ByteBuffer byteData = StandardCharsets.UTF_8.encode(input);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());

        // Test case 2: Test parsing byte data with ISO-8859-1 charset, base URI, and XML parser
        byteData = StandardCharsets.ISO_8859_1.encode(input);
        charsetName = "ISO-8859-1";
        parser = Parser.xmlParser();
        document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());

        // Test case 3: Test parsing byte data with null charset, base URI, and HTML parser
        byteData = StandardCharsets.UTF_8.encode(input);
        charsetName = null;
        parser = Parser.htmlParser();
        document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());

    }

    @Test
    public void testReadToByteBuffer() {
        // Test case 1: Test reading input stream to byte buffer
        String input = "<html><head><title>Test</title></head><body><h1>Hello world!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Assert.assertNotNull(byteData);
        Assert.assertEquals(input.length(), byteData.capacity());

        // Test case 2: Test reading empty input stream to byte buffer
        inputStream = new ByteArrayInputStream("".getBytes());
        byteData = DataUtil.readToByteBuffer(inputStream);
        Assert.assertNotNull(byteData);
        Assert.assertEquals(0, byteData.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Test case 1: Test getting charset from content type header with valid charset
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNotNull(charset);
        Assert.assertEquals("UTF-8", charset);

        // Test case 2: Test getting charset from content type header without charset
        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);

        // Test case 3: Test getting charset from content type header with invalid charset
        contentType = "text/html; charset=invalidcharset";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }
}