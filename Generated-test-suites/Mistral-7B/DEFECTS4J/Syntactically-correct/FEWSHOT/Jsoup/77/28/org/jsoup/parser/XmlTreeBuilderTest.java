package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.jsoup.nodes.Xml.*;

import org.junit.Before;
import org.junit.Test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder builder;
    private ParseSettings settings;

    @Before
    public void setup() {
        builder = new XmlTreeBuilder();
        settings = builder.defaultSettings();
    }

    @Test
    public void testParse() {
        Document doc = builder.parse(new StringReader("<root><element attr='value' /><element><child></child></element></root>"), "http://example.com");
        assertNotNull(doc);
        assertEquals("root", doc.tagName());
        assertEquals("http://example.com", doc.baseUri());

        Element root = doc.getElementById("root");
        assertNotNull(root);
        assertEquals("root", root.tagName());

        List<Node> children = root.children();
        assertEquals(2, children.size());

        Element element1 = (Element) children.get(0);
        assertNotNull(element1);
        assertEquals("element", element1.tagName());
        assertEquals("value", element1.attribute("attr"));

        Element element2 = (Element) children.get(1);
        assertNotNull(element2);
        assertEquals("element", element2.tagName());
        assertEquals(1, element2.childNodeSize());

        TextNode child = (TextNode) element2.childNode(0);
        assertNotNull(child);
        assertEquals("", child.text());
    }

    @Test
    public void testParseFragment() {
        List<Node> nodes = builder.parseFragment("<root><element attr='value' /><element><child></child></element></root>", "http://example.com", ParseErrorList.noTracking(), settings);
        Document doc = new Document();
        doc.appendChild(nodes.get(0));

        Element root = (Element) nodes.get(0);
        assertEquals("root", root.tagName());
        assertEquals("http://example.com", root.baseUri());

        List<Node> children = root.children();
        assertEquals(2, children.size());

        Element element1 = (Element) children.get(0);
        assertEquals("element", element1.tagName());
        assertEquals("value", element1.attribute("attr"));

        Element element2 = (Element) children.get(1);
        assertEquals("element", element2.tagName());
        assertEquals(1, element2.childNodeSize());

        TextNode child = (TextNode) element2.childNode(0);
        assertEquals("", child.text());
    }

    @Test
    public void testProcessStartTag() {
        Token startTag = new Token(Token.StartTag.class, "<element attr='value'>");
        Element element = builder.insert(startTag);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute("attr"));
    }

    @Test
    public void testProcessEndTag() {
        Element element = new Element("element");
        builder.insertNode(element);
        builder.stack.add(element);

        Token endTag = new Token(Token.EndTag.class, "</element>");
        builder.popStackToClose(endTag);

        assertEquals(0, builder.stack.size());
        assertEquals(null, builder.doc.child(0));
    }

    @Test
    public void testProcessComment() {
        Token commentToken = new Token(Token.Comment.class, "-- This is a comment -->");
        builder.insert(commentToken);

        Comment comment = (Comment) builder.doc.childNodes().get(0);
        assertEquals("This is a comment", comment.data());
    }

    @Test
    public void testProcessCharacter() {
        Token characterToken = new Token(Token.Character.class, "A", false);
        TextNode textNode = (TextNode) builder.insert(characterToken);
        assertEquals("A", textNode.text());
    }

    @Test
    public void testProcessCharacterCData() {
        Token characterToken = new Token(Token.Character.class, "<![CDATA[A]]>", false);
        CDataNode cDataNode = (CDataNode) builder.insert(characterToken);
        assertEquals("A", cDataNode.getData());
    }

    @Test
    public void testProcessDoctype() {
        Token doctypeToken = new Token(Token.Doctype.class, "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED > ]>");
        DocumentType doctypeNode = (DocumentType) builder.insert(doctypeToken);
        assertEquals("root", doctypeNode.tagName());
    }
}