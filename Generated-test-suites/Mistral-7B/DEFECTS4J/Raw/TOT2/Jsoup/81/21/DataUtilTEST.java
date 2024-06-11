import org.junit.Test;
import org.jsoup.helper.DataUtil;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File("test.html");
        Document doc = DataUtil.load(testFile, "UTF-8", null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testInput = "<html><body>Test</body></html>";
        InputStream testIS = new ByteArrayInputStream(testInput.getBytes("UTF-8"));
        Document doc = DataUtil.load(testIS, "UTF-8", null);
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String testInput = "<html><body>Test</body></html>";
        InputStream testIS = new ByteArrayInputStream(testInput.getBytes("UTF-8"));
        Document doc = DataUtil.parseInputStream(testIS, "UTF-8", null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testEmptyInputStream() throws IOException {
        InputStream testIS = new ByteArrayInputStream(new byte[0]);
        Document doc = DataUtil.parseInputStream(testIS, null, null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testInvalidCharsetName() throws IOException {
        InputStream testIS = new ByteArrayInputStream("Invalid charset name".getBytes());
        try {
            DataUtil.parseInputStream(testIS, "Invalid charset name", null, Parser.htmlParser());
        } catch (IllegalCharsetNameException e) {
            // Expected
        }
    }
}