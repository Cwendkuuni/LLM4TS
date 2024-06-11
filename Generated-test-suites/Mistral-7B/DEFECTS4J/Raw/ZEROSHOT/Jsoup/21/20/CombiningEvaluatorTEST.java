package org.jsoup.select.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.ElementFinder;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Elements;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.And;
import org.jsoup.select.Or;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CombiningEvaluatorTest {

 private ElementFinder finder = new ElementFinder();

 @org.junit.Before
 public void setUp() {
 }

 @org.junit.Test
 public void testAndEvaluator() {
 // Create an Element for testing
 Document doc = new Document();
 Element root = doc.body();
 Element a = doc.createElement("a");
 Element b = doc.createElement("b");
 Element c = doc.createElement("c");

 root.appendChild(a);
 root.appendChild(b);
 root.appendChild(c);

 // Test Matches method with multiple Evaluators
 List<Evaluator> evaluatorsA = new ArrayList<>();
 evaluatorsA.add(new ElementNameEvaluator("a"));
 evaluatorsA.add(new ElementAttributeEvaluator("a", "class", "test"));
 And andEvaluatorA = new And(evaluatorsA);
 assertTrue(andEvaluatorA.matches(root, a));
 assertFalse(andEvaluatorA.matches(root, b));
 assertFalse(andEvaluatorA.matches(root, c));

 // Test Matches method with single Evaluator
 List<Evaluator> evaluatorsB = Collections.singletonList(new ElementNameEvaluator("a"));
 And andEvaluatorB = new And(evaluatorsB);
 assertTrue(andEvaluatorB.matches(root, a));
 assertFalse(andEvaluatorB.matches(root, b));
 assertFalse(andEvaluatorB.matches(root, c));

 // Test Matches method with empty evaluators list
 And andEvaluatorC = new And(Collections.emptyList());
 assertFalse(andEvaluatorC.matches(root, a));
 assertFalse(andEvaluatorC.matches(root, b));
 assertFalse(andEvaluatorC.matches(root, c));
 }

 @org.junit.Test
 public void testOrEvaluator() {
 // Create an Element for testing
 Document doc = new Document();
 Element root = doc.body();
 Element a = doc.createElement("a");
 Element b = doc.createElement("b");
 Element c = doc.createElement("c");

 root.appendChild(a);
 root.appendChild(b);
 root.appendChild(c);

 // Test Matches method with multiple Evaluators
 List<Evaluator> evaluatorsA = new ArrayList<>();
 evaluatorsA.add(new ElementNameEvaluator("a"));
 evaluatorsA.add(new ElementNameEvaluator("b"));
 Or orEvaluatorA = new Or(evaluatorsA);
 assertTrue(orEvaluatorA.matches(root, a));
 assertTrue(orEvaluatorA.matches(root, b));
 assertFalse(orEvaluatorA.matches(root, c));

 // Test Matches method with single Evaluator
 List<Evaluator> evaluatorsB = Collections.singletonList(new ElementNameEvaluator("a"));
 Or orEvaluatorB = new Or(evaluatorsB);
 assertTrue(orEvaluatorB.matches(root, a));
 assertFalse(orEvaluatorB.matches(root, b));
 assertFalse(orEvaluatorB.matches(root, c));

 // Test Add method
 Or orEvaluatorC = new Or();
 orEvaluatorC.add(new ElementNameEvaluator("a"));
 orEvaluatorC.add(new ElementNameEvaluator("b"));
 orEvaluatorC.add(new ElementNameEvaluator("c"));
 assertTrue(orEvaluatorC.matches(root, a));
 assertTrue(orEvaluatorC.matches(root, b));
 assertTrue(orEvaluatorC.matches(root, c));

 // Test toString method
 List<Evaluator> evaluatorsD = new ArrayList<>();
 evaluatorsD.add(new ElementNameEvaluator("a"));
 evaluatorsD.add(new ElementNameEvaluator("b"));
 Or orEvaluatorD = new Or(evaluatorsD);
 String expectedOrD = ":or(a b)";
 String actualOrD = orEvaluatorD.toString();
 assertTrue(StringUtil.equals(expectedOrD, actualOrD));
 }
}

class ElementNameEvaluator extends ElementEvaluator {
 @Override
 public boolean matches(Element root, Element node) {
 return node.tagName().equals("a");
 }
}

class ElementAttributeEvaluator extends AttributeEvaluator {
 @Override
 public boolean matches(Element root, Element node) {
 return node.className().equals("test");
 }
}