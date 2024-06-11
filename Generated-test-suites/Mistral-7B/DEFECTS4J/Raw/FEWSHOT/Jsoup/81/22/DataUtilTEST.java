package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        String filePath = "path/to/your/test/file.html";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(new File(filePath), charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadDocumentFromInvalidFile() {
        File invalidFile = new File("path/to/non-existent/file");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        try {
            DataUtil.load(invalidFile, charsetName, baseUri);
            assert False : "Expected IOException when loading non-existent file";
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadDocumentFromInputStream() throws IOException {
        String htmlContent = "<html lang=\"en\"><head><meta charset=\"UTF-8\"></head><body><p>Hello World!</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadDocumentFromInvalidInputStream() {
        String content = "";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        try {
            DataUtil.load(inputStream, charsetName, baseUri);
            assert False : "Expected IOException when loading empty input stream";
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testParseInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = new ByteArrayInputStream("<html lang=\"en\"><head><meta charset=\"UTF-8\"></head><body><p>Hello World!</p></body></html>".getBytes(StandardCharsets.UTF_8));
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "invalidCharset";
        String baseUri = "http://example.com";
        InputStream inputStream = new ByteArrayInputStream("<html lang=\"en\"><head><meta charset=\"UTF-8\"></head><body><p>Hello World!</p></body></html>".getBytes(StandardCharsets.UTF_8));
        Parser parser = Parser.htmlParser();

        try {
            DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
            assert False : "Expected IOException when parsing input stream with an unsupported charset";
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testParseInputStreamWithEmptyInput() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        assertNull(document);
    }
}