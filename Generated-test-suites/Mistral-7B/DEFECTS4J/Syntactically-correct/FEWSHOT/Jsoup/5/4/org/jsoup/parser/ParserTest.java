package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body>" +
                "<div><p>Hello World</p></div></body></html>";

        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc.rootElement();

        assertEquals("html", root.tagName());
        assertEquals("http://test.com", root.baseUri());

        Element title = root.select("title").first();
        assertEquals("title", title.tagName());
        assertEquals("Test", title.text());

        Element body = root.select("body").first();
        assertEquals("body", body.tagName());

        Element div = body.select("div").first();
        assertEquals("div", div.tagName());

        Element p = div.select("p").first();
        assertEquals("p", p.tagName());
        assertEquals("Hello World", p.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Hello World</p></div>";

        Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");
        Element root = doc.rootElement().body();

        assertEquals("div", root.tagName());

        Element p = root.select("p").first();
        assertEquals("p", p.tagName());
        assertEquals("Hello World", p.text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><p>Hello <world></world> World</p></div>";

        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://test.com");
        Element root = doc.rootElement().body();

        assertEquals("div", root.tagName());

        Element p = root.select("p").first();
        assertEquals("p", p.tagName());
        assertEquals("Hello <world></world> World", p.text());

        Element world = p.child(0);
        assertEquals("world", world.tagName());
        assertNull(world.text());
    }

    @Test
    public void testParseEmptyBodyFragment() {
        String bodyHtml = "";

        Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");
        Element root = doc.rootElement();

        assertTrue(root.isEmpty());
    }

    @Test
    public void testParseInvalidHtml() {
        String html = "<html><head><title>Test</title></head></html><body>";

        try {
            Document doc = Parser.parse(html, "http://test.com");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            Document doc = Parser.parseBodyFragment(html, "http://test.com");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseInvalidXmlDecl() {
        String html = "<html xml:version='1.0'><head><title>Test</title></head><body>";

        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc.rootElement();

        XmlDeclaration xmlDeclaration = root.selectXmlDeclaration();
        assertEquals("xml version='1.0'", xmlDeclaration.data());
    }

    @Test
    public void testParseXmlDeclWithProcessingInstruction() {
        String html = "<html xml-version='1.0' xml:lang='en'><head><title>Test</title></head><body>";

        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc.rootElement();

        XmlDeclaration xmlDeclaration = root.selectXmlDeclaration();
        assertEquals("xml version='1.0'", xmlDeclaration.data());
        assertTrue(xmlDeclaration.isProcessingInstructionSet());
        assertEquals("xml-version", xmlDeclaration.processingInstructionTarget());
        assertEquals("1.0", xmlDeclaration.processingInstructionData());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --><head><title>Test</title></head><body>";

        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc.rootElement();

        Comment comment = root.select("!-- This is a comment -->").first();
        assertEquals("-- This is a comment --", comment.text());
    }

    @Test
    public void testParseCommentWithCDATA() {
        String html = "<html><!-- <![CDATA[ This is a CDATA comment ]]> --><head><title>Test</title></head><body>";

        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc.rootElement();

        Comment comment = root.select("<!-- <![CDATA[ This is a CDATA comment ]]> -->").first();
        assertEquals("<![CDATA[ This is a CDATA comment ]]", comment.data());
    }

    @Test
    public void testParseScriptTag() {
        String html = "<html><head><title>Test</title><script src=\"script.js\"></script></head><body>";

        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc.rootElement();

        Element script = root.select("script").first();
        assertEquals("script", script.tagName());
        assertEquals("http://test.com/script.js", script.absUrl("src"));
    }

    @Test
    public void testParseStyleTag() {
        String html = "<html><head><title>Test</title><style type=\"text/css\">body { color: red; }</style></head><body>";

        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc.rootElement();

        Element style = root.select("style").first();
        assertEquals("style", style.tagName());
        assertEquals("text/css", style.attributes().get("type"));
        assertEquals("body { color: red; }", style.text());
    }

    @Test
    public void testParseBaseTag() {
        String html = "<html><head><base href=\"/subdir/\"></head><body>";

        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc.rootElement();

        Element base = root.select("base").first();
        assertEquals("base", base.tagName());
        assertEquals("/subdir/", base.absUrl("href"));
        assertEquals("/subdir/", doc.baseUri());
    }

    @Test
    public void testParseEmptyElement() {
        String html = "<html><head><title>Test</title></head><body><img src=\"image.png\"></body></html>";

        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc.rootElement();

        Element img = root.select("img").first();
        assertEquals("img", img.tagName());
        assertTrue(img.isEmpty());
    }

    @Test
    public void testParseEmptySelfClosedElement() {
        String html = "<html><head><title>Test</title></head><body><img src=\"image.png\" /></body></html>";

        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc.rootElement();

        Element img = root.select("img").first();
        assertEquals("img", img.tagName());
        assertTrue(img.isEmpty());
    }

    @Test
    public void testParseSelfClosedElementWithAttributes() {
        String html = "<html><head><title>Test</title></head><body><img src=\"image.png\" alt=\"Image\" class=\"image\"></body></html>";

        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc.rootElement();

        Element img = root.select("img").first();
        assertEquals("img", img.tagName());
        assertFalse(img.isEmpty());
        assertEquals("image.png", img.absUrl("src"));
        assertEquals("Image", img.attributes().get("alt"));
        assertEquals("image", img.attributes().get("class"));
    }

    @Test
    public void testParseSelfClosedElementWithContent() {
        String html = "<html><head><title>Test</title></head><body><img src=\"image.png\" alt=\"Image\" class=\"image\">Content</body></html>";

        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc.rootElement();

        Element img = root.select("img").first();
        assertEquals("img", img.tagName());
        assertFalse(img.isEmpty());
        assertEquals("image.png", img.absUrl("src"));
        assertEquals("Image", img.attributes().get("alt"));
        assertEquals("image", img.attributes().get("class"));

        TextNode content = img.child(0);
        assertEquals("Content", content.text());
    }

    @Test
    public void testParseSelfClosedElementWithMixedContent() {
        String html = "<html><head><title>Test</title></head><body><img src=\"image.png\" alt=\"Image\" class=\"image\">Content<br/>Another line</body></html>";

        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc.rootElement();

        Element img = root.select("img").first();
        assertEquals("img", img.tagName());
        assertFalse(img.isEmpty());
        assertEquals("image.png", img.absUrl("src"));
        assertEquals("Image", img.attributes().get("alt"));
        assertEquals("image", img.attributes().get("class"));

        TextNode content = img.child(0);
        assertEquals("Content", content.text());

        Element br = img.child(1);
        assertEquals("br", br.tagName());
        assertTrue(br.isEmpty());

        TextNode anotherLine = br.nextSibling();
        assertEquals("Another line", anotherLine.text());
    }

    @Test
    public void testParseAttribute() {
        String html = "<html><head><title>Test</title></head><body><img src=\"image.png\" alt=\"Image\" class=\"image\" title=\"Tooltip\"></body></html>";

        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc.rootElement();

        Element img = root.select("img").first();
        Attribute srcAttribute = img.attributes().get("src");
        assertEquals("image.png", srcAttribute.value());
        assertEquals("src", srcAttribute.key());

        Attribute altAttribute = img.attributes().get("alt");
        assertEquals("Image", altAttribute.value());
        assertEquals("alt", altAttribute.key());

        Attribute classAttribute = img.attributes().get("class");
        assertEquals("image", classAttribute.value());
        assertEquals("class", classAttribute.key());

        Attribute titleAttribute = img.attributes().get("title");
        assertEquals("Tooltip", titleAttribute.value());
        assertEquals("title", titleAttribute.key());
    }

    @Test
    public void testParseAttributeWithQuotes() {
        String html = "<html><head><title>Test</title></head><body><img src=\"image.png\" alt=\"Image with \"quotes\" in it\" class=\"image\"></body></html>";

        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc.rootElement();

        Element img = root.select("img").first();
        Attribute altAttribute = img.attributes().get("alt");
        assertEquals("Image with \"quotes\" in it", altAttribute.value());
    }

    @Test
    public void testParseAttributeWithSingleQuoteInValue() {
        String html = "<html><head><title>Test</title></head><body><img src=\"image.png\" alt=\"Image with 'single quote' in it\" class=\"image\"></body></html>";

        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc.rootElement();

        Element img = root.select("img").first();
        Attribute altAttribute = img.attributes().get("alt");
        assertEquals("Image with 'single quote' in it", altAttribute.value());
    }

    @Test
    public void testParseAttributeWithDashInName() {
        String html = "<html><head><title>Test</title></head><body><img src=\"image.png\" alt=\"Image\" class=\"class-name\"></body></html>";

        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc.rootElement();

        Element img = root.select("img").first();
        Attribute classNameAttribute = img.attributes().get("class-name");
        assertEquals("class-name", classNameAttribute.key());
        assertEquals("", classNameAttribute.value());
    }

    @Test
    public void testParseAttributeWithSameNameMultipleTimes() {
        String html = "<html><head><title>Test</title></head><body><img src=\"image.png\" id=\"image\" id=\"icon\"></body></html>";

        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc.rootElement();

        Element img = root.select("img").first();
        Attribute idAttribute1 = img.attributes().get("id");
        assertEquals("image", idAttribute1.value());
        assertEquals("id", idAttribute1.key());

        Attribute idAttribute2 = img.attributes().get("id");
        assertEquals("icon", idAttribute2.value());
        assertEquals("id", idAttribute2.key());
    }

    @Test
    public void testParseAttributeWithEmptyValue() {
        String html = "<html><head><title>Test</title></head><body><img src=\"image.png\" alt=\"Image\" class=\"\"></body></html>";

        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc.rootElement();

        Element img = root.select("img").first();
        Attribute classNameAttribute = img.attributes().get("class");
        assertEquals("", classNameAttribute.value());
        assertEquals("class", classNameAttribute.key());
    }

    @Test
    public void testParseEmptyElementWithAttributes() {
        String html = "<html><head><title>Test</title></head><body><input type=\"text\" name=\"username\" /></body></html>";

        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc.rootElement();

        Element input = root.select("input").first();
        assertEquals("input", input.tagName());
        assertFalse(input.isEmpty());
        assertEquals("text", input.attributes().get("type").value());
        assertEquals("username", input.attributes().get("name").value());
    }
}