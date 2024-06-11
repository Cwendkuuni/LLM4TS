import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.JsoupParser;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class JsoupTest {

    @Test
    public void testParseHTMLWithBaseUri() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseHTMLWithBaseUriAndParser() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new JsoupParser();
        Document document = Jsoup.parse(html, baseUri, parser);
        assertNotNull(document);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullHtml() {
        String baseUri = "http://example.com";
        Document document = Jsoup.parse((String) null, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullBaseUri() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document document = Jsoup.parse(html, (String) null);
    }

    @Test
    public void testParseWithEmptyHtml() {
        String baseUri = "http://example.com";
        Document document = Jsoup.parse("", baseUri);
        assertNotNull(document);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullParser() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri, (Parser) null);
    }

    @Test(expected = IOException.class)
    public void testParseWithIOException() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri, new FailingParser());
    }

    @Test
    public void testParseWithMalformedHtml() {
        String html = "<html<body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        assertNotNull(document);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithMalformedBaseUri() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "example.com";
        Document document = Jsoup.parse(html, baseUri);
    }

    private static class FailingParser extends Parser {
        @Override
        public Document parseInput(String input, String baseUri) throws IOException {
            throw new IOException("Failed to parse input");
        }
    }
}