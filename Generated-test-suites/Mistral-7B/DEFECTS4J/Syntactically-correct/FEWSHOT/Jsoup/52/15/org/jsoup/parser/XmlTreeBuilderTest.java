package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Attribute;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element root = xmlTreeBuilder.parseFragment("<?xml version='1.0' encoding='UTF-8'?>\n<root><child/></root>", null, null).get(0);
        assertNotNull(root);
        Element child = root.childNodes().get(0);
        assertEquals("child", child.nodeName());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element root = xmlTreeBuilder.parseFragment("<?xml version='1.0' encoding='UTF-8'?>\n<root><child><grandchild/></child></root>", null, null).get(0);
        Element grandchild = root.childNodes().get(1).childNodes().get(0);
        xmlTreeBuilder.popStackToClose(new EndTag("grandchild"));
        List<Node> childNodes = root.childNodes();
        assertFalse(childNodes.get(1).childNodes().isEmpty());
        assertEquals(0, childNodes.get(1).childNodes().size());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment("<?xml version='1.0' encoding='UTF-8'?>\n<!--Comment-->\n<root><child/></root>", null, null);
        Comment comment = (Comment) nodes.get(1);
        assertEquals("Comment", comment.nodeName());
        assertEquals("", comment.text());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment("<?xml version='1.0' encoding='UTF-8'?>\n<root>\n<child>Text</child>\n</root>", null, null);
        Text text = (Text) nodes.get(1).childNodes().get(0);
        assertEquals("Text", text.text());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment("<?xml version='1.0' encoding='UTF-8'?>\n<!DOCTYPE root [ <!ATTLIST child attr CDATA #REQUIRED > ]>\n<root><child attr='value'/></root>", null, null);
        DocumentType documentType = (DocumentType) nodes.get(0);
        assertEquals("root", documentType.nodeName());
        assertEquals("", documentType.text());
        Attribute attr = documentType.attributes().get(0);
        assertEquals("child", attr.getElementName());
        assertEquals("attr", attr.getName());
        assertEquals("CDATA", attr.getType());
        assertTrue(attr.isRequired());
    }

    @Test
    public void testProcessSelfClosingTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment("<root><child/></root>", null, null);
        Element child = (Element) nodes.get(1);
        assertTrue(child.isSelfClosing());
    }

    @Test
    public void testProcessUnknownTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment("<root><unknown/></root>", null, null);
        Element unknown = (Element) nodes.get(1);
        assertFalse(unknown.isSelfClosing());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment("<?xml version='1.0' encoding='UTF-8'?>\n<root>\n<child attr='value'/>\n<grandchild/>\n</root>", null, null);
        Element root = (Element) nodes.get(0);
        Element child = (Element) root.childNodes().get(0);
        Element grandchild = (Element) root.childNodes().get(1);
        assertSame(root, child.parentNode());
        assertSame(root, grandchild.parentNode());
        assertEquals("root", root.nodeName());
        assertEquals("child", child.nodeName());
        assertEquals("grandchild", grandchild.nodeName());
        assertEquals("value", child.attribute(0).getValue());
        assertNull(grandchild.attribute(0));
    }
}