import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.JXPathContext;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationLessThanTest {

 @Test
 public void testLessThan() {
 // Test case 1: Valid input, both arguments are numbers, and the first argument is less than the second
 Expression arg1 = new ConstantExpression(1.0);
 Expression arg2 = new ConstantExpression(2.0);
 CoreOperationLessThan lessThanExpression = new CoreOperationLessThan(arg1, arg2);
 InfoSet infoSet = new JXPathContext().newContext(new Object[] {});
 EvaluationContext context = new EvaluationContext(infoSet, new JXPathContext());
 Object result = lessThanExpression.computeValue(context);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testLessThanWithZero() {
 // Test case 2: Valid input, both arguments are numbers, and the first argument is less than or equal to zero, and the second argument is a positive number
 Expression arg1 = new ConstantExpression(0.0);
 Expression arg2 = new ConstantExpression(1.0);
 CoreOperationLessThan lessThanExpression = new CoreOperationLessThan(arg1, arg2);
 InfoSet infoSet = new JXPathContext().newContext(new Object[] {});
 EvaluationContext context = new EvaluationContext(infoSet, new JXPathContext());
 Object result = lessThanExpression.computeValue(context);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testLessThanWithNaN() {
 // Test case 3: Valid input, both arguments are numbers, and one of them is NaN
 Expression arg1 = new ConstantExpression(Double.NaN);
 Expression arg2 = new ConstantExpression(1.0);
 CoreOperationLessThan lessThanExpression = new CoreOperationLessThan(arg1, arg2);
 InfoSet infoSet = new JXPathContext().newContext(new Object[] {});
 EvaluationContext context = new EvaluationContext(infoSet, new JXPathContext());
 Object result = lessThanExpression.computeValue(context);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testLessThanWithStrings() {
 // Test case 4: Invalid input, both arguments are strings
 Expression arg1 = new ConstantExpression("abc");
 Expression arg2 = new ConstantExpression("def");
 CoreOperationLessThan lessThanExpression = new CoreOperationLessThan(arg1, arg2);
 InfoSet infoSet = new JXPathContext().newContext(new Object[] {});
 EvaluationContext context = new EvaluationContext(infoSet, new JXPathContext());
 Object result = lessThanExpression.computeValue(context);
 assertEquals(Boolean.FALSE, result);
 }

 @Test(expected = ClassCastException.class)
 public void testLessThanWithMismatchedTypes() {
 // Test case 5: Invalid input, both arguments have different types
 Expression arg1 = new ConstantExpression(1.0);
 Expression arg2 = new ConstantStringExpression("abc");
 CoreOperationLessThan lessThanExpression = new CoreOperationLessThan(arg1, arg2);
 InfoSet infoSet = new JXPathContext().newContext(new Object[] {});
 EvaluationContext context = new EvaluationContext(infoSet, new JXPathContext());
 lessThanExpression.computeValue(context);
 }
}