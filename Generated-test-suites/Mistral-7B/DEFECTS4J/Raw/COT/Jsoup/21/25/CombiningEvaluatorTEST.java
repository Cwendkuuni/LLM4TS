package org.jsoup.select.test;

import org.junit.Test;
import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator;
import org.jsoup.select.And;
import org.jsoup.select.Or;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CombiningEvaluatorTest {

 private Element createTestNode(String tagName, String id) {
 Element element = new Element("html");
 Element testNode = new Element(tagName, id);
 element.appendChild(testNode);
 return testNode;
 }

 // Testing the constructor and getters
 @Test
 public void testCombiningEvaluatorConstruction() {
 CombiningEvaluator ce = new CombiningEvaluator();
 assertTrue(ce instanceof CombiningEvaluator);
 assertTrue(ce.evaluators.isEmpty());

 CombiningEvaluator ce2 = new CombiningEvaluator(Arrays.asList(new And(new Evaluator[]{}), new Or(new Evaluator[]{})));
 assertTrue(ce2 instanceof CombiningEvaluator);
 assertFalse(ce2.evaluators.isEmpty());
 }

 // Testing And evaluator
 @Test
 public void testAndEvaluator() {
 // Basic functionality test
 And andEvaluator = new And(Arrays.asList(new ElementStartsWithEvaluator("div"), new ElementIdEvaluator("testId")));
 Element testNode1 = createTestNode("div", "testId");
 assertTrue(andEvaluator.matches(null, testNode1));

 Element testNode2 = createTestNode("p", "testId");
 assertFalse(andEvaluator.matches(null, testNode2));

 // Test with empty list
 And andEvaluatorEmpty = new And(Arrays.asList(new Evaluator[]{}));
 assertTrue(andEvaluatorEmpty.matches(null, null));

 // Test with single evaluator
 And andEvaluatorSingle = new And(Arrays.asList(new ElementStartsWithEvaluator("div")));
 Element testNode3 = createTestNode("div", "testId");
 assertTrue(andEvaluatorSingle.matches(null, testNode3));

 Element testNode4 = createTestNode("p", "testId");
 assertFalse(andEvaluatorSingle.matches(null, testNode4));

 // Test with multiple evaluators
 And andEvaluatorMultiple = new And(Arrays.asList(new ElementStartsWithEvaluator("div"), new ElementIdEvaluator("testId"), new ElementHasText("text")));
 Element testNode5 = createTestNode("div", "testId", "text");
 assertTrue(andEvaluatorMultiple.matches(null, testNode5));

 Element testNode6 = createTestNode("div", "testId", "otherText");
 assertFalse(andEvaluatorMultiple.matches(null, testNode6));

 Element testNode7 = createTestNode("p", "testId", "text");
 assertFalse(andEvaluatorMultiple.matches(null, testNode7));
 }

 // Testing Or evaluator
 @Test
 public void testOrEvaluator() {
 // Basic functionality test
 Or orEvaluator = new Or(Arrays.asList(new ElementStartsWithEvaluator("div"), new ElementStartsWithEvaluator("p")));
 Element testNode1 = createTestNode("div", "testId");
 assertTrue(orEvaluator.matches(null, testNode1));

 Element testNode2 = createTestNode("p", "testId");
 assertTrue(orEvaluator.matches(null, testNode2));

 // Test with empty list
 Or orEvaluatorEmpty = new Or(Collections.emptyList());
 assertTrue(orEvaluatorEmpty.matches(null, null));

 // Test with single evaluator
 Or orEvaluatorSingle = new Or(Arrays.asList(new ElementStartsWithEvaluator("div")));
 Element testNode3 = createTestNode("div", "testId");
 assertTrue(orEvaluatorSingle.matches(null, testNode3));

 Element testNode4 = createTestNode("p", "testId");
 assertFalse(orEvaluatorSingle.matches(null, testNode4));

 // Test with multiple evaluators
 List<Evaluator> evaluators = new ArrayList<>();
 evaluators.add(new ElementStartsWithEvaluator("div"));
 evaluators.add(new ElementIdEvaluator("testId"));
 Or orEvaluatorMultiple = new Or(evaluators);
 Element testNode5 = createTestNode("div", "testId");
 assertTrue(orEvaluatorMultiple.matches(null, testNode5));

 Element testNode6 = createTestNode("p", "testId");
 assertTrue(orEvaluatorMultiple.matches(null, testNode6));

 Element testNode7 = createTestNode("div", "testIdOther");
 assertFalse(orEvaluatorMultiple.matches(null, testNode7));

 Element testNode8 = createTestNode("p", "testIdOther");
 assertFalse(orEvaluatorMultiple.matches(null, testNode8));

 // Test adding new evaluator
 Or orEvaluatorAdd = new Or(Arrays.asList(new ElementStartsWithEvaluator("div")));
 orEvaluatorAdd.add(new ElementHasText("text"));
 Element testNode9 = createTestNode("div", "testId", "text");
 assertTrue(orEvaluatorAdd.matches(null, testNode9));

 Element testNode10 = createTestNode("div", "testId", "otherText");
 assertFalse(orEvaluatorAdd.matches(null, testNode10));
 }
}