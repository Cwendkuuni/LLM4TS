package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder treeBuilder;
    
    @Before
    public void setup() {
        treeBuilder = new XmlTreeBuilder();
    }
    
    @Test
    public void testDefaultSettings() {
        ParseSettings settings = treeBuilder.defaultSettings();
        assertTrue(settings.preserveCase);
    }
    
    @Test
    public void testParse() {
        String input = "<root><child>test</child></root>";
        String baseUri = "";
        Document doc = treeBuilder.parse(input, baseUri);

        assertEquals("root", doc.nodeName());
        assertEquals(1, doc.childNodeSize());
        
        Element rootElement = doc.child(0);
        assertEquals("root", rootElement.nodeName());
        assertEquals(1, rootElement.childNodeSize());
        
        Element childElement = rootElement.child(0);
        assertEquals("child", childElement.nodeName());
        assertEquals(1, childElement.childNodeSize());
        
        Node textNode = childElement.child(0);
        assertTrue(textNode instanceof TextNode);
        assertEquals("test", ((TextNode) textNode).getWholeText());
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<root><child>test</child></root>";
        String baseUri = "";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        treeBuilder.initialiseParse(input, baseUri, errors, settings);
        
        Document doc = treeBuilder.doc;
        Element rootElement = treeBuilder.stack.get(0);
        assertEquals("root", doc.nodeName());
        assertEquals("root", rootElement.nodeName());
    }
    
    @Test
    public void testProcess() {
        // Process StartTag
        Token.StartTag startTag = new Token.StartTag("div", "div", null, false);
        boolean result = treeBuilder.process(startTag);
        assertTrue(result);
        
        Element div = treeBuilder.stack.get(0);
        assertEquals("div", div.nodeName());
        
        // Process EndTag
        Token.EndTag endTag = new Token.EndTag("div", "div");
        result = treeBuilder.process(endTag);
        assertTrue(result);
        
        assertEquals(0, treeBuilder.stack.size());
        
        // Process Comment
        Token.Comment comment = new Token.Comment("test");
        result = treeBuilder.process(comment);
        assertTrue(result);
        
        Node node = treeBuilder.currentElement().childNode(0);
        assertTrue(node instanceof Comment);
        assertEquals("test", ((Comment) node).getData());
        
        // Process Character
        Token.Character character = new Token.Character("test");
        result = treeBuilder.process(character);
        assertTrue(result);
        
        node = treeBuilder.currentElement().childNode(1);
        assertTrue(node instanceof TextNode);
        assertEquals("test", ((TextNode) node).getWholeText());
        
        // Process Doctype
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");
        result = treeBuilder.process(doctype);
        assertTrue(result);
        
        node = treeBuilder.currentElement().childNode(2);
        assertTrue(node instanceof DocumentType);
        assertEquals("html", ((DocumentType) node).getName());
    }
    
    @Test
    public void testInsertNode() {
        Element element = new Element("div");
        treeBuilder.insertNode(element);
        
        Node node = treeBuilder.currentElement().childNode(0);
        assertEquals(element, node);
    }
    
    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("div", "div", null, false);
        Element div = treeBuilder.insert(startTag);
        
        assertEquals("div", div.nodeName());
        assertEquals(treeBuilder.baseUri, div.baseUri());
        assertEquals(startTag.attributes, div.attributes());
        assertEquals(1, treeBuilder.stack.size());
        assertEquals(div, treeBuilder.stack.get(0));
    }
    
    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("test");
        treeBuilder.insert(commentToken);
        
        Comment comment = (Comment) treeBuilder.currentElement().childNode(0);
        assertEquals("test", comment.getData());
        assertEquals(treeBuilder.baseUri, comment.baseUri());
        assertFalse(comment.bogus);
    }
    
    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character("test");
        treeBuilder.insert(characterToken);
        
        TextNode textNode = (TextNode) treeBuilder.currentElement().childNode(0);
        assertEquals("test", textNode.getWholeText());
        assertEquals(treeBuilder.baseUri, textNode.baseUri());
    }
    
    @Test
    public void testInsertDoctype() {
        Token.Doctype d = new Token.Doctype("html", "", "", "");
        treeBuilder.insert(d);
        
        DocumentType doctypeNode = (DocumentType) treeBuilder.currentElement().childNode(0);
        assertEquals("html", doctypeNode.getName());
        assertEquals("", doctypeNode.getPublicIdentifier());
        assertEquals("", doctypeNode.getSystemIdentifier());
        assertEquals(treeBuilder.baseUri, doctypeNode.baseUri());
    }
    
    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("div", "div");
        
        Element div = new Element("div");
        Element child = new Element("child");
        
        treeBuilder.stack.add(div);
        treeBuilder.stack.add(child);
        treeBuilder.stack.add(div);
        
        treeBuilder.popStackToClose(endTag);
        
        assertEquals(1, treeBuilder.stack.size());
        assertEquals(child, treeBuilder.stack.get(0));
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<root><child>test</child></root>";
        String baseUri = "";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        List<Node> nodes = treeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        
        assertEquals(1, nodes.size());
        
        Element rootElement = (Element) nodes.get(0);
        assertEquals("root", rootElement.nodeName());
        assertEquals(1, rootElement.childNodeSize());
        
        Element childElement = rootElement.child(0);
        assertEquals("child", childElement.nodeName());
        assertEquals(1, childElement.childNodeSize());
        
        Node textNode = childElement.child(0);
        assertTrue(textNode instanceof TextNode);
        assertEquals("test", ((TextNode) textNode).getWholeText());
    }
    
}