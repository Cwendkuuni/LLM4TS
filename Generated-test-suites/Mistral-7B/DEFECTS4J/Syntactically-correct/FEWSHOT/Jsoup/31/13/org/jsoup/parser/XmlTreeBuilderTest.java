package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.AttributeToken;
import org.jsoup.parser.Token.CommentToken;
import org.jsoup.parser.Token.CharacterToken;
import org.jsoup.parser.Token.DoctypeToken;
import org.jsoup.parser.Token.EndTagToken;
import org.jsoup.parser.Token.StartTagToken;

import java.util.Stack;

import org.junit.Test;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder builder = new XmlTreeBuilder();

    @Test
    public void testInsertNode() {
        String xml = "<root><child></child></root>";
        builder.initialiseParse(xml, null, null);

        Element root = new Element("root", null, null);
        Element child = new Element("child", null, null);
        Node insert = child;
        builder.insertNode(insert);
        Element actualRoot = builder.getCurrentElement();

        assertEquals(root, actualRoot);
        assertEquals(1, root.childElementCount());
        assertSame(child, root.child(0));
    }

    @Test
    public void testInsertStartTag() {
        String xml = "<root><child></child></root>";
        builder.initialiseParse(xml, null, null);

        Token startTagToken = new StartTagToken("<root>", 0, null, 0, 4);
        Element root = builder.insert(startTagToken);

        assertEquals("root", root.nodeName());
        assertNotNull(root.baseUri());
        assertEquals(0, root.attributeCount());

        Token endTagToken = new EndTagToken("</root>");
        builder.process(endTagToken);

        Element actualRoot = builder.getCurrentElement();
        assertEquals(root, actualRoot);
        assertNull(builder.getCurrentElement());
    }

    @Test
    public void testInsertEndTag() {
        String xml = "<root><child><grandchild></grandchild></child></root>";
        builder.initialiseParse(xml, null, null);

        Token startTagToken1 = new StartTagToken("<root>", 0, null, 0, 4);
        Element root = builder.insert(startTagToken1);

        Token startTagToken2 = new StartTagToken("<child>", 0, null, 6, 8);
        Element child = builder.insert(startTagToken2);

        Token endTagToken1 = new EndTagToken("</child>");
        builder.process(endTagToken1);

        Token startTagToken3 = new StartTagToken("<grandchild>", 0, null, 9, 14);
        Element grandchild = builder.insert(startTagToken3);

        Token endTagToken2 = new EndTagToken("</grandchild>");
        builder.process(endTagToken2);

        Token endTagToken3 = new EndTagToken("</child>");
        Element actualChild = builder.getCurrentElement();
        builder.process(endTagToken3);

        assertEquals(root, builder.getCurrentElement());
        assertEquals(child, actualChild);
        assertEquals(grandchild, child.child(0));
    }

    @Test
    public void testInsertComment() {
        String xml = "<!-- This is a comment -->";
        builder.initialiseParse(xml, null, null);

        Token commentToken = new CommentToken("<!-- This is a comment -->");
        Comment comment = builder.insert(commentToken);

        Node insert = comment;
        builder.insertNode(insert);

        Comment actualComment = builder.getCurrentElement();
        assertEquals("This is a comment", actualComment.text());
        assertNull(builder.getCurrentElement());
    }

    @Test
    public void testInsertCharacter() {
        String xml = "<root>&#123;</root>";
        builder.initialiseParse(xml, null, null);

        Token characterToken = new CharacterToken('&', "#123;", 0, 3);
        TextNode textNode = builder.insert(characterToken);

        Node insert = textNode;
        builder.insertNode(insert);

        TextNode actualTextNode = builder.getCurrentElement();
        assertEquals("&#123;", actualTextNode.text());
        assertNull(builder.getCurrentElement());
    }

    @Test
    public void testInsertDoctype() {
        String xml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
        builder.initialiseParse(xml, null, null);

        Token doctypeToken = new DoctypeToken("html", null, null, null);
        DocumentType documentType = builder.insert(doctypeToken);

        Node insert = documentType;
        builder.insertNode(insert);

        DocumentType actualDocumentType = builder.getCurrentElement();
        assertEquals("html", actualDocumentType.nodeName());
        assertNull(builder.getCurrentElement());
    }

    @Test
    public void testPopStackToClose() {
        String xml = "<root><child><grandchild></grandchild></child></root>";
        builder.initialiseParse(xml, null, null);

        Token startTagToken1 = new StartTagToken("<root>", 0, null, 0, 4);
        Element root = builder.insert(startTagToken1);

        Token startTagToken2 = new StartTagToken("<child>", 0, null, 6, 8);
        Element child = builder.insert(startTagToken2);

        Token startTagToken3 = new StartTagToken("<grandchild>", 0, null, 9, 14);
        Element grandchild = builder.insert(startTagToken3);

        Token endTagToken1 = new EndTagToken("</grandchild>");
        builder.process(endTagToken1);

        Token endTagToken2 = new EndTagToken("</child>");
        Element actualChild = builder.getCurrentElement();
        builder.process(endTagToken2);

        assertEquals(root, builder.getCurrentElement());
        assertEquals(child, actualChild);

        String elName = "grandchild";
        Token endTagToken3 = new EndTagToken(String.format("<%s>", elName));
        builder.popStackToClose(endTagToken3);

        Element grandchildElement = stack.peek();
        assertEquals(grandchild, grandchildElement);
    }

    @Test(expected = AssertionError.class)
    public void testPopStackToCloseNotFound() {
        String xml = "<root><child></child></root>";
        builder.initialiseParse(xml, null, null);

        Token startTagToken1 = new StartTagToken("<root>", 0, null, 0, 4);
        Element root = builder.insert(startTagToken1);

        Token startTagToken2 = new StartTagToken("<child>", 0, null, 6, 8);
        Element child = builder.insert(startTagToken2);

        String elName = "unknown";
        Token endTagToken = new EndTagToken(String.format("<%s>", elName));
        builder.popStackToClose(endTagToken);
    }
}