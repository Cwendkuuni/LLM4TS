package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.apache.commons.jxpath. ri.EvalContext;
import org.apache.commons.jxpath. ri.InfoSetUtil;

public class CoreOperationLessThanOrEqualTest {

 @Test
 public void testCoreOperationLessThanOrEqual() {
 CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new ConstantExpression(1.0), new ConstantExpression(2.0));
 EvalContext context = new EvalContext();
 Object result = coreOperationLessThanOrEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);

 coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new ConstantExpression(1.0), new ConstantExpression(1.0));
 result = coreOperationLessThanOrEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testCoreOperationLessThanOrEqualWithComplexExpressions() {
 Expression arg1 = new VariableExpression("x");
 Expression arg2 = new VariableExpression("y");
 CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 context.setVariable("x", new ConstantExpression(5.0));
 context.setVariable("y", new ConstantExpression(3.0));
 Object result = coreOperationLessThanOrEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);

 arg1 = new VariableExpression("x");
 arg2 = new VariableExpression("y");
 coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
 context.setVariable("x", new ConstantExpression(3.0));
 context.setVariable("y", new ConstantExpression(5.0));
 result = coreOperationLessThanOrEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }
}