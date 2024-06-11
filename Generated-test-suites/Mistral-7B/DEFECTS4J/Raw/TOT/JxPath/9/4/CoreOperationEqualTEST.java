import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqualTest;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.functions.Functions;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationEqualTest {

 @Test
 public void testEqualBoolToBool() {
 CoreOperationEqual equal = new CoreOperationEqual(new CoreConstantExpression(true), new CoreConstantExpression(true));
 Object result = equal.computeValue(new EvalContext());
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testEqualBoolToFalse() {
 CoreOperationEqual equal = new CoreOperationEqual(new CoreConstantExpression(true), new CoreConstantExpression(false));
 Object result = equal.computeValue(new EvalContext());
 assertFalse(result instanceof Boolean);
 assertFalse(result.toString().equals("true"));
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testEqualNullToNull() {
 CoreOperationEqual equal = new CoreOperationEqual(new CoreConstantExpression(null), new CoreConstantExpression(null));
 Object result = equal.computeValue(new EvalContext());
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testEqualNullToNonnull() {
 CoreOperationEqual equal = new CoreOperationEqual(new CoreConstantExpression(null), new CoreConstantExpression("test"));
 Object result = equal.computeValue(new EvalContext());
 assertFalse(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testEqualNumToNum() {
 CoreOperationEqual equal = new CoreOperationEqual(new CoreConstantExpression(1), new CoreConstantExpression(1));
 Object result = equal.computeValue(new EvalContext());
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testEqualNumToDifferentNum() {
 CoreOperationEqual equal = new CoreOperationEqual(new CoreConstantExpression(1), new CoreConstantExpression(2));
 Object result = equal.computeValue(new EvalContext());
 assertFalse(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testEqualNumToString() {
 CoreOperationEqual equal = new CoreOperationEqual(new CoreConstantExpression(1), new CoreConstantExpression("1"));
 Object result = equal.computeValue(new EvalContext());
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqualArgNull() {
 CoreOperationEqual equal = new CoreOperationEqual(null, new CoreConstantExpression(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqualArg2Null() {
 CoreOperationEqual equal = new CoreOperationEqual(new CoreConstantExpression(1), null);
 }

 @Test
 public void testEqualsFunction() {
 CoreOperationEqual equal = new CoreOperationEqual(new CoreFunctionExpression(Functions.EQUALS, new CoreExpression[]{new CoreConstantExpression("test"), new CoreConstantExpression("test")}), null);
 Object result = equal.computeValue(new EvalContext());
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testEqualsFunctionNotEqual() {
 CoreOperationEqual equal = new CoreOperationEqual(new CoreFunctionExpression(Functions.EQUALS, new CoreExpression[]{new CoreConstantExpression("test1"), new CoreConstantExpression("test2")}), null);
 Object result = equal.computeValue(new EvalContext());
 assertFalse(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }
}