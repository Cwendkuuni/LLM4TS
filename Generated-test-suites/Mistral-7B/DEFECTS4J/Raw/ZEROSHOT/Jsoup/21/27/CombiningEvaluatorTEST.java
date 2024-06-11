package org.jsoup.select.test;

import org.junit.Test;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Jsoup;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class CombiningEvaluatorTest {

 @Test
 public void testAndEvaluator() {
 // Given an And evaluator with two child evaluators
 List<Evaluator> childEvaluators1 = Arrays.asList(new ElementStartsWith("p"), new ElementHasClass("class1"));
 And andEvaluator1 = new And(childEvaluators1);

 // Given an Element that matches both child evaluators
 Document document1 = Jsoup.parse("<html><body><p class='class1'>Test</p></body></html>");
 Element element1 = document1.body().selectFirst("p.class1");

 // When testing the And evaluator with the Element
 assertTrue(andEvaluator1.matches(document1.rootElement(), element1));

 // Given an Element that does not match one of the child evaluators
 Document document2 = Jsoup.parse("<html><body><p class='class2'>Test</p></body></html>");
 Element element2 = document2.body().selectFirst("p.class1");

 // When testing the And evaluator with the Element
 assertFalse(andEvaluator1.matches(document2.rootElement(), element2));

 // Given an And evaluator with three child evaluators
 List<Evaluator> childEvaluators2 = Arrays.asList(new ElementStartsWith("p"), new ElementHasClass("class1"), new ElementHasClass("class2"));
 And andEvaluator2 = new And(childEvaluators2);

 // Given an Element that matches all three child evaluators
 Document document3 = Jsoup.parse("<html><body><p class='class1 class2'>Test</p></body></html>");
 Element element3 = document3.body().selectFirst("p.class1.class2");

 // When testing the And evaluator with the Element
 assertTrue(andEvaluator2.matches(document3.rootElement(), element3));

 // Given an Element that does not match one of the child evaluators
 Document document4 = Jsoup.parse("<html><body><p class='class1'>Test</p></body></html>");
 Element element4 = document4.body().selectFirst("p.class2");

 // When testing the And evaluator with the Element
 assertFalse(andEvaluator2.matches(document4.rootElement(), element4));
 }

 @Test
 public void testOrEvaluator() {
 // Given an Or evaluator with two child evaluators
 List<Evaluator> childEvaluatorsOr1 = Arrays.asList(new ElementStartsWith("p"), new ElementHasClass("class1"));
 And andEvaluatorOr1 = new And(childEvaluatorsOr1);
 Or orEvaluator1 = new Or(andEvaluatorOr1);

 // Given an Element that matches one of the child evaluators
 Document document1 = Jsoup.parse("<html><body><p class='class1'>Test</p></body></html>");
 Element element1 = document1.body().selectFirst("p.class1");

 // When testing the Or evaluator with the Element
 assertTrue(orEvaluator1.matches(document1.rootElement(), element1));

 // Given an Element that does not match any of the child evaluators
 Document document2 = Jsoup.parse("<html><body><p class='class2'>Test</p></body></html>");
 Element element2 = document2.body().selectFirst("p.class1");

 // When testing the Or evaluator with the Element
 assertFalse(orEvaluator1.matches(document2.rootElement(), element2));

 // Given an Or evaluator with three child evaluators
 List<Evaluator> childEvaluatorsOr2 = Arrays.asList(new ElementStartsWith("p"), new ElementHasClass("class1"), new ElementHasClass("class2"));
 And andEvaluatorOr2 = new And(childEvaluatorsOr2);
 Or orEvaluator2 = new Or();
 orEvaluator2.add(andEvaluatorOr2);

 // Given an Element that matches one of the child evaluators
 Document document3 = Jsoup.parse("<html><body><p class='class1'>Test</p></body></html>");
 Element element3 = document3.body().selectFirst("p.class1");

 // When testing the Or evaluator with the Element
 assertTrue(orEvaluator2.matches(document3.rootElement(), element3));

 // Given an Element that matches another child evaluator
 Document document4 = Jsoup.parse("<html><body><p class='class2'>Test</p></body></html>");
 Element element4 = document4.body().selectFirst("p.class2");

 // When testing the Or evaluator with the Element
 assertTrue(orEvaluator2.matches(document4.rootElement(), element4));

 // Given an Element that does not match any of the child evaluators
 Document document5 = Jsoup.parse("<html><body><p class='class3'>Test</p></body></html>");
 Element element5 = document5.body().selectFirst("p.class1");

 // When testing the Or evaluator with the Element
 assertFalse(orEvaluator2.matches(document5.rootElement(), element5));
 }

}