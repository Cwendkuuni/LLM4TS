package org.jsoup.parser;

import org.junit.*;
import static org.junit.Assert.*;
import org.jsoup.nodes.*;
import org.jsoup.parser.Token.*;
import org.jsoup.helper.StringUtil;
import java.util.List;

public class XmlTreeBuilderTest {
    
    // Test for defaultSetting() method
    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
        
        assertNotNull(parseSettings);
        assertEquals(ParseSettings.preserveCase, parseSettings);
    }
    
    // Test for parse(Reader input, String baseUri) method
    @Test
    public void testParseWithReader() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        
        String input = "<root></root>";
        Reader reader = new StringReader(input);
        String baseUri = "https://www.example.com";
        
        Document document = xmlTreeBuilder.parse(reader, baseUri);
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertEquals(input, document.html());
    }
    
    // Test for parse(String input, String baseUri) method
    @Test
    public void testParseWithString() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        
        String input = "<root></root>";
        String baseUri = "https://www.example.com";
        
        Document document = xmlTreeBuilder.parse(input, baseUri);
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertEquals(input, document.html());
    }
    
    // Test for initialiseParse(Reader input, String baseUri, ParseErrorList errors, ParseSettings settings) method
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        
        String input = "<root></root>";
        Reader reader = new StringReader(input);
        String baseUri = "https://www.example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);
        
        assertNotNull(xmlTreeBuilder.stack);
        assertNotNull(xmlTreeBuilder.doc);
        assertEquals(doc, xmlTreeBuilder.stack.get(0));
    }
    
    // Test for process(Token token) method
    @Test
    public void testProcess() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        
        StartTag startTag = new Token.StartTag("", "name", "");
        xmlTreeBuilder.process(startTag);
        
        assertNotNull(xmlTreeBuilder.currentElement());
        assertNotNull(xmlTreeBuilder.stack);
        assertEquals(xmlTreeBuilder.currentElement(), xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1));
        assertNotEquals(startTag, xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1));
    }
    
    // Test for insertNode(Node node) method
    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        
        Element element = new Element(Tag.valueOf("p"), "https://www.example.com", ParseSettings.preserveCase);
        xmlTreeBuilder.insertNode(element);
        
        assertNotNull(xmlTreeBuilder.currentElement());
        assertNotNull(xmlTreeBuilder.currentElement().parent());
        assertTrue(xmlTreeBuilder.currentElement().parent().childNodes().size() > 0);
        assertEquals(element, xmlTreeBuilder.currentElement().parent().childNodes().get(xmlTreeBuilder.currentElement().parent().childNodes().size() - 1));
    }
    
    // Test for insert(Token.StartTag startTag) method
    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        
        Token.StartTag startTag = new Token.StartTag("", "name", "");
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertNotNull(element);
        assertNotNull(xmlTreeBuilder.currentElement());
        assertNotNull(xmlTreeBuilder.stack);
        assertNotNull(xmlTreeBuilder.currentElement().parent());
        assertNotNull(xmlTreeBuilder.currentElement().tag());
        assertEquals(element, xmlTreeBuilder.currentElement().parent().childNodes().get(xmlTreeBuilder.currentElement().parent().childNodes().size() - 1));
    }
    
    // Test for insert(Token.Comment commentToken) method
    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        
        Token.Comment commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);
        
        assertNotNull(xmlTreeBuilder.currentElement());
        assertNotNull(xmlTreeBuilder.currentElement().parent());
        assertNotNull(xmlTreeBuilder.currentElement().parent().childNodes().get(xmlTreeBuilder.currentElement().parent().childNodes().size() - 1));
        assertTrue(xmlTreeBuilder.currentElement().parent().childNodes().get(xmlTreeBuilder.currentElement().parent().childNodes().size() - 1) instanceof Comment);
    }
    
    // Test for insert(Token.Character token) method
    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        
        Token.Character token = new Token.Character("This is a character");
        xmlTreeBuilder.insert(token);
        
        assertNotNull(xmlTreeBuilder.currentElement());
        assertNotNull(xmlTreeBuilder.currentElement().parent());
        assertNotNull(xmlTreeBuilder.currentElement().parent().childNodes().get(xmlTreeBuilder.currentElement().parent().childNodes().size() - 1));
        assertTrue(token.isCData() != StringUtil.isWhitespace(normalizedText));
        assertTrue(xmlTreeBuilder.currentElement().parent().childNodes().get(xmlTreeBuilder.currentElement().parent().childNodes().size() - 1) instanceof TextNode);
    }
    
    // Test for insert(Token.Doctype d) method
    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        
        Token.Doctype doctype = new Token.Doctype("html");
        xmlTreeBuilder.insert(doctype);
        
        assertNotNull(xmlTreeBuilder.currentElement());
        assertNotNull(xmlTreeBuilder.currentElement().parent());
        assertNotNull(xmlTreeBuilder.currentElement().parent().childsNodes().get(xmlTreeBuilder.currentElement().parent().childNodes().size() - 1));
        assertTrue(xmlTreeBuilder.currentElement().parent().childNodes().get(xmlTreeBuilder.currentElement().parent().childNodes().size() - 1) instanceof DocumentType);
    }
    
    // Test for popStackToClose(Token.EndTag endTag) method
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        
        Token.EndTag endTag = new Token.EndTag("", "name");
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertNotNull(xmlTreeBuilder.stack);
        assertNotNull(xmlTreeBuilder.stack.get(0));
        assertNotNull(xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1));
        assertNotEquals(endTag, xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1));
    }
    
    // Test for parseFragment(String inputFragment, String baseUri, ParseErrorList errors, ParseSettings settings) method
    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        
        String inputFragment = "<root></root>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        
        assertNotNull(nodes);
        assertTrue(nodes.size() > 0);
    }
    
}