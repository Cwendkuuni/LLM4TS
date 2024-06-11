import org.jsoup.helper.DataUtil;
import org.jsoup.helper.DataUtilTest;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {
    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        String expectedCharset = "UTF-8";
        String baseUri = "http://example.com";
        File inputFile = new File("test.html");
        Document document = DataUtil.load(inputFile, expectedCharset, baseUri);
        assertNotNull(document);
        assertEquals(expectedCharset, document.outputSettings().charset());
    }

    @Test
    public void testLoadFileWithNullCharset() throws IOException {
        String baseUri = "http://example.com";
        File inputFile = new File("test.html");
        try {
            DataUtil.load(inputFile, null, baseUri);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String expectedCharset = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = DataUtilTest.class.getResourceAsStream("/test.html");
        Document document = DataUtil.load(inputStream, expectedCharset, baseUri);
        assertNotNull(document);
        assertEquals(expectedCharset, document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String baseUri = "http://example.com";
        InputStream inputStream = DataUtilTest.class.getResourceAsStream("/test.html");
        try {
            DataUtil.load(inputStream, null, baseUri);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String expectedCharset = "invalidCharset";
        String baseUri = "http://example.com";
        InputStream inputStream = DataUtilTest.class.getResourceAsStream("/test.html");
        try {
            DataUtil.load(inputStream, expectedCharset, baseUri);
            fail("Expected IOException");
        } catch (IOException e) {
            assertThat(e.getMessage(), containsString("Charset 'invalidCharset' not supported"));
        }
    }
}