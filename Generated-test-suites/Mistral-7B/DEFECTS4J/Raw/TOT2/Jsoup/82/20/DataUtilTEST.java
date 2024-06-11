import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("sample.html"); // assume this file exists
        Document document = DataUtil.load(file, null);
        assertNotNull(document);
        // add assertions based on the structure and content of the loaded document
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("sample.html"); // assume this file exists
        Document document = DataUtil.load(file, "UTF-16");
        assertNotNull(document);
        // add assertions based on the structure and content of the loaded document
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("sample.html"); // assume this file exists
        Document document = null;
        try {
            document = DataUtil.load(file, "InvalidCharset");
        } catch (IOException e) {
            // expected
            assertEquals("Expected IOException", e.getMessage());
        }
        assertNull(document);
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/sample.html"); // assume this resource exists
        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertNotNull(document);
        // add assertions based on the structure and content of the loaded document
    }

    @Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/sample.html"); // assume this resource exists
        Document document = DataUtil.parseInputStream(inputStream, "UTF-16", null, Parser.htmlParser());
        assertNotNull(document);
        // add assertions based on the structure and content of the loaded document
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/sample.html"); // assume this resource exists
        Document document = null;
        try {
            document = DataUtil.parseInputStream(inputStream, "InvalidCharset", null, Parser.htmlParser());
        } catch (IOException e) {
            // expected
            assertEquals("Expected IOException", e.getMessage());
        }
        assertNull(document);
    }
}