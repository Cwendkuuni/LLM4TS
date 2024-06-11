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
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        String xmlString = "<xml><tag1 attr1=\"value1\">Content</tag1></xml>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String baseUri = "http://example.com";

        Reader input = new StringReader(xmlString);
        Document document = xmlTreeBuilder.parse(input, baseUri);
        Element rootElement = document.getRootElement();
        assertEquals("xml", rootElement.tagName());
        assertEquals("tag1", rootElement.child(0).tagName());
        assertEquals("value1", rootElement.child(0).attr("attr1"));
        assertEquals("Content", rootElement.child(0).child(0).text());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String xmlString = "<xml><tag1 attr1=\"value1\">Content</tag1></xml>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String baseUri = "http://example.com";

        Document document = xmlTreeBuilder.parse(xmlString, baseUri);
        Element rootElement = document.getRootElement();
        assertEquals("xml", rootElement.tagName());
        assertEquals("tag1", rootElement.child(0).tagName());
        assertEquals("value1", rootElement.child(0).attr("attr1"));
        assertEquals("Content", rootElement.child(0).child(0).text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitialiseParseWithNullInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(null, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitialiseParseWithEmptyInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(new StringReader(""), "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullBaseUri() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse(new StringReader("<xml></xml>"), null);
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token startTag = new Token.StartTag("tag1", null, null, null, null, null, null, 1, 1, 1, 1, null, null);
        Element element = xmlTreeBuilder.insert(startTag);
        assertEquals("tag1", element.tagName());
        assertEquals("tag1", element.nodeName());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token endTag = new Token.EndTag("tag1", null);
        xmlTreeBuilder.popStackToClose(endTag);
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessEndTagWithNullEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.popStackToClose(null);
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlFragment = "<tag1 attr1=\"value1\">Content<tag2></tag2></tag1>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertEquals(3, nodes.size());
        Element rootElement = (Element) nodes.get(0);
        assertEquals("tag1", rootElement.tagName());
        assertEquals("tag2", rootElement.child(0).tagName());
        assertEquals("Content", rootElement.child(1).child(0).text());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token commentToken = new Token.Comment("<!-- This is a comment -->");
        xmlTreeBuilder.insert(commentToken);
        Comment comment = (Comment) xmlTreeBuilder.currentElement().child(0);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token characterToken = new Token.Character('A');
        xmlTreeBuilder.insert(characterToken);
        TextNode textNode = (TextNode) xmlTreeBuilder.currentElement().child(0);
        assertEquals("A", textNode.text());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token doctypeToken = new Token.Doctype("html", null, null);
        xmlTreeBuilder.insert(doctypeToken);
        DocumentType documentTypeNode = (DocumentType) xmlTreeBuilder.currentElement().child(0);
        assertEquals("html", documentTypeNode.tagName());
    }

    @Test
    public void testPopStackToCloseWithUnknownEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element1 = new Element("tag1");
        Element element2 = new Element("unknowntag");
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);
        Token endTag = new Token.EndTag("unknowntag", null);
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(element1, xmlTreeBuilder.stack.get(0));
    }
}