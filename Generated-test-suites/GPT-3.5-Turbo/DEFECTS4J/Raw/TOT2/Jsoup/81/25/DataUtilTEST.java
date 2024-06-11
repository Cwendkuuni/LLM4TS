import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(document);
        // Add more assertions as needed
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(document);
        // Add more assertions as needed
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        assertNotNull(document);
        // Add more assertions as needed
    }

    @Test
    public void testCrossStreams() throws IOException {
        FileInputStream input = new FileInputStream("input.txt");
        // Create an output stream for testing purposes
        // Mock OutputStream
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        DataUtil.crossStreams(input, output);
        // Add assertions to check the output stream content
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        FileInputStream input = new FileInputStream("input.txt");
        ByteBuffer buffer = DataUtil.readToByteBuffer(input, 1024);
        assertNotNull(buffer);
        // Add more assertions as needed
    }

    // Add more test methods for other public methods in DataUtil class

}