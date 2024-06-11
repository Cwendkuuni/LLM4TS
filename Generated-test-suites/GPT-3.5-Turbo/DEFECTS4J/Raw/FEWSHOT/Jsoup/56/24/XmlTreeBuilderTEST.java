package org.jsoup.parser;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.helper.Validate;
import org.jsoup.Jsoup;
import java.util.List;
import org.jsoup.nodes.DocumentOutputSettings;

import static org.junit.Assert.*;
import org.junit.Test;

public class XmlTreeBuilderTest {
    
    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings defaultSettings = builder.defaultSettings();
        assertNotNull(defaultSettings);
        assertEquals(ParseSettings.preserveCase, defaultSettings);
    }
    
    @Test
    public void testParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<html><head><title>Test</title></head><body>Hello, world!</body></html>";
        String baseUri = "http://example.com";
        Document document = builder.parse(input, baseUri);
        assertNotNull(document);
        assertEquals(input, document.html());
        assertEquals(baseUri, document.baseUri());
    }
    
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<html><head><title>Test</title></head><body>Hello, world!</body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        builder.initialiseParse(input, baseUri, errors, settings);
        assertEquals(input, builder.input);
        assertEquals(baseUri, builder.baseUri);
        assertEquals(errors, builder.errors);
        assertEquals(settings, builder.settings);
        assertEquals(builder.doc, builder.stack.get(0));
        DocumentOutputSettings.Syntax xmlSyntax = Document.OutputSettings.Syntax.xml;
        assertEquals(xmlSyntax, builder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testProcess_StartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag token = new Token.StartTag("div");
        Element element = builder.insert(token);
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals(builder.baseUri, element.baseUri());
        assertEquals(builder.settings.normalizeAttributes(token.attributes), element.attributes());
        assertFalse(element.tag().isKnownTag());
        assertFalse(element.tag().isSelfClosing());
        assertFalse(builder.tokeniser.isSelfClosingFlagAcknowledged());
    }
    
    @Test
    public void testProcess_EndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag token = new Token.EndTag("div");
        builder.stack.add(new Element("div"));
        builder.stack.add(new Element("span"));
        builder.stack.add(new Element("p"));
        builder.stack.add(new Element("body"));
        builder.stack.add(builder.doc);
        builder.process(token);
        assertEquals(3, builder.stack.size());
        assertEquals("p", builder.stack.get(0).nodeName());
        assertEquals("span", builder.stack.get(1).nodeName());
        assertEquals("div", builder.stack.get(2).nodeName());
    }
    
    @Test
    public void testProcess_Comment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment token = new Token.Comment("Test Comment");
        builder.insert(token);
        List<Node> nodes = builder.stack.get(0).childNodes();
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Comment);
        Comment comment = (Comment) nodes.get(0);
        assertEquals("Test Comment", comment.getData());
    }
    
    @Test
    public void testProcess_Character() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character token = new Token.Character("Test Character");
        builder.insert(token);
        List<Node> nodes = builder.stack.get(0).childNodes();
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof TextNode);
        TextNode textNode = (TextNode) nodes.get(0);
        assertEquals("Test Character", textNode.getWholeText());
    }
    
    @Test
    public void testProcess_Doctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype token = new Token.Doctype("html", "", "", "");
        builder.insert(token);
        List<Node> nodes = builder.stack.get(0).childNodes();
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof DocumentType);
        DocumentType doctype = (DocumentType) nodes.get(0);
        assertEquals("html", doctype.getName());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertEquals(builder.baseUri, doctype.baseUri());
    }
    
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element("div"));
        builder.stack.add(new Element("span"));
        builder.stack.add(new Element("p"));
        builder.stack.add(new Element("body"));
        builder.stack.add(builder.doc);
        Token.EndTag endTag = new Token.EndTag("body");
        builder.popStackToClose(endTag);
        assertEquals(2, builder.stack.size());
        assertEquals("p", builder.stack.get(0).nodeName());
        assertEquals("span", builder.stack.get(1).nodeName());
    }
    
    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<div><span>Test</span></div>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, errors, settings);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element div = (Element) nodes.get(0);
        assertEquals("div", div.nodeName());
        assertEquals(baseUri, div.baseUri());
        List<Node> childNodes = div.childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Element);
        Element span = (Element) childNodes.get(0);
        assertEquals("span", span.nodeName());
    }
}