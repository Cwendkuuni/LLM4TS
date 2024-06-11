package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Assert;
import org.junit.Test;

public class XmlTreeBuilderTest {
    @Test
    public void testDefaultSettings() {
        String xmlString = "<root><child>Hello World</child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(xmlString, "baseUri");
        Element root = xmlDoc.getElementById("root");
        Element child = root.getChild(0);
        Assert.assertEquals("Hello World", child.text());
    }

    @Test
    public void testProcessStartTag() {
        String xmlString = "<root><child></child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(xmlString, "baseUri");
        Element root = xmlDoc.getElementById("root");
        Element child = root.getChild(0);
        // Edge case: self-closing tag
        Assert.assertNotNull(child.tagName().isSelfClosing());
    }

    @Test
    public void testProcessEndTag() {
        String xmlString = "<root><child></child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(xmlString, "baseUri");
        Element root = xmlDoc.getElementById("root");
        Assert.assertEquals(2, root.childNodeSize());
        Element child = root.getChild(0);
        // Edge case: check if popStackToClose method works correctly
        xmlTreeBuilder.popStackToClose(new Token.EndTag("child"));
        Assert.assertEquals(1, root.childNodeSize());
    }

    @Test(expected = AssertionError.class)
    public void testUnexpectedTokenType() {
        String xmlString = "<root>"; // malformed XML with missing closing tag
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(xmlString, "baseUri");
    }

    @Test
    public void testInsertNode() {
        String xmlString = "<root><child><grandchild>Hello Grandchild</grandchild></child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(xmlString, "baseUri");
        Element root = xmlDoc.getElementById("root");
        Element child = root.getChild(0);
        Element grandchild = child.getChild(0);
        TextNode textNode = (TextNode) grandchild.child(0);
        Assert.assertEquals("Hello Grandchild", textNode.text());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<root><child>Fragment</child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Assert.assertEquals(2, nodes.size());
        Element root = (Element) nodes.get(0);
        Element child = (Element) nodes.get(1);
        Assert.assertEquals("Fragment", child.text());
    }

    // Complete JUnit 4 test file starts here
    
}