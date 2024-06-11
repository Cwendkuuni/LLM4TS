import org.junit.Test;
import org.jsoup.Jsoup;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class JsoupTest {

    @Test
    public void testParseStringWithBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://example.com";
        Document document = Jsoup.parse(html, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseStringWithoutBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document document = Jsoup.parse(html);
        assertNotNull(document);
    }

    @Test
    public void testParseFileWithBaseUriAndCharset() throws IOException {
        String filePath = "test.html";
        String baseUri = "https://example.com";
        String charsetName = "UTF-8";
        Document document = Jsoup.parse(new File(filePath), charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseFileWithBaseUri() throws IOException {
        String filePath = "test.html";
        String baseUri = "https://example.com";
        Document document = Jsoup.parse(new File(filePath), baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithBaseUriAndCharset() throws IOException {
        String fileName = "test.html";
        String baseUri = "https://example.com";
        String charsetName = "UTF-8";
        InputStream inputStream = getClass().getResourceAsStream(fileName);
        Document document = Jsoup.parse(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithBaseUri() throws IOException {
        String fileName = "test.html";
        String baseUri = "https://example.com";
        InputStream inputStream = getClass().getResourceAsStream(fileName);
        Document document = Jsoup.parse(inputStream, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        String fileName = "test.html";
        Parser parser = Parser.xmlParser();
        InputStream inputStream = getClass().getResourceAsStream(fileName);
        Document document = Jsoup.parse(inputStream, parser);
        assertNotNull(document);
    }

    @Test
    public void testParseStringWithParser() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = Parser.xmlParser();
        Document document = Jsoup.parse(html, parser);
        assertNotNull(document);
    }

    @Test
    public void testConnect() {
        String url = "https://example.com";
        Connection connection = Jsoup.connect(url);
        assertNotNull(connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringWithNullBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document document = Jsoup.parse(html, null);
        assertThrows(IllegalArgumentException.class, () -> document);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputStreamWithNullBaseUri() throws IOException {
        String fileName = "test.html";
        InputStream inputStream = getClass().getResourceAsStream(fileName);
        Document document = Jsoup.parse(inputStream, null);
        assertThrows(IllegalArgumentException.class, () -> document);
    }
}