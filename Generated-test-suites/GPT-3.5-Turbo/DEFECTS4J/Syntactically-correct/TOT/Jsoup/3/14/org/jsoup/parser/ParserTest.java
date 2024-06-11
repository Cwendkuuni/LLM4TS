import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {
    
    private Parser parser;
    private String html;
    private String baseUri;
    
    @Before
    public void setUp() {
        html = "<html><head><title>Test Page</title></head><body><h1>Welcome</h1></body></html>";
        baseUri = "http://www.example.com";
        parser = new Parser(html, baseUri, false);
    }
    
    @Test
    public void testParse() {
        Document doc = parser.parse();
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://www.example.com", doc.baseUri());
        Assert.assertEquals("html", doc.tagName());
        
        Assert.assertNotNull(doc.head());
        Assert.assertEquals("head", doc.head().tagName());
        
        Assert.assertNotNull(doc.body());
        Assert.assertEquals("body", doc.body().tagName());
        
        Assert.assertNotNull(doc.title());
        Assert.assertEquals("Test Page", doc.title());
    }
    
    @Test
    public void testParseComment() {
        String commentHtml = "<!-- This is a comment -->";
        
        parser = new Parser(commentHtml, baseUri, false);
        parser.parse();
        
        // Assert that the comment is added to the document
        Assert.assertNotNull(parser.last().childNode(0));
        Assert.assertEquals("<!-- This is a comment -->", parser.last().childNode(0).outerHtml());
    }
    
    @Test
    public void testParseXmlDecl() {
        String xmlDeclHtml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        
        parser = new Parser(xmlDeclHtml, baseUri, false);
        parser.parse();
        
        // Assert that the XML declaration is added to the document
        Assert.assertNotNull(parser.last().childNode(0));
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", parser.last().childNode(0).outerHtml());
    }
    
    @Test
    public void testParseEndTag() {
        String endTagHtml = "</h1>";
        
        parser = new Parser(endTagHtml, baseUri, false);
        parser.parse();
        
        // Assert that the corresponding start tag is closed in the document
        Assert.assertNull(parser.last());
    }
    
    @Test
    public void testParseStartTag() {
        String startTagHtml = "<h2>This is a heading</h2>";
        
        parser = new Parser(startTagHtml, baseUri, false);
        parser.parse();
        
        // Assert that the start tag and its content are added to the document
        Assert.assertNotNull(parser.last().childNode(0));
        Assert.assertEquals("<h2>This is a heading</h2>", parser.last().childNode(0).outerHtml());
    }
    
    @Test
    public void testParseTextNode() {
        String textNodeHtml = "This is a text node";
        
        parser = new Parser(textNodeHtml, baseUri, false);
        parser.parse();
        
        // Assert that the text node is added to the document
        Assert.assertNotNull(parser.last().childNode(0));
        Assert.assertEquals("This is a text node", parser.last().childNode(0).outerHtml());
    }
    
    @Test
    public void testParseCdata() {
        String cdataHtml = "<![CDATA[ This is CDATA ]]>";
        
        parser = new Parser(cdataHtml, baseUri, false);
        parser.parse();
        
        // Assert that the CDATA section is added to the document
        Assert.assertNotNull(parser.last().childNode(0));
        Assert.assertEquals("<![CDATA[ This is CDATA ]]>", parser.last().childNode(0).outerHtml());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Welcome</h1>";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://www.example.com", doc.baseUri());
        Assert.assertEquals("body", doc.tagName());
        
        Assert.assertNotNull(doc.child(0));
        Assert.assertEquals("h1", doc.child(0).tagName());
        Assert.assertEquals("Welcome", doc.child(0).text());
    }
    
    @Test
    public void testParseBodyFragmentWithEmptyHtml() {
        String bodyHtml = "";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://www.example.com", doc.baseUri());
        Assert.assertEquals("body", doc.tagName());
        Assert.assertEquals("", doc.text());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullHtml() {
        String html = null;
        
        Parser.parse(html, baseUri);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentWithNullHtml() {
        String bodyHtml = null;
        
        Parser.parseBodyFragment(bodyHtml, baseUri);
    }
}