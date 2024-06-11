import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() {
        // Typical use case
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        try {
            Document document = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadFromInputStream() {
        // Typical use case
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadFromInputStreamWithParser() {
        // Typical use case
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();

        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
            assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Additional test cases for edge cases and error scenarios can be added here
    
}