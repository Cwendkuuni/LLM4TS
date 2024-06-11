package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xml = "<root><element attr1=\"val1\" attr2='val2'><subElement attr3=\"val3\"/></element></root>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml), "");
        Element root = doc.getRootElement();
        assertEquals("root", root.tagName());
        Element element = root.elementSibling(0);
        assertEquals("element", element.tagName());
        Element subElement = element.child(0);
        assertEquals("subElement", subElement.tagName());
        assertEquals("val1", element.attribute("attr1"));
        assertEquals("val2", element.attribute("attr2"));
        assertEquals("val3", subElement.attribute("attr3"));
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<subElement attr1=\"val1\"/>";
        List<Node> nodes = new XmlTreeBuilder().parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase).childNodes();
        assertEquals(1, nodes.size());
        Element subElement = (Element) nodes.get(0);
        assertEquals("subElement", subElement.tagName());
        assertEquals("val1", subElement.attribute("attr1"));
    }

    @Test
    public void testCommentProcessing() {
        String xml = "<!-- This is a comment -->";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml), "");
        assertEquals(1, doc.childNodes().size());
        Comment comment = (Comment) doc.child(0);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testDoctypeProcessing() {
        String xml = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml), "");
        DocumentType doctype = (DocumentType) doc.childNodes().get(0);
        assertEquals("html", doctype.tagName());
        assertEquals("about:legacy-compat", doctype.systemId());
    }

    @Test
    public void testUnknownTagProcessing() {
        String xml = "<unknownTag/>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml), "");
        Element element = (Element) doc.childNodes().get(0);
        assertEquals("unknownTag", element.tagName());
        assertTrue(element.isSelfClosing());
    }

    @Test
    public void testErrorHandling() {
        String xml = "<invalid>"; // intentionally incomplete xml
        Document doc = null;
        try {
            doc = new XmlTreeBuilder().parse(new StringReader(xml), "");
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertNull(doc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        new XmlTreeBuilder().parse(new StringReader("invalid input"), "");
    }
}