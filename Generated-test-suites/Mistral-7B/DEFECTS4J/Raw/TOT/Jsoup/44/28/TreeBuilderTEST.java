package org.jsoup.parser.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.jsoup.parser.CharacterReader;
import org.jsoup.helper.Validate;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class TreeBuilderTest {

 @Test
 public void testParseEmptyInput() {
 TreeBuilder treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }
 };

 treeBuilder.initialiseParse("", "", ParseErrorList.noTracking());
 Document doc = treeBuilder.parse("", "");
 assertNotNull(doc);
 assertNotNull(doc.body());
 assertNull(doc.head());
 }

 @Test
 public void testParseWithValidInput() {
 String input = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
 TreeBuilder treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }

 @Override
 protected boolean processStartTag(String name) {
 return name.equals("html") || name.equals("head") || name.equals("title") || name.equals("h1");
 }
 };

 treeBuilder.initialiseParse(input, "", ParseErrorList.noTracking());
 Document doc = treeBuilder.parse(input, "");
 assertNotNull(doc);
 Element html = doc.body();
 Element title = html.selectFirst("title");
 Element heading = html.selectFirst("h1");

 Validate.isTrue(title != null, "Title element was not found.");
 Validate.isTrue(heading != null, "Heading element was not found.");
 Validate.isTrue(title.text().equals("Test Title"), "Title text does not match.");
 Validate.isTrue(heading.text().equals("Test Heading"), "Heading text does not match.");
 }

 @Test
 public void testParseWithInvalidInput() {
 TreeBuilder treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }

 @Override
 protected boolean processStartTag(String name) {
 return name.equals("html") || name.equals("head") || name.equals("title") || name.equals("h1");
 }

 @Override
 protected boolean processEndTag(String name) {
 return name.equals("html");
 }
 };

 treeBuilder.initialiseParse("</html>", "", ParseErrorList.noTracking());
 Document doc = treeBuilder.parse("</html>", "");
 assertNull(doc);
 }

 @Test
 public void testParseWithErrors() {
 String input = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
 TreeBuilder treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }

 @Override
 protected boolean processStartTag(String name) {
 return name.equals("html") || name.equals("head") || name.equals("title") || name.equals("h1");
 }

 @Override
 protected void runParser() {
 super.runParser();
 if (currentElement() != null && currentElement().tagName().equals("html")) {
 errors.add(new ParseError("Expected end tag <\\/html>", currentToken.column(), currentToken.line()));
 }
 }
 };

 treeBuilder.initialiseParse(input, "", new ParseErrorList());
 Document doc = treeBuilder.parse(input, "");
 assertNotNull(doc);
 assertEquals(1, treeBuilder.errors.size());
 }
}