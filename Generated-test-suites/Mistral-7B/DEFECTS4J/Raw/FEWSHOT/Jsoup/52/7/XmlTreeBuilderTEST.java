package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xmlString = "<root><child>Hello World</child></root>";
        Document doc = Jsoup.parse(xmlString, "", Parser.xmlParser());
        Node node = doc.select("child[0]").first();
        builder.initialiseParse(xmlString, "", new ParseErrorList());
        builder.runParser();
        Element rootElement = (Element) builder.doc.childNodes().get(0);
        Element childElement = (Element) builder.doc.childNodes().get(1);

        builder.insertNode(node);
        assertEquals(rootElement.childNodes().size(), 2);
        assertSame(rootElement.childNodes().get(1), childElement);
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xmlString = "<root><child>Hello World</child></root>";
        builder.initialiseParse(xmlString, "", new ParseErrorList());

        Token startTag = new Token.StartTag("root");
        builder.process(startTag);

        Element rootElement = (Element) builder.doc.childNodes().get(0);
        assertEquals(rootElement.tagName(), "root");
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xmlString = "<root><child><grandChild/></child></root>";
        builder.initialiseParse(xmlString, "", new ParseErrorList());

        Token startTag = new Token.StartTag("root");
        builder.process(startTag);
        Element rootElement = (Element) builder.doc.childNodes().get(0);

        Token endTag = new Token.EndTag("child");
        builder.process(endTag);

        assertEquals(rootElement.childNodes().size(), 0);
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xmlString = "<!-- This is a comment -->";
        builder.initialiseParse(xmlString, "", new ParseErrorList());

        Token commentToken = new Token.Comment("This is a comment");
        builder.process(commentToken);

        Comment comment = (Comment) builder.doc.childNodes().get(0);
        assertEquals(comment.data(), "This is a comment");
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xmlString = "<root>Hello World</root>";
        builder.initialiseParse(xmlString, "", new ParseErrorList());

        Token characterToken = new Token.Character('H');
        builder.process(characterToken);

        TextNode characterNode = (TextNode) builder.doc.childNodes().get(0);
        assertEquals(characterNode.text(), "Hello World");
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xmlString = "<!DOCTYPE root [ <!ATTLIST root foo CDATA #REQUIRED > ]>";
        builder.initialiseParse(xmlString, "", new ParseErrorList());

        Token doctypeToken = new Token.Doctype("root", null, null, null);
        builder.process(doctypeToken);

        DocumentType doctypeNode = (DocumentType) builder.doc.childNodes().get(0);
        assertEquals(doctypeNode.nodeName(), "root");
    }

    @Test
    public void testProcessUnknownTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xmlString = "<unknownTag/>";
        builder.initialiseParse(xmlString, "", new ParseErrorList());

        Token startTag = new Token.StartTag("unknownTag");
        builder.process(startTag);

        Element unknownTagElement = (Element) builder.doc.childNodes().get(0);
        assertTrue(unknownTagElement.isUnknown());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xmlString = "<root><child><grandChild/></grandChild></child></root>";
        builder.initialiseParse(xmlString, "", new ParseErrorList());

        Element rootElement = (Element) builder.doc.childNodes().get(0);
        Element childElement = (Element) builder.doc.childNodes().get(1);
        Element grandChildElement = (Element) builder.doc.childNodes().get(2);

        builder.stack.add(rootElement);
        builder.stack.add(childElement);
        builder.stack.add(grandChildElement);

        Token endTag = new Token.EndTag("grandChild");
        builder.popStackToClose(endTag);

        assertEquals(builder.stack.size(), 2);
        assertFalse(builder.stack.contains(grandChildElement));
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xmlFragment = "<root><child>Hello World</child></root>";
        List<Node> nodes = builder.parseFragment(xmlFragment, "", new ParseErrorList());

        Document doc = Jsoup.parse(xmlFragment, "", Parser.xmlParser());
        List<Node> docNodes = doc.childNodes();

        assertEquals(nodes.size(), docNodes.size());
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            Node docNode = docNodes.get(i);
            assertEquals(node, docNode);
        }
    }

    @Test
    public void testParseFragmentWithErrors() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xmlFragment = "<root><child><grandChild/><error/></child></root>";
        ParseErrorList errors = new ParseErrorList();
        List<Node> nodes = builder.parseFragment(xmlFragment, "", errors);

        Document doc = Jsoup.parse(xmlFragment, "", Parser.xmlParser());
        List<Node> docNodes = doc.childNodes();

        assertEquals(nodes.size(), docNodes.size() - errors.size());
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            Node docNode = docNodes.get(i);
            if (i < nodes.size() - errors.size()) {
                assertEquals(node, docNode);
            } else {
                assertNull(node);
            }
        }

        for (ParseError error : errors) {
            assertEquals(error.columnNumber(), 5);
            assertEquals(error.lineNumber(), 2);
            assertEquals(error.getMessage(), "Error message");
        }
    }
}