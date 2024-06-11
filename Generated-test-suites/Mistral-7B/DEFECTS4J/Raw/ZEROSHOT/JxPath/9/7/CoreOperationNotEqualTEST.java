package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.expressions.Expression;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationNotEqualTest {

 private Expression arg1;
 private Expression arg2;
 private CoreOperationNotEqual coreOperationNotEqual;

 @Test
 public void testCoreOperationNotEqual() {
 arg1 = new LiteralExpression(1);
 arg2 = new LiteralExpression(2);
 coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
 assertNotNull(coreOperationNotEqual);
 }

 @Test
 public void testComputeValue() {
 EvalContext context = new EvalContext();
 assertFalse(coreOperationNotEqual.computeValue(context).equals(Boolean.TRUE));
 }

 @Test
 public void testGetSymbol() {
 assertTrue(coreOperationNotEqual.getSymbol().equals("!="));
 }

 @Test
 public void testComputeValueWithSameObjects() {
 arg1 = new LiteralExpression("Hello");
 arg2 = new LiteralExpression("Hello");
 coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
 assertFalse(coreOperationNotEqual.computeValue(new EvalContext()).equals(Boolean.TRUE));
 }

 @Test
 public void testComputeValueWithSameObjectsAndBooleanResult() {
 arg1 = new LiteralExpression(Boolean.TRUE);
 arg2 = new LiteralExpression(Boolean.TRUE);
 coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
 assertFalse(coreOperationNotEqual.computeValue(new EvalContext()).equals(Boolean.TRUE));
 }

 @Test
 public void testComputeValueWithNullAndNotNullObjects() {
 arg1 = new LiteralExpression(null);
 arg2 = new LiteralExpression("Hello");
 coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
 assertTrue(coreOperationNotEqual.computeValue(new EvalContext()).equals(Boolean.TRUE));

 arg2 = new LiteralExpression(null);
 coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
 assertTrue(coreOperationNotEqual.computeValue(new EvalContext()).equals(Boolean.TRUE));
 }

 @Test
 public void testComputeValueWithSameStrings() {
 arg1 = new LiteralExpression("Hello");
 arg2 = new LiteralExpression("Hello");
 coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
 assertFalse(coreOperationNotEqual.computeValue(new EvalContext()).equals(Boolean.TRUE));
 }

 @Test
 public void testComputeValueWithDifferentStrings() {
 arg1 = new LiteralExpression("Hello");
 arg2 = new LiteralExpression("World");
 coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
 assertTrue(coreOperationNotEqual.computeValue(new EvalContext()).equals(Boolean.TRUE));
 }

 @Test
 public void testComputeValueWithDifferentTypes() {
 arg1 = new LiteralExpression(1);
 arg2 = new LiteralExpression("Hello");
 coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
 assertTrue(coreOperationNotEqual.computeValue(new EvalContext()).equals(Boolean.TRUE));
 }

 @Test
 public void testComputeValueWithBooleanAndInteger() {
 arg1 = new LiteralExpression(Boolean.TRUE);
 arg2 = new LiteralExpression(1);
 coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
 assertTrue(coreOperationNotEqual.computeValue(new EvalContext()).equals(Boolean.TRUE));
 }

 @Test
 public void testComputeValueWithNullExpression() {
 coreOperationNotEqual = new CoreOperationNotEqual(null, arg2);
 assertNull(coreOperationNotEqual.computeValue(new EvalContext()));
 }

 @Test
 public void testComputeValueWithNullArg1() {
 arg1 = null;
 coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
 assertNull(coreOperationNotEqual.computeValue(new EvalContext()));
 }

 @Test
 public void testComputeValueWithNullArg2() {
 arg2 = null;
 coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
 assertNull(coreOperationNotEqual.computeValue(new EvalContext()));
 }
}