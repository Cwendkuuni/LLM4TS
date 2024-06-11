package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        assertTrue(settings.preserveCase);
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Reader input = new StringReader("test");
        String baseUri = "http://example.com";
        Parser parser = new Parser(builder);
        builder.initialiseParse(input, baseUri, parser);
        assertEquals(doc, builder.stack.get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
    }

    @Test
    public void testParseWithReader() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Reader input = new StringReader("test");
        String baseUri = "http://example.com";
        Document doc = builder.parse(input, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testParseWithString() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "test";
        String baseUri = "http://example.com";
        Document doc = builder.parse(input, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("div");
        Element element = builder.insert(startTag);
        assertEquals("div", element.tagName());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("div");
        builder.stack.add(new Element("div"));
        builder.popStackToClose(endTag);
        assertEquals(0, builder.stack.size());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("test");
        builder.insert(commentToken);
        Comment comment = (Comment) builder.currentElement().childNode(0);
        assertEquals("test", comment.getData());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("test");
        builder.insert(character);
        TextNode textNode = (TextNode) builder.currentElement().childNode(0);
        assertEquals("test", textNode.getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html");
        builder.insert(doctype);
        DocumentType documentType = (DocumentType) builder.currentElement().childNode(0);
        assertEquals("html", documentType.nodeName());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = new Element("div");
        builder.insertNode(element);
        assertEquals(element, builder.currentElement().childNode(0));
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("div");
        Element element = builder.insert(startTag);
        assertEquals("div", element.tagName());
        assertTrue(element.isBlock());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("test");
        builder.insert(commentToken);
        Comment comment = (Comment) builder.currentElement().childNode(0);
        assertEquals("test", comment.getData());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("test");
        builder.insert(character);
        TextNode textNode = (TextNode) builder.currentElement().childNode(0);
        assertEquals("test", textNode.getWholeText());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html");
        builder.insert(doctype);
        DocumentType documentType = (DocumentType) builder.currentElement().childNode(0);
        assertEquals("html", documentType.nodeName());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("div");
        builder.stack.add(new Element("div"));
        builder.popStackToClose(endTag);
        assertEquals(0, builder.stack.size());
    }

    @Test
    public void testParseFragmentWithInputFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<div>test</div>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(builder);
        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, parser);
        assertEquals(1, nodes.size());
        Element element = (Element) nodes.get(0);
        assertEquals("div", element.tagName());
        TextNode textNode = (TextNode) element.childNode(0);
        assertEquals("test", textNode.getWholeText());
    }

    @Test
    public void testParseFragmentWithContext() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<div>test</div>";
        Element context = new Element("body");
        String baseUri = "http://example.com";
        Parser parser = new Parser(builder);
        List<Node> nodes = builder.parseFragment(inputFragment, context, baseUri, parser);
        assertEquals(1, nodes.size());
        Element element = (Element) nodes.get(0);
        assertEquals("div", element.tagName());
        TextNode textNode = (TextNode) element.childNode(0);
        assertEquals("test", textNode.getWholeText());
    }
}