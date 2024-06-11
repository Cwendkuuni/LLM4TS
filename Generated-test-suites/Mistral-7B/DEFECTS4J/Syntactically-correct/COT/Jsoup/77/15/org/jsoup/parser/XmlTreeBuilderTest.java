package org.jsoup.parser;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

/**
 * Test class for XmlTreeBuilder
 *
 * @author YourName
 */
public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseWithReader() {
        String xml = "<root><element attr='value'><subElement/></element></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader(xml), "http://example.com");
        assertNotNull(doc);
        assertEquals(1, doc.childNodeSize());
        Element root = doc.element("root");
        assertEquals(1, root.childNodeSize());
        Element element = root.element("element");
        assertEquals(1, element.childNodeSize());
        assertTrue(element.hasAttrSpecific("attr", "value"));
        assertNull(element.childNode(0));
    }

    @Test
    public void testParseWithString() {
        String xml = "<root><element attr='value'><subElement/></element></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(xml, "http://example.com");
        assertNotNull(doc);
        assertEquals(1, doc.childNodeSize());
        Element root = doc.element("root");
        assertEquals(1, root.childNodeSize());
        Element element = root.element("element");
        assertEquals(1, element.childNodeSize());
        assertTrue(element.hasAttrSpecific("attr", "value"));
        assertNull(element.childNode(0));
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Reader input = new StringReader("<root><element attr='value'></element></root>");
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        builder.initialiseParse(input, baseUri, errors, settings);
        assertEquals(baseUri, doc.baseUri());
        assertTrue(stack.contains(doc));
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token startTag = new Token.StartTag("element", null, null, null, null, null, null, null, null, null, null);
        Element el = builder.insert(startTag);
        assertNotNull(el);
        assertEquals("element", el.tagName());
        assertTrue(el.hasAttrSpecific("attr", "value"));
        assertFalse(el.isSelfClosing());
        assertTrue(stack.contains(el));
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token endTag = new Token.EndTag("element");
        builder.popStackToClose(endTag);
        Element element = stack.get(stack.size() - 1);
        assertTrue(element.nodeName().equals("element"));
        assertTrue(stack.size() == stack.size() - 1);
    }

    @Test(expected = AssertionError.class)
    public void testProcessUnexpectedTokenType() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token(5); // unexpected token type
        builder.process(token);
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xmlFragment = "<root><element attr='value'><subElement/></element></root>";
        List<Node> nodes = builder.parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Element root = (Element) nodes.get(0);
        assertEquals(1, root.childNodeSize());
        Element element = root.element("element");
        assertEquals(1, element.childNodeSize());
        assertTrue(element.hasAttrSpecific("attr", "value"));
        assertNull(element.childNode(0));
    }

    @Test(expected = AssertionError.class)
    public void testInvalidTokenData() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token characterToken = new Token.Character("!");
        builder.insert(characterToken);
        Validate.fail("Unexpected token type: " + characterToken.type);
    }
}