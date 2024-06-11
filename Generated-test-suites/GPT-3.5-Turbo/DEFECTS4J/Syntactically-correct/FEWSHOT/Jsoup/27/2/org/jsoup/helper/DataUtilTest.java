package org.jsoup.helper;

import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {
    private final static String TEST_HTML = "<html><head><title>Test HTML</title></head><body><h1>Hello, World!</h1></body></html>";

    private File createTempFile(String content) throws IOException {
        File tempFile = File.createTempFile("temp", ".html");
        tempFile.deleteOnExit();
        Files.write(tempFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
        return tempFile;
    }

    @Test
    public void testLoad_withFile() throws IOException {
        String html = "<html><head><title>Test HTML</title></head><body><h1>Hello, World!</h1></body></html>";
        File tempFile = createTempFile(html);

        Document document = DataUtil.load(tempFile, null, "");
        String loadedHtml = document.html();

        assertEquals(html, loadedHtml);
    }

    @Test
    public void testLoad_withInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_HTML.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, null, "");
        String loadedHtml = document.html();

        assertEquals(TEST_HTML, loadedHtml);
    }

    @Test
    public void testLoad_withInputStreamAndParser() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_HTML.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, null, "", Parser.xmlParser());
        String loadedHtml = document.html();

        assertEquals(TEST_HTML, loadedHtml);
    }

    @Test
    public void testParseByteData_withCharsetFromMeta() {
        String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body><h1>Hello, World!</h1></body></html>";

        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        String loadedHtml = document.html();

        assertEquals(html, loadedHtml);
    }
    
    @Test
    public void testParseByteData_withSpecifiedCharset() {
        String html = "<html><head><title>Test HTML</title></head><body><h1>Hello, World!</h1></body></html>";

        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteData, "UTF-8", "", Parser.htmlParser());
        String loadedHtml = document.html();

        assertEquals(html, loadedHtml);
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        String content = "Hello, World!";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        String loadedContent = new String(byteData.array(), StandardCharsets.UTF_8);

        assertEquals(content, loadedContent);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", charset);
    }
}