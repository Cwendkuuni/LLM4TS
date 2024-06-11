import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File file = new File("sample.html");
        Document doc = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFromInputStreamWithDefaultParser() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = this.getClass().getResourceAsStream("/sample.html");
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFromInputStreamWithHtmlParser() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = this.getClass().getResourceAsStream("/sample.html");
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, DataUtil.Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream input = this.getClass().getResourceAsStream("/sample.html");
        OutputStream output = new FileOutputStream("output.html");
        DataUtil.crossStreams(input, output);
        input.close();
        output.close();
        // Add assertions here if necessary
    }
}