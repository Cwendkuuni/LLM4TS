package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.Constant;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testCoreOperationRelationalExpression() {
 CoreOperationRelationalExpression expression = createExpression(">");
 assert instanceOf(expression, CoreOperationRelationalExpression.class);
 assertFalse(expression.isSymmetric());
 assertFalse(expression.hasChildExpressions());
 assert expression.getPrecedence() == 3;
 }

 @Test
 public void testCoreOperationRelationalExpressionWithArgs() {
 CoreOperationRelationalExpression expression = createExpression(">", new Expression[] { new Constant(1), new Constant(2) });
 assert instanceOf(expression, CoreOperationRelationalExpression.class);
 assertTrue(expression.hasChildExpressions());
 assert expression.getPrecedence() == 3;
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCoreOperationRelationalExpressionWithInvalidArgs() {
 CoreOperationRelationalExpression expression = createExpression(">", new Expression[] { new CoreOperation(), new CoreOperation() });
 }

 @Test
 public void testCoreOperationRelationalExpressionWithSameExpression() {
 CoreOperationRelationalExpression expression = createExpression(">=", new Expression[] { new Constant(1), new Constant(1) });
 assertTrue(expression.evaluate(new Object[] { new Object[] { 1 } }, new Context())[0]);
 }

 @Test
 public void testCoreOperationRelationalExpressionWithInvalidValues() {
 CoreOperationRelationalExpression expression = createExpression(">", new Expression[] { new Constant("a"), new Constant(1) });
 assertFalse(expression.evaluate(new Object[] { new Object[] { "a" } }, new Context())[0]);
 }

 @Test
 public void testCoreOperationRelationalExpressionWithValidValues() {
 CoreOperationRelationalExpression expression = createExpression(">=", new Expression[] { new Constant(2), new Constant(3) });
 assertFalse(expression.evaluate(new Object[] { new Object[] { 2, 3 } }, new Context())[0]);
 expression = createExpression(">=", new Expression[] { new Constant(3), new Constant(3) });
 assertTrue(expression.evaluate(new Object[] { new Object[] { 3, 3 } }, new Context())[0]);
 expression = createExpression(">=", new Expression[] { new Constant(3), new Constant(2) });
 assertFalse(expression.evaluate(new Object[] { new Object[] { 3, 2 } }, new Context())[0]);
 }

 @Test
 public void testCoreOperationRelationalExpressionWithMixedTypes() {
 CoreOperationRelationalExpression expression = createExpression(">=", new Expression[] { new Constant("2"), new Constant(3) });
 assertFalse(expression.evaluate(new Object[] { new Object[] { "2", 3 } }, new Context())[0]);
 }

 private CoreOperationRelationalExpression createExpression(String op, Expression... args) {
 return new CoreOperationRelationalExpression(args) {
 @Override
 public String getOperator() {
 return op;
 }
 };
 }

}