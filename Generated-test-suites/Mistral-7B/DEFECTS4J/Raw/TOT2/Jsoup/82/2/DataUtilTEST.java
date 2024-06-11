import org.junit.Test;
import org.jsoup.helper.DataUtil;
import static org.junit.Assert.*;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // replace with actual test file
        String charsetName = "UTF-8";
        String baseUri = "http://localhost";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
        // add assertions to check the loaded document
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://localhost";
        InputStream inputStream = getClass().getResourceAsStream("/test.html"); // replace with actual test file path
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
        // add assertions to check the loaded document
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://localhost";
        InputStream inputStream = getClass().getResourceAsStream("/test.html"); // replace with actual test file path
        Parser parser = DataUtil.Parser.htmlParser();
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(doc);
        // add assertions to check the loaded document
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream input = getClass().getResourceAsStream("/test.html"); // replace with actual test file path
        OutputStream output = new ByteArrayOutputStream();
        DataUtil.crossStreams(input, output);
        // add assertions to check the content of the output stream
    }

    @Test(expected = IOException.class)
    public void testLoadFileInvalidCharset() throws IOException {
        File file = new File("test.html"); // replace with actual test file
        String charsetName = "INVALID_CHARSET";
        String baseUri = "http://localhost";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        fail("Expected IOException"); // add assertions to check the exception message
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamInvalidCharset() throws IOException {
        String charsetName = "INVALID_CHARSET";
        String baseUri = "http://localhost";
        InputStream inputStream = getClass().getResourceAsStream("/test.html"); // replace with actual test file path
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        fail("Expected IOException"); // add assertions to check the exception message
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamInvalidParser() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://localhost";
        InputStream inputStream = getClass().getResourceAsStream("/test.html"); // replace with actual test file path
        Parser parser = new Parser(); // invalid parser
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
        fail("Expected IOException"); // add assertions to check the exception message
    }
}