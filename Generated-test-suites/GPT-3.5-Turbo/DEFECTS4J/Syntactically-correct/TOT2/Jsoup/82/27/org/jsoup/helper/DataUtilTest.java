import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "https://example.com");
        assertNotNull(document);
        // Add more assertions as needed
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "https://example.com");
        assertNotNull(document);
        // Add more assertions as needed
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.xml");
        Document document = DataUtil.load(inputStream, "UTF-8", "https://example.com", Parser.xmlParser());
        assertNotNull(document);
        // Add more assertions as needed
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.txt");
        OutputStream outputStream = System.out; // System out
        DataUtil.crossStreams(inputStream, outputStream);
        // Add more assertions as needed
    }

    @Test
    public void testInvalidFileLoad() {
        File file = new File("nonexistent.html");
        try {
            DataUtil.load(file, "UTF-8", "https://example.com");
        } catch (IOException e) {
            assertEquals("java.io.FileNotFoundException", e.getClass().getName());
        }
    }

    // Add more test cases as needed for other methods in DataUtil

}