package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.*;

public class XmlTreeBuilderTest {
    
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root></root>";
        String baseUri = "http://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        assertEquals(baseUri, xmlTreeBuilder.getBaseUri());
        assertNotNull(xmlTreeBuilder.getErrors());
        assertNotNull(xmlTreeBuilder.getStack());
        assertEquals(1, xmlTreeBuilder.getStack().size());
        assertEquals(xmlTreeBuilder.getDoc(), xmlTreeBuilder.getStack().get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.getDoc().outputSettings().syntax());
        assertEquals(input, xmlTreeBuilder.getTokeniser().getReader().getCurrentInput());
    }
    
    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("div", "test");
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals("test", element.baseUri());
        assertNotNull(element.attributes());
        assertEquals(1, xmlTreeBuilder.getStack().size());
        assertEquals(element, xmlTreeBuilder.getStack().get(0));
    }
    
    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("div", "test");
        Element element = xmlTreeBuilder.insert(startTag);
        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.process(endTag);
        assertEquals(0, xmlTreeBuilder.getStack().size());
    }
    
    @Test
    public void testProcessComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("test");
        xmlTreeBuilder.insert(commentToken);
        Node node = xmlTreeBuilder.getStack().get(0).childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof Comment);
        assertEquals("test", ((Comment) node).getData());
        assertEquals(Document.OutputSettings.Syntax.xml, node.outputSettings().syntax());
    }
    
    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("test");
        xmlTreeBuilder.insert(characterToken);
        Node node = xmlTreeBuilder.getStack().get(0).childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof TextNode);
        assertEquals("test", ((TextNode) node).getWholeText());
        assertEquals(Document.OutputSettings.Syntax.xml, node.outputSettings().syntax());
    }
    
    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("test", "", "", "");
        xmlTreeBuilder.insert(doctypeToken);
        Node node = xmlTreeBuilder.getStack().get(0).childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof DocumentType);
        assertEquals("test", ((DocumentType) node).getName());
        assertEquals(Document.OutputSettings.Syntax.xml, node.outputSettings().syntax());
    }
    
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag1 = new Token.StartTag("div", "test1");
        Element element1 = xmlTreeBuilder.insert(startTag1);
        Token.StartTag startTag2 = new Token.StartTag("div", "test2");
        Element element2 = xmlTreeBuilder.insert(startTag2);
        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.process(endTag);
        assertEquals(1, xmlTreeBuilder.getStack().size());
        assertEquals(element1, xmlTreeBuilder.getStack().get(0));
        xmlTreeBuilder.process(endTag);
        assertEquals(0, xmlTreeBuilder.getStack().size());
    }
    
    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<div>test</div>";
        String baseUri = "http://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("div", ((Element) nodes.get(0)).tagName());
        assertEquals("test", ((Element) nodes.get(0)).text());
    }
}