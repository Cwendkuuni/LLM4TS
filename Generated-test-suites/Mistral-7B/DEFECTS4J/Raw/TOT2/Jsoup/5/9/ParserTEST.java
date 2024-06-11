package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

/**
 * ParserTest class to test Parser methods in the Parser class
 */
public class ParserTest {

 // Test parse method
 @Test
 public void testParse() {
 String html = "<html><head><title>Title</title></head><body><h1>Header</h1><p>Paragraph</p></body></html>";
 Document doc = Parser.parse(html, "http://example.com");

 Element htmlTag = doc.head();
 assertEquals(htmlTag, doc.body().previousElementSibling());
 assertEquals("Title", htmlTag.text());
 assertEquals("http://example.com", doc.baseUri());

 Element h1 = doc.select("h1").first();
 assertEquals("Header", h1.text());
 Element p = doc.select("p").first();
 assertEquals("Paragraph", p.text());
 }

 // Test parseBodyFragment method
 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><p>Paragraph</p></body>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

 assertEquals("http://example.com", doc.baseUri());
 Element p = doc.select("p").first();
 assertEquals("Paragraph", p.text());
 }

 // Test parseBodyFragmentRelaxed method
 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<body><div></div>";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");

 assertEquals("http://example.com", doc.baseUri());
 Element div = doc.select("div").first();
 assertNotNull(div);
 assertEquals(0, div.childNodes().size());
 }

 // Test parse method with invalid html
 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidHtml() {
 Parser.parse("<", "http://example.com");
 }

 // Test parseEndTag method
 @Test
 public void testParseEndTag() {
 String html = "<html><body><div><p></p></div></body></html>";
 Document doc = Parser.parse(html, "http://example.com");

 Element div = doc.select("div").first();
 Element p = doc.select("p").first();

 assertEquals(1, div.childNodes().size());
 assertEquals(1, p.childNodes().size());
 assertNull(p.childNodes().get(0));

 Parser.parseEndTag(doc, "div");

 assertEquals(0, div.childNodes().size());
 assertNull(div.previousElementSibling());
 }

 // Test parseStartTag method
 @Test
 public void testParseStartTag() {
 String html = "<html><body><div><p><input type='text' name='name' value='John' /></p></div></body></html>";
 Document doc = Parser.parse(html, "http://example.com");

 Element div = doc.select("div").first();
 Element p = doc.select("p").first();
 Element input = p.select("input").first();

 assertEquals(1, div.childNodes().size());
 assertEquals(1, p.childNodes().size());
 assertEquals(1, input.childNodes().size());

 Attribute nameAttr = input.attr("name");
 assertEquals("name", nameAttr.getKey());
 assertEquals("John", nameAttr.getValue());

 assertEquals("John", input.value());
 }

 // Test parseAttribute method
 @Test
 public void testParseAttribute() {
 String html = "<div id='id1' class='class1 class2' title='Title'></div>";
 Document doc = Parser.parse(html, "http://example.com");

 Element div = doc.select("div").first();

 assertEquals(4, div.attributeTags().size());
 Attribute idAttr = div.attr("id");
 assertEquals("id", idAttr.getKey());
 assertEquals("id1", idAttr.getValue());

 Attribute classAttr = div.attr("class");
 assertEquals("class", classAttr.getKey());
 String classes = classAttr.getValue();
 assertEquals("class1 class2", classes);

 Attribute titleAttr = div.attr("title");
 assertEquals("title", titleAttr.getKey());
 assertEquals("Title", titleAttr.getValue());
 }

 // Test parseTextNode method
 @Test
 public void testParseTextNode() {
 String html = "<html><body><p>Paragraph with <textarea>textarea</textarea></p></body></html>";
 Document doc = Parser.parse(html, "http://example.com");

 Element p = doc.select("p").first();
 TextNode textNode = (TextNode) p.text();
 assertEquals("Paragraph with ", textNode.textBefore());
 assertEquals("textarea", textNode.textAfter());
 }

 // Test parseCdata method
 @Test
 public void testParseCdata() {
 String html = "<html><body><![CDATA[Paragraph with <textarea>textarea</textarea>]]></body></html>";
 Document doc = Parser.parse(html, "http://example.com");

 Element body = doc.body();
 TextNode cdataNode = (TextNode) body.childNodes().get(0);
 assertEquals("Paragraph with <textarea>textarea</textarea>", cdataNode.text());
 }

 // Test parseComment method
 @Test
 public void testParseComment() {
 String html = "<html><body><!-- This is a comment --></body></html>";
 Document doc = Parser.parse(html, "http://example.com");

 Element body = doc.body();
 Comment comment = (Comment) body.childNodes().get(0);
 assertEquals("This is a comment", comment.text());
 }

 // Test stackHasValidParent method
 @Test
 public void testStackHasValidParent() {
 Parser parser = new Parser("", "http://example.com");
 parser.stack.add(new Element(Parser.headTag, "http://example.com"));
 parser.stack.add(new Element(Parser.htmlTag, "http://example.com"));

 assertEquals(true, parser.stackHasValidParent(Parser.bodyTag));
 assertEquals(true, parser.stackHasValidParent(Parser.headTag));
 assertEquals(false, parser.stackHasValidParent(Parser.titleTag));
 }

 // Test addChildToParent method
 @Test
 public void testAddChildToParent() {
 Parser parser = new Parser("", "http://example.com");
 Element parent = new Element(Parser.bodyTag, "http://example.com");
 parser.stack.add(parent);
 Element child = new Element(Parser.pTag, "http://example.com");

 parser.addChildToParent(child, false);
 parser.stack.add(child);

 assertEquals(child, parent.child(0));
 assertEquals(1, parent.childNodes().size());
 }

 // Test popStackToSuitableContainer method
 @Test
 public void testPopStackToSuitableContainer() {
 Parser parser = new Parser("", "http://example.com");
 parser.stack.add(new Element(Parser.bodyTag, "http://example.com"));
 parser.stack.add(new Element(Parser.htmlTag, "http://example.com"));

 Element div = new Element(Parser.divTag, "http://example.com");
 parser.stack.add(div);

 parser.popStackToSuitableContainer(Parser.pTag);

 assertEquals(parser.stack.getLast(), div);
 assertEquals(2, parser.stack.size());
 }

 // Test popStackToClose method
 @Test
 public void testPopStackToClose() {
 Parser parser = new Parser("", "http://example.com");
 parser.stack.add(new Element(Parser.bodyTag, "http://example.com"));
 parser.stack.add(new Element(Parser.htmlTag, "http://example.com"));
 parser.stack.add(new Element(Parser.divTag, "http://example.com"));
 parser.stack.add(new Element(Parser.pTag, "http://example.com"));
 parser.stack.add(new Element(Parser.pTag, "http://example.com"));

 parser.popStackToClose(Parser.divTag);

 assertEquals(parser.stack.getLast(), parser.stack.getLast().previousElementSibling());
 assertEquals(4, parser.stack.size());
 }
}