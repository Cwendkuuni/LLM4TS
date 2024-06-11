package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * JUnit test cases for Parser class
 */
public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://www.example.com");
        assertNotNull(doc);
        Element root = doc.body();
        Element title = root.head();
        assertNotNull(title);
        String titleText = title.text();
        assertTrue(titleText.equals("Test"));
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://www.example.com");
        assertNotNull(doc);
        Element root = doc.body();
        Element h1 = root.child(0);
        assertNotNull(h1);
        String h1Text = h1.text();
        assertTrue(h1Text.equals("Hello World"));
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --></html>";
        Parser parser = new Parser(html, null, false);
        Comment comment = parser.parseComment();
        assertNotNull(comment);
        String commentText = comment.data();
        assertTrue(commentText.equals(" This is a comment "));
    }

    @Test
    public void testParseCdata() {
        String html = "<html><![CDATA[This is CDATA]]><html>";
        Parser parser = new Parser(html, null, false);
        TextNode cdata = parser.parseCdata();
        assertNotNull(cdata);
        String cdataText = cdata.text();
        assertTrue(cdataText.equals("This is CDATA"));
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head></head><body></body></html>";
        Parser parser = new Parser(html, null, false);
        XmlDeclaration xmlDecl = parser.parseXmlDecl();
        assertNotNull(xmlDecl);
        String xmlDeclData = xmlDecl.data();
        assertTrue(xmlDeclData.contains("xmlns"));
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><div><p>Hello World</p></div></body></html>";
        Parser parser = new Parser(html, null, false);
        parser.parse();
        Element div = parser.last().child(0);
        parser.parseEndTag();
        Element p = div.child(0);
        assertTrue(p.parent() == null);
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><div><p>Hello World</p></div></body></html>";
        Parser parser = new Parser(html, null, false);
        parser.parse();
        Element div = parser.last().child(0);
        parser.parseStartTag();
        Element p = parser.last();
        assertNotNull(p);
        assertTrue(p.tagName().equals("p"));
    }

    @Test
    public void testParseAttribute() {
        String html = "<html><body><div id=\"test\"></div></body></html>";
        Parser parser = new Parser(html, null, false);
        Attribute idAttr = parser.last().attributes().get("id");
        assertNotNull(idAttr);
        assertTrue(idAttr.value().equals("test"));
    }

    @Test(expected = AssertionError.class)
    public void testParseInvalidStartTag() {
        String html = "<html><body><div<p></body></html>";
        Parser parser = new Parser(html, null, false);
        parser.parse();
        // this line should throw an AssertionError
        parser.parseStartTag();
    }

    @Test(expected = AssertionError.class)
    public void testParseInvalidEndTag() {
        String html = "<html><body><div><p>Hello World</p></div></body></html>";
        Parser parser = new Parser(html, null, false);
        parser.parse();
        parser.parseEndTag();
        parser.parseEndTag();
        // this line should throw an AssertionError
        parser.parseEndTag();
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Parser parser = new Parser(html, null, false);
        parser.parse();
        TextNode textNode = (TextNode) parser.last().child(0);
        assertNotNull(textNode);
        String text = textNode.text();
        assertTrue(text.equals("Hello World"));
    }

    @Test
    public void testParseEmptyElement() {
        String html = "<html><body><img src=\"test.jpg\"></body></html>";
        Parser parser = new Parser(html, null, false);
        parser.parse();
        Element img = parser.last();
        assertTrue(img.tagName().equals("img"));
        assertTrue(img.childNodes().size() == 0);
    }

    @Test
    public void testParseBaseTag() {
        String html = "<html><head><base href=\"/test/\"></head><body><a href=\"about.html\"></a></body></html>";
        Parser parser = new Parser(html, null, false);
        parser.parse();
        Document doc = parser.doc;
        String baseUri = doc.baseUri();
        assertTrue(baseUri.equals("/test/"));
        String href = doc.createElement("a").attr("href");
        assertTrue(href.equals("about.html"));
    }

    @Test
    public void testParseImplicitParent() {
        String html = "<html><body><script src=\"test.js\"></script></body></html>";
        Parser parser = new Parser(html, null, false);
        parser.parse();
        Element script = parser.last();
        Element parent = parser.popStackToSuitableContainer(script.tag());
        assertTrue(parent != null);
        Element body = parent;
        while ((parent = parser.popStackToSuitableContainer(parent.tag())) != null) {
            assertTrue(parent.tagName().equals("body"));
            body = parent;
        }
        assertTrue(body.child(0).equals(script));
    }

    @Test
    public void testParseInvalidTagOrder() {
        String html = "<html><body><div><p>Hello World</p></body></html>";
        try {
            Parser parser = new Parser(html, null, false);
            parser.parse();
            assertFalse(true); // this line should not be reached
        } catch (AssertionError e) {
            // expected
        }
    }

    @Test
    public void testParseEmptyDocument() {
        String html = "<html></html>";
        Document doc = Parser.parse(html, null);
        assertNotNull(doc);
        assertTrue(doc.body() == null);
        assertTrue(doc.head() == null);
    }
}