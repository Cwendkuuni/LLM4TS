import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() {
        // Typical use case
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");

        assertNotNull(doc);
        // Add more specific assertions here
    }

    @Test
    public void testLoadFromInputStream() {
        // Edge case: Empty input stream
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");

        assertNotNull(doc);
        // Add more specific assertions here
    }

    @Test
    public void testLoadFromInputStreamWithParser() {
        // Error scenario: Invalid parser
        InputStream inputStream = new ByteArrayInputStream("Invalid input".getBytes());
        Parser parser = Parser.htmlParser(); // Change to invalid parser
        assertThrows(IOException.class, () -> DataUtil.load(inputStream, "UTF-8", "http://example.com", parser));
    }

    @Test
    public void testReadToByteBuffer() {
        // Typical use case
        InputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream);

        assertNotNull(buffer);
        // Add more specific assertions here
    }

    @Test
    public void testReadToByteBufferWithMaxSize() {
        // Error scenario: Max size exceeded
        InputStream inputStream = new FileInputStream("largeFile.dat");
        assertThrows(IOException.class, () -> DataUtil.readToByteBuffer(inputStream, 1000));
    }

    // Add more test cases for other methods in DataUtil class

}