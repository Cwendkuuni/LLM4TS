package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParseXml() {
        String xmlInput = "<root>\n" +
                "  <element1 attr='value1'/>\n" +
                "  <element2>\n" +
                "    <subElement attr='value2'/>\n" +
                "  </element2>\n" +
                "</root>";

        Document xmlDoc = new XmlTreeBuilder().parse(xmlInput, null);

        Element root = xmlDoc.getRootElement();
        assertEquals("root", root.nodeName());

        List<Node> rootChildren = root.children();
        assertEquals(2, rootChildren.size());

        Element element1 = (Element) rootChildren.get(0);
        assertEquals("element1", element1.nodeName());
        assertEquals("value1", element1.attr("attr"));

        Element element2 = (Element) rootChildren.get(1);
        assertEquals("element2", element2.nodeName());

        List<Node> element2Children = element2.children();
        assertEquals(1, element2Children.size());

        Element subElement = (Element) element2Children.get(0);
        assertEquals("subElement", subElement.nodeName());
        assertEquals("value2", subElement.attr("attr"));
    }

    @Test
    public void testParseXmlWithDoctype() {
        String xmlInput = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED> ]>\n" +
                "<root>\n" +
                "  <element attr='value1'/>\n" +
                "</root>";

        Document xmlDoc = new XmlTreeBuilder().parse(xmlInput, null);

        DocumentType doctype = xmlDoc.doctype();
        assertNotNull(doctype);
        assertEquals("root", doctype.nodeName());

        Element root = xmlDoc.getRootElement();
        assertEquals("root", root.nodeName());

        List<Node> rootChildren = root.children();
        assertEquals(1, rootChildren.size());

        Element element = (Element) rootChildren.get(0);
        assertEquals("element", element.nodeName());
        assertEquals("value1", element.attr("attr"));
    }

    @Test
    public void testParseXmlWithComment() {
        String xmlInput = "<!-- This is a comment -->\n" +
                "<root>\n" +
                "  <element attr='value1'/>\n" +
                "</root>";

        Document xmlDoc = new XmlTreeBuilder().parse(xmlInput, null);

        List<Node> docChildren = xmlDoc.childNodes();
        assertEquals(2, docChildren.size());

        Node firstChild = docChildren.get(0);
        assertEquals(Node.COMMENT_NODE, firstChild.nodeType());
        assertEquals("This is a comment", firstChild.text());

        Node secondChild = docChildren.get(1);
        assertEquals(Element.class, secondChild.getClass());
        assertEquals("root", secondChild.nodeName());
    }

    @Test
    public void testParseXmlWithSelfClosingTag() {
        String xmlInput = "<root><element attr='value1'/></root>";

        Document xmlDoc = new XmlTreeBuilder().parse(xmlInput, null);

        Element root = xmlDoc.getRootElement();
        assertEquals("root", root.nodeName());

        List<Node> rootChildren = root.children();
        assertEquals(1, rootChildren.size());

        Element element = (Element) rootChildren.get(0);
        assertEquals("element", element.nodeName());
        assertEquals("value1", element.attr("attr"));

        assertTrue(element.isSelfClosing());
    }

    @Test
    public void testParseXmlWithUnknownTag() {
        String xmlInput = "<root><unknownTag attr='value1'/></root>";

        Document xmlDoc = new XmlTreeBuilder().parse(xmlInput, null);

        Element root = xmlDoc.getRootElement();
        assertEquals("root", root.nodeName());

        List<Node> rootChildren = root.children();
        assertEquals(1, rootChildren.size());

        Element element = (Element) rootChildren.get(0);
        assertEquals("unknownTag", element.nodeName());
        assertEquals("value1", element.attr("attr"));

        assertFalse(element.isSelfClosing());
    }

    @Test(expected = AssertionError.class)
    public void testParseXmlWithInvalidInput() {
        new XmlTreeBuilder().parse("", null);
    }

    @Test
    public void testParseXmlFragment() {
        String xmlFragment = "<root>\n" +
                "  <element attr='value1'/>\n" +
                "</root>";

        List<Node> fragmentNodes = new XmlTreeBuilder().parseFragment(xmlFragment, null, null, ParseSettings.defaultSettings()).get(0).childNodes();

        Element root = (Element) fragmentNodes.get(0);
        assertEquals("root", root.nodeName());

        List<Node> rootChildren = root.children();
        assertEquals(1, rootChildren.size());

        Element element = (Element) rootChildren.get(0);
        assertEquals("element", element.nodeName());
        assertEquals("value1", element.attr("attr"));
    }
}