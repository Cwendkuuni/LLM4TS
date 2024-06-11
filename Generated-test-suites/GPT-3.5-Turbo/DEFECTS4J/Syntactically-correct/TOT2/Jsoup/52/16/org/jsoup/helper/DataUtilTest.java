import org.jsoup.helper.DataUtil;
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
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
        // Add more assertions related to the loaded document
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
        // Add more assertions related to the loaded document
    }

    @Test
    public void testLoadInputStreamParser() throws IOException {
        String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
        // Add more assertions related to the loaded document
    }

    @Test
    public void testCrossStreams() throws IOException {
        String input = "Input data to be written";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals(input, outputStream.toString());
    }

    // Add more test cases for other methods in DataUtil class

}