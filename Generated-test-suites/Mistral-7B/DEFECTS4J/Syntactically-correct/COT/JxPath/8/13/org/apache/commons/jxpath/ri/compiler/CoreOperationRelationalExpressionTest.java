import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Iterator;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Test;
import org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

 private CoreOperationRelationalExpression expression;

 @Test
 public void testComputeValue() {
 expression = new CoreOperationRelationalExpression(new CoreConstantExpression(5), new CoreConstantExpression(5));
 EvalContext context = new EvalContext();
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test(expected = AssertionError.class)
 public void testComputeValueDifferentTypes() {
 expression = new CoreOperationRelationalExpression(new CoreConstantExpression(5), new CoreConstantExpression("5"));
 EvalContext context = new EvalContext();
 expression.computeValue(context);
 }

 @Test
 public void testComputeValueFalse() {
 expression = new CoreOperationRelationalExpression(new CoreConstantExpression(5), new CoreConstantExpression(6));
 EvalContext context = new EvalContext();
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testGetPrecedence() {
 expression = new CoreOperationRelationalExpression(new CoreConstantExpression(5), new CoreConstantExpression(5));
 int precedence = expression.getPrecedence();
 assertEquals(3, precedence);
 }

 @Test
 public void testIsSymmetric() {
 expression = new CoreOperationRelationalExpression(new CoreConstantExpression(5), new CoreConstantExpression(5));
 boolean symmetric = expression.isSymmetric();
 assertFalse(symmetric);
 }

 @Test
 public void testEvaluateCompare() {
 expression = new CoreComparisonExpressionGreaterThan();
 boolean result = expression.evaluateCompare(0);
 assertFalse(result);
 result = expression.evaluateCompare(1);
 assertFalse(result);
 result = expression.evaluateCompare(-1);
 assertTrue(result);
 }

 @Test
 public void testReduceInitialContext() {
 expression = new CoreOperationRelationalExpression(new CoreConstantExpression(5), new InitialContext());
 Object result = expression.reduce(expression.args[1]);
 assertNotNull(result);
 assertTrue(result instanceof EvalContext);
 }

 @Test
 public void testReduceCollection() {
 expression = new CoreOperationRelationalExpression(new CoreConstantExpression(5), new CoreListExpression(new Object[] { 1, 2, 3 }));
 Object result = expression.reduce(expression.args[1]);
 assertNotNull(result);
 assertTrue(result instanceof Iterator);
 }

 @Test
 public void testContainsMatchIteratorAndObject() {
 expression = new CoreOperationRelationalExpression(new CoreListExpression(new Object[] { 1, 2, 3 }), new CoreConstantExpression(2));
 Iterator leftIterator = expression.args[0].computeValue(new EvalContext());
 Object right = expression.args[1].computeValue(new EvalContext());
 boolean result = expression.containsMatch(leftIterator, right);
 assertFalse(result);
 }

 @Test
 public void testContainsMatchIteratorAndIterator() {
 expression = new CoreOperationRelationalExpression(new CoreListExpression(new Object[] { 1, 2, 3 }), new CoreListExpression(new Object[] { 2, 3 }));
 Iterator leftIterator = expression.args[0].computeValue(new EvalContext());
 Iterator rightIterator = expression.args[1].computeValue(new EvalContext());
 boolean result = expression.containsMatch(leftIterator, rightIterator);
 assertFalse(result);
 }

 @Test
 public void testContainsMatchIteratorAndMatch() {
 expression = new CoreOperationRelationalExpression(new CoreListExpression(new Object[] { 1, 2, 3 }), new CoreConstantExpression(2));
 Iterator leftIterator = expression.args[0].computeValue(new EvalContext());
 Object right = expression.args[1].computeValue(new EvalContext());
 boolean result = expression.containsMatch(leftIterator, right);
 assertFalse(result);
 }

 @Test
 public void testContainsMatchMatchAndIterator() {
 expression = new CoreOperationRelationalExpression(new CoreConstantExpression(2), new CoreListExpression(new Object[] { 1, 2, 3 }));
 Iterator leftIterator = expression.args[0].computeValue(new EvalContext());
 Object right = expression.args[1].computeValue(new EvalContext());
 boolean result = expression.containsMatch(leftIterator, right);
 assertFalse(result);
 }

 @Test
 public void testContainsMatchMatchAndMatch() {
 expression = new CoreOperationRelationalExpression(new CoreConstantExpression(2), new CoreConstantExpression(2));
 Iterator leftIterator = expression.args[0].computeValue(new EvalContext());
 Object right = expression.args[1].computeValue(new EvalContext());
 boolean result = expression.containsMatch(leftIterator, right);
 assertTrue(result);
 }

 @Test
 public void testFindMatch() {
 expression = new CoreOperationRelationalExpression(new CoreListExpression(new Object[] { 1, 2, 3 }), new CoreListExpression(new Object[] { 1, 2, 3, 4 }));
 Iterator leftIterator = expression.args[0].computeValue(new EvalContext());
 Iterator rightIterator = expression.args[1].computeValue(new EvalContext());
 boolean result = expression.findMatch(leftIterator, rightIterator);
 assertFalse(result);
 }

 @Test
 public void testFindMatchMatchAndIterator() {
 expression = new CoreOperationRelationalExpression(new CoreListExpression(new Object[] { 1, 2, 3 }), new CoreConstantExpression(1));
 Iterator leftIterator = expression.args[0].computeValue(new EvalContext());
 Object right = expression.args[1].computeValue(new EvalContext());
 boolean result = expression.findMatch(leftIterator, right);
 assertFalse(result);
 }

 @Test
 public void testFindMatchIteratorAndMatch() {
 expression = new CoreOperationRelationalExpression(new CoreConstantExpression(1), new CoreListExpression(new Object[] { 1, 2, 3 }));
 Iterator leftIterator = expression.args[0].computeValue(new EvalContext());
 Object right = expression.args[1].computeValue(new EvalContext());
 boolean result = expression.findMatch(leftIterator, right);
 assertFalse(result);
 }

 @Test
 public void testFindMatchMatchAndMatch() {
 expression = new CoreOperationRelationalExpression(new CoreConstantExpression(1), new CoreConstantExpression(1));
 Iterator leftIterator = expression.args[0].computeValue(new EvalContext());
 Object right = expression.args[1].computeValue(new EvalContext());
 boolean result = expression.findMatch(leftIterator, right);
 assertTrue(result);
 }

}