import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {
    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8");
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String html = "<html><head><meta charset='UTF-8'></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String html = "<html><head><meta charset='UTF-8'></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-8");
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        String html = "<html><head><meta charset='UTF-8'></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException {
        String html = "<html><head><meta charset='UTF-8'></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    // Add more test cases here...
}