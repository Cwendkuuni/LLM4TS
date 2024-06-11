import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqualTest;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.util.TestUtils;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationEqualTest extends CoreOperationCompareTest {

 @Test
 public void testEqual() {
 CoreOperationEqual op = new CoreOperationEqual(new ConstantExpression(1), new ConstantExpression(1));
 Assert.assertEquals(Boolean.TRUE, op.computeValue(TestUtils.getContext()));

 op = new CoreOperationEqual(new ConstantExpression(1), new ConstantExpression(2));
 Assert.assertEquals(Boolean.FALSE, op.computeValue(TestUtils.getContext()));
 }

 @Test
 public void testEqualNull() {
 CoreOperationEqual op = new CoreOperationEqual(new ConstantExpression(1), new ConstantExpression(null));
 Assert.assertEquals(Boolean.FALSE, op.computeValue(TestUtils.getContext()));

 op = new CoreOperationEqual(new ConstantExpression(null), new ConstantExpression(null));
 Assert.assertEquals(Boolean.TRUE, op.computeValue(TestUtils.getContext()));
 }

 @Test
 public void testEqualTypeMismatch() {
 CoreOperationEqual op = new CoreOperationEqual(new ConstantExpression(1), new NumberExpression(1.0));
 Assert.assertEquals(Boolean.FALSE, op.computeValue(TestUtils.getContext()));
 }

 @Test
 public void testEqualNaN() {
 CoreOperationEqual op = new CoreOperationEqual(new ConstantExpression(Double.NaN), new ConstantExpression(Double.NaN));
 Assert.assertEquals(Boolean.TRUE, op.computeValue(TestUtils.getContext()));
 }

 @Test
 public void testEqualLongAndInt() {
 CoreOperationEqual op = new CoreOperationEqual(new ConstantExpression(1L), new ConstantExpression(1));
 Assert.assertEquals(Boolean.TRUE, op.computeValue(TestUtils.getContext()));
 }

 @Test
 public void testEqualComplex() {
 CoreOperationEqual op = new CoreOperationEqual(new VariableExpression("x"), new VariableExpression("x"));
 op.setVariableValue("x", new ConstantExpression(1));
 Assert.assertEquals(Boolean.TRUE, op.computeValue(TestUtils.getContext()));
 }

 @Test
 public void testEqualsNullContext() {
 CoreOperationEqual op = new CoreOperationEqual(new ConstantExpression(1), new ConstantExpression(1));
 Assert.assertNull(op.computeValue(null));
 }
}