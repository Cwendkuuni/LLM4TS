package org.jsoup.select.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.jsoup.helper.ElementFinder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.And;
import org.jsoup.select.Or;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CombiningEvaluatorTest {

 private Document document;
 private ElementFinder elementFinder;

 @InjectMocks
 private CombiningEvaluator combiningEvaluator;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 document = Mockito.mock(Document.class);
 elementFinder = Mockito.spy(new ElementFinder(document));
 }

 // Testing the constructor
 @Test
 public void testCombiningEvaluatorConstructor() {
 CombiningEvaluator ce = new CombiningEvaluator();
 assertTrue(ce instanceof CombiningEvaluator);
 }

 // Testing the constructor with Collection
 @Test
 public void testCombiningEvaluatorWithCollectionConstructor() {
 List<Evaluator> evaluators = new ArrayList<>();
 Evaluator eval1 = Mockito.mock(Evaluator.class);
 Evaluator eval2 = Mockito.mock(Evaluator.class);
 evaluators.add(eval1);
 evaluators.add(eval2);
 CombiningEvaluator ce = new CombiningEvaluator(evaluators);
 assertTrue(ce instanceof CombiningEvaluator);
 }

 // Testing the static inner class And
 @Test
 public void testAndMethodMatches() {
 List<Evaluator> evaluators = new ArrayList<>();
 Evaluator eval1 = Mockito.mock(Evaluator.class);
 Evaluator eval2 = Mockito.mock(Evaluator.class);
 evaluators.add(eval1);
 evaluators.add(eval2);
 And and = new And(evaluators);
 Element element = Mockito.mock(Element.class);
 assertFalse(and.matches(document, element)); // initial state should be false
 Mockito.when(eval1.matches(document, element)).thenReturn(true);
 Mockito.when(eval2.matches(document, element)).thenReturn(true);
 assertTrue(and.matches(document, element)); // when both evaluators match, the final result is true
 }

 @Test
 public void testAndMethodToString() {
 List<Evaluator> evaluators = Arrays.asList(Mockito.mock(Evaluator.class), Mockito.mock(Evaluator.class));
 And and = new And(evaluators);
 String expected = StringUtil.join(evaluators, " ");
 assertTrue(expected.equals(and.toString()));
 }

 // Testing the static inner class Or
 @Test
 public void testOrMethodConstructorWithCollection() {
 List<Evaluator> evaluators = new ArrayList<>();
 Evaluator eval1 = Mockito.mock(Evaluator.class);
 Evaluator eval2 = Mockito.mock(Evaluator.class);
 Or or = new Or(evaluators);
 assertTrue(or instanceof Or);
 }

 @Test
 public void testOrMethodConstructorWithSingleEvaluator() {
 Evaluator eval = Mockito.mock(Evaluator.class);
 Or or = new Or(eval);
 assertTrue(or instanceof Or);
 }

 @Test
 public void testOrMethodAddEvaluator() {
 Or or = new Or();
 Evaluator eval = Mockito.mock(Evaluator.class);
 or.add(eval);
 assertTrue(or.evaluators.contains(eval));
 }

 @Test
 public void testOrMethodMatches() {
 List<Evaluator> evaluators = new ArrayList<>();
 Evaluator eval1 = Mockito.mock(Evaluator.class);
 Evaluator eval2 = Mockito.mock(Evaluator.class);
 evaluators.add(eval1);
 evaluators.add(eval2);
 Or or = new Or(evaluators);
 Element element = Mockito.mock(Element.class);
 assertFalse(or.matches(document, element)); // initial state should be false
 Mockito.when(eval1.matches(document, element)).thenReturn(true);
 assertTrue(or.matches(document, element)); // when one evaluator matches, the final result is true
 }

 @Test
 public void testOrMethodToString() {
 List<Evaluator> evaluators = Arrays.asList(Mockito.mock(Evaluator.class), Mockito.mock(Evaluator.class));
 Or or = new Or(evaluators);
 String expected = String.format(":or%s", evaluators);
 assertTrue(expected.equals(or.toString()));
 }

}