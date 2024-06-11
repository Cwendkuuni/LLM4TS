package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationEqualTest {

 @Test
 public void testEqual() {
 Expression arg1 = new Const(5);
 Expression arg2 = new Const(5);
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
 EqualEvalContext context = new EqualEvalContext();
 Object result = coreOperationEqual.computeValue(context);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testNotEqual() {
 Expression arg1 = new Const(5);
 Expression arg2 = new Const(6);
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
 EqualEvalContext context = new EqualEvalContext();
 Object result = coreOperationEqual.computeValue(context);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testNullLeftSide() {
 Expression arg1 = new Const(null);
 Expression arg2 = new Const(5);
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
 EqualEvalContext context = new EqualEvalContext();
 Object result = coreOperationEqual.computeValue(context);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testNullRightSide() {
 Expression arg1 = new Const(5);
 Expression arg2 = new Const(null);
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
 EqualEvalContext context = new EqualEvalContext();
 Object result = coreOperationEqual.computeValue(context);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testDifferentTypeLeftSide() {
 Expression arg1 = new Const(5);
 Expression arg2 = new NumberOperatorAdd(new Const(2), new Const(3));
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
 EqualEvalContext context = new EqualEvalContext();
 Object result = coreOperationEqual.computeValue(context);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testDifferentTypeRightSide() {
 Expression arg1 = new Const(5);
 Expression arg2 = new StringOperatorConcat(new Const(""), new Const("abc"));
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
 EqualEvalContext context = new EqualEvalContext();
 Object result = coreOperationEqual.computeValue(context);
 assertEquals(Boolean.FALSE, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArgType() {
 Expression arg1 = new Operator("+"); // invalid type
 Expression arg2 = new Const(5);
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
 EqualEvalContext context = new EqualEvalContext();
 coreOperationEqual.computeValue(context);
 }
}