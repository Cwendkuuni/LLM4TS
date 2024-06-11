package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.*;

import java.util.*;

public class ParserTest {

    private Document parseHtml(String html, String baseUri) {
        Parser parser = new Parser(html, baseUri, false);
        return parser.parse();
    }

    @Test
    public void testParseEmptyHtml() {
        Document doc = parseHtml("", "");
        assertTrue(doc.outerHtml().isEmpty());
        assertTrue(doc.body().outerHtml().isEmpty());
        assertTrue(doc.head().outerHtml().isEmpty());
    }

    @Test
    public void testParseHtmlWithSingleTag() {
        Document doc = parseHtml("<html></html>", "");
        assertEquals(htmlTag, doc.tagName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testParseHtmlWithSingleTagAndSelfClosing() {
        Document doc = parseHtml("<html/>", "");
        assertEquals(htmlTag, doc.tagName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testParseHtmlWithSingleTagAttributes() {
        Document doc = parseHtml("<html id='test' lang='en'></html>", "");
        assertEquals("test", doc.id());
        assertEquals("en", doc.lang());
    }

    @Test
    public void testParseHtmlWithMultipleTags() {
        Document doc = parseHtml("<html><head><title>Test</title></head><body></body></html>", "");
        assertEquals("Test", doc.title().text());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testParseHtmlWithTextNode() {
        Document doc = parseHtml("<html><body>Test</body></html>", "");
        assertEquals("Test", doc.body().text());
    }

    @Test
    public void testParseHtmlWithScriptTag() {
        Document doc = parseHtml("<html><body><script src='test.js'></script></body></html>", "");
        Element script = doc.body().child(0);
        assertEquals("test.js", script.absUrl("src"));
    }

    @Test
    public void testParseHtmlWithBaseTag() {
        Document doc = parseHtml("<html><head><base href='/test'></base></head><body></body></html>", "");
        assertEquals("/test", doc.baseUri());
        assertEquals("/test", doc.head().baseUri());
        assertEquals("/test", doc.body().baseUri());
    }

    @Test
    public void testParseHtmlWithComment() {
        Document doc = parseHtml("<html><!-- Test --></html>", "");
        assertTrue(doc.body().text().contains("Test"));
    }

    @Test
    public void testParseHtmlWithCdata() {
        Document doc = parseHtml("<html><script><![CDATA[console.log('Test');]]></script></html>", "");
        Element script = doc.body().child(0);
        String content = script.text();
        assertTrue(content.startsWith("console.log('Test');"));
    }

    @Test
    public void testParseHtmlWithEmptyBodyFragment() {
        Document doc = parseHtml("", "", true);
        assertNotNull(doc.body());
        assertTrue(doc.body().outerHtml().isEmpty());
    }

    @Test
    public void testParseHtmlWithBodyFragment() {
        Document doc = parseHtml("<html><body><p>Test</p></body></html>", "", true);
        assertEquals("Test", doc.body().text());
    }

    @Test
    public void testParseHtmlWithImplicitParent() {
        Document doc = parseHtml("<html><body><img src='test.png'></body></html>", "");
        Element img = doc.body().child(0);
        assertEquals("img", img.tagName());
        assertEquals("test.png", img.absUrl("src"));

        Element div = new Element(Tag.valueOf("div"), "", null);
        div.appendChild(img);
        Element parent = addChildToParent(div, false);
        assertEquals("div", parent.tagName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidHtml() {
        parseHtml("</html>", "");
    }

    private Element addChildToParent(Element child, boolean isEmptyElement) {
        Parser parser = new Parser("", "", false);
        parser.stack = parser.stack;
        Element parent = parser.popStackToSuitableContainer(child.tag());
        assertEquals(parent, parser.addChildToParent(child, isEmptyElement));
        return parent;
    }
}