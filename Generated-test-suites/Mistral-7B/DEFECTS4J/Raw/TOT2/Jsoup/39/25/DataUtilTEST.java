import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String expectedCharset = "UTF-8";
        String baseUri = "http://example.com";
        File inputFile = new File("test.html");
        Document doc = DataUtil.load(inputFile, expectedCharset, baseUri);
        // Check the loaded document using assertions
        Assert.assertNotNull(doc);
        // More checks based on the functionality of the method
    }

}