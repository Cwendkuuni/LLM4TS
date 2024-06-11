package org.jsoup.parser;

import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        ParseSettings defaultSettings = treeBuilder.defaultSettings();
        assertNotNull(defaultSettings);
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        String input = "<html><head><title>Test</title></head><body><p>Test</p></body></html>";
        Reader reader = new StringReader(input);
        String baseUri = "http://example.com";
        Parser parser = new Parser(treeBuilder);
        treeBuilder.initialiseParse(reader, baseUri, parser);

        assertNotNull(treeBuilder.stack);
        assertNotNull(treeBuilder.doc);
        assertSame(treeBuilder.doc, treeBuilder.stack.get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, treeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        String input = "<html><head><title>Test</title></head><body><p>Test</p></body></html>";
        Reader reader = new StringReader(input);
        String baseUri = "http://example.com";
        Document doc = treeBuilder.parse(reader, baseUri);

        assertNotNull(doc);
        assertEquals("<html>\n <head>\n  <title>Test</title>\n </head>\n <body>\n  <p>Test</p>\n </body>\n</html>", doc.toString());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        String input = "<html><head><title>Test</title></head><body><p>Test</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = treeBuilder.parse(input, baseUri);

        assertNotNull(doc);
        assertEquals("<html>\n <head>\n  <title>Test</title>\n </head>\n <body>\n  <p>Test</p>\n </body>\n</html>", doc.toString());
    }

    @Test
    public void testProcessWithStartTagToken() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("div");
        startTag.attributes.put("class", "container");

        assertTrue(treeBuilder.process(startTag));

        Element element = treeBuilder.currentElement();
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals("container", element.attr("class"));
    }

    @Test
    public void testProcessWithEndTagToken() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.stack.add(new Element("div"));

        Token.EndTag endTag = new Token.EndTag("div");

        assertTrue(treeBuilder.process(endTag));

        assertEquals(0, treeBuilder.stack.size());
    }

    @Test
    public void testProcessWithCommentToken() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("Test comment");

        assertTrue(treeBuilder.process(commentToken));

        List<Node> childNodes = treeBuilder.currentElement().childNodes();
        Comment comment = (Comment) childNodes.get(0);
        assertNotNull(comment);
        assertEquals("Test comment", comment.getData());
    }

    @Test
    public void testProcessWithCharacterToken() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("Test character data");

        assertTrue(treeBuilder.process(characterToken));

        List<Node> childNodes = treeBuilder.currentElement().childNodes();
        TextNode textNode = (TextNode) childNodes.get(0);
        assertNotNull(textNode);
        assertEquals("Test character data", textNode.getWholeText());
    }

    @Test
    public void testProcessWithDoctypeToken() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("html");

        assertTrue(treeBuilder.process(doctypeToken));

        List<Node> childNodes = treeBuilder.currentElement().childNodes();
        DocumentType doctype = (DocumentType) childNodes.get(0);
        assertNotNull(doctype);
        assertEquals("html", doctype.getName());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.stack.add(new Element("div"));

        TextNode textNode = new TextNode("Test text");
        treeBuilder.insertNode(textNode);

        List<Node> childNodes = treeBuilder.currentElement().childNodes();
        assertNotNull(childNodes);
        assertEquals(1, childNodes.size());
        assertSame(textNode, childNodes.get(0));
    }

    @Test
    public void testInsertWithStartTag() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.stack.add(new Element("div"));

        Token.StartTag startTag = new Token.StartTag("span");
        startTag.attributes.put("class", "highlight");

        Element element = treeBuilder.insert(startTag);

        assertNotNull(element);
        assertEquals("span", element.tagName());
        assertEquals("highlight", element.attr("class"));
        assertNotSame(treeBuilder.currentElement(), element);
    }

    @Test
    public void testInsertWithCommentToken() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.stack.add(new Element("div"));

        Token.Comment commentToken = new Token.Comment("Test comment");

        treeBuilder.insert(commentToken);

        List<Node> childNodes = treeBuilder.currentElement().childNodes();
        Comment comment = (Comment) childNodes.get(0);
        assertNotNull(comment);
        assertEquals("Test comment", comment.getData());
    }

    @Test
    public void testInsertWithCharacterToken() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.stack.add(new Element("div"));

        Token.Character characterToken = new Token.Character("Test character data");

        treeBuilder.insert(characterToken);

        List<Node> childNodes = treeBuilder.currentElement().childNodes();
        TextNode textNode = (TextNode) childNodes.get(0);
        assertNotNull(textNode);
        assertEquals("Test character data", textNode.getWholeText());
    }

    @Test
    public void testInsertWithDoctypeToken() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.stack.add(new Element("div"));

        Token.Doctype doctypeToken = new Token.Doctype("html");

        treeBuilder.insert(doctypeToken);

        List<Node> childNodes = treeBuilder.currentElement().childNodes();
        DocumentType doctype = (DocumentType) childNodes.get(0);
        assertNotNull(doctype);
        assertEquals("html", doctype.getName());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.stack.add(new Element("div"));
        treeBuilder.stack.add(new Element("span"));

        Token.EndTag endTag = new Token.EndTag("div");

        treeBuilder.popStackToClose(endTag);

        assertEquals(1, treeBuilder.stack.size());
        assertEquals("span", treeBuilder.currentElement().tagName());
    }

    @Test
    public void testParseFragmentWithStringBaseUriAndParser() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        String inputFragment = "<div class=\"container\"><p>Test</p></div>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(treeBuilder);

        List<Node> nodes = treeBuilder.parseFragment(inputFragment, baseUri, parser);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("div", element.tagName());
        assertEquals("container", element.attr("class"));

        List<Node> childNodes = element.childNodes();
        assertNotNull(childNodes);
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Element);
        Element childElement = (Element) childNodes.get(0);
        assertEquals("p", childElement.tagName());
        assertEquals("Test", childElement.text());
    }

    @Test
    public void testParseFragmentWithStringElementBaseUriAndParser() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        String inputFragment = "<div class=\"container\"><p>Test</p></div>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        Parser parser = new Parser(treeBuilder);

        List<Node> nodes = treeBuilder.parseFragment(inputFragment, context, baseUri, parser);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("div", element.tagName());
        assertEquals("container", element.attr("class"));

        List<Node> childNodes = element.childNodes();
        assertNotNull(childNodes);
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Element);
        Element childElement = (Element) childNodes.get(0);
        assertEquals("p", childElement.tagName());
        assertEquals("Test", childElement.text());
    }
}