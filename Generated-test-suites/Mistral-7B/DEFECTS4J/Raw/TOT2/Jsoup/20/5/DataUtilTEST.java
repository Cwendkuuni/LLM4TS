import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // Assuming test.html exists in the classpath
        Document document = DataUtil.load(file, "UTF-8", "");
        assertNotNull(document);
    }
}