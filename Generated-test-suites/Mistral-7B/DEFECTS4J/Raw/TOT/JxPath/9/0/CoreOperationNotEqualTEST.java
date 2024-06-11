import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual.CoreOperationCompareArg;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.util.ExpressionUtil;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationNotEqualTest {

 @Test
 public void testNotEqualBooleanFalseAndBooleanFalse() {
 CoreOperationCompareArg arg1 = new CoreOperationCompareArg(Boolean.FALSE, null);
 CoreOperationCompareArg arg2 = new CoreOperationCompareArg(Boolean.FALSE, null);
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 Object result = notEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testNotEqualBooleanFalseAndBooleanTrue() {
 CoreOperationCompareArg arg1 = new CoreOperationCompareArg(Boolean.FALSE, null);
 CoreOperationCompareArg arg2 = new CoreOperationCompareArg(Boolean.TRUE, null);
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 Object result = notEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testNotEqualBooleanTrueAndBooleanFalse() {
 CoreOperationCompareArg arg1 = new CoreOperationCompareArg(Boolean.TRUE, null);
 CoreOperationCompareArg arg2 = new CoreOperationCompareArg(Boolean.FALSE, null);
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 Object result = notEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testNotEqualBooleanTrueAndBooleanTrue() {
 CoreOperationCompareArg arg1 = new CoreOperationCompareArg(Boolean.TRUE, null);
 CoreOperationCompareArg arg2 = new CoreOperationCompareArg(Boolean.TRUE, null);
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 Object result = notEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testNotEqualNullAndString() {
 CoreOperationCompareArg arg1 = new CoreOperationCompareArg(null, null);
 CoreOperationCompareArg arg2 = new CoreOperationCompareArg(new String("test"), null);
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 Object result = notEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testNotEqualNullAndNull() {
 CoreOperationCompareArg arg1 = new CoreOperationCompareArg(null, null);
 CoreOperationCompareArg arg2 = new CoreOperationCompareArg(null, null);
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 Object result = notEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEqualNullAndNonNullableExpression() {
 CoreOperationCompareArg arg1 = new CoreOperationCompareArg(null, null);
 CoreOperationCompareArg arg2 = new CoreOperationCompareArg(new String("test"), null);
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 notEqual.computeValue(context);
 }

 @Test
 public void testNotEqualExpressionWithString() {
 CoreOperationCompareArg arg1 = ExpressionUtil.compileExpression("true()", null);
 CoreOperationCompareArg arg2 = new CoreOperationCompareArg(new String("test"), null);
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 Object result = notEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testNotEqualExpressionWithExpression() {
 CoreOperationCompareArg arg1 = ExpressionUtil.compileExpression("2 > 1", null);
 CoreOperationCompareArg arg2 = ExpressionUtil.compileExpression("2 > 1", null);
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 Object result = notEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testNotEqualDifferentDataTypes() {
 CoreOperationCompareArg arg1 = new CoreOperationCompareArg(new Integer(1), null);
 CoreOperationCompareArg arg2 = new CoreOperationCompareArg(new Double("2.0"), null);
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 Object result = notEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testNotEqualNullContext() {
 CoreOperationCompareArg arg1 = new CoreOperationCompareArg(Boolean.FALSE, null);
 CoreOperationCompareArg arg2 = new CoreOperationCompareArg(Boolean.FALSE, null);
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 assertNull(notEqual.computeValue(null));
 }
}