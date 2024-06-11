package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Attribute;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.newDocument();
        assertSame(doc, builder.doc);
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        // Test with a known tag
        Token startTag = new Token(Token.StartTag.class, "<root>", new int[0], new Attribute[0]);
        Element root = builder.insert(startTag);
        assertEquals("root", root.nodeName());

        // Test with a self-closing tag
        Token selfClosingTag = new Token(Token.StartTag.class, "<tag />", new int[0], new Attribute[0]);
        Element tag = builder.insert(selfClosingTag);
        assertEquals("tag", tag.nodeName());
        assertFalse(tag.hasChildNodes());

        // Test with an unknown tag
        Token unknownTag = new Token(Token.StartTag.class, "<unknown>", new int[0], new Attribute[0]);
        Element unknown = builder.insert(unknownTag);
        assertEquals("unknown", unknown.nodeName());
        assertTrue(unknown.hasChildNodes());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root", null, null);
        builder.stack.add(root);

        Token endTag = new Token(Token.EndTag.class, "</root>", null, null);
        builder.popStackToClose(endTag);
        assertNull(builder.stack.get(builder.stack.size() - 1));
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Token comment = new Token(Token.Comment.class, "--comment--", null, null);
        Comment commentNode = builder.insert(comment);
        assertEquals("--comment--", commentNode.getData());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Token character = new Token(Token.Character.class, "text", null, null);
        TextNode textNode = builder.insert(character);
        assertEquals("text", textNode.getData());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Token doctype = new Token(Token.Doctype.class, "<!DOCTYPE html SYSTEM \"about:blank\">", null, null);
        DocumentType documentType = builder.insert(doctype);
        assertEquals("html", documentType.getName());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root", null, null);
        Element child = new Element("child", null, null);
        builder.stack.add(root);
        builder.stack.add(child);

        Token endTag = new Token(Token.EndTag.class, "</child>", null, null);
        builder.popStackToClose(endTag);
        Element lastElement = builder.stack.get(builder.stack.size() - 1);
        assertSame(child, lastElement);
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        String input = "<root><child></child></root>";
        List<Node> parsedFragment = builder.parseFragment(input, null, null);

        Element root = (Element) parsedFragment.get(0);
        assertEquals("root", root.nodeName());
        Element child = (Element) root.childNodes().get(0);
        assertEquals("child", child.nodeName());
        assertFalse(child.hasChildNodes());
    }
}