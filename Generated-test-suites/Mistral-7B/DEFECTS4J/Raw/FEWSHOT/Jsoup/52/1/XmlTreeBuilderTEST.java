package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.*;
import org.jsoup.parser.Token.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlFragment = "<root><element attr=\"value\"><subElement/></element></root>";
        List<Node> parsedNodes = xmlTreeBuilder.parseFragment(xmlFragment, null, null);

        Document doc = parsedNodes.get(0);
        assertNotNull(doc);
        Element root = doc.elementSiblings().get(0);
        Element element = root.elementChildren().get(0);
        assertEquals("root", root.nodeName());
        assertEquals("element", element.nodeName());
        assertEquals("value", element.attributes().get("attr"));
        assertNull(element.childSiblings().size());
        Element subElement = element.childNodes().get(0);
        assertEquals("subElement", subElement.nodeName());
        assertNull(subElement.attributes().size());
        assertNull(subElement.childSiblings().size());
    }

    @Test
    public void testCommentProcessing() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlFragment = "<!-- Comment --><element attr=\"value\"><!-- Another comment --></element>";
        List<Node> parsedNodes = xmlTreeBuilder.parseFragment(xmlFragment, null, null);

        Document doc = parsedNodes.get(0);
        Element element = doc.elementSiblings().get(0);
        assertEquals("element", element.nodeName());
        assertNull(element.childSiblings().size());
        List<Node> commentList = element.childNodes();
        assertEquals(2, commentList.size());
        Comment firstComment = (Comment) commentList.get(0);
        assertEquals("Comment", firstComment.text());
        Comment secondComment = (Comment) commentList.get(1);
        assertEquals("Another comment", secondComment.text());
    }

    @Test
    public void testDoctypeProcessing() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlFragment = "<!DOCTYPE root [ <!ENTITY foo SYMBOL \"bar\"> ]><root></root>";
        List<Node> parsedNodes = xmlTreeBuilder.parseFragment(xmlFragment, null, null);

        Document doc = parsedNodes.get(0);
        DocumentType doctype = doc.doctype();
        assertEquals("root", doctype.nodeName());
        assertEquals("root", doc.nodeName());
        List<Entity> entityList = doctype.entityDefinitions();
        assertEquals(1, entityList.size());
        Entity fooEntity = entityList.get(0);
        assertEquals("foo", fooEntity.entityName());
        assertEquals("bar", fooEntity.entityString());
    }

    @Test
    public void testSelfClosingTagProcessing() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlFragment = "<root><selfClosingTag/></root>";
        List<Node> parsedNodes = xmlTreeBuilder.parseFragment(xmlFragment, null, null);

        Document doc = parsedNodes.get(0);
        Element root = doc.elementSiblings().get(0);
        List<Node> selfClosingTagList = root.childNodes();
        assertEquals(1, selfClosingTagList.size());
        Element selfClosingTag = (Element) selfClosingTagList.get(0);
        assertEquals("selfClosingTag", selfClosingTag.nodeName());
        assertEquals(0, selfClosingTag.childNodes().size());
    }

    @Test
    public void testUnknownTagProcessing() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlFragment = "<root><unknownTag/></root>";
        List<Node> parsedNodes = xmlTreeBuilder.parseFragment(xmlFragment, null, null);

        Document doc = parsedNodes.get(0);
        Element root = doc.elementSiblings().get(0);
        List<Node> unknownTagList = root.childNodes();
        assertEquals(1, unknownTagList.size());
        Element unknownTag = (Element) unknownTagList.get(0);
        assertEquals("unknownTag", unknownTag.nodeName());
        assertEquals(0, unknownTag.childNodes().size());
    }

    @Test(expected = AssertionError.class)
    public void testUnexpectedTokenType() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlFragment = "<root 123>";
        xmlTreeBuilder.parseFragment(xmlFragment, null, null);
    }

    @Test
    public void testStackPopping() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlFragment = "<root><element><subElement><subElement2/></subElement></element></root>";
        List<Node> parsedNodes = xmlTreeBuilder.parseFragment(xmlFragment, null, null);

        Document doc = parsedNodes.get(0);
        Element root = doc.elementSiblings().get(0);
        Element element = root.elementChildren().get(0);
        Element subElement = element.elementChildren().get(0);
        Element subElement2 = subElement.elementChildren().get(0);

        xmlTreeBuilder.popStackToClose(subElement2.asEndTag());
        assertEquals(2, stack.size());
        xmlTreeBuilder.popStackToClose(subElement.asEndTag());
        assertEquals(1, stack.size());
        xmlTreeBuilder.popStackToClose(element.asEndTag());
        assertEquals(0, stack.size());
    }
}