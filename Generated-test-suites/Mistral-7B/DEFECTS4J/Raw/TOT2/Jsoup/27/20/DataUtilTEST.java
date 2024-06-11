import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("path/to/your/testfile.html");
        Document document = DataUtil.load(file, "UTF-8", null);
        assertNotNull(document);
    }
}