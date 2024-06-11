import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFileWithValidInput() {
        File file = new File("test.html"); // replace with a valid file path
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    // Add test cases for other scenarios such as valid input with different charsets, valid input with null charsetName, valid input with null baseUri, etc.
}