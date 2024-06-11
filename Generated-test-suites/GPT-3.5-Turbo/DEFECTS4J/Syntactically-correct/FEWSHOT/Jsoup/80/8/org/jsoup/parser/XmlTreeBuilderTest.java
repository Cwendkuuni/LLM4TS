package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class XmlTreeBuilderTest {
    private final String baseUri = "https://www.example.com";

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        ParseSettings settings = treeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseReaderString() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        String input = "<html><head><title>Test</title></head><body></body></html>";
        Document doc = treeBuilder.parse(new java.io.StringReader(input), baseUri);
        assertEquals("html", doc.nodeName());
        assertEquals("head", doc.head().nodeName());
        assertEquals("title", doc.title().nodeName());
        assertEquals("Test", doc.title().text());
        assertEquals("body", doc.body().nodeName());
    }

    @Test
    public void testParseStringString() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        String input = "<html><head><title>Test</title></head><body></body></html>";
        Document doc = treeBuilder.parse(input, baseUri);
        assertEquals("html", doc.nodeName());
        assertEquals("head", doc.head().nodeName());
        assertEquals("title", doc.title().nodeName());
        assertEquals("Test", doc.title().text());
        assertEquals("body", doc.body().nodeName());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        treeBuilder.initialiseParse(new java.io.StringReader(""), baseUri, errors, settings);
        assertTrue(treeBuilder.stack.contains(treeBuilder.doc));
        assertEquals(Document.OutputSettings.Syntax.xml, treeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcess() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token startTagToken = new Token.Tag("<div>", Tag.TokenType.StartTag);
        assertTrue(treeBuilder.process(startTagToken));
        assertEquals(1, treeBuilder.stack.size());
        assertEquals("div", treeBuilder.currentElement().nodeName());

        Token endTagToken = new Token.Tag("</div>", Tag.TokenType.EndTag);
        assertTrue(treeBuilder.process(endTagToken));
        assertEquals(0, treeBuilder.stack.size());

        Token commentToken = new Token.Comment("This is a comment");
        assertTrue(treeBuilder.process(commentToken));
        assertEquals(1, treeBuilder.doc.childNodes().size());
        assertTrue(treeBuilder.doc.childNode(0) instanceof Comment);

        Token characterToken = new Token.Character("This is a character");
        assertTrue(treeBuilder.process(characterToken));
        assertEquals(2, treeBuilder.doc.childNodes().size());
        assertTrue(treeBuilder.doc.childNode(1) instanceof TextNode);

        Token doctypeToken = new Token.Doctype("html");
        assertTrue(treeBuilder.process(doctypeToken));
        assertEquals(3, treeBuilder.doc.childNodes().size());
        assertTrue(treeBuilder.doc.childNode(2) instanceof DocumentType);

        Token eofToken = new Token.EOF();
        assertTrue(treeBuilder.process(eofToken));
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Element parent = new Element("parent");
        treeBuilder.stack.add(parent);

        Node child = new Element("child");
        treeBuilder.insertNode(child);

        assertEquals(1, parent.childNodeSize());
        assertEquals(child, parent.childNode(0));
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("<div>", ParseSettings.preserveCase);
        Element el = treeBuilder.insert(startTag);

        assertEquals("div", el.nodeName());
        assertTrue(el.isBlock());
        assertFalse(el.isInline());
        assertFalse(el.isData());
        assertFalse(el.isUnknown());

        startTag.selfClosing = true;
        treeBuilder.insert(startTag);
        assertTrue(el.tag().isSelfClosing());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("This is a comment");
        treeBuilder.insert(commentToken);

        assertEquals(1, treeBuilder.doc.childNodes().size());
        assertTrue(treeBuilder.doc.childNode(0) instanceof Comment);
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("This is a character");
        treeBuilder.insert(characterToken);

        assertEquals(1, treeBuilder.doc.childNodes().size());
        assertTrue(treeBuilder.doc.childNode(0) instanceof TextNode);
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("html");
        treeBuilder.insert(doctypeToken);

        assertEquals(1, treeBuilder.doc.childNodes().size());
        assertTrue(treeBuilder.doc.childNode(0) instanceof DocumentType);
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.stack.add(new Element("div"));
        treeBuilder.stack.add(new Element("p"));

        Token.EndTag endTag = new Token.EndTag("</div>");
        treeBuilder.popStackToClose(endTag);

        assertEquals(1, treeBuilder.stack.size());
        assertEquals("p", treeBuilder.stack.get(0).nodeName());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        String inputFragment = "<div><p>Test</p></div>";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> nodes = treeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element div = (Element) nodes.get(0);
        assertEquals("div", div.nodeName());
        assertEquals(1, div.childNodeSize());
        assertTrue(div.childNode(0) instanceof Element);
        Element p = div.child(0);
        assertEquals("p", p.nodeName());
        assertEquals(1, p.childNodeSize());
        assertTrue(p.childNode(0) instanceof TextNode);
        TextNode textNode = p.textNodes().get(0);
        assertEquals("Test", textNode.getWholeText());
    }

}