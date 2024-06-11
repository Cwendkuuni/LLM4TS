package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Locale;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser.htmlParser;

public class DataUtilTest {

    @Test
    public void testLoad() throws Exception {
        String contentType = "text/html; charset=UTF-8";
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        String html = "<html><head><meta charset='UTF-8'></head><body>Hello, world!</body></html>";

        InputStream inputStream = new ByteArrayInputStream(html.getBytes(Charset.forName(charset)));
        Document document = DataUtil.load(inputStream, charset, baseUri);
        assertNotNull(document);
        assertEquals(html, document.html());

        // Test with null charset
        charset = null;
        document = DataUtil.load(inputStream, charset, baseUri);
        assertNotNull(document);

        // Test with invalid charset
        charset = "InvalidCharset";
        document = DataUtil.load(inputStream, charset, baseUri);
        assertNotNull(document);

        // Test with no charset in content type header
        contentType = "text/html";
        document = DataUtil.load(inputStream, null, baseUri);
        assertNotNull(document);

        // Test with charset in content type header
        inputStream = new ByteArrayInputStream(html.getBytes(Charset.forName(contentType)));
        document = DataUtil.load(inputStream, null, baseUri);
        assertEquals(charset, document.outputSettings().charset());
    }

    @Test
    public void testParseByteData() throws Exception {
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = mock(Parser.htmlParser.class);
        Document document;
        String html = "<html><head><meta charset='UTF-8'></head><body>Hello, world!</body></html>";

        when(parser.parseInput(html, baseUri)).thenReturn(Document.createFromEncodedString(html, charset));

        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(Charset.forName(charset)));
        document = DataUtil.parseByteData(byteData, charset, baseUri, parser);
        assertNotNull(document);
        assertEquals(html, document.html());

        // Test with null charset
        charset = null;
        byteData = ByteBuffer.wrap(html.getBytes(Charset.forName(charset)));
        document = DataUtil.parseByteData(byteData, charset, baseUri, parser);
        assertNotNull(document);

        // Test with invalid charset
        charset = "InvalidCharset";
        byteData = ByteBuffer.wrap(html.getBytes(Charset.forName(charset)));
        document = DataUtil.parseByteData(byteData, charset, baseUri, parser);
        assertNotNull(document);

        // Test with charset determined from meta tag
        byteData = ByteBuffer.wrap(html.getBytes(Charset.forName(charset)));
        when(parser.parseInput(html, baseUri)).thenReturn(null);
        document = DataUtil.parseByteData(byteData, null, baseUri, parser);
        assertEquals(charset, document.outputSettings().charset());
    }

    @Test
    public void testGetCharsetFromContentType() throws Exception {
        String contentType = "text/html; charset=UTF-8";
        String charset = "UTF-8";

        assertEquals(charset, DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testReadToByteBuffer() throws Exception {
        String filePath = "test.html";
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        String html = "<html><head><meta charset='UTF-8'></head><body>Hello, world!</body></html>";
        File file = new File(filePath);
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(html.getBytes(Charset.forName(charset)));
        outputStream.close();

        FileInputStream inputStream = new FileInputStream(file);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 0);
        assertNotNull(byteData);

        // Test with maxSize > 0
        byteData = DataUtil.readToByteBuffer(inputStream, html.length() + 100);
        byte[] bytes = byteData.array();
        assertEquals(html.length() + 100, bytes.length);

        // Test with maxSize = 0
        byteData = DataUtil.readToByteBuffer(inputStream);
        bytes = byteData.array();
        assertEquals(html.length(), bytes.length);

        inputStream.close();
        file.delete();
    }
}