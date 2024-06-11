package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Doctype;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

/**
 * JUnit 4 test file for XmlTreeBuilder class.
 */
public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xmlString = "<root><element attr=\"value\">Hello World</element></root>";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com/");
        assertNotNull(xmlDoc);
        Element root = xmlDoc.rootElement();
        assertNotNull(root);
        List<Node> nodes = root.childNodes();
        assertTrue(nodes.size() == 1);
        Element element = (Element) nodes.get(0);
        assertNotNull(element);
        Attributes attributes = element.attributes();
        assertTrue(attributes.size() == 1);
        String attrValue = attributes.get("attr");
        assertTrue(attrValue != null && attrValue.equals("value"));
        TextNode textNode = (TextNode) element.childNodes().get(0);
        assertTrue(textNode.text().equals("Hello World"));
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<element attr=\"value\">Hello World</element>";
        List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, "http://example.com/", ParseErrorList.noTracking(), ParseSettings.preserveCase).get();
        Element element = (Element) nodes.get(0);
        assertNotNull(element);
        Attributes attributes = element.attributes();
        assertTrue(attributes.size() == 1);
        String attrValue = attributes.get("attr");
        assertTrue(attrValue != null && attrValue.equals("value"));
        TextNode textNode = (TextNode) element.childNodes().get(0);
        assertTrue(textNode.text().equals("Hello World"));
    }

    @Test
    public void testParseWithEmptyElement() {
        String xmlString = "<root><element/></root>";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com/");
        assertNotNull(xmlDoc);
        Element root = xmlDoc.rootElement();
        assertNotNull(root);
        List<Node> nodes = root.childNodes();
        assertTrue(nodes.size() == 1);
        Element emptyElement = (Element) nodes.get(0);
        assertNotNull(emptyElement);
        assertTrue(emptyElement.childNodes().size() == 0);
    }

    @Test
    public void testParseWithSelfClosingTag() {
        String xmlString = "<root><element/></root>";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com/");
        assertNotNull(xmlDoc);
        Element root = xmlDoc.rootElement();
        assertNotNull(root);
        List<Node> nodes = root.childNodes();
        assertTrue(nodes.size() == 1);
        Element emptyElement = (Element) nodes.get(0);
        assertNotNull(emptyElement);
        assertTrue(emptyElement.tagName().equals("element"));
        assertTrue(emptyElement.isTagClosed());
        assertTrue(emptyElement.childNodes().size() == 0);
    }

    @Test
    public void testParseWithComment() {
        String xmlString = "<root><!-- This is a comment --></root>";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com/");
        assertNotNull(xmlDoc);
        Element root = xmlDoc.rootElement();
        assertNotNull(root);
        List<Node> nodes = root.childNodes();
        assertTrue(nodes.size() == 1);
        Node commentNode = nodes.get(0);
        assertTrue(commentNode instanceof Comment);
        Comment comment = (Comment) commentNode;
        assertTrue(comment.getData().equals(" This is a comment "));
    }

    @Test
    public void testParseWithCDataSection() {
        String xmlString = "<root><![CDATA[This is a CData section]]></root>";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com/");
        assertNotNull(xmlDoc);
        Element root = xmlDoc.rootElement();
        assertNotNull(root);
        List<Node> nodes = root.childNodes();
        assertTrue(nodes.size() == 1);
        Node cdataNode = nodes.get(0);
        assertTrue(cdataNode instanceof CDataNode);
        CDataNode cdata = (CDataNode) cdataNode;
        assertTrue(cdata.getData().equals("This is a CData section"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTokenType() {
        String xmlString = "<root><![CDATA[This is a CData section]]></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        StringReader input = new StringReader(xmlString);
        Token token = xmlTreeBuilder.nextToken(input);
        xmlTreeBuilder.process(token);
        Validate.fail("Test should throw IllegalArgumentException for unexpected token type");
    }
}