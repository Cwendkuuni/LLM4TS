package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationRelationalExpressionFactory;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationRelationalExpressionType;
import org.apache.commons.jxpath.ri.compiler.TestUtils;
import org.apache.commons.jxpath.util.WrapperIterator;

public class CoreOperationRelationalExpressionTest {

 private CoreOperationRelationalExpressionFactory factory;

 @org.junit.Before
 public void setUp() {
 factory = new CoreOperationRelationalExpressionFactory();
 }

 /**
 * Test method for {@link CoreOperationRelationalExpression#CoreOperationRelationalExpression(Expression[])}
 */
 @org.junit.Test
 public void testCoreOperationRelationalExpression() {
 CoreOperationRelationalExpression expr = factory.createGreaterExpression(
 new CoreOperationIdentifier("a"),
 new CoreOperationIdentifier("b")
 );
 assertNotNull(expr);
 }

 /**
 * Test method for {@link CoreOperationRelationalExpression#computeValue(EvalContext)}
 */
 @org.junit.Test
 public void testComputeValue() {
 CoreOperationRelationalExpression expr = factory.createGreaterExpression(
 new CoreOperationIdentifier("a"),
 new CoreOperationIdentifier("b")
 );
 EvalContext context = TestUtils.createEvaluationContext();
 context.setNode(new Object());
 context.add("a", new Integer(1));
 context.add("b", new Integer(2));
 Object result = expr.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);

 context.clear();
 context.add("a", new Integer(2));
 context.add("b", new Integer(1));
 result = expr.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertNull(result);
 }

 /**
 * Test method for {@link CoreOperationRelationalExpression#getPrecedence()}
 */
 @org.junit.Test
 public void testGetPrecedence() {
 CoreOperationRelationalExpression expr = factory.createGreaterExpression(
 new CoreOperationIdentifier("a"),
 new CoreOperationIdentifier("b")
 );
 int precedence = expr.getPrecedence();
 assertEquals(CoreOperationRelationalExpression.RELATIONAL_EXPR_PRECEDENCE, precedence);
 }

 /**
 * Test method for {@link CoreOperationRelationalExpression#isSymmetric()}
 */
 @org.junit.Test
 public void testIsSymmetric() {
 CoreOperationRelationalExpression expr = factory.createGreaterExpression(
 new CoreOperationIdentifier("a"),
 new CoreOperationIdentifier("b")
 );
 assertFalse(expr.isSymmetric());
 }

 /**
 * Test method for {@link CoreOperationRelationalExpression#evaluateCompare(int)}
 */
 @org.junit.Test
 public void testEvaluateCompare() {
 CoreOperationRelationalExpression expr = factory.createGreaterExpression(
 new CoreOperationIdentifier("a"),
 new CoreOperationIdentifier("b")
 );
 assertTrue(expr.evaluateCompare(1) == true);
 assertTrue(expr.evaluateCompare(-1) == false);
 assertTrue(expr.evaluateCompare(0) == false);
 }

 /**
 * Test method for {@link CoreOperationRelationalExpression#compute(Object, Object)}
 */
 @org.junit.Test
 public void testCompute() {
 CoreOperationRelationalExpression expr = factory.createGreaterExpression(
 new CoreOperationIdentifier("a"),
 new CoreOperationIdentifier("b")
 );
 Object left = new Integer(2);
 Object right = new Integer(1);
 Object result = expr.compute(left, right);
 assertFalse(result instanceof Boolean);
 assertNull(result);

 left = new Integer(3);
 right = new Integer(2);
 result = expr.compute(left, right);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);

 left = new Integer(1);
 right = new Integer(2);
 result = expr.compute(left, right);
 assertFalse(result instanceof Boolean);
 assertNull(result);

 left = new Integer(Double.NaN);
 right = new Integer(1);
 result = expr.compute(left, right);
 assertFalse(result instanceof Boolean);
 assertNull(result);
 }

 /**
 * Test method for {@link CoreOperationRelationalExpression#reduce(Object)}
 */
 @org.junit.Test
 public void testReduce() {
 CoreOperationRelationalExpression expr = factory.createGreaterExpression(
 new CoreOperationIdentifier("a"),
 new CoreOperationIdentifier("b")
 );
 Object left = new Object();
 Object right = new Object();
 Object reducedLeft = expr.reduce(left);
 assertSame(left, reducedLeft);

 left = new Integer(1);
 reducedLeft = expr.reduce(left);
 assertNotNull(reducedLeft);
 assertTrue(reducedLeft instanceof Iterator);

 left = new ArrayList<>();
 left.add(new Integer(1));
 reducedLeft = expr.reduce(left);
 assertNotNull(reducedLeft);
 assertTrue(reducedLeft instanceof Iterator);

 left = new InitialContext(new Object());
 reducedLeft = expr.reduce(left);
 assertNotNull(reducedLeft);
 assertTrue(reducedLeft instanceof Iterator);

 left = new SelfContext();
 reducedLeft = expr.reduce(left);
 assertSame(left, reducedLeft);

 left = new Collection<>() {
 @Override
 public boolean add(Object o) {
 throw new UnsupportedOperationException();
 }

 @Override
 public boolean remove(Object o) {
 throw new UnsupportedOperationException();
 }

 @Override
 public boolean contains(Object o) {
 return false;
 }

 @Override
 public boolean isEmpty() {
 return true;
 }

 @Override
 public Iterator iterator() {
 return WrapperIterator.EMPTY_ITERATOR;
 }

 @Override
 public int size() {
 return 0;
 }
 };
 reducedLeft = expr.reduce(left);
 assertSame(left, reducedLeft);

 left = new WrapperIterator<Object>(new Object[0]) {
 @Override
 public boolean hasNext() {
 return false;
 }
 };
 reducedLeft = expr.reduce(left);
 assertSame(left, reducedLeft);
 }

 /**
 * Test method for {@link CoreOperationRelationalExpression#containsMatch(Iterator, Object)}
 */
 @org.junit.Test
 public void testContainsMatch() {
 CoreOperationRelationalExpression expr = factory.createGreaterExpression(
 new CoreOperationIdentifier("a"),
 new CoreOperationIdentifier("b")
 );
 Iterator it = Arrays.asList(1, 2, 3).iterator();
 Object value = new Integer(2);
 boolean result = expr.containsMatch(it, value);
 assertTrue(result);

 it = Arrays.asList(1, 3, 5).iterator();
 result = expr.containsMatch(it, value);
 assertFalse(result);
 }

 /**
 * Test method for {@link CoreOperationRelationalExpression#findMatch(Iterator, Iterator)}
 */
 @org.junit.Test
 public void testFindMatch() {
 CoreOperationRelationalExpression expr = factory.createGreaterExpression(
 new CoreOperationIdentifier("a"),
 new CoreOperationIdentifier("b")
 );
 Iterator lit = Arrays.asList(1, 2, 3).iterator();
 Iterator rit = Arrays.asList(2, 3, 4).iterator();
 boolean result = expr.findMatch(lit, rit);
 assertTrue(result);

 lit = Arrays.asList(1, 2, 4).iterator();
 result = expr.findMatch(lit, rit);
 assertFalse(result);
 }

 /**
 * Test method for {@link CoreOperationRelationalExpression#evaluateCompare(int)} with different types
 */
 @org.junit.Test
 public void testEvaluateCompareWithDifferentTypes() {
 CoreOperationRelationalExpression expr = factory.createGreaterExpression(
 new CoreOperationIdentifier("a"),
 new CoreOperationIdentifier("b")
 );
 int result = expr.evaluateCompare(Double.TYPE);
 assertEquals(CoreOperationRelationalExpression.RELATIONAL_EXPR_PRECEDENCE, result);

 result = expr.evaluateCompare(Integer.TYPE);
 assertEquals(CoreOperationRelationalExpression.RELATIONAL_EXPR_PRECEDENCE, result);
 }

 /**
 * Test method for {@link CoreOperationRelationalExpression#evaluateCompare(int)} with invalid type
 */
 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testEvaluateCompareWithInvalidType() {
 CoreOperationRelationalExpression expr = factory.createGreaterExpression(
 new CoreOperationIdentifier("a"),
 new CoreOperationIdentifier("b")
 );
 int result = expr.evaluateCompare('c');
 }

 /**
 * Test method for {@link CoreOperationRelationalExpression#evaluateCompare(int)} with null
 */
 @org.junit.Test(expected = NullPointerException.class)
 public void testEvaluateCompareWithNull() {
 CoreOperationRelationalExpression expr = factory.createGreaterExpression(
 new CoreOperationIdentifier("a"),
 new CoreOperationIdentifier("b")
 );
 int result = expr.evaluateCompare(null);
 }

 /**
 * Test method for {@link CoreOperationRelationalExpression#createGreaterExpression(Expression[], CoreOperationRelationalExpressionType)}
 */
 @org.junit.Test
 public void testCreateGreaterExpression() {
 CoreOperationRelationalExpression expr = factory.createGreaterExpression(
 new CoreOperationIdentifier[] { new CoreOperationIdentifier("a"), new CoreOperationIdentifier("b") },
 CoreOperationRelationalExpressionType.GREATER
 );
 assertNotNull(expr);
 }

 /**
 * Test method for {@link CoreOperationRelationalExpression#createGreaterOrEqualExpression(Expression[], CoreOperationRelationalExpressionType)}
 */
 @org.junit.Test
 public void testCreateGreaterOrEqualExpression() {
 CoreOperationRelationalExpression expr = factory.createGreaterOrEqualExpression(
 new CoreOperationIdentifier[] { new CoreOperationIdentifier("a"), new CoreOperationIdentifier("b") },
 CoreOperationRelationalExpressionType.GREATER_OR_EQUAL
 );
 assertNotNull(expr);
 }

 /**
 * Test method for {@link CoreOperationRelationalExpression#createLessExpression(Expression[], CoreOperationRelationalExpressionType)}
 */
 @org.junit.Test
 public void testCreateLessExpression() {
 CoreOperationRelationalExpression expr = factory.createLessExpression(
 new CoreOperationIdentifier[] { new CoreOperationIdentifier("a"), new CoreOperationIdentifier("b") },
 CoreOperationRelationalExpressionType.LESS
 );
 assertNotNull(expr);
 }

 /**
 * Test method for {@link CoreOperationRelationalExpression#createLessOrEqualExpression(Expression[], CoreOperationRelationalExpressionType)}
 */
 @org.junit.Test
 public void testCreateLessOrEqualExpression() {
 CoreOperationRelationalExpression expr = factory.createLessOrEqualExpression(
 new CoreOperationIdentifier[] { new CoreOperationIdentifier("a"), new CoreOperationIdentifier("b") },
 CoreOperationRelationalExpressionType.LESS_OR_EQUAL
 );
 assertNotNull(expr);
 }

}