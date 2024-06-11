package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class QueryParserTest {

 private String query;
 private QueryParser parser;

 @Test
 public void testParseEmptyQuery() {
 query = "";
 parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.class, evaluator.getClass());
 }

 @Test
 public void testParseSingleTag() {
 query = "div";
 parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.Tag.class, evaluator.getClass());
 assertEquals("div", evaluator.asTag().getName());
 }

 @Test
 public void testParseSingleClass() {
 query = ".class1";
 parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.Class.class, evaluator.getClass());
 assertEquals("class1", evaluator.asClass().getValue());
 }

 @Test
 public void testParseSingleId() {
 query = "#id1";
 parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.Id.class, evaluator.getClass());
 assertEquals("id1", evaluator.asId().getValue());
 }

 @Test
 public void testParseCombinedTags() {
 query = "div.class1.class2";
 parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
 List<Evaluator> evals = ((CombiningEvaluator.And) evaluator).getEvaluators();
 assertEquals(2, evals.size());
 Evaluator first = evals.get(0);
 assertEquals(Evaluator.Tag.class, first.getClass());
 assertEquals("div", first.asTag().getName());
 Evaluator second = evals.get(1);
 assertEquals(Evaluator.Class.class, second.getClass());
 assertEquals("class2", second.asClass().getValue());
 }

 @Test
 public void testParseCombinedClasses() {
 query = ".class1.class2.class3";
 parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
 List<Evaluator> evals = ((CombiningEvaluator.And) evaluator).getEvaluators();
 assertEquals(3, evals.size());
 Evaluator first = evals.get(0);
 assertEquals(Evaluator.Class.class, first.getClass());
 assertEquals("class1", first.asClass().getValue());
 Evaluator second = evals.get(1);
 assertEquals(Evaluator.Class.class, second.getClass());
 assertEquals("class3", second.asClass().getValue());
 Evaluator third = evals.get(2);
 assertEquals(Evaluator.Class.class, third.getClass());
 assertEquals("class2", third.asClass().getValue());
 }

 @Test
 public void testParseTagWithAttribute() {
 query = "div[title='Test']";
 parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.Tag.class, evaluator.getClass());
 Evaluator attributeEvaluator = ((TagEvaluator) evaluator.asTag()).getFirstAttribute();
 assertEquals("title", attributeEvaluator.asAttribute().getName());
 assertEquals("Test", attributeEvaluator.asAttribute().getValue());
 }

 @Test
 public void testParseTagWithMultipleAttributes() {
 query = "div[title='Test'][class='testClass']";
 parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
 List<Evaluator> evals = ((CombiningEvaluator.And) evaluator).getEvaluators();
 assertEquals(2, evals.size());
 Evaluator first = evals.get(0);
 assertEquals(Evaluator.Tag.class, first.getClass());
 assertEquals("div", first.asTag().getName());
 Evaluator second = evals.get(1);
 assertEquals(CombiningEvaluator.And.class, second.getClass());
 List<Evaluator> secondEvals = ((CombiningEvaluator.And) second).getEvaluators();
 assertEquals(2, secondEvals.size());
 Evaluator secondFirst = secondEvals.get(0);
 assertEquals(Evaluator.Attribute.class, secondFirst.getClass());
 assertEquals("title", secondFirst.asAttribute().getName());
 assertEquals("Test", secondFirst.asAttribute().getValue());
 Evaluator secondSecond = secondEvals.get(1);
 assertEquals(Evaluator.Class.class, secondSecond.getClass());
 assertEquals("testClass", secondSecond.asClass().getValue());
 }

 @Test
 public void testParseTagWithAttributeValueContains() {
 query = "div[title*='Test']";
 parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.ContainsText.class, evaluator.getClass());
 assertEquals("Test", evaluator.asContainsText().getText());
 }

 @Test
 public void testParseTagWithAttributeValueEquals() {
 query = "div[title='Test']";
 parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.AttributeWithValue.class, evaluator.getClass());
 assertEquals("title", evaluator.asAttributeWithValue().getName());
 assertEquals("Test", evaluator.asAttributeWithValue().getValue());
 }

 @Test
 public void testParseTagWithAttributeValueStartsWith() {
 query = "div[title^='Test']";
 parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.AttributeWithValueStarting.class, evaluator.getClass());
 assertEquals("title", evaluator.asAttributeWithValueStarting().getName());
 assertEquals("Test", evaluator.asAttributeWithValueStarting().getValue());
 }

 @Test
 public void testParseTagWithAttributeValueEndsWith() {
 query = "div[title$='Test']";
 parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.AttributeWithValueEnding.class, evaluator.getClass());
 assertEquals("title", evaluator.asAttributeWithValueEnding().getName());
 assertEquals("Test", evaluator.asAttributeWithValueEnding().getValue());
 }

 @Test
 public void testParseTagWithAttributeValueMatching() {
 query = "div[title~='Test']";
 parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.AttributeWithValueMatching.class, evaluator.getClass());
 assertEquals("title", evaluator.asAttributeWithValueMatching().getName());
 assertEquals(Pattern.compile("Test"), evaluator.asAttributeWithValueMatching().getPattern());
 }

 @Test
 public void testParseTagWithAttributeValueNot() {
 query = "div:not([title='Test'])";
 parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 assertEquals(StructuralEvaluator.Not.class, evaluator.getClass());
 Evaluator notEvaluator = ((StructuralEvaluator.Not) evaluator).getEvaluator();
 assertEquals(Evaluator.AttributeWithValue.class, notEvaluator.getClass());
 assertEquals("title", notEvaluator.asAttributeWithValue().getName());
 assertEquals("Test", notEvaluator.asAttributeWithValue().getValue());
 }

 @Test
 public void testParseTagWithDescendant() {
 query = "div ul li";
 parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
 List<Evaluator> evals = ((CombiningEvaluator.And) evaluator).getEvaluators();
 assertEquals(3, evals.size());
 Evaluator first = evals.get(0);
 assertEquals(Evaluator.Tag.class, first.getClass());
 assertEquals("div", first.asTag().getName());
 Evaluator second = evals.get(1);
 assertEquals(Evaluator.Class.class, second.getClass());
 assertEquals("ul", second.asClass().getValue());
 Evaluator third = evals.get(2);
 assertEquals(Evaluator.Tag.class, third.getClass());
 assertEquals("li", third.asTag().getName());
 }

 @Test
 public void testParseTagWithAncestor() {
 query = "li > ul > li";
 parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
 List<Evaluator> evals = ((CombiningEvaluator.And) evaluator).getEvaluators();
 assertEquals(3, evals.size());
 Evaluator first = evals.get(0);
 assertEquals(Evaluator.Tag.class, first.getClass());
 assertEquals("li", first.asTag().getName());
 Evaluator second = evals.get(1);
 assertEquals(Evaluator.Class.class, second.getClass());
 assertEquals("ul", second.asClass().getValue());
 Evaluator third = evals.get(2);
 assertEquals(Evaluator.Tag.class, third.getClass());
 assertEquals("li", third.asTag().getName());
 }

 @Test
 public void testParseTagWithAdjacentSibling() {
 query = "li ~ ul > li";
 parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
 List<Evaluator> evals = ((CombiningEvaluator.And) evaluator).getEvaluators();
 assertEquals(3, evals.size());
 Evaluator first = evals.get(0);
 assertEquals(Evaluator.Tag.class, first.getClass());
 assertEquals("li", first.asTag().getName());
 Evaluator second = evals.get(1);
 assertEquals(Evaluator.Class.class, second.getClass());
 assertEquals("ul", second.asClass().getValue());
 Evaluator third = evals.get(2);
 assertEquals(Evaluator.Tag.class, third.getClass());
 assertEquals("li", third.asTag().getName());
 }

 @Test
 public void testParseTagWithGeneralSibling() {
 query = "li + ul > li";
 parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
 List<Evaluator> evals = ((CombiningEvaluator.And) evaluator).getEvaluators();
 assertEquals(3, evals.size());
 Evaluator first = evals.get(0);
 assertEquals(Evaluator.Tag.class, first.getClass());
 assertEquals("li", first.asTag().getName());
 Evaluator second = evals.get(1);
 assertEquals(Evaluator.Class.class, second.getClass());
 assertEquals("ul", second.asClass().getValue());
 Evaluator third = evals.get(2);
 assertEquals(Evaluator.Tag.class, third.getClass());
 assertEquals("li", third.asTag().getName());
 }

 @Test
 public void testParseTagWithAttributeWithOperator() {
 query = "div[title='Test'][data-foo='bar']";
 parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
 List<Evaluator> evals = ((CombiningEvaluator.And) evaluator).getEvaluators();
 assertEquals(2, evals.size());
 Evaluator first = evals.get(0);
 assertEquals(Evaluator.Tag.class, first.getClass());
 assertEquals("div", first.asTag().getName());
 Evaluator second = evals.get(1);
 assertEquals(CombiningEvaluator.And.class, second.getClass());
 List<Evaluator> secondEvals = ((CombiningEvaluator.And) second).getEvaluators();
 assertEquals(2, secondEvals.size());
 Evaluator secondFirst = secondEvals.get(0);
 assertEquals(Evaluator.Attribute.class, secondFirst.getClass());
 assertEquals("title", secondFirst.asAttribute().getName());
 assertEquals("Test", secondFirst.asAttribute().getValue());
 Evaluator secondSecond = secondEvals.get(1);
 assertEquals(Evaluator.Attribute.class, secondSecond.getClass());
 assertEquals("data-foo", secondSecond.asAttribute().getName());
 assertEquals("bar", secondSecond.asAttribute().getValue());
 }

 @Test
 public void testParseTagWithAttributeWithMultipleValues() {
 query = "div[title='Test1' class='class1 class2']";
 parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
 List<Evaluator> evals = ((CombiningEvaluator.And) evaluator).getEvaluators();
 assertEquals(2, evals.size());
 Evaluator first = evals.get(0);
 assertEquals(Evaluator.Tag.class, first.getClass());
 assertEquals("div", first.asTag().getName());
 Evaluator second = evals.get(1);
 assertEquals(CombiningEvaluator.And.class, second.getClass());
 List<Evaluator> secondEvals = ((CombiningEvaluator.And) second).getEvaluators();
 assertEquals(2, secondEvals.size());
 Evaluator secondFirst = secondEvals.get(0);
 assertEquals(Evaluator.Attribute.class, secondFirst.getClass());
 assertEquals("title", secondFirst.asAttribute().getName());
 assertEquals("Test1", secondFirst.asAttribute().getValue());
 Evaluator secondSecond = secondEvals.get(1);
 assertEquals(CombiningEvaluator.And.class, secondSecond.getClass());
 List<Evaluator> secondSecondEvals = ((CombiningEvaluator.And) secondSecond).getEvaluators();
 assertEquals(2, secondSecondEvals.size());
 Evaluator secondSecondFirst = secondSecondEvals.get(0);
 assertEquals(Evaluator.Class.class, secondSecondFirst.getClass());
 assertEquals("class1", secondSecondFirst.asClass().getValue());
 Evaluator secondSecondSecond = secondSecondEvals.get(1);
 assertEquals(Evaluator.Class.class, secondSecondSecond.getClass());
 assertEquals("class2", secondSecondSecond.asClass().getValue());
 }

 @Test
 public void testParseTagWithPseudoClass() {
 query = "li:first-child";
 parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.PseudoClass.class, evaluator.getClass());
 assertEquals("first-child", evaluator.asPseudoClass().getName());
 }

 @Test
 public void testParseTagWithPseudoElement() {
 query = "li::before";
 parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.PseudoElement.class, evaluator.getClass());
 assertEquals("before", evaluator.asPseudoElement().getName());
 }

 @Test
 public void testParseTagWithMultiplePseudoClasses() {
 query = "li.class1:hover:focus";
 parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
 List<Evaluator> evals = ((CombiningEvaluator.And) evaluator).getEvaluators();
 assertEquals(3, evals.size());
 Evaluator first = evals.get(0);
 assertEquals(Evaluator.Tag.class, first.getClass());
 assertEquals("li", first.asTag().getName());
 Evaluator second = evals.get(1);
 assertEquals(Evaluator.Class.class, second.getClass());
 assertEquals("class1", second.asClass().getValue());
 Evaluator third = evals.get(2);
 assertEquals(CombiningEvaluator.PseudoClass.class, third.getClass());
 assertEquals("hover", third.asPseudoClass().getName());
 assertEquals("focus", ((PseudoClassEvaluator) third).getPseudoElement().getValue());
 }
}