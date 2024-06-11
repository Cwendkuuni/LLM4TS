package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.helper.Validate;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xmlInput = "<root><element attr=\"value\">Content</element></root>";
        Document xmlDoc = XmlTreeBuilder.parse(xmlInput, "http://example.com");

        Element root = xmlDoc.rootElement();
        List<Node> children = root.children();
        assertEquals(1, children.size());
        Element element = (Element) children.get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute("attr"));
        assertEquals("Content", element.text());
        assertEquals("http://example.com", xmlDoc.baseUri());
    }

    @Test
    public void testParseWithEmptyDocument() {
        String xmlInput = "<>";
        Document xmlDoc = XmlTreeBuilder.parse(xmlInput, "http://example.com");
        assertTrue(xmlDoc.isEmpty());
    }

    @Test
    public void testParseWithSelfClosingTag() {
        String xmlInput = "<img src=\"image.jpg\" />";
        Document xmlDoc = XmlTreeBuilder.parse(xmlInput, "http://example.com");
        Element imgElement = xmlDoc.getElementsByTag("img").first();
        assertTrue(imgElement.isSelfClosing());
    }

    @Test
    public void testParseWithEmptyComment() {
        String xmlInput = "<!-- -->";
        Document xmlDoc = XmlTreeBuilder.parse(xmlInput, "http://example.com");
        List<Node> comments = xmlDoc.getElementsByTag("comment");
        assertEquals(1, comments.size());
        Comment comment = (Comment) comments.get(0);
        assertEquals("", comment.text());
    }

    @Test
    public void testParseWithCommentWithData() {
        String xmlInput = "<!-- This is a comment -->";
        Document xmlDoc = XmlTreeBuilder.parse(xmlInput, "http://example.com");
        List<Node> comments = xmlDoc.getElementsByTag("comment");
        assertEquals(1, comments.size());
        Comment comment = (Comment) comments.get(0);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseWithCharacterData() {
        String xmlInput = "<root><element>&lt;content&gt;</element></root>";
        Document xmlDoc = XmlTreeBuilder.parse(xmlInput, "http://example.com");
        Element root = xmlDoc.rootElement();
        List<Node> children = root.children();
        Element element = (Element) children.get(0);
        assertEquals("content", element.text());
    }

    @Test
    public void testParseWithDoctypeDeclaration() {
        String xmlInput = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED > ]><root><element attr=\"value\">Content</element></root>";
        Document xmlDoc = XmlTreeBuilder.parse(xmlInput, "http://example.com");
        DocumentType doctype = xmlDoc.doctype();
        assertEquals("root", doctype.tagName());
        List<AttributeSpec> attributes = doctype.attributes();
        assertEquals(1, attributes.size());
        AttributeSpec attrSpec = attributes.get(0);
        assertEquals("element", attrSpec.elementName());
        assertEquals("attr", attrSpec.attributeName());
        assertEquals("CDATA", attrSpec.attributeType());
        assertEquals("#REQUIRED", attrSpec.required());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<root><element attr=\"value\">Content</element></root>";
        List<Node> fragmentNodes = XmlTreeBuilder.parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase).get(0).childNodes();

        Element root = (Element) fragmentNodes.get(0);
        Element element = (Element) fragmentNodes.get(1);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute("attr"));
        assertEquals("Content", element.text());
    }

    @Test
    public void testParseWithEmptyInput() {
        Document xmlDoc = XmlTreeBuilder.parse("", "http://example.com");
        assertTrue(xmlDoc.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullInput() {
        XmlTreeBuilder.parse(null, "http://example.com");
    }

    @Test
    public void testParseWithNullBaseUri() {
        Document xmlDoc = XmlTreeBuilder.parse("<root><element attr=\"value\">Content</element></root>", null);
        assertEquals("", xmlDoc.baseUri());
    }

    @Test
    public void testProcessUnknownTag() {
        String xmlInput = "<unknownTag attr=\"value\">Content</unknownTag>";
        Document xmlDoc = XmlTreeBuilder.parse(xmlInput, "http://example.com");
        Element unknownTag = xmlDoc.getElementsByTag("unknownTag").first();
        assertFalse(unknownTag.isSelfClosing());
        assertTrue(unknownTag.isUnknownTag());
        assertEquals("value", unknownTag.attribute("attr"));
        assertEquals("Content", unknownTag.text());
    }

    @Test
    public void testPopStackToClose() {
        String xmlInput = "<root><element><subElement></subElement></element></root>";
        Document xmlDoc = XmlTreeBuilder.parse(xmlInput, "http://example.com");
        Element root = xmlDoc.rootElement();
        Element element = root.getElementsByTag("element").first();
        Element subElement = element.getElementsByTag("subElement").first();
        XmlTreeBuilder.popStackToClose(tokeniser(xmlInput), "subElement");
        assertEquals(1, stack.size());
        assertEquals(root, stack.peek());
    }

    @Test
    public void testPopStackToCloseWithNonExistentTag() {
        String xmlInput = "<root><element><subElement></subElement></element></root>";
        Document xmlDoc = XmlTreeBuilder.parse(xmlInput, "http://example.com");
        XmlTreeBuilder.popStackToClose(tokeniser(xmlInput), "unknownTag");
        assertEquals(1, stack.size());
        assertEquals(xmlDoc.rootElement(), stack.peek());
    }

    private Tokenizer tokeniser(String input) {
        return new Tokenizer(input, "http://example.com", ParseSettings.preserveCase, new ParseErrorList());
    }

    private static List<Element> stack = new ArrayList<>();
}