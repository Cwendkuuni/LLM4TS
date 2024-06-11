import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {
    // Test case for load(File, String, String) method
    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File("test.html"); // Replace with the path to your test file
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";

        Document document = DataUtil.load(testFile, charsetName, baseUri);

        // Check if the document is not null
        Assert.assertNotNull(document);
    }

    // Test case for edge case - loading a file with an unknown charset
    @Test(expected = IOException.class)
    public void testLoadFileUnknownCharset() throws IOException {
        File testFile = new File("test.html"); // Replace with the path to your test file
        String charsetName = "unknown-charset";
        String baseUri = "http://example.com/";

        DataUtil.load(testFile, charsetName, baseUri);
    }

    // Test case for load(InputStream, String, String) method
    @Test
    public void testLoadInputStream() throws IOException {
        byte[] testInput = new byte[1024]; // Replace with the byte array to test
        InputStream testInputStream = new ByteArrayInputStream(testInput);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";

        Document document = DataUtil.load(testInputStream, charsetName, baseUri);

        // Check if the document is not null
        Assert.assertNotNull(document);
    }

    // Test case for load(InputStream, String, String, Parser) method
    @Test
    public void testLoadInputStreamParser() throws IOException {
        byte[] testInput = new byte[1024]; // Replace with the byte array to test
        InputStream testInputStream = new ByteArrayInputStream(testInput);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.load(testInputStream, charsetName, baseUri, parser);

        // Check if the document is not null
        Assert.assertNotNull(document);
    }
}