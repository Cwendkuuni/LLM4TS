package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }
    
    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
    }
    
    @Test
    public void testParseWithReaderAndBaseUri() {
        String input = "<html><head><title>Test</title></head><body><h1>Header</h1></body></html>";
        Reader reader = new StringReader(input);
        String baseUri = "https://www.example.com";
        
        Document doc = xmlTreeBuilder.parse(reader, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("<html><head><title>Test</title></head><body><h1>Header</h1></body></html>", doc.html());
    }
    
    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<html><head><title>Test</title></head><body><h1>Header</h1></body></html>";
        String baseUri = "https://www.example.com";
        
        Document doc = xmlTreeBuilder.parse(input, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("<html><head><title>Test</title></head><body><h1>Header</h1></body></html>", doc.html());
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<html><head><title>Test</title></head><body><h1>Header</h1></body></html>";
        Reader reader = new StringReader(input);
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        
        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);
        
        assertEquals(baseUri, xmlTreeBuilder.baseUri);
        assertEquals(errors, xmlTreeBuilder.errors);
        assertEquals(settings, xmlTreeBuilder.settings);
        assertFalse(xmlTreeBuilder.stack.isEmpty());
        assertNull(xmlTreeBuilder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testProcessWithStartTag() {
        Token startTagToken = new Token.StartTag("div", new Attributes());
        
        boolean result = xmlTreeBuilder.process(startTagToken);
        
        assertTrue(result);
    }
    
    @Test
    public void testProcessWithEndTag() {
        Token endTagToken = new Token.EndTag("div");
        
        boolean result = xmlTreeBuilder.process(endTagToken);
        
        assertTrue(result);
    }
    
    @Test
    public void testProcessWithComment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        
        boolean result = xmlTreeBuilder.process(commentToken);
        
        assertTrue(result);
    }
    
    @Test
    public void testProcessWithCharacter() {
        Token.Character characterToken = new Token.Character("This is a character");
        
        boolean result = xmlTreeBuilder.process(characterToken);
        
        assertTrue(result);
    }
    
    @Test
    public void testProcessWithDoctype() {
        Token.Doctype doctypeToken = new Token.Doctype("html", "", "");
        
        boolean result = xmlTreeBuilder.process(doctypeToken);
        
        assertTrue(result);
    }
    
    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("div"), "https://www.example.com");
        
        xmlTreeBuilder.insertNode(element);
        
        assertEquals(element, xmlTreeBuilder.currentElement().child(0));
    }
    
    @Test
    public void testInsertWithStartTag() {
        Token.StartTag startTag = new Token.StartTag("div", new Attributes());
        Element element = new Element(Tag.valueOf("div"), "https://www.example.com");
        Element expectedResult = new Element(Tag.valueOf("div"), "https://www.example.com");
        
        Element result = xmlTreeBuilder.insert(startTag);
        
        assertEquals(expectedResult.html(), result.html());
        if (result.isKnownTag()) {
            assertFalse(result.tag().isSelfClosing());
        } else {
            assertTrue(result.tag().isSelfClosing());
        }
        assertEquals(element.html(), xmlTreeBuilder.currentElement().html());
    }
    
    @Test
    public void testInsertWithComment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        Comment comment = new Comment("This is a comment");
        
        xmlTreeBuilder.insert(commentToken);
        
        assertEquals(comment.toString(), xmlTreeBuilder.currentElement().child(0).toString());
    }
    
    @Test
    public void testInsertWithCharacter() {
        Token.Character characterToken = new Token.Character("This is a character");
        TextNode textNode = new TextNode("This is a character");
        
        xmlTreeBuilder.insert(characterToken);
        
        assertEquals(textNode.toString(), xmlTreeBuilder.currentElement().child(0).toString());
    }
    
    @Test
    public void testInsertWithDoctype() {
        Token.Doctype doctypeToken = new Token.Doctype("html", "", "");
        DocumentType doctypeNode = new DocumentType("html", "", "");
        
        xmlTreeBuilder.insert(doctypeToken);
        
        assertEquals(doctypeNode.toString(), xmlTreeBuilder.currentElement().child(0).toString());
    }
    
    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("div");
        Element element = new Element(Tag.valueOf("div"), "https://www.example.com");
        xmlTreeBuilder.stack.push(element);
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<div><p>Paragraph 1</p><p>Paragraph 2</p></div>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        List<Node> expectedResult = new Element(Tag.valueOf("div"), "https://www.example.com").childNodes();
        
        List<Node> result = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        
        assertEquals(expectedResult.toString(), result.toString());
    }
}