
package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testParse() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><child1>data1</child1><child2>data2</child2></root>";
        Document document = xmlTreeBuilder.parse(xml, "");
        assertEquals("root", document.nodeName());
        assertEquals(2, document.childNodes().size());
        assertTrue(document.child(0) instanceof Element);
        assertTrue(document.child(1) instanceof Element);

        Element child1 = document.child(0);
        assertEquals("child1", child1.nodeName());
        assertEquals("data1", child1.text());

        Element child2 = document.child(1);
        assertEquals("child2", child2.nodeName());
        assertEquals("data2", child2.text());
    }

    @Test
    public void testInsertNode() {
        Element root = new Element(Tag.valueOf("root"), "");
        xmlTreeBuilder.insertNode(root);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().child(0) instanceof Element);

        Element child = xmlTreeBuilder.currentElement().child(0);
        assertTrue(child == root);
    }

    @Test
    public void testInsert() {
        Token.StartTag startTag = new Token.StartTag(Tag.valueOf("child"), "");
        Element element = xmlTreeBuilder.insert(startTag);
        assertEquals("child", element.nodeName());
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().child(0) instanceof Element);

        Element child = xmlTreeBuilder.currentElement().child(0);
        assertTrue(child == element);
    }

    @Test
    public void testInsertWithSelfClosing() {
        Token.StartTag startTag = new Token.StartTag(Tag.valueOf("child"), "", true);
        Element element = xmlTreeBuilder.insert(startTag);
        assertEquals("child", element.nodeName());
        assertEquals(0, xmlTreeBuilder.currentElement().childNodes().size());
    }

    @Test
    public void testInsertComment() {
        String commentText = "This is a comment";
        Token.Comment commentToken = new Token.Comment(commentText, "");
        xmlTreeBuilder.insert(commentToken);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().child(0) instanceof Comment);

        Comment comment = (Comment) xmlTreeBuilder.currentElement().child(0);
        assertEquals(commentText, comment.getData());
    }

    @Test
    public void testInsertCharacter() {
        String characterData = "This is character data";
        Token.Character characterToken = new Token.Character(characterData, false);
        xmlTreeBuilder.insert(characterToken);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().child(0) instanceof TextNode);

        TextNode textNode = (TextNode) xmlTreeBuilder.currentElement().child(0);
        assertEquals(characterData, textNode.getWholeText());
    }

    @Test
    public void testInsertCharacterWithCData() {
        String characterData = "This is character data";
        Token.Character characterToken = new Token.Character(characterData, true);
        xmlTreeBuilder.insert(characterToken);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().child(0) instanceof CDataNode);

        CDataNode cDataNode = (CDataNode) xmlTreeBuilder.currentElement().child(0);
        assertEquals(characterData, cDataNode.getWholeText());
    }

    @Test
    public void testInsertDoctype() {
        String name = "root";
        String publicIdentifier = "-//W3C//DTD HTML 4.01 Transitional//EN";
        String systemIdentifier = "http://www.w3.org/TR/html4/loose.dtd";
        Token.Doctype doctype = new Token.Doctype(name, publicIdentifier, systemIdentifier, "");
        xmlTreeBuilder.insert(doctype);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().child(0) instanceof DocumentType);

        DocumentType documentType = (DocumentType) xmlTreeBuilder.currentElement().child(0);
        assertEquals(name, documentType.nodeName());
        assertEquals(publicIdentifier, documentType.getPublicIdentifier());
        assertEquals(systemIdentifier, documentType.getSystemIdentifier());
    }

    @Test
    public void testPopStackToClose() {
        Token.StartTag startTag1 = new Token.StartTag(Tag.valueOf("child"), "");
        xmlTreeBuilder.insert(startTag1);

        Token.StartTag startTag2 = new Token.StartTag(Tag.valueOf("anotherChild"), "");
        xmlTreeBuilder.insert(startTag2);

        Token.StartTag startTag3 = new Token.StartTag(Tag.valueOf("child"), "");
        xmlTreeBuilder.insert(startTag3);

        Token.EndTag endTag = new Token.EndTag("child");
        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(1, xmlTreeBuilder.stack.size());
        assertTrue(xmlTreeBuilder.stack.get(0).nodeName().equals("child"));
    }

    @Test
    public void testParseFragment() {
        String fragment = "<child1>data1</child1><child2>data2</child2>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(fragment, "");
        assertEquals(2, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertTrue(nodes.get(1) instanceof Element);

        Element child1 = (Element) nodes.get(0);
        assertEquals("child1", child1.nodeName());
        assertEquals("data1", child1.text());

        Element child2 = (Element) nodes.get(1);
        assertEquals("child2", child2.nodeName());
        assertEquals("data2", child2.text());
    }

    @Test
    public void testParseFragmentWithContext() {
        String fragment = "<child1>data1</child1><child2>data2</child2>";
        Element context = new Element(Tag.valueOf("root"), "");
        List<Node> nodes = xmlTreeBuilder.parseFragment(fragment, context, "");
        assertEquals(2, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertTrue(nodes.get(1) instanceof Element);

        Element child1 = (Element) nodes.get(0);
        assertEquals("child1", child1.nodeName());
        assertEquals("data1", child1.text());

        Element child2 = (Element) nodes.get(1);
        assertEquals("child2", child2.nodeName());
        assertEquals("data2", child2.text());
    }
}

