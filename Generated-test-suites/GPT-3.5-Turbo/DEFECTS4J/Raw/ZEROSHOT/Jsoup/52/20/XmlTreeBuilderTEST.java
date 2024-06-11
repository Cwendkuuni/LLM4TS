package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.Validate;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html>Hello World</html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        // Assert statements here
        assertTrue(xmlTreeBuilder.stack.contains(xmlTreeBuilder.doc));
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcess_StartTag() {
        Token token = new Token.StartTag("tag");
        
        assertTrue(xmlTreeBuilder.process(token));
    }
    
    @Test
    public void testProcess_EndTag() {
        Token token = new Token.EndTag("tag");
        
        assertTrue(xmlTreeBuilder.process(token));
    }
    
    @Test
    public void testProcess_Comment() {
        Token token = new Token.Comment("This is a comment");
        
        assertTrue(xmlTreeBuilder.process(token));
    }
    
    @Test
    public void testProcess_Character() {
        Token token = new Token.Character("Some text");
        
        assertTrue(xmlTreeBuilder.process(token));
    }
    
    @Test
    public void testProcess_Doctype() {
        Token token = new Token.Doctype("html", "publicId", "systemId");
        
        assertTrue(xmlTreeBuilder.process(token));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testProcess_InvalidToken() {
        Token token = new Token();
        
        xmlTreeBuilder.process(token);
        
        // Exception should be thrown
    }
    
    @Test
    public void testInsertNode() {
        Node node = new Element("div");
        
        xmlTreeBuilder.insertNode(node);
        
        // Assert statements here
        assertTrue(xmlTreeBuilder.currentElement().childNodes().contains(node));
    }
    
    @Test
    public void testInsert_StartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        
        Element el = xmlTreeBuilder.insert(startTag);
        
        // Assert statements here
        assertEquals(Tag.valueOf("div"), el.tag());
        assertTrue(xmlTreeBuilder.tokeniser.isSelfClosingFlagAcknowledged());
        assertFalse(el.tag().isKnownTag());
    }
    
    @Test
    public void testInsert_Comment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        
        xmlTreeBuilder.insert(commentToken);
        
        // Assert statements here
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof Comment);
    }
    
    @Test
    public void testInsert_Character() {
        Token.Character characterToken = new Token.Character("Some text");
        
        xmlTreeBuilder.insert(characterToken);
        
        // Assert statements here
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof TextNode);
    }
    
    @Test
    public void testInsert_Doctype() {
        Token.Doctype doctype = new Token.Doctype("html", "publicId", "systemId");
        
        xmlTreeBuilder.insert(doctype);
        
        // Assert statements here
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof DocumentType);
    }
    
    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("div");
        
        Element element = new Element("div");
        xmlTreeBuilder.stack.add(element);
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        // Assert statements here
        assertFalse(xmlTreeBuilder.stack.contains(element));
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<div></div>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);
        
        // Assert statements here
        assertNotNull(nodes);
        assertFalse(nodes.isEmpty());
    }
}