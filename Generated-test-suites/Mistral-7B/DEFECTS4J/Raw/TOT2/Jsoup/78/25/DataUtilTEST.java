import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // Assuming there is a test.html file in the project directory
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testContent = "Some test content";
        InputStream inputStream = new ByteArrayInputStream(testContent.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals(testContent, document.body().html());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String testContent = "Some test content";
        InputStream inputStream = new ByteArrayInputStream(testContent.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals(testContent, document.body().html());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String testContent = "Some test content";
        InputStream inputStream = new ByteArrayInputStream(testContent.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        Assert.assertArrayEquals(testContent.getBytes(), outputStream.toByteArray());
    }
}