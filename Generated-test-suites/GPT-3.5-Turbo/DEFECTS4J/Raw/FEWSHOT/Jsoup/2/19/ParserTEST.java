
package org.jsoup.parser;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;

import java.util.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("Test", doc.title());
        
        Element body = doc.body();
        assertNotNull(body);
        
        Elements h1s = body.getElementsByTag("h1");
        assertEquals(1, h1s.size());
        
        Element h1 = h1s.get(0);
        assertEquals("Hello, World!", h1.text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        
        assertEquals("http://example.com", doc.baseUri());
        
        Element body = doc.body();
        assertNull(body);
        
        Elements h1s = doc.getElementsByTag("h1");
        assertEquals(1, h1s.size());
        
        Element h1 = h1s.get(0);
        assertEquals("Hello, World!", h1.text());
    }
    
    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        Document doc = Parser.parse(html, "http://example.com");
        
        assertEquals("http://example.com", doc.baseUri());
        
        Comment comment = (Comment) doc.childNode(0);
        assertEquals(" This is a comment ", comment.getData());
    }
    
    @Test
    public void testParseCdata() {
        String html = "<![CDATA[This is CDATA]]>";
        Document doc = Parser.parse(html, "http://example.com");
        
        assertEquals("http://example.com", doc.baseUri());
        
        TextNode textNode = (TextNode) doc.childNode(0);
        assertEquals("This is CDATA", textNode.getWholeText());
    }
    
    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Document doc = Parser.parse(html, "http://example.com");
        
        assertEquals("http://example.com", doc.baseUri());
        
        XmlDeclaration decl = (XmlDeclaration) doc.childNode(0);
        assertEquals("1.0", decl.getVersion());
        assertEquals("UTF-8", decl.getEncoding());
    }
    
    @Test
    public void testParseEndTag() {
        String html = "<p>This is a <b>bold</b> paragraph.</p>";
        Document doc = Parser.parse(html, "http://example.com");
        
        assertEquals("http://example.com", doc.baseUri());
        
        Element p = doc.getElementsByTag("p").get(0);
        assertEquals("This is a bold paragraph.", p.text());
        
        Element b = doc.getElementsByTag("b").get(0);
        assertEquals("bold", b.text());
        assertSame(p, b.parent());
    }
    
    @Test
    public void testParseStartTag() {
        String html = "<p>This is a <b>bold</b> paragraph.</p>";
        Document doc = Parser.parse(html, "http://example.com");
        
        assertEquals("http://example.com", doc.baseUri());
        
        Element p = doc.getElementsByTag("p").get(0);
        assertEquals("This is a bold paragraph.", p.text());
        
        Element b = doc.getElementsByTag("b").get(0);
        assertEquals("bold", b.text());
        assertSame(p, b.parent());
    }
    
    @Test
    public void testParseTextNode() {
        String html = "<p>This is a <b>bold</b> paragraph.</p>";
        Document doc = Parser.parse(html, "http://example.com");
        
        assertEquals("http://example.com", doc.baseUri());
        
        Element p = doc.getElementsByTag("p").get(0);
        assertEquals("This is a bold paragraph.", p.text());
        
        Element b = doc.getElementsByTag("b").get(0);
        assertEquals("bold", b.text());
        assertSame(p, b.parent());
    }
    
    @Test
    public void testParseAttribute() {
        String html = "<a href=\"http://example.com\" target=\"_blank\">Link</a>";
        Document doc = Parser.parse(html, "http://example.com");
        
        assertEquals("http://example.com", doc.baseUri());
        
        Element a = doc.getElementsByTag("a").get(0);
        assertEquals("http://example.com", a.absUrl("href"));
        assertEquals("_blank", a.attr("target"));
    }

}
