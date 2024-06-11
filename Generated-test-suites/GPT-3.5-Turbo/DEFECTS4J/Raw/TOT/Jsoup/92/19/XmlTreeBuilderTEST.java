package org.jsoup.parser;

import org.junit.Test;

import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        // Test normal case
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Reader input = new StringReader("<xml></xml>");
        String baseUri = "http://www.example.com";
        Parser parser = new Parser();
        
        builder.initialiseParse(input, baseUri, parser);
        assertEquals(builder.stack.size(), 1);  // Document element should be added to the stack
        assertEquals(builder.currentElement().nodeName(), "xml");  // Document element should be "xml"

        // Test with null input
        builder = new XmlTreeBuilder();
        input = null;
        baseUri = "http://www.example.com";
        parser = new Parser();

        builder.initialiseParse(input, baseUri, parser);
        assertEquals(builder.stack.size(), 1);  // Document element should be added to the stack
        assertEquals(builder.currentElement().nodeName(), "#root");  // Document element should be "#root"
    }
    
    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = new Element("div");
        
        builder.insertNode(element);
        assertTrue(builder.currentElement().childNodeSize() > 0);
        assertEquals(builder.currentElement().childNode(0), element);
    }
    
    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("div");
        
        Element element = builder.insert(startTag);
        assertEquals(builder.stack.size(), 1);
        assertEquals(builder.currentElement().nodeName(), "div");
        assertEquals(builder.currentElement(), element);
    }
    
    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("This is a comment");
        
        builder.insert(commentToken);
        assertTrue(builder.currentElement().childNodeSize() > 0);
        Node insertedNode = builder.currentElement().childNode(0);
        assertTrue(insertedNode instanceof Comment);
        assertEquals(((Comment) insertedNode).getData(), "This is a comment");
    }
    
    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("This is some character data");
        
        builder.insert(characterToken);
        assertTrue(builder.currentElement().childNodeSize() > 0);
        Node insertedNode = builder.currentElement().childNode(0);
        assertTrue(insertedNode instanceof TextNode);
        assertEquals(((TextNode) insertedNode).getWholeText(), "This is some character data");
    }
    
    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("html");
        
        builder.insert(doctypeToken);
        assertTrue(builder.currentElement().childNodeSize() > 0);
        Node insertedNode = builder.currentElement().childNode(0);
        assertTrue(insertedNode instanceof DocumentType);
        assertEquals(((DocumentType) insertedNode).name(), "html");
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        // Add some elements to stack
        Element div1 = new Element("div");
        Element div2 = new Element("div");
        Element div3 = new Element("div");
        builder.stack.add(div1);
        builder.stack.add(div2);
        builder.stack.add(div3);
        
        // Pop stack until first occurrence of "div" tag
        Token.EndTag endTag = new Token.EndTag("div");
        builder.popStackToClose(endTag);
        
        assertTrue(builder.stack.size() > 0);
        assertEquals(builder.currentElement(), div2);
    }
    
    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<div>Fragment</div>";
        String baseUri = "http://www.example.com";
        Parser parser = new Parser();
        
        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, parser);
        assertTrue(nodes.size() > 0);
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals(((Element) nodes.get(0)).nodeName(), "div");
    }
}