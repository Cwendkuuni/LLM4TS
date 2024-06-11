package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

 private CoreOperationRelationalExpression expression;
 private Expression arg1;
 private Expression arg2;

 @Test
 public void testCoreOperationRelationalExpressionConstruction() {
 expression = new CoreOperationRelationalExpression(new Expression[0]);
 assertNull(expression);

 arg1 = new LiteralExpression("5");
 arg2 = new LiteralExpression("6");
 expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2});
 assertEquals("relational expression with 2 args", expression.getClass(), CoreOperationRelationalExpression.class);
 }

 @Test
 public void testGetPrecedence() {
 expression = new CoreOperationRelationalExpression(new Expression[0]);
 assertNull(expression);

 arg1 = new LiteralExpression("5");
 arg2 = new LiteralExpression("6");
 expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2});
 assertEquals("precedence of relational expression", 3, expression.getPrecedence());
 }

 @Test
 public void testIsSymmetric() {
 expression = new CoreOperationRelationalExpression(new Expression[0]);
 assertNull(expression);

 arg1 = new LiteralExpression("5");
 arg2 = new LiteralExpression("6");
 expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2});
 assertFalse("relational expression is not symmetric", expression.isSymmetric());
 }

 @Test
 public void testCoreOperationRelationalExpressionGreaterThan() {
 expression = new CoreOperationGreaterThan(new Expression[0]);
 assertNull(expression);

 arg1 = new LiteralExpression("5");
 arg2 = new LiteralExpression("6");
 expression = new CoreOperationGreaterThan(new Expression[]{arg1, arg2});
 assertTrue("expression is greater than", expression.evaluate(new ContextItem(), new ContextItem[]{arg1, arg2}));
 assertFalse("expression is not greater than itself", expression.evaluate(new ContextItem(), new ContextItem[]{expression, expression}));

 arg1 = new LiteralExpression("5");
 arg2 = new LiteralExpression("5");
 assertFalse("expression is not greater than itself", expression.evaluate(new ContextItem(), new ContextItem[]{arg1, arg2}));
 }

 @Test
 public void testCoreOperationRelationalExpressionGreaterThanOrEqual() {
 expression = new CoreOperationGreaterThanOrEqual(new Expression[0]);
 assertNull(expression);

 arg1 = new LiteralExpression("5");
 arg2 = new LiteralExpression("6");
 expression = new CoreOperationGreaterThanOrEqual(new Expression[]{arg1, arg2});
 assertTrue("expression is greater than or equal", expression.evaluate(new ContextItem(), new ContextItem[]{arg1, arg2}));
 assertTrue("expression is greater than or equal to itself", expression.evaluate(new ContextItem(), new ContextItem[]{expression, expression}));

 arg1 = new LiteralExpression("5");
 arg2 = new LiteralExpression("5");
 assertTrue("expression is greater than or equal to itself", expression.evaluate(new ContextItem(), new ContextItem[]{arg1, arg2}));
 }

 @Test
 public void testCoreOperationRelationalExpressionLessThan() {
 expression = new CoreOperationLessThan(new Expression[0]);
 assertNull(expression);

 arg1 = new LiteralExpression("5");
 arg2 = new LiteralExpression("6");
 expression = new CoreOperationLessThan(new Expression[]{arg1, arg2});
 assertFalse("expression is not less than", expression.evaluate(new ContextItem(), new ContextItem[]{arg1, arg2}));
 assertFalse("expression is not less than itself", expression.evaluate(new ContextItem(), new ContextItem[]{expression, expression}));

 arg1 = new LiteralExpression("5");
 arg2 = new LiteralExpression("5");
 assertFalse("expression is not less than itself", expression.evaluate(new ContextItem(), new ContextItem[]{arg1, arg2}));
 }

 @Test
 public void testCoreOperationRelationalExpressionLessThanOrEqual() {
 expression = new CoreOperationLessThanOrEqual(new Expression[0]);
 assertNull(expression);

 arg1 = new LiteralExpression("5");
 arg2 = new LiteralExpression("6");
 expression = new CoreOperationLessThanOrEqual(new Expression[]{arg1, arg2});
 assertFalse("expression is not less than or equal", expression.evaluate(new ContextItem(), new ContextItem[]{arg1, arg2}));
 assertTrue("expression is less than or equal to itself", expression.evaluate(new ContextItem(), new ContextItem[]{expression, expression}));

 arg1 = new LiteralExpression("5");
 arg2 = new LiteralExpression("5");
 assertTrue("expression is less than or equal to itself", expression.evaluate(new ContextItem(), new ContextItem[]{arg1, arg2}));
 }
}