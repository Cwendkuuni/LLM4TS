package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {
    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings defaultSettings = xmlTreeBuilder.defaultSettings();

        assertEquals(ParseSettings.preserveCase, defaultSettings);
    }

    @Test
    public void testParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse("<root></root>", "");

        assertEquals("root", document.child(0).tagName());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse("<root></root>", "", errors, settings);

        Element docElement = xmlTreeBuilder.doc;
        Element stackElement = xmlTreeBuilder.stack.get(0);
        Document.OutputSettings.Syntax syntax = xmlTreeBuilder.doc.outputSettings().syntax();

        assertEquals("root", docElement.child(0).tagName());
        assertEquals("root", stackElement.child(0).tagName());
        assertEquals(Document.OutputSettings.Syntax.xml, syntax);
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("div");
        startTag.attributes.put("id", "testId");

        assertTrue(xmlTreeBuilder.process(startTag));
        Element stackElement = xmlTreeBuilder.stack.get(0);

        assertEquals("div", stackElement.child(0).tagName());
        assertEquals("testId", stackElement.child(0).attr("id"));
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = Jsoup.parse("<root><div></div></root>");
        xmlTreeBuilder.stack.add(document.getElementById("div"));
        Token.EndTag endTag = new Token.EndTag("div");

        assertTrue(xmlTreeBuilder.process(endTag));
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("comment");

        assertTrue(xmlTreeBuilder.process(comment));
        Comment result = (Comment) xmlTreeBuilder.doc.childNode(0);

        assertEquals("comment", result.getData());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("text");

        assertTrue(xmlTreeBuilder.process(character));
        TextNode result = (TextNode) xmlTreeBuilder.doc.childNode(0);

        assertEquals("text", result.getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");

        assertTrue(xmlTreeBuilder.process(doctype));
        DocumentType result = (DocumentType) xmlTreeBuilder.doc.childNode(0);

        assertEquals("html", result.getName());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element("div");

        xmlTreeBuilder.insertNode(element);
        Element result = xmlTreeBuilder.currentElement().child(0);

        assertEquals("div", result.tagName());
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("div");
        startTag.attributes.put("id", "testId");

        Element result = xmlTreeBuilder.insert(startTag);

        assertEquals("div", result.tagName());
        assertEquals("testId", result.attr("id"));
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("comment");

        xmlTreeBuilder.insert(comment);
        Comment result = (Comment) xmlTreeBuilder.doc.childNode(0);

        assertEquals("comment", result.getData());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("text");

        xmlTreeBuilder.insert(character);
        TextNode result = (TextNode) xmlTreeBuilder.doc.childNode(0);

        assertEquals("text", result.getWholeText());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");

        xmlTreeBuilder.insert(doctype);
        DocumentType result = (DocumentType) xmlTreeBuilder.doc.childNode(0);

        assertEquals("html", result.getName());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = Jsoup.parse("<root><div>test</div></root>");
        xmlTreeBuilder.stack.add(document.getElementById("div"));
        Token.EndTag endTag = new Token.EndTag("div");

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> nodes = xmlTreeBuilder.parseFragment("<div>test</div>", "", errors, settings);

        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("div", nodes.get(0).nodeName());
    }

    @Test
    public void testParseFragmentWithError() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> nodes = xmlTreeBuilder.parseFragment("<div>test", "", errors, settings);

        assertEquals(0, nodes.size());
        assertFalse(errors.isEmpty());
    }
}