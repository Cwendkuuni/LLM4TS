package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Node node = new Element("tag", "http://example.com", null);
        xmlTreeBuilder.insertNode(node);
        Element root = xmlTreeBuilder.getDocument();
        assertSame(root.childNodes().size(), 1);
        assertSame(node, root.childNodes().get(0));
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag", new Attribute("attr", "value"), null, true);
        Element el = xmlTreeBuilder.insert(startTag);
        assertEquals("tag", el.nodeName());
        assertEquals("http://example.com", el.baseUri());
        assertEquals(1, el.attributeCount());
        assertSame("value", el.attribute("attr").getValue());
        assertEquals(0, el.childNodes().size());
    }

    @Test
    public void testInsertEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("tag");
        xmlTreeBuilder.popStackToClose(endTag);
        assertNull(xmlTreeBuilder.peek());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("/* This is a comment */");
        xmlTreeBuilder.insert(commentToken);
        Comment comment = (Comment) xmlTreeBuilder.peek();
        assertEquals("/* This is a comment */", comment.data());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character('A');
        xmlTreeBuilder.insert(characterToken);
        TextNode textNode = (TextNode) xmlTreeBuilder.peek();
        assertEquals("A", textNode.text());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("html", null, null, null);
        xmlTreeBuilder.insert(doctypeToken);
        DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.peek();
        assertEquals("html", doctypeNode.name());
        assertNull(doctypeNode.publicIdentifier());
        assertNull(doctypeNode.systemIdentifier());
        assertEquals(null, doctypeNode.internalSubset());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element root = new Element("root", "http://example.com", null);
        xmlTreeBuilder.stack.add(root);
        Element tag1 = new Element("tag1", "http://example.com", null);
        xmlTreeBuilder.stack.add(tag1);
        Element tag2 = new Element("tag2", "http://example.com", null);
        xmlTreeBuilder.stack.add(tag2);

        xmlTreeBuilder.popStackToClose(new Token.EndTag("tag1"));
        assertSame(tag1, xmlTreeBuilder.peek());

        xmlTreeBuilder.popStackToClose(new Token.EndTag("tag2"));
        assertSame(root, xmlTreeBuilder.peek());
    }

    @Test(expected = AssertionError.class)
    public void testPopStackToCloseUnknownTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element root = new Element("root", "http://example.com", null);
        xmlTreeBuilder.stack.add(root);
        Element tag = new Element("unknown", "http://example.com", null);
        xmlTreeBuilder.stack.add(tag);

        xmlTreeBuilder.popStackToClose(new Token.EndTag("tag"));
    }

    @Test
    public void testProcessToken() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root><tag attr=\"value\" /><tag1><comment>This is a comment</comment></tag1></root>";
        Tokenizer tokenizer = new Tokenizer(input);

        for (int i = 0; i < 5; i++) {
            Token token = tokenizer.nextToken();
            xmlTreeBuilder.process(token);
        }

        Element root = xmlTreeBuilder.getDocument();
        assertEquals("root", root.nodeName());
        assertEquals("http://example.com", root.baseUri());
        assertEquals(2, root.childNodes().size());

        Element tag = (Element) root.childNodes().get(0);
        assertEquals("tag", tag.nodeName());
        assertEquals("http://example.com", tag.baseUri());
        assertEquals(0, tag.childNodes().size());
        assertEquals("value", tag.attribute("attr").getValue());

        Element tag1 = (Element) root.childNodes().get(1);
        assertEquals("tag1", tag1.nodeName());
        assertEquals("http://example.com", tag1.baseUri());
        assertEquals(1, tag1.childNodes().size());

        Comment comment = (Comment) tag1.childNodes().get(0);
        assertEquals("This is a comment", comment.data());
    }
}