package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, null);
        assertNotNull(doc);
        assertEquals(2, doc.numberOfChildren());
        Element head = doc.head();
        assertEquals(1, head.numberOfChildren());
        Element title = head.child(0);
        assertEquals("Test", title.text());
        Element body = doc.body();
        assertEquals(1, body.numberOfChildren());
        Element p = body.child(0);
        assertEquals("Hello World", p.text());
    }

    @Test
    public void testParseFragment() {
        String html = "<p>Hello World</p>";
        Element context = Element.parse("<html><body></body></html>").body();
        List<Node> nodes = Parser.parseFragment(html, context, null);
        Node node = nodes.get(0);
        assertTrue(node instanceof Element);
        Element p = (Element) node;
        assertEquals("Hello World", p.text());
    }

    @Test
    public void testParseXmlFragment() {
        String xml = "<person><name>John Doe</name></person>";
        List<Node> nodes = Parser.parseXmlFragment(xml, null);
        Node node = nodes.get(0);
        assertTrue(node instanceof Element);
        Element person = (Element) node;
        assertEquals("person", person.tagName());
        Element name = person.child(0);
        assertEquals("John Doe", name.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, null);
        Element body = doc.body();
        assertTrue(body.child(0) instanceof Element);
        Element p = (Element) body.child(0);
        assertEquals("Hello World", p.text());
    }

    @Test
    public void testUnescapeEntities() {
        String input = "&lt;p&gt;Hello &amp; World&lt;/p&gt;";
        String expected = "<p>Hello & World</p>";
        String output = Parser.unescapeEntities(input, false);
        assertEquals(expected, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullInput() {
        Parser.parse(null, null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testParseWithEmptyInput() {
        Parser.parse("", null);
    }
}