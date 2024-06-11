import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationGreaterThanTest {

 @Test
 public void testCoreOperationGreaterThan_Numbers_WithinRange() {
 Expression arg1 = new Const(2.5);
 Expression arg2 = new Const(1.5);
 CoreOperationGreaterThan expression = new CoreOperationGreaterThan(arg1, arg2);

 EvalContext context = new EvalContext(new Path(""), new InfoSet(), new NodeInfo(null), null);
 Object result = expression.computeValue(context);

 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testCoreOperationGreaterThan_Numbers_Equal() {
 Expression arg1 = new Const(2.5);
 Expression arg2 = new Const(2.5);
 CoreOperationGreaterThan expression = new CoreOperationGreaterThan(arg1, arg2);

 EvalContext context = new EvalContext(new Path(""), new InfoSet(), new NodeInfo(null), null);
 Object result = expression.computeValue(context);

 assertFalse(result instanceof Boolean);
 }

 @Test
 public void testCoreOperationGreaterThan_Numbers_NotWithinRange_FirstGreater() {
 Expression arg1 = new Const(5.5);
 Expression arg2 = new Const(2.5);
 CoreOperationGreaterThan expression = new CoreOperationGreaterThan(arg1, arg2);

 EvalContext context = new EvalContext(new Path(""), new InfoSet(), new NodeInfo(null), null);
 Object result = expression.computeValue(context);

 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test(expected = java.lang.ClassCastException.class)
 public void testCoreOperationGreaterThan_MixedTypes() {
 Expression arg1 = new Const(2.5);
 Expression arg2 = new StringConst("5");
 CoreOperationGreaterThan expression = new CoreOperationGreaterThan(arg1, arg2);

 EvalContext context = new EvalContext(new Path(""), new InfoSet(), new NodeInfo(null), null);
 Object result = expression.computeValue(context);
 }

 @Test(expected = java.lang.ArithmeticException.class)
 public void testCoreOperationGreaterThan_NaN() {
 Expression arg1 = new Const(Double.NaN);
 Expression arg2 = new Const(1.5);
 CoreOperationGreaterThan expression = new CoreOperationGreaterThan(arg1, arg2);

 EvalContext context = new EvalContext(new Path(""), new InfoSet(), new NodeInfo(null), null);
 Object result = expression.computeValue(context);
 }

 @Test(expected = java.lang.ArithmeticException.class)
 public void testCoreOperationGreaterThan_NegativeNumbers() {
 Expression arg1 = new Const(-2.5);
 Expression arg2 = new Const(-1.5);
 CoreOperationGreaterThan expression = new CoreOperationGreaterThan(arg1, arg2);

 EvalContext context = new EvalContext(new Path(""), new InfoSet(), new NodeInfo(null), null);
 Object result = expression.computeValue(context);
 }
}