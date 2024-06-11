import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(doc);
        // Add more assertions as needed
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream input = new FileInputStream("test.html");
        Document doc = DataUtil.load(input, "UTF-8", "http://example.com");
        assertNotNull(doc);
        // Add more assertions as needed
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream input = new FileInputStream("test.html");
        Document doc = DataUtil.load(input, "UTF-8", "http://example.com", Parser.xmlParser());
        assertNotNull(doc);
        // Add more assertions as needed
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new FileInputStream("input.txt");
        OutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out);
        // Add assertions for output stream content
    }

    // Include more test cases for other methods in DataUtil class

}