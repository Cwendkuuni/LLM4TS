import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() {
        File file = new File("test.html");
        try {
            Document document = DataUtil.load(file, "UTF-8", "http://example.com");
            assertNotNull(document);
            // Add more assertions based on the expected behavior of load() method
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadFromInputStream() {
        try {
            InputStream in = new FileInputStream("test.html");
            Document document = DataUtil.load(in, "UTF-8", "http://example.com");
            assertNotNull(document);
            // Add more assertions based on the expected behavior of load() method with InputStream
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadFromInputStreamWithParser() {
        try {
            InputStream in = new FileInputStream("test.html");
            Document document = DataUtil.load(in, "UTF-8", "http://example.com", Parser.htmlParser());
            assertNotNull(document);
            // Add more assertions based on the expected behavior of load() method with InputStream and Parser
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCrossStreams() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = new ByteArrayInputStream("test".getBytes());
        try {
            DataUtil.crossStreams(in, out);
            String result = out.toString();
            assertEquals("test", result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Add more test methods for other public methods in DataUtil class

}