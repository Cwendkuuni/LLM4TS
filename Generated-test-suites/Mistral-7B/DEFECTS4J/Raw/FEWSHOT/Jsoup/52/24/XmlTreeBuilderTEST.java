package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.ArrayList;

import org.jsoup.nodes.*;
import org.jsoup.helper.Validate;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token startTag = new Token.StartTag("root", null, new HashMap<>());
        builder.insert(startTag);
        Element root = builder.currentElement();
        assertEquals("root", root.nodeName());
        assertEquals(null, root.baseUri());
        assertTrue(root.attributes().isEmpty());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root", null, null);
        builder.stack.add(root);
        Token endTag = new Token.EndTag("root");
        builder.popStackToClose(endTag);
        assertTrue(builder.stack.isEmpty());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token commentToken = new Token.Comment("This is a comment");
        builder.insert(commentToken);
        Comment comment = (Comment) builder.currentNode();
        assertEquals("This is a comment", comment.text());
        assertEquals(null, comment.baseUri());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token characterToken = new Token.Character('A');
        builder.insert(characterToken);
        TextNode textNode = (TextNode) builder.currentNode();
        assertEquals("A", textNode.text());
        assertEquals(null, textNode.baseUri());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token doctypeToken = new Token.Doctype("html", "public -//W3C//DTD HTML 4.01//EN", "DTD_HTML_4_01.dtd", null);
        builder.insert(doctypeToken);
        DocumentType documentTypeNode = (DocumentType) builder.currentNode();
        assertEquals("html", documentTypeNode.nodeName());
        assertEquals("public -//W3C//DTD HTML 4.01//EN", documentTypeNode.publicId());
        assertEquals("DTD_HTML_4_01.dtd", documentTypeNode.systemId());
        assertEquals(null, documentTypeNode.baseUri());
    }

    @Test
    public void testPopStackToCloseUnknownTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root", null, null);
        builder.stack.add(root);
        Element unknown = new Element("unknown", null, null);
        builder.stack.add(unknown);
        Token endTag = new Token.EndTag("unknown");
        builder.popStackToClose(endTag);
        assertEquals(1, builder.stack.size());
        assertEquals(root, builder.stack.get(0));
    }

    @Test
    public void testPopStackToCloseKnownTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root", null, null);
        builder.stack.add(root);
        Element known = new Element("known", null, null);
        builder.stack.add(known);
        Token endTag = new Token.EndTag("known");
        builder.popStackToClose(endTag);
        assertTrue(builder.stack.isEmpty());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xmlFragment = "<root><child>Hello</child></root>";
        List<Node> expectedNodes = new ArrayList<>();
        expectedNodes.add(new Element("root", null, null));
        expectedNodes.add(new Element("child", null, null));
        expectedNodes.add(new TextNode("Hello", null));
        List<Node> nodes = builder.parseFragment(xmlFragment, null, null);
        assertEquals(expectedNodes.size(), nodes.size());
        for (int i = 0; i < expectedNodes.size(); i++) {
            assertEquals(expectedNodes.get(i).nodeName(), nodes.get(i).nodeName());
            if (expectedNodes.get(i) instanceof TextNode) {
                assertEquals(((TextNode) expectedNodes.get(i)).text(), ((TextNode) nodes.get(i)).text());
            } else {
                assertEquals(expectedNodes.get(i).attributes().size(), nodes.get(i).attributes().size());
            }
        }
    }

    @Test
    public void testParseFragmentWithEmptyInput() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("", null, null);
        assertTrue(nodes.isEmpty());
    }

    @Test
    public void testValidateUnexpectedTokenType() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token invalidToken = new Token(123);
        try {
            builder.process(invalidToken);
            Validate.fail("Should have thrown an exception");
        } catch (AssertionError e) {
            // Expected
        }
    }
}