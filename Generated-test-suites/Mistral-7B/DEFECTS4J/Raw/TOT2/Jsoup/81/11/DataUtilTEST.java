import org.junit.Test;
import org.jsoup.helper.DataUtil;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("sample.html");
        Document doc = DataUtil.load(file, "UTF-8", null);
        assertNotNull(doc);
        // add assertions to check the contents of the loaded document
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "html content";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(doc);
        // add assertions to check the contents of the loaded document
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "html content";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("ISO-8859-1"));
        try {
            Document doc = DataUtil.load(inputStream, null, null);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected exception
        }
    }

    // Add more test cases for other methods in DataUtil class
}