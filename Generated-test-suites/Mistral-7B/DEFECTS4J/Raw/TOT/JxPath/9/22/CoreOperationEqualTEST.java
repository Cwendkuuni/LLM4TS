import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

 @Test
 public void testEqualTrue() {
 Expression arg1 = new CoreConstantExpression(1);
 Expression arg2 = new CoreConstantExpression(1);
 CoreOperationEqual equal = new CoreOperationEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 Object result = equal.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testEqualFalse() {
 Expression arg1 = new CoreConstantExpression(1);
 Expression arg2 = new CoreConstantExpression(2);
 CoreOperationEqual equal = new CoreOperationEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 Object result = equal.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testEqualNullAndNull() {
 Expression arg1 = new CoreConstantExpression(null);
 Expression arg2 = new CoreConstantExpression(null);
 CoreOperationEqual equal = new CoreOperationEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 Object result = equal.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testEqualNullAndNonNull() {
 Expression arg1 = new CoreConstantExpression(null);
 Expression arg2 = new CoreConstantExpression(1);
 CoreOperationEqual equal = new CoreOperationEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 Object result = equal.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertNull(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqualWithInvalidArguments() {
 Expression arg1 = new CoreOperationCompare(new CoreConstantExpression(1), new CoreConstantExpression(1), ">");
 CoreOperationEqual equal = new CoreOperationEqual(arg1, new CoreConstantExpression(1));
 EvalContext context = new EvalContext();
 try {
 Object result = equal.computeValue(context);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqualWithMismatchedTypes() {
 Expression arg1 = new CoreConstantExpression(1);
 Expression arg2 = new CoreVariableExpression("x");
 CoreOperationEqual equal = new CoreOperationEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 try {
 Object result = equal.computeValue(context);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}