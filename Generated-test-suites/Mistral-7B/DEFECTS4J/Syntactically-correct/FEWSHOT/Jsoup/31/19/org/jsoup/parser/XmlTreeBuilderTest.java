package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Node node = new Element("tag");
        builder.insertNode(node);
        Element root = builder.getDocument();
        assertNotNull(root.childNodes().size());
        assertNotNull(root.childNodes().get(0));
        assertEquals("tag", root.childNodes().get(0).nodeName());
    }

    @Test
    public void testInsertElement() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag", null, null, null, null, null, 0, 0, 0, 0, false, false);
        Element el = builder.insert(startTag);
        assertNotNull(el);
        assertEquals("tag", el.nodeName());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("comment");
        builder.insert(commentToken);
        Comment comment = (Comment) builder.getDocument().childNodes().get(0);
        assertEquals("comment", comment.getData());
    }

    @Test
    public void testInsertTextNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character('a');
        builder.insert(characterToken);
        TextNode textNode = (TextNode) builder.getDocument().childNodes().get(0);
        assertEquals("a", textNode.text());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype d = new Token.Doctype("html", null, null, null);
        builder.insert(d);
        DocumentType doctypeNode = (DocumentType) builder.getDocument().childNodes().get(0);
        assertEquals("html", doctypeNode.name());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element parent = new Element("parent");
        Element child1 = new Element("child1");
        Element child2 = new Element("child2");
        builder.insertNode(parent);
        builder.insertNode(child1);
        builder.stack.add(child1);
        builder.insertNode(child2);
        builder.stack.add(child2);

        Token.EndTag endTag1 = new Token.EndTag("child1");
        builder.popStackToClose(endTag1);
        assertEquals(1, builder.stack.size());
        assertNotNull(builder.stack.peek());
        assertEquals("parent", builder.stack.peek().nodeName());

        Token.EndTag endTag2 = new Token.EndTag("parent");
        builder.popStackToClose(endTag2);
        assertEquals(0, builder.stack.size());
        assertNull(builder.stack.peek());
    }

    @Test(expected = AssertionError.class)
    public void testPopStackToCloseUnknownTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element parent = new Element("parent");
        builder.insertNode(parent);

        Token.EndTag endTag = new Token.EndTag("unknown");
        builder.popStackToClose(endTag);
    }
}