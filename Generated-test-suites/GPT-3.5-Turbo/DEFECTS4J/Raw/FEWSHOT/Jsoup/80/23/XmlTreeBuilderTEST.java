package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder builder;
    
    @Before
    public void setUp() {
        builder = new XmlTreeBuilder();
    }
    
    @Test
    public void testDefaultSettings() {
        ParseSettings settings = builder.defaultSettings();
        assertNotNull(settings);
        assertEquals(ParseSettings.preserveCase, settings);
    }
    
    @Test
    public void testParseReaderString() {
        Reader input = new StringReader("<xml><test></test></xml>");
        String baseUri = "http://example.com";
        
        Document document = builder.parse(input, baseUri);
        
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        
        Element rootElement = document.children().get(0);
        assertEquals("xml", rootElement.nodeName());
        
        Element testElement = rootElement.children().get(0);
        assertEquals("test", testElement.nodeName());
    }
    
    @Test
    public void testParseStringString() {
        String input = "<xml><test></test></xml>";
        String baseUri = "http://example.com";
        
        Document document = builder.parse(input, baseUri);
        
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        
        Element rootElement = document.children().get(0);
        assertEquals("xml", rootElement.nodeName());
        
        Element testElement = rootElement.children().get(0);
        assertEquals("test", testElement.nodeName());
    }
    
    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("<xml></xml>");
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        builder.initialiseParse(input, baseUri, errors, settings);
        
        Document document = builder.document;
        assertNotNull(document);
        assertTrue(builder.stack.contains(document));
        assertEquals(Document.OutputSettings.Syntax.xml, document.outputSettings().syntax());
    }
    
    @Test
    public void testProcessStartTagToken() {
        Token.StartTag token = new Token.StartTag();
        token.name("test");
        
        builder.process(token);
        
        Document document = builder.document;
        assertNotNull(document);
        
        Element rootElement = document.children().get(0);
        assertEquals("test", rootElement.nodeName());
    }
    
    @Test
    public void testProcessEndTagToken() {
        Token.StartTag startTagToken = new Token.StartTag();
        startTagToken.name("test");
        
        builder.process(startTagToken);
        
        Token.EndTag endTagToken = new Token.EndTag();
        endTagToken.tagName("test");
        
        builder.process(endTagToken);
        
        Document document = builder.document;
        assertNotNull(document);
        assertTrue(builder.stack.isEmpty());
    }
    
    @Test
    public void testProcessCommentToken() {
        String data = "test comment";
        
        Token.Comment token = new Token.Comment();
        token.data(data);
        
        builder.process(token);
        
        Document document = builder.document;
        assertNotNull(document);
        
        Comment comment = (Comment) document.childNodes().get(0);
        assertEquals(data, comment.getData());
    }
    
    @Test
    public void testProcessCharacterToken() {
        String data = "test text";
        
        Token.Character token = new Token.Character();
        token.data(data);
        
        builder.process(token);
        
        Document document = builder.document;
        assertNotNull(document);
        
        TextNode textNode = (TextNode) document.childNodes().get(0);
        assertEquals(data, textNode.getWholeText());
    }
    
    @Test
    public void testProcessDoctypeToken() {
        String name = "html";
        String publicIdentifier = "-//W3C//DTD XHTML 1.0 Strict//EN";
        String systemIdentifier = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
        String pubSysKey = null;
        
        Token.Doctype token = new Token.Doctype();
        token.name(name);
        token.publicIdentifier(publicIdentifier);
        token.systemIdentifier(systemIdentifier);
        token.pubSysKey(pubSysKey);
        
        builder.process(token);
        
        Document document = builder.document;
        assertNotNull(document);
        
        org.jsoup.nodes.DocumentType doctype = document.childNode(0);
        assertEquals(name, doctype.name());
        assertEquals(publicIdentifier, doctype.publicIdentifier());
        assertEquals(systemIdentifier, doctype.systemIdentifier());
        assertEquals(pubSysKey, doctype.pubSysKey());
    }
    
    @Test
    public void testInsertNode() {
        Document document = new Document("");
        builder.document = document;
        
        Element parentElement = document.createElement("parent");
        document.appendChild(parentElement);
        
        TextNode textNode = new TextNode("test text", "");
        builder.insertNode(textNode);
        
        assertEquals(textNode, parentElement.childNode(0));
    }
    
    @Test
    public void testInsertStartTagToken() {
        Token.StartTag token = new Token.StartTag();
        token.name("test");
        token.attributes().put("attr", "value");
        
        builder.insert(token);
        
        Document document = builder.document;
        assertNotNull(document);
        
        Element rootElement = document.children().get(0);
        assertEquals("test", rootElement.nodeName());
        assertTrue(rootElement.hasAttr("attr"));
        assertEquals("value", rootElement.attr("attr"));
    }
    
    @Test
    public void testInsertCommentToken() {
        String data = "test comment";
        
        Token.Comment token = new Token.Comment();
        token.data(data);
        
        builder.insert(token);
        
        Document document = builder.document;
        assertNotNull(document);
        
        Comment comment = (Comment) document.childNodes().get(0);
        assertEquals(data, comment.getData());
    }
    
    @Test
    public void testInsertCharacterToken() {
        String data = "test text";
        
        Token.Character token = new Token.Character();
        token.data(data);
        
        builder.insert(token);
        
        Document document = builder.document;
        assertNotNull(document);
        
        TextNode textNode = (TextNode) document.childNodes().get(0);
        assertEquals(data, textNode.getWholeText());
    }
    
    @Test
    public void testInsertDoctypeToken() {
        String name = "html";
        String publicIdentifier = "-//W3C//DTD XHTML 1.0 Strict//EN";
        String systemIdentifier = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
        String pubSysKey = null;
        
        Token.Doctype token = new Token.Doctype();
        token.name(name);
        token.publicIdentifier(publicIdentifier);
        token.systemIdentifier(systemIdentifier);
        token.pubSysKey(pubSysKey);
        
        builder.insert(token);
        
        Document document = builder.document;
        assertNotNull(document);
        
        org.jsoup.nodes.DocumentType doctype = document.childNode(0);
        assertEquals(name, doctype.name());
        assertEquals(publicIdentifier, doctype.publicIdentifier());
        assertEquals(systemIdentifier, doctype.systemIdentifier());
        assertEquals(pubSysKey, doctype.pubSysKey());
    }
    
    @Test
    public void testPopStackToClose() {
        Token.StartTag startTagToken = new Token.StartTag();
        startTagToken.name("test");
        
        builder.process(startTagToken);
        
        Token.EndTag endTagToken = new Token.EndTag();
        endTagToken.tagName("test");
        
        builder.process(endTagToken);
        
        Document document = builder.document;
        assertNotNull(document);
        assertTrue(builder.stack.isEmpty());
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<test>text</test>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, errors, settings);
        
        assertNotNull(nodes);
        assertFalse(nodes.isEmpty());
        
        Element rootElement = (Element) nodes.get(0);
        assertEquals("test", rootElement.nodeName());
        
        TextNode textNode = (TextNode) rootElement.childNodes().get(0);
        assertEquals("text", textNode.getWholeText());
    }
}