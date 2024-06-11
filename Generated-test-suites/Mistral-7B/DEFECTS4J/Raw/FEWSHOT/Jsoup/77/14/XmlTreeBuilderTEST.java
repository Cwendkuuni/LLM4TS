package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.List;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.helper.Validate;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xmlString = "<root><child1>hello</child1><child2><grandChild>world</grandChild></child2></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), null);

        assertNotNull(xmlDoc);
        Element root = xmlDoc.getRootElement();
        assertNotNull(root);
        assertEquals("root", root.tagName());

        List<Element> children = new ArrayList<Element>();
        List<Node> nodeList = xmlDoc.childNodes();
        for (Node node : nodeList) {
            if (node instanceof Element) {
                children.add((Element) node);
            }
        }

        assertEquals(2, children.size());
        Element child1 = children.get(0);
        assertEquals("child1", child1.tagName());
        Text textNode = child1.text();
        assertEquals("hello", textNode.text());

        Element child2 = children.get(1);
        assertEquals("child2", child2.tagName());

        List<Element> grandChildren = new ArrayList<Element>();
        List<Node> grandChildrenNodeList = child2.childNodes();
        for (Node node : grandChildrenNodeList) {
            if (node instanceof Element) {
                grandChildren.add((Element) node);
            }
        }

        assertEquals(1, grandChildren.size());
        Element grandChild = grandChildren.get(0);
        assertEquals("grandChild", grandChild.tagName());
        Text grandChildTextNode = grandChild.text();
        assertEquals("world", grandChildTextNode.text());
    }

    @Test
    public void testParseWithEmptyInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(""), null);
        assertNotNull(xmlDoc);
        assertTrue(xmlDoc.tagName().equals(""));
        assertEquals(0, xmlDoc.childNodes().size());
    }

    @Test
    public void testParseWithNullInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        try {
            xmlTreeBuilder.parse(null, null);
            Validate.fail("Expected ParseException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseWithInvalidInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        try {
            xmlTreeBuilder.parse(new StringReader("invalid xml"), null);
            Validate.fail("Expected ParseException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<child1><grandChild>world</grandChild></child1>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, ParseErrorList.noTracking(), ParseSettings.preserveCase).childNodes();

        Element child1 = (Element) nodes.get(0);
        assertEquals("child1", child1.tagName());
        List<Node> grandChildren = child1.childNodes();
        Element grandChild = (Element) grandChildren.get(0);
        assertEquals("grandChild", grandChild.tagName());
        Text grandChildTextNode = (Text) grandChildren.get(1);
        assertEquals("world", grandChildTextNode.text());
    }

    @Test
    public void testProcessStartTag() {
        String xmlString = "<root><child1>hello</child1></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), null);
        Element root = xmlDoc.getRootElement();

        Token token = xmlTreeBuilder.tokenize("<root><child1>hello</child1></root>").next();
        // start tag
        assertTrue(token.type == Token.Type.StartTag);

        Element rootFromToken = xmlTreeBuilder.insert(token.asStartTag());
        assertEquals(root, rootFromToken);
    }

    @Test
    public void testProcessEndTag() {
        String xmlString = "<root><child1>hello</child1></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), null);
        Element root = xmlDoc.getRootElement();

        Token token = xmlTreeBuilder.tokenize("<root><child1>hello</child1></root>").next();
        // end tag
        assertTrue(token.type == Token.Type.EndTag);

        Element child1 = root.getChild(0);
        String child1Name = child1.tagName();

        xmlTreeBuilder.popStackToClose(token.asEndTag());

        assertEquals(0, root.numChildren());
        assertNull(root.getChild(0));
        assertTrue(child1Name.equals("child1"));
    }

    @Test
    public void testProcessComment() {
        String xmlString = "<root><!-- comment --></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), null);
        Element root = xmlDoc.getRootElement();

        Token token = xmlTreeBuilder.tokenize("<root><!-- comment --></root>").next();
        // comment
        assertTrue(token.type == Token.Type.Comment);

        Comment comment = (Comment) xmlTreeBuilder.insert(token);
        assertEquals("", comment.data());
        assertEquals(root.getChild(0).toString(), comment.toString());
    }

    @Test
    public void testProcessCharacter() {
        String xmlString = "<root>&#x1F600;</root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), null);
        Element root = xmlDoc.getRootElement();

        Token token = xmlTreeBuilder.tokenize("<root>&#x1F600;</root>").next();
        // character
        assertTrue(token.type == Token.Type.Character);

        Text textNode = (Text) xmlTreeBuilder.insert(token);
        assertEquals("\uD83C\uDC00", textNode.data().toString());
        assertEquals(root.getChild(0).toString(), textNode.toString());
    }

    @Test
    public void testProcessDoctype() {
        String xmlString = "<!DOCTYPE root SYSTEM \"root.dtd\">";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), null);
        Element root = xmlDoc.getRootElement();

        Token token = xmlTreeBuilder.tokenize("<!DOCTYPE root SYSTEM \"root.dtd\">").next();
        // doctype
        assertTrue(token.type == Token.Type.Doctype);

        DocumentType documentType = (DocumentType) xmlTreeBuilder.insert(token);
        assertEquals("root", documentType.tagName());
        assertEquals("root.dtd", documentType.systemId());
        assertEquals("root", documentType.publicId());
        assertNull(documentType.internalSubset());
        assertEquals(documentType, root.getFirstChild());
    }
}