package org.jsoup.select.test;

import org.junit.Test;
import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.And;
import org.jsoup.select.Or;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CombiningEvaluatorTest {

 @Test
 public void testAndEvaluator() {
 // Create a list of evaluators for the And test
 List<Evaluator> evaluatorsAnd = new ArrayList<>();
 evaluatorsAnd.add(new ElementEvaluator("class", "testClass1"));
 evaluatorsAnd.add(new ElementEvaluator("class", "testClass2"));

 // Create the And evaluator with the list of evaluators
 And and = new And(evaluatorsAnd);

 // Test the And evaluator with a valid element
 Element elementValid = new Element("div");
 elementValid.classes().add("testClass1");
 elementValid.classes().add("testClass2");
 assertTrue(and.matches(null, elementValid));

 // Test the And evaluator with an invalid element
 Element elementInvalid = new Element("div");
 elementInvalid.classes().add("testClass1");
 assertFalse(and.matches(null, elementInvalid));
 }

 @Test
 public void testOrEvaluator() {
 // Create a list of evaluators for the first clause of the Or test
 List<Evaluator> evaluatorsOrFirstClause = new ArrayList<>();
 evaluatorsOrFirstClause.add(new ElementEvaluator("class", "testClass1"));
 evaluatorsOrFirstClause.add(new ElementEvaluator("class", "testClass2"));
 And andOrFirstClause = new And(evaluatorsOrFirstClause);

 // Create the Or evaluator with the first clause and an additional evaluator
 Or or = new Or();
 or.add(andOrFirstClause);
 or.add(new ElementEvaluator("class", "testClass3"));

 // Test the Or evaluator with a valid element
 Element elementValid = new Element("div");
 elementValid.classes().add("testClass1");
 elementValid.classes().add("testClass2");
 assertTrue(or.matches(null, elementValid));

 // Test the Or evaluator with an element that matches only the second clause
 Element elementValidSecondClause = new Element("div");
 elementValidSecondClause.classes().add("testClass3");
 assertTrue(or.matches(null, elementValidSecondClause));

 // Test the Or evaluator with an invalid element
 Element elementInvalid = new Element("div");
 assertFalse(or.matches(null, elementInvalid));
 }
}

class ElementEvaluator extends AbstractElementMatcher {
 private String attributeName;
 private String attributeValue;

 ElementEvaluator(String attributeName, String attributeValue) {
 this.attributeName = attributeName;
 this.attributeValue = attributeValue;
 }

 @Override
 public boolean matches(Element root, Element node) {
 return node.attribute(attributeName).equals(attributeValue);
 }

 @Override
 public String toString() {
 return String.format("[%s='%s']", attributeName, attributeValue);
 }
}