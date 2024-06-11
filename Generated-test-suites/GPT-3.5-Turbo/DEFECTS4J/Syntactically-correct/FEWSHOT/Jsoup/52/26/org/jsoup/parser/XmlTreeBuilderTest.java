package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Document;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;
    
    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document.OutputSettings.Syntax syntax = Document.OutputSettings.Syntax.xml;
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        assertEquals(input, xmlTreeBuilder.getInput());
        assertEquals(baseUri, xmlTreeBuilder.getBaseUri());
        assertEquals(errors, xmlTreeBuilder.getErrors());
        assertEquals(syntax, xmlTreeBuilder.getDoc().outputSettings().syntax());
        assertTrue(xmlTreeBuilder.getStack().contains(xmlTreeBuilder.getDoc()));
    }
    
    @Test
    public void testProcess() {
        Token startTagToken = new Token.StartTag("div", "id", "test");
        
        assertTrue(xmlTreeBuilder.process(startTagToken));
        assertEquals(1, xmlTreeBuilder.getStack().size());
        assertEquals(startTagToken.asStartTag().name(), xmlTreeBuilder.getCurrentElement().nodeName());
        
        Token endTagToken = new Token.EndTag("div");
        
        assertTrue(xmlTreeBuilder.process(endTagToken));
        assertEquals(0, xmlTreeBuilder.getStack().size());
    }
    
    @Test
    public void testInsertNode() {
        Node node = new TextNode("Test Node");
        Element currentElement = xmlTreeBuilder.getCurrentElement();
        int childNodeCount = currentElement.childNodeSize();
        
        xmlTreeBuilder.insertNode(node);
        
        assertEquals(childNodeCount + 1, currentElement.childNodeSize());
        assertEquals(node, currentElement.childNode(childNodeCount));
    }
    
    @Test
    public void testInsert() {
        Token.StartTag startTag = new Token.StartTag("h1", "class", "title");
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertEquals(startTag.name(), element.nodeName());
        assertEquals(startTag.attributes.get("class"), element.attributes().get("class"));
        assertTrue(xmlTreeBuilder.getStack().contains(element));
        
        startTag.setSelfClosing(true);
        
        xmlTreeBuilder.insert(startTag);
        
        assertTrue(element.tag().isKnownTag());
        assertTrue(element.tag().isSelfClosing());
    }
    
    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("Test Comment");
        
        xmlTreeBuilder.insert(commentToken);
        
        assertEquals(1, xmlTreeBuilder.getCurrentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.getCurrentElement().childNode(0) instanceof Comment);
    }
    
    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character("Test Character");
        
        xmlTreeBuilder.insert(characterToken);
        
        assertEquals(1, xmlTreeBuilder.getCurrentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.getCurrentElement().childNode(0) instanceof TextNode);
    }
    
    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        
        xmlTreeBuilder.insert(doctype);
        
        assertEquals(1, xmlTreeBuilder.getCurrentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.getCurrentElement().childNode(0) instanceof DocumentType);
    }
    
    @Test
    public void testPopStackToClose() {
        Element element1 = new Element("div");
        Element element2 = new Element("span");
        
        xmlTreeBuilder.getStack().add(element1);
        xmlTreeBuilder.getStack().add(element2);
        
        Token.EndTag endTag = new Token.EndTag("div");
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertEquals(1, xmlTreeBuilder.getStack().size());
        assertTrue(xmlTreeBuilder.getStack().contains(element2));
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<p>Test Fragment</p>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);
        
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("p", nodes.get(0).nodeName());
    }
}