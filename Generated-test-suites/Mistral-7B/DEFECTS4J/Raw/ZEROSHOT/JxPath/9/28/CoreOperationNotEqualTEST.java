package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.test.TestUtil;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CoreOperationNotEqualTest {

 private Expression arg1;
 private Expression arg2;
 private CoreOperationNotEqual operation;

 @Test
 public void testCoreOperationNotEqualWithNullArg1() {
 arg1 = TestUtil.newNullExpression();
 arg2 = new ConstExpression(0);
 operation = new CoreOperationNotEqual(arg1, arg2);
 assertNull(operation.computeValue(new EvaluationContext()));
 }

 @Test
 public void testCoreOperationNotEqualWithNullArg2() {
 arg1 = new ConstExpression(1);
 arg2 = TestUtil.newNullExpression();
 operation = new CoreOperationNotEqual(arg1, arg2);
 assertTrue(operation.computeValue(new EvaluationContext()));
 }

 @Test
 public void testCoreOperationNotEqualWithSameValues() {
 arg1 = new ConstExpression(1);
 arg2 = new ConstExpression(1);
 operation = new CoreOperationNotEqual(arg1, arg2);
 assertFalse(operation.computeValue(new EvaluationContext()));
 }

 @Test
 public void testCoreOperationNotEqualWithDifferentValues() {
 arg1 = new ConstExpression(1);
 arg2 = new ConstExpression(2);
 operation = new CoreOperationNotEqual(arg1, arg2);
 assertTrue(operation.computeValue(new EvaluationContext()));
 }

 @Test
 public void testCoreOperationNotEqualWithSameExpressions() {
 arg1 = new ConstExpression(new ConstExpression(1));
 arg2 = new ConstExpression(new ConstExpression(1));
 operation = new CoreOperationNotEqual(arg1, arg2);
 assertFalse(operation.computeValue(new EvaluationContext()));
 }

 @Test
 public void testCoreOperationNotEqualWithDifferentExpressions() {
 arg1 = new ConstExpression(new ConstExpression(1));
 arg2 = new ConstExpression(new ConstExpression(2));
 operation = new CoreOperationNotEqual(arg1, arg2);
 assertTrue(operation.computeValue(new EvaluationContext()));
 }

 @Test
 public void testCoreOperationNotEqualWithComplexExpressions() {
 arg1 = new Add(TestUtil.newNumberExpression(1), TestUtil.newNumberExpression(2));
 arg2 = new Add(TestUtil.newNumberExpression(3), TestUtil.newNumberExpression(3));
 operation = new CoreOperationNotEqual(arg1, arg2);
 assertTrue(operation.computeValue(new EvaluationContext()));
 }

 @Test
 public void testCoreOperationNotEqualWithEqualityOperation() {
 arg1 = new CoreOperationEquals(new ConstExpression(1), new ConstExpression(1));
 arg2 = new CoreOperationEquals(new ConstExpression(1), new ConstExpression(2));
 operation = new CoreOperationNotEqual(arg1, arg2);
 assertFalse(operation.computeValue(new EvaluationContext()));
 }

 @Test
 public void testCoreOperationNotEqualWithGreaterThanOperation() {
 arg1 = new CoreOperationGreaterThan(new ConstExpression(1), new ConstExpression(1));
 arg2 = new ConstExpression(2);
 operation = new CoreOperationNotEqual(arg1, arg2);
 assertTrue(operation.computeValue(new EvaluationContext()));
 }

 @Test
 public void testCoreOperationNotEqualWithLessThanOperation() {
 arg1 = new ConstExpression(1);
 arg2 = new CoreOperationLessThan(new ConstExpression(1), new ConstExpression(1));
 operation = new CoreOperationNotEqual(arg1, arg2);
 assertFalse(operation.computeValue(new EvaluationContext()));
 }

 @Test
 public void testCoreOperationNotEqualWithInstanceofOperation() {
 arg1 = new ConstExpression("java.lang.String");
 arg2 = new CoreOperationInstanceof(new ConstExpression("java.lang.Number"));
 operation = new CoreOperationNotEqual(arg1, arg2);
 assertTrue(operation.computeValue(new EvaluationContext()));
 }

 @Test
 public void testCoreOperationNotEqualWithPropertyAccessOperation() {
 arg1 = new PropertyAccess(new ConstExpression("this"), "name");
 arg2 = new ConstExpression("John");
 operation = new CoreOperationNotEqual(arg1, arg2);
 assertTrue(operation.computeValue(new EvaluationContext()));
 }

 @Test
 public void testCoreOperationNotEqualWithMethodCallOperation() {
 Expression arg1 = new PropertyAccess(new ConstExpression("this"), "getAge");
 Expression arg2 = new MethodCall(arg1, "isEven");
 operation = new CoreOperationNotEqual(arg1, arg2);
 assertTrue(operation.computeValue(new EvaluationContext()));
 }

 @Test
 public void testCoreOperationNotEqualWithVariableAccessOperation() {
 Expression arg1 = new VariableAccess("x");
 arg2 = new ConstExpression("y");
 operation = new CoreOperationNotEqual(arg1, arg2);
 assertTrue(operation.computeValue(new EvaluationContext()));
 }

 @Test
 public void testCoreOperationNotEqualWithFunctionCallOperation() {
 Expression arg1 = new MethodCall(new ConstExpression("Math"), "abs");
 Expression arg2 = new ConstExpression(-1);
 operation = new CoreOperationNotEqual(arg1, arg2);
 assertTrue(operation.computeValue(new EvaluationContext()));
 }

 @Test
 public void testCoreOperationNotEqualWithOperationChain() {
 arg1 = new CoreOperationGreaterThan(new ConstExpression(1), new ConstExpression(1));
 arg2 = new CoreOperationNotEqual(arg1, new ConstExpression(1));
 operation = new CoreOperationNotEqual(arg1, arg2);
 assertFalse(operation.computeValue(new EvaluationContext()));
 }

 @Test
 public void testCoreOperationNotEqualWithSetOperation() {
 arg1 = new ConstExpression(1);
 arg2 = new Set(new ConstExpression("a"), arg1);
 operation = new CoreOperationNotEqual(arg1, arg2);
 assertFalse(operation.computeValue(new EvaluationContext()));
 }

 @Test
 public void testCoreOperationNotEqualWithMapIndexOperation() {
 Expression key = new ConstExpression("key");
 Expression index = new ConstExpression(1);
 arg1 = new MapIndex(new ConstExpression("map"), key);
 arg2 = new MapIndex(arg1, index);
 operation = new CoreOperationNotEqual(arg1, arg2);
 assertFalse(operation.computeValue(new EvaluationContext()));
 }

 @Test
 public void testCoreOperationNotEqualWithListIndexOperation() {
 Expression index = new ConstExpression(1);
 arg1 = new ListIndex(new ConstExpression("list"), index);
 arg2 = new ConstExpression(2);
 operation = new CoreOperationNotEqual(arg1, arg2);
 assertFalse(operation.computeValue(new EvaluationContext()));
 }

 @Test
 public void testCoreOperationNotEqualWithArrayIndexOperation() {
 arg1 = new ArrayIndex(new ConstExpression("array"), new ConstExpression(1));
 arg2 = new ConstExpression(2);
 operation = new CoreOperationNotEqual(arg1, arg2);
 assertFalse(operation.computeValue(new EvaluationContext()));
 }

 @Test
 public void testCoreOperationNotEqualWithRaiseOperation() {
 arg1 = new ConstExpression(1);
 arg2 = new CoreOperationRaise(arg1, new ConstExpression(2));
 operation = new CoreOperationNotEqual(arg1, arg2);
 assertFalse(operation.computeValue(new EvaluationContext()));
 }

 @Test
 public void testCoreOperationNotEqualWithCastOperation() {
 arg1 = new CoreOperationCast(new ConstExpression(1.0), String.class);
 arg2 = new ConstExpression("1");
 operation = new CoreOperationNotEqual(arg1, arg2);
 assertFalse(operation.computeValue(new EvaluationContext()));
 }

 @Test
 public void testCoreOperationNotEqualWithNegateOperation() {
 arg1 = new ConstExpression(1);
 arg2 = new CoreOperationNegate(arg1);
 operation = new CoreOperationNotEqual(arg1, arg2);
 assertFalse(operation.computeValue(new EvaluationContext()));
 }

 @Test
 public void testCoreOperationNotEqualWithFunctionCallWithArguments() {
 Expression arg1 = new PropertyAccess(new ConstExpression("this"), "getAge");
 Expression arg2 = new MethodCall(arg1, "isEven", new ConstExpression(2));
 operation = new CoreOperationNotEqual(arg1, arg2);
 assertTrue(operation.computeValue(new EvaluationContext()));
 }

 @Test
 public void testCoreOperationNotEqualWithConstructor() {
 CoreOperationCompare compare = new CoreOperationCompare(arg1 = new ConstExpression(1), arg2 = new ConstExpression(2));
 operation = new CoreOperationNotEqual(compare.args[0], compare.args[1]);
 assertTrue(operation.computeValue(new EvaluationContext()));
 }

 @Test
 public void testCoreOperationNotEqualWithToString() {
 assertEquals("!=" + System.lineSeparator() + "(", operation.toString());
 }

 @Test
 public void testCoreOperationNotEqualWithGetSymbol() {
 assertEquals("!=", operation.getSymbol());
 }
}