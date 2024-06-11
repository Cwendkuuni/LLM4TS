package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, "UTF-8", baseUri);

        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, "UTF-8", baseUri);

        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String html = "<html><head></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        Document doc = DataUtil.load(inputStream, "UTF-8", baseUri, parser);

        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String text = "Hello, World!";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        int maxSize = 100;
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        assertNotNull(byteBuffer);
        assertEquals(text, new String(byteBuffer.array(), StandardCharsets.UTF_8));
    }

}