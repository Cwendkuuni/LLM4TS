package org.jsoup.parser;

import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {
    private Document doc;
    private String baseUri;

    @Before
    public void setUp() {
        baseUri = "http://example.com";
        doc = new Document(baseUri);
    }

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        Document parsedDoc = Parser.parse(html, baseUri);
        
        assertNotNull(parsedDoc);
        assertEquals(doc, parsedDoc);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        Document bodyDoc = Parser.parseBodyFragment(bodyHtml, baseUri);
        Element body = bodyDoc.body();
        
        assertNotNull(bodyDoc);
        assertNotNull(body);
        assertEquals("Hello World!", body.text());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        Document parsedDoc = Parser.parse(html, baseUri);
        Comment comment = (Comment) parsedDoc.childNodes().get(0);
        
        assertNotNull(comment);
        assertEquals(" This is a comment ", comment.getData());
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[<h1>This is a CDATA section</h1>]]>";
        Document parsedDoc = Parser.parse(html, baseUri);
        TextNode cdata = (TextNode) parsedDoc.childNodes().get(0);
        
        assertNotNull(cdata);
        assertEquals("<h1>This is a CDATA section</h1>", cdata.getWholeText());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Document parsedDoc = Parser.parse(html, baseUri);
        XmlDeclaration xmlDecl = (XmlDeclaration) parsedDoc.childNodes().get(0);
        
        assertNotNull(xmlDecl);
        assertEquals("1.0", xmlDecl.getWholeData());
    }

    @Test
    public void testParseEndTag() {
        String html = "<div><p>Test</p></div>";
        Document parsedDoc = Parser.parse(html, baseUri);
        Element div = parsedDoc.getElementsByTag("div").first();
        
        assertNotNull(div);
        assertEquals(1, div.children().size());
        assertEquals("div", div.tagName());
        assertEquals("p", div.child(0).tagName());
        assertEquals("Test", div.child(0).text());
    }

    @Test
    public void testParseStartTag() {
        String html = "<div><p>Test</p></div>";
        Document parsedDoc = Parser.parse(html, baseUri);
        Element div = parsedDoc.getElementsByTag("div").first();
        
        assertNotNull(div);
        assertEquals(1, div.children().size());
        assertEquals("div", div.tagName());
        assertEquals("p", div.child(0).tagName());
        assertEquals("Test", div.child(0).text());
    }

    @Test
    public void testParseTextNode() {
        String html = "<div><p>Test</p></div>";
        Document parsedDoc = Parser.parse(html, baseUri);
        Element div = parsedDoc.getElementsByTag("div").first();
        TextNode text = (TextNode) div.childNode(0);
        
        assertNotNull(text);
        assertEquals("Test", text.text());
    }

    @Test
    public void testParseAttribute() {
        String html = "<div id=\"test\" class=\"container\"></div>";
        Document parsedDoc = Parser.parse(html, baseUri);
        Element div = parsedDoc.getElementsByTag("div").first();
        
        assertNotNull(div);
        assertEquals("test", div.id());
        assertEquals("container", div.className());
    }

    @Test
    public void testAddChildToParent() {
        Element parent = new Element(Tag.valueOf("div"), baseUri);
        Element child = new Element(Tag.valueOf("p"), baseUri);
        
        Element addedChild = Parser.addChildToParent(child, false);
        
        assertNotNull(addedChild);
        assertEquals(parent, addedChild.parent());
        assertEquals(1, parent.children().size());
        assertEquals(child, parent.child(0));
    }

    @Test
    public void testStackHasValidParent() {
        Element parent1 = new Element(Tag.valueOf("div"), baseUri);
        Element parent2 = new Element(Tag.valueOf("body"), baseUri);
        Element child = new Element(Tag.valueOf("p"), baseUri);
        
        Parser.addChildToParent(parent1, true);
        Parser.addChildToParent(parent2, true);
        
        assertTrue(Parser.stackHasValidParent(child.tag()));
    }

    @Test
    public void testPopStackToSuitableContainer() {
        Element parent1 = new Element(Tag.valueOf("div"), baseUri);
        Element parent2 = new Element(Tag.valueOf("body"), baseUri);
        Element child = new Element(Tag.valueOf("p"), baseUri);
        
        Parser.addChildToParent(parent1, true);
        Parser.addChildToParent(parent2, true);
        Element poppedParent = Parser.popStackToSuitableContainer(child.tag());
        
        assertNotNull(poppedParent);
        assertEquals(parent2, poppedParent);
        assertEquals(1, parent2.children().size());
        assertEquals(child, parent2.child(0));
    }

    @Test
    public void testPopStackToClose() {
        Element parent1 = new Element(Tag.valueOf("div"), baseUri);
        Element parent2 = new Element(Tag.valueOf("body"), baseUri);
        
        Parser.addChildToParent(parent1, true);
        Parser.addChildToParent(parent2, true);
        Element poppedParent = Parser.popStackToClose(parent1.tag());
        
        assertNotNull(poppedParent);
        assertEquals(parent1, poppedParent);
        assertTrue(parent2.children().isEmpty());
    }

    @Test
    public void testLast() {
        Element parent = new Element(Tag.valueOf("div"), baseUri);
        Element child = new Element(Tag.valueOf("p"), baseUri);
        
        Parser.addChildToParent(parent, true);
        Parser.addChildToParent(child, true);
        
        assertEquals(child, Parser.last());
    }
}