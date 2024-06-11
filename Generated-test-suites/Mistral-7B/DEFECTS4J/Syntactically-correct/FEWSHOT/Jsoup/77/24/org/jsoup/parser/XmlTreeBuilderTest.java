package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xmlString = "<root><element attr1=\"value1\" attr2=\"value2\"><subelement/></element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlString), "");

        assertNotNull(xmlDocument);
        assertEquals("root", xmlDocument.tagName());
        Element rootElement = xmlDocument.child(0);
        assertEquals("element", rootElement.tagName());
        assertNotNull(rootElement.attr("attr1"));
        assertNotNull(rootElement.attr("attr2"));
        Element subelement = rootElement.child(0);
        assertNull(subelement.attr("attr1"));
        assertNull(subelement.attr("attr2"));
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token startTagToken = new Token.StartTag("element", null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        Element element = xmlTreeBuilder.insert(startTagToken);

        assertNotNull(element);
        assertEquals("element", element.tagName());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token endTagToken = new Token.EndTag("element", null, null, null);

        xmlTreeBuilder.stack.add(new Element("root"));
        xmlTreeBuilder.stack.add(new Element("element"));

        xmlTreeBuilder.popStackToClose(endTagToken);

        assertEquals(1, xmlTreeBuilder.stack.size());
        assertSame(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token commentToken = new Token.Comment("This is a comment");

        xmlTreeBuilder.insert(commentToken);

        Node commentNode = xmlTreeBuilder.doc.childNodes().get(0);
        assertEquals(Node.COMMENT_NODE, commentNode.nodeType());
        assertEquals("This is a comment", commentNode.textContent());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token characterToken = new Token.Character('A');

        xmlTreeBuilder.insert(characterToken);

        Node characterNode = xmlTreeBuilder.doc.childNodes().get(0);
        assertEquals(Node.TEXT_NODE, characterNode.nodeType());
        assertEquals("A", characterNode.textContent());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token doctypeToken = new Token.Doctype("html", null, null);

        xmlTreeBuilder.insert(doctypeToken);

        DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.doc.childNodes().get(0);
        assertEquals("html", doctypeNode.nodeName());
        assertNull(doctypeNode.data());
        assertNull(doctypeNode.entity());
        assertNull(doctypeNode.systemId());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlFragment = "<root><element attr1=\"value1\" attr2=\"value2\"><subelement/></element></root>";
        List<Node> parsedNodes = xmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);

        assertFalse(parsedNodes.isEmpty());
        Node rootNode = parsedNodes.get(0);
        assertEquals(Node.ELEMENT_NODE, rootNode.nodeType());
        assertEquals("root", rootNode.tagName());
        Element rootElement = (Element) rootNode;
        Node elementNode = rootElement.child(0);
        assertEquals(Node.ELEMENT_NODE, elementNode.nodeType());
        assertEquals("element", elementNode.tagName());
        Element element = (Element) elementNode;
        assertNull(element.attr("attr2"));
    }
}