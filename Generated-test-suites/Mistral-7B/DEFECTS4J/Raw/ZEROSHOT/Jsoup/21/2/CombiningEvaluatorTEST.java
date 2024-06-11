package org.jsoup.select.test;

import org.junit.Test;
import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator;
import org.jsoup.select.And;
import org.jsoup.select.Or;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CombiningEvaluatorTest {

 @Test
 public void testAndEvaluator() {
 // Test empty And evaluator
 And emptyAnd = new And();
 assertTrue(emptyAnd.matches(new Element("div"), new Element("div")));

 // Test And evaluator with one evaluator
 And oneEvaluator = new And(new ElementSelector("div"));
 assertTrue(oneEvaluator.matches(new Element("div"), new Element("div")));

 // Test And evaluator with multiple evaluators
 And multipleEvaluators = new And(Arrays.asList(
 new ElementSelector("div"),
 new ClassSelector(Element.CLASS, "class1"),
 new AttributeSelector("data-test", "test")
 ));
 Element testElement = new Element("div").addClass("class1").attr("data-test", "test");
 assertTrue(multipleEvaluators.matches(new Element("div"), testElement));

 // Test And evaluator with incorrect element
 And incorrectElement = new And(new ElementSelector("div"), new ElementSelector("span"));
 assertFalse(incorrectElement.matches(new Element("div"), new Element("div")));
 }

 @Test
 public void testOrEvaluator() {
 // Test empty Or evaluator
 Or emptyOr = new Or();
 assertTrue(emptyOr.matches(new Element("div"), new Element("div")));

 // Test Or evaluator with one evaluator
 Or oneEvaluator = new Or(new ElementSelector("div"));
 assertTrue(oneEvaluator.matches(new Element("div"), new Element("div")));

 // Test Or evaluator with multiple evaluators (ANDed together as the first clause)
 Or multipleEvaluatorsAndFirstClause = new Or(Arrays.asList(
 new And(new ElementSelector("div"), new ClassSelector(Element.CLASS, "class1")),
 new ElementSelector("span")
 ));
 assertFalse(multipleEvaluatorsAndFirstClause.matches(new Element("div"), new Element("div")));

 // Test Or evaluator with multiple evaluators (ORed together)
 Or multipleEvaluators = new Or();
 multipleEvaluators.add(new ElementSelector("div"));
 multipleEvaluators.add(new ElementSelector("span"));
 assertTrue(multipleEvaluators.matches(new Element("div"), new Element("div")));
 assertTrue(multipleEvaluators.matches(new Element("span"), new Element("span")));
 }

 @Test
 public void testCombiningEvaluatorNesting() {
 // Test nesting And and Or evaluators
 List<Evaluator> evaluators = new ArrayList<>();
 evaluators.add(new ElementSelector("div"));
 evaluators.add(new And(
 new ElementSelector("div"),
 new ClassSelector(Element.CLASS, "class1"),
 new AttributeSelector("data-test", "test")
 ));
 evaluators.add(new Or(
 new ElementSelector("span"),
 new And(
 new ElementSelector("div"),
 new ClassSelector(Element.CLASS, "class2"),
 new AttributeSelector("data-test2", "test2")
 )
 ));

 Or combiningEvaluator = new Or(evaluators);
 Element testElement = new Element("div")
 .addClass("class1")
 .attr("data-test", "test")
 .appendElement(new Element("span")
 .addClass("class2")
 .attr("data-test2", "test2")
 );
 assertTrue(combiningEvaluator.matches(new Element("div"), testElement));
 }

}