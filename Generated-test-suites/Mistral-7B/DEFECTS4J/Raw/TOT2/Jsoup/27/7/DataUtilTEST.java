import org.junit.Assert;
import org.jsoup.nodes.Document;
import org.jsoup.helper.DataUtil;
import java.io.File;
import java.io.IOException;

public class DataUtilTest {
    @org.junit.Test
    public void testLoadFile() throws IOException {
        File file = new File("path/to/file.html"); // Replace with the actual path to an existing file
        String charsetName = "UTF-8";
        String baseUri = "file:///path/to/base/uri"; // Replace with the actual base URI
        Document document = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    // Add edge cases and exception handling scenarios here.
}