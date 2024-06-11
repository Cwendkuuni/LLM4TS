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
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(document);
        // Add more assertions here
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(document);
        // Add more assertions here
    }

    @Test
    public void testCrossStreams() throws IOException {
        // Add test for crossStreams method
    }

    // Add more test cases for other methods

}