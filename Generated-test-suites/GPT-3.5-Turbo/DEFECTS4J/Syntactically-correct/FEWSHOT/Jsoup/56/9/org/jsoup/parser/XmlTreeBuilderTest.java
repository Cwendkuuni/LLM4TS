
package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document document = builder.parse("<root><element>text</element></root>", "https://example.com");
        assertEquals("https://example.com", document.baseUri());
        assertEquals("<root>\n <element>\n  text\n </element>\n</root>\n", document.toString());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = builder.defaultSettings();
        builder.initialiseParse("<root><element>text</element></root>", "https://example.com", errors, settings);

        assertEquals("https://example.com", builder.doc.baseUri());
        assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
        assertTrue(builder.stack.contains(builder.doc));
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.push(builder.doc);
        Token.StartTag startTag = new Token.StartTag("element", new Attributes());
        builder.process(startTag);

        Elements elements = builder.doc.getElementsByTag("element");
        assertEquals(1, elements.size());
        assertEquals("element", elements.first().nodeName());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.push(builder.doc);
        Element element = new Element("element");
        builder.stack.push(element);
        builder.process(new Token.EndTag("element"));

        assertEquals(1, builder.stack.size());
        assertEquals(builder.doc, builder.stack.get(0));
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.push(builder.doc);
        builder.process(new Token.Comment("Comment"));

        List<Node> childNodes = builder.doc.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals("#comment", childNodes.get(0).nodeName());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.push(builder.doc);
        builder.process(new Token.Character("Text"));

        List<Node> childNodes = builder.doc.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals("#text", childNodes.get(0).nodeName());
        assertEquals("Text", childNodes.get(0).toString());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.push(builder.doc);
        builder.process(new Token.Doctype("html", "", "", ""));

        List<Node> childNodes = builder.doc.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals("!doctype html", childNodes.get(0).nodeName());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.push(builder.doc);
        Element element = new Element("element");
        builder.insertNode(element);

        Elements elements = builder.doc.getElementsByTag("element");
        assertEquals(1, elements.size());
        assertEquals("element", elements.first().nodeName());
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.push(builder.doc);
        Token.StartTag startTag = new Token.StartTag("element", new Attributes());
        builder.insert(startTag);

        Elements elements = builder.doc.getElementsByTag("element");
        assertEquals(1, elements.size());
        assertEquals("element", elements.first().nodeName());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.push(builder.doc);
        Token.Comment comment = new Token.Comment("Comment");
        builder.insert(comment);

        List<Node> childNodes = builder.doc.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals("#comment", childNodes.get(0).nodeName());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.push(builder.doc);
        Token.Character character = new Token.Character("Text");
        builder.insert(character);

        List<Node> childNodes = builder.doc.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals("#text", childNodes.get(0).nodeName());
        assertEquals("Text", childNodes.get(0).toString());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.push(builder.doc);
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");
        builder.insert(doctype);

        List<Node> childNodes = builder.doc.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals("!doctype html", childNodes.get(0).nodeName());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.push(builder.doc);

        Element element = new Element("element");
        builder.stack.push(element);

        builder.popStackToClose(new Token.EndTag("element"));
        assertTrue(builder.stack.isEmpty());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("<element>text</element>", "https://example.com", new ParseErrorList(), builder.defaultSettings());

        assertEquals(1, nodes.size());
        assertEquals("element", nodes.get(0).nodeName());
        assertEquals("text", nodes.get(0).childNode(0).toString());
    }
}
