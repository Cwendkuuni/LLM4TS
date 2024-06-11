package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
        Assert.assertEquals(ParseSettings.preserveCase, parseSettings);
    }

    @Test
    public void testParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<xml><tag></tag></xml>";
        String baseUri = "";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<xml><tag></tag></xml>";
        String baseUri = "";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        Assert.assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));
        Assert.assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcess() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(Tag.createStart("tag"), Token.TokenType.StartTag);
        boolean result = xmlTreeBuilder.process(token);
        Assert.assertTrue(result);
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element(Tag.createStart("tag"), "");
        xmlTreeBuilder.insertNode(element);
        Assert.assertEquals(element, xmlTreeBuilder.currentElement().childNodes().get(0));
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag", "attribute");
        Element element = xmlTreeBuilder.insert(startTag);
        Assert.assertEquals(Tag.createStart("tag"), element.tag());
        Assert.assertEquals("", element.baseUri());
        Assert.assertEquals("attribute", element.attributes().toString());
        Assert.assertNotEquals("", element.outerHtml());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(comment);
        Assert.assertEquals("<!--This is a comment-->", xmlTreeBuilder.currentElement().outerHtml());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("This is a character");
        xmlTreeBuilder.insert(character);
        Assert.assertEquals("This is a character", xmlTreeBuilder.currentElement().outerHtml());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html");
        xmlTreeBuilder.insert(doctype);
        Assert.assertEquals("<!DOCTYPE html>", xmlTreeBuilder.currentElement().outerHtml());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("tag");
        Element element = new Element(Tag.createStart("tag"), "");
        xmlTreeBuilder.stack.add(element);
        xmlTreeBuilder.popStackToClose(endTag);
        Assert.assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<tag></tag>";
        String baseUri = "";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
    }
}