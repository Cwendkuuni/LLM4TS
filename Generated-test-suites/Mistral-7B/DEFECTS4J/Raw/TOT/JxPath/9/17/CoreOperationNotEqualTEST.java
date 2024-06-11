import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.Context;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

 @Test
 public void testNotEqual_DifferentValues() {
 Expression arg1 = new StringLiteralExpression("5");
 Expression arg2 = new StringLiteralExpression("10");
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 Context testContext = new Context();
 testContext.setVariable("ctx", context);
 Object result = notEqual.computeValue(testContext);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result == Boolean.TRUE);
 }

 @Test
 public void testNotEqual_SameValue() {
 Expression arg1 = new IntegerLiteralExpression(1);
 Expression arg2 = new IntegerLiteralExpression(1);
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 Context testContext = new Context();
 testContext.setVariable("ctx", context);
 Object result = notEqual.computeValue(testContext);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result == Boolean.FALSE);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEqual_NullArg1() {
 Expression arg1 = null;
 Expression arg2 = new IntegerLiteralExpression(1);
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEqual_NullArg2() {
 Expression arg1 = new IntegerLiteralExpression(1);
 Expression arg2 = null;
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEqual_MismatchTypes() {
 Expression arg1 = new IntegerLiteralExpression(1);
 Expression arg2 = new StringLiteralExpression("5");
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 }

 @Test
 public void testNotEqual_EqualEvalContext() {
 Expression arg1 = new IntegerLiteralExpression(1);
 Expression arg2 = new IntegerLiteralExpression(1);
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 context.setVariable("x", 1);
 context.setVariable("y", 1);
 Context testContext = new Context();
 testContext.setVariable("ctx", context);
 Object result = notEqual.computeValue(testContext);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result == Boolean.FALSE);
 }

 @Test
 public void testNotEqual_NonEqualEvalContext() {
 Expression arg1 = new IntegerLiteralExpression(1);
 Expression arg2 = new IntegerLiteralExpression(2);
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 context.setVariable("x", 1);
 context.setVariable("y", 2);
 Context testContext = new Context();
 testContext.setVariable("ctx", context);
 Object result = notEqual.computeValue(testContext);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result == Boolean.TRUE);
 }
}