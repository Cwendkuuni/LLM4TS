package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringReader;
import java.util.List;

import org.junit.Test;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public class XmlTreeBuilderTest {

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token(Token.StartTag.class, null, null, null, "tag", null, null, null);
        Element expected = new Element("tag");
        builder.initialiseParse(new StringReader(""), "", new Parser(builder));
        builder.stack.clear();
        builder.process(token);
        Element actual = builder.stack.get(builder.stack.size() - 1);
        assertEquals(expected, actual);
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token(Token.EndTag.class, null, null, null, "tag", null, null, null);
        Element parent = new Element("parent");
        Element child = new Element("tag");
        parent.appendChild(child);
        builder.initialiseParse(new StringReader(""), "", new Parser(builder));
        builder.stack.clear();
        builder.stack.add(parent);
        builder.process(token);
        assertEquals(1, builder.stack.size());
        assertNull(builder.stack.get(0).getChildNodes().get(0));
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token(Token.Comment.class, null, null, null, null, null, null, "comment");
        Comment expected = new Comment("comment");
        builder.initialiseParse(new StringReader(""), "", new Parser(builder));
        builder.stack.clear();
        builder.process(token);
        Comment actual = (Comment) builder.doc.childNodes().get(0);
        assertEquals(expected, actual);
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token(Token.Character.class, null, null, null, null, null, null, "character");
        TextNode expected = new TextNode("character");
        builder.initialiseParse(new StringReader(""), "", new Parser(builder));
        builder.stack.clear();
        builder.process(token);
        TextNode actual = (TextNode) builder.doc.childNodes().get(0);
        assertEquals(expected, actual);
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token(Token.Doctype.class, null, null, null, null, null, null, null, null, "doctype");
        DocumentType expected = new DocumentType("doctype", null, null, null);
        builder.initialiseParse(new StringReader(""), "", new Parser(builder));
        builder.stack.clear();
        builder.process(token);
        DocumentType actual = (DocumentType) builder.doc.childNodes().get(0);
        assertEquals(expected, actual);
    }

    @Test
    public void testProcessEof() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse(new StringReader(""), "", new Parser(builder));
        builder.stack.clear();
        assertTrue(builder.process(new Token(Token.EOF.class, null, null, null, null, null, null, null)));
        assertEquals(0, builder.stack.size());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = new Element("element");
        builder.insertNode(element);
        Element actual = builder.stack.get(builder.stack.size() - 1);
        assertEquals(element, actual);
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Comment comment = new Comment("comment");
        builder.insert(new Token.Comment(null, null, null, null, null, null, null, "comment"));
        Node actual = builder.stack.get(builder.stack.size() - 1);
        assertEquals(comment, actual);
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String character = "character";
        builder.insert(new Token.Character(null, null, null, null, null, null, null, character));
        Node actual = builder.stack.get(builder.stack.size() - 1);
        assertTrue(actual instanceof TextNode);
        TextNode textNode = (TextNode) actual;
        assertEquals(character, textNode.text());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String name = "doctype";
        DocumentType doctype = new DocumentType(name, null, null, null);
        builder.insert(new Token.Doctype(null, null, null, null, null, null, null, null, null, name));
        Node actual = builder.stack.get(builder.stack.size() - 1);
        assertEquals(doctype, actual);
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element parent = new Element("parent");
        Element child1 = new Element("tag1");
        Element child2 = new Element("tag2");
        parent.appendChild(child1);
        parent.appendChild(child2);
        builder.initialiseParse(new StringReader(""), "", new Parser(builder));
        builder.stack.clear();
        builder.stack.add(parent);
        builder.stack.add(child1);
        builder.stack.add(child2);
        Token token = new Token(Token.EndTag.class, null, null, null, "tag1", null, null, null);
        builder.popStackToClose(token);
        assertEquals(2, builder.stack.size());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document expected = new Document();
        Element root = new Element("root");
        expected.setRootElement(root);
        List<Node> fragmentNodes = builder.parseFragment("<root><tag>foo</tag></root>", "", new Parser(builder));
        Document actual = builder.doc;
        assertEquals(expected, actual);
        List<Node> actualNodes = actual.childNodes();
        assertEquals(expected.childNodes().size(), actualNodes.size());
        for (int i = 0; i < expected.childNodes().size(); i++) {
            Node expectedNode = expected.childNodes().get(i);
            Node actualNode = actualNodes.get(i);
            if (expectedNode instanceof Element) {
                Element expectedElement = (Element) expectedNode;
                Element actualElement = (Element) actualNode;
                assertEquals(expectedElement.tagName(), actualElement.tagName());
                assertEquals(expectedElement.text(), actualElement.text());
                assertEquals(expectedElement.attributes().size(), actualElement.attributes().size());
                for (String key : expectedElement.attributes().keySet()) {
                    assertEquals(expectedElement.attribute(key), actualElement.attribute(key));
                }
            } else if (expectedNode instanceof TextNode) {
                TextNode expectedTextNode = (TextNode) expectedNode;
                TextNode actualTextNode = (TextNode) actualNode;
                assertEquals(expectedTextNode.text(), actualTextNode.text());
            }
        }
    }

    @Test
    public void testParseFragmentWithContext() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element context = new Element("context");
        Document expected = new Document();
        Element root = new Element("root");
        expected.setRootElement(root);
        List<Node> fragmentNodes = builder.parseFragment("<root><tag>foo</tag></root>", context, "", new Parser(builder));
        Document actual = builder.doc;
        assertEquals(expected, actual);
        List<Node> actualNodes = actual.childNodes();
        assertEquals(expected.childNodes().size(), actualNodes.size());
        for (int i = 0; i < expected.childNodes().size(); i++) {
            Node expectedNode = expected.childNodes().get(i);
            Node actualNode = actualNodes.get(i);
            if (expectedNode instanceof Element) {
                Element expectedElement = (Element) expectedNode;
                Element actualElement = (Element) actualNode;
                assertEquals(expectedElement.tagName(), actualElement.tagName());
                assertEquals(expectedElement.text(), actualElement.text());
                assertEquals(expectedElement.attributes().size(), actualElement.attributes().size());
                for (String key : expectedElement.attributes().keySet()) {
                    assertEquals(expectedElement.attribute(key), actualElement.attribute(key));
                }
                assertEquals(context, actualElement.parent());
            } else if (expectedNode instanceof TextNode) {
                TextNode expectedTextNode = (TextNode) expectedNode;
                TextNode actualTextNode = (TextNode) actualNode;
                assertEquals(expectedTextNode.text(), actualTextNode.text());
            }
        }
    }

    @Test
    public void testParseEmptyFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document expected = new Document();
        List<Node> fragmentNodes = builder.parseFragment("", "", new Parser(builder));
        Document actual = builder.doc;
        assertEquals(expected, actual);
        assertTrue(actual.childNodes().isEmpty());
    }
}