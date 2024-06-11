package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder builder;

    @Before
    public void setup() {
        builder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = builder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        String xml = "<root><child>content</child></root>";
        Reader input = new StringReader(xml);
        String baseUri = "http://example.com";
        Document document = builder.parse(input, baseUri);
        
        assertEquals("http://example.com", document.baseUri());
        assertEquals("root", document.nodeName());
        assertEquals(1, document.childNodeSize());
        assertTrue(document.childNode(0) instanceof Element);
        
        Element root = (Element) document.childNode(0);
        assertEquals("child", root.childNode(0).nodeName());
        assertEquals("content", root.childNode(0).childNode(0).outerHtml());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String xml = "<root><child>content</child></root>";
        String baseUri = "http://example.com";
        Document document = builder.parse(xml, baseUri);
        
        assertEquals("http://example.com", document.baseUri());
        assertEquals("root", document.nodeName());
        assertEquals(1, document.childNodeSize());
        assertTrue(document.childNode(0) instanceof Element);
        
        Element root = (Element) document.childNode(0);
        assertEquals("child", root.childNode(0).nodeName());
        assertEquals("content", root.childNode(0).childNode(0).outerHtml());
    }

    @Test
    public void testInitialiseParse() {
        String xml = "<root><child>content</child></root>";
        Reader input = new StringReader(xml);
        String baseUri = "http://example.com";
        
        builder.initialiseParse(input, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertEquals(Document.OutputSettings.Syntax.xml, builder.getDocument().outputSettings().syntax());
        assertEquals("http://example.com", builder.baseUri());
        assertEquals("root", builder.currentElement().nodeName());
        assertTrue(builder.currentElement() instanceof Element);
        
        Element root = builder.currentElement();
        assertEquals("child", root.childNode(0).nodeName());
        assertEquals("content", root.childNode(0).childNode(0).outerHtml());
    }

    @Test
    public void testProcess() {
        StartTag startTag = new Token.StartTag("root", null);
        builder.process(startTag);
        
        assertEquals("root", builder.currentElement().nodeName());
        assertTrue(builder.currentElement() instanceof Element);
        
        Element root = builder.currentElement();
        assertEquals(startTag, root.startTag());
    }

    @Test
    public void testInsertNode() {
        TextNode textNode = new TextNode("content");
        builder.insertNode(textNode);
        
        assertEquals("root", builder.currentElement().nodeName());
        assertEquals(textNode, builder.currentElement().childNode(0));
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("child", null);
        builder.insert(startTag);
        
        assertEquals("child", builder.currentElement().nodeName());
        assertTrue(builder.currentElement() instanceof Element);
        
        Element child = builder.currentElement();
        assertEquals(startTag, child.startTag());
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("comment");
        builder.insert(commentToken);
        
        assertEquals("root", builder.currentElement().nodeName());
        assertEquals(commentToken.getData(), builder.currentElement().childNode(0).outerHtml());
    }

    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character("content");
        builder.insert(characterToken);
        
        assertEquals("root", builder.currentElement().nodeName());
        assertEquals(characterToken.getData(), builder.currentElement().childNode(0).outerHtml());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "");
        builder.insert(doctype);
        
        assertEquals("root", builder.currentElement().nodeName());
        assertTrue(builder.currentElement().childNode(0) instanceof DocumentType);
        
        DocumentType doctypeNode = (DocumentType) builder.currentElement().childNode(0);
        assertEquals(doctype.getName(), doctypeNode.getName());
        assertEquals(doctype.getPublicIdentifier(), doctypeNode.getPublicIdentifier());
        assertEquals(doctype.getSystemIdentifier(), doctypeNode.getSystemIdentifier());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("child");
        Element childElement = new Element(Tag.valueOf("child"), "");
        builder.stack.add(new Element(Tag.valueOf("root"), ""));
        builder.stack.add(childElement);
        builder.stack.add(new Element(Tag.valueOf("sibling"), ""));
        
        builder.popStackToClose(endTag);
        
        assertEquals(1, builder.stack.size());
        assertEquals("root", builder.currentElement().nodeName());
    }

    @Test
    public void testParseFragment() {
        String fragment = "<child>content</child>";
        String baseUri = "http://example.com";
        List<Node> nodes = builder.parseFragment(fragment, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
        
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        
        Element child = (Element) nodes.get(0);
        assertEquals("child", child.nodeName());
        assertEquals("content", child.childNode(0).outerHtml());
    }
}