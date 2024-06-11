package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class XmlTreeBuilderTest {
    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        ParseSettings parseSettings = treeBuilder.defaultSettings();
        Assert.assertNotNull(parseSettings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        String input = "<root><node>content</node></root>";
        String baseUri = "http://www.example.com";
        Reader reader = new StringReader(input);
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Document document = treeBuilder.parse(reader, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<root><node>content</node></root>";
        String baseUri = "http://www.example.com";
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Document document = treeBuilder.parse(input, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root><node>content</node></root>";
        String baseUri = "http://www.example.com";
        Reader reader = new StringReader(input);
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.initialiseParse(reader, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Assert.assertEquals(ParseSettings.preserveCase, treeBuilder.settings);
    }

    @Test
    public void testProcessStartTag() {
        String input = "<root><node>content</node></root>";
        String baseUri = "http://www.example.com";
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.initialiseParse(new StringReader(input), baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
        boolean result = treeBuilder.process(new Token.StartTag("node"));
        Assert.assertTrue(result);
    }

    @Test
    public void testProcessEndTag() {
        String input = "<root><node>content</node></root>";
        String baseUri = "http://www.example.com";
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.initialiseParse(new StringReader(input), baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
        boolean result = treeBuilder.process(new Token.EndTag("node"));
        Assert.assertTrue(result);
    }

    @Test
    public void testProcessComment() {
        String input = "<root><node>content</node></root>";
        String baseUri = "http://www.example.com";
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.initialiseParse(new StringReader(input), baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
        boolean result = treeBuilder.process(new Token.Comment("comment"));
        Assert.assertTrue(result);
    }

    @Test
    public void testProcessCharacter() {
        String input = "<root><node>content</node></root>";
        String baseUri = "http://www.example.com";
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.initialiseParse(new StringReader(input), baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
        boolean result = treeBuilder.process(new Token.Character("data"));
        Assert.assertTrue(result);
    }

    @Test
    public void testProcessDoctype() {
        String input = "<root><node>content</node></root>";
        String baseUri = "http://www.example.com";
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.initialiseParse(new StringReader(input), baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
        boolean result = treeBuilder.process(new Token.Doctype("doctype"));
        Assert.assertTrue(result);
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Document document = Document.createShell("");
        treeBuilder.stack.add(document);
        treeBuilder.insertNode(new TextNode("text"));
        Assert.assertEquals(1, document.childNodes().size());
        Assert.assertEquals("text", document.childNodes().get(0).toString());
    }

    @Test
    public void testInsertStartTag() {
        String input = "<root><node>content</node></root>";
        String baseUri = "http://www.example.com";
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.initialiseParse(new StringReader(input), baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Token.StartTag startTag = new Token.StartTag("node");
        treeBuilder.insert(startTag);
        Assert.assertEquals(1, treeBuilder.stack.size());
        Assert.assertEquals("node", treeBuilder.stack.get(0).nodeName());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Document document = Document.createShell("");
        treeBuilder.stack.add(document);
        treeBuilder.insert(new Token.Comment("comment"));
        Assert.assertEquals(1, document.childNodes().size());
        Assert.assertEquals("comment", document.childNodes().get(0).toString());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Document document = Document.createShell("");
        treeBuilder.stack.add(document);
        treeBuilder.insert(new Token.Character("data"));
        Assert.assertEquals(1, document.childNodes().size());
        Assert.assertEquals("data", document.childNodes().get(0).toString());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Document document = Document.createShell("");
        treeBuilder.stack.add(document);
        treeBuilder.insert(new Token.Doctype("doctype"));
        Assert.assertEquals(1, document.childNodes().size());
        Assert.assertEquals("doctype", document.childNodes().get(0).toString());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Document document = Document.createShell("");
        Element element = new Element(Tag.valueOf("node"), "");
        treeBuilder.stack.add(document);
        treeBuilder.stack.add(element);
        treeBuilder.popStackToClose(new Token.EndTag("node"));
        Assert.assertEquals(1, treeBuilder.stack.size());
        Assert.assertEquals(document, treeBuilder.stack.get(0));
    }

    @Test
    public void testParseFragment() {
        String input = "<node>content</node>";
        String baseUri = "http://www.example.com";
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        List<Node> nodeList = treeBuilder.parseFragment(input, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Assert.assertEquals(1, nodeList.size());
        Assert.assertEquals("node", ((Element) nodeList.get(0)).nodeName());
    }
}