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
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
        // Add assertions here
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        // Add assertions here
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        // Add assertions here
    }

    @Test
    public void testCrossStreams() throws IOException {
        // Setup input and output streams
        // Call crossStreams method
        // Add assertions here
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        DataUtil.readToByteBuffer(inputStream, 100);
        // Add assertions here
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        DataUtil.readFileToByteBuffer(file);
        // Add assertions here
    }

    @Test
    public void testEmptyByteBuffer() {
        DataUtil.emptyByteBuffer();
        // Add assertions here
    }

}