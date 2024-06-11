import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class DataUtilTest {
    
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(file, charsetName, baseUri);

        // Perform assertions on the document
        // Example:
        assertEquals("Title", document.title(), "Test Title");
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("<html><head><title>Test Title</title></head><body></body></html>".getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        // Perform assertions on the document
        // Example:
        assertEquals("Title", document.title(), "Test Title");
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("<html><head><title>Test Title</title></head><body></body></html>".getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        // Perform assertions on the document
        // Example:
        assertEquals("Title", document.title(), "Test Title");
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test Data".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        // Perform assertions on the output stream
        // Example:
        assertEquals("Test Data", outputStream.toString(StandardCharsets.UTF_8));
    }

    // Add more test cases for other methods in DataUtil class
    
}