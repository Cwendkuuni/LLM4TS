package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.TreeBuilder;
import org.junit.Test;

import java.util.ArrayList;

public class TreeBuilderTest {

 private static final String INPUT = " <html> <head></head> <body> <h1 id=\"title\">Test</h1> <p class=\"content\">Hello World!</p> </body> </html>";

 @Test
 public void testParse() {
 TreeBuilder treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }

 @Override
 protected boolean processStartTag(String name) {
 System.out.println("Encountered start tag: " + name);
 return true;
 }

 @Override
 protected boolean processEndTag(String name) {
 System.out.println("Encountered end tag: " + name);
 return true;
 }
 };

 Document doc = treeBuilder.parse(INPUT, "http://example.com");

 assertEquals("html", doc.tagName());
 Element html = doc.body();
 assertEquals("body", html.tagName());
 Element title = html.head().getElementsByTag("h1").first();
 assertEquals("h1", title.tagName());
 assertEquals("Test", title.text());
 Element content = html.getElementsByClass("content").first();
 assertEquals("p", content.tagName());
 assertEquals("Hello World!", content.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 TreeBuilder treeBuilder = new TreeBuilder();
 treeBuilder.parse(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 TreeBuilder treeBuilder = new TreeBuilder();
 treeBuilder.parse("<html>", null);
 }

 @Test
 public void testParseWithErrors() {
 ParseErrorList errors = new ParseErrorList();
 TreeBuilder treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }

 @Override
 protected boolean processStartTag(String name) {
 System.out.println("Encountered start tag: " + name);
 return true;
 }

 @Override
 protected boolean processEndTag(String name) {
 System.out.println("Encountered end tag: " + name);
 return true;
 }

 @Override
 protected void initialiseParse(String input, String baseUri, ParseErrorList errors) {
 super.initialiseParse(input, baseUri, null);
 this.errors = errors;
 }
 };

 try {
 Document doc = treeBuilder.parse("<html><body></body>", "http://example.com", errors);
 fail("Expected an error during parsing.");
 } catch (RuntimeException e) {
 List<String> errorsList = errors.asErrors();
 assertEquals(1, errorsList.size());
 assertEquals("Error parsing start tag : body", errorsList.get(0).getMessage());
 }
 }

 @Test
 public void testProcessStartTagWithAttributes() {
 TreeBuilder treeBuilder = new TreeBuilder() {
 @Override
 protected boolean processStartTag(String name, Attributes attrs) {
 System.out.println("Encountered start tag: " + name);
 System.out.println("Attributes: " + attrs.toString());
 return true;
 }

 @Override
 protected boolean processEndTag(String name) {
 return true;
 }
 };

 Document doc = treeBuilder.parse(INPUT, "http://example.com");
 Element html = doc.body();
 Element title = html.head().getElementsByTag("h1").first();
 Element content = html.getElementsByClass("content").first();

 title.tagName("header");
 title.classes("my-class");
 title.attr("data-custom", "custom-value");

 assertEquals("header", title.tagName());
 assertEquals("my-class", title.classes().get(0));
 assertEquals("custom-value", title.attr("data-custom"));
 }
}