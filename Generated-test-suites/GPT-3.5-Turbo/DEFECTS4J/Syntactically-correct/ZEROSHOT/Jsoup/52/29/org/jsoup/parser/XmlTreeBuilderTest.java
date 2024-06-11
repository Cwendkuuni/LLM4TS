package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder xmlTreeBuilder;
    
    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<xml></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        assertEquals(baseUri, xmlTreeBuilder.baseUri);
        assertEquals(0, errors.size());
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testProcess() {
        Token startTagToken = new Token.StartTag("tag", new Attributes());
        Token endTagToken = new Token.EndTag("tag");
        Token commentToken = new Token.Comment("comment");
        Token characterToken = new Token.Character("character");
        Token doctypeToken = new Token.Doctype("doctype", "publicIdentifier", "systemIdentifier");
        Token eofToken = new Token.EOF();
        
        assertTrue(xmlTreeBuilder.process(startTagToken));
        xmlTreeBuilder.stack.add(new Element(Tag.newInstance("tag"), "http://example.com", new Attributes()));
        assertTrue(xmlTreeBuilder.process(endTagToken));
        assertTrue(xmlTreeBuilder.process(commentToken));
        assertTrue(xmlTreeBuilder.process(characterToken));
        assertTrue(xmlTreeBuilder.process(doctypeToken));
        assertTrue(xmlTreeBuilder.process(eofToken));
        
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
    }
    
    @Test
    public void testInsertNode() {
        Document doc = new Document("");
        xmlTreeBuilder.stack.add(new Element(Tag.newInstance("tag"), "http://example.com", new Attributes()));
        xmlTreeBuilder.doc = doc;
        
        Node node = new Element(Tag.newInstance("tag"), "http://example.com", new Attributes());
        xmlTreeBuilder.insertNode(node);
        
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
    }
    
    @Test
    public void testInsertStartTag() {
        String name = "tag";
        Attributes attributes = new Attributes();
        attributes.put("attr", "value");
        
        Element el = xmlTreeBuilder.insert(new Token.StartTag(name, attributes));
        
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertEquals(name, el.tagName());
        assertEquals(attributes, el.attributes());
    }
    
    @Test
    public void testInsertComment() {
        String comment = "comment";
        
        xmlTreeBuilder.insert(new Token.Comment(comment));
        
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertTrue(xmlTreeBuilder.doc.childNode(0) instanceof Comment);
        Comment commentNode = (Comment) xmlTreeBuilder.doc.childNode(0);
        assertEquals(comment, commentNode.getData());
        assertEquals("http://example.com", commentNode.baseUri());
    }
    
    @Test
    public void testInsertCharacter() {
        String character = "character";
        
        xmlTreeBuilder.insert(new Token.Character(character));
        
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertTrue(xmlTreeBuilder.doc.childNode(0) instanceof TextNode);
        TextNode characterNode = (TextNode) xmlTreeBuilder.doc.childNode(0);
        assertEquals(character, characterNode.getWholeText());
        assertEquals("http://example.com", characterNode.baseUri());
    }
    
    @Test
    public void testInsertDoctype() {
        String name = "doctype";
        String publicIdentifier = "publicIdentifier";
        String systemIdentifier = "systemIdentifier";
        
        xmlTreeBuilder.insert(new Token.Doctype(name, publicIdentifier, systemIdentifier));
        
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertTrue(xmlTreeBuilder.doc.childNode(0) instanceof DocumentType);
        DocumentType doctype = (DocumentType) xmlTreeBuilder.doc.childNode(0);
        assertEquals(name, doctype.getName());
        assertEquals(publicIdentifier, doctype.getPublicIdentifier());
        assertEquals(systemIdentifier, doctype.getSystemIdentifier());
        assertEquals("http://example.com", doctype.baseUri());
    }
    
    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("tag");
        Element element1 = new Element(Tag.newInstance("tag"), "http://example.com", new Attributes());
        Element element2 = new Element(Tag.newInstance("tag"), "http://example.com", new Attributes());
        Element element3 = new Element(Tag.newInstance("tag"), "http://example.com", new Attributes());
        
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);
        xmlTreeBuilder.stack.add(element3);
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertFalse(xmlTreeBuilder.stack.contains(element1));
        assertFalse(xmlTreeBuilder.stack.contains(element2));
        assertTrue(xmlTreeBuilder.stack.contains(element3));
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<tag>Test</tag>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);
        
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("tag", element.tagName());
        assertEquals("Test", element.text());
    }
}