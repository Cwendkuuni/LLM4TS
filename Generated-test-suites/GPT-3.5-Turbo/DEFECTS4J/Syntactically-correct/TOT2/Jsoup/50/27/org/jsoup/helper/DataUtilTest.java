import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        try {
            File file = new File("test.html");
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            Document doc = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    // Other test methods for each public method in DataUtil class

}