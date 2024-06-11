import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual_Expression_arg1_Expression_arg2_Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationEqualTest {

 @Test
 public void testEqual() {
 Expression arg1 = new CoreOperationEqual_Expression_arg1_Expression_arg2_Test(1, 1);
 Expression arg2 = new CoreOperationEqual_Expression_arg1_Expression_arg2_Test(1, 1);
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

 EvalContext context = new EvalContext();
 Object result = coreOperationEqual.computeValue(context);

 Assert.assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testNotEqual() {
 Expression arg1 = new CoreOperationEqual_Expression_arg1_Expression_arg2_Test(1, 2);
 Expression arg2 = new CoreOperationEqual_Expression_arg1_Expression_arg2_Test(1, 1);
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

 EvalContext context = new EvalContext();
 Object result = coreOperationEqual.computeValue(context);

 Assert.assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testNullArg1() {
 Expression arg1 = null;
 Expression arg2 = new CoreOperationEqual_Expression_arg1_Expression_arg2_Test(1, 1);
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

 EvalContext context = new EvalContext();
 Object result = coreOperationEqual.computeValue(context);

 Assert.assertNull(result);
 }

 @Test
 public void testNullArg2() {
 Expression arg1 = new CoreOperationEqual_Expression_arg1_Expression_arg2_Test(1, 1);
 Expression arg2 = null;
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

 EvalContext context = new EvalContext();
 Object result = coreOperationEqual.computeValue(context);

 Assert.assertNull(result);
 }

 @Test
 public void testCompareDifferentTypes() {
 Expression arg1 = new CoreOperationEqual_Expression_arg1_Expression_arg2_Test(1, "1");
 Expression arg2 = new CoreOperationEqual_Expression_arg1_Expression_arg2_Test("1", 1);
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

 EvalContext context = new EvalContext();
 Object result = coreOperationEqual.computeValue(context);

 Assert.assertFalse(result.booleanValue());
 }

 @Test
 public void testCompareDifferentTypesWithStringOneNull() {
 Expression arg1 = new CoreOperationEqual_Expression_arg1_Expression_arg2_Test(null, "1");
 Expression arg2 = new CoreOperationEqual_Expression_arg1_Expression_arg2_Test("1", 1);
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

 EvalContext context = new EvalContext();
 Object result = coreOperationEqual.computeValue(context);

 Assert.assertFalse(result.booleanValue());
 }

 @Test
 public void testCompareDifferentTypesWithNumberOneNull() {
 Expression arg1 = new CoreOperationEqual_Expression_arg1_Expression_arg2_Test(1, null);
 Expression arg2 = new CoreOperationEqual_Expression_arg1_Expression_arg2_Test("1", 1);
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

 EvalContext context = new EvalContext();
 Object result = coreOperationEqual.computeValue(context);

 Assert.assertFalse(result.booleanValue());
 }

 @Test
 public void testCompareSameTypeDifferentValues() {
 Expression arg1 = new CoreOperationEqual_Expression_arg1_Expression_arg2_Test(2, 1);
 Expression arg2 = new CoreOperationEqual_Expression_arg1_Expression_arg2_Test(1, 1);
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

 EvalContext context = new EvalContext();
 Object result = coreOperationEqual.computeValue(context);

 Assert.assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testCompareSameTypeSameValues() {
 Expression arg1 = new CoreOperationEqual_Expression_arg1_Expression_arg2_Test(1, 1);
 Expression arg2 = new CoreOperationEqual_Expression_arg1_Expression_arg2_Test(1, 1);
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

 EvalContext context = new EvalContext();
 Object result = coreOperationEqual.computeValue(context);

 Assert.assertEquals(Boolean.TRUE, result);
 }
}