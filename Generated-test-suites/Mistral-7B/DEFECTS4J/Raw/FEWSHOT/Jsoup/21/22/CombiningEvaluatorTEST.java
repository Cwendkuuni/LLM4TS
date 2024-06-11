package org.jsoup.select;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.jsoup.helper.StringUtil.join;

import org.junit.Test;

public class CombiningEvaluatorTest {

// Testing base class methods
@Test
public void testCombiningEvaluatorCreating() {
CombiningEvaluator ce = new CombiningEvaluator();
assertNotNull(ce);
}

// Testing And evaluator
@Test
public void testAndEvaluator() {
// Testing And evaluator with no evaluators
And andEvaluatorEmpty = new CombiningEvaluator.And(null);
assertTrue(andEvaluatorEmpty.matches(null, null));

// Testing And evaluator with one evaluator
Evaluator evaluator1 = new ElementStartsWithEvaluator("div");
And andEvaluatorOneEvaluator = new CombiningEvaluator.And(evaluator1);
Element root = new Element("html");
Element node = new Element("div");
assertTrue(andEvaluatorOneEvaluator.matches(root, node));

// Testing And evaluator with multiple evaluators
Evaluator evaluator1_1 = new ElementStartsWithEvaluator("div");
Evaluator evaluator1_2 = new ElementHasClassEvaluator("class1");
And andEvaluatorMultipleEvaluators = new CombiningEvaluator.And(evaluator1_1, evaluator1_2);
root = new Element("html");
node = new Element("div").addClass("class1");
assertTrue(andEvaluatorMultipleEvaluators.matches(root, node));

// Testing And evaluator with conflicting evaluators
Evaluator evaluator2 = new ElementDoesNotHaveClassEvaluator("class1");
andEvaluatorMultipleEvaluators = new CombiningEvaluator.And(evaluator1_1, evaluator2);
assertFalse(andEvaluatorMultipleEvaluators.matches(root, node));
}

// Testing Or evaluator
@Test
public void testOrEvaluator() {
// Testing Or evaluator with no evaluators
Or orEvaluatorEmpty = new CombiningEvaluator.Or(null);
assertTrue(orEvaluatorEmpty.matches(null, null));

// Testing Or evaluator with one evaluator
Evaluator evaluator1 = new ElementStartsWithEvaluator("div");
Or orEvaluatorOneEvaluator = new CombiningEvaluator.Or(evaluator1);
Element root = new Element("html");
Element node1 = new Element("div");
assertTrue(orEvaluatorOneEvaluator.matches(root, node1));

// Testing Or evaluator with multiple evaluators
Evaluator evaluator1_1 = new ElementStartsWithEvaluator("div");
Evaluator evaluator1_2 = new ElementHasClassEvaluator("class1");
Or orEvaluatorMultipleEvaluators = new CombiningEvaluator.Or(evaluator1_1, evaluator1_2);
Element node2 = new Element("span").addClass("class1");
assertTrue(orEvaluatorMultipleEvaluators.matches(root, node1));
assertTrue(orEvaluatorMultipleEvaluators.matches(root, node2));

// Testing Or evaluator with conflicting evaluators
Evaluator evaluator2 = new ElementDoesNotHaveClassEvaluator("class1");
Or orEvaluatorMultipleEvaluatorsConflicting = new CombiningEvaluator.Or(evaluator1_1, evaluator2);
assertFalse(orEvaluatorMultipleEvaluatorsConflicting.matches(root, node1));
assertTrue(orEvaluatorMultipleEvaluatorsConflicting.matches(root, node2));
}

// Testing And evaluator with multiple And and Or evaluators
@Test
public void testComplexCombiningEvaluator() {
Evaluator evaluator1_1 = new ElementStartsWithEvaluator("div");
Evaluator evaluator1_2 = new ElementHasClassEvaluator("class1");
And andEvaluator1 = new CombiningEvaluator.And(evaluator1_1, evaluator1_2);

Evaluator evaluator2_1 = new ElementStartsWithEvaluator("span");
Evaluator evaluator2_2 = new ElementHasClassEvaluator("class2");
And andEvaluator2 = new CombiningEvaluator.And(evaluator2_1, evaluator2_2);

Or orEvaluator = new CombiningEvaluator.Or(andEvaluator1, andEvaluator2);

Element root = new Element("html");
Element node1 = new Element("div").addClass("class1");
Element node2 = new Element("span").addClass("class2");
Element node3 = new Element("div").addClass("class1");
Element node4 = new Element("div");

assertTrue(orEvaluator.matches(root, node1));
assertTrue(orEvaluator.matches(root, node2));
assertTrue(orEvaluator.matches(root, node3));
assertFalse(orEvaluator.matches(root, node4));
}

// Testing toString method
@Test
public void testToString() {
And andEvaluatorMultipleEvaluators = new CombiningEvaluator.And(new ElementStartsWithEvaluator("div"), new ElementHasClassEvaluator("class1"));
assertEquals("div:hasClass(class1)", andEvaluatorMultipleEvaluators.toString());

Or orEvaluatorMultipleEvaluators = new CombiningEvaluator.Or(new ElementStartsWithEvaluator("div"), new ElementHasClassEvaluator("class1"));
assertEquals(":or(div, div:hasClass(class1))", orEvaluatorMultipleEvaluators.toString());
}
}