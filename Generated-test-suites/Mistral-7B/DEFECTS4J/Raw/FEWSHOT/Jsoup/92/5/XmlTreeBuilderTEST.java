package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xmlString = "<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<root>\n" +
                "<element1 attr1='value1' attr2='value2'>\n" +
                "<subElement>\n" +
                "<!-- this is a comment -->\n" +
                "<![CDATA[this is a CDATA]]>\n" +
                "</subElement>\n" +
                "</element1>\n" +
                "</root>";

        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString), "");
        assertEquals(1, xmlDoc.childNodeSize());
        Element root = xmlDoc.child(0);
        assertEquals("root", root.nodeName());
        assertEquals(1, root.childNodeSize());
        Element element1 = root.child(0);
        assertEquals("element1", element1.nodeName());
        assertEquals(1, element1.childNodeSize());
        Element subElement = element1.child(0);
        assertEquals("subElement", subElement.nodeName());
        assertTrue(subElement.childNodes().isEmpty());
        assertNull(subElement.attr("attr1"));
        assertEquals("value2", subElement.attr("attr2"));

        // test comment
        Comment comment = (Comment) subElement.childNodes().get(0);
        assertEquals(" this is a comment ", comment.data());

        // test CDATA
        CDataNode cdata = (CDataNode) subElement.childNodes().get(1);
        assertEquals("this is a CDATA", cdata.data());
    }

    @Test
    public void testParseEmpty() {
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(""), "");
        assertTrue(xmlDoc.childNodes().isEmpty());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<element1 attr1='value1' attr2='value2'><subElement/></element1>";
        List<Node> fragmentNodes = new XmlTreeBuilder().parseFragment(xmlFragment, "", "");
        Element element1 = (Element) fragmentNodes.get(0);
        assertEquals("element1", element1.nodeName());
        assertEquals(1, element1.childNodeSize());
        Element subElement = (Element) element1.child(0);
        assertEquals("subElement", subElement.nodeName());
        assertTrue(subElement.childNodes().isEmpty());
        assertNull(subElement.attr("attr1"));
        assertEquals("value2", subElement.attr("attr2"));
    }

    @Test
    public void testParseFragmentWithContext() {
        String xmlFragment = "<element1 attr1='value1' attr2='value2'><subElement/></element1>";
        Element context = new Element("root");
        List<Node> fragmentNodes = new XmlTreeBuilder().parseFragment(xmlFragment, context, "", "");
        Element element1 = (Element) fragmentNodes.get(0);
        assertEquals("element1", element1.nodeName());
        assertEquals(1, element1.childNodeSize());
        Element subElement = (Element) element1.child(0);
        assertEquals("subElement", subElement.nodeName());
        assertTrue(subElement.childNodes().isEmpty());
        assertNull(subElement.attr("attr1"));
        assertEquals("value2", subElement.attr("attr2"));
        context.appendChild(element1);
    }
}