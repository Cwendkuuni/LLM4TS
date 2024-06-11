package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.*;
import org.jsoup.nodes.xml.XmlDeclaration;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testProcess_StartTag() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Token token = new Token(Tag.START_TAG, "root", new AttributeSet());
        parser.process(token);

        Element root = parser.getCurrentElement();
        assertEquals("root", root.nodeName());
    }

    @Test
    public void testProcess_EndTag() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Token token = new Token(Tag.END_TAG, "root");
        parser.process(token);

        Element root = parser.getCurrentElement();
        assertTrue(parser.stack.isEmpty());
        assertNull(root);
    }

    @Test
    public void testProcess_Comment() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Token token = new Token(Token.Type.COMMENT, "This is a comment");
        parser.process(token);

        Comment comment = parser.getCurrentElement();
        assertEquals("This is a comment", comment.nodeName());
    }

    @Test
    public void testProcess_Character() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Token token = new Token(Token.Type.CHARACTERS, "Hello World");
        parser.process(token);

        Text textNode = parser.getCurrentElement();
        assertEquals("Hello World", textNode.nodeValue());
    }

    @Test
    public void testProcess_Doctype() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Token token = new Token(Token.Type.DOCTYPE, "html", null, null);
        parser.process(token);

        DocumentType documentType = parser.getCurrentElement();
        assertEquals("html", documentType.nodeName());
    }

    @Test
    public void testProcess_Eof() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        parser.process(new Token(Token.Type.EOF));

        assertTrue(parser.stack.isEmpty());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Element root = new Element("root", null);
        parser.insertNode(root);

        Element currentElement = parser.getCurrentElement();
        assertEquals(root, currentElement);
    }

    @Test
    public void testInsert_StartTag() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Token token = new Token(Tag.START_TAG, "element", new AttributeSet());
        parser.insert(token);

        Element element = parser.getCurrentElement();
        assertEquals("element", element.nodeName());
    }

    @Test
    public void testInsert_EndTag() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Token token = new Token(Tag.END_TAG, "element");
        parser.insert(token);

        Element element = parser.getCurrentElement();
        assertNull(element);
    }

    @Test
    public void testInsert_Comment() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Token token = new Token(Token.Type.COMMENT, "This is a comment");
        parser.insert(token);

        Comment comment = parser.getCurrentElement();
        assertEquals("This is a comment", comment.nodeName());
    }

    @Test
    public void testInsert_Character() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Token token = new Token(Token.Type.CHARACTERS, "Hello World");
        parser.insert(token);

        Text textNode = parser.getCurrentElement();
        assertEquals("Hello World", textNode.nodeValue());
    }

    @Test
    public void testInsert_Doctype() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Token token = new Token(Token.Type.DOCTYPE, "html", null, null);
        parser.insert(token);

        DocumentType documentType = parser.getCurrentElement();
        assertEquals("html", documentType.nodeName());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Element root = new Element("root", null);
        Element child = new Element("child", null);
        parser.stack.add(root);
        parser.stack.add(child);

        parser.popStackToClose(new Token(Tag.END_TAG, "child"));

        assertEquals(1, parser.stack.size());
        assertEquals(root, parser.stack.get(0));
    }

    @Test(expected = AssertionError.class)
    public void testPopStackToClose_NonMatchingTag() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Element root = new Element("root", null);
        Element child = new Element("child", null);
        parser.stack.add(root);
        parser.stack.add(child);

        parser.popStackToClose(new Token(Tag.END_TAG, "parent"));
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        String xmlString = "<root><child></child></root>";
        List<Node> nodes = parser.parseFragment(xmlString, "", new ArrayList<>());

        assertEquals(3, nodes.size());

        Element root = (Element) nodes.get(0);
        Element child = (Element) nodes.get(1);

        assertEquals("root", root.nodeName());
        assertEquals("child", child.nodeName());

        assertEquals(0, root.childNodes().size());
        assertEquals(0, child.childNodes().size());
    }

    @Test
    public void testParseFragment_WithXmlDeclaration() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><child></child></root>";
        List<Node> nodes = parser.parseFragment(xmlString, "", new ArrayList<>());

        assertEquals(4, nodes.size());

        XmlDeclaration xmlDeclaration = (XmlDeclaration) nodes.get(0);
        Element root = (Element) nodes.get(1);
        Element child = (Element) nodes.get(2);

        assertEquals("xml", xmlDeclaration.nodeName());
        assertEquals("1.0", xmlDeclaration.version());
        assertEquals("UTF-8", xmlDeclaration.encoding());

        assertEquals("root", root.nodeName());
        assertEquals("child", child.nodeName());

        assertEquals(0, root.childNodes().size());
        assertEquals(0, child.childNodes().size());
    }
}