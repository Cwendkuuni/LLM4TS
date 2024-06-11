package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.axes.Iterators;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationRelationalExpressionFactory;
import org.apache.commons.jxpath.ri.compiler.CoreOperationValue;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

 private EvalContext context;
 private CoreOperationRelationalExpressionFactory factory;

 @Before
 public void setUp() {
 context = new EvalContext();
 factory = new CoreOperationRelationalExpressionFactory();
 }

 @Test
 public void testCreateCoreOperationRelationalExpression() {
 CoreOperationRelationalExpression expression = factory.createGreaterThanExpression(new CoreOperationValue[] {
 new CoreOperationValue(new InfoSet()), new CoreOperationValue(new InfoSet()) });
 assertTrue(expression instanceof CoreOperationRelationalExpression);
 }

 @Test
 public void testComputeValueEquals() {
 CoreOperationRelationalExpression expression = factory.createEqualsExpression(new CoreOperationValue[] {
 new CoreOperationValue(new InfoSet(1)), new CoreOperationValue(new InfoSet(1)) });
 assertTrue(expression.computeValue(context));
}

 @Test
 public void testComputeValueNotEquals() {
 CoreOperationRelationalExpression expression = factory.createNotEqualsExpression(new CoreOperationValue[] {
 new CoreOperationValue(new InfoSet(1)), new CoreOperationValue(new InfoSet(2)) });
 assertFalse(expression.computeValue(context));
 }

 @Test
 public void testComputeValueGreaterThan() {
 CoreOperationRelationalExpression expression = factory.createGreaterThanExpression(new CoreOperationValue[] {
 new CoreOperationValue(new InfoSet(2)), new CoreOperationValue(new InfoSet(1)) });
 assertTrue(expression.computeValue(context));
 }

 @Test
 public void testComputeValueGreaterThanOrEquals() {
 CoreOperationRelationalExpression expression = factory.createGreaterThanOrEqualsExpression(new CoreOperationValue[] {
 new CoreOperationValue(new InfoSet(2)), new CoreOperationValue(new InfoSet(1)) });
 assertTrue(expression.computeValue(context));
 }

 @Test
 public void testComputeValueLessThan() {
 CoreOperationRelationalExpression expression = factory.createLessThanExpression(new CoreOperationValue[] {
 new CoreOperationValue(new InfoSet(1)), new CoreOperationValue(new InfoSet(2)) });
 assertFalse(expression.computeValue(context));
 }

 @Test
 public void testComputeValueLessThanOrEquals() {
 CoreOperationRelationalExpression expression = factory.createLessThanOrEqualsExpression(new CoreOperationValue[] {
 new CoreOperationValue(new InfoSet(1)), new CoreOperationValue(new InfoSet(1)) });
 assertTrue(expression.computeValue(context));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidNumberOfArguments() {
 factory.createGreaterThanExpression(new CoreOperationValue[] { new CoreOperationValue(new InfoSet()) });
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArgumentType() {
 CoreOperationRelationalExpression expression = factory.createGreaterThanExpression(new CoreOperationValue[] {
 new CoreOperationValue("string"), new CoreOperationValue(new InfoSet()) });
 }

 @Test
 public void testReduceNull() {
 Object left = null;
 Object right = new InfoSet();
 Object reducedLeft = factory.reduce(left);
 assertEquals(left, reducedLeft);
 }

 @Test
 public void testReduceSelfContext() {
 Object left = new SelfContext();
 Object right = new InfoSet();
 Object reducedLeft = factory.reduce(left);
 assertNull(reducedLeft);
 }

 @Test
 public void testReduceCollection() {
 Collection<Object> collection = new ArrayList<>();
 collection.add(new InfoSet());
 Object reducedCollection = factory.reduce(collection.iterator());
 assertNull(reducedCollection);
 }

 @Test
 public void testReduceIteratorsMatching() {
 Iterator<Object> it1 = Iterators.emptyIterator();
 Iterator<Object> it2 = Iterators.emptyIterator();
 it1.add(new InfoSet());
 it2.add(new InfoSet());
 Object reducedIt1 = factory.reduce(it1);
 Object reducedIt2 = factory.reduce(it2);
 Iterator<Object> mergedIt = Iterators.mergeIterators(it1, it2);
 Object reducedMergedIt = factory.reduce(mergedIt);
 assertEquals(reducedIt1, reducedMergedIt);
 }

 @Test
 public void testReduceIteratorsNoMatch() {
 Iterator<Object> it1 = Iterators.emptyIterator();
 Iterator<Object> it2 = Iterators.emptyIterator();
 it1.add(new InfoSet(1));
 it2.add(new InfoSet(2));
 Object reducedIt1 = factory.reduce(it1);
 Object reducedIt2 = factory.reduce(it2);
 Object reducedMergedIt = factory.reduce(Iterators.mergeIterators(it1, it2));
 assertEquals(reducedIt1, reducedMergedIt);
 }

 @Test
 public void testReduceDoubleNaN() {
 Object left = Double.NaN;
 Object right = Double.NaN;
 Object reducedLeft = factory.reduce(left);
 assertEquals(left, reducedLeft);
 }

 @Test
 public void testReduceDoubleLessThan() {
 Object left = 2.0;
 Object right = 1.0;
 Object reducedLeft = factory.reduce(left);
 assertEquals(left, reducedLeft);
 }

 @Test
 public void testReduceDoubleGreaterThan() {
 Object left = 1.0;
 Object right = 2.0;
 Object reducedRight = factory.reduce(right);
 assertEquals(right, reducedRight);
 }

 @Test
 public void testEvaluateCompareEquals() {
 int compare = 0;
 boolean result = factory.evaluateCompare(compare);
 assertTrue(result);
 }

 @Test
 public void testEvaluateCompareLessThan() {
 int compare = -1;
 boolean result = factory.evaluateCompare(compare);
 assertTrue(result);
 }

 @Test
 public void testEvaluateCompareGreaterThan() {
 int compare = 1;
 boolean result = factory.evaluateCompare(compare);
 assertTrue(result);
 }

 @Test
 public void testEvaluateCompareNotEquals() {
 int compare = 0;
 boolean result = factory.evaluateCompare(compare);
 assertFalse(result);
 }

 @Test
 public void testContainsMatch() {
 Iterator<Object> it = Arrays.asList(1, 2, 3).iterator();
 Object value = 2;
 boolean result = factory.containsMatch(it, value);
 assertTrue(result);
 }

 @Test
 public void testContainsMatchEmptyIterators() {
 Iterator<Object> it1 = Iterators.emptyIterator();
 Iterator<Object> it2 = Iterators.emptyIterator();
 Object value = new InfoSet(1);
 boolean result = factory.containsMatch(it1, value);
 assertFalse(result);
 }

 @Test
 public void testContainsMatchValueNotInIterators() {
 Iterator<Object> it = Arrays.asList(1, 2, 3).iterator();
 Object value = 4;
 boolean result = factory.containsMatch(it, value);
 assertFalse(result);
 }

 @Test
 public void testFindMatch() {
 List<Object> list1 = Arrays.asList(new InfoSet(1), new InfoSet(2), new InfoSet(3));
 List<Object> list2 = Arrays.asList(new InfoSet(1), new InfoSet(2));
 boolean result = factory.findMatch(list1.iterator(), list2.iterator());
 assertTrue(result);
 }

 @Test
 public void testFindMatchEmptyIterators() {
 List<Object> list1 = Collections.emptyList();
 List<Object> list2 = Arrays.asList(new InfoSet(1), new InfoSet(2));
 boolean result = factory.findMatch(list1.iterator(), list2.iterator());
 assertFalse(result);
 }

 @Test
 public void testFindMatchNoMatch() {
 List<Object> list1 = Arrays.asList(new InfoSet(1), new InfoSet(2), new InfoSet(3));
 List<Object> list2 = Arrays.asList(new InfoSet(4), new InfoSet(5));
 boolean result = factory.findMatch(list1.iterator(), list2.iterator());
 assertFalse(result);
 }
}