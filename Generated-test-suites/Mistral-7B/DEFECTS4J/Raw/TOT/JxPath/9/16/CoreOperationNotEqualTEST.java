import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.expressions.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

 @Test
 public void testNotEqualWithStrings() {
 Expression arg1 = new CoreStringLiteralExpression("hello");
 Expression arg2 = new CoreStringLiteralExpression("world");
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 Object result = notEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testNotEqualWithNumbers() {
 Expression arg1 = new CoreNumberLiteralExpression(1);
 Expression arg2 = new CoreNumberLiteralExpression(2);
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 Object result = notEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testNotEqualWithSameValue() {
 Expression arg1 = new CoreNumberLiteralExpression(1);
 Expression arg2 = new CoreNumberLiteralExpression(1);
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 Object result = notEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEqualWithNullArg1() {
 Expression arg1 = null;
 Expression arg2 = new CoreNumberLiteralExpression(1);
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 notEqual.computeValue(context); // This will throw an exception
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEqualWithNullArg2() {
 Expression arg1 = new CoreNumberLiteralExpression(1);
 Expression arg2 = null;
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 notEqual.computeValue(context); // This will throw an exception
 }
}