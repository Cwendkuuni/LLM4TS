package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File sampleFile = new File("sample.html"); // Replace with the path to your test HTML file
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(sampleFile, charset, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadFromFileWithInvalidCharset() throws IOException {
        File sampleFile = new File("sample.html");
        String charset = "InvalidCharset";
        String baseUri = "http://example.com";

        assertThrows(IOException.class, () -> DataUtil.load(sampleFile, charset, baseUri));
    }

    @Test
    public void testLoadFromFileWithNullCharset() throws IOException {
        File sampleFile = new File("sample.html");
        String baseUri = "http://example.com";

        Document document = DataUtil.load(sampleFile, null, baseUri);
        assertNotNull(document); // Default charset will be used
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String htmlContent = "Your HTML content here...";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, charset, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadFromInputStreamWithNullCharset() throws IOException {
        String htmlContent = "Your HTML content here...";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, null);
        assertNotNull(document); // Default charset will be used
    }

    @Test
    public void testLoadFromInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "Your HTML content here...";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charset = "InvalidCharset";
        String baseUri = "http://example.com";

        assertThrows(IOException.class, () -> DataUtil.load(inputStream, charset));
    }

    @Test
    public void testLoadFromInputStreamWithEmptyContent() throws IOException {
        String charset = "UTF-8";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, charset, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), null, null, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"GBK\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), "GBK", null, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithNullCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), null, null, Parser.htmlParser());
        assertNotNull(document); // Default charset will be used
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String htmlContent = "Your HTML content here...";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteData);
    }

    @Test
    public void testReadToByteBufferWithEmptyContent() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteData);
    }
}