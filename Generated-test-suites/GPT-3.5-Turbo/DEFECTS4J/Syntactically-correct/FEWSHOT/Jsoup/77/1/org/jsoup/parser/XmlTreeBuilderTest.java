package org.jsoup.parser;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings.Syntax;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;
    private Document document;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
        document = new Document("");
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings defaultSettings = xmlTreeBuilder.defaultSettings();
        Assert.assertEquals(ParseSettings.preserveCase, defaultSettings);
    }

    @Test
    public void testParseReaderAndBaseUri() {
        StringReader input = new StringReader("");
        String baseUri = "";
        Document doc = xmlTreeBuilder.parse(input, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseStringAndBaseUri() {
        String input = "";
        String baseUri = "";
        Document doc = xmlTreeBuilder.parse(input, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testInitialiseParse() {
        StringReader input = new StringReader("");
        String baseUri = "";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        Assert.assertEquals(document, xmlTreeBuilder.stack.get(0));
        Assert.assertEquals(Syntax.xml, document.outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag");
        startTag.attributes.put("attr", "value");
        Element el = xmlTreeBuilder.insert(startTag);
        Assert.assertEquals("tag", el.tagName());
        Assert.assertEquals("value", el.attr("attr"));
        Assert.assertFalse(el.isKnownTag());
    }

    @Test
    public void testProcessStartTagSelfClosingKnownTag() {
        Token.StartTag startTag = new Token.StartTag("tag");
        startTag.attributes.put("attr", "value");
        startTag.selfClosing = true;
        Element el = xmlTreeBuilder.insert(startTag);
        Assert.assertEquals("tag", el.tagName());
        Assert.assertEquals("value", el.attr("attr"));
        Assert.assertTrue(el.isKnownTag());
        Assert.assertTrue(el.tag().isSelfClosing());
    }

    @Test
    public void testProcessStartTagSelfClosingUnknownTag() {
        Token.StartTag startTag = new Token.StartTag("unknownTag");
        startTag.attributes.put("attr", "value");
        startTag.selfClosing = true;
        Element el = xmlTreeBuilder.insert(startTag);
        Assert.assertEquals("unknownTag", el.tagName());
        Assert.assertEquals("value", el.attr("attr"));
        Assert.assertFalse(el.isKnownTag());
        Assert.assertTrue(el.tag().isSelfClosing());
    }

    @Test
    public void testInsertCommentToken() {
        Token.Comment commentToken = new Token.Comment("comment");
        xmlTreeBuilder.insert(commentToken);
        Assert.assertEquals(1, document.childNodes().size());
        Node node = document.childNodes().get(0);
        Assert.assertTrue(node instanceof Comment);
        Comment comment = (Comment) node;
        Assert.assertEquals("comment", comment.getData());
    }

    @Test
    public void testInsertBogusCommentToken() {
        Token.Comment commentToken = new Token.Comment("<!--?xml version=\"1.0\" encoding=\"utf-8\"?-->");
        xmlTreeBuilder.insert(commentToken);
        Assert.assertEquals(1, document.childNodes().size());
        Node node = document.childNodes().get(0);
        Assert.assertFalse(node instanceof Comment);
        Assert.assertTrue(node instanceof XmlDeclaration);
        XmlDeclaration xmlDeclaration = (XmlDeclaration) node;
        Assert.assertEquals("?xml", xmlDeclaration.tagName());
    }

    @Test
    public void testInsertCharacterToken() {
        Token.Character characterToken = new Token.Character("data");
        xmlTreeBuilder.insert(characterToken);
        Assert.assertEquals(1, document.childNodes().size());
        Node node = document.childNodes().get(0);
        Assert.assertTrue(node instanceof TextNode);
        TextNode textNode = (TextNode) node;
        Assert.assertEquals("data", textNode.getWholeText());
    }

    @Test
    public void testInsertCDataToken() {
        Token.Character characterToken = new Token.Character("data");
        characterToken.isCData = true;
        xmlTreeBuilder.insert(characterToken);
        Assert.assertEquals(1, document.childNodes().size());
        Node node = document.childNodes().get(0);
        Assert.assertTrue(node instanceof CDataNode);
        CDataNode cdataNode = (CDataNode) node;
        Assert.assertEquals("data", cdataNode.getWholeText());
    }

    @Test
    public void testInsertDoctypeToken() {
        Token.Doctype doctypeToken = new Token.Doctype("doctype");
        doctypeToken.pubSysKey = "pubSysKey";
        doctypeToken.publicIdentifier = "publicIdentifier";
        doctypeToken.systemIdentifier = "systemIdentifier";
        xmlTreeBuilder.insert(doctypeToken);
        Assert.assertEquals(1, document.childNodes().size());
        Node node = document.childNodes().get(0);
        Assert.assertTrue(node instanceof DocumentType);
        DocumentType documentType = (DocumentType) node;
        Assert.assertEquals("doctype", documentType.tagName());
        Assert.assertEquals("pubSysKey", documentType.getPubSysKey());
        Assert.assertEquals("publicIdentifier", documentType.getPublicIdentifier());
        Assert.assertEquals("systemIdentifier", documentType.getSystemIdentifier());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("tag");
        Element element = new Element("tag");
        xmlTreeBuilder.stack.add(element);
        xmlTreeBuilder.popStackToClose(endTag);
        Assert.assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testPopStackToCloseNotFound() {
        Token.EndTag endTag = new Token.EndTag("unknownTag");
        Element element = new Element("tag");
        xmlTreeBuilder.stack.add(element);
        xmlTreeBuilder.popStackToClose(endTag);
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<tag>data</tag>";
        String baseUri = "";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        Assert.assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        Assert.assertTrue(node instanceof Element);
        Element element = (Element) node;
        Assert.assertEquals("tag", element.tagName());
        Assert.assertEquals("data", element.text());
    }
}