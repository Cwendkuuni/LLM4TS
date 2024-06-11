import org.jsoup.nodes.Document;

import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Document</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertEquals("Test Document", document.title());
        Assert.assertEquals("Hello World!", document.body().select("h1").text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h2>Fragment Test</h2><p>This is a fragment of HTML.</p>";
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertEquals("Fragment Test", document.body().select("h2").text());
        Assert.assertEquals("This is a fragment of HTML.", document.body().select("p").text());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h2>Relaxed Fragment Test</h2><p>This is a relaxed fragment of HTML.</p>";
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        Assert.assertEquals("Relaxed Fragment Test", document.body().select("h2").text());
        Assert.assertEquals("This is a relaxed fragment of HTML.", document.body().select("p").text());
    }
    
    @Test
    public void testParseComment() {
        String commentHtml = "<!-- This is a comment -->";
        String baseUri = "http://example.com";
        Document document = Parser.parse(commentHtml, baseUri);
        
        Assert.assertEquals("This is a comment", document.body().childNode(0).outerHtml());
    }
    
    @Test
    public void testParseXmlDecl() {
        String xmlDeclHtml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(xmlDeclHtml, baseUri);
        
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", document.childNode(0).outerHtml());
    }
    
    @Test
    public void testParseEndTag() {
        String endTagHtml = "<h1>Test</h1>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(endTagHtml, baseUri);
        
        Assert.assertEquals("Test", document.body().select("h1").text());
    }
    
    @Test
    public void testParseStartTag() {
        String startTagHtml = "<div class=\"test\">Test Content</div>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(startTagHtml, baseUri);
        
        Assert.assertEquals("Test Content", document.body().select("div.test").text());
    }
    
    @Test
    public void testParseTextNode() {
        String textNodeHtml = "This is a text node.";
        String baseUri = "http://example.com";
        Document document = Parser.parse(textNodeHtml, baseUri);
        
        Assert.assertEquals("This is a text node.", document.body().text());
    }
    
    @Test
    public void testParseCdata() {
        String cdataHtml = "<![CDATA[This is CDATA content]]>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(cdataHtml, baseUri);
        
        Assert.assertEquals("<![CDATA[This is CDATA content]]>", document.body().childNode(0).outerHtml());
    }
    
    @Test
    public void testParseAttribute() {
        String attributeHtml = "<div id=\"test\" class=\"test-class\">Test Content</div>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(attributeHtml, baseUri);
        
        Assert.assertEquals("test", document.body().select("div").attr("id"));
        Assert.assertEquals("test-class", document.body().select("div").attr("class"));
    }
    
}