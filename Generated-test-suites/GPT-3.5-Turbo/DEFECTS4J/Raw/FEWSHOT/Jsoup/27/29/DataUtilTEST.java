import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.*;

public class DataUtilTest {
  
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "https://www.example.com");
        // Add assertions to verify the loaded document
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", "https://www.example.com");
        // Add assertions to verify the loaded document
    }
  
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.load(inputStream, "UTF-8", "https://www.example.com", parser);
        // Add assertions to verify the loaded document
    }
  
    // Add more test methods for other methods in DataUtil class
  
}