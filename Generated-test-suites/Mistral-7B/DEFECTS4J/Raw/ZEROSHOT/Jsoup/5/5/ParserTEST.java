package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 @Test
 public void testParser() {
 Parser parser = new Parser("", "", false); // empty constructor should not throw
 Document doc = parser.parse();
 assertEquals(HtmlTag.HTML, doc.tagName());
 assertTrue(doc.body().isEmpty());

 parser = new Parser("<html><head></head><body></body></html>", "", false);
 doc = parser.parse();
 assertEquals(HtmlTag.HTML, doc.tagName());
 assertEquals(HeadTag.HEAD, doc.head().tagName());
 assertEquals(BodyTag.BODY, doc.body().tagName());
 assertTrue(doc.body().isEmpty());

 parser = new Parser("<html><head></head><body><title>Title</title></body></html>", "", false);
 doc = parser.parse();
 assertEquals(HtmlTag.HTML, doc.tagName());
 assertEquals(HeadTag.HEAD, doc.head().tagName());
 assertEquals(TitleTag.TITLE, doc.head().child(0).tagName());
 assertEquals("Title", doc.title().text());
 assertEquals(BodyTag.BODY, doc.body().tagName());
 assertTrue(doc.body().isEmpty());
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser("<div><p>Hello</p><p>World</p></div>", "", true);
 Document doc = parser.parse();
 assertEquals(HtmlTag.HTML, doc.tagName());
 assertEquals(BodyTag.BODY, doc.body().tagName());
 assertEquals(ParagraphTag.PARAGRAPH, doc.body().child(0).tagName());
 assertEquals("Hello", doc.body().child(0).text());
 assertEquals(ParagraphTag.PARAGRAPH, doc.body().child(1).tagName());
 assertEquals("World", doc.body().child(1).text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 Parser parser = new Parser("<div><p>Hello</p><p>World</p></div>", "", true);
 parser.relaxed = true;
 Document doc = parser.parse();
 assertEquals(HtmlTag.HTML, doc.tagName());
 assertEquals(BodyTag.BODY, doc.body().tagName());
 assertEquals(Element.class, doc.body().child(0).getClass());
 assertEquals("Hello", doc.body().child(0).text());
 assertEquals(Element.class, doc.body().child(1).getClass());
 assertEquals("World", doc.body().child(1).text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullHtml() {
 Parser parser = new Parser(null, "", false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 Parser parser = new Parser("<html></html>", null, false);
 }
}