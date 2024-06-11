package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xml = "<root><element1>Hello</element1><element2 attr1=\"value1\" attr2=\"value2\"/></root>";
        XmlTreeBuilder xmlBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlBuilder.parse(new StringReader(xml), null);
        assertNotNull(xmlDoc);
        assertEquals("<root><element1>Hello</element1><element2 attr1=\"value1\" attr2=\"value2\"/></root>", xmlDoc.html());
    }

    @Test
    public void testParseWithEmptyInput() {
        XmlTreeBuilder xmlBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlBuilder.parse(new StringReader(""), null);
        assertNull(xmlDoc);
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlBuilder = new XmlTreeBuilder();
        Token startToken = new Token(Token.StartTag.class, "<root>", null);
        Element rootElement = xmlBuilder.insert(startToken);
        assertNotNull(rootElement);
        assertEquals("root", rootElement.nodeName());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlBuilder = new XmlTreeBuilder();
        Element rootElement = new Element("root");
        stack.push(rootElement);
        Token endToken = new Token(Token.EndTag.class, "</root>");
        xmlBuilder.popStackToClose(endToken);
        assertEquals(0, stack.size());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder xmlBuilder = new XmlTreeBuilder();
        Token commentToken = new Token(Token.Comment.class, "-- This is a comment --");
        xmlBuilder.insert(commentToken);
        Element commentNode = (Element) stack.peek();
        assertEquals("-- This is a comment --", commentNode.text());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlBuilder = new XmlTreeBuilder();
        Token characterToken = new Token(Token.Character.class, "Hello World");
        xmlBuilder.insert(characterToken);
        TextNode textNode = (TextNode) stack.peek();
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder xmlBuilder = new XmlTreeBuilder();
        Token doctypeToken = new Token(Token.Doctype.class, "<!DOCTYPE html SYSTEM \"about:local\"]");
        xmlBuilder.insert(doctypeToken);
        DocumentType docTypeNode = (DocumentType) stack.peek();
        assertEquals("html", docTypeNode.nodeName());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlBuilder = new XmlTreeBuilder();
        String xmlFragment = "<element1>Hello</element1>";
        List<Node> fragmentNodes = xmlBuilder.parseFragment(xmlFragment, null, null, ParseSettings.preserveCase);
        assertNotNull(fragmentNodes);
        assertEquals(1, fragmentNodes.size());
        Element fragmentElement = (Element) fragmentNodes.get(0);
        assertEquals("element1", fragmentElement.nodeName());
    }
}