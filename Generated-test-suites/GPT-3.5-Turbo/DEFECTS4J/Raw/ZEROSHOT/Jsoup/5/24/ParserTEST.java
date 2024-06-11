package org.jsoup.parser;

import org.jsoup.nodes.*;
import org.junit.*;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("html", doc.tagName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertNotNull(doc.title());
        assertEquals("Test", doc.title().text());
        assertNotNull(doc.select("h1"));
        assertEquals("Hello", doc.select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Hello</p></div>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertNotNull(doc.body());
        assertNotNull(doc.select("div"));
        assertNotNull(doc.select("p"));
        assertEquals("Hello", doc.select("p").text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><p>Hello</p></div>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertNotNull(doc.body());
        assertNotNull(doc.select("div"));
        assertNotNull(doc.select("p"));
        assertEquals("Hello", doc.select("p").text());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals(1, doc.childNodes().size());
        assertEquals(Comment.class, doc.childNode(0).getClass());
        assertEquals(" This is a comment ", ((Comment) doc.childNode(0)).getData());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals(1, doc.childNodes().size());
        assertEquals(XmlDeclaration.class, doc.childNode(0).getClass());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ((XmlDeclaration) doc.childNode(0)).getData());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals(1, doc.select("title").size());
        assertEquals("title", doc.select("title").first().tagName());
        doc.select("title").first().remove();
        assertEquals(0, doc.select("title").size());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals(1, doc.select("h1").size());
        assertEquals("h1", doc.select("h1").first().tagName());
        assertEquals("Hello", doc.select("h1").first().text());
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><h1>Hello</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals(1, doc.select("h1").size());
        assertEquals("h1", doc.select("h1").first().tagName());
        assertEquals("Hello", doc.select("h1").first().text());
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[This is cdata]]>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals(1, doc.childNodes().size());
        assertEquals(TextNode.class, doc.childNode(0).getClass());
        assertEquals("This is cdata", ((TextNode) doc.childNode(0)).getWholeText());
    }

    @Test
    public void testParseAttribute() {
        String html = "<div id=\"test\" class=\"test-class\">Hello</div>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        Element div = doc.select("div").first();
        assertNotNull(div);
        assertEquals("test", div.attr("id"));
        assertEquals("test-class", div.attr("class"));
    }

    @Test
    public void testPopStackToSuitableContainer() {
        Parser parser = new Parser("", "https://www.example.com", false);
        Element child = new Element(Tag.valueOf("div"), "https://www.example.com");
        Element parent = new Element(Tag.valueOf("body"), "https://www.example.com");
        parent.appendChild(child);
        parser.stack.addLast(parent);
        
        Element result = parser.popStackToSuitableContainer(Tag.valueOf("div"));
        
        assertEquals(child, result);
    }

    @Test
    public void testPopStackToClose() {
        Parser parser = new Parser("", "https://www.example.com", false);
        Element child1 = new Element(Tag.valueOf("div"), "https://www.example.com");
        Element child2 = new Element(Tag.valueOf("p"), "https://www.example.com");
        Element parent = new Element(Tag.valueOf("body"), "https://www.example.com");
        parent.appendChild(child1);
        parent.appendChild(child2);
        parser.stack.addLast(parent);
        
        Element result = parser.popStackToClose(Tag.valueOf("div"));
        
        assertEquals(child1, result);
        assertEquals(1, parser.stack.size());
        assertEquals(child2, parser.stack.getFirst());
    }

    @Test
    public void testLast() {
        Parser parser = new Parser("", "https://www.example.com", false);
        Element element1 = new Element(Tag.valueOf("div"), "https://www.example.com");
        Element element2 = new Element(Tag.valueOf("p"), "https://www.example.com");
        parser.stack.addLast(element1);
        parser.stack.addLast(element2);
        
        Element result = parser.last();
        
        assertEquals(element2, result);
    }
}