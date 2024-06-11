import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.helper.DataUtil;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws Exception {
        File testFile = new File("sample.html"); // replace with the actual file path
        Document document = DataUtil.load(testFile, "UTF-8", null);
        Assert.assertNotNull(document);
    }
}