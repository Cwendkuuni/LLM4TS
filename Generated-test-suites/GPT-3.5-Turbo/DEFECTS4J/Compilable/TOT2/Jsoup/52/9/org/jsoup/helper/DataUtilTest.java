package org.jsoup.helper;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(document);
        // Add additional assertions if needed
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head></head><body>Hello World</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(document);
        // Add additional assertions if needed
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String html = "<html><head></head><body>Hello World</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        assertNotNull(document);
        // Add additional assertions if needed
    }

    @Test
    public void testCrossStreams() throws IOException {
        // Implement test for crossStreams method
    }

    // Add test cases for remaining methods in DataUtil class

}