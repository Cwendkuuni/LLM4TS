
package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.util.List;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XmlTreeBuilderTest {
    
    private final XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
    
    @Test
    public void testDefaultSettings() {
        ParseSettings settings = treeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }
    
    @Test
    public void testParse() {
        String input = "<root><element1>value1</element1><element2>value2</element2></root>";
        String baseUri = "http://example.com";
        Document document = treeBuilder.parse(input, baseUri);
        
        assertEquals("http://example.com", document.baseUri());
        List<Node> childNodes = document.childNodes();
        assertEquals(1, childNodes.size());
        
        Element rootElement = (Element)childNodes.get(0);
        assertEquals("root", rootElement.tagName());
        assertEquals(2, rootElement.children.size());
        
        Element element1 = (Element)rootElement.children.get(0);
        assertEquals("element1", element1.tagName());
        assertEquals("value1", element1.text());
        
        Element element2 = (Element)rootElement.children.get(1);
        assertEquals("element2", element2.tagName());
        assertEquals("value2", element2.text());
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<root><sub>value</sub></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        
        treeBuilder.initialiseParse(input, baseUri, errors, settings);
        
        Document document = treeBuilder.doc;
        assertEquals("http://example.com", document.baseUri());
        
        List<Node> stack = treeBuilder.stack;
        assertEquals(1, stack.size());
        
        Element rootElement = (Element)stack.get(0);
        assertEquals("root", rootElement.tagName());
        
        Document.OutputSettings.Syntax syntax = document.outputSettings().syntax();
        assertEquals(Document.OutputSettings.Syntax.xml, syntax);
    }
    
    @Test
    public void testProcess() {
        Token startTagToken = new Token.StartTag("element");
        Token endTagToken = new Token.EndTag("element");
        Token commentToken = new Token.Comment("comment");
        Token characterToken = new Token.Character("text");
        Token doctypeToken = new Token.Doctype("html");
        Token eofToken = new Token.EOF();
        
        boolean result;
        
        result = treeBuilder.process(startTagToken);
        assertEquals(true, result);
        
        result = treeBuilder.process(endTagToken);
        assertEquals(true, result);
        
        result = treeBuilder.process(commentToken);
        assertEquals(true, result);
        
        result = treeBuilder.process(characterToken);
        assertEquals(true, result);
        
        result = treeBuilder.process(doctypeToken);
        assertEquals(true, result);
        
        result = treeBuilder.process(eofToken);
        assertEquals(true, result);
    }
    
    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("element"), "http://example.com");
        treeBuilder.stack.add(new Element(Tag.valueOf("root"), "http://example.com"));
        
        treeBuilder.insertNode(element);
        
        Element rootElement = treeBuilder.currentElement();
        assertEquals(1, rootElement.children.size());
        assertEquals(element, rootElement.children.get(0));
    }
    
    @Test
    public void testInsertStartTag() {
        Token.StartTag startTagToken = new Token.StartTag("element");
        startTagToken.selfClosing = false;
        startTagToken.attributes.put("attr1", "value1");
        startTagToken.attributes.put("attr2", "value2");
        
        Element element = treeBuilder.insert(startTagToken);
        
        assertEquals("element", element.tagName());
        assertEquals("http://example.com", element.baseUri());
        assertEquals(2, element.attributes.size());
        assertEquals("value1", element.attributes.get("attr1"));
        assertEquals("value2", element.attributes.get("attr2"));
        
        List<Element> stack = treeBuilder.stack;
        assertEquals(1, stack.size());
        assertEquals(element, stack.get(0));
    }
    
    @Test
    public void testInsertStartTagSelfClosing() {
        Token.StartTag startTagToken = new Token.StartTag("element");
        startTagToken.selfClosing = true;
        
        Element element = treeBuilder.insert(startTagToken);
        
        assertEquals("element", element.tagName());
        assertEquals("http://example.com", element.baseUri());
        
        List<Element> stack = treeBuilder.stack;
        assertEquals(0, stack.size());
    }
    
    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("comment");
        
        treeBuilder.insert(commentToken);
        
        Document document = treeBuilder.doc;
        assertEquals(1, document.childNodes().size());
        
        Node node = document.childNodes().get(0);
        assertEquals(NodeType.COMMENT, node.nodeType());
        assertEquals("comment", node.toString());
    }
    
    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character("text");
        
        treeBuilder.insert(characterToken);
        
        Document document = treeBuilder.doc;
        assertEquals(1, document.childNodes().size());
        
        Node node = document.childNodes().get(0);
        assertEquals(NodeType.TEXT, node.nodeType());
        assertEquals("text", node.toString());
    }
    
    @Test
    public void testInsertDoctype() {
        Token.Doctype doctypeToken = new Token.Doctype("html");
        doctypeToken.publicIdentifier = "public";
        doctypeToken.systemIdentifier = "system";
        
        treeBuilder.insert(doctypeToken);
        
        Document document = treeBuilder.doc;
        assertEquals(1, document.childNodes().size());
        
        Node node = document.childNodes().get(0);
        assertEquals(NodeType.DOCTYPE, node.nodeType());
        DocumentType doctype = (DocumentType)node;
        assertEquals("html", doctype.name());
        assertEquals("public", doctype.publicId());
        assertEquals("system", doctype.systemId());
    }
    
    @Test
    public void testPopStackToClose() {
        Element rootElement = new Element(Tag.valueOf("root"), "http://example.com");
        Element element1 = new Element(Tag.valueOf("element1"), "http://example.com");
        Element element2 = new Element(Tag.valueOf("element2"), "http://example.com");
        
        treeBuilder.stack.add(rootElement);
        treeBuilder.stack.add(element1);
        treeBuilder.stack.add(element2);
        
        Token.EndTag endTag = new Token.EndTag("element1");
        treeBuilder.popStackToClose(endTag);
        
        List<Element> stack = treeBuilder.stack;
        assertEquals(2, stack.size());
        assertEquals(element2, stack.get(0));
        assertEquals(rootElement, stack.get(1));
    }
    
    @Test
    public void testParseFragment() {
        String input = "<sub>value</sub>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        
        List<Node> nodes = treeBuilder.parseFragment(input, baseUri, errors, settings);
        
        assertEquals(1, nodes.size());
        
        Element subElement = (Element)nodes.get(0);
        assertEquals("sub", subElement.tagName());
        assertEquals("value", subElement.text());
    }
}
