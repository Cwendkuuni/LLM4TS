import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws IOException {
        String filePath = "path/to/your/test.html"; // replace with the actual path to a valid HTML file
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(new File(filePath), charsetName, baseUri);
        assertEquals("Expected HTML content", "<html>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>", document.html());
    }

    // Add edge cases and exception handling tests here
}