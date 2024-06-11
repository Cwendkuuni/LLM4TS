package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder xmlTreeBuilder;
    
    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<root></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        // Assert the stack contains the document
        Assertions.assertEquals(xmlTreeBuilder.stack.size(), 1);
        Assertions.assertEquals(xmlTreeBuilder.stack.get(0), xmlTreeBuilder.doc);
        
        // Assert the document's output settings syntax is set to xml
        Assertions.assertEquals(xmlTreeBuilder.doc.outputSettings().syntax(),
                Document.OutputSettings.Syntax.xml);
    }
    
    @Test
    public void testProcess_StartTag() {
        Token token = new Token.StartTag("tag", "attr1", "value1");
        
        boolean result = xmlTreeBuilder.process(token);
        
        // Assert the token is inserted as a new element
        Assertions.assertTrue(result);
        Assertions.assertEquals(xmlTreeBuilder.stack.size(), 2);
        Assertions.assertEquals(xmlTreeBuilder.stack.get(1).nodeName(), "tag");
        Assertions.assertEquals(xmlTreeBuilder.stack.get(1).attributes().get("attr1"), "value1");
    }
    
    @Test
    public void testProcess_EndTag() {
        Element element = new Element(Tag.valueOf("tag"), "http://example.com");
        xmlTreeBuilder.stack.add(element);
        
        Token token = new Token.EndTag("tag");
        
        boolean result = xmlTreeBuilder.process(token);
        
        // Assert the stack is popped to close the element
        Assertions.assertTrue(result);
        Assertions.assertEquals(xmlTreeBuilder.stack.size(), 1);
        Assertions.assertEquals(xmlTreeBuilder.stack.get(0), xmlTreeBuilder.doc);
    }
    
    @Test
    public void testProcess_Comment() {
        Token token = new Token.Comment("comment");
        
        boolean result = xmlTreeBuilder.process(token);
        
        // Assert the comment token is inserted as a comment node
        Assertions.assertTrue(result);
        Assertions.assertEquals(xmlTreeBuilder.stack.size(), 1);
        Assertions.assertEquals(xmlTreeBuilder.stack.get(0).childNode(0).nodeName(), "#comment");
        Assertions.assertEquals(xmlTreeBuilder.stack.get(0).childNode(0).toString(), "comment");
    }
    
    @Test
    public void testProcess_Character() {
        Token token = new Token.Character("data");
        
        boolean result = xmlTreeBuilder.process(token);
        
        // Assert the character token is inserted as a text node
        Assertions.assertTrue(result);
        Assertions.assertEquals(xmlTreeBuilder.stack.size(), 1);
        Assertions.assertEquals(xmlTreeBuilder.stack.get(0).childNode(0).nodeName(), "#text");
        Assertions.assertEquals(xmlTreeBuilder.stack.get(0).childNode(0).toString(), "data");
    }
    
    @Test
    public void testProcess_Doctype() {
        Token token = new Token.Doctype("name", "public", "system");
        
        boolean result = xmlTreeBuilder.process(token);
        
        // Assert the doctype token is inserted as a doctype node
        Assertions.assertTrue(result);
        Assertions.assertEquals(xmlTreeBuilder.stack.size(), 1);
        Assertions.assertEquals(xmlTreeBuilder.stack.get(0).childNode(0).nodeName(), "#doctype");
        Assertions.assertEquals(xmlTreeBuilder.stack.get(0).childNode(0).toString(), "<!DOCTYPE name PUBLIC \"public\" \"system\">");
    }
    
    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("tag"), "http://example.com");
        xmlTreeBuilder.stack.add(element);
        
        Node node = new TextNode("text", xmlTreeBuilder.baseUri);
        
        xmlTreeBuilder.insertNode(node);
        
        // Assert the node is appended to the current element as a child node
        Assertions.assertEquals(element.childNode(0), node);
    }
    
    @Test
    public void testInsert_StartTag() {
        Token.StartTag startTag = new Token.StartTag("tag", "attr1", "value1");
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        // Assert a new element is inserted and returned
        Assertions.assertEquals(xmlTreeBuilder.stack.size(), 2);
        Assertions.assertEquals(xmlTreeBuilder.stack.get(1), element);
        Assertions.assertEquals(element.nodeName(), "tag");
        Assertions.assertEquals(element.attributes().get("attr1"), "value1");
    }
    
    @Test
    public void testInsert_StartTag_SelfClosing() {
        Token.StartTag startTag = new Token.StartTag("tag", "attr1", "value1");
        startTag.selfClosing = true;
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        // Assert a new element is inserted and returned
        Assertions.assertEquals(xmlTreeBuilder.stack.size(), 1);
        Assertions.assertEquals(element.nodeName(), "tag");
        Assertions.assertEquals(element.attributes().get("attr1"), "value1");
        Assertions.assertTrue(element.tag().isKnownTag());
        Assertions.assertTrue(element.tag().isSelfClosing());
    }
    
    @Test
    public void testInsert_StartTag_Unknown() {
        Token.StartTag startTag = new Token.StartTag("unknown", "attr1", "value1");
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        // Assert a new element is inserted and returned
        Assertions.assertEquals(xmlTreeBuilder.stack.size(), 2);
        Assertions.assertEquals(xmlTreeBuilder.stack.get(1), element);
        Assertions.assertEquals(element.nodeName(), "unknown");
        Assertions.assertEquals(element.attributes().get("attr1"), "value1");
    }
    
    @Test
    public void testInsert_Comment() {
        Token.Comment commentToken = new Token.Comment("comment");
        
        xmlTreeBuilder.insert(commentToken);
        
        // Assert the comment token is inserted as a comment node
        Assertions.assertEquals(xmlTreeBuilder.stack.size(), 1);
        Assertions.assertEquals(xmlTreeBuilder.stack.get(0).childNode(0).nodeName(), "#comment");
        Assertions.assertEquals(xmlTreeBuilder.stack.get(0).childNode(0).toString(), "comment");
    }
    
    @Test
    public void testInsert_Comment_Bogus() {
        Token.Comment commentToken = new Token.Comment("<!DOCTYPE name PUBLIC \"public\" \"system\">");
        commentToken.bogus = true;
        
        xmlTreeBuilder.insert(commentToken);
        
        // Assert the comment token is inserted as a comment node and parsed as a doctype declaration
        Assertions.assertEquals(xmlTreeBuilder.stack.size(), 1);
        Assertions.assertEquals(xmlTreeBuilder.stack.get(0).childNode(0).nodeName(), "#doctype");
        Assertions.assertEquals(xmlTreeBuilder.stack.get(0).childNode(0).toString(),
                "<!DOCTYPE name PUBLIC \"public\" \"system\">");
    }
    
    @Test
    public void testInsert_Character() {
        Token.Character characterToken = new Token.Character("data");
        
        xmlTreeBuilder.insert(characterToken);
        
        // Assert the character token is inserted as a text node
        Assertions.assertEquals(xmlTreeBuilder.stack.size(), 1);
        Assertions.assertEquals(xmlTreeBuilder.stack.get(0).childNode(0).nodeName(), "#text");
        Assertions.assertEquals(xmlTreeBuilder.stack.get(0).childNode(0).toString(), "data");
    }
    
    @Test
    public void testInsert_Doctype() {
        Token.Doctype doctype = new Token.Doctype("name", "public", "system");
        
        xmlTreeBuilder.insert(doctype);
        
        // Assert the doctype token is inserted as a doctype node
        Assertions.assertEquals(xmlTreeBuilder.stack.size(), 1);
        Assertions.assertEquals(xmlTreeBuilder.stack.get(0).childNode(0).nodeName(), "#doctype");
        Assertions.assertEquals(xmlTreeBuilder.stack.get(0).childNode(0).toString(),
                "<!DOCTYPE name PUBLIC \"public\" \"system\">");
    }
    
    @Test
    public void testPopStackToClose() {
        Element element1 = new Element(Tag.valueOf("tag"), "http://example.com");
        Element element2 = new Element(Tag.valueOf("tag"), "http://example.com");
        Element element3 = new Element(Tag.valueOf("tag"), "http://example.com");
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);
        xmlTreeBuilder.stack.add(element3);
        
        Token.EndTag endTag = new Token.EndTag("tag");
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        // Assert the stack is popped until the first occurrence of the endTag's name
        Assertions.assertEquals(xmlTreeBuilder.stack.size(), 2);
        Assertions.assertEquals(xmlTreeBuilder.stack.get(0), element1);
        Assertions.assertEquals(xmlTreeBuilder.stack.get(1), xmlTreeBuilder.doc);
    }
    
    @Test
    public void testPopStackToClose_NotFound() {
        Element element1 = new Element(Tag.valueOf("tag1"), "http://example.com");
        Element element2 = new Element(Tag.valueOf("tag2"), "http://example.com");
        Element element3 = new Element(Tag.valueOf("tag3"), "http://example.com");
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);
        xmlTreeBuilder.stack.add(element3);
        
        Token.EndTag endTag = new Token.EndTag("tag");
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        // Assert no elements are popped from the stack
        Assertions.assertEquals(xmlTreeBuilder.stack.size(), 3);
        Assertions.assertEquals(xmlTreeBuilder.stack.get(0), element1);
        Assertions.assertEquals(xmlTreeBuilder.stack.get(1), element2);
        Assertions.assertEquals(xmlTreeBuilder.stack.get(2), element3);
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<tag>text</tag>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);
        
        // Assert the inputFragment is parsed into a list of nodes
        Assertions.assertEquals(nodes.size(), 1);
        Assertions.assertEquals(nodes.get(0).nodeName(), "tag");
        Assertions.assertEquals(nodes.get(0).childNode(0).nodeName(), "#text");
        Assertions.assertEquals(nodes.get(0).childNode(0).toString(), "text");
    }
}