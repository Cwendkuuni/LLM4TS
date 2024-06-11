package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token.CData;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.helper.Validate;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.ArrayList;

public class XmlTreeBuilderTest {
    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }
    
    @Test
    public void testParseWithReaderAndBaseUri() {
        String input = "<xml><tag></tag></xml>";
        String baseUri = "https://www.example.com";
        Reader reader = new StringReader(input);
        
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(reader, baseUri);
        
        assertEquals("xml", doc.nodeName());
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals(1, doc.childNodeSize());
        assertTrue(doc.childNode(0) instanceof Element);
    }
    
    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<xml><tag></tag></xml>";
        String baseUri = "https://www.example.com";
        
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(input, baseUri);
        
        assertEquals("xml", doc.nodeName());
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals(1, doc.childNodeSize());
        assertTrue(doc.childNode(0) instanceof Element);
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<xml><tag></tag></xml>";
        String baseUri = "https://www.example.com";
        Reader reader = new StringReader(input);
        
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        builder.initialiseParse(reader, baseUri, errors, settings);
        
        Document doc = builder.getDocument();
        assertEquals("xml", doc.nodeName());
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals(1, doc.childNodeSize());
        assertTrue(doc.childNode(0) instanceof Element);
    }
    
    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse(new StringReader(""), "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        
        Token.StartTag startTag = Token.StartTag.valueOf("tag");
        Element element = builder.insert(startTag);
        
        assertEquals("tag", element.nodeName());
        assertEquals(0, element.childNodeSize());
    }
    
    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse(new StringReader(""), "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        
        Token.StartTag startTag = Token.StartTag.valueOf("tag");
        builder.insert(startTag);
        Token.EndTag endTag = Token.EndTag.valueOf("tag");
        builder.process(endTag);
        
        Document doc = builder.getDocument();
        assertEquals("tag", doc.nodeName());
        assertEquals(0, doc.childNodeSize());
    }
    
    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse(new StringReader(""), "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        
        Token.Comment commentToken = Token.Comment.valueOf("comment");
        builder.process(commentToken);
        
        Document doc = builder.getDocument();
        assertEquals(1, doc.childNodeSize());
        assertTrue(doc.childNode(0) instanceof Comment);
        Comment comment = (Comment) doc.childNode(0);
        assertEquals("comment", comment.getData());
    }
    
    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse(new StringReader(""), "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        
        Token.Character characterToken = Token.Character.valueOf("character");
        builder.process(characterToken);
        
        Document doc = builder.getDocument();
        assertEquals(1, doc.childNodeSize());
        assertTrue(doc.childNode(0) instanceof TextNode);
        TextNode textNode = (TextNode) doc.childNode(0);
        assertEquals("character", textNode.getWholeText());
    }
    
    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse(new StringReader(""), "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        
        Token.Doctype doctype = Token.Doctype.valueOf("html");
        builder.process(doctype);
        
        Document doc = builder.getDocument();
        assertEquals(1, doc.childNodeSize());
        assertTrue(doc.childNode(0) instanceof DocumentType);
        DocumentType doctypeNode = (DocumentType) doc.childNode(0);
        assertEquals("html", doctypeNode.nodeName());
    }
    
    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse(new StringReader(""), "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        
        Element element = new Element("tag");
        builder.insertNode(element);
        
        Document doc = builder.getDocument();
        assertEquals(1, doc.childNodeSize());
        assertTrue(doc.childNode(0) instanceof Element);
        assertEquals("tag", doc.childNode(0).nodeName());
    }
    
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse(new StringReader(""), "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        
        Token.StartTag startTag1 = Token.StartTag.valueOf("tag1");
        builder.insert(startTag1);
        Token.StartTag startTag2 = Token.StartTag.valueOf("tag2");
        builder.insert(startTag2);
        
        Token.EndTag endTag1 = Token.EndTag.valueOf("tag2");
        builder.popStackToClose(endTag1);
        
        Document doc = builder.getDocument();
        assertEquals(1, doc.childNodeSize());
        assertEquals("tag1", doc.childNode(0).nodeName());
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<tag>text</tag>";
        String baseUri = "https://www.example.com";
        
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, errors, settings);
        
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("tag", element.nodeName());
        assertEquals("https://www.example.com", element.baseUri());
        assertEquals(1, element.childNodeSize());
        assertTrue(element.childNode(0) instanceof TextNode);
        TextNode textNode = (TextNode) element.childNode(0);
        assertEquals("text", textNode.getWholeText());
    }
}