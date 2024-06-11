import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(doc);
        // Add more assertions if needed
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(doc);
        // Add more assertions if needed
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        assertNotNull(doc);
        // Add more assertions if needed
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("input.txt");
        OutputStream outputStream = System.out;
        DataUtil.crossStreams(inputStream, outputStream);
        // Add more assertions if needed
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
        assertNotNull(byteBuffer);
        // Add more assertions if needed
    }

    // Add more test cases for other methods in DataUtil class

}