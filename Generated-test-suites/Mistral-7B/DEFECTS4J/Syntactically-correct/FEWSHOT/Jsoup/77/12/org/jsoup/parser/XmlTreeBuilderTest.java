package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testXmlTreeBuilder() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        assertNotNull(xmlTreeBuilder);
    }

    @Test
    public void testParse() {
        String xmlString = "<root><element attr=\"value\">Content</element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), null);
        assertNotNull(doc);
        assertEquals("root", doc.tagName());
        Element root = doc.rootElement();
        assertNotNull(root);
        assertEquals("element", root.child(0).tagName());
        assertEquals("value", root.child(0).attr("attr"));
        assertEquals("Content", root.child(0).text());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(TokenType.StartTag, "<root>", null, null, null, null, null);
        Element root = xmlTreeBuilder.insert(token.asStartTag());
        assertEquals("root", root.tagName());
        assertNotNull(root.attributes());
        assertEquals(0, root.attributes().size());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(TokenType.EndTag, "</root>", null, null, null, null, null);
        xmlTreeBuilder.popStackToClose(token.asEndTag());
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(TokenType.Comment, "<!-- comment -->", null, null, null, null, null);
        Comment comment = (Comment) xmlTreeBuilder.insert(token);
        assertEquals("comment", comment.data());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(TokenType.Character, 'a', null, null, null, null, null);
        TextNode textNode = (TextNode) xmlTreeBuilder.insert(token);
        assertEquals("a", textNode.text());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(TokenType.Doctype, "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED > ]>", null, null, null, null, null);
        DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.insert(token);
        assertEquals("root", doctypeNode.tagName());
        assertNotNull(doctypeNode.publicIdentifier());
        assertNull(doctypeNode.systemIdentifier());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<root><element attr=\"value\">Content</element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, null, null);
        assertNotNull(nodes);
        assertEquals(2, nodes.size());
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.tagName());
        Element element = (Element) nodes.get(1);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));
        assertEquals("Content", element.text());
    }

    @Test
    public void testUnexpectedTokenType() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(123, null, null, null, null, null, null);
        Validate.expected(RuntimeException.class);
        xmlTreeBuilder.process(token);
    }

    @Test
    public void testProcessEOF() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        // do nothing
    }
}