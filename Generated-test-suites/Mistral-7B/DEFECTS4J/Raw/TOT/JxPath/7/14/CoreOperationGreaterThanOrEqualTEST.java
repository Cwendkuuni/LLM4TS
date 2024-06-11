import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.nodes.NumberNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

 @Test
 public void testGreaterThanOrEqual() {
 Expression arg1 = new NumberNode(5.0);
 Expression arg2 = new NumberNode(5.0);
 CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);

 InfoSet is = InfoSet.EMPTY;
 EvalContext context = new EvalContext(is);

 Object result = op.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testGreaterThan() {
 Expression arg1 = new NumberNode(5.0);
 Expression arg2 = new NumberNode(3.0);
 CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);

 InfoSet is = InfoSet.EMPTY;
 EvalContext context = new EvalContext(is);

 Object result = op.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testNotGreaterThanOrEqual() {
 Expression arg1 = new NumberNode(5.0);
 Expression arg2 = new NumberNode(6.0);
 CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);

 InfoSet is = InfoSet.EMPTY;
 EvalContext context = new EvalContext(is);

 Object result = op.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertFalse(result.equals(Boolean.TRUE));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullArg1() {
 Expression arg1 = null;
 Expression arg2 = new NumberNode(5.0);
 CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);

 InfoSet is = InfoSet.EMPTY;
 EvalContext context = new EvalContext(is);

 try {
 op.computeValue(context);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Success
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullArg2() {
 Expression arg1 = new NumberNode(5.0);
 Expression arg2 = null;
 CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEQUAL(arg1, arg2);

 InfoSet is = InfoSet.EMPTY;
 EvalContext context = new EvalContext(is);

 try {
 op.computeValue(context);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Success
 }
 }
}