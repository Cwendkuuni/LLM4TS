import org.jsoup.HelperException;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        // Given a valid file
        File file = new File("path/to/valid/file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // When loading the file
        Document document = DataUtil.load(file, charsetName, baseUri);

        // Then the document should not be null
        assertNotNull(document);
    }

    // Edge cases and exception handling scenarios can be tested in other methods
}