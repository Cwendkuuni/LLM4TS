import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "http://www.example.com");
        assertNotNull(document);
        // Add more assertions here if needed
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head><title>Test</title></head><body><p>Hello, World!</p></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", "http://www.example.com");
        assertNotNull(document);
        // Add more assertions here if needed
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String html = "<html><head><title>Test</title></head><body><p>Hello, World!</p></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", "http://www.example.com", Parser.xmlParser());
        assertNotNull(document);
        // Add more assertions here if needed
    }

    @Test
    public void testCrossStreams() throws IOException {
        // Implement this test case if needed
    }

    @Test
    public void testParseByteData() {
        // Implement this test case if needed
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        // Implement this test case if needed
    }

    // Add more test cases for other methods in the DataUtil class, covering edge cases and exceptions

}