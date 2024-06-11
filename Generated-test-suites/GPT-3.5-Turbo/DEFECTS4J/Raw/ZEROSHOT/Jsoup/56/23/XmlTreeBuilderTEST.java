package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        Assert.assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<xml><tag>Test</tag></xml>";
        String baseUri = "https://www.example.com";
        Document doc = builder.parse(input, baseUri);
        Assert.assertEquals("<xml>\n <tag>\n  Test\n </tag>\n</xml>\n", doc.outerHtml());
        Assert.assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<tag>Test</tag>";
        String baseUri = "https://www.example.com";
        builder.initialiseParse(input, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Document doc = builder.getDocument();
        Assert.assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.StartTag("tag", "name", new Attributes());
        boolean result = builder.process(token);
        Assert.assertTrue(result);
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.EndTag("tag");
        boolean result = builder.process(token);
        Assert.assertTrue(result);
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.Comment("This is a comment");
        boolean result = builder.process(token);
        Assert.assertTrue(result);
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.Character("Test");
        boolean result = builder.process(token);
        Assert.assertTrue(result);
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.Doctype("html", "public", "system");
        boolean result = builder.process(token);
        Assert.assertTrue(result);
    }

    @Test(expected = RuntimeException.class)
    public void testProcessUnexpectedToken() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.EOF();
        builder.process(token);
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("tag"), "https://www.example.com", ParseSettings.preserveCase.normalizeAttributes(new Attributes()));
        builder.insertNode(element);
        Document doc = builder.getDocument();
        Assert.assertEquals("<tag></tag>", doc.outerHtml());
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag", "name", new Attributes());
        Element element = builder.insert(startTag);
        Document doc = builder.getDocument();
        Assert.assertEquals("<tag></tag>", doc.outerHtml());
        Assert.assertEquals("tag", element.nodeName());
    }

    @Test
    public void testInsertEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("tag");
        builder.insert(endTag);
        Document doc = builder.getDocument();
        Assert.assertEquals("</tag>", doc.outerHtml());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("This is a comment");
        builder.insert(comment);
        Document doc = builder.getDocument();
        Assert.assertEquals("<!--This is a comment-->", doc.outerHtml());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("Test");
        builder.insert(character);
        Document doc = builder.getDocument();
        Assert.assertEquals("Test", doc.text());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "public", "system");
        builder.insert(doctype);
        Document doc = builder.getDocument();
        Assert.assertEquals("<!DOCTYPE html PUBLIC \"public\" \"system\">", doc.outerHtml());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("tag"), "https://www.example.com", ParseSettings.preserveCase.normalizeAttributes(new Attributes()));
        builder.insertNode(element);
        Token.EndTag endTag = new Token.EndTag("tag");
        builder.popStackToClose(endTag);
        Document doc = builder.getDocument();
        Assert.assertEquals("<tag></tag>", doc.outerHtml());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<tag>Test</tag>";
        String baseUri = "https://www.example.com";
        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("Test", nodes.get(0).outerHtml());
    }
}