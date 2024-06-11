package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings.Syntax;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Tag;
import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    private ParseSettings settings;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        settings = ParseSettings.preserveCase;
    }

    @Test
    public void testDefaultSettings() {
        assertEquals(ParseSettings.preserveCase, xmlTreeBuilder.defaultSettings());
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        Reader input = new StringReader("<root><child></child></root>");
        String baseUri = "https://example.com";
        Document doc = xmlTreeBuilder.parse(input, baseUri);
        assertEquals("https://example.com", doc.baseUri());
        assertEquals(Syntax.xml, doc.outputSettings().syntax());
        assertEquals("<root>\n <child></child>\n</root>", doc.outerHtml());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<root><child></child></root>";
        String baseUri = "https://example.com";
        Document doc = xmlTreeBuilder.parse(input, baseUri);
        assertEquals("https://example.com", doc.baseUri());
        assertEquals(Syntax.xml, doc.outputSettings().syntax());
        assertEquals("<root>\n <child></child>\n</root>", doc.outerHtml());
    }

    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("<root><child></child></root>");
        String baseUri = "https://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        Element root = xmlTreeBuilder.currentElement();
        assertEquals("root", root.nodeName());
        assertTrue(root.childNodes().size() == 1);
        assertEquals("<root>\n <child></child>\n</root>", root.outerHtml());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("child", "foo");
        Element el = xmlTreeBuilder.insert(startTag);
        assertEquals("child", el.nodeName());
        assertEquals("<child foo></child>", el.outerHtml());
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("child");
        xmlTreeBuilder.popStackToClose(endTag);
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("test comment");
        xmlTreeBuilder.insert(commentToken);
        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Comment);
        assertEquals("<!--test comment-->", childNodes.get(0).outerHtml());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = new Token.Character("test character");
        xmlTreeBuilder.insert(characterToken);
        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertFalse(childNodes.get(0) instanceof CDataNode);
        assertEquals("test character", childNodes.get(0).outerHtml());
    }

    @Test
    public void testProcessCDataCharacter() {
        Token.Character characterToken = new Token.Character("test character");
        characterToken.setCData();
        xmlTreeBuilder.insert(characterToken);
        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof CDataNode);
        assertEquals("test character", childNodes.get(0).outerHtml());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        doctype.setPublicIdentifier("test public");
        doctype.setSystemIdentifier("test system");
        doctype.setPubSysKey("TEST");
        xmlTreeBuilder.insert(doctype);
        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof DocumentType);
        DocumentType docType = (DocumentType) childNodes.get(0);
        assertEquals("html", docType.nodeName());
        assertEquals("test public", docType.getPublicIdentifier());
        assertEquals("test system", docType.getSystemIdentifier());
        assertEquals("TEST", docType.getPubSysKey());
    }

    @Test
    public void testPopStackToClose() {
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("child"), "https://example.com"));
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("child"), "https://example.com"));
        Token.EndTag endTag = new Token.EndTag("child");
        xmlTreeBuilder.popStackToClose(endTag);
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<child>test</child>";
        String baseUri = "https://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        List<Node> childNodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Element);
        Element child = (Element) childNodes.get(0);
        assertEquals("child", child.nodeName());
        assertEquals("test", child.text());
    }
}