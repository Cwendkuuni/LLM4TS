package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Test class for Parser.java
 */
public class ParserTest {

    /**
     * Test constructor
     */
    @Test
    public void testParserConstructor() {
        String html = "<html><head><title>Test</title></head><body></body></html>";
        String baseUri = "http://test.com";

        Parser parser = Parser.parse(html, baseUri);
        assertNotNull(parser);
        assertTrue(parser instanceof Parser);
        assertEquals(html, parser.tq.source());
        assertEquals(baseUri, parser.baseUri);
        assertEquals(1, parser.stack.size());
        assertEquals(htmlTag, parser.stack.getLast().tag());
    }

    /**
     * Test parse method
     */
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body></body></html>";
        String baseUri = "http://test.com";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertEquals(htmlTag, doc.tag());
        assertEquals(1, doc.childNodes().size());
        assertEquals(headTag, doc.head().tag());
        assertEquals(1, doc.head().childNodes().size());
        assertEquals(titleTag, doc.head().childNodes().get(0).tag());
        assertEquals(1, doc.body().childNodes().size());
    }

    /**
     * Test parseBodyFragment method
     */
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><p>Test</p></body>";
        String baseUri = "http://test.com";

        Parser parser = new Parser(bodyHtml, baseUri, true);
        Document doc = parser.parse();

        assertEquals(htmlTag, doc.tag());
        assertEquals(1, doc.childNodes().size());
        assertEquals(headTag, doc.head(), null);
        assertEquals(1, doc.body().childNodes().size());
        assertEquals(tag("p"), doc.body().childNodes().get(0));
    }

    /**
     * Test parseComment method
     */
    @Test
    public void testParseComment() {
        String html = "<html><!-- Test Comment -->";
        String baseUri = "http://test.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        Element comment = doc.head().childNodes().get(0);
        assertEquals(Comment.class, comment.getClass());
        assertEquals("Test Comment", comment.text());
    }

    /**
     * Test parseCdata method
     */
    @Test
    public void testParseCdata() {
        String html = "<html><![CDATA[Test CDATA]]>";
        String baseUri = "http://test.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        Element cdata = doc.head().childNodes().get(0);
        assertEquals(DataNode.class, cdata.getClass());
        assertEquals("Test CDATA", cdata.text());
    }

    /**
     * Test parseXmlDecl method
     */
    @Test
    public void testParseXmlDecl() {
        String html = "<html <?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        String baseUri = "http://test.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        Element xmlDeclaration = doc.head().childNodes().get(0);
        assertEquals(XmlDeclaration.class, xmlDeclaration.getClass());
    }

    /**
     * Test parseStartTag method
     */
    @Test
    public void testParseStartTag() {
        String html = "<html><body><p id=\"test\">Test</p></body></html>";
        String baseUri = "http://test.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        Element p = doc.body().childNodes().get(0);
        assertEquals(tag("p"), p.tag());
        assertEquals("test", p.id());
        assertEquals("Test", p.text());
    }

    /**
     * Test parseEndTag method
     */
    @Test
    public void testParseEndTag() {
        String html = "<html><body><p>Test</p></body></html>";
        String baseUri = "http://test.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertEquals(1, doc.body().childNodes().size());
        Element p = doc.body().childNodes().get(0);
        parser.parseEndTag();
        assertEquals(0, doc.body().childNodes().size());
    }

    /**
     * Test parseAttribute method
     */
    @Test
    public void testParseAttribute() {
        String html = "<html><body><p id=\"test\" class=\"testClass\">Test</p></body></html>";
        String baseUri = "http://test.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        Element p = doc.body().childNodes().get(0);
        List<Attribute> attributes = p.attributes();
        assertEquals(2, attributes.size());
        Attribute id = attributes.get(0);
        assertEquals("id", id.key());
        assertEquals("test", id.value());
        Attribute classAttr = attributes.get(1);
        assertEquals("class", classAttr.key());
        assertEquals("testClass", classAttr.value());
    }

    /**
     * Test parseTextNode method
     */
    @Test
    public void testParseTextNode() {
        String html = "<html><body><p>Test</p></body></html>";
        String baseUri = "http://test.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        Element p = doc.body().childNodes().get(0);
        TextNode textNode = (TextNode) p.childNodes().get(0);
        assertEquals("Test", textNode.text());
    }

    /**
     * Test parseComment with invalid comment
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseCommentInvalid() {
        String html = "<html><!-- Test-->";
        String baseUri = "http://test.com";
        Parser parser = new Parser(html, baseUri, false);
        parser.parseComment();
    }

    /**
     * Test parseCdata with invalid CDATA
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseCdataInvalid() {
        String html = "<html><![CDATA[Test]]>";
        String baseUri = "http://test.com";
        Parser parser = new Parser(html, baseUri, false);
        parser.parseCdata();
    }

    /**
     * Test parseAttribute with invalid key
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseAttributeInvalidKey() {
        String html = "<html><body><p x=\"Test\"></p></body></html>";
        String baseUri = "http://test.com";
        Parser parser = new Parser(html, baseUri, false);
        parser.parseStartTag();
    }

    /**
     * Test parseAttribute with invalid value
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseAttributeInvalidValue() {
        String html = "<html><body><p id=\"test\" onsubmit=\"return false;\"></p></body></html>";
        String baseUri = "http://test.com";
        Parser parser = new Parser(html, baseUri, false);
        parser.parseStartTag();
        parser.parseAttribute();
    }

    /**
     * Test parseEmptyElement method
     */
    @Test
    public void testParseEmptyElement() {
        String html = "<html><body><img src=\"test.jpg\"/></body></html>";
        String baseUri = "http://test.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        assertEquals(1, doc.body().childNodes().size());
        Element img = doc.body().childNodes().get(0);
        assertTrue(img.isEmptyElement());
    }

    /**
     * Test parseTextarea tag
     */
    @Test
    public void testParseTextarea() {
        String html = "<html><body><textarea id=\"test\">Test</textarea></body></html>";
        String baseUri = "http://test.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        Element textarea = doc.body().childNodes().get(0);
        assertTrue(textarea.tag().isData());
        assertEquals("Test", textarea.text());
    }

    /**
     * Test parseBase tag
     */
    @Test
    public void testParseBaseTag() {
        String html = "<html><body><base href=\"http://new.test.com\"/><a href=\"test.html\">Test</a></body></html>";
        String baseUri = "http://test.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        Element base = doc.body().childNodes().get(0);
        assertEquals("new.test.com", base.absUrl("href"));
        assertEquals("http://new.test.com/test.html", doc.createElement("a").absUrl("href"));
    }

    /**
     * Test parseInvalidTag
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidTag() {
        String html = "<html><body><invalidTag/></body></html>";
        String baseUri = "http://test.com";
        Parser parser = new Parser(html, baseUri, false);
        parser.parse();
    }

    /**
     * Test parseInvalidTagWithEmptyStack
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidTagWithEmptyStack() {
        String html = "<html><body></body></html>";
        String baseUri = "http://test.com";
        Parser parser = new Parser(html, baseUri, false);
        parser.parse();
    }

    /**
     * Test parseEmptyTag
     */
    @Test
    public void testParseEmptyTag() {
        String html = "<html><body><img src=\"test.jpg\"></body></html>";
        String baseUri = "http://test.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        Element img = doc.body().childNodes().get(0);
        assertEquals(tag("img"), img.tag());
        assertTrue(img.isEmptyElement());
    }

    /**
     * Test parseTagWithInvalidParent
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseTagWithInvalidParent() {
        String html = "<html><body><html></body></html>";
        String baseUri = "http://test.com";
        Parser parser = new Parser(html, baseUri, false);
        parser.parse();
    }

    /**
     * Test parseTagWithValidParent
     */
    @Test
    public void testParseTagWithValidParent() {
        String html = "<html><body><div><p>Test</p></div></body></html>";
        String baseUri = "http://test.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        Element div = doc.body().childNodes().get(0);
        Element p = div.childNodes().get(0);
        assertEquals(tag("div"), div.tag());
        assertEquals(tag("p"), p.tag());
    }

    /**
     * Test parseTagWithImplicitParent
     */
    @Test
    public void testParseTagWithImplicitParent() {
        String html = "<html><body><script src=\"test.js\"></script></body></html>";
        String baseUri = "http://test.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        Element script = doc.body().childNodes().get(0);
        assertEquals(tag("script"), script.tag());

        Element parent = parser.popStackToSuitableContainer(script.tag());
        assertEquals(htmlTag, parent.tag());
    }

    /**
     * Test parseTagWithInvalidImplicitParent
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseTagWithInvalidImplicitParent() {
        String html = "<html><body><script src=\"test.js\"></body></html>";
        String baseUri = "http://test.com";
        Parser parser = new Parser(html, baseUri, false);
        parser.parse();
    }

    /**
     * Test parseTagWithInvalidParentStack
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseTagWithInvalidParentStack() {
        String html = "<html><body><html><p>Test</p></html></body></html>";
        String baseUri = "http://test.com";
        Parser parser = new Parser(html, baseUri, false);
        parser.parse();
    }

    /**
     * Test parseTagWithValidParentStack
     */
    @Test
    public void testParseTagWithValidParentStack() {
        String html = "<html><body><div><p>Test</p></div></body></html>";
        String baseUri = "http://test.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        Element div = doc.body().childNodes().get(0);
        Element p = div.childNodes().get(0);
        assertEquals(tag("div"), div.tag());
        assertEquals(tag("p"), p.tag());

        Element parent = parser.popStackToSuitableContainer(p.tag());
        assertEquals(tag("div"), parent.tag());
    }

    private Tag tag(String tagName) {
        return Tag.valueOf(tagName);
    }

    private static void assertEquals(Object expected, Object actual) {
        assertEquals(expected.getClass(), actual.getClass());
        assertEquals(expected, actual);
    }

    private static void assertEquals(String expected, String actual) {
        assertEquals(expected, actual);
    }

    private static void assertEquals(int expected, int actual) {
        assertEquals(expected, actual);
    }

    private static void assertEquals(boolean expected, boolean actual) {
        assertEquals(expected, actual);
    }

    private static void assertEquals(List<?> expected, List<?> actual) {
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }
}