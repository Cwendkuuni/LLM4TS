package org.jsoup.select.test;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;
import org.jsoup.nodes.Element;
import org.jsoup.helper.ElementFinder;
import org.jsoup.helper.StringUtil;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.And;
import org.jsoup.select.Or;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for CombiningEvaluator and its subclasses And, Or.
 */
public class CombiningEvaluatorTest {

 private CombiningEvaluator andEvaluator;
 private CombiningEvaluator orEvaluator;
 private List<Element> elements;

 @Before
 public void setup() {
 elements = Arrays.asList(new ElementFinder.HtmlDocument().getElementsByTag("div").stream().limit(3).collect(Collectors.toList()));
 }

 @Test
 public void testAndConstructor() {
 andEvaluator = new And(Arrays.asList(new ElementSelector("div"), new ElementSelector("p")));
 }

 @Test
 public void testAndMatches() {
 Element node = elements.get(0);
 Assert.assertTrue(andEvaluator.matches(node, node));
 }

 @Test
 public void testAndDoesNotMatch() {
 Element node = elements.get(2);
 Assert.assertFalse(andEvaluator.matches(node, node));
 }

 @Test
 public void testOrConstructor() {
 orEvaluator = new Or(Arrays.asList(new ElementSelector("div"), new ElementSelector("p")));
 }

 @Test
 public void testOrAddEvaluator() {
 orEvaluator = new Or();
 orEvaluator.add(new ElementSelector("div"));
 orEvaluator.add(new ElementSelector("p"));
 }

 @Test
 public void testOrMatches() {
 Element node = elements.get(0); // div
 Assert.assertTrue(orEvaluator.matches(node, node));
 }

 @Test
 public void testOrMatchesWithEmptyList() {
 Assert.assertFalse(orEvaluator.matches(elements.get(0), elements.get(0)));
 }

 @Test
 public void testOrMatchesMultipleElements() {
 Element node1 = elements.get(0); // div
 Element node2 = elements.get(1); // div
 orEvaluator.add(new ElementSelector("div"));
 orEvaluator.add(new ElementSelector("p"));
 Assert.assertTrue(orEvaluator.matches(node1, node2));
 }

 @Test(expected = AssertionError.class)
 public void testOrMatchesDoesNotMatchAll() {
 Element node = elements.get(2); // not div nor p
 orEvaluator.add(new ElementSelector("div"));
 orEvaluator.add(new ElementSelector("p"));
 Assert.assertFalse(orEvaluator.matches(node, node));
 }
}