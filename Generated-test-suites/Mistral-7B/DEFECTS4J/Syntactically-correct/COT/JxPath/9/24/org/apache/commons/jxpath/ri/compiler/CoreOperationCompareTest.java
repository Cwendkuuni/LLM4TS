package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.junit.Test;

public class CoreOperationCompareTest {

 private CoreOperationCompare op;

 @Test
 public void testConstructor() {
 op = new CoreOperationCompare(new ConstantExpression(new String("A")), new ConstantExpression(new String("A")));
 assertNotNull(op);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorNullArg() {
 op = new CoreOperationCompare(null, new ConstantExpression(new String("A")));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorNullArg2() {
 op = new CoreOperationCompare(new ConstantExpression(new String("A")), null);
 }

 @Test
 public void testGetPrecedence() {
 op = new CoreOperationCompare(new ConstantExpression(new Integer(1)), new ConstantExpression(new Integer(2)));
 int precedence = op.getPrecedence();
 assertTrue(precedence == 2);
 }

 @Test
 public void testIsSymmetric() {
 op = new CoreOperationCompare(new ConstantExpression(new Integer(1)), new ConstantExpression(new Integer(1)));
 boolean symmetric = op.isSymmetric();
 assertTrue(symmetric);
 }

 @Test
 public void testIsNotSymmetric() {
 op = new CoreOperationCompare(new ConstantExpression(new Integer(1)), new ConstantExpression(new Integer(2)));
 boolean symmetric = op.isSymmetric();
 assertFalse(symmetric);
 }

 @Test
 public void testEqualNullContext() {
 op = new CoreOperationCompare(new ConstantExpression(new String("A")), new ConstantExpression(new String("A")));
 EvalContext context = new EvalContext();
 assertFalse(op.equal(context, null, null));
 }

 @Test
 public void testEqualContextWithSingleNodePointer() {
 JXPathContext jxpathContext = new JXPathContext();
 Pointer node = jxpathContext.getNodesByXPath("//test", new StringExpression("//test")).get(0);
 op = new CoreOperationCompare(new ConstantExpression(new String("A")), new ConstantExpression(new String("A")));
 EvalContext context = new EvalContext(jxpathContext);
 context.setCurrentNode(node);
 assertTrue(op.equal(context, null, null));
 }

 @Test
 public void testEqualContextWithNullLeft() {
 op = new CoreOperationCompare(new ConstantExpression(null), new ConstantExpression(new String("A")));
 EvalContext context = new EvalContext();
 assertFalse(op.equal(context, null, null));
 }

 @Test
 public void testEqualContextWithNullRight() {
 op = new CoreOperationCompare(new ConstantExpression(new String("A")), null);
 EvalContext context = new EvalContext();
 assertFalse(op.equal(context, null, null));
 }

 @Test
 public void testEqualContextWithSameValue() {
 op = new CoreOperationCompare(new ConstantExpression(new String("A")), new ConstantExpression(new String("A")));
 EvalContext context = new EvalContext();
 assertTrue(op.equal(context, null, null));
 }

 @Test
 public void testEqualContextWithDifferentValues() {
 op = new CoreOperationCompare(new ConstantExpression(new String("A")), new ConstantExpression(new String("B")));
 EvalContext context = new EvalContext();
 assertFalse(op.equal(context, null, null));
 }

 @Test
 public void testEqualContextWithIterators() {
 Collection<Integer> left = new ArrayList<>();
 left.add(1);
 left.add(2);
 Collection<Integer> right = new ArrayList<>();
 right.add(1);
 Collection<Integer> result = op.equal(new EvalContext(), new IteratorExpression(left.iterator()), new IteratorExpression(right.iterator()));
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testContainsNullContext() {
 op = new CoreOperationCompare(new ConstantExpression(new Integer(1)), new ConstantExpression(new Integer(2)));
 assertFalse(op.contains(null, null));
 }

 @Test
 public void testContainsContextWithEmptyIterators() {
 Collection<Integer> left = new ArrayList<>();
 Collection<Integer> right = new ArrayList<>();
 Collection<Boolean> result = op.contains(new EvalContext(), new IteratorExpression(left.iterator()), new IteratorExpression(right.iterator()));
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testContainsContextWithMatchingValues() {
 Collection<Integer> left = new ArrayList<>();
 left.add(1);
 left.add(2);
 Collection<Integer> right = new ArrayList<>();
 right.add(1);
 Collection<Boolean> result = op.contains(new EvalContext(), new IteratorExpression(left.iterator()), new IteratorExpression(right.iterator()));
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testContainsContextWithNonMatchingValues() {
 Collection<Integer> left = new ArrayList<>();
 left.add(1);
 left.add(2);
 Collection<Integer> right = new ArrayList<>();
 right.add(3);
 Collection<Boolean> result = op.contains(new EvalContext(), new IteratorExpression(left.iterator()), new IteratorExpression(right.iterator()));
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testFindMatchNullContext() {
 op = new CoreOperationCompare(new ConstantExpression(Arrays.asList(1, 2)), new ConstantExpression(Arrays.asList(3, 4)));
 EvalContext context = new EvalContext();
 assertFalse(op.findMatch(null, null));
 }

 @Test
 public void testFindMatchEmptyIterators() {
 Collection<Object> left = new ArrayList<>();
 Collection<Object> right = new ArrayList<>();
 HashSet<Boolean> result = (HashSet<Boolean>) op.findMatch(new EvalContext(), new IteratorExpression(left.iterator()), new IteratorExpression(right.iterator()));
 assertTrue(result.isEmpty());
 }

 @Test
 public void testFindMatchMatchingValues() {
 Collection<Object> left = new ArrayList<>();
 left.add(1);
 left.add(2);
 Collection<Object> right = new ArrayList<>();
 right.add(1);
 right.add(2);
 HashSet<Boolean> result = (HashSet<Boolean>) op.findMatch(new EvalContext(), new IteratorExpression(left.iterator()), new IteratorExpression(right.iterator()));
 assertTrue(result.size() > 0);
 }

 @Test
 public void testFindMatchNonMatchingValues() {
 Collection<Object> left = new ArrayList<>();
 left.add(1);
 left.add(2);
 Collection<Object> right = new ArrayList<>();
 right.add(3);
 right.add(4);
 HashSet<Boolean> result = (HashSet<Boolean>) op.findMatch(new EvalContext(), new IteratorExpression(left.iterator()), new IteratorExpression(right.iterator()));
 assertTrue(result.isEmpty());
 }

 @Test
 public void testEqualNullValues() {
 op = new CoreOperationCompare(new ConstantExpression(null), new ConstantExpression(null));
 boolean result = op.equal(null, null, null);
 assertTrue(result);
 }

 @Test
 public void testEqualBooleanValues() {
 op = new CoreOperationCompare(new ConstantExpression(true), new ConstantExpression(true));
 boolean result = op.equal(null, null, null);
 assertTrue(result);
 }

 @Test
 public void testEqualBooleanValuesDifferent() {
 op = new CoreOperationCompare(new ConstantExpression(true), new ConstantExpression(false));
 boolean result = op.equal(null, null, null);
 assertFalse(result);
 }

 @Test
 public void testEqualNumberValues() {
 op = new CoreOperationCompare(new ConstantExpression(new Double(1.0)), new ConstantExpression(new Double(1.0)));
 boolean result = op.equal(null, null, null);
 assertTrue(result);
 }

 @Test
 public void testEqualNumberValuesDifferent() {
 op = new CoreOperationCompare(new ConstantExpression(new Double(1.0)), new ConstantExpression(new Double(2.0)));
 boolean result = op.equal(null, null, null);
 assertFalse(result);
 }

 @Test
 public void testEqualStringValues() {
 op = new CoreOperationCompare(new ConstantExpression(new String("A")), new ConstantExpression(new String("A")));
 boolean result = op.equal(null, null, null);
 assertTrue(result);
 }

 @Test
 public void testEqualStringValuesDifferent() {
 op = new CoreOperationCompare(new ConstantExpression(new String("A")), new ConstantExpression(new String("B")));
 boolean result = op.equal(null, null, null);
 assertFalse(result);
 }

 @Test
 public void testEqualObjectValues() {
 op = new CoreOperationCompare(new ConstantExpression(new Integer(1)), new ConstantExpression(new Integer(1)));
 boolean result = op.equal(null, null, null);
 assertTrue(result);
 }

 @Test
 public void testEqualObjectValuesDifferentClass() {
 op = new CoreOperationCompare(new ConstantExpression(new Integer(1)), new ConstantExpression(new String("A")));
 boolean result = op.equal(null, null, null);
 assertFalse(result);
 }

 @Test
 public void testEqualObjectValuesDifferent() {
 op = new CoreOperationCompare(new ConstantExpression(new Integer(1)), new ConstantExpression(new Integer(2)));
 boolean result = op.equal(null, null, null);
 assertFalse(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqualObjectValuesNullLeft() {
 op = new CoreOperationCompare(null, new ConstantExpression(new Integer(1)));
 boolean result = op.equal(null, null, null);
 assertFalse(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqualObjectValuesNullRight() {
 op = new CoreOperationCompare(new ConstantExpression(new Integer(1)), null);
 boolean result = op.equal(null, null, null);
 assertFalse(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsObjectValuesNullIterator() {
 op = new CoreOperationCompare(new ConstantExpression(new Integer(1)), new ConstantExpression(new Integer(2)));
 boolean result = op.contains(null, null);
 assertFalse(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindMatchObjectValuesNullIterator1() {
 op = new CoreOperationCompare(new ConstantExpression(Arrays.asList(1, 2)), new ConstantExpression(Arrays.asList(3, 4)));
 HashSet<Boolean> result = (HashSet<Boolean>) op.findMatch(null, null, null);
 assertNull(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindMatchObjectValuesNullIterator2() {
 op = new CoreOperationCompare(new ConstantExpression(Arrays.asList(1, 2)), new ConstantExpression(Arrays.asList(3, 4)));
 HashSet<Boolean> result = (HashSet<Boolean>) op.findMatch(new EvalContext(), null, null);
 assertNull(result);
 }

}