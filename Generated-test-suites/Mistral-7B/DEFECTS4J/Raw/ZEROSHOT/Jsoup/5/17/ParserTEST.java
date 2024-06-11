package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.AttributeSet;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Xml;
import org.jsoup.parser.Parser;

public class ParserTest {

 private static String html = "<!doctype html>\n" +
         "<html lang=\"en\">\n" +
         "<head>\n" +
         "  <meta charset=\"UTF-8\">\n" +
         "  <title>Test Page</title>\n" +
         "</head>\n" +
         "<body>\n" +
         "  <div id=\"container\">\n" +
         "    <h1>Hello <world></h1>\n" +
         "    <p>Content <a href=\"link.html\">Link</a></p>\n" +
         "    <script src=\"script.js\"></script>\n" +
         "    <textarea>Textarea</textarea>\n" +
         "    <img src=\"image.png\" alt=\"Image\"></img>\n" +
         "    <base href=\"base.html\">\n" +
         "  </div>\n" +
         "</body>\n" +
         "</html>";

 @Test
 public void parseTest() {
 Parser parser = new Parser(html, null, false);
 Document doc = parser.parse();

 assertEquals(Parser.htmlTag, doc.tagName());
 assertEquals(html, doc.body().outerHtml());

 Element container = doc.body().getElementById("container");
 assertTrue(container.tagName().equals("div"));
 assertTrue(container.hasChildren());

 Element h1 = container.child(0);
 assertEquals(Element.Tag.H1, h1.tagName());
 assertEquals("Hello ", h1.text());

 Element world = h1.child(0);
 assertTrue(world instanceof Element);
 assertTrue(world.tagName().equals(Element.Tag.TEXT));
 assertEquals(" <world>", world.outerHtml());

 Element p = container.child(1);
 assertEquals(Element.Tag.P, p.tagName());
 assertTrue(p.hasChildren());

 assertSame(Element.Tag.A, p.child(0).tagName());
 assertEquals("Link", p.child(0).text());

 assertEquals("script.js", doc.head().child(0).attr("src"));

 Element textarea = container.child(3);
 assertTrue(textarea.tagName().equals(Element.Tag.TEXTAREA));
 assertEquals("Textarea", textarea.text());

 Element img = container.child(5);
 assertTrue(img.tagName().equals(Element.Tag.IMG));
 assertEquals("image.png", img.absUrl("src"));
 assertEquals("Image", img.attr("alt"));

 Element base = container.child(4);
 assertEquals(Element.Tag.BASE, base.tagName());
 assertEquals("base.html", base.absUrl("href"));
 }

 @Test
 public void parseBodyFragmentTest() {
 Parser parser = new Parser("<p>Body fragment</p>", null, true);
 Document doc = parser.parse();

 assertEquals(Element.Tag.P, doc.tagName());
 assertEquals("<p>Body fragment</p>", doc.outerHtml());
 assertTrue(doc.head() == null);
 assertTrue(doc.body() != null);
 }

 @Test
 public void parseBodyFragmentRelaxedTest() {
 Parser parser = new Parser("<div><p>Body fragment</p></div>", null, true);
 parser.relaxed = true;
 Document doc = parser.parse();

 assertEquals(Element.Tag.DIV, doc.tagName());
 assertEquals("<div><p>Body fragment</p></div>", doc.outerHtml());
 assertTrue(doc.head() == null);
 assertTrue(doc.body() != null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void parseInvalidHtmlTest() {
 Parser parser = new Parser("", null, false);
 parser.parse();
 }

 @Test
 public void parseEmptyHtmlTest() {
 Parser parser = new Parser("", null, false);
 Document doc = parser.parse();

 assertEquals(Parser.htmlTag, doc.tagName());
 assertTrue(doc.body().text().isEmpty());
 }

 @Test
 public void parseXmlDeclAndCommentTest() {
 String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "<!-- This is a comment -->\n" +
         "<html><body></body></html>";
 Parser parser = new Parser(xml, null, false);
 Document doc = parser.parse();

 assertEquals(Parser.htmlTag, doc.tagName());
 assertEquals(xml, doc.outerHtml());
 assertTrue(doc.body().child(0) instanceof Comment);
 assertTrue(doc.head() == null);
 }

 @Test
 public void parseSelfClosingTagTest() {
 String html = "<img src=\"image.png\" alt=\"Image\" />";
 Parser parser = new Parser(html, null, false);
 Document doc = parser.parse();

 Element img = doc.body().child(0);
 assertTrue(img.tagName().equals(Element.Tag.IMG));
 assertTrue(img.hasAttrs());
 assertEquals(2, img.numChildren());
 assertTrue(img.child(0) instanceof TextNode);
 assertTrue(img.child(1) instanceof DataNode);
 }

 @Test
 public void parseDataTagTest() {
 Parser parser = new Parser("<script type=\"text/javascript\">console.log(\"Hello, world!\");</script>", null, false);
 Document doc = parser.parse();

 Element script = doc.body().child(0);
 assertTrue(script.tagName().equals(Element.Tag.SCRIPT));
 assertTrue(script.hasAttrs());
 assertEquals(1, script.numChildren());
 assertTrue(script.child(0) instanceof DataNode);
 }

 @Test
 public void parseInvalidTagNameTest() {
 Parser parser = new Parser("<foo></foo>", null, false);
 Document doc = parser.parse();

 assertNull(doc);
 }

 @Test
 public void parseEmptyTagTest() {
 Parser parser = new Parser("<br>", null, false);
 Document doc = parser.parse();

 assertTrue(doc != null);
 assertTrue(doc.body() != null);
 assertTrue(doc.body().child(0) instanceof TextNode);
 assertEquals("\n", doc.body().child(0).text());
 }

 @Test
 public void parseAttributeTest() {
 String html = "<div id=\"myDiv\" class=\"myClass\" title=\"My title\">My content</div>";
 Parser parser = new Parser(html, null, false);
 Document doc = parser.parse();

 Element div = doc.body().child(0);
 AttributeSet attrs = div.attributes();

 assertEquals(3, attrs.size());
 assertEquals("myDiv", attrs.get("id"));
 assertEquals("myClass", attrs.get("class"));
 assertEquals("My title", attrs.get("title").toString());
 }

 @Test
 public void parseAttributeValuesTest() {
 String html = "<a href=\"mailto:test@test.com\">Email me</a>";
 Parser parser = new Parser(html, null, false);
 Document doc = parser.parse();

 assertEquals("mailto:test@test.com", doc.head().child(0).attr("href"));
 }

 @Test
 public void parseAttributelessTagTest() {
 String html = "<img src=\"image.png\" alt=\"Image\">";
 Parser parser = new Parser(html, null, false);
 Document doc = parser.parse();

 Element img = doc.body().child(0);
 assertTrue(img.tagName().equals(Element.Tag.IMG));
 assertNull(img.attributes());
 }

 @Test
 public void parseMissingEndTagTest() {
 String html = "<div><p>Hello";
 Parser parser = new Parser(html, null, false);
 try {
 parser.parse();
 fail();
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test
 public void parseMissingEndTagRelaxedTest() {
 Parser parser = new Parser("<html><head><title>Test</title>", null, false);
 parser.relaxed = true;
 Document doc = parser.parse();

 assertEquals(Parser.htmlTag, doc.tagName());
 assertTrue(doc.body() == null);
 assertTrue(doc.head() != null);
 }

 @Test
 public void parseNestedTagsTest() {
 String html = "<div><h1>Hello <p>world</p></h1></div>";
 Parser parser = new Parser(html, null, false);
 Document doc = parser.parse();

 Element div = doc.body().child(0);
 assertTrue(div.tagName().equals(Element.Tag.DIV));
 Element h1 = div.child(0);
 assertTrue(h1.tagName().equals(Element.Tag.H1));
 Element p = h1.child(0);
 assertTrue(p.tagName().equals(Element.Tag.P));
 assertEquals("world", p.text());
 }

 @Test
 public void parseBaseTagTest() {
 String html = "<html>\n" +
         "<head>\n" +
         "<base href=\"http://www.example.com/\">\n" +
         "</head>\n" +
         "<body>\n" +
         "<a href=\"contact.html\">Contact Us</a>\n" +
         "</body>\n" +
         "</html>";
 Parser parser = new Parser(html, null, false);
 Document doc = parser.parse();

 assertEquals("http://www.example.com/contact.html", doc.head().child(0).attr("href"));
 }

 @Test
 public void parseNestedBaseTagTest() {
 String html = "<html>\n" +
         "<head>\n" +
         "<base href=\"http://www.example.com/\">\n" +
         "<base href=\"/path/\">\n" +
         "</head>\n" +
         "<body>\n" +
         "<a href=\"contact.html\">Contact Us</a>\n" +
         "</body>\n" +
         "</html>";
 Parser parser = new Parser(html, null, false);
 Document doc = parser.parse();

 assertEquals("http://www.example.com/path/contact.html", doc.head().child(1).attr("href"));
 }

 @Test
 public void parseImplicitTagTest() {
 String html = "<h1>Hello <world></world></h1>";
 Parser parser = new Parser(html, null, false);
 Document doc = parser.parse();

 Element h1 = doc.body().child(0);
 Element world = h1.child(1);
 assertTrue(world.tagName().equals(Element.Tag.DIV));
 assertTrue(world.tagName().isImplicit());
 assertEquals(Element.Tag.TEXT, world.tagName().getImplicitParent());
 }

 @Test
 public void parseImplicitTagWithChildrenTest() {
 String html = "<ul>\n" +
         "  <li>Apple\n" +
         "    <ul>\n" +
         "      <li>Red</li>\n" +
         "      <li>Green</li>\n" +
         "    </ul>\n" +
         "  </li>\n" +
         "  <li>Banana</li>\n" +
         "  <li>Orange</li>\n" +
         "</ul>";
 Parser parser = new Parser(html, null, false);
 Document doc = parser.parse();

 Element ul = doc.body().child(0);
 assertTrue(ul.tagName().equals(Element.Tag.UL));
 Element li = ul.child(0);
 assertTrue(li.tagName().equals(Element.Tag.LI));
 Element ul2 = li.child(0);
 assertTrue(ul2.tagName().equals(Element.Tag.UL));
 assertTrue(ul2.tagName().isImplicit());
 Element li2 = ul2.child(0);
 assertTrue(li2.tagName().equals(Element.Tag.LI));
 assertEquals("Red", li2.text());
 li2 = ul2.child(1);
 assertEquals("Green", li2.text());
 }

 @Test
 public void parseRelativeUrlTest() {
 String html = "<html>\n" +
         "<head>\n" +
         "<base href=\"http://www.example.com/\">\n" +
         "</head>\n" +
         "<body>\n" +
         "<a href=\"images/image1.png\">Link</a>\n" +
         "</body>\n" +
         "</html>";
 Parser parser = new Parser(html, null, false);
 Document doc = parser.parse();

 assertEquals("http://www.example.com/images/image1.png", doc.head().child(0).attr("href"));
 }

 @Test
 public void parseRelativeUrlWithBaseHrefTest() {
 String html = "<html>\n" +
         "<head>\n" +
         "<base href=\"/path/to/\">\n" +
         "</head>\n" +
         "<body>\n" +
         "<a href=\"images/image1.png\">Link</a>\n" +
         "</body>\n" +
         "</html>";
 Parser parser = new Parser(html, null, false);
 Document doc = parser.parse();

 assertEquals("http://www.example.com/path/to/images/image1.png", doc.head().child(0).attr("href"));
 }

 @Test
 public void parseImplicitTagWithInvalidParentTest() {
 String html = "<div><h1>Hello <p></p></h1></div>";
 Parser parser = new Parser(html, null, false);
 try {
 parser.parse();
 fail();
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test
 public void parseImplicitTagWithInvalidParentRelaxedTest() {
 Parser parser = new Parser("<html><body><h1>Hello <p></p></h1></body>", null, false);
 parser.relaxed = true;
 Document doc = parser.parse();

 assertEquals(Parser.htmlTag, doc.tagName());
 assertTrue(doc.body() != null);
 assertTrue(doc.head() == null);
 }

 @Test
 public void parseImplicitTagWithInvalidParentAndRelaxedTest() {
 Parser parser = new Parser("<html><body><h1>Hello <p></p></h1></body>", null, true);
 Document doc = parser.parse();

 assertEquals(Parser.htmlTag, doc.tagName());
 assertTrue(doc.body() != null);
 assertTrue(doc.head() == null);
 }

}