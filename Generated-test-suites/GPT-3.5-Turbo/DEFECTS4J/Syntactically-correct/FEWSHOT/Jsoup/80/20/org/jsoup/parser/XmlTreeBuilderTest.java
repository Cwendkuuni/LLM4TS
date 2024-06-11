package org.jsoup.parser;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() throws IOException {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        StringReader reader = new StringReader("<root><child>data</child></root>");
        Document doc = xmlTreeBuilder.parse(reader, "http://example.com");
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("<root>\n <child>\n  data\n </child>\n</root>\n", doc.html());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse("<root><child>data</child></root>", "http://example.com");
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("<root>\n <child>\n  data\n </child>\n</root>\n", doc.html());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        StringReader reader = new StringReader("<root><child>data</child></root>");
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(reader, "http://example.com", errors, settings);
        assertEquals("http://example.com", xmlTreeBuilder.baseUri);
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("child", "http://example.com", StringUtil.parserConnection(settings), "data", true);
        Element element = xmlTreeBuilder.insert(startTag);
        assertEquals("child", element.tagName());
        assertEquals("http://example.com", element.baseUri());
        assertTrue(element.isKnownTag());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("child");
        xmlTreeBuilder.stack.add(xmlTreeBuilder.doc.createElement("child"));
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("comment");
        xmlTreeBuilder.insert(comment);
        assertEquals("comment", xmlTreeBuilder.currentElement().childNode(0).toString());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("data");
        xmlTreeBuilder.insert(character);
        assertEquals("data", xmlTreeBuilder.currentElement().childNode(0).toString());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "publicId", "systemId");
        xmlTreeBuilder.insert(doctype);
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">", xmlTreeBuilder.doc.childNodes().get(0).toString());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = xmlTreeBuilder.doc.createElement("child");
        xmlTreeBuilder.insertNode(element);
        assertEquals("child", xmlTreeBuilder.currentElement().childNode(0).nodeName());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<child>data</child>";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "http://example.com", errors, settings);
        assertEquals(1, nodes.size());
        assertEquals("<child>\n data\n</child>\n", nodes.get(0).toString());
    }

}