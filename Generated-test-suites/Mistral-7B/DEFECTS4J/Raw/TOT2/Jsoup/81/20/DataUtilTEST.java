import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File inputFile = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputFile, charsetName, baseUri);
        Assert.assertNotNull(doc);
        // add more assertions based on expected document structure
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testHtml = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testHtml.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(doc);
        // add more assertions based on expected document structure
    }

    @Test
    public void testParseInputStream() throws IOException {
        String testHtml = "<html><body></body></html>";
        byte[] testBytes = testHtml.getBytes("UTF-8");
        InputStream inputStream = new ByteArrayInputStream(testBytes);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = DataUtil.Parser.htmlParser();
        Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        Assert.assertNotNull(doc);
        // add more assertions based on expected document structure
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File inputFile = new File("test.html");
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";
        Document doc = null;
        try {
            doc = DataUtil.load(inputFile, charsetName, baseUri);
        } catch (IOException e) {
            // test passes if the expected IOException is thrown
        }
        Assert.assertNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String testHtml = "<html><body></body></html>";
        byte[] testBytes = testHtml.getBytes("UTF-8");
        InputStream inputStream = new ByteArrayInputStream(testBytes);
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";
        Document doc = null;
        try {
            doc = DataUtil.load(inputStream, charsetName, baseUri);
        } catch (IOException e) {
            // test passes if the expected IOException is thrown
        }
        Assert.assertNull(doc);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String testHtml = "<html><body></body></html>";
        byte[] testBytes = testHtml.getBytes("UTF-8");
        InputStream inputStream = new ByteArrayInputStream(testBytes);
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";
        Parser parser = DataUtil.Parser.htmlParser();
        Document doc = null;
        try {
            doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        } catch (IOException e) {
            // test passes if the expected IOException is thrown
        }
        Assert.assertNull(doc);
    }
}