package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xmlString = "<root><element attr=\"test\">Content</element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), "baseUri");

        Element root = xmlDoc.getRootElement();
        assertEquals("root", root.tagName());
        Element element = root.getChild(0);
        assertEquals("element", element.tagName());
        assertEquals("test", element.attributes().get("attr"));
        assertEquals("Content", element.text());
    }

    @Test
    public void testProcessStartTag() {
        String xmlString = "<root><element attr=\"test\"></element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), "baseUri");

        Element root = xmlDoc.getRootElement();
        Element element = root.getChild(0);

        // test processStartTag method for 'StartTag' token
        Token startTagToken = new Token.StartTag("element", null, null, null, null, null, null, null, null, null);
        xmlTreeBuilder.insert(startTagToken);

        assertEquals("element", element.tagName());
        assertEquals("baseUri", element.baseUri());
    }

    @Test
    public void testProcessEndTag() {
        String xmlString = "<root><element attr=\"test\"><subElement/></element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), "baseUri");

        Element root = xmlDoc.getRootElement();
        Element element = root.getChild(0);
        assertEquals(2, element.childNodeSize());

        // test processEndTag method for 'EndTag' token
        Token endTagToken = new Token.EndTag("element", null, null, null, null, null, null, null, null, null);
        xmlTreeBuilder.popStackToClose(endTagToken);
        assertEquals(1, element.childNodeSize());
    }

    @Test
    public void testProcessComment() {
        String xmlString = "<root><!-- comment --></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), "baseUri");

        Element root = xmlDoc.getRootElement();
        List<Node> commentNodes = root.childNodes();
        assertEquals(1, commentNodes.size());

        Comment comment = (Comment) commentNodes.get(0);
        assertEquals("comment", comment.getData());
    }

    @Test
    public void testProcessCharacter() {
        String xmlString = "<root>Content</root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), "baseUri");

        Element root = xmlDoc.getRootElement();
        List<Node> textNodes = root.childNodes();
        assertEquals(1, textNodes.size());

        TextNode textNode = (TextNode) textNodes.get(0);
        assertEquals("Content", textNode.text());
    }

    @Test
    public void testProcessDoctype() {
        String xmlString = "<!DOCTYPE root SYSTEM \"file:root.dtd\">";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), "baseUri");

        DocumentType documentTypeNode = xmlDoc.getDoctype();
        assertEquals("root", documentTypeNode.nodeName());
        assertEquals("file:root.dtd", documentTypeNode.systemId());
    }

    @Test(expected = AssertionError.class)
    public void testUnexpectedTokenType() {
        String xmlString = "<root><!-- comment --><!-- unexpected -->";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), "baseUri");

        Validate.fail("Unexpected test: this test case should throw AssertionError");
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<root><element attr=\"test\">Content</element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);

        Element root = (Element) nodes.get(0);
        assertEquals("root", root.tagName());
        Element element = (Element) root.getChild(0);
        assertEquals("element", element.tagName());
        assertEquals("test", element.attributes().get("attr"));
        assertEquals("Content", element.text());
    }
}