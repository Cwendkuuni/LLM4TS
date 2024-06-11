import org.junit.Test;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("path/to/your/file.html"); // Replace with the valid path to an HTML file
        Document document = DataUtil.load(file, "UTF-8", null);
        assertNotNull(document);
        assertTrue(document.body() != null);
    }
}