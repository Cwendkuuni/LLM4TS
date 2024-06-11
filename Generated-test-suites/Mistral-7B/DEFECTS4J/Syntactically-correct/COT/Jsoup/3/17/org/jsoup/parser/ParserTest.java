package org.jsoup.parser;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;

/**
 * Test class for Parser.
 */
public class ParserTest {

    private Parser parser;
    private Document doc;

    @Before
    public void setUp() {
        String html = "<html><head><title>Test</title></head>" +
                "<body><h1>Hello<h1><p id='p1'>World</p>" +
                "<script type='text/javascript' src='script.js'></script>" +
                "<img src='image.jpg' alt='Image'>" +
                "<textarea name='textarea'>Text</textarea>" +
                "</body></html>";
        String baseUri = "http://www.example.com";
        parser = new Parser(html, baseUri, false);
        doc = parser.parse();
    }

    @After
    public void tearDown() {
        parser = null;
        doc = null;
    }

    

    // Test parse() method
    @Test
    public void testParse() {
        Document expectedDoc = new Document(baseUri);
        Element html = expectedDoc.getElementsByTag("html").first();
        Element head = expectedDoc.getElementsByTag("head").first();
        Element body = expectedDoc.getElementsByTag("body").first();

        Element title = doc.getElementsByTag("title").first();
        assertEquals("Test", title.text());

        Element h1 = doc.getElementsByTag("h1").first();
        assertEquals("Hello", h1.text());

        Element p = doc.getElementsByTag("p").first();
        assertEquals("World", p.text());

        Element script = doc.getElementsByTag("script").first();
        assertTrue(script.hasAttr("src"));
        assertEquals("script.js", script.attr("src"));

        Element img = doc.getElementsByTag("img").first();
        assertTrue(img.hasAttr("src"));
        assertEquals("image.jpg", img.attr("src"));

        Element textarea = doc.getElementsByTag("textarea").first();
        assertTrue(textarea.hasAttr("name"));
        assertEquals("textarea", textarea.attr("name"));
        assertEquals("Text", textarea.text());

        assertEquals(expectedDoc.body().children(), doc.body().children());
    }

    // Test parseBodyFragment() method
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<script type='text/javascript' src='script.js'></script>" +
                "<textarea name='textarea'>Text</textarea>";
        Document expectedDoc = new Document(baseUri);
        Element body = expectedDoc.getElementsByTag("body").first();

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        Element script = doc.getElementsByTag("script").first();
        assertTrue(script.hasAttr("src"));
        assertEquals("script.js", script.attr("src"));

        Element textarea = doc.getElementsByTag("textarea").first();
        assertTrue(textarea.hasAttr("name"));
        assertEquals("textarea", textarea.attr("name"));
        assertEquals("Text", textarea.text());

        assertEquals(expectedDoc.body().children(), doc.body().children());
    }

    // Test parseComment() method
    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->" +
                "<html><head><title>Test</title></head>" +
                "<body><h1>Hello<h1><p id='p1'>World</p>" +
                "</body></html>";
        String baseUri = "http://www.example.com";
        parser = new Parser(html, baseUri, false);
        doc = parser.parse();

        Element comment = doc.getElementsByTag("comment").first();
        assertNull(comment);

        comment = doc.getElementsByClass("jsoup-comment").first();
        assertEquals("This is a comment", comment.text());
    }

    // Test parseXmlDecl() method
    @Test
    public void testParseXmlDecl() {
        String html = "<xml version='1.0' encoding='UTF-8'> " +
                "<html><head><title>Test</title></head>" +
                "<body><h1>Hello<h1><p id='p1'>World</p>" +
                "</body></html>";
        String baseUri = "http://www.example.com";
        parser = new Parser(html, baseUri, false);
        doc = parser.parse();

        Element xmlDeclaration = doc.getElementsByTag("xml-declaration").first();
        assertEquals("1.0", xmlDeclaration.attr("version"));
        assertEquals("UTF-8", xmlDeclaration.attr("encoding"));
        assertTrue(xmlDeclaration.isValid());
    }

    // Test parseEndTag() method
    @Test
    public void testParseEndTag() {
        String html = "<html><body><p><h1>Hello</h1></p></body></html>";
        String baseUri = "http://www.example.com";
        parser = new Parser(html, baseUri, false);
        doc = parser.parse();

        Element p = doc.getElementsByTag("p").first();
        Element h1 = p.getElementsByTag("h1").first();
        assertEquals("Hello", h1.text());

        parser = new Parser(html, baseUri, false); // reset for next test
        doc = parser.parse();

        p = doc.getElementsByTag("p").first();
        h1 = p.getElementsByTag("h1");
        assertNotNull(h1);
        assertFalse(h1.hasSameParent(p));
    }

    // Test parseStartTag() method
    @Test
    public void testParseStartTag() {
        String html = "<html><head><title>Test</title></head>" +
                "<body><h1>Hello<img src='image.jpg' alt='Image'></h1></body></html>";
        String baseUri = "http://www.example.com";
        parser = new Parser(html, baseUri, false);
        doc = parser.parse();

        Element title = doc.getElementsByTag("title").first();
        assertEquals("Test", title.text());

        Element h1 = doc.getElementsByTag("h1").first();
        assertEquals("Hello", h1.text());

        Element img = h1.getElementsByTag("img").first();
        assertEquals("image.jpg", img.attr("src"));
        assertEquals("Image", img.attr("alt"));
    }

    // TestparseAttribute() method
    @Test
    public void testParseAttribute() {
        String html = "<img src='image.jpg' alt='Image' title='Title' id='id'>";
        String baseUri = "http://www.example.com";
        parser = new Parser(html, baseUri, false);
        doc = parser.parse();

        Element img = doc.getElementsByTag("img").first();
        assertEquals("image.jpg", img.attr("src"));
        assertEquals("Image", img.attr("alt"));
        assertEquals("Title", img.attr("title"));
        assertEquals("id", img.attr("id"));

        String invalidHtml = "<img src='image.jpg' title='Title' alt='Image'>";
        parser = new Parser(invalidHtml, baseUri, false);
        doc = parser.parse();

        img = doc.getElementsByTag("img").first();
        assertEquals("image.jpg", img.attr("src"));
        assertNull(img.attr("id")); // missing id attribute
    }

    // Test parseTextNode() method
    @Test
    public void testParseTextNode() {
        String html = "<html><head><title>Test</title></head>" +
                "<body><h1>Hello<h1><p id='p1'>World</p></body></html>";
        String baseUri = "http://www.example.com";
        parser = new Parser(html, baseUri, false);
        doc = parser.parse();

        Element title = doc.getElementsByTag("title").first();
        assertEquals("Test", title.text());

        Element h1 = doc.getElementsByTag("h1").first();
        assertEquals("Hello", h1.text());

        Element p = doc.getElementsByTag("p").first();
        assertEquals("World", p.text());

        html = "<html><head><title>Test</title></head>" +
                "<body><h1>Hello<h1><p id='p1'></p></body></html>";
        parser = new Parser(html, baseUri, false);
        doc = parser.parse();

        p = doc.getElementsByTag("p").first();
        assertEquals(StringUtils.EMPTY, p.text());
    }

    // Test parseCdata() method
    @Test
    public void testParseCdata() {
        String html = "<html><head><title>Test</title></head>" +
                "<body><h1>Hello<h1><p><![CDATA[World]]></p></body></html>";
        String baseUri = "http://www.example.com";
        parser = new Parser(html, baseUri, false);
        doc = parser.parse();

        Element p = doc.getElementsByTag("p").first();
        assertEquals("World", p.child(0).text());
    }

    // Test addChildToParent() method
    @Test
    public void testAddChildToParent() {
        String html = "<html><head><title>Test</title></head>" +
                "<body><h1>Hello<h1><p><img src='image.jpg' alt='Image'></p></body></html>";
        String baseUri = "http://www.example.com";
        parser = new Parser(html, baseUri, false);
        doc = parser.parse();

        Element p = doc.getElementsByTag("p").first();
        Element img = new Element(Tag.IMG, baseUri);
        img.attr("src", "image.jpg");
        img.attr("alt", "Image");
        parser.addChildToParent(img, false);

        assertEquals(p, img.getParent());
        assertEquals(2, p.childSize());
    }

    // Test stackHasValidParent() method
    @Test
    public void testStackHasValidParent() {
        String html = "<html><head><title>Test</title></head>" +
                "<body><h1>Hello<h1><p><img src='image.jpg' alt='Image'></p></body></html>";
        String baseUri = "http://www.example.com";
        parser = new Parser(html, baseUri, false);
        doc = parser.parse();

        Element p = doc.getElementsByTag("p").first();
        Element img = new Element(Tag.IMG, baseUri);
        img.attr("src", "image.jpg");
        img.attr("alt", "Image");

        assertTrue(parser.stackHasValidParent(img.tag()));

        img.tag(Tag.DIV);
        assertFalse(parser.stackHasValidParent(img.tag()));

        Element div = new Element(Tag.DIV, baseUri);
        parser.stack.getLast().appendChild(div);
        assertTrue(parser.stackHasValidParent(img.tag()));
        parser.stack.removeLast();
    }

    // Test popStackToSuitableContainer() method
    @Test
    public void testPopStackToSuitableContainer() {
        String html = "<html><head><title>Test</title></head>" +
                "<body><h1>Hello<h1><p><img src='image.jpg' alt='Image'></p></body></html>";
        String baseUri = "http://www.example.com";
        parser = new Parser(html, baseUri, false);
        doc = parser.parse();

        Element p = doc.getElementsByTag("p").first();
        Element img = new Element(Tag.IMG, baseUri);
        img.attr("src", "image.jpg");
        img.attr("alt", "Image");

        Element div = parser.popStackToSuitableContainer(img.tag());
        assertEquals(Tag.BODY, div.tag());
        assertEquals(p, div);

        img.tag(Tag.DIV);
        div = parser.popStackToSuitableContainer(img.tag());
        assertEquals(Tag.BODY, div.tag());
        assertEquals(p, div);

        Element head = new Element(Tag.HEAD, baseUri);
        parser.stack.getLast().appendChild(head);
        div = parser.popStackToSuitableContainer(img.tag());
        assertEquals(Tag.HEAD, div.tag());

        parser.stack.removeLast();
        div = parser.popStackToSuitableContainer(img.tag());
        assertEquals(Tag.BODY, div.tag());
    }

    // Test popStackToClose() method
    @Test
    public void testPopStackToClose() {
        String html = "<html><head><title>Test</title></head>" +
                "<body><h1>Hello<h1><p><img src='image.jpg' alt='Image'></p></body></html>";
        String baseUri = "http://www.example.com";
        parser = new Parser(html, baseUri, false);
        doc = parser.parse();

        Element p = doc.getElementsByTag("p").first();
        Element img = new Element(Tag.IMG, baseUri);
        img.attr("src", "image.jpg");
        img.attr("alt", "Image");

        parser.stack.getLast().appendChild(img);
        Element div = parser.popStackToClose(Tag.IMG);
        assertEquals(p, div);

        img.tag(Tag.DIV);
        div = parser.popStackToClose(Tag.DIV);
        assertEquals(p, div);

        parser.stack.getLast().appendChild(htmlTag.createElement(parser));
        div = parser.popStackToClose(Tag.DIV);
        assertEquals(htmlTag, div);
    }

    
}