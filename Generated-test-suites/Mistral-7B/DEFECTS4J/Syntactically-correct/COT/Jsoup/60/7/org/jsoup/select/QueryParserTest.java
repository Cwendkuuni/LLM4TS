package org.jsoup.select;

import static org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class QueryParserTest {

 private QueryParser queryParser;

 @Before
 public void setUp() {
 queryParser = new QueryParser("div.class1 > div.class2 > div.class3");
 }

 @Test
 public void testParse() {
 Evaluator evaluator = queryParser.parse();
 assertNotNull(evaluator);
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseWithInvalidQuery() {
 queryParser = new QueryParser("div.*"); // unhandled character '.'
 queryParser.parse();
 fail("Expected Selector.SelectorParseException");
 }

 @Test
 public void testFindElements() {
 List<Evaluator> evaluators = new ArrayList<>();
 queryParser = new QueryParser("div.class1");
 queryParser.parse();
 evaluators.addAll(queryParser.evals);
 assertEquals(1, evaluators.size());
 assertTrue(evaluators.get(0) instanceof Evaluator.Class);

 queryParser = new QueryParser("div");
 queryParser.parse();
 evaluators.clear();
 evaluators.addAll(queryParser.evals);
 assertEquals(1, evaluators.size());
 assertTrue(evaluators.get(0) instanceof Evaluator.Tag);
 }

 @Test
 public void testFindElementById() {
 queryParser = new QueryParser("#id1");
 queryParser.parse();
 evaluators.clear();
 evaluators.addAll(queryParser.evals);
 assertEquals(1, evaluators.size());
 assertTrue(evaluators.get(0) instanceof Evaluator.Id);
 }

 @Test
 public void testFindElementByClass() {
 queryParser = new QueryParser(".class1");
 queryParser.parse();
 evaluators.clear();
 evaluators.addAll(queryParser.evals);
 assertEquals(1, evaluators.size());
 assertTrue(evaluators.get(0) instanceof Evaluator.Class);
 }

 @Test
 public void testFindElementByTag() {
 queryParser = new QueryParser("p");
 queryParser.parse();
 evaluators.clear();
 evaluators.addAll(queryParser.evals);
 assertEquals(1, evaluators.size());
 assertTrue(evaluators.get(0) instanceof Evaluator.Tag);
 }

 @Test
 public void testFindElementsByAttribute() {
 queryParser = new QueryParser("input[type='text']");
 queryParser.parse();
 evaluators.clear();
 evaluators.addAll(queryParser.evals);
 assertEquals(1, evaluators.size());
 assertTrue(evaluators.get(0) instanceof Evaluator.Attribute);
 }

 @Test
 public void testFindElementWithAttributeValue() {
 queryParser = new QueryParser("a[href='#']");
 queryParser.parse();
 evaluators.clear();
 evaluators.addAll(queryParser.evals);
 assertEquals(1, evaluators.size());
 assertTrue(evaluators.get(0) instanceof Evaluator.AttributeWithValue);
 }

 @Test
 public void testFindElementWithAttributeValueStarting() {
 queryParser = new QueryParser("a[href^='mailto:']");
 queryParser.parse();
 evaluators.clear();
 evaluators.addAll(queryParser.evals);
 assertEquals(1, evaluators.size());
 assertTrue(evaluators.get(0) instanceof Evaluator.AttributeWithValueStarting);
 }

 @Test
 public void testFindElementWithAttributeValueEnding() {
 queryParser = new QueryParser("a[href$='.png']");
 queryParser.parse();
 evaluators.clear();
 evaluators.addAll(queryParser.evals);
 assertEquals(1, evaluators.size());
 assertTrue(evaluators.get(0) instanceof Evaluator.AttributeWithValueEnding);
 }

 @Test
 public void testFindElementWithAttributeValueContaining() {
 queryParser = new QueryParser("a[href*='example']");
 queryParser.parse();
 evaluators.clear();
 evaluators.addAll(queryParser.evals);
 assertEquals(1, evaluators.size());
 assertTrue(evaluators.get(0) instanceof Evaluator.AttributeWithValueContaining);
 }

 @Test
 public void testFindElementWithAttributeValueMatching() {
 queryParser = new QueryParser("a[href~='example']");
 queryParser.parse();
 evaluators.clear();
 evaluators.addAll(queryParser.evals);
 assertEquals(1, evaluators.size());
 assertTrue(evaluators.get(0) instanceof Evaluator.AttributeWithValueMatching);
 }

 @Test
 public void testFindAllElements() {
 queryParser = new QueryParser("*:*");
 queryParser.parse();
 evaluators.clear();
 evaluators.addAll(queryParser.evals);
 assertEquals(1, evaluators.size());
 assertTrue(evaluators.get(0) instanceof Evaluator.AllElements);
 }

 @Test
 public void testFindElementWithIndex() {
 queryParser = new QueryParser("li:nth-child(2)");
 queryParser.parse();
 evaluators.clear();
 evaluators.addAll(queryParser.evals);
 assertEquals(1, evaluators.size());
 assertTrue(evaluators.get(0) instanceof Evaluator.IsNthChild);
 }

 @Test
 public void testFindElementWithIndexOfType() {
 queryParser = new QueryParser("li:nth-of-type(2)");
 queryParser.parse();
 evaluators.clear();
 evaluators.addAll(queryParser.evals);
 assertEquals(1, evaluators.size());
 assertTrue(evaluators.get(0) instanceof Evaluator.IsNthOfType);
 }

 @Test
 public void testFindElementWithIndexBackwards() {
 queryParser = new QueryParser("li:nth-last-child(2)");
 queryParser.parse();
 evaluators.clear();
 evaluators.addAll(queryParser.evals);
 assertEquals(1, evaluators.size());
 assertTrue(evaluators.get(0) instanceof Evaluator.IsNthLastChild);
 }

 @Test
 public void testFindElementWithIndexOfTypeBackwards() {
 queryParser = new QueryParser("li:nth-last-of-type(2)");
 queryParser.parse();
 evaluators.clear();
 evaluators.addAll(queryParser.evals);
 assertEquals(1, evaluators.size());
 assertTrue(evaluators.get(0) instanceof Evaluator.IsNthLastOfType);
 }

 @Test
 public void testFindElementFirstChild() {
 queryParser = new QueryParser("div > p:first-child");
 queryParser.parse();
 evaluators.clear();
 evaluators.addAll(queryParser.evals);
 assertEquals(1, evaluators.size());
 assertTrue(evaluators.get(0) instanceof Evaluator.IsFirstChild);
 }

 @Test
 public void testFindElementLastChild() {
 queryParser = new QueryParser("div > p:last-child");
 queryParser.parse();
 evaluators.clear();
 evaluators.addAll(queryParser.evals);
 assertEquals(1, evaluators.size());
 assertTrue(evaluators.get(0) instanceof Evaluator.IsLastChild);
 }

 @Test
 public void testFindElementFirstOfType() {
 queryParser = new QueryParser("div > p:first-of-type");
 queryParser.parse();
 evaluators.clear();
 evaluators.addAll(queryParser.evals);
 assertEquals(1, evaluators.size());
 assertTrue(evaluators.get(0) instanceof Evaluator.IsFirstOfType);
 }

 @Test
 public void testFindElementLastOfType() {
 queryParser = new QueryParser("div > p:last-of-type");
 queryParser.parse();
 evaluators.clear();
 evaluators.addAll(queryParser.evals);
 assertEquals(1, evaluators.size());
 assertTrue(evaluators.get(0) instanceof Evaluator.IsLastOfType);
 }

 @Test
 public void testFindElementEmpty() {
 queryParser = new QueryParser("li:empty");
 queryParser.parse();
 evaluators.clear();
 evaluators.addAll(queryParser.evals);
 assertEquals(1, evaluators.size());
 assertTrue(evaluators.get(0) instanceof Evaluator.IsEmpty);
 }

 @Test
 public void testFindElementRoot() {
 queryParser = new QueryParser(":root");
 queryParser.parse();
 evaluators.clear();
 evaluators.addAll(queryParser.evals);
 assertEquals(1, evaluators.size());
 assertTrue(evaluators.get(0) instanceof Evaluator.IsRoot);
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testFindElementWithInvalidCombinator() {
 queryParser = new QueryParser("div.class > > div.class2");
 queryParser.parse();
 fail("Expected Selector.SelectorParseException");
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testFindElementWithMissingCombinator() {
 queryParser = new QueryParser("div.class1 div.class2");
 queryParser.parse();
 fail("Expected Selector.SelectorParseException");
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testFindElementWithEmptySubQuery() {
 queryParser = new QueryParser("div.class:not()");
 queryParser.parse();
 fail("Expected Selector.SelectorParseException");
 }
}