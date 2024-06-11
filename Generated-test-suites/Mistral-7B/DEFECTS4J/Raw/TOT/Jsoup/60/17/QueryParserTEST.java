package org.jsoup.select.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.StringUtil;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;

public class QueryParserTest {

 @Test
 public void testParseEmptyQuery() {
 QueryParser parser = new QueryParser("");
 Evaluator evaluator = parser.parse();
 Assert.assertEquals(Evaluator.class, evaluator.getClass());
 }

 @Test
 public void testParseSimpleTagSelector() {
 QueryParser parser = new QueryParser("div");
 Evaluator evaluator = parser.parse();
 Assert.assertEquals(Evaluator.Tag.class, evaluator.getClass());
 }

 @Test
 public void testParseClassSelector() {
 QueryParser parser = new QueryParser(".myclass");
 Evaluator evaluator = parser.parse();
 Assert.assertEquals(Evaluator.Class.class, evaluator.getClass());
 }

 @Test
 public void testParseIdSelector() {
 QueryParser parser = new QueryParser("#myid");
 Evaluator evaluator = parser.parse();
 Assert.assertEquals(Evaluator.Id.class, evaluator.getClass());
 }

 @Test
 public void testParseAttributeSelector() {
 QueryParser parser = new QueryParser("[title~='test']");
 Evaluator evaluator = parser.parse();
 Assert.assertTrue(evaluator instanceof Evaluator.AttributeWithValueMatching);
 }

 @Test
 public void testParseComplexSelector() {
 QueryParser parser = new QueryParser("div.myclass #myid > ul > li:nth-child(2) > a[href^='http']");
 Evaluator evaluator = parser.parse();
 // check the structure of the evaluator tree
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidQuery() {
 QueryParser parser = new QueryParser("div class='myclass' #myid > ul > li:nth-child(2) > a[href^='http']");
 parser.parse();
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidCharSequence() {
 QueryParser parser = new QueryParser("div\\\\ class='myclass' #myid > ul > li:nth-child(2) > a[href^='http']");
 parser.parse();
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidCombinator() {
 QueryParser parser = new QueryParser("div > + ul > li:nth-child(2) > a[href^='http']");
 parser.parse();
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseMissingRightParen() {
 QueryParser parser = new QueryParser("div.myclass #myid > ul > li:nth-child(2) > a[href^='http'");
 parser.parse();
 }
}