package org.jsoup.select;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class QueryParserTest {

 private QueryParser queryParser;

 @Before
 public void setUp() {
 queryParser = new QueryParser("#test");
 }

 @Test
 public void testParse() {
 Evaluator evaluator = queryParser.parse();
 assertNotNull(evaluator);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidQuery() {
 queryParser = new QueryParser("");
 }

 @Test
 public void testCombinator() {
 queryParser = new QueryParser("#test > .class1 > .class2");
 char combinator = queryParser.tq.consume();
 assertEquals(">", combinator);
 }

 @Test
 public void testFindElementsByTag() {
 queryParser = new QueryParser("div");
 Evaluator evaluator = queryParser.parse();
 assertEquals("div", evaluator.toString());
 }

 @Test
 public void testFindElementsById() {
 queryParser = new QueryParser("#test-id");
 Evaluator evaluator = queryParser.parse();
 assertEquals("#test-id", evaluator.toString());
 }

 @Test
 public void testFindElementsByClass() {
 queryParser = new QueryParser(".class1 .class2");
 Evaluator evaluator = queryParser.parse();
 assertEquals(".class1 .class2", evaluator.toString());
 }

 @Test
 public void testFindElementsByAttribute() {
 queryParser = new QueryParser("[title='Test']");
 Evaluator evaluator = queryParser.parse();
 assertEquals("[title='Test']", evaluator.toString());
 }

 @Test
 public void testFindAllElements() {
 queryParser = new QueryParser("*");
 Evaluator evaluator = queryParser.parse();
 assertEquals("*", evaluator.toString());
 }

 @Test
 public void testFindElementsByIndex() {
 queryParser = new QueryParser(":nth-child(2)");
 Evaluator evaluator = queryParser.parse();
 assertEquals(":nth-child(2)", evaluator.toString());
 }

 @Test
 public void testFindElementsByPseudoClass() {
 queryParser = new QueryParser(":first-child");
 Evaluator evaluator = queryParser.parse();
 assertEquals(":first-child", evaluator.toString());
 }

 @Test
 public void testFindElementsByHasSelector() {
 queryParser = new QueryParser(":has(#test)");
 Evaluator evaluator = queryParser.parse();
 assertEquals(":has(#test)", evaluator.toString());
 }

 @Test
 public void testFindElementsByContainsSelector() {
 queryParser = new QueryParser(":contains(Test)");
 Evaluator evaluator = queryParser.parse();
 assertEquals(":contains(Test)", evaluator.toString());
 }

 @Test
 public void testFindElementsByContainsDataSelector() {
 queryParser = new QueryParser(":containsData(dataTest)");
 Evaluator evaluator = queryParser.parse();
 assertEquals(":containsData(dataTest)", evaluator.toString());
 }

 @Test
 public void testFindElementsByMatchesSelector() {
 queryParser = new QueryParser(":matches(p:contains(Test))");
 Evaluator evaluator = queryParser.parse();
 assertEquals(":matches(p:contains(Test))", evaluator.toString());
 }

 @Test
 public void testFindElementsByNotSelector() {
 queryParser = new QueryParser(":not(.class1)");
 Evaluator evaluator = queryParser.parse();
 assertEquals(":not(.class1)", evaluator.toString());
 }

 @Test
 public void testFindElementsByCombinatorWithMultipleSelectors() {
 queryParser = new QueryParser("#test > .class1 + .class2");
 List<Evaluator> evaluators = queryParser.parse().getChildSelectors();
 assertEquals(2, evaluators.size());
 assertEquals("#test > .class1", evaluators.get(0).toString());
 assertEquals(".class2", evaluators.get(1).toString());
 }

 @Test
 public void testFindElementsByCombinatorWithMultiplePseudoClasses() {
 queryParser = new QueryParser("#test:first-child:last-child");
 Evaluator evaluator = queryParser.parse();
 assertEquals("#test:first-child:last-child", evaluator.toString());
 }

 @Test
 public void testFindElementsByAttributeWithDifferentOperators() {
 queryParser = new QueryParser("[title^='Test' and title$='Test']");
 Evaluator evaluator = queryParser.parse();
 assertEquals("[title^='Test' and title$='Test']", evaluator.toString());
 }

}