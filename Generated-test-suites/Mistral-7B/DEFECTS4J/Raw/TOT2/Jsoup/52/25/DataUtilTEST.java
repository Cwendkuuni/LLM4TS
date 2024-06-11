import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        File inputFile = new File("test.html"); // replace with a valid file path
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(inputFile, charsetName, baseUri);

        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String xmlData = "<html><head></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlData.getBytes());

        Document doc = DataUtil.load(inputStream, null);

        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String xmlData = "<html><head></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlData.getBytes("UTF-16LE"));

        Document doc = DataUtil.load(inputStream, "UTF-16LE");

        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseByteData(byteData, null, baseUri, parser);

        Assert.assertNull(doc);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String xmlData = "<html><head></head><body></body></html>";
        byte[] xmlBytes = xmlData.getBytes("UTF-16LE");
        ByteBuffer byteData = ByteBuffer.wrap(xmlBytes);
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseByteData(byteData, "UTF-16LE", baseUri, parser);

        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidFile() {
        File inputFile = new File("non_existent_file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        DataUtil.load(inputFile, charsetName, baseUri);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithIOException() throws IOException {
        String xmlData = "<html><head></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlData.getBytes());
        inputStream.close(); // close the input stream before testing

        Document doc = DataUtil.load(inputStream, null);
    }
}