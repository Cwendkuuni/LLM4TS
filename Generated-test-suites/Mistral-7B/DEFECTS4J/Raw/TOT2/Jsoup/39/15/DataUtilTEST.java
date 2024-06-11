import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // replace with actual test file
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        InputStream inputStream = getClass().getResourceAsStream("/test.html"); // replace with actual test file
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithException() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testParseByteDataDefaultCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("test data".getBytes());
        String charsetName = null;
        String baseUri = "http://test.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteDataSpecifiedCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("test data".getBytes("UTF-16LE"));
        String charsetName = "UTF-16LE";
        String baseUri = "http://test.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteDataCharsetNotFound() {
        ByteBuffer byteData = ByteBuffer.wrap("test data".getBytes());
        String charsetName = "GBK";
        String baseUri = "http://test.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        Assert.assertNull(document);
    }

    @Test
    public void testParseByteDataEmptyData() {
        ByteBuffer byteData = ByteBuffer.allocate(0);
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        Assert.assertNull(document);
    }
}