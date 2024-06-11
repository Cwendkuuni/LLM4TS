import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File file = new File("test.html"); // Replace with the path to a valid HTML file
        Document doc = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = getClass().getResourceAsStream("/test.html"); // Replace with the path to a valid HTML file
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = getClass().getResourceAsStream("/test.html"); // Replace with the path to a valid HTML file
        Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    // Add more test cases for other methods
}