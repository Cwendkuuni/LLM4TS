package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<root><element name=\"test\"/></root>";
        Document doc = builder.parseFragment(input, null, null);

        Element root = doc.getRootElement();
        Element element = root.getFirstChild();
        assertEquals("element", element.tagName());
        assertEquals("test", element.attributes().get("name"));
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<root><element name=\"test\"><subelement/></element></root>";
        Document doc = builder.parseFragment(input, null, null);

        Element root = doc.getRootElement();
        Element element = root.getFirstChild();
        Element subelement = element.getLastChild();
        assertNull(subelement.getParent());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<!-- This is a comment -->";
        Document doc = builder.parseFragment(input, null, null);

        Comment comment = doc.body().child(0);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<root>&#x26;<test>&#xA9;</test>&#x27;</root>";
        Document doc = builder.parseFragment(input, null, null);

        TextNode rootText = (TextNode) doc.getRootElement().child(0);
        assertEquals("&amp;<test>&#x00A9;</test>&apos;", rootText.text());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED > ]>";
        Document doc = builder.parseFragment(input, null, null);

        DocumentType docType = doc.getDoctype();
        assertEquals("root", docType.name());
        assertEquals("root", docType.nodeName());
    }

    @Test
    public void testProcessSelfClosingTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<root><element name=\"test\"/></root>";
        Document doc = builder.parseFragment(input, null, null);

        Element root = doc.getRootElement();
        Element element = root.getFirstChild();
        assertTrue(element.isTagClosed());
    }

    @Test
    public void testProcessUnknownTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<root><unknown/></root>";
        Document doc = builder.parseFragment(input, null, null);

        Element root = doc.getRootElement();
        Element unknown = root.getFirstChild();
        assertFalse(unknown.isTagClosed());
    }

    @Test
    public void testProcessNestedTags() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<root><element name=\"test\"><subelement/><otherelement/></element></root>";
        Document doc = builder.parseFragment(input, null, null);

        Element root = doc.getRootElement();
        Element element = root.getFirstChild();
        Element subelement = element.getFirstChild();
        Element otherelement = element.getChild(1);

        assertEquals("element", element.tagName());
        assertEquals(2, element.childNodeSize());
        assertNull(subelement.getParent());
        assertNull(otherelement.getParent());
    }
}