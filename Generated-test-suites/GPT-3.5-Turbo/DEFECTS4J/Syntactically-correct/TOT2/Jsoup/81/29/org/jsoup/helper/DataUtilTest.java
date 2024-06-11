import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("testFile.html");
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(doc);
        // Add more assertions as needed
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("testFile.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(doc);
        // Add more assertions as needed
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new FileInputStream("input.txt");
        OutputStream out = new FileOutputStream("output.txt");
        DataUtil.crossStreams(in, out);
        // Add assertions for the output stream contents
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inStream = new FileInputStream("testFile.html");
        ByteBuffer buffer = DataUtil.readToByteBuffer(inStream, 1024);
        assertNotNull(buffer);
        // Add more assertions as needed
    }

    // Add more test methods for other public methods in DataUtil class

}