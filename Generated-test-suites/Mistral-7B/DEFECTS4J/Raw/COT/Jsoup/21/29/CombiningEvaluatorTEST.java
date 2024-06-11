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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CombiningEvaluatorTest {

 private Element root = Mockito.mock(Element.class);
 private Element node = Mockito.mock(Element.class);

 @Test
 public void testAndConstructorWithCollection() {
 And andEvaluator = new And(Arrays.asList(Mockito.mock(Evaluator.class), Mockito.mock(Evaluator.class)));
 assertTrue(andEvaluator.matches(root, node));
 }

 @Test
 public void testAndConstructorWithEvaluators() {
 And andEvaluator = new And(Mockito.mock(Evaluator.class), Mockito.mock(Evaluator.class));
 assertTrue(andEvaluator.matches(root, node));
 }

 @Test
 public void testAndMatchesFalse() {
 And andEvaluator = new And(Arrays.asList(Mockito.mock(Evaluator.class), Mockito.mock(Evaluator.class)));
 Mockito.when(Mockito.any(Element.class).matches(Mockito.anyString())).thenReturn(false);
 assertFalse(andEvaluator.matches(root, node));
 }

 @Test
 public void testOrConstructorWithCollection() {
 Or orEvaluator = new Or(Arrays.asList(Mockito.mock(Evaluator.class), Mockito.mock(Evaluator.class)));
 assertTrue(orEvaluator.matches(root, node) || orEvaluator.matches(root, node));
 }

 @Test
 public void testOrConstructorWithSingleEvaluator() {
 Or orEvaluator = new Or(Mockito.mock(Evaluator.class));
 assertTrue(orEvaluator.matches(root, node));
 }

 @Test
 public void testOrMatchesFalse() {
 Or orEvaluator = new Or(Arrays.asList(Mockito.mock(Evaluator.class), Mockito.mock(Evaluator.class)));
 Mockito.when(Mockito.any(Element.class).matches(Mockito.anyString())).thenReturn(false);
 assertFalse(orEvaluator.matches(root, node));
 }

 @Test
 public void testOrAddEvaluator() {
 Or orEvaluator = new Or();
 orEvaluator.add(Mockito.mock(Evaluator.class));
 assertTrue(orEvaluator.matches(root, node) || orEvaluator.matches(root, node));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAndCannotAddEvaluator() {
 And andEvaluator = new And();
 andEvaluator.add(Mockito.mock(Evaluator.class));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOrAddNullEvaluator() {
 Or orEvaluator = new Or();
 orEvaluator.add(null);
 }
}