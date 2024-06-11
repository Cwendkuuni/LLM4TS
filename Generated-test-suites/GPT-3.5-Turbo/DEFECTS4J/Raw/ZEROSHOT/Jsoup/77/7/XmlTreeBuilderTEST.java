package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
        Reader input = new StringReader("<xml></xml>");
        String baseUri = "http://example.com";
        Document doc = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(doc);
    }
    
    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<xml></xml>";
        String baseUri = "http://example.com";
        Document doc = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(doc);
    }
    
    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("<xml></xml>");
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
    }
    
    @Test
    public void testProcessWithStartTag() {
        Token token = new Token.StartTag("tag", new Attributes());
        boolean result = xmlTreeBuilder.process(token);
        assertTrue(result);
    }
    
    @Test
    public void testProcessWithEndTag() {
        Token token = new Token.EndTag("tag");
        boolean result = xmlTreeBuilder.process(token);
        assertTrue(result);
    }
    
    @Test
    public void testProcessWithComment() {
        Token token = new Token.Comment("comment");
        boolean result = xmlTreeBuilder.process(token);
        assertTrue(result);
    }
    
    @Test
    public void testProcessWithCharacter() {
        Token token = new Token.Character("character");
        boolean result = xmlTreeBuilder.process(token);
        assertTrue(result);
    }
    
    @Test
    public void testProcessWithDoctype() {
        Token token = new Token.Doctype("doctype", "public", "system");
        boolean result = xmlTreeBuilder.process(token);
        assertTrue(result);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testProcessWithUnexpectedTokenType() {
        Token token = new Token(null, null);
        xmlTreeBuilder.process(token);
    }
    
    @Test
    public void testInsertNode() {
        Node node = new Element(Tag.valueOf("tag"), "http://example.com", new Attributes());
        xmlTreeBuilder.insertNode(node);
        Element element = xmlTreeBuilder.currentElement();
        assertNotNull(element);
        assertEquals("tag", element.tagName());
    }
    
    @Test
    public void testInsertWithStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag", new Attributes());
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals("tag", element.tagName());
    }
    
    @Test
    public void testInsertWithCommentToken() {
        Token.Comment commentToken = new Token.Comment("comment");
        xmlTreeBuilder.insert(commentToken);
        Element element = xmlTreeBuilder.currentElement();
        assertNotNull(element);
        assertEquals("comment", element.nodeName());
    }
    
    @Test
    public void testInsertWithCharacterToken() {
        Token.Character characterToken = new Token.Character("character");
        xmlTreeBuilder.insert(characterToken);
        Element element = xmlTreeBuilder.currentElement();
        assertNotNull(element);
        assertEquals("character", element.childNode(0).outerHtml());
    }
    
    @Test
    public void testInsertWithDoctype() {
        Token.Doctype doctype = new Token.Doctype("doctype", "public", "system");
        xmlTreeBuilder.insert(doctype);
        Element element = xmlTreeBuilder.currentElement();
        assertNotNull(element);
        assertEquals("doctype", element.tagName());
    }
    
    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("tag");
        xmlTreeBuilder.popStackToClose(endTag);
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<xml></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
    }
    
}