import org.jsoup.helper.DataUtil;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File inputFile = new File("test.html");
        Document doc = DataUtil.load(inputFile, charsetName, baseUri);
        Assert.assertNotNull(doc);
        //add assertions to check the loaded document
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(doc);
        //add assertions to check the loaded document
    }

    @Test
    public void testLoadInputStreamWithUnsupportedCharset() throws IOException {
        String charsetName = "gb2312";
        String baseUri = "http://example.com";
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            Assert.fail(); //should not reach here
        } catch (IOException e) {
            //expected, so no assertion needed
        }
    }

    @Test
    public void testParseInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(doc);
        //add assertions to check the loaded document
    }

    @Test
    public void testParseInputStreamWithNullInputStream() {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.parseInputStream(null, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNull(doc);
    }
}