package org.junit.test;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class XmlTreeBuilderTest {
    List<Node> stack;

    @Before
    public void setup() {
        stack = new ArrayList<>();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = XmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParse() {
        String xml = "<root><child>Hello</child></root>";
        Document doc = XmlTreeBuilder.parse(xml, "http://example.com");

        Element root = doc.getElementById("root");
        Element child = root.getChild(0);

        assertEquals("Hello", child.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse(null, "http://example.com");
    }

    @Test
    public void testEmptyInput() {
        Document doc = XmlTreeBuilder.parse("", "http://example.com");
        assertEquals(0, doc.childNodes().size());
    }

    @Test
    public void testPopStackToClose() {
        String xml = "<root><child><grandchild></grandchild></child></root>";
        Document doc = XmlTreeBuilder.parse(xml, "http://example.com");

        Element root = doc.getElementById("root");
        Element child = root.getChild(0);
        Element grandchild = child.getChild(0);

        XmlTreeBuilder.popStackToClose(new Token.EndTag("child"));
        assertEquals(1, stack.size());
        assertEquals(grandchild, stack.get(0));
    }

    @Test
    public void testParseFragment() {
        String xml = "<root><child>Hello</child></root>";
        List<Node> nodes = XmlTreeBuilder.parseFragment(xml, "http://example.com", new ParseErrorList(), ParseSettings.preserveCase);

        assertEquals(2, nodes.size());
        Element root = (Element) nodes.get(0);
        Element child = (Element) nodes.get(1);

        assertEquals("Hello", child.text());
    }
}