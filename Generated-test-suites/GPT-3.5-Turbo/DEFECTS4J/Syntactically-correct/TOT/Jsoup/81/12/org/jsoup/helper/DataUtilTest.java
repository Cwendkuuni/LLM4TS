import org.junit.Test;
import org.jsoup.nodes.Document;
import java.io.IOException;
import static org.junit.Assert.*;

public class DataUtilTest {

    private final String file = "path/to/test/file.html";
    private final String charsetName = "UTF-8";
    private final String baseUri = "http://example.com";

    @Test
    public void testLoad_File_ValidInput() throws IOException {
        File input = new File(file);
        Document document = DataUtil.load(input, charsetName, baseUri);

        assertNotNull(document);
        // Assert other conditions based on the expected output
    }

    @Test
    public void testLoad_File_InvalidInput() throws IOException {
        File input = new File("invalid/path/to/file.html");
        try {
            DataUtil.load(input, charsetName, baseUri);
        } catch (IOException e) {
            assertEquals("Expected exception message", e.getMessage());
        }
        // Assert other conditions based on the expected output
    }

    @Test
    public void testLoad_InputStream_ValidInput() throws IOException {
        InputStream input = new FileInputStream(file);
        Document document = DataUtil.load(input, charsetName, baseUri);

        assertNotNull(document);
        // Assert other conditions based on the expected output
    }

    @Test
    public void testLoad_InputStream_InvalidInput() throws IOException {
        InputStream input = new FileInputStream("invalid/path/to/file.html");
        try {
            DataUtil.load(input, charsetName, baseUri);
        } catch (IOException e) {
            assertEquals("Expected exception message", e.getMessage());
        }
        // Assert other conditions based on the expected output
    }

    // Other test cases for each method in DataUtil class

}