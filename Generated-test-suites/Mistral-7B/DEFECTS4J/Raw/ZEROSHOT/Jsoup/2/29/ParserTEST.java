package org.jsoup.parser;

import org.apache.commons.lang3.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.jsoup.Jsoup.parse;
import static org.jsoup.Jsoup.parseBodyFragment;

public class ParserTest {

 @Test
 public void testParse() {
 Parser parser = new Parser("", "baseUrl", false);
 Document doc = parser.parse();
 assertThat(doc, is(notNullValue()));
 }

 @Test(expected = NullPointerException.class)
 public void testParseNullHtml() {
 Parser parser = new Parser(null, "baseUrl", false);
 parser.parse();
 }

 @Test
 public void testParseEmptyString() {
 Parser parser = new Parser("", "baseUrl", false);
 Document doc = parser.parse();
 assertThat(doc, is(notNullValue()));
 assertThat(doc.body(), is(notNullValue()));
 assertThat(doc.body().text(), is(""));
 }

 @Test
 public void testParseSimpleHtml() {
 String simpleHtml = "<html><body><h1>Hello</h1></body></html>";
 Parser parser = new Parser(simpleHtml, "baseUrl", false);
 Document doc = parser.parse();
 assertThat(doc, is(notNullValue()));
 assertThat(doc.body(), is(notNullValue()));
 assertThat(doc.body().head(), is(notNullValue()));
 assertThat(doc.body().head().title(), is(notNullValue()));
 assertThat(doc.body().title().text(), is("Hello"));
 }

 @Test
 public void testParseBodyFragment() {
 String bodyFragment = "<body><h1>Hello</h1></body>";
 Parser parser = new Parser(bodyFragment, "baseUrl", true);
 Document doc = parser.parse();
 assertThat(doc, is(notNullValue()));
 assertThat(doc.body(), is(notNullValue()));
 assertThat(doc.head(), is(notNullValue()));
 assertThat(doc.head().title(), is(notNullValue()));
 assertThat(doc.head().title().text(), is(""));
 assertThat(doc.body().title(), is(notNullValue()));
 assertThat(doc.body().title().text(), is("Hello"));
 }

 @Test
 public void testParseComment() {
 String html = "<html><!-- this is a comment --> <body></body></html>";
 Parser parser = new Parser(html, "baseUrl", false);
 Document doc = parser.parse();
 Element comment = doc.body().child(0);
 assertThat(comment, is(instanceOf(Comment.class)));
 assertThat(comment.data(), is(" this is a comment "));
 }

 @Test
 public void testParseCdata() {
 String html = "<html><script><![CDATA[var x = 5;]]></script></html>";
 Parser parser = new Parser(html, "baseUrl", false);
 Document doc = parser.parse();
 Element cdata = doc.body().child(0);
 assertThat(cdata, is(instanceOf(TextNode.class)));
 assertThat(cdata.text(), is("var x = 5;\n"));
 }

 @Test
 public void testParseXmlDecl() {
 String html = "<html xmlns:html=\"http://www.w3.org/1999/xhtml\">";
 Parser parser = new Parser(html, "baseUrl", false);
 Document doc = parser.parse();
 XmlDeclaration xmlDecl = doc.head().child(0);
 assertThat(xmlDecl, is(notNullValue()));
 assertThat(xmlDecl.xmlVersion(), is(""));
 }

 @Test
 public void testParseEndTag() {
 String html = "<html><body><p>Hello</p></body></html>";
 Parser parser = new Parser(html, "baseUrl", false);
 Document doc = parser.parse();
 Element p = doc.body().child(0);
 assertThat(p, is(instanceOf(Element.class)));
 assertThat(p.tagName(), is("p"));
 assertThat(p.text(), is("Hello"));
 }

 @Test
 public void testParseStartTag() {
 String html = "<html><body><div id=\"myDiv\"></div></body></html>";
 Parser parser = new Parser(html, "baseUrl", false);
 Document doc = parser.parse();
 Element div = doc.body().child(0);
 assertThat(div, is(instanceOf(Element.class)));
 assertThat(div.tagName(), is("div"));
 assertThat(div.id(), is("myDiv"));
 }

 @Test
 public void testParseAttribute() {
 String html = "<html><body><a class=\"link\" href=\"http://example.com\">Link</a></body></html>";
 Parser parser = new Parser(html, "baseUrl", false);
 Document doc = parser.parse();
 Element a = doc.body().child(0);
 Attribute classAttr = a.attr("class");
 Attribute hrefAttr = a.attr("href");
 assertThat(classAttr, is(notNullValue()));
 assertThat(classAttr.value(), is("link"));
 assertThat(hrefAttr, is(notNullValue()));
 assertThat(hrefAttr.value(), is("http://example.com"));
 }

 @Test
 public void testParseTextNode() {
 String html = "<html><body><p>Hello World</p></body></html>";
 Parser parser = new Parser(html, "baseUrl", false);
 Document doc = parser.parse();
 TextNode textNode = doc.body().child(0).child(0);
 assertThat(textNode, is(instanceOf(TextNode.class)));
 assertThat(textNode.text(), is("Hello World"));
 }

 @Test
 public void testParseEmptyTextNode() {
 String html = "<html><body><p></p></body></html>";
 Parser parser = new Parser(html, "baseUrl", false);
 Document doc = parser.parse();
 TextNode textNode = doc.body().child(0).child(0);
 assertThat(textNode, is(instanceOf(TextNode.class)));
 assertThat(textNode.text(), is(""));
 }

 @Test
 public void testParseElementWithEmptyTag() {
 String html = "<html><body><img src=\"example.png\"/></body></html>";
 Parser parser = new Parser(html, "baseUrl", false);
 Document doc = parser.parse();
 Element img = doc.body().child(0);
 assertThat(img, is(instanceOf(Element.class)));
 assertThat(img.tagName(), is("img"));
 assertThat(img.attr("src"), is(notNullValue()));
 assertThat(img.attr("src").value(), is("example.png"));
 }

 @Test
 public void testParseElementWithTextAndAttributes() {
 String html = "<html><body><a href=\"http://example.com\" class=\"link\">Link text</a></body></html>";
 Parser parser = new Parser(html, "baseUrl", false);
 Document doc = parser.parse();
 Element a = doc.body().child(0);
 assertThat(a, is(instanceOf(Element.class)));
 assertThat(a.tagName(), is("a"));
 assertThat(a.text(), is("Link text"));
 List<Attribute> attributes = a.attributes();
 assertThat(attributes.size(), is(2));
 Attribute hrefAttr = attributes.get(0);
 Attribute classAttr = attributes.get(1);
 assertThat(hrefAttr.key(), is("href"));
 assertThat(hrefAttr.value(), is("http://example.com"));
 assertThat(classAttr.key(), is("class"));
 assertThat(classAttr.value(), is("link"));
 }

 @Test
 public void testParseElementWithNestedTags() {
 String html = "<html><body><p><a href=\"http://example.com\">Link text</a></p></body></html>";
 Parser parser = new Parser(html, "baseUrl", false);
 Document doc = parser.parse();
 Element p = doc.body().child(0);
 Element a = p.child(0);
 assertThat(p, is(instanceOf(Element.class)));
 assertThat(p.tagName(), is("p"));
 assertThat(a, is(instanceOf(Element.class)));
 assertThat(a.tagName(), is("a"));
 assertThat(p.child(0), is(a));
 }

 @Test
 public void testParseBaseTag() {
 String html = "<html><head><base href=\"http://example.com\"/></head><body><p>Hello</p></body></html>";
 Parser parser = new Parser(html, "baseUrl", false);
 Document doc = parser.parse();
 assertThat(doc.baseUri(), is("http://example.com"));
 }

 @Test
 public void testParseScriptTag() {
 String html = "<html><body><script src=\"script.js\"></script></body></html>";
 Parser parser = new Parser(html, "baseUrl", false);
 Document doc = parser.parse();
 Element script = doc.body().child(0);
 assertThat(script, is(notNullValue()));
 assertThat(script.tagName(), is("script"));
 }

 @Test
 public void testParseScriptTagWithSrcAttribute() {
 String html = "<html><body><script src=\"script.js\"></script></body></html>";
 Parser parser = new Parser(html, "baseUrl", false);
 Document doc = parser.parse();
 Element script = doc.body().child(0);
 assertThat(script, is(notNullValue()));
 assertThat(script.tagName(), is("script"));
 assertThat(script.attr("src"), is(notNullValue()));
 assertThat(script.attr("src").value(), is("script.js"));
 }

 @Test
 public void testParseTitleTag() {
 String html = "<html><head><title>Title</title></head><body></body></html>";
 Parser parser = new Parser(html, "baseUrl", false);
 Document doc = parser.parse();
 Element title = doc.head().child(0);
 assertThat(title, is(notNullValue()));
 assertThat(title.tagName(), is("title"));
 assertThat(title.text(), is("Title"));
 }

 @Test
 public void testParseTextareaTag() {
 String html = "<html><body><textarea name=\"textarea\"></textarea></body></html>";
 Parser parser = new Parser(html, "baseUrl", false);
 Document doc = parser.parse();
 Element textarea = doc.body().child(0);
 assertThat(textarea, is(instanceOf(Element.class)));
 assertThat(textarea.tagName(), is("textarea"));
 }

 @Test
 public void testParseTextareaTagWithAttributes() {
 String html = "<html><body><textarea name=\"textarea\" rows=\"5\" cols=\"30\"></textarea></body></html>";
 Parser parser = new Parser(html, "baseUrl", false);
 Document doc = parser.parse();
 Element textarea = doc.body().child(0);
 assertThat(textarea, is(instanceOf(Element.class)));
 assertThat(textarea.tagName(), is("textarea"));
 List<Attribute> attributes = textarea.attributes();
 assertThat(attributes.size(), is(3));
 Attribute nameAttr = attributes.get(0);
 Attribute rowsAttr = attributes.get(1);
 Attribute colsAttr = attributes.get(2);
 assertThat(nameAttr.key(), is("name"));
 assertThat(nameAttr.value(), is("textarea"));
 assertThat(rowsAttr.key(), is("rows"));
 assertThat(rowsAttr.value(), is("5"));
 assertThat(colsAttr.key(), is("cols"));
 assertThat(colsAttr.value(), is("30"));
 }
}