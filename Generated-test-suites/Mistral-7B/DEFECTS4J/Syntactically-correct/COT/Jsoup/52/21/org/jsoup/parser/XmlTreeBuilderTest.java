package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.util.List;

/**
 * XmlTreeBuilderTest
 */
public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        String input = "<root><child><grandchild/></child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, "", new ParseErrorList());
        assertEquals("root", xmlTreeBuilder.doc.tagName());
    }

    @Test
    public void testProcessStartTag() {
        String input = "<root><child/></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, "", new ParseErrorList());
        xmlTreeBuilder.runParser();

        Element root = xmlTreeBuilder.doc.child(0);
        Element child = root.child(0);
        assertEquals("child", child.tagName());
        assertEquals(1, root.childSize());
        assertEquals(1, child.childSize());
    }

    @Test
    public void testProcessEndTag() {
        String input = "<root><child/></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, "", new ParseErrorList());
        xmlTreeBuilder.runParser();

        Element root = xmlTreeBuilder.doc.child(0);
        Element child = root.child(0);
        xmlTreeBuilder.popStackToClose(new Token.EndTag("child"));
        assertEquals(0, root.childSize());
    }

    @Test
    public void testProcessComment() {
        String input = "<!--comment-->";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, "", new ParseErrorList());
        xmlTreeBuilder.runParser();

        Element comment = xmlTreeBuilder.doc.child(0);
        assertEquals(Comment.class, comment.getClass());
    }

    @Test
    public void testProcessCharacter() {
        String input = "<root>char</root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, "", new ParseErrorList());
        xmlTreeBuilder.runParser();

        TextNode characterNode = (TextNode) xmlTreeBuilder.doc.child(0).child(0);
        assertEquals("char", characterNode.text());
    }

    @Test
    public void testProcessDoctype() {
        String input = "<!DOCTYPE root SYSTEM \"sys:doc.dtd\">";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, "", new ParseErrorList());
        xmlTreeBuilder.runParser();

        DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.doc.child(0);
        assertEquals("root", doctypeNode.name());
        assertEquals("sys:doc.dtd", doctypeNode.systemIdentifier());
    }

    @Test(expected = AssertionError.class)
    public void testProcessUnexpectedToken() {
        String input = "<root>123</root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, "", new ParseErrorList());
        xmlTreeBuilder.runParser();
    }

    @Test
    public void testParseFragment() {
        String input = "<root><child/><grandchild/></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(input, "", new ParseErrorList());

        Element root = (Element) nodes.get(0);
        Element child = (Element) root.child(0);
        Element grandchild = (Element) child.child(0);

        assertEquals("child", child.tagName());
        assertEquals("grandchild", grandchild.tagName());
    }
}