package org.jsoup.select.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Element;
import org.jsoup.helper.ElementHelper;
import org.jsoup.helper.StringUtil;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.And;
import org.jsoup.select.Or;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombiningEvaluatorTest {

 private static Element createTestElement() {
 return ElementHelper.parse("<div><p><a href='link1'>Link 1</a></p><p><a href='link2'>Link 2</a></p></div>");
 }

 @Test
 public void testAnd() {
 // Test basic functionality
 And andEvaluator = new And(Arrays.asList(
 new ElementNameEvaluator("p"),
 new ElementAttributeEvaluator("a", "href", "link1")
 ));
 Element testElement = createTestElement();
 assertTrue(andEvaluator.matches(testElement, testElement));

 // Test edge case: empty list of evaluators
 And emptyAndEvaluator = new And(new ArrayList<>());
 assertFalse(emptyAndEvaluator.matches(testElement, testElement));

 // Test exception handling: null root or node
 try {
 andEvaluator.matches(null, testElement);
 fail("Should have thrown NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }

 try {
 andEvaluator.matches(testElement, null);
 fail("Should have thrown NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testOr() {
 // Test basic functionality
 Or orEvaluator = new Or(
 Arrays.asList(
 new ElementNameEvaluator("p"),
 new ElementAttributeEvaluator("a", "href", "link1")
 ),
 new ElementNameEvaluator("p")
 );
 Element testElement = createTestElement();
 assertTrue(orEvaluator.matches(testElement, testElement));

 // Test edge case: empty list of evaluators
 Or emptyOrEvaluator = new Or(new ArrayList<>());
 assertFalse(emptyOrEvaluator.matches(testElement, testElement));

 // Test exception handling: null root or node
 try {
 orEvaluator.matches(null, testElement);
 fail("Should have thrown NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }

 try {
 orEvaluator.matches(testElement, null);
 fail("Should have thrown NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }

 // Test exception handling: NullPointerException when evaluating an evaluator
 And nestedAndEvaluator = new And(
 Arrays.asList(
 new ElementNameEvaluator("p"),
 new ElementAttributeEvaluator("a", "href", null) // NullPointerException is thrown here
 )
 );
 Or orEvaluatorWithNestedNullException = new Or(nestedAndEvaluator, new ElementNameEvaluator("p"));
 assertFalse(orEvaluatorWithNestedNullException.matches(testElement, testElement));
 }

 class ElementNameEvaluator extends Evaluator {
 private final String name;

 ElementNameEvaluator(String name) {
 this.name = name;
 }

 @Override
 public boolean matches(Element root, Element node) {
 return name.equals(node.tagName());
 }
 }

 class ElementAttributeEvaluator extends Evaluator {
 private final String tagName;
 private final String attributeName;
 private final String attributeValue;

 ElementAttributeEvaluator(String tagName, String attributeName, String attributeValue) {
 this.tagName = tagName;
 this.attributeName = attributeName;
 this.attributeValue = attributeValue;
 }

 @Override
 public boolean matches(Element root, Element node) {
 return StringUtil.equalsIgnoreCase(node.tagName(), tagName)
 && StringUtil.equalsIgnoreCase(node.attr(attributeName), attributeValue);
 }
 }
}