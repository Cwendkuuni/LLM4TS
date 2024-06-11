package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {
    private final String baseUri = "https://example.com";

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        String expectedCharset = "UTF-8";

        Document document = DataUtil.load(file, expectedCharset, baseUri);

        // TODO: Add assertions for loaded document
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String input = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        String expectedCharset = "UTF-8";

        Document document = DataUtil.load(inputStream, expectedCharset, baseUri);

        // TODO: Add assertions for loaded document
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        String input = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        String expectedCharset = "UTF-8";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.load(inputStream, expectedCharset, baseUri, parser);

        // TODO: Add assertions for loaded document
    }

    @Test
    public void testParseByteDataWithCharset() {
        String input = "<html><head></head><body></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(input.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String expectedCharset = charsetName;

        Document document = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, Parser.htmlParser());

        // TODO: Add assertions for parsed document
        assertEquals(expectedCharset, document.outputSettings().charset().name());
    }

    @Test
    public void testParseByteDataWithoutCharset() {
        String input = "<html><head></head><body></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(input.getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String expectedCharset = "UTF-8";

        Document document = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, Parser.htmlParser());

        // TODO: Add assertions for parsed document
        assertEquals(expectedCharset, document.outputSettings().charset().name());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String input = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(input.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        // TODO: Add assertions for byte buffer
        assertEquals(expectedByteBuffer, byteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        // TODO: Add assertions for extracted charset
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithoutCharset() {
        String contentType = "text/html";
        String expectedCharset = null;

        String charset = DataUtil.getCharsetFromContentType(contentType);

        // TODO: Add assertions for extracted charset
        assertEquals(expectedCharset, charset);
    }
}