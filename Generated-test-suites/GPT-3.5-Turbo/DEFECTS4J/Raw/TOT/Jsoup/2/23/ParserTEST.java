import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {
    private static final String BASE_URI = "http://example.com";
    private static final String HTML = "<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>";
    private static final String BODY_HTML = "<h1>Hello</h1><p>World</p>";
    
    private Parser parser;
    
    @Before
    public void setUp() {
        parser = new Parser();
    }
    
    @Test
    public void testParse() {
        Document document = parser.parse(HTML, BASE_URI);
        Assert.assertEquals("Test Page", document.title());
        Assert.assertEquals("Hello World!", document.body().select("h1").text());
    }
    
    @Test
    public void testParseBodyFragment() {
        Document document = parser.parseBodyFragment(BODY_HTML, BASE_URI);
        Assert.assertEquals("Hello", document.body().select("h1").text());
        Assert.assertEquals("World", document.body().select("p").text());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullHtml() {
        parser.parse(null, BASE_URI);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullBaseUri() {
        parser.parse(HTML, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentWithNullHtml() {
        parser.parseBodyFragment(null, BASE_URI);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentWithNullBaseUri() {
        parser.parseBodyFragment(BODY_HTML, null);
    }
}