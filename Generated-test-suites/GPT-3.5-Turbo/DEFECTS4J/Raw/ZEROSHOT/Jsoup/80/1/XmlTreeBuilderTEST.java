package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;
    private Reader input;
    private String baseUri;
    
    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        input = new StringReader("<root><element1>Value1</element1><element2>Value2</element2></root>");
        baseUri = "http://example.com";
    }
    
    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }
    
    @Test
    public void testParseReaderBaseUri() {
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertTrue(document instanceof Document);
    }
    
    @Test
    public void testParseStringBaseUri() {
        String inputString = "<root><element1>Value1</element1><element2>Value2</element2></root>";
        Document document = xmlTreeBuilder.parse(inputString, baseUri);
        assertTrue(document instanceof Document);
    }
    
    @Test
    public void testInitialiseParse() {
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        // assert something
    }
    
    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("element1");
        Element element = xmlTreeBuilder.insert(startTag);
        assertEquals("element1", element.tagName());
    }
    
    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("element2");
        xmlTreeBuilder.popStackToClose(endTag);
        // assert something
    }
    
    @Test
    public void testProcessComment() {
        Token.Comment comment = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(comment);
        // assert something
    }
    
    @Test
    public void testProcessCharacter() {
        Token.Character character = new Token.Character("Value1");
        xmlTreeBuilder.insert(character);
        // assert something
    }
    
    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "public", "system");
        xmlTreeBuilder.insert(doctype);
        // assert something
    }
    
    @Test
    public void testInsertNode() {
        Node node = new Element(Tag.valueOf("element"), "http://example.com", ParseSettings.preserveCase);
        xmlTreeBuilder.insertNode(node);
        // assert something
    }
    
    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("element");
        Element element = xmlTreeBuilder.insert(startTag);
        assertTrue(element instanceof Element);
    }
    
    @Test
    public void testInsertComment() {
        Token.Comment comment = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(comment);
        // assert something
    }
    
    @Test
    public void testInsertCharacter() {
        Token.Character character = new Token.Character("Value");
        xmlTreeBuilder.insert(character);
        // assert something
    }
    
    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "public", "system");
        xmlTreeBuilder.insert(doctype);
        // assert something
    }
    
    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("element");
        xmlTreeBuilder.popStackToClose(endTag);
        // assert something
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<element>Value</element>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertEquals(1, nodes.size());
    }
}