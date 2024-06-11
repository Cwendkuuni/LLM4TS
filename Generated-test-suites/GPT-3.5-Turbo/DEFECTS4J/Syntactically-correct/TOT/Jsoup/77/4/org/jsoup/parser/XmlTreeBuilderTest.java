package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder xmlTreeBuilder;
    
    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }
    
    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
        // assert the default settings have been correctly initialized
        assertEquals(ParseSettings.preserveCase, settings);
    }
    
    @Test
    public void testParseWithReaderAndBaseUri() {
        Reader reader = new StringReader("<root>Test</root>");
        String baseUri = "https://example.com/";
        
        Document document = xmlTreeBuilder.parse(reader, baseUri);
        assertNotNull(document);
        // assert the correct document structure has been created
        assertEquals("Test", document.text());
    }
    
    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<root>Test</root>";
        String baseUri = "https://example.com/";
        
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
        // assert the correct document structure has been created
        assertEquals("Test", document.text());
    }
    
    @Test
    public void testInitialiseParse() {
        Reader reader = new StringReader("<root>Test</root>");
        String baseUri = "https://example.com/";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        
        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);
        
        // assert the stack has been initialized correctly
        assertNotNull(xmlTreeBuilder.stack);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));
        
        // assert the document output settings have been set to XML syntax
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testProcessStartTag() {
        Tag tag = Tag.valueOf("root", ParseSettings.preserveCase);
        Token.StartTag startTag = new Token.StartTag(tag);
        
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        // assert the correct element has been created and added to the current element
        assertEquals("root", element.tagName());
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
    }
    
    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("root", ParseSettings.preserveCase), "https://example.com/");
        TextNode textNode = new TextNode("Test");
        
        xmlTreeBuilder.insertNode(element);
        xmlTreeBuilder.insertNode(textNode);
        
        // assert the correct node has been inserted as a child of the current element
        assertEquals(2, xmlTreeBuilder.currentElement().childNodeSize());
        assertEquals(textNode, xmlTreeBuilder.currentElement().childNode(1));
    }
    
    @Test
    public void testInsertComment() {
        Token.Comment comment = new Token.Comment("Test Comment");
        
        xmlTreeBuilder.insert(comment);
        
        // assert the correct comment has been inserted as a child of the current element
        assertEquals("Test Comment", xmlTreeBuilder.currentElement().childNode(0).toString());
    }
    
    @Test
    public void testInsertCharacter() {
        Token.Character character = new Token.Character("Test Character");
        
        xmlTreeBuilder.insert(character);
        
        // assert the correct character has been inserted as a child of the current element
        assertEquals("Test Character", xmlTreeBuilder.currentElement().childNode(0).toString());
    }
    
    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        
        xmlTreeBuilder.insert(doctype);
        
        // assert the correct doctype has been inserted as a child of the current element
        assertEquals(DocumentType.class, xmlTreeBuilder.currentElement().childNode(0).getClass());
    }
    
    @Test
    public void testPopStackToClose() {
        Element rootElement = new Element(Tag.valueOf("root", ParseSettings.preserveCase), "https://example.com/");
        Element childElement = new Element(Tag.valueOf("child", ParseSettings.preserveCase), "https://example.com/");
        xmlTreeBuilder.stack.add(rootElement);
        xmlTreeBuilder.stack.add(childElement);
        
        Token.EndTag endTag = new Token.EndTag("root");
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        // assert the correct elements have been removed from the stack
        assertEquals(0, xmlTreeBuilder.stack.size());
    }
    
    @Test
    public void testParseFragment() {
        String input = "<root>Test</root>";
        String baseUri = "https://example.com/";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(input, baseUri, errors, settings);
        assertNotNull(nodes);
        // assert the correct fragment nodes have been parsed and returned
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("root", ((Element) nodes.get(0)).tagName());
    }
}