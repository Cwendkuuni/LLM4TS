package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * Test cases for {@link Parser} class.
 */
public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        String html = "<html><head><title>Test Page</title></head>" +
                "<body><div><p>Hello World!</p></div></body></html>";
        parser = new Parser(html, "http://test.com/", false);
    }

    /**
     * Test constructor.
     */
    @Test
    public void testParserConstructor() {
        Parser parser = new Parser("", "", false);
        assertNotNull(parser);
    }

    /**
     * Test parse method.
     */
    @Test
    public void testParse() {
        Document doc = parser.parse();
        Element root = doc.rootElement();
        assertEquals("html", root.tagName());
        assertEquals("Test Page", root.head().title().text());
        assertEquals("Hello World!", root.body().get(0).get(0).text());
    }

    /**
     * Test parseBodyFragment method.
     */
    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<body><p>Hello World!</p>", "http://test.com/", false);
        Element root = doc.rootElement();
        assertEquals("html", root.tagName());
        assertEquals("", root.headTag().html());
        assertEquals("Hello World!", root.body().get(0).get(0).text());
    }

    /**
     * Test parseComment method.
     */
    @Test
    public void testParseComment() {
        parser.parseComment();
        Element comment = parser.last();
        assertEquals("comment", comment.tagName());
        assertEquals("Test Comment", comment.text());
    }

    /**
     * Test parseXmlDecl method.
     */
    @Test
    public void testParseXmlDecl() {
        parser.parseXmlDecl();
        Element xmlDeclaration = parser.last();
        assertEquals("xml-declaration", xmlDeclaration.tagName());
        assertEquals("version='1.0' encoding='UTF-8'", xmlDeclaration.attributes().toString());
    }

    /**
     * Test parseEndTag method.
     */
    @Test
    public void testParseEndTag() {
        parser.parseStartTag("div");

        parser.parseEndTag();
        Element div = parser.last();
        assertEquals("div", div.tagName());
        assertNull(parser.last());
    }

    /**
     * Test parseStartTag method.
     */
    @Test
    public void testParseStartTag() {
        parser.parseStartTag("html");
        Element html = parser.last();
        assertEquals("html", html.tagName());
        assertNull(parser.last());

        parser.parseStartTag("head");
        Element head = parser.last();
        assertEquals("head", head.tagName());
        assertEquals("http://test.com/", head.baseUri());
        assertNull(parser.last());

        parser.parseStartTag("title");
        Element title = parser.last();
        assertEquals("title", title.tagName());
        assertEquals("Test Page", title.text());
        assertNull(parser.last());

        parser.parseStartTag("body");
        Element body = parser.last();
        assertEquals("body", body.tagName());
        assertNull(parser.last());

        parser.parseStartTag("div");
        Element div = parser.last();
        assertEquals("div", div.tagName());
        assertNull(parser.last());

        parser.parseStartTag("p");
        Element p = parser.last();
        assertEquals("p", p.tagName());
        assertEquals("Hello World!", p.text());
        assertNull(parser.last());

        parser.parseStartTag("textarea");
        Element textarea = parser.last();
        assertEquals("textarea", textarea.tagName());
        assertNotNull(textarea.children());
        assertEquals(0, textarea.children().size());
    }

    /**
     * Test parseAttribute method.
     */
    @Test
    public void testParseAttribute() {
        parser.parseStartTag("a");
        parser.parseAttribute("href", "http://example.com");
        parser.parseEndTag();

        Element a = parser.last();
        Map<String, String> attributes = a.attributes();
        assertEquals("href", attributes.keySet().iterator().next());
        assertEquals("http://example.com", attributes.get("href"));
    }

    /**
     * Test parseTextNode method.
     */
    @Test
    public void testParseTextNode() {
        parser.parseTextNode("Hello World!");
        Element textNode = parser.last();
        assertEquals("text", textNode.tagName());
        assertEquals("Hello World!", textNode.text());
    }

    /**
     * Test parseCdata method.
     */
    @Test
    public void testParseCdata() {
        parser.parseCdata("<![CDATA[Hello World!]]>");
        Element cdata = parser.last();
        assertEquals("text", cdata.tagName());
        assertEquals("Hello World!", cdata.text());
    }

    /**
     * Test addChildToParent method.
     */
    @Test
    public void testAddChildToParent() {
        Element parent = new Element(tag("div"), "http://test.com/");
        Element child = new Element(tag("p"), "http://test.com/");
        Element result = parser.addChildToParent(child, false, parent);
        assertEquals(parent, result);
        assertEquals(1, parent.children().size());
        assertEquals(child, parent.children().get(0));
    }

    /**
     * Test addChildToParent with empty element.
     */
    @Test
    public void testAddChildToParentEmptyElement() {
        Element parent = new Element(tag("div"), "http://test.com/");
        Element child = new Element(tag("img"), "http://test.com/");
        Element result = parser.addChildToParent(child, true, parent);
        assertEquals(parent, result);
        assertEquals(1, parent.children().size());
        assertEquals(child, parent.children().get(0));
    }

    /**
     * Test stackHasValidParent method.
     */
    @Test
    public void testStackHasValidParent() {
        assertTrue(parser.stackHasValidParent(tag("html")));
        assertTrue(parser.stackHasValidParent(tag("body")));
        assertTrue(parser.stackHasValidParent(tag("div")));
        assertFalse(parser.stackHasValidParent(tag("p")));
    }

    /**
     * Test popStackToSuitableContainer method.
     */
    @Test
    public void testPopStackToSuitableContainer() {
        Element root = new Element(tag("html"), "http://test.com/");
        Element body = new Element(tag("body"), "http://test.com/");
        Element div = new Element(tag("div"), "http://test.com/");
        Element p = new Element(tag("p"), "http://test.com/");

        parser.stack.addAll(List.of(root, body, div));

        assertEquals(div, parser.popStackToSuitableContainer(tag("div")));
        assertEquals(body, parser.popStackToSuitableContainer(tag("body")));
        assertEquals(root, parser.popStackToSuitableContainer(tag("html")));
        assertNull(parser.popStackToSuitableContainer(tag("p")));
    }

    /**
     * Test popStackToClose method.
     */
    @Test
    public void testPopStackToClose() {
        Element root = new Element(tag("html"), "http://test.com/");
        Element body = new Element(tag("body"), "http://test.com/");
        Element div1 = new Element(tag("div"), "http://test.com/");
        Element div2 = new Element(tag("div"), "http://test.com/");
        Element p = new Element(tag("p"), "http://test.com/");

        parser.stack.addAll(List.of(root, body, div1, div2, div1, p));

        assertEquals(div1, parser.popStackToClose(tag("div")));
        assertEquals(div1, parser.popStackToClose(tag("div")));
        assertEquals(body, parser.popStackToClose(tag("body")));
        assertEquals(root, parser.popStackToClose(tag("html")));

        assertNull(parser.popStackToClose(tag("p")));
    }

    private Tag tag(String tagName) {
        return Tag.valueOf(tagName);
    }
}