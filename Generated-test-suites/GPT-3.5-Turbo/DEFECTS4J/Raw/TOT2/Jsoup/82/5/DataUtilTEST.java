import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        Document doc = DataUtil.load(new File("test.html"), "UTF-8", "http://example.com");
        assertNotNull(doc);
        // Add more assertions for edge cases and error scenarios here
    }

    @Test
    public void testLoadInputStream() {
        Document doc = DataUtil.load(new FileInputStream("test.html"), "UTF-8", "http://example.com");
        assertNotNull(doc);
        // Add more assertions for edge cases and error scenarios here
    }

    @Test
    public void testLoadInputStreamWithParser() {
        Document doc = DataUtil.load(new FileInputStream("test.html"), "UTF-8", "http://example.com", Parser.xmlParser());
        assertNotNull(doc);
        // Add more assertions for edge cases and error scenarios here
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(new FileInputStream("input.txt"), outputStream);
        // Add assertions to check if the streams are correctly written
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream("data.txt");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
        assertNotNull(byteBuffer);
        // Add more assertions for edge cases and error scenarios here
    }

    // Add more test cases for other methods in DataUtil class

}