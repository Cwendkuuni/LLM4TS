package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axis.InitialContext;
import org.apache.commons.jxpath.ri.axis.SelfContext;
import org.junit.Test;

/**
 * Test class for CoreOperationCompare
 */
public class CoreOperationCompareTest {

 /**
 * Test method for {@link CoreOperationCompare#CoreOperationCompare(Expression, Expression)} .
 */
 @Test
 public void testCoreOperationCompare() {
 CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
 new LiteralExpression(new Boolean(true)), new LiteralExpression(new Boolean(true)));
 assertTrue(coreOperationCompare.isSymmetric());
 assertEquals(2, coreOperationCompare.getPrecedence());
 }

 /**
 * Test method for {@link CoreOperationCompare#equal(EvalContext, Expression, Expression)} .
 */
 @Test
 public void testEqual() {
 EvalContext context = new EvalContext();
 CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
 new LiteralExpression(new Integer(1)), new LiteralExpression(new Integer(1)));
 assertTrue(coreOperationCompare.equal(context, new LiteralExpression(new Integer(1)), new LiteralExpression(new Integer(1))));

 CoreOperationCompare coreOperationCompare1 = new CoreOperationCompare(
 new LiteralExpression(new Integer(1)), new LiteralExpression(new Integer(2)));
 assertFalse(coreOperationCompare1.equal(context, new LiteralExpression(new Integer(1)), new LiteralExpression(new Integer(2))));
 }

 /**
 * Test method for {@link CoreOperationCompare#contains(Iterator, Object)} .
 */
 @Test
 public void testContains() {
 Iterator lit = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new Integer(1);
 }
 }.iterator();

 assertTrue(CoreOperationCompare.contains(lit, new Integer(1)));
 assertFalse(CoreOperationCompare.contains(lit, new Integer(2)));
 }

 /**
 * Test method for {@link CoreOperationCompare#findMatch(Iterator, Iterator)} .
 */
 @Test
 public void testFindMatch() {
 Iterator lit = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new Integer(1);
 }
 }.iterator();

 Iterator rit = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new Integer(1);
 }
 }.iterator();

 assertTrue(CoreOperationCompare.findMatch(lit, rit));

 Iterator rit1 = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new Integer(2);
 }
 }.iterator();

 assertFalse(CoreOperationCompare.findMatch(lit, rit1));
 }

 /**
 * Test method for {@link CoreOperationCompare#equal(Object, Object)} .
 */
 @Test
 public void testEqualObjectObject() {
 assertTrue(CoreOperationCompare.equal(new Boolean(true), new Boolean(true)));
 assertFalse(CoreOperationCompare.equal(new Boolean(true), new Boolean(false)));

 assertTrue(CoreOperationCompare.equal(new Integer(1), new Integer(1)));
 assertFalse(CoreOperationCompare.equal(new Integer(1), new Integer(2)));

 assertTrue(CoreOperationCompare.equal(InfoSetUtil.stringValue("test"), InfoSetUtil.stringValue("test")));
 assertFalse(CoreOperationCompare.equal(InfoSetUtil.stringValue("test"), InfoSetUtil.stringValue("test2")));

 assertTrue(CoreOperationCompare.equal(Pointer.NULL, Pointer.NULL));
 assertFalse(CoreOperationCompare.equal(Pointer.NULL, new Pointer(new Integer(1))));
 }

 /**
 * Test method for {@link CoreOperationCompare#equal(Pointer, Pointer)} .
 */
 @Test
 public void testEqualPointerPointer() {
 Pointer p1 = new Pointer(new Integer(1));
 Pointer p2 = new Pointer(new Integer(1));
 assertTrue(CoreOperationCompare.equal(p1, p2));

 Pointer p3 = new Pointer(new Integer(1));
 Pointer p4 = new Pointer(new Integer(2));
 assertFalse(CoreOperationCompare.equal(p3, p4));
 }

 /**
 * Test method for {@link CoreOperationCompare#getPrecedence()} .
 */
 @Test
 public void testGetPrecedence() {
 CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
 new LiteralExpression(new Integer(1)), new LiteralExpression(new Integer(1)));
 assertEquals(2, coreOperationCompare.getPrecedence());
 }

 /**
 * Test method for {@link CoreOperationCompare#isSymmetric()} .
 */
 @Test
 public void testIsSymmetric() {
 CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
 new LiteralExpression(new Integer(1)), new LiteralExpression(new Integer(1)));
 assertTrue(coreOperationCompare.isSymmetric());

 CoreOperationCompare coreOperationCompare1 = new CoreOperationCompare(
 new LiteralExpression(new Integer(1)), new LiteralExpression(new Integer(2)));
 assertFalse(coreOperationCompare1.isSymmetric());
 }

 /**
 * Test method for {@link CoreOperationCompare#compute(EvalContext)} .
 */
 @Test
 public void testCompute() {
 EvalContext context = new EvalContext();
 CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
 new LiteralExpression(new Integer(1)), new LiteralExpression(new Integer(1)));
 assertTrue(coreOperationCompare.compute(context) instanceof Boolean);
 assertTrue(((Boolean) coreOperationCompare.compute(context)).booleanValue());
 }

 /**
 * Test method for {@link CoreOperationCompare#compute(EvalContext)} with null context.
 */
 @Test(expected = NullPointerException.class)
 public void testComputeNullContext() {
 CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
 new LiteralExpression(new Integer(1)), new LiteralExpression(new Integer(1)));
 coreOperationCompare.compute(null);
 }

 /**
 * Test method for {@link CoreOperationCompare#compute(EvalContext)} with null left expression.
 */
 @Test(expected = NullPointerException.class)
 public void testComputeNullLeftExpression() {
 EvalContext context = new EvalContext();
 CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, new LiteralExpression(new Integer(1)));
 coreOperationCompare.compute(context);
 }

 /**
 * Test method for {@link CoreOperationCompare#compute(EvalContext)} with null right expression.
 */
 @Test(expected = NullPointerException.class)
 public void testComputeNullRightExpression() {
 EvalContext context = new EvalContext();
 CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
 new LiteralExpression(new Integer(1)), null);
 coreOperationCompare.compute(context);
 }

 /**
 * Test method for {@link CoreOperationCompare#compute(EvalContext)} with InitialContext
 */
 @Test
 public void testComputeInitialContext() {
 EvalContext context = new EvalContext();
 context.setInitialContext(new InitialContext());
 CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
 new LiteralExpression(new Integer(1)), new LiteralExpression(new Integer(1)));
 assertTrue(coreOperationCompare.compute(context) instanceof Boolean);
 assertTrue(((Boolean) coreOperationCompare.compute(context)).booleanValue());
 }

 /**
 * Test method for {@link CoreOperationCompare#compute(EvalContext)} with SelfContext
 */
 @Test
 public void testComputeSelfContext() {
 EvalContext context = new EvalContext();
 context.setSelfContext(new SelfContext());
 CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
 new LiteralExpression(new Integer(1)), new LiteralExpression(new Integer(1)));
 assertTrue(coreOperationCompare.compute(context) instanceof Boolean);
 assertTrue(((Boolean) coreOperationCompare.compute(context)).booleanValue());
 }
}