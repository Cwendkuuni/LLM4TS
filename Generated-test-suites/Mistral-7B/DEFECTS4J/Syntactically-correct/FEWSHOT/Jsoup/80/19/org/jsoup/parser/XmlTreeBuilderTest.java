package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        ParseSettings settings = new ParseSettings();
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlInput = "<root><element attr=\"value\">Content</element></root>";

        Document document = xmlTreeBuilder.parse(new StringReader(xmlInput), "http://example.com");
        assertEquals("root", document.tagName());
        Element root = document.child(0);
        assertEquals("element", root.child(0).tagName());
        assertEquals("value", root.child(0).attribute(0).value());
        assertEquals("Content", root.child(0).child(0).text());

        // Test empty element
        String xmlInputEmpty = "<root></root>";
        Document documentEmpty = xmlTreeBuilder.parse(new StringReader(xmlInputEmpty), "http://example.com");
        Element rootEmpty = documentEmpty.child(0);
        assertNull(rootEmpty.child(0));
    }

    @Test
    public void testParseFragment() {
        ParseSettings settings = new ParseSettings();
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlInputFragment = "<element attr=\"value\">Content</element>";

        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlInputFragment, "http://example.com", ParseErrorList.noTracking(), settings);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertEquals("element", node.tagName());
        assertEquals("value", node.attribute(0).value());
        assertEquals("Content", node.child(0).text());
    }

    @Test
    public void testProcessStartTag() {
        ParseSettings settings = new ParseSettings();
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlInput = "<root><element attr=\"value\">Content</element></root>";

        StringReader input = new StringReader(xmlInput);
        xmlTreeBuilder.initialiseParse(input, "http://example.com", ParseErrorList.noTracking(), settings);

        Token token;

        token = xmlTreeBuilder.getNextToken();
        assertEquals(Token.StartTag.class, token.getClass());
        assertEquals("root", token.asStartTag().name());
        xmlTreeBuilder.process(token);

        assertNotNull(xmlTreeBuilder.doc);
        assertEquals("root", xmlTreeBuilder.doc.tagName());
        Element root = xmlTreeBuilder.doc.child(0);

        token = xmlTreeBuilder.getNextToken();
        assertEquals(Token.StartTag.class, token.getClass());
        assertEquals("element", token.asStartTag().name());
        Element element = xmlTreeBuilder.insert(token.asStartTag());
        assertEquals("element", element.tagName());
        assertEquals("http://example.com", element.baseUri());
        assertEquals("value", element.attribute(0).value());

        token = xmlTreeBuilder.getNextToken();
        assertEquals(Token.Character.class, token.getClass());
        xmlTreeBuilder.insert(token.asCharacter());
        assertEquals("Content", root.child(0).child(0).text());

        token = xmlTreeBuilder.getNextToken();
        assertEquals(Token.EndTag.class, token.getClass());
        xmlTreeBuilder.popStackToClose(token.asEndTag());
        assertNull(root.child(0));
    }

    @Test
    public void testProcessComment() {
        ParseSettings settings = new ParseSettings();
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlInput = "<!-- This is a comment --><root><element attr=\"value\">Content</element></root>";

        StringReader input = new StringReader(xmlInput);
        xmlTreeBuilder.initialiseParse(input, "http://example.com", ParseErrorList.noTracking(), settings);

        Token token = xmlTreeBuilder.getNextToken();
        assertEquals(Token.Comment.class, token.getClass());
        xmlTreeBuilder.insert(token.asComment());
        assertNull(xmlTreeBuilder.doc.child(0));

        token = xmlTreeBuilder.getNextToken();
        assertEquals(Token.StartTag.class, token.getClass());
        xmlTreeBuilder.process(token);
        Element root = xmlTreeBuilder.doc.child(0);

        token = xmlTreeBuilder.getNextToken();
        assertEquals(Token.StartTag.class, token.getClass());
        xmlTreeBuilder.process(token);
        Element element = xmlTreeBuilder.insert(token.asStartTag());
        assertEquals("element", element.tagName());
        assertEquals("http://example.com", element.baseUri());
        assertEquals("value", element.attribute(0).value());

        token = xmlTreeBuilder.getNextToken();
        assertEquals(Token.Character.class, token.getClass());
        xmlTreeBuilder.insert(token.asCharacter());
        assertEquals("Content", root.child(0).child(0).text());

        token = xmlTreeBuilder.getNextToken();
        assertEquals(Token.EndTag.class, token.getClass());
        xmlTreeBuilder.popStackToClose(token.asEndTag());
        assertNull(root.child(0));
    }
}