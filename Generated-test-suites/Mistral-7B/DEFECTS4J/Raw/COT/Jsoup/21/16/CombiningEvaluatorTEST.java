import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class CombiningEvaluatorTest {

 private CombiningEvaluator andEvaluator;
 private CombiningEvaluator orEvaluator;

 @Test
 public void testAndEvaluator_multipleEvaluators_allMatch_returnsTrue() {
     List<Evaluator> evaluators = Arrays.asList(new ElementNameEvaluator("div"), new ElementAttributeEvaluator("class", "test"));
     andEvaluator = new CombiningEvaluator.And(evaluators);

     Element element = new Element("div");
     element.addClass("test");

     assertTrue(andEvaluator.matches(null, element));
 }

 @Test
 public void testAndEvaluator_oneEvaluatorDoesNotMatch_returnsFalse() {
     List<Evaluator> evaluators = Arrays.asList(new ElementNameEvaluator("div"), new ElementAttributeEvaluator("class", "notTest"));
     andEvaluator = new CombiningEvaluator.And(evaluators);

     Element element = new Element("div");
     element.addClass("notTest");

     assertFalse(andEvaluator.matches(null, element));
 }

 @Test
 public void testAndEvaluator_toString_returnsCorrectCombinationOfEvaluators() {
     List<Evaluator> evaluators = Arrays.asList(new ElementNameEvaluator("div"), new ElementAttributeEvaluator("class", "test"));
     andEvaluator = new CombiningEvaluator.And(evaluators);

     String expected = "div:hasClass(test)";
     String actual = andEvaluator.toString();

     assertTrue(expected.equals(actual));
 }

 @Test
 public void testOrEvaluator_multipleClauses_anyOneMatch_returnsTrue() {
     List<Evaluator> evaluators = Arrays.asList(new ElementNameEvaluator("div"), new ElementNameEvaluator("span"));
     orEvaluator = new CombiningEvaluator.Or(evaluators);

     Element divElement = new Element("div");
     Element spanElement = new Element("span");

     assertTrue(orEvaluator.matches(null, divElement));
     assertTrue(orEvaluator.matches(null, spanElement));
 }

 @Test
 public void testOrEvaluator_noClausesMatch_returnsFalse() {
     List<Evaluator> evaluators = Collections.singletonList(new ElementNameEvaluator("div"));
     orEvaluator = new CombiningEvaluator.Or(evaluators);

     Element element = new Element("span");

     assertFalse(orEvaluator.matches(null, element));
 }

 @Test
 public void testOrEvaluator_addNewClause_newClauseMatches_returnsTrue() {
     List<Evaluator> evaluators = Arrays.asList(new ElementNameEvaluator("div"));
     orEvaluator = new CombiningEvaluator.Or(evaluators);

     orEvaluator.add(new ElementAttributeEvaluator("class", "test"));

     Element element = new Element("div");
     element.addClass("test");

     assertTrue(orEvaluator.matches(null, element));
 }

 @Test
 public void testOrEvaluator_addNewClause_newClauseDoesNotMatch_returnsFalseIfNoneMatches() {
     List<Evaluator> evaluators = Arrays.asList(new ElementNameEvaluator("div"));
     orEvaluator = new CombiningEvaluator.Or(evaluators);

     orEvaluator.add(new ElementAttributeEvaluator("class", "notTest"));

     Element element = new Element("div");

     assertFalse(orEvaluator.matches(null, element));
 }

 @Test
 public void testOrEvaluator_toString_returnsCorrectFormat() {
     List<Evaluator> evaluators = Arrays.asList(new ElementNameEvaluator("div"), new ElementNameEvaluator("span"));
     orEvaluator = new CombiningEvaluator.Or(evaluators);

     orEvaluator.add(new ElementAttributeEvaluator("class", "test"));

     String expected = ":or(div, span:hasClass(test))";
     String actual = orEvaluator.toString();

     assertTrue(expected.equals(actual));
 }

 private static class ElementAttributeEvaluator extends AbstractElementEvaluator {
     private final String attributeName;
     private final String attributeValue;

     public ElementAttributeEvaluator(String attributeName, String attributeValue) {
         this.attributeName = attributeName;
         this.attributeValue = attributeValue;
     }

     @Override
     public boolean matches(Element root, Element node) {
         return node.attribute(attributeName).equals(attributeValue);
     }
 }

 private static class ElementNameEvaluator extends AbstractElementEvaluator {
     private final String elementName;

     public ElementNameEvaluator(String elementName) {
         this.elementName = elementName;
     }

     @Override
     public boolean matches(Element root, Element node) {
         return node.tagName().equals(elementName);
     }
 }

 private abstract static class AbstractElementEvaluator extends Evaluator {
 }
}