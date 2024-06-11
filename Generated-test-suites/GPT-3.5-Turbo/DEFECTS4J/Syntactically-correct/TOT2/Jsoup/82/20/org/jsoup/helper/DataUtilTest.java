import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class DataUtilTest {
    
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(document);
        // Add more assertions if needed
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(document);
        // Add more assertions if needed
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.xml");
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        assertNotNull(document);
        // Add more assertions if needed
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream("test.txt");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
        assertNotNull(byteBuffer);
        // Add more assertions if needed
    }

    // Add more test cases for other methods in DataUtil class

}