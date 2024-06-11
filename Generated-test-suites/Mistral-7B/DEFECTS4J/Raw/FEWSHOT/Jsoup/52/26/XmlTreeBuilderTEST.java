package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.*;
import org.jsoup.parser.Token;
import org.junit.Test;

public class XmlTreeBuilderTest {
    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token startTag = new Token.StartTag("root");
        builder.insert(startTag);
        Element root = builder.currentElement();
        assertEquals("root", root.nodeName());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root", null, null);
        builder.stack.add(root);
        Token endTag = new Token.EndTag("root");
        builder.popStackToClose(endTag);
        assertNull(builder.stack.get(builder.stack.size() - 1));
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token comment = new Token.Comment("This is a comment");
        builder.insert(comment);
        Comment commentNode = (Comment) builder.currentNode();
        assertEquals("This is a comment", commentNode.data());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token character = new Token.Character('A');
        builder.insert(character);
        TextNode characterNode = (TextNode) builder.currentNode();
        assertEquals("A", characterNode.text());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token doctype = new Token.Doctype("html", null, null, null);
        builder.insert(doctype);
        DocumentType doctypeNode = (DocumentType) builder.currentNode();
        assertEquals("html", doctypeNode.name());
        assertNull(doctypeNode.publicIdentifier());
        assertNull(doctypeNode.systemIdentifier());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root", null, null);
        Element child = new Element("child", null, null);
        builder.stack.add(root);
        builder.stack.add(child);

        Token endTag = new Token.EndTag("child");
        builder.popStackToClose(endTag);
        assertEquals(root, builder.stack.get(builder.stack.size() - 1));
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xmlFragment = "<root><child></child></root>";
        List<Node> nodes = builder.parseFragment(xmlFragment, null, null);
        assertEquals(3, nodes.size());
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.nodeName());
        Element child = (Element) nodes.get(1);
        assertEquals("child", child.nodeName());
        assertEquals(0, child.childNodes().size());
    }
}