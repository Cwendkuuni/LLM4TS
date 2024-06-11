import org.apache.commons.jxpath.ri.CompilerTest;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.iterator.SingletonIterator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest extends CompilerTest {

 private CoreOperationRelationalExpression createExpression(Expression... args) {
 return new CoreOperationRelationalExpression(args) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare > 0;
 }
 };
 }

 @Test
 public void testGreaterThan() {
 Expression left = new ConstantExpression(new Integer(1));
 Expression right = new ConstantExpression(new Integer(2));
 CoreOperationRelationalExpression expression = createExpression(left, right);
 EvalContext context = createEvalContext();
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testGreaterThanOrEqual() {
 Expression left = new ConstantExpression(new Integer(1));
 Expression right = new ConstantExpression(new Integer(1));
 CoreOperationRelationalExpression expression = createExpression(left, right);
 EvalContext context = createEvalContext();
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);

 left = new ConstantExpression(new Integer(1));
 right = new ConstantExpression(new Integer(2));
 expression = createExpression(left, right);
 context = createEvalContext();
 result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testLessThan() {
 Expression left = new ConstantExpression(new Integer(2));
 Expression right = new ConstantExpression(new Integer(1));
 CoreOperationRelationalExpression expression = createExpression(left, right);
 EvalContext context = createEvalContext();
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testLessThanOrEqual() {
 Expression left = new ConstantExpression(new Integer(2));
 Expression right = new ConstantExpression(new Integer(2));
 CoreOperationRelationalExpression expression = createExpression(left, right);
 EvalContext context = createEvalContext();
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);

 left = new ConstantExpression(new Integer(2));
 right = new ConstantExpression(new Integer(1));
 expression = createExpression(left, right);
 context = createEvalContext();
 result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testComparisonWithCollections() {
 Expression left = new SingletonIterator(new Integer(1));
 Expression right = new SingletonIterator(new Integer(1));
 CoreOperationRelationalExpression expression = createExpression(left, right);
 EvalContext context = createEvalContext();
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);

 left = new SingletonIterator(new Integer(1));
 right = new SingletonIterator(new Integer(2));
 expression = createExpression(left, right);
 context = createEvalContext();
 result = expression.computeValue(context);
 assertFalse(result instanceof Boolean);
 }

 @Test
 public void testComparisonWithDoubles() {
 Expression left = new ConstantExpression(new Double(1.0));
 Expression right = new ConstantExpression(new Double(2.0));
 CoreOperationRelationalExpression expression = createExpression(left, right);
 EvalContext context = createEvalContext();
 Object result = expression.computeValue(context);
 assertFalse(result instanceof Boolean);

 left = new ConstantExpression(new Double(1.0));
 right = new ConstantExpression(new Double(1.0));
 expression = createExpression(left, right);
 context = createEvalContext();
 result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testComparisonWithSelfContext() {
 Expression left = new SelfContext();
 Expression right = new ConstantExpression(new Integer(1));
 CoreOperationRelationalExpression expression = createExpression(left, right);
 EvalContext context = createEvalContext();
 Object result = expression.computeValue(context);
 assertNotNull(result);
 assertFalse(result instanceof Boolean);
 }

 @Test(expected = RuntimeException.class)
 public void testComparisonWithInvalidArguments() {
 Expression left = new CoreOperation("invalid");
 Expression right = new ConstantExpression(new Integer(1));
 CoreOperationRelationalExpression expression = createExpression(left, right);
 EvalContext context = createEvalContext();
 expression.computeValue(context);
 }

}