import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Iterate;
import org.apache.commons.jxpath.ri.Iterators;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.compilers.CoreOperationRelationalExpression;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testCoreOperationRelationalExpressionConstructors() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
 new CoreExpressionLiteral(new Integer(5)),
 new CoreExpressionLiteral(new Integer(3)));
 assertTrue(expression instanceof CoreOperationRelationalExpression);
 }

 @Test
 public void testComputeValue() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
 new CoreExpressionVariable("x"), new CoreExpressionLiteral(new Integer(5)));
 EvalContext context = new EvalContext(new InfoSet(), new InitialContext());
 context.setVariable("x", new Integer(6));
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testComputeValueWithIncompatibleTypes() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
 new CoreExpressionVariable("x"), new CoreExpressionLiteral(new String("5")));
 EvalContext context = new EvalContext(new InfoSet(), new InitialContext());
 context.setVariable("x", new Integer(6));
 Object result = expression.computeValue(context);
 assertNull(result);
 }

 @Test
 public void testComputeValueWithBothNull() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
 new CoreExpressionVariable("x"), new CoreExpressionVariable("y"));
 EvalContext context = new EvalContext(new InfoSet(), new InitialContext());
 Object result = expression.computeValue(context);
 assertNull(result);
 }

 @Test
 public void testComputeValueWithBothNotNullButBothFalse() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
 new CoreExpressionLiteral(new Integer(3)), new CoreExpressionLiteral(new Integer(3)));
 EvalContext context = new EvalContext(new InfoSet(), new InitialContext());
 Object result = expression.computeValue(context);
 assertNull(result);
 }

 @Test
 public void testComputeValueWithBoolFalseAndIntGreaterThanZero() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
 new CoreExpressionLiteral(Boolean.FALSE), new CoreExpressionLiteral(new Integer(1)));
 EvalContext context = new EvalContext(new InfoSet(), new InitialContext());
 Object result = expression.computeValue(context);
 assertFalse((Boolean) result);
 }

 @Test
 public void testComputeValueWithBoolTrueAndIntEqualZero() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
 new CoreExpressionLiteral(Boolean.TRUE), new CoreExpressionLiteral(new Integer(0)));
 EvalContext context = new EvalContext(new InfoSet(), new InitialContext());
 Object result = expression.computeValue(context);
 assertFalse((Boolean) result);
 }

 @Test
 public void testComputeValueWithBoolTrueAndIntGreaterThanZero() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
 new CoreExpressionLiteral(Boolean.TRUE), new CoreExpressionLiteral(new Integer(1)));
 EvalContext context = new EvalContext(new InfoSet(), new InitialContext());
 Object result = expression.computeValue(context);
 assertTrue((Boolean) result);
 }

 @Test
 public void testComputeValueWithBoolFalseAndIntLessThanZero() {
 CoreOperationRelationalExpression expression = new CoreOperationLessThan(
 new CoreExpressionLiteral(Boolean.FALSE), new CoreExpressionLiteral(new Integer(-1)));
 EvalContext context = new EvalContext(new InfoSet(), new InitialContext());
 Object result = expression.computeValue(context);
 assertFalse((Boolean) result);
 }

 @Test
 public void testComputeValueWithBoolTrueAndIntLessThanZero() {
 CoreOperationRelationalExpression expression = new CoreOperationLessThan(
 new CoreExpressionLiteral(Boolean.TRUE), new CoreExpressionLiteral(new Integer(-1)));
 EvalContext context = new EvalContext(new InfoSet(), new InitialContext());
 Object result = expression.computeValue(context);
 assertTrue((Boolean) result);
 }

 @Test
 public void testComputeValueWithBoolFalseAndIntEqualZero() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(
 new CoreExpressionLiteral(Boolean.FALSE), new CoreExpressionLiteral(new Integer(0)));
 EvalContext context = new EvalContext(new InfoSet(), new InitialContext());
 Object result = expression.computeValue(context);
 assertFalse((Boolean) result);
 }

 @Test
 public void testComputeValueWithBoolTrueAndIntEqualZero() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(
 new CoreExpressionLiteral(Boolean.TRUE), new CoreExpressionLiteral(new Integer(0)));
 EvalContext context = new EvalContext(new InfoSet(), new InitialContext());
 Object result = expression.computeValue(context);
 assertTrue((Boolean) result);
 }

 @Test
 public void testComputeValueWithBoolFalseAndIntGreaterThanZero() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(
 new CoreExpressionLiteral(Boolean.FALSE), new CoreExpressionLiteral(new Integer(1)));
 EvalContext context = new EvalContext(new InfoSet(), new InitialContext());
 Object result = expression.computeValue(context);
 assertFalse((Boolean) result);
 }

 @Test
 public void testComputeValueWithBoolTrueAndIntGreaterThanZero() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(
 new CoreExpressionLiteral(Boolean.TRUE), new CoreExpressionLiteral(new Integer(1)));
 EvalContext context = new EvalContext(new InfoSet(), new InitialContext());
 Object result = expression.computeValue(context);
 assertTrue((Boolean) result);
 }

 @Test
 public void testComputeValueWithBoolFalseAndIntLessThanZero() {
 CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(
 new CoreExpressionLiteral(Boolean.FALSE), new CoreExpressionLiteral(new Integer(-1)));
 EvalContext context = new EvalContext(new InfoSet(), new InitialContext());
 Object result = expression.computeValue(context);
 assertFalse((Boolean) result);
 }

 @Test
 public void testComputeValueWithBoolTrueAndIntLessThanZero() {
 CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(
 new CoreExpressionLiteral(Boolean.TRUE), new CoreExpressionLiteral(new Integer(-1)));
 EvalContext context = new EvalContext(new InfoSet(), new InitialContext());
 Object result = expression.computeValue(context);
 assertTrue((Boolean) result);
 }

 @Test
 public void testComputeValueWithBoolFalseAndIntEqualZero() {
 CoreOperationRelationalExpression expression = new CoreOperationNotEqual(
 new CoreExpressionLiteral(Boolean.FALSE), new CoreExpressionLiteral(new Integer(0)));
 EvalContext context = new EvalContext(new InfoSet(), new InitialContext());
 Object result = expression.computeValue(context);
 assertFalse((Boolean) result);
 }

 @Test
 public void testComputeValueWithBoolTrueAndIntEqualZero() {
 CoreOperationRelationalExpression expression = new CoreOperationNotEqual(
 new CoreExpressionLiteral(Boolean.TRUE), new CoreExpressionLiteral(new Integer(0)));
 EvalContext context = new EvalContext(new InfoSet(), new InitialContext());
 Object result = expression.computeValue(context);
 assertTrue((Boolean) result);
 }

 @Test
 public void testComputeValueWithBoolFalseAndIntGreaterThanZero() {
 CoreOperationRelationalExpression expression = new CoreOperationNotEqual(
 new CoreExpressionLiteral(Boolean.FALSE), new CoreExpressionLiteral(new Integer(1)));
 EvalContext context = new EvalContext(new InfoSet(), new InitialContext());
 Object result = expression.computeValue(context);
 assertTrue((Boolean) result);
 }

 @Test
 public void testComputeValueWithBoolTrueAndIntGreaterThanZero() {
 CoreOperationRelationalExpression expression = new CoreOperationNotEqual(
 new CoreExpressionLiteral(Boolean.TRUE), new CoreExpressionLiteral(new Integer(1)));
 EvalContext context = new EvalContext(new InfoSet(), new InitialContext());
 Object result = expression.computeValue(context);
 assertFalse((Boolean) result);
 }

 @Test
 public void testComputeValueWithTwoIterators() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
 new CoreExpressionVariable("x"), new CoreExpressionVariable("y"));
 EvalContext context = new EvalContext(new InfoSet(), new InitialContext());
 Iterator<Object> iteratorX = Iterators.iterate(new Integer(1), i -> i + 1);
 Iterator<Object> iteratorY = Iterators.iterate(new Integer(1), i -> i + 2);
 context.setVariables(new Object[]{"x", iteratorX, "y", iteratorY});
 Object result = expression.computeValue(context);
 assertFalse((Boolean) result);
 }

 @Test
 public void testComputeValueWithTwoIteratorsWhereFirstContainsSecond() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
 new CoreExpressionVariable("x"), new CoreExpressionVariable("y"));
 EvalContext context = new EvalContext(new InfoSet(), new InitialContext());
 Iterator<Object> iteratorX = Iterators.iterate(new Integer(1), i -> i + 1);
 Iterator<Object> iteratorY = Iterators.iterate(new Integer(1), i -> i + 1);
 context.setVariables(new Object[]{"x", iteratorX, "y", iteratorY});
 Object result = expression.computeValue(context);
 assertTrue((Boolean) result);
 }

 @Test
 public void testComputeValueWithTwoIteratorsWhereFirstDoesNotContainSecond() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
 new CoreExpressionVariable("x"), new CoreExpressionVariable("y"));
 EvalContext context = new EvalContext(new InfoSet(), new InitialContext());
 Iterator<Object> iteratorX = Iterators.iterate(new Integer(1), i -> i + 1);
 Iterator<Object> iteratorY = Iterators.iterate(new Integer(2), i -> i + 1);
 context.setVariables(new Object[]{"x", iteratorX, "y", iteratorY});
 Object result = expression.computeValue(context);
 assertFalse((Boolean) result);
 }

 @Test
 public void testComputeValueWithTwoIteratorsWhereFirstPartiallyContainsSecond() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
 new CoreExpressionVariable("x"), new CoreExpressionVariable("y"));
 EvalContext context = new EvalContext(new InfoSet(), new InitialContext());
 Iterator<Object> iteratorX = Iterators.iterate(new Integer(1), i -> i + 1);
 Iterator<Object> iteratorY = Iterators.iterate(new Integer(1), i -> i + 2);
 context.setVariables(new Object[]{"x", iteratorX, "y", iteratorY});
 Object result = expression.computeValue(context);
 assertFalse((Boolean) result);
 }

 @Test
 public void testGetPrecedence() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
 new CoreExpressionVariable("x"), new CoreExpressionVariable("y"));
 assertEquals(3, expression.getPrecedence());
 }

 @Test
 public void testIsSymmetric() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
 new CoreExpressionVariable("x"), new CoreExpressionVariable("y"));
 assertFalse(expression.isSymmetric());
 }

 @Test
 public void testEvaluateCompare() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThan();
 assertTrue(expression.evaluateCompare(1));
 assertFalse(expression.evaluateCompare(0));
 assertFalse(expression.evaluateCompare(-1));
 }

 @Test
 public void testEvaluateCompareForEqual() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual();
 assertTrue(expression.evaluateCompare(0));
 assertTrue(expression.evaluateCompare(1));
 assertFalse(expression.evaluateCompare(-1));
 }

 @Test
 public void testEvaluateCompareForNotEqual() {
 CoreOperationRelationalExpression expression = new CoreOperationNotEqual();
 assertFalse(expression.evaluateCompare(0));
 assertFalse(expression.evaluateCompare(1));
 assertTrue(expression.evaluateCompare(-1));
 }

 @Test
 public void testEvaluateCompareForLessThan() {
 CoreOperationRelationalExpression expression = new CoreOperationLessThan();
 assertFalse(expression.evaluateCompare(0));
 assertTrue(expression.evaluateCompare(-1));
 assertFalse(expression.evaluateCompare(1));
 }

 @Test
 public void testEvaluateCompareForLessThanOrEqual() {
 CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual();
 assertTrue(expression.evaluateCompare(0));
 assertTrue(expression.evaluateCompare(-1));
 assertTrue(expression.evaluateCompare(0));
 }

}