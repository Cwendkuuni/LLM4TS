import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqualTest;
import org.apache.commons.jxpath.ri.compiler.CoreOperationTest;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.util.JXPathContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest extends CoreOperationTest {

 @Test
 public void testEqual() {
 CoreOperationEqual operation = new CoreOperationEqual(new ConstantExpression(1), new ConstantExpression(1));
 EvalContext context = new EvalContext(new JXPathContext(new Object[] { 1, 1 }));
 Object result = operation.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testNotEqual() {
 CoreOperationEqual operation = new CoreOperationEqual(new ConstantExpression(1), new ConstantExpression(2));
 EvalContext context = new EvalContext(new JXPathContext(new Object[] { 1, 2 }));
 Object result = operation.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testLeftNull() {
 CoreOperationEqual operation = new CoreOperationEqual(new ConstantExpression(null), new ConstantExpression(1));
 EvalContext context = new EvalContext(new JXPathContext(new Object[] { null, 1 }));
 Object result = operation.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testRightNull() {
 CoreOperationEqual operation = new CoreOperationEqual(new ConstantExpression(1), new ConstantExpression(null));
 EvalContext context = new EvalContext(new JXPathContext(new Object[] { 1, null }));
 Object result = operation.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidType() {
 CoreOperationEqual operation = new CoreOperationEqual(new NumberExpression("1"), new StringExpression("2"));
 EvalContext context = new EvalContext(new JXPathContext(new Object[] {}));
 try {
 operation.computeValue(context);
 } catch (IllegalArgumentException e) {
 fail("Did not throw IllegalArgumentException when left expression is not a number");
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidType2() {
 CoreOperationEqual operation = new CoreOperationEqual(new StringExpression("1"), new NumberExpression("2"));
 EvalContext context = new EvalContext(new JXPathContext(new Object[] {}));
 try {
 operation.computeValue(context);
 } catch (IllegalArgumentException e) {
 fail("Did not throw IllegalArgumentException when right expression is not a string");
 }
 }
}