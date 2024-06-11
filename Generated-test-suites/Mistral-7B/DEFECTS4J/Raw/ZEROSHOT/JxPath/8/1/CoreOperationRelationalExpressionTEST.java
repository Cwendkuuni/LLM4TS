import org.apache.commons.jxpath.ri.CompilerTest;
import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.iter.ArrayIter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest extends CompilerTest {

 private CoreOperationRelationalExpression expression;
 private Object leftValue;
 private Object rightValue;
 private EvaluationContext context;

 @Before
 public void setUp() {
 leftValue = createNode("5");
 rightValue = createNode("6");
 expression = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(leftValue), new ConstantExpression(rightValue)});
 context = new EvaluationContext(new InitialContext(new ArrayIter(new Object[]{leftValue, rightValue})));
 }

 @Test
 public void testComputeValueGreaterThan() {
 expression = new CoreOperationGreaterThan();
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);

 leftValue = createNode("6");
 rightValue = createNode("5");
 expression = new CoreOperationGreaterThan();
 result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testComputeValueGreaterThanOrEqual() {
 expression = new CoreOperationGreaterThanOrEqual();
 Object result = expression.computeValue(context);
 assertFalse(result instanceof Boolean);

 leftValue = createNode("6");
 rightValue = createNode("5");
 expression = new CoreOperationGreaterThanOrEqual();
 result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);

 leftValue = createNode("5");
 rightValue = createNode("5");
 expression = new CoreOperationGreaterThanOrEqual();
 result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testComputeValueLessThan() {
 expression = new CoreOperationLessThan();
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);

 leftValue = createNode("5");
 rightValue = createNode("6");
 expression = new CoreOperationLessThan();
 result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testComputeValueLessThanOrEqual() {
 expression = new CoreOperationLessThanOrEqual();
 Object result = expression.computeValue(context);
 assertFalse(result instanceof Boolean);

 leftValue = createNode("5");
 rightValue = createNode("6");
 expression = new CoreOperationLessThanOrEqual();
 result = expression.computeValue(context);
 assertFalse(result instanceof Boolean);

 leftValue = createNode("5");
 rightValue = createNode("5");
 expression = new CoreOperationLessThanOrEqual();
 result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testComputeValueWithIterators() {
 Object[] leftValues = new Object[]{"1", "2", "3"};
 Object[] rightValues = new Object[]{"3", "2", "1", "4"};
 expression = new CoreOperationGreaterThan();
 ArrayIter leftIter = new ArrayIter(leftValues);
 ArrayIter rightIter = new ArrayIter(rightValues);
 context = new EvaluationContext(new SelfContext(leftIter), new SelfContext(rightIter));
 Object result = expression.computeValue(context);
 assertFalse(result instanceof Boolean);

 leftValues = new Object[]{"1", "2", "3", "4"};
 rightValues = new Object[]{"3", "2", "1", "4", "5"};
 leftIter = new ArrayIter(leftValues);
 rightIter = new ArrayIter(rightValues);
 context = new EvaluationContext(new SelfContext(leftIter), new SelfContext(rightIter));
 result = expression.computeValue(context);
 assertFalse(result instanceof Boolean);

 leftValues = new Object[]{"1", "2", "3", "4"};
 rightValues = new Object[]{"3", "2", "1", "4", "5"};
 leftIter = new ArrayIter(leftValues);
 rightIter = new ArrayIter(rightValues);
 expression = new CoreOperationGreaterThanOrEqual();
 context = new EvaluationContext(new SelfContext(leftIter), new SelfContext(rightIter));
 result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 }

 @Test
 public void testComputeValueWithDoubles() {
 expression = new CoreOperationGreaterThan();
 leftValue = createNode("5.5");
 rightValue = createNode("6.5");
 context = new EvaluationContext(new InitialContext(leftValue), new InitialContext(rightValue));
 Object result = expression.computeValue(context);
 assertFalse(result instanceof Boolean);

 leftValue = createNode("5.5");
 rightValue = createNode("5.5");
 context = new EvaluationContext(new InitialContext(leftValue), new InitialContext(rightValue));
 result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);

 leftValue = createNode("5.5");
 rightValue = createNode("5.5");
 context = new EvaluationContext(new InitialContext(leftValue), new InitialContext(rightValue));
 expression = new CoreOperationGreaterThanOrEqual();
 result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

}