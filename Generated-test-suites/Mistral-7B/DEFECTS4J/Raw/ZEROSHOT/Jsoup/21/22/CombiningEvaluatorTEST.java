package org.jsoup.select.test;

import org.junit.Test;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Elements;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.And;
import org.jsoup.select.Or;
import static org.junit.Assert.*;

public class CombiningEvaluatorTest {

 @Test
 public void testAndEvaluator() {
 // Given
 Element root = Element.parse("<html><body><div id='div1'><p class='p1 p2'>Test</p></div><div id='div2'><p class='p3'>Test</p></div></body></html>");
 And andEvaluator = new And(Arrays.asList(new ClassNameEvaluator("p1"), new ClassNameEvaluator("p2")));

 // When
 Element matchedElement = andEvaluator.matches(root, root.body());

 // Then
 assertTrue("Expected an element to be matched by And evaluator", matchedElement != null);
 assertEquals("Expected matched element to have id 'div1'", "div1", matchedElement.id());
 }

 @Test
 public void testAndEvaluator_NoMatch() {
 // Given
 Element root = Element.parse("<html><body><div id='div1'><p class='p1'>Test</p></div><div id='div2'><p class='p3'>Test</p></div></body></html>");
 And andEvaluator = new And(Arrays.asList(new ClassNameEvaluator("p1"), new ClassNameEvaluator("p4")));

 // When
 Element matchedElement = andEvaluator.matches(root, root.body());

 // Then
 assertFalse("Expected no match", matchedElement != null);
 }

 @Test
 public void testOrEvaluator() {
 // Given
 Element root = Element.parse("<html><body><div id='div1'><p class='p1 p2'>Test</p></div><div id='div2'><p class='p3 p1'>Test</p></div></body></html>");
 Or orEvaluator = new Or(Arrays.asList(new ClassNameEvaluator("p1"), new ClassNameEvaluator("p3")));

 // When
 Element matchedElement = orEvaluator.matches(root, root.body());

 // Then
 assertTrue("Expected an element to be matched by Or evaluator", matchedElement != null);
 }

 @Test
 public void testOrEvaluator_NoMatch() {
 // Given
 Element root = Element.parse("<html><body><div id='div1'><p class='p1'>Test</p></div><div id='div2'><p class='p4'>Test</p></div></body></html>");
 Or orEvaluator = new Or(Arrays.asList(new ClassNameEvaluator("p1"), new ClassNameEvaluator("p3")));

 // When
 Element matchedElement = orEvaluator.matches(root, root.body());

 // Then
 assertFalse("Expected no match", matchedElement != null);
 }

 @Test
 public void testOrEvaluator_AddEvaluator() {
 // Given
 Element root = Element.parse("<html><body><div id='div1'><p class='p1 p2'>Test</p></div><div id='div2'><p class='p3 p4'>Test</p></div></body></html>");
 Or orEvaluator = new Or();
 orEvaluator.add(new ClassNameEvaluator("p1"));
 orEvaluator.add(new ClassNameEvaluator("p3"));

 // When
 Element matchedElement = orEvaluator.matches(root, root.body());

 // Then
 assertTrue("Expected an element to be matched by Or evaluator", matchedElement != null);
 }

 @Test
 public void testOrEvaluator_MultipleMatches() {
 // Given
 Element root = Element.parse("<html><body><div id='div1'><p class='p1 p2'>Test</p></div><div id='div2'><p class='p3 p1 p4'>Test</p></div></body></html>");
 Or orEvaluator = new Or(Arrays.asList(new ClassNameEvaluator("p1"), new ClassNameEvaluator("p3")));

 // When
 Elements matchedElements = evaluator.findAll(root, orEvaluator);

 // Then
 assertEquals("Expected 2 elements to be matched by Or evaluator", 2, matchedElements.size());
 }
}