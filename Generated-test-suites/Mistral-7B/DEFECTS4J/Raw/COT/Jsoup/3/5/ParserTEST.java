package org.junit.tests;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for Parser.
 */
public class ParserTest {
    @Test
    public void testParser_parse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, "http://test.com/");
        Document doc = parser.parse();

        assertEquals("Test", doc.title());
        assertEquals("Hello World", doc.body().text());
        assertEquals("http://test.com/", doc.baseUri());
    }

    @Test
    public void testParser_parseBodyFragment() {
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        Parser parser = new Parser(bodyHtml, "http://test.com/");
        Document doc = parser.parse();

        assertEquals("", doc.head().text());
        assertEquals("Hello World", doc.body().text());
        assertEquals("http://test.com/", doc.baseUri());
    }

    @Test
    public void testParser_parseEmptyInput() {
        Parser parser = new Parser("", "http://test.com/");
        Document doc = parser.parse();
        assertEquals("", doc.body().text());
        assertNull(doc.title());
        assertEquals("http://test.com/", doc.baseUri());
    }

    @Test
    public void testParser_parseInvalidInput() {
        Parser parser = new Parser("Invalid Input", "http://test.com/");
        try {
            parser.parse();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParser_parseComment() {
        String html = "<html><!-- Test --></html>";
        Parser parser = new Parser(html, "http://test.com/");
        Document doc = parser.parse();

        Element comment = doc.body().child(0);
        assertEquals("Test", comment.text());
    }

    @Test
    public void testParser_parseCdata() {
        String html = "<html><![CDATA[Test]]></html>";
        Parser parser = new Parser(html, "http://test.com/");
        Document doc = parser.parse();

        TextNode textNode = (TextNode) doc.body().child(0);
        assertEquals("Test", textNode.text());
    }

    @Test
    public void testParser_parseXmlDecl() {
        String html = "<html xmlns:foo=\"bar\">";
        Parser parser = new Parser(html, "http://test.com/");
        Document doc = parser.parse();

        XmlDeclaration decl = (XmlDeclaration) doc.body().child(0);
        assertNotNull(decl);
        assertEquals("http://test.com/", decl.namespace());
        assertTrue(decl.isStandalone());
    }

    @Test
    public void testParser_parseEndTag() {
        String html = "<html><body><p>Test</p></body></html>";
        Parser parser = new Parser(html, "http://test.com/");
        Document doc = parser.parse();

        Element p = doc.body().child(0);
        assertEquals("p", p.tagName());
        assertEquals(1, p.childSize());
        assertNull(p.child(0));
    }

    @Test
    public void testParser_parseStartTag() {
        String html = "<html><body><p><b>Test</b></p></body></html>";
        Parser parser = new Parser(html, "http://test.com/");
        Document doc = parser.parse();

        Element p = doc.body().child(0);
        assertEquals("p", p.tagName());
        assertEquals(1, p.childSize());
        Element b = p.child(0);
        assertEquals("b", b.tagName());
        assertEquals("Test", b.text());
    }

    @Test
    public void testParser_parseTextNode() {
        String html = "<html><body><p>Test</p></body></html>";
        Parser parser = new Parser(html, "http://test.com/");
        Document doc = parser.parse();

        TextNode textNode = (TextNode) doc.body().child(1);
        assertEquals("Test", textNode.text());
    }

    @Test
    public void testParser_parseUnknownTag() {
        String html = "<html><body><zoo></zoo></body></html>";
        Parser parser = new Parser(html, "http://test.com/");
        Document doc = parser.parse();

        Element zoo = doc.body().child(0);
        assertEquals("zoo", zoo.tagName());
        assertNull(zoo.text());
    }

    @Test
    public void testParser_parseSelfClosingTag() {
        String html = "<html><body><img src=\"test.jpg\"/></body></html>";
        Parser parser = new Parser(html, "http://test.com/");
        Document doc = parser.parse();

        Element img = doc.body().child(0);
        assertEquals("img", img.tagName());
        assertEquals(0, img.childSize());
        assertTrue(img.isSelfClosingTag());
    }

    @Test
    public void testParser_parseBaseTag() {
        String html = "<html><head><base href=\"/subdir\"/><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, "http://test.com/");
        Document doc = parser.parse();

        assertEquals("/subdir", doc.baseUri());
        assertEquals("Test", doc.title().text());
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParser_parseScriptTag() {
        String html = "<html><body><script type=\"text/javascript\">var test = \"Hello World\";</script></body></html>";
        Parser parser = new Parser(html, "http://test.com/");
        Document doc = parser.parse();

        DataNode scriptNode = (DataNode) doc.body().child(0);
        assertEquals("script", scriptNode.tagName());
        assertEquals("text/javascript", scriptNode.attributes().get("type"));
        assertEquals("var test = \"Hello World\";", scriptNode.text());
    }

    @Test
    public void testParser_parseEmptyElement() {
        String html = "<html><body><img src=\"test.jpg\" /></body></html>";
        Parser parser = new Parser(html, "http://test.com/");
        Document doc = parser.parse();

        Element img = doc.body().child(0);
        assertEquals("img", img.tagName());
        assertEquals(0, img.childSize());
        assertTrue(img.isSelfClosingTag());
        assertTrue(StringUtils.isBlank(img.text()));
    }

    @Test
    public void testParser_parseErrorHandling() {
        String html = "<html><head><title>Test</title></head><body><script>throw new Error('Test Error');</script></body></html>";
        Parser parser = new Parser(html, "http://test.com/");
        Document doc = parser.parse();

        try {
            doc.body().child(1).text();
            fail("Expected java.lang.Error");
        } catch (Error e) {
            assertEquals("Test Error", e.getMessage());
        }
    }

    @Test
    public void testParser_parseMissingImplicitParent() {
        String html = "<html><body><img src=\"test.jpg\"/></body></html>";
        Parser parser = new Parser(html, "http://test.com/");
        Document doc = parser.parse();

        Element img = doc.body().child(0);
        assertEquals("body", img.parent().tagName());

        Element div = doc.createElement("div");
        doc.body().appendChild(div);
        Element newImg = doc.createElement("img");
        newImg.tagName("img");
        newImg.addAttribute("src", "test2.jpg");
        div.appendChild(newImg);

        assertEquals("div", img.parent().tagName());
    }

    @Test
    public void testParser_parseInvalidParent() {
        String html = "<html><body><h1>Test</h1><script>test();</script></body></html>";
        Parser parser = new Parser(html, "http://test.com/");
        Document doc = parser.parse();

        Element h1 = doc.body().child(0);
        Element script = doc.body().child(1);

        assertFalse(h1.canContain(script.tag()));
    }

    @Test
    public void testParser_parseNestedElements() {
        String html = "<html><body><div><h1>Test</h1><p>Test</p></div></body></html>";
        Parser parser = new Parser(html, "http://test.com/");
        Document doc = parser.parse();

        Element div = doc.body().child(0);
        Element h1 = div.child(0);
        Element p = div.child(1);

        assertEquals("div", div.tagName());
        assertEquals("h1", h1.tagName());
        assertEquals("p", p.tagName());
        assertEquals("Test", h1.text());
        assertEquals("Test", p.text());
    }

    @Test
    public void testParser_parseNestedElementsWithImplicitParents() {
        String html = "<html><body><p><b>Test</b></p></body></html>";
        Parser parser = new Parser(html, "http://test.com/");
        Document doc = parser.parse();

        Element p = doc.body().child(0);
        Element b = p.child(0);

        assertEquals("p", p.tagName());
        assertEquals("b", b.tagName());
        assertEquals("Test", b.text());

        Element div = doc.createElement("div");
        doc.body().appendChild(div);
        Element newP = doc.createElement("p");
        newP.tagName("p");
        newP.appendChild(doc.createElement("b").setText("Test2"));
        div.appendChild(newP);

        assertEquals("div", p.parent().tagName());
    }
}