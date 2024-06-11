package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root><child attr=\"value\"/></root>";
        Document doc = xmlTreeBuilder.parseFragment(input, null, null)[0];

        Element root = doc.getElementsByTag("root").first();
        Element child = root.getElementsByTag("child").first();

        assertEquals(1, doc.childNodes().size());
        assertEquals(1, root.childNodes().size());
        assertEquals("root", root.nodeName());
        assertEquals("value", child.attribute("attr"));
        assertEquals(1, child.childNodes().size());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root><child/></root>";
        Document doc = xmlTreeBuilder.parseFragment(input, null, null)[0];

        Element root = doc.getElementsByTag("root").first();
        Element child = root.getElementsByTag("child").first();

        assertEquals(1, doc.childNodes().size());
        assertEquals(1, root.childNodes().size());
        assertEquals("root", root.nodeName());
        assertEquals(0, child.childNodes().size());

        root.remove();
        assertEquals(0, doc.childNodes().size());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<!-- This is a comment -->";
        Document doc = xmlTreeBuilder.parseFragment(input, null, null)[0];

        Comment comment = doc.getElementsByTag("comment").first();
        assertEquals("This is a comment", comment.data());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root>&#x1F600;</root>";
        Document doc = xmlTreeBuilder.parseFragment(input, null, null)[0];

        Element root = doc.getElementsByTag("root").first();
        TextNode textNode = root.getElementsByTag("text").first();

        assertEquals(1, doc.childNodes().size());
        assertEquals(1, root.childNodes().size());
        assertEquals("root", root.nodeName());
        assertEquals("\uD835\uDD10", textNode.data());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
        Document doc = xmlTreeBuilder.parseFragment(input, null, null)[0];

        DocumentType documentType = doc.getDoctype();
        assertEquals("html", documentType.nodeName());
        assertEquals("about:legacy-compat", documentType.systemId());
    }

    @Test
    public void testProcessUnknownTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<unknown/>";
        Document doc = xmlTreeBuilder.parseFragment(input, null, null)[0];

        Element unknown = doc.getElementsByTag("unknown").first();
        assertNotNull(unknown);
        assertSame(Element.Tag.UNKNOWN, unknown.tag());
    }

    @Test
    public void testProcessEmptyElement() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<empty/>";
        Document doc = xmlTreeBuilder.parseFragment(input, null, null)[0];

        Element empty = doc.getElementsByTag("empty").first();
        assertEquals("empty", empty.nodeName());
        assertEquals(0, empty.childNodes().size());
    }

    @Test
    public void testProcessSelfClosingTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<selfClosing/>";
        Document doc = xmlTreeBuilder.parseFragment(input, null, null)[0];

        Element selfClosing = doc.getElementsByTag("selfClosing").first();
        assertEquals("selfClosing", selfClosing.nodeName());
        assertEquals(0, selfClosing.childNodes().size());
        assertFalse(selfClosing.hasParent());
    }

    @Test(expected = AssertionError.class)
    public void testProcessUnexpectedToken() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root><html><body><script><script>";
        xmlTreeBuilder.parseFragment(input, null, null);
    }

    @Test
    public void testParseXmlDeclaration() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Document doc = xmlTreeBuilder.parseFragment(input, null, null)[0];

        XmlDeclaration xmlDeclaration = doc.getElementsByTag("xml-declaration").first();
        assertEquals(1, doc.childNodes().size());
        assertEquals("xml-declaration", xmlDeclaration.nodeName());
        assertEquals("version=\"1.0\"", xmlDeclaration.attributes().toString());
        assertEquals("UTF-8", xmlDeclaration.xmlVersion().encoding());
        assertFalse(xmlDeclaration.isStandalone());
    }

    @Test
    public void testParseXmlDeclarationWithStandalone() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
        Document doc = xmlTreeBuilder.parseFragment(input, null, null)[0];

        XmlDeclaration xmlDeclaration = doc.getElementsByTag("xml-declaration").first();
        assertEquals(1, doc.childNodes().size());
        assertEquals("xml-declaration", xmlDeclaration.nodeName());
        assertEquals("version=\"1.0\"", xmlDeclaration.attributes().toString());
        assertEquals("UTF-8", xmlDeclaration.xmlVersion().encoding());
        assertTrue(xmlDeclaration.isStandalone());
    }

    @Test
    public void testParseEmptyXmlDeclaration() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<?xml?>";
        Document doc = xmlTreeBuilder.parseFragment(input, null, null)[0];

        XmlDeclaration xmlDeclaration = doc.getElementsByTag("xml-declaration").first();
        assertEquals(1, doc.childNodes().size());
        assertEquals("xml-declaration", xmlDeclaration.nodeName());
        assertTrue(xmlDeclaration.isEmpty());
    }

    @Test
    public void testParseXmlDeclarationWithProcessingInstruction() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<?myProcessingInstruction target=\"myTarget\"?>";
        Document doc = xmlTreeBuilder.parseFragment(input, null, null)[0];

        XmlDeclaration xmlDeclaration = doc.getElementsByTag("xml-declaration").first();
        assertEquals(2, doc.childNodes().size());
        assertEquals("xml-declaration", xmlDeclaration.nodeName());
        assertEquals("version=\"1.0\"", xmlDeclaration.attributes().toString());
        assertEquals("UTF-8", xmlDeclaration.xmlVersion().encoding());
        assertFalse(xmlDeclaration.isStandalone());

        ProcessingInstruction processingInstruction = doc.getElementsByTag("processing-instruction").first();
        assertEquals("myProcessingInstruction", processingInstruction.target());
        assertEquals("", processingInstruction.data());
    }
}