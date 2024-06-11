package org.jsoup.select.test;

import org.junit.Test;
import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.And;
import org.jsoup.select.Evaluator;
import org.jsoup.select.Or;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CombiningEvaluatorTest {

 @Test
 public void testAndEvaluator() {
 // Given two evaluators
 Evaluator evaluator1 = new ElementStartsWithTagName("div");
 Evaluator evaluator2 = new ElementHasClass("class1");

 // When combining evaluators using And
 And andEvaluator = new And(Arrays.asList(evaluator1, evaluator2));

 // Then the And evaluator matches only elements that match both evaluators
 Element root = new Element("html");
 Element node1 = new Element("body").add("div", "class1");
 root.appendChild(node1);
 Element node2 = new Element("body").add("p", "class1");
 root.appendChild(node2);

 assertTrue(andEvaluator.matches(root, node1));
 assertFalse(andEvaluator.matches(root, node2));
 }

 @Test
 public void testOrEvaluator() {
 // Given three evaluators
 Evaluator evaluator1 = new ElementStartsWithTagName("div");
 Evaluator evaluator2 = new ElementHasClass("class1");
 Evaluator evaluator3 = new ElementHasClass("class2");

 // When combining evaluators using Or
 Or orEvaluator = new Or(Arrays.asList(evaluator1, evaluator2, evaluator3));

 // Then the Or evaluator matches elements that match any of the evaluators
 Element root = new Element("html");
 Element node1 = new Element("body").add("div", "class1");
 root.appendChild(node1);
 Element node2 = new Element("body").add("p", "class1");
 root.appendChild(node2);
 Element node3 = new Element("body").add("div", "class2");
 root.appendChild(node3);

 assertTrue(orEvaluator.matches(root, node1));
 assertTrue(orEvaluator.matches(root, node3));
 assertFalse(orEvaluator.matches(root, node2));
 }

 @Test
 public void testOrAddEvaluator() {
 // Given two evaluators and an Or evaluator with no initial evaluators
 Or orEvaluator = new Or();

 // When adding two evaluators to the Or evaluator
 orEvaluator.add(new ElementStartsWithTagName("div"));
 orEvaluator.add(new ElementHasClass("class1"));

 // Then the Or evaluator matches elements that match any of the added evaluators
 Element root = new Element("html");
 Element node1 = new Element("body").add("div", "class1");
 root.appendChild(node1);
 Element node2 = new Element("body").add("p", "class1");
 root.appendChild(node2);
 Element node3 = new Element("body").add("div", "class2");
 root.appendChild(node3);

 assertTrue(orEvaluator.matches(root, node1));
 assertTrue(orEvaluator.matches(root, node3));
 assertFalse(orEvaluator.matches(root, node2));
 }
}