import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    private Parser parser;
    private String baseUri;

    @Before
    public void setUp() {
        parser = new Parser();
        baseUri = "http://example.com";
    }

    @After
    public void tearDown() {
        parser = null;
        baseUri = null;
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        Document document = parser.parse(html, baseUri);
        
        // Typical use case
        Element bodyElement = document.body();
        Assert.assertNotNull(bodyElement);
        Assert.assertEquals("body", bodyElement.tagName());
        Assert.assertEquals(1, bodyElement.children().size());
        
        // Edge case - empty html
        String emptyHtml = "";
        Document emptyDocument = parser.parse(emptyHtml, baseUri);
        Assert.assertNotNull(emptyDocument);
        Assert.assertEquals(0, emptyDocument.children().size());
        
        // Error scenario - invalid HTML
        String invalidHtml = "<html><body>";
        Document invalidDocument = parser.parse(invalidHtml, baseUri);
        Assert.assertNull(invalidDocument);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Body Fragment</h1><p>This is a body fragment</p>";
        Document document = parser.parseBodyFragment(bodyHtml, baseUri);
        
        // Typical use case
        Element bodyElement = document.body();
        Assert.assertNotNull(bodyElement);
        Assert.assertEquals("body", bodyElement.tagName());
        Assert.assertEquals(2, bodyElement.children().size());
        
        // Edge case - empty body fragment
        String emptyBodyHtml = "";
        Document emptyDocument = parser.parseBodyFragment(emptyBodyHtml, baseUri);
        Assert.assertNotNull(emptyDocument);
        Assert.assertEquals(0, emptyDocument.children().size());
        
        // Error scenario - invalid HTML in fragment
        String invalidBodyHtml = "<h1>Body Fragment<p>Missing closing tag for <p></h1>";
        Document invalidDocument = parser.parseBodyFragment(invalidBodyHtml, baseUri);
        Assert.assertNotNull(invalidDocument);
        Assert.assertEquals(1, invalidDocument.body().children().size());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Body Fragment</h1><p>This is a body fragment</p>";
        Document document = parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        // Typical use case
        Element bodyElement = document.body();
        Assert.assertNotNull(bodyElement);
        Assert.assertEquals("body", bodyElement.tagName());
        Assert.assertEquals(2, bodyElement.children().size());
        
        // Edge case - empty body fragment
        String emptyBodyHtml = "";
        Document emptyDocument = parser.parseBodyFragmentRelaxed(emptyBodyHtml, baseUri);
        Assert.assertNotNull(emptyDocument);
        Assert.assertEquals(0, emptyDocument.children().size());
        
        // Error scenario - invalid HTML in fragment
        String invalidBodyHtml = "<h1>Body Fragment<p>Missing closing tag for <p></h1>";
        Document invalidDocument = parser.parseBodyFragmentRelaxed(invalidBodyHtml, baseUri);
        Assert.assertNotNull(invalidDocument);
        Assert.assertEquals(2, invalidDocument.body().children().size());
    }

}