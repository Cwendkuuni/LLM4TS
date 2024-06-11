import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        String filePath = "testFile.html";
        Document doc = DataUtil.load(new File(filePath), StandardCharsets.UTF_8.name(), "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String html = "<html><head></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testLoadFromInputStreamWithCustomParser() throws IOException {
        String html = "<html><head></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), "http://example.com", Parser.xmlParser());
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testCrossStreams() throws IOException {
        byte[] inputBytes = "Test input data".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        byte[] outputBytes = outputStream.toByteArray();
        assertNotNull(outputBytes);
        assertTrue(inputBytes.length == outputBytes.length);
    }

    // Add more test cases for remaining methods in DataUtil class

}