package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        Assert.assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        Reader reader = new StringReader("<root></root>");
        String baseUri = "http://example.com";
        Document doc = xmlTreeBuilder.parse(reader, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("root", doc.nodeName());
        Assert.assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<root></root>";
        String baseUri = "http://example.com";
        Document doc = xmlTreeBuilder.parse(input, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("root", doc.nodeName());
        Assert.assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("<root></root>");
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertEquals("root", xmlTreeBuilder.stack.get(0).nodeName());
        Assert.assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("root");
        startTag.addAttribute("attr1", "value1");
        startTag.addAttribute("attr2", "value2");
        Element element = xmlTreeBuilder.insert(startTag);
        Assert.assertNotNull(element);
        Assert.assertEquals("root", element.nodeName());
        Assert.assertEquals("http://example.com", element.baseUri());
        Assert.assertEquals(2, element.attributes().size());
        Assert.assertEquals("value1", element.attributes().get("attr1"));
        Assert.assertEquals("value2", element.attributes().get("attr2"));
        Assert.assertFalse(element.tag().isSelfClosing());
        Assert.assertTrue(xmlTreeBuilder.stack.contains(element));
    }

    @Test
    public void testProcessStartTagWithSelfClosing() {
        Token.StartTag startTag = new Token.StartTag("selfClosing");
        startTag.setSelfClosing();
        Element element = xmlTreeBuilder.insert(startTag);
        Assert.assertNotNull(element);
        Assert.assertEquals("selfClosing", element.nodeName());
        Assert.assertEquals("http://example.com", element.baseUri());
        Assert.assertTrue(element.tag().isSelfClosing());
        Assert.assertFalse(xmlTreeBuilder.stack.contains(element));
    }

    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);
        List<Node> nodes = xmlTreeBuilder.currentElement().childNodes();
        Assert.assertEquals(1, nodes.size());
        Assert.assertTrue(nodes.get(0) instanceof Comment);
        Comment commentNode = (Comment) nodes.get(0);
        Assert.assertEquals("This is a comment", commentNode.getData());
    }

    @Test
    public void testProcessBogusComment() {
        Token.Comment commentToken = new Token.Comment("<!DOCTYPE html>");
        commentToken.setBogus(true);
        xmlTreeBuilder.insert(commentToken);
        List<Node> nodes = xmlTreeBuilder.currentElement().childNodes();
        Assert.assertEquals(1, nodes.size());
        Assert.assertTrue(nodes.get(0) instanceof XmlDeclaration);
        XmlDeclaration xmlDeclaration = (XmlDeclaration) nodes.get(0);
        Assert.assertEquals("!DOCTYPE html", xmlDeclaration.tagName());
        Assert.assertEquals("<!DOCTYPE html>", xmlDeclaration.outerHtml());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = new Token.Character("This is a character");
        xmlTreeBuilder.insert(characterToken);
        List<Node> nodes = xmlTreeBuilder.currentElement().childNodes();
        Assert.assertEquals(1, nodes.size());
        Assert.assertTrue(nodes.get(0) instanceof TextNode);
        TextNode textNode = (TextNode) nodes.get(0);
        Assert.assertEquals("This is a character", textNode.getWholeText());
    }

    @Test
    public void testProcessCDataCharacter() {
        Token.Character characterToken = new Token.Character("This is a CDATA character");
        characterToken.setCData(true);
        xmlTreeBuilder.insert(characterToken);
        List<Node> nodes = xmlTreeBuilder.currentElement().childNodes();
        Assert.assertEquals(1, nodes.size());
        Assert.assertTrue(nodes.get(0) instanceof CDataNode);
        CDataNode cDataNode = (CDataNode) nodes.get(0);
        Assert.assertEquals("This is a CDATA character", cDataNode.getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        doctype.setPublicIdentifier("-//W3C//DTD HTML 4.01//EN");
        doctype.setSystemIdentifier("http://www.w3.org/TR/html4/strict.dtd");
        xmlTreeBuilder.insert(doctype);
        List<Node> nodes = xmlTreeBuilder.currentElement().childNodes();
        Assert.assertEquals(1, nodes.size());
        Assert.assertTrue(nodes.get(0) instanceof DocumentType);
        DocumentType documentType = (DocumentType) nodes.get(0);
        Assert.assertEquals("html", documentType.nodeName());
        Assert.assertEquals("-//W3C//DTD HTML 4.01//EN", documentType.publicId());
        Assert.assertEquals("http://www.w3.org/TR/html4/strict.dtd", documentType.systemId());
    }

    @Test
    public void testPopStackToClose() {
        Element root = new Element(Tag.valueOf("root"), "http://example.com");
        Element child1 = new Element(Tag.valueOf("child1"), "http://example.com");
        Element child2 = new Element(Tag.valueOf("child2"), "http://example.com");
        xmlTreeBuilder.stack.add(root);
        xmlTreeBuilder.stack.add(child1);
        xmlTreeBuilder.stack.add(child2);

        Token.EndTag endTag = new Token.EndTag("child1");
        xmlTreeBuilder.popStackToClose(endTag);

        Assert.assertFalse(xmlTreeBuilder.stack.contains(child1));
        Assert.assertTrue(xmlTreeBuilder.stack.contains(child2));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<child1></child1><child2></child2>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        Assert.assertEquals(2, nodes.size());
        Assert.assertEquals("child1", nodes.get(0).nodeName());
        Assert.assertEquals("child2", nodes.get(1).nodeName());
    }
}