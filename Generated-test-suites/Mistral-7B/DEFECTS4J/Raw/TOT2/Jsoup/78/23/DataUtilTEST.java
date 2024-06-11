import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFileDocument() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File inputFile = new File("testFile.html");

        Document document = DataUtil.load(inputFile, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileDocumentWithInvalidCharset() throws IOException {
        String charsetName = "invalidCharset";
        String baseUri = "http://example.com";
        File inputFile = new File("testFile.html");

        try {
            Document document = DataUtil.load(inputFile, charsetName, baseUri);
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadInputStreamDocument() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File inputFile = new File("testFile.html");
        InputStream inputStream = new FileInputStream(inputFile);

        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamDocumentWithNullInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        try {
            Document document = DataUtil.load(null, charsetName, baseUri);
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testParseInputStreamDocument() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = new FileInputStream("testFile.html");

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamDocumentWithNullInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        try {
            Document document = DataUtil.parseInputStream(null, charsetName, baseUri, Parser.htmlParser());
        } catch (IOException e) {
            // expected
        }
    }
}