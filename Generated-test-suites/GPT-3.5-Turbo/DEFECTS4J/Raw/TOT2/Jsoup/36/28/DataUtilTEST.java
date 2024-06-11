import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "https://example.com");
        // Add assertions for the loaded document
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "https://example.com");
        // Add assertions for the loaded document
    }

    @Test
    public void testLoadInputStreamParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "https://example.com", Parser.xmlParser());
        // Add assertions for the loaded document
    }

    // Add more test cases for other public methods in DataUtil class if needed

    // Edge cases and exception handling scenarios can also be tested here

}