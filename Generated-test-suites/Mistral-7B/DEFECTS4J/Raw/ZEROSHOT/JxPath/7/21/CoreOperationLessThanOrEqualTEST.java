package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationLessThanOrEqualTest {

 @Test
 public void testLessThanOrEqual() {
 CoreExpression arg1 = new CoreNumber(1.5);
 CoreExpression arg2 = new CoreNumber(2.5);
 CoreExpression expression = new CoreOperationLessThanOrEqual(arg1, arg2);

 EvaluationContext context = new EvaluationContext(new InfoSet());
 Object result = expression.computeValue(context);

 assertEquals("Expected result to be true", Boolean.TRUE, result);
 }

 @Test
 public void testLessThan() {
 CoreExpression arg1 = new CoreNumber(1.5);
 CoreExpression arg2 = new CoreNumber(2.5);
 CoreExpression expression = new CoreOperationLessThanOrEqual(arg1, arg2);

 EvaluationContext context = new EvaluationContext(new InfoSet());
 Object result = expression.computeValue(context);

 assertEquals("Expected result to be false", Boolean.FALSE, result);
 }

 @Test
 public void testEqualValues() {
 CoreExpression arg1 = new CoreNumber(1.5);
 CoreExpression arg2 = new CoreNumber(1.5);
 CoreExpression expression = new CoreOperationLessThanOrEqual(arg1, arg2);

 EvaluationContext context = new EvaluationContext(new InfoSet());
 Object result = expression.computeValue(context);

 assertEquals("Expected result to be true", Boolean.TRUE, result);
 }

 @Test
 public void testDifferentTypes() {
 CoreExpression arg1 = new CoreNumber(1.5);
 CoreExpression arg2 = new CoreString("2");
 CoreExpression expression = new CoreOperationLessThanOrEqual(arg1, arg2);

 EvaluationContext context = new EvaluationContext(new InfoSet());
 Object result = expression.computeValue(context);

 assertEquals("Expected result to be false", Boolean.FALSE, result);
 }

 @Test
 public void testNullArg1() {
 CoreExpression arg1 = null;
 CoreExpression arg2 = new CoreNumber(2.5);
 CoreExpression expression = new CoreOperationLessThanOrEqual(arg1, arg2);

 EvaluationContext context = new EvaluationContext(new InfoSet());
 Object result = expression.computeValue(context);

 assertEquals("Expected result to be null", null, result);
 }

 @Test
 public void testNullArg2() {
 CoreExpression arg1 = new CoreNumber(1.5);
 CoreExpression arg2 = null;
 CoreExpression expression = new CoreOperationLessThanOrEqual(arg1, arg2);

 EvaluationContext context = new EvaluationContext(new InfoSet());
 Object result = expression.computeValue(context);

 assertEquals("Expected result to be null", null, result);
 }

 @Test(expected = RuntimeException.class)
 public void testInvalidExpression() {
 CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(null, new CoreNumber(2.5));
 }
}