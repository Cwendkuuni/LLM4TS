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
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        DataUtil.load(file, "UTF-8", "http://example.com");
        //TODO: Add more assertions
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("<html></html>".getBytes());
        DataUtil.load(inputStream, "UTF-8", "http://example.com");
        //TODO: Add more assertions
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("<html></html>".getBytes());
        DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        //TODO: Add more assertions
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello, World!".getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        //TODO: Add more assertions
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello, World!".getBytes());
        ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream, 100);
        //TODO: Add more assertions
    }

    // Additional test methods for other public methods in DataUtil class

}