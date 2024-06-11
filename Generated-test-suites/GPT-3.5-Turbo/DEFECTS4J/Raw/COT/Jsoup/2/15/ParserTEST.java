import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        Document parsedDoc = Parser.parse(html, baseUri);
        
        assertNotNull(parsedDoc);
        assertEquals("http://example.com", parsedDoc.baseUri());
        assertEquals(1, parsedDoc.childNodes().size());
        
        Element htmlElement = parsedDoc.child(0);
        assertEquals("html", htmlElement.tagName());
        assertEquals(2, htmlElement.childNodes().size());
        
        Element headElement = htmlElement.child(0);
        assertEquals("head", headElement.tagName());
        assertEquals(0, headElement.childNodes().size());
        
        Element bodyElement = htmlElement.child(1);
        assertEquals("body", bodyElement.tagName());
        assertEquals(0, bodyElement.childNodes().size());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div>Some text</div>";
        String baseUri = "http://example.com";
        Document parsedDoc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(parsedDoc);
        assertEquals("http://example.com", parsedDoc.baseUri());
        assertEquals(1, parsedDoc.childNodes().size());
        
        Element divElement = parsedDoc.child(0);
        assertEquals("div", divElement.tagName());
        assertEquals(1, divElement.childNodes().size());
        
        TextNode textNode = (TextNode) divElement.childNode(0);
        assertEquals("Some text", textNode.text());
    }
    
    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "http://example.com";
        Document parsedDoc = Parser.parse(html, baseUri);
        
        assertNotNull(parsedDoc);
        assertEquals("http://example.com", parsedDoc.baseUri());
        assertEquals(1, parsedDoc.childNodes().size());
        
        Comment comment = (Comment) parsedDoc.childNode(0);
        assertEquals(" This is a comment ", comment.getData());
    }
    
    @Test
    public void testParseCdata() {
        String html = "<![CDATA[ This is CDATA ]]>";
        String baseUri = "http://example.com";
        Document parsedDoc = Parser.parse(html, baseUri);
        
        assertNotNull(parsedDoc);
        assertEquals("http://example.com", parsedDoc.baseUri());
        assertEquals(1, parsedDoc.childNodes().size());
        
        TextNode textNode = (TextNode) parsedDoc.childNode(0);
        assertEquals(" This is CDATA ", textNode.text());
    }
    
    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String baseUri = "http://example.com";
        Document parsedDoc = Parser.parse(html, baseUri);
        
        assertNotNull(parsedDoc);
        assertEquals("http://example.com", parsedDoc.baseUri());
        assertEquals(1, parsedDoc.childNodes().size());
        
        XmlDeclaration xmlDecl = (XmlDeclaration) parsedDoc.childNode(0);
        assertEquals("xml", xmlDecl.nodeName());
        assertEquals("1.0", xmlDecl.attr("version"));
        assertEquals("UTF-8", xmlDecl.attr("encoding"));
    }
    
    @Test
    public void testParseStartTag() {
        String html = "<div id=\"example\">Some text</div>";
        String baseUri = "http://example.com";
        Document parsedDoc = Parser.parse(html, baseUri);
        
        assertNotNull(parsedDoc);
        assertEquals("http://example.com", parsedDoc.baseUri());
        assertEquals(1, parsedDoc.childNodes().size());
        
        Element divElement = parsedDoc.child(0);
        assertEquals("div", divElement.tagName());
        assertEquals("example", divElement.attr("id"));
        assertEquals(1, divElement.childNodes().size());
        
        TextNode textNode = (TextNode) divElement.childNode(0);
        assertEquals("Some text", textNode.text());
    }
    
    @Test
    public void testParseEndTag() {
        String html = "<div>Some text</div>";
        String baseUri = "http://example.com";
        Document parsedDoc = Parser.parse(html, baseUri);
        
        assertNotNull(parsedDoc);
        assertEquals("http://example.com", parsedDoc.baseUri());
        assertEquals(1, parsedDoc.childNodes().size());
        
        Element divElement = parsedDoc.child(0);
        assertEquals("div", divElement.tagName());
        assertEquals(1, divElement.childNodes().size());
        
        TextNode textNode = (TextNode) divElement.childNode(0);
        assertEquals("Some text", textNode.text());
    }
    
    @Test
    public void testParseAttribute() {
        String html = "<div id=\"example\" class=\"example-class\"></div>";
        String baseUri = "http://example.com";
        Document parsedDoc = Parser.parse(html, baseUri);
        
        assertNotNull(parsedDoc);
        assertEquals("http://example.com", parsedDoc.baseUri());
        assertEquals(1, parsedDoc.childNodes().size());
        
        Element divElement = parsedDoc.child(0);
        assertEquals("div", divElement.tagName());
        assertEquals("example", divElement.attr("id"));
        assertEquals("example-class", divElement.attr("class"));
    }
    
    @Test
    public void testParseTextNode() {
        String html = "Some text";
        String baseUri = "http://example.com";
        Document parsedDoc = Parser.parse(html, baseUri);
        
        assertNotNull(parsedDoc);
        assertEquals("http://example.com", parsedDoc.baseUri());
        assertEquals(1, parsedDoc.childNodes().size());
        
        TextNode textNode = (TextNode) parsedDoc.childNode(0);
        assertEquals("Some text", textNode.text());
    }
    
    @Test
    public void testParseBaseHref() {
        String html = "<base href=\"http://example.com\">";
        String baseUri = "http://example.com/page";
        Document parsedDoc = Parser.parse(html, baseUri);
        
        assertNotNull(parsedDoc);
        assertEquals("http://example.com", parsedDoc.baseUri());
        assertEquals(1, parsedDoc.childNodes().size());
        
        Element baseElement = parsedDoc.child(0);
        assertEquals("base", baseElement.tagName());
        assertEquals("http://example.com", baseElement.absUrl("href"));
        
        assertEquals("http://example.com/page", parsedDoc.select("a").first().absUrl("href"));
    }

}