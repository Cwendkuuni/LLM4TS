import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {
    // Test for load method
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // replace with the path to a valid HTML file
        String charsetName = "UTF-8";
        String baseUri = "file:/path/to/base"; // replace with the base URI
        Document document = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    // Test for loadInputStream method with null input stream
    @Test(expected = NullPointerException.class)
    public void testLoadInputStreamNull() {
        InputStream inputStream = null;
        String charsetName = "UTF-8";
        String baseUri = "file:/path/to/base";
        DataUtil.load(inputStream, charsetName, baseUri);
    }

    // Test for loadInputStream method with valid input stream
    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html"); // replace with the path to a valid HTML file
        String charsetName = "UTF-8";
        String baseUri = "file:/path/to/base";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(document);
        inputStream.close();
    }

    // Test for parseInputStream method with null input stream
    @Test(expected = NullPointerException.class)
    public void testParseInputStreamNull() {
        InputStream inputStream = null;
        String charsetName = "UTF-8";
        String baseUri = "file:/path/to/base";
        DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
    }

    // Test for parseInputStream method with valid input stream
    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html"); // replace with the path to a valid HTML file
        String charsetName = "UTF-8";
        String baseUri = "file:/path/to/base";
        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(document);
        inputStream.close();
    }
}