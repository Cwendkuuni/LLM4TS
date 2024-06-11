package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root");
        Node node = new Element("node");
        builder.insertNode(node);
        assertEquals(node, root.getFirstChild());
    }

    @Test
    public void testInsertElement() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root");
        Element el = builder.insert(mock(Token.StartTag.class));
        assertEquals(el, root.getFirstChild());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Comment comment = new Comment("Test Comment", "");
        Node insert = comment;
        builder.insert(mock(Token.Comment.class));
        assertEquals(insert, builder.doc.childNodes().item(0));
    }

    @Test
    public void testInsertText() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        TextNode textNode = new TextNode("Test Text", "");
        builder.insert(mock(Token.Character.class));
        assertEquals(textNode, builder.doc.childNodes().item(0));
    }

    @Test
    public void testInsertDocumentType() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        DocumentType doctypeNode = new DocumentType("html", "", "", "");
        builder.insert(mock(Token.Doctype.class));
        assertEquals(doctypeNode, builder.doc.childNodes().item(0));
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root");
        Element parent = new Element("parent");
        Element child = new Element("child");
        parent.appendChild(child);
        root.appendChild(parent);
        builder.stack.add(root);
        builder.stack.add(parent);

        builder.popStackToClose(mock(Token.EndTag.class));
        assertEquals(root, builder.stack.peek());
    }

    @Test(expected = NullPointerException.class)
    public void testPopStackToCloseNull() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.popStackToClose(null);
    }

    @Test
    public void testPopStackToCloseUnknownTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root");
        Element parent = new Element("parent");
        Element child = new Element("child");
        parent.appendChild(child);
        root.appendChild(parent);
        builder.stack.add(root);
        builder.stack.add(parent);

        builder.popStackToClose(mock(Token.EndTag.class, "unknownTag"));
        assertEquals(root, builder.stack.peek());
    }

    @Test
    public void testPopStackToCloseTagExists() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root");
        Element parent = new Element("parent");
        Element child = new Element("child");
        parent.appendChild(child);
        root.appendChild(parent);
        builder.stack.add(root);
        builder.stack.add(parent);

        builder.popStackToClose(mock(Token.EndTag.class, "parent"));
        assertEquals(child, builder.stack.peek());
    }

    @Test
    public void testPopStackToCloseMultipleTags() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root");
        Element parent1 = new Element("parent1");
        Element child1 = new Element("child1");
        parent1.appendChild(child1);
        Element parent2 = new Element("parent2");
        root.appendChild(parent1);
        root.appendChild(parent2);
        builder.stack.add(root);
        builder.stack.add(parent1);
        builder.stack.add(parent2);

        builder.popStackToClose(mock(Token.EndTag.class, "parent1"));
        assertEquals(parent2, builder.stack.peek());

        builder.popStackToClose(mock(Token.EndTag.class, "parent2"));
        assertEquals(root, builder.stack.peek());
    }
}