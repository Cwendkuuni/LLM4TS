import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqualTestCase;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.JsPathContext;
import org.junit.Test;

/**
 * Test for CoreOperationEqual class.
 */
public class CoreOperationEqualTest extends CoreOperationEqualTestCase {

 @Test
 public void testEqual_Boolean_Boolean() {
 Expression arg1 = new ConstantExpression(true);
 Expression arg2 = new ConstantExpression(true);
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
 EvalContext context = new EvalContext(new JsPathContext());
 Object result = coreOperationEqual.computeValue(context);
 assertEquals("true == true should return true", Boolean.TRUE, result);
 }

 @Test
 public void testEqual_Boolean_False() {
 Expression arg1 = new ConstantExpression(true);
 Expression arg2 = new ConstantExpression(false);
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
 EvalContext context = new EvalContext(new JsPathContext());
 Object result = coreOperationEqual.computeValue(context);
 assertEquals("true == false should return false", Boolean.FALSE, result);
 }

 @Test
 public void testEqual_Integer_Integer() {
 Expression arg1 = new ConstantExpression(1);
 Expression arg2 = new ConstantExpression(1);
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
 EvalContext context = new EvalContext(new JsPathContext());
 Object result = coreOperationEqual.computeValue(context);
 assertEquals("1 == 1 should return true", Boolean.TRUE, result);
 }

 @Test
 public void testEqual_Integer_Long() {
 Expression arg1 = new ConstantExpression(1);
 Expression arg2 = new ConstantExpression(1L);
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
 EvalContext context = new EvalContext(new JsPathContext());
 Object result = coreOperationEqual.computeValue(context);
 assertEquals("1 == 1L should return true", Boolean.TRUE, result);
 }

 @Test(expected = ClassCastException.class)
 public void testEqual_String_Integer() {
 Expression arg1 = new ConstantExpression("hello");
 Expression arg2 = new ConstantExpression(1);
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
 EvalContext context = new EvalContext(new JsPathContext());
 coreOperationEqual.computeValue(context);
 fail("String != Integer should throw ClassCastException");
 }

 @Test
 public void testEqual_Null_Null() {
 Expression arg1 = new ConstantExpression(null);
 Expression arg2 = new ConstantExpression(null);
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
 EvalContext context = new EvalContext(new JsPathContext());
 Object result = coreOperationEqual.computeValue(context);
 assertEquals("null == null should return true", Boolean.TRUE, result);
 }

 @Test
 public void testEqual_Null_Object() {
 Expression arg1 = new ConstantExpression(null);
 Expression arg2 = new ConstantExpression("test");
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
 EvalContext context = new EvalContext(new JsPathContext());
 Object result = coreOperationEqual.computeValue(context);
 assertNull("null != Object should return null", result);
 }

 @Test
 public void testEqual_Object_Null() {
 Expression arg1 = new ConstantExpression("test");
 Expression arg2 = new ConstantExpression(null);
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
 EvalContext context = new EvalContext(new JsPathContext());
 Object result = coreOperationEqual.computeValue(context);
 assertNull("Object != null should return null", result);
 }

 @Test
 public void testEqual_Object_Object() {
 Expression arg1 = new ConstantExpression("test1");
 Expression arg2 = new ConstantExpression("test1");
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
 EvalContext context = new EvalContext(new JsPathContext());
 Object result = coreOperationEqual.computeValue(context);
 assertEquals("\"test1\" == \"test1\" should return true", Boolean.TRUE, result);
 }

 @Test
 public void testEqual_Object_DifferentObject() {
 Expression arg1 = new ConstantExpression("test1");
 Expression arg2 = new ConstantExpression("test2");
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
 EvalContext context = new EvalContext(new JsPathContext());
 Object result = coreOperationEqual.computeValue(context);
 assertEquals("\"test1\" != \"test2\" should return false", Boolean.FALSE, result);
 }
}