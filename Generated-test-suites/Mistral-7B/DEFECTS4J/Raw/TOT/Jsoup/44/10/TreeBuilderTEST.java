package org.jsoup.parser.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;

import java.util.ArrayList;

public class TreeBuilderTest {

 @Test
 public void testParseEmptyInput() {
 TreeBuilder parser = new ParserTreeBuilder();
 Document doc = parser.parse("", "", ParseErrorList.noTracking());
 Assert.assertNull(doc);
 }

 @Test
 public void testParseSimpleStartTag() {
 TreeBuilder parser = new ParserTreeBuilder();
 Document doc = parser.parse("<div>", "", ParseErrorList.noTracking());
 Element div = doc.body();
 Assert.assertEquals("div", div.tagName());
 }

 @Test
 public void testParseSimpleEndTag() {
 TreeBuilder parser = new ParserTreeBuilder();
 String input = "<div><p></p></div>";
 Document doc = parser.parse(input, "", ParseErrorList.noTracking());
 Element div = doc.body();
 Element p = div.child(0);
 parser = new ParserTreeBuilder();
 Document doc2 = parser.parse("<div/>", "", ParseErrorList.noTracking());
 Element div2 = doc2.body();
 Assert.assertEquals(div, div2);
 }

 @Test
 public void testParseSimpleAttributes() {
 TreeBuilder parser = new ParserTreeBuilder();
 String input = "<div id='test' class='testClass'></div>";
 Document doc = parser.parse(input, "", ParseErrorList.noTracking());
 Element div = doc.body();
 Assert.assertEquals("test", div.id());
 Assert.assertEquals("testClass", div.classes().toString());
 }

 @Test
 public void testParseErrorHandling() {
 TreeBuilder parser = new ParserTreeBuilder();
 ParseErrorList errors = new ParseErrorList();
 String input = "<invalid>";
 Document doc = parser.parse(input, "", errors);
 Assert.assertNull(doc);
 Assert.assertEquals(1, errors.size());
 }

 @Test
 public void testParseNestedElements() {
 TreeBuilder parser = new ParserTreeBuilder();
 String input = "<div><p><em></em></p></div>";
 Document doc = parser.parse(input, "", ParseErrorList.noTracking());
 Element div = doc.body();
 Element p = div.child(0);
 Element em = p.child(0);
 Assert.assertEquals("div", div.tagName());
 Assert.assertEquals("p", p.tagName());
 Assert.assertEquals("em", em.tagName());
 }

 @Test
 public void testParseEmptyStartTag() {
 TreeBuilder parser = new ParserTreeBuilder();
 String input = "<div></div>";
 Document doc = parser.parse(input, "", ParseErrorList.noTracking());
 Element div = doc.body();
 Assert.assertEquals("div", div.tagName());
 Assert.assertNull(div.classes());
 }

 @Test
 public void testParseSelfClosingStartTag() {
 TreeBuilder parser = new ParserTreeBuilder();
 String input = "<input type='text' />";
 Document doc = parser.parse(input, "", ParseErrorList.noTracking());
 Element inputElement = doc.body().child(0);
 Assert.assertEquals("input", inputElement.tagName());
 Assert.assertFalse(inputElement.hasChildren());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullInput() {
 TreeBuilder parser = new ParserTreeBuilder();
 parser.parse(null, "", ParseErrorList.noTracking());
 }
}

class ParserTreeBuilder extends TreeBuilder {
 @Override
 protected boolean process(Token token) {
 return super.process(token);
 }
}