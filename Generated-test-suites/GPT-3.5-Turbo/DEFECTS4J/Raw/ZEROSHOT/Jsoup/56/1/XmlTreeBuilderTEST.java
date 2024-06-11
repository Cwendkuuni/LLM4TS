package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        ParseSettings parseSettings = treeBuilder.defaultSettings();
        assert parseSettings == ParseSettings.preserveCase;
    }

    @Test
    public void testParse() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        String input = "<html><head><title>Test</title></head><body><p>Test</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = treeBuilder.parse(input, baseUri);
        assert doc.outerHtml().equals("<html><head><title>Test</title></head><body><p>Test</p></body></html>");
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        String input = "<html><head><title>Test</title></head><body><p>Test</p></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        treeBuilder.initialiseParse(input, baseUri, errors, settings);
        assert treeBuilder.doc == treeBuilder.stack.get(0);
        assert treeBuilder.doc.outputSettings().syntax() == Document.OutputSettings.Syntax.xml;
    }

    @Test
    public void testProcess() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("test", new Attributes());
        treeBuilder.process(startTag);
        assert treeBuilder.stack.get(treeBuilder.stack.size() - 1).parent().nodeName().equals("test");
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Element parentElement = new Element("parent");
        treeBuilder.stack.add(parentElement);
        Node node = new TextNode("test", treeBuilder.baseUri);
        treeBuilder.insertNode(node);
        assert treeBuilder.currentElement().childNodes().size() == 1;
        assert treeBuilder.currentElement().childNodes().get(0) == node;
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Token.StartTag startTag = new Token.StartTag("div", attributes);
        treeBuilder.insert(startTag);
        assert treeBuilder.stack.get(treeBuilder.stack.size() - 1).nodeName().equals("div");
        assert treeBuilder.stack.get(treeBuilder.stack.size() - 1).attributes().get("class").equals("test");
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("Test comment");
        treeBuilder.insert(comment);
        assert treeBuilder.currentElement().childNodes().size() == 1;
        assert treeBuilder.currentElement().childNodes().get(0) instanceof Comment;
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("Test character");
        treeBuilder.insert(character);
        assert treeBuilder.currentElement().childNodes().size() == 1;
        assert treeBuilder.currentElement().childNodes().get(0) instanceof TextNode;
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html");
        treeBuilder.insert(doctype);
        assert treeBuilder.currentElement().childNodes().size() == 1;
        assert treeBuilder.currentElement().childNodes().get(0) instanceof DocumentType;
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Element element1 = new Element("div");
        Element element2 = new Element("p");
        Element element3 = new Element("div");
        treeBuilder.stack.add(element1);
        treeBuilder.stack.add(element2);
        treeBuilder.stack.add(element3);
        Token.EndTag endTag = new Token.EndTag("div");
        treeBuilder.popStackToClose(endTag);
        assert !treeBuilder.stack.contains(element3);
        assert treeBuilder.stack.contains(element2);
        assert treeBuilder.stack.contains(element1);
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        String inputFragment = "<p>Test</p>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> nodes = treeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        assert nodes.size() == 1;
        assert nodes.get(0) instanceof Element;
        assert nodes.get(0).nodeName().equals("p");
    }
}