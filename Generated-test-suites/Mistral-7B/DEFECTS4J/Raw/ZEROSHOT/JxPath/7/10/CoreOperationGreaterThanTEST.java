package org.apache.commons.jxpath.ri.compiler.test;

 import static org.junit.Assert.*;

 import org.apache.commons.jxpath.ri.EvalContext;
 import org.apache.commons.jxpath.ri.InfoSet;
 import org.apache.commons.jxpath.ri.Expression;
 import org.apache.commons.jxpath.ri.CoreOperationGreaterThan;
 import org.apache.commons.jxpath.ri.CoreOperationRelationalExpression;
 import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanTest;

 /**
  * Test class for CoreOperationGreaterThan.
  */
 public class CoreOperationGreaterThanTest {

 private static final double DELTA = 0.001;

 @Test
 public void testComputeValueGreater() {
 CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
 new CoreOperationNumber(5.0), new CoreOperationNumber(3.0));
 EvalContext context = new EvalContext(new InfoSet());
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testComputeValueEqual() {
 CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
 new CoreOperationNumber(5.0), new CoreOperationNumber(5.0));
 EvalContext context = new EvalContext(new InfoSet());
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testComputeValueLess() {
 CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
 new CoreOperationNumber(3.0), new CoreOperationNumber(5.0));
 EvalContext context = new EvalContext(new InfoSet());
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testComputeValueNullAndNumber() {
 CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
 new CoreOperationNull(), new CoreOperationNumber(5.0));
 EvalContext context = new EvalContext(new InfoSet());
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertNull(result);
 }

 @Test
 public void testComputeValueNumberAndNull() {
 CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
 new CoreOperationNumber(5.0), new CoreOperationNull());
 EvalContext context = new EvalContext(new InfoSet());
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse(result);
 }

 @Test
 public void testComputeValueBothNull() {
 CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
 new CoreOperationNull(), new CoreOperationNull());
 EvalContext context = new EvalContext(new InfoSet());
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertNull(result);
 }

 @Test
 public void testComputeValueBothSameNumber() {
 CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
 new CoreOperationNumber(5.0), new CoreOperationNumber(5.0));
 EvalContext context = new EvalContext(new InfoSet());
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorNullArgument() {
 new CoreOperationGreaterThan(null, new CoreOperationNumber(5.0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorNullArgument2() {
 new CoreOperationGreaterThan(new CoreOperationNumber(5.0), null);
 }

 @Test
 public void testSymbol() {
 CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
 new CoreOperationNumber(5.0), new CoreOperationNumber(3.0));
 String symbol = expression.getSymbol();
 assertEquals(">", symbol);
 }
 }