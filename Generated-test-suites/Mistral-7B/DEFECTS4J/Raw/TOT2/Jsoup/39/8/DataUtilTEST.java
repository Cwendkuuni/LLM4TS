import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.nodes.Document;
import org.jsoup.helper.DataUtil;
import java.io.File;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    void testLoadFile() throws IOException {
        File inputFile = new File("path/to/input/file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document expectedDocument = new Document("expectedContent"); // Replace this with the expected Document object

        Document actualDocument = DataUtil.load(inputFile, charsetName, baseUri);

        Assertions.assertNotNull(actualDocument);
        Assertions.assertEquals(expectedDocument.html(), actualDocument.html());
    }

    // Add edge cases and exception handling scenarios for other methods here.
}