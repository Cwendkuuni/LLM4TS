import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        String baseUri = "http://example.com";
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", baseUri);
        assertNotNull(document);
        assertEquals("Document title", document.title(), "Test Document");
        // Add more assertions here for other properties of the document
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String baseUri = "http://example.com";
        InputStream inputStream = new FileInputStream("test.html");
        Document document = DataUtil.load(inputStream, "UTF-8", baseUri);
        assertNotNull(document);
        assertEquals("Document title", document.title(), "Test Document");
        // Add more assertions here for other properties of the document
    }

    // Add more test methods for other public methods in DataUtil class

}