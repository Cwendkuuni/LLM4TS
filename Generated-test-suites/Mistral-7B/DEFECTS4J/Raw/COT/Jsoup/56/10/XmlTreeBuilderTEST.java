package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.helper.Validate;
import org.jsoup.parser.ParseSettings;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseWithBaseUri() {
        String xmlString = "<xml><tag attr='value'></tag></xml>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(xmlString, "http://test.com");
        Element root = doc.rootElement();
        assertNotNull(root);
        assertEquals("xml", root.tagName());
        assertEquals("http://test.com", doc.baseUri().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse(null, "http://test.com");
        fail("Expected IllegalArgumentException but was not thrown.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullBaseUri() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<xml><tag attr='value'></tag></xml>";
        xmlTreeBuilder.parse(xmlString, null);
        fail("Expected IllegalArgumentException but was not thrown.");
    }

    @Test
    public void testInitialiseParse() {
        String xmlString = "<xml><tag attr='value'></tag></xml>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(xmlString, "http://test.com", errors, settings);
        assertEquals(ParseSettings.preserveCase, settings);
        assertEquals("http://test.com", doc.baseUri().toString());
        assertNotNull(doc);
    }

    @Test
    public void testProcessStartTag() {
        String xmlString = "<xml><tag attr='value'></tag></xml>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(xmlString, "http://test.com");
        Element tagElement = doc.select("tag").first();
        assertNotNull(tagElement);
        assertEquals("tag", tagElement.tagName());
    }

    @Test
    public void testProcessEndTag() {
        String xmlString = "<xml><tag><subtag></subtag></tag></xml>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(xmlString, "http://test.com");
        Element tagElement = doc.select("tag").first();
        assertEquals(1, tagElement.children().size());
        xmlTreeBuilder.popStackToClose(tagElement.tagName());
        assertEquals(0, tagElement.children().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessEndTagWithUnknownTag() {
        String xmlString = "<xml><tag></tag><unknown></unknown></xml>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(xmlString, "http://test.com");
        Element tagElement = doc.select("tag").first();
        Element unknownElement = doc.select("unknown").first();
        xmlTreeBuilder.popStackToClose(unknownElement.tagName());
        fail("Expected IllegalArgumentException but was not thrown.");
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = Jsoup.parse("<xml/>", "http://test.com");
        Element root = doc.rootElement();
        Node node = new Element("tag", "http://test.com");
        xmlTreeBuilder.insertNode(node);
        root.appendChild(node);
        assertEquals(1, root.children().size());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<tag attr='value'></tag>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parseFragment(xmlFragment, "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Element tagElement = doc.select("tag").first();
        assertNotNull(tagElement);
        assertEquals("tag", tagElement.tagName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFragmentWithNullInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(null, "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        fail("Expected IllegalArgumentException but was not thrown.");
    }
}