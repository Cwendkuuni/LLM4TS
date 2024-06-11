package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import static org.junit.Assert.*;
import org.junit.Test;

public class XmlTreeBuilderTest {
    
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse("input", "baseUri", new ParseErrorList());
        
        Document doc = xmlTreeBuilder.getDocument();
        assertNotNull(doc);
        assertEquals("baseUri", doc.baseUri());
    }
    
    @Test
    public void testProcess() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token();
        token.type = Token.TokenType.Character;
        
        assertTrue(xmlTreeBuilder.process(token));
    }
    
    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element("testElement");
        
        xmlTreeBuilder.insertNode(element);
        Elements elements = xmlTreeBuilder.currentElement().children();
        
        assertEquals(1, elements.size());
        assertEquals("testElement", elements.get(0).tagName());
    }
    
    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("testTag", "attr=value");
        
        Element insertedElement = xmlTreeBuilder.insert(startTag);
        Elements elements = xmlTreeBuilder.currentElement().children();
        
        assertEquals(1, elements.size());
        assertEquals("testTag", insertedElement.tagName());
    }
    
    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("test comment");
        
        xmlTreeBuilder.insert(commentToken);
        Elements elements = xmlTreeBuilder.currentElement().children();
        
        assertEquals(1, elements.size());
        assertEquals("#comment", elements.get(0).nodeName());
    }
    
    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("test character");
        
        xmlTreeBuilder.insert(characterToken);
        Elements elements = xmlTreeBuilder.currentElement().children();
        
        assertEquals(1, elements.size());
        assertEquals("#text", elements.get(0).nodeName());
    }
    
    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "-//W3C//DTD HTML 4.01//EN", "");
        
        xmlTreeBuilder.insert(doctype);
        Elements elements = xmlTreeBuilder.currentElement().children();
        
        assertEquals(1, elements.size());
        assertEquals("!DOCTYPE", elements.get(0).nodeName());
    }
    
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("testTag");
        xmlTreeBuilder.stack.add(new Element("testTag"));
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }
}