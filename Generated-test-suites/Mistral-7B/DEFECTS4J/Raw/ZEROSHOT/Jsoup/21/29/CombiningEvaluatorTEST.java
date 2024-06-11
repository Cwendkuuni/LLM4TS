package org.jsoup.select.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator;
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

 @Test
 public void testAndEvaluator() {
 Element root = Mockito.mock(Element.class);
 Element node = Mockito.mock(Element.class);

 Evaluator evaluator1 = Mockito.spy(new ElementSelector("tag1"));
 Evaluator evaluator2 = Mockito.spy(new ElementSelector("tag2"));
 And andEvaluator = new And(Arrays.asList(evaluator1, evaluator2));

 Mockito.when(evaluator1.matches(root, node)).thenReturn(true);
 Mockito.when(evaluator2.matches(root, node)).thenReturn(true);

 assertTrue(andEvaluator.matches(root, node));

 Mockito.verify(evaluator1).matches(root, node);
 Mockito.verify(evaluator2).matches(root, node);
 }

 @Test
 public void testAndEvaluator_false() {
 Element root = Mockito.mock(Element.class);
 Element node = Mockito.mock(Element.class);

 Evaluator evaluator1 = Mockito.spy(new ElementSelector("tag1"));
 Evaluator evaluator2 = Mockito.spy(new ElementSelector("tag2"));
 And andEvaluator = new And(Arrays.asList(evaluator1, evaluator2));

 Mockito.when(evaluator1.matches(root, node)).thenReturn(false);

 assertFalse(andEvaluator.matches(root, node));

 Mockito.verify(evaluator1, Mockito.times(1)).matches(root, node);
 Mockito.verify(evaluator2, Mockito.never()).matches(root, node);
 }

 @Test
 public void testOrEvaluator() {
 Element root = Mockito.mock(Element.class);
 Element node = Mockito.mock(Element.class);

 Evaluator evaluator1 = Mockito.spy(new ElementSelector("tag1"));
 Evaluator evaluator2 = Mockito.spy(new ElementSelector("tag2"));
 Or orEvaluator = new Or(Arrays.asList(evaluator1, evaluator2));

 Mockito.when(evaluator1.matches(root, node)).thenReturn(true);

 orEvaluator.add(evaluator2);

 assertTrue(orEvaluator.matches(root, node));

 Mockito.verify(evaluator1).matches(root, node);
 Mockito.verify(evaluator2, Mockito.times(1)).matches(root, node);
 }

 @Test
 public void testOrEvaluator_false_all() {
 Element root = Mockito.mock(Element.class);
 Element node = Mockito.mock(Element.class);

 Evaluator evaluator1 = Mockito.spy(new ElementSelector("tag1"));
 Evaluator evaluator2 = Mockito.spy(new ElementSelector("tag2"));
 Or orEvaluator = new Or(Arrays.asList(evaluator1, evaluator2));

 Mockito.when(evaluator1.matches(root, node)).thenReturn(false);
 Mockito.when(evaluator2.matches(root, node)).thenReturn(false);

 assertFalse(orEvaluator.matches(root, node));

 Mockito.verify(evaluator1, Mockito.times(1)).matches(root, node);
 Mockito.verify(evaluator2, Mockito.times(1)).matches(root, node);
 }

 @Test
 public void testOrEvaluator_true_one() {
 Element root = Mockito.mock(Element.class);
 Element node = Mockito.mock(Element.class);

 Evaluator evaluator1 = Mockito.spy(new ElementSelector("tag1"));
 Evaluator evaluator2 = Mockito.spy(new ElementSelector("tag2"));
 Or orEvaluator = new Or(Arrays.asList(evaluator1, evaluator2));

 Mockito.when(evaluator1.matches(root, node)).thenReturn(false);
 Mockito.when(evaluator2.matches(root, node)).thenReturn(true);

 assertTrue(orEvaluator.matches(root, node));

 Mockito.verify(evaluator1, Mockito.times(1)).matches(root, node);
 Mockito.verify(evaluator2, Mockito.times(1)).matches(root, node);
 }
}