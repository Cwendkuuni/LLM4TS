import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
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
        // Add more assertions as needed
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
        // Add more assertions as needed
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
        // Add more assertions as needed
    }

    @Test
    public void testCrossStreams() throws IOException {
        // Implement edge case testing for crossStreams method
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        // Implement edge case testing for readToByteBuffer method
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        // Implement edge case testing for readFileToByteBuffer method
    }

    // Add more test cases for other methods in DataUtil class

}