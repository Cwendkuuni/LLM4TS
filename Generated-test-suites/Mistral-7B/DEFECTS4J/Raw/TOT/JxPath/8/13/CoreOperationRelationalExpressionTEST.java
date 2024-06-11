package org.apache.commons.jxpath.ri.compiler;

 import junit.framework.Assert;
 import org.apache.commons.jxpath.ri.EvalContext;
 import org.apache.commons.jxpath.ri.IterateIterable;
 import org.apache.commons.jxpath.ri.NodeIter;
 import org.apache.commons.jxpath.ri.NodeTest;
 import org.apache.commons.jxpath.ri.TestUtils;
 import org.junit.Test;

 /**
 * Test cases for the CoreOperationRelationalExpression class.
 */
 public class CoreOperationRelationalExpressionTest {

 /**
 * Test case for the '>' operation with integer values.
 */
 @Test
 public void testGreaterThan() {
 CoreOperationRelationalExpression expression = new GreaterThan(new Constant(5), new Constant(3));
 EvalContext context = TestUtils.createContext();
 Object result = expression.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);
 }

 /**
 * Test case for the '>' operation with floating point values.
 */
 @Test
 public void testGreaterThanFloat() {
 CoreOperationRelationalExpression expression = new GreaterThan(new Constant(5.5), new Constant(3.5));
 EvalContext context = TestUtils.createContext();
 Object result = expression.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);
 }

 /**
 * Test case for the '>' operation with null values.
 */
 @Test
 public void testGreaterThanNull() {
 CoreOperationRelationalExpression expression = new GreaterThan(new Constant(new Object()), new Constant(null));
 EvalContext context = TestUtils.createContext();
 Object result = expression.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);
 }

 /**
 * Test case for the '>' operation with invalid types.
 */
 @Test(expected = ClassCastException.class)
 public void testGreaterThanInvalidTypes() {
 CoreOperationRelationalExpression expression = new GreaterThan(new Constant("5"), new Constant(new Object()));
 EvalContext context = TestUtils.createContext();
 expression.computeValue(context);
 }

 /**
 * Test case for the '>'= operation with integer values.
 */
 @Test
 public void testGreaterThanOrEqual() {
 CoreOperationRelationalExpression expression = new GreaterThanOrEqual(new Constant(5), new Constant(3));
 EvalContext context = TestUtils.createContext();
 Object result = expression.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);
 }

 /**
 * Test case for the '>'= operation with floating point values.
 */
 @Test
 public void testGreaterThanOrEqualFloat() {
 CoreOperationRelationalExpression expression = new GreaterThanOrEqual(new Constant(5.5), new Constant(3.5));
 EvalContext context = TestUtils.createContext();
 Object result = expression.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);
 }

 /**
 * Test case for the '>'= operation with null values.
 */
 @Test
 public void testGreaterThanOrEqualNull() {
 CoreOperationRelationalExpression expression = new GreaterThanOrEqual(new Constant(new Object()), new Constant(null));
 EvalContext context = TestUtils.createContext();
 Object result = expression.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);
 }

 /**
 * Test case for the '<' operation with integer values.
 */
 @Test
 public void testLessThan() {
 CoreOperationRelationalExpression expression = new LessThan(new Constant(3), new Constant(5));
 EvalContext context = TestUtils.createContext();
 Object result = expression.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);
 }

 /**
 * Test case for the '<' operation with floating point values.
 */
 @Test
 public void testLessThanFloat() {
 CoreOperationRelationalExpression expression = new LessThan(new Constant(3.5), new Constant(5.5));
 EvalContext context = TestUtils.createContext();
 Object result = expression.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);
 }

 /**
 * Test case for the '<' operation with null values.
 */
 @Test
 public void testLessThanNull() {
 CoreOperationRelationalExpression expression = new LessThan(new Constant(null), new Constant(new Object()));
 EvalContext context = TestUtils.createContext();
 Object result = expression.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);
 }

 /**
 * Test case for the '<'= operation with integer values.
 */
 @Test
 public void testLessThanOrEqual() {
 CoreOperationRelationalExpression expression = new LessThanOrEqual(new Constant(3), new Constant(5));
 EvalContext context = TestUtils.createContext();
 Object result = expression.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);
 }

 /**
 * Test case for the '<'= operation with floating point values.
 */
 @Test
 public void testLessThanOrEqualFloat() {
 CoreOperationRelationalExpression expression = new LessThanOrEqual(new Constant(3.5), new Constant(5.5));
 EvalContext context = TestUtils.createContext();
 Object result = expression.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);
 }

 /**
 * Test case for the '<'= operation with null values.
 */
 @Test
 public void testLessThanOrEqualNull() {
 CoreOperationRelationalExpression expression = new LessThanOrEqual(new Constant(null), new Constant(null));
 EvalContext context = TestUtils.createContext();
 Object result = expression.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);
 }

 /**
 * Test case for the comparison of two different iterators.
 */
 @Test
 public void testCompareIterators() {
 CoreOperationRelationalExpression expression = new GreaterThan(new IteratorTest(), new IteratorTest2());
 EvalContext context = TestUtils.createContext();
 Object result = expression.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);
 }

 /**
 * Test case for the comparison of an iterator and an object.
 */
 @Test
 public void testCompareIteratorAndObject() {
 CoreOperationRelationalExpression expression = new GreaterThan(new IteratorTest(), new Constant(10));
 EvalContext context = TestUtils.createContext();
 Object result = expression.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);
 }

 /**
 * Test case for the comparison of an object and an iterator.
 */
 @Test
 public void testCompareObjectAndIterator() {
 CoreOperationRelationalExpression expression = new GreaterThan(new Constant(10), new IteratorTest());
 EvalContext context = TestUtils.createContext();
 Object result = expression.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);
 }

 /**
 * Test case for the comparison of two identical iterators.
 */
 @Test
 public void testCompareIdenticalIterators() {
 CoreOperationRelationalExpression expression = new GreaterThan(new IteratorTest(), new IteratorTest());
 EvalContext context = TestUtils.createContext();
 Object result = expression.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);
 }

 /**
 * Test case for the comparison of a self context and a value.
 */
 @Test
 public void testCompareSelfContextAndValue() {
 CoreOperationRelationalExpression expression = new GreaterThan(new SelfContext(), new Constant(5));
 EvalContext context = TestUtils.createContext();
 Object result = expression.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);
 }

 /**
 * Test case for the comparison of a value and a self context.
 */
 @Test
 public void testCompareValueAndSelfContext() {
 CoreOperationRelationalExpression expression = new GreaterThan(new Constant(5), new SelfContext());
 EvalContext context = TestUtils.createContext();
 Object result = expression.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);
 }
 }

 /**
 * Test iterable that always returns the same value.
 */
 class IteratorTest implements IterateIterable {
 @Override
 public NodeIter iterator() {
 return new NodeIter() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public NodeTest next() {
 return new NodeTest() {};
 }
 };
 }
 }

 /**
 * Test iterable that always returns a different value.
 */
 class IteratorTest2 implements IterateIterable {
 @Override
 public NodeIter iterator() {
 return new NodeIter() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public NodeTest next() {
 return new NodeTest() {
 @Override
 public int compareTo(NodeTest o) {
 return 1;
 }
 };
 }
 };
 }
 }