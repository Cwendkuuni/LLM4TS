import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        File file = new File("test.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document doc = DataUtil.load(file, charset, baseUri);
            assertNotNull(doc);
        } catch (IOException e) {
            assertNull(e);
        }
    }

    @Test
    public void testLoadInputStream() {
        InputStream in = new FileInputStream("test.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document doc = DataUtil.load(in, charset, baseUri);
            assertNotNull(doc);
        } catch (IOException e) {
            assertNull(e);
        }
    }

    @Test
    public void testLoadInputStreamParser() {
        InputStream in = new FileInputStream("test.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        try {
            Document doc = DataUtil.load(in, charset, baseUri, parser);
            assertNotNull(doc);
        } catch (IOException e) {
            assertNull(e);
        }
    }
}