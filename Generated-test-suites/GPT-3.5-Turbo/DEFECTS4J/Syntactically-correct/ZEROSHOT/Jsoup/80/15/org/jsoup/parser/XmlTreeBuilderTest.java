package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @After
    public void tearDown() {
        xmlTreeBuilder = null;
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
        Assert.assertEquals(ParseSettings.preserveCase, parseSettings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        String input = "<root><child></child></root>";
        Reader reader = new StringReader(input);
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(reader, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertEquals("root", document.child(0).nodeName());
        Assert.assertEquals("child", document.child(0).child(0).nodeName());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<root><child></child></root>";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertEquals("root", document.child(0).nodeName());
        Assert.assertEquals("child", document.child(0).child(0).nodeName());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root><child></child></root>";
        Reader reader = new StringReader(input);
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);
        Assert.assertEquals(settings, xmlTreeBuilder.settings);
        Assert.assertEquals(baseUri, xmlTreeBuilder.baseUri);
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertEquals("root", xmlTreeBuilder.stack.get(0).nodeName());
        Assert.assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcessWithStartTagToken() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("root");
        startTag.attributes(new Attributes());
        boolean result = xmlTreeBuilder.process(startTag);
        Assert.assertTrue(result);
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertEquals("root", xmlTreeBuilder.currentElement().nodeName());
    }

    @Test
    public void testProcessWithEndTagToken() {
        xmlTreeBuilder.stack.add(new Element("root"));
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName("root");
        boolean result = xmlTreeBuilder.process(endTag);
        Assert.assertTrue(result);
        Assert.assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcessWithCommentToken() {
        Token.Comment comment = new Token.Comment();
        comment.data("This is a comment");
        boolean result = xmlTreeBuilder.process(comment);
        Assert.assertTrue(result);
        Assert.assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        Assert.assertEquals(NodeType.COMMENT, xmlTreeBuilder.currentElement().childNode(0).nodeType());
        Assert.assertEquals("This is a comment", ((Comment) xmlTreeBuilder.currentElement().childNode(0)).getData());
    }

    @Test
    public void testProcessWithCharacterToken() {
        Token.Character character = new Token.Character();
        character.data("This is a character");
        boolean result = xmlTreeBuilder.process(character);
        Assert.assertTrue(result);
        Assert.assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        Assert.assertEquals(NodeType.TEXT, xmlTreeBuilder.currentElement().childNode(0).nodeType());
        Assert.assertEquals("This is a character", ((TextNode) xmlTreeBuilder.currentElement().childNode(0)).getWholeText());
    }

    @Test
    public void testProcessWithDoctypeToken() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("html");
        doctype.publicIdentifier("public");
        doctype.systemIdentifier("system");
        doctype.pubSysKey("key");
        boolean result = xmlTreeBuilder.process(doctype);
        Assert.assertTrue(result);
        Assert.assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        Assert.assertEquals(NodeType.DOCTYPE, xmlTreeBuilder.currentElement().childNode(0).nodeType());
        DocumentType documentType = (DocumentType) xmlTreeBuilder.currentElement().childNode(0);
        Assert.assertEquals("html", documentType.getName());
        Assert.assertEquals("public", documentType.getPublicIdentifier());
        Assert.assertEquals("system", documentType.getSystemIdentifier());
        Assert.assertEquals("key", documentType.getPubSysKey());
    }

    @Test
    public void testInsertNode() {
        Element element = new Element("child");
        xmlTreeBuilder.stack.add(new Element("root"));
        xmlTreeBuilder.insertNode(element);
        Assert.assertEquals(element, xmlTreeBuilder.currentElement().childNode(0));
    }

    @Test
    public void testInsertWithStartTagToken() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("child");
        startTag.attributes(new Attributes());
        Element element = xmlTreeBuilder.insert(startTag);
        Assert.assertNotNull(element);
        Assert.assertEquals("child", element.nodeName());
        Assert.assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        Assert.assertEquals(element, xmlTreeBuilder.currentElement().childNode(0));
    }

    @Test
    public void testInsertWithCommentToken() {
        Token.Comment comment = new Token.Comment();
        comment.data("This is a comment");
        xmlTreeBuilder.insert(comment);
        Assert.assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        Assert.assertEquals(NodeType.COMMENT, xmlTreeBuilder.currentElement().childNode(0).nodeType());
        Assert.assertEquals("This is a comment", ((Comment) xmlTreeBuilder.currentElement().childNode(0)).getData());
    }

    @Test
    public void testInsertWithCharacterToken() {
        Token.Character character = new Token.Character();
        character.data("This is a character");
        xmlTreeBuilder.insert(character);
        Assert.assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        Assert.assertEquals(NodeType.TEXT, xmlTreeBuilder.currentElement().childNode(0).nodeType());
        Assert.assertEquals("This is a character", ((TextNode) xmlTreeBuilder.currentElement().childNode(0)).getWholeText());
    }

    @Test
    public void testInsertWithDoctypeToken() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("html");
        doctype.publicIdentifier("public");
        doctype.systemIdentifier("system");
        doctype.pubSysKey("key");
        xmlTreeBuilder.insert(doctype);
        Assert.assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        Assert.assertEquals(NodeType.DOCTYPE, xmlTreeBuilder.currentElement().childNode(0).nodeType());
        DocumentType documentType = (DocumentType) xmlTreeBuilder.currentElement().childNode(0);
        Assert.assertEquals("html", documentType.getName());
        Assert.assertEquals("public", documentType.getPublicIdentifier());
        Assert.assertEquals("system", documentType.getSystemIdentifier());
        Assert.assertEquals("key", documentType.getPubSysKey());
    }

    @Test
    public void testPopStackToCloseWhenEndElementFound() {
        Element element = new Element("child");
        xmlTreeBuilder.stack.add(element);
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName("child");
        xmlTreeBuilder.popStackToClose(endTag);
        Assert.assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testPopStackToCloseWhenEndElementNotFound() {
        Element element1 = new Element("child");
        Element element2 = new Element("child");
        xmlTreeBuilder.stack.add(element1);
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName("child");
        xmlTreeBuilder.popStackToClose(endTag);
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertEquals(element1, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<child></child>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("child", nodes.get(0).nodeName());
    }
}