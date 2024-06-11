package org.jsoup.select.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;

public class QueryParserTest {

 @Test
 public void testParseEmptyQuery() {
 QueryParser parser = new QueryParser("");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.AllElements);
 }

 @Test
 public void testParseSimpleTagSelector() {
 QueryParser parser = new QueryParser("p");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.Tag);
 assertEquals("p", evaluator.toString());
 }

 @Test
 public void testParseClassSelector() {
 QueryParser parser = new QueryParser(".class");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.Class);
 assertEquals(".class", evaluator.toString());
 }

 @Test
 public void testParseIdSelector() {
 QueryParser parser = new QueryParser("#id");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.Id);
 assertEquals("#id", evaluator.toString());
 }

 @Test
 public void testParseAttributeSelector() {
 QueryParser parser = new QueryParser("[title]");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.Attribute);
 assertEquals("[title]", evaluator.toString());
 }

 @Test
 public void testParseMultiSelector() {
 QueryParser parser = new QueryParser(".class1, .class2");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof CombiningEvaluator.Or);
 assertEquals(2, evaluator.getCombiningChildren().size());
 }

 @Test
 public void testParseDescendantSelector() {
 QueryParser parser = new QueryParser("div ul li a");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 Evaluator firstChild = evaluator.getCombiningChildren().get(0);
 assertTrue(firstChild instanceof StructuralEvaluator.Parent);
 Evaluator secondChild = evaluator.getCombiningChildren().get(1);
 assertTrue(secondChild instanceof Evaluator.Tag);
 assertEquals("a", secondChild.toString());
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidQuery() {
 QueryParser parser = new QueryParser(":invalid");
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseMissingCombinatorException() {
 QueryParser parser = new QueryParser("p.class");
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseUnsupportedSelectorType() {
 QueryParser parser = new QueryParser(":custom");
 }
}