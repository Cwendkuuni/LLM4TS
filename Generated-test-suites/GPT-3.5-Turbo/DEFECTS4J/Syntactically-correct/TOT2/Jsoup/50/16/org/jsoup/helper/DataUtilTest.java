import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
        // Add more assertions here
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
        // Add more assertions here
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(doc);
        // Add more assertions here
    }

    @Test
    public void testCrossStreams() throws IOException {
        // Create test scenario for crossStreams method
        // Add more assertions here
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        // Create test scenario for readToByteBuffer method
        // Add more assertions here
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        // Create test scenario for readFileToByteBuffer method
        // Add more assertions here
    }

    // Add more test methods for other public methods in DataUtil class

}