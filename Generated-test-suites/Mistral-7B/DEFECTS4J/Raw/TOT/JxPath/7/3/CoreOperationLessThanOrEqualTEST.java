import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

 @Test
 public void testLessThanOrEqualWithDoubleValues() {
 Expression arg1 = new Const(1.0);
 Expression arg2 = new Const(2.0);
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);

 InfoSet infoSet = new InfoSet();
 infoSet.setRoot(new NodeInfo(new Path(""), new Object(), null));
 EvalContext context = new EvalContext(infoSet);

 Object result = op.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testLessThanOrEqualWithDoubleValuesFalse() {
 Expression arg1 = new Const(3.0);
 Expression arg2 = new Const(2.0);
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);

 InfoSet infoSet = new InfoSet();
 infoSet.setRoot(new NodeInfo(new Path(""), new Object(), null));
 EvalContext context = new EvalContext(infoSet);

 Object result = op.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertNull(result);
 }

 @Test(expected = IllegalStateException.class)
 public void testLessThanOrEqualWithNonNumericValues() {
 Expression arg1 = new Const("abc");
 Expression arg2 = new Const(2.0);
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);

 InfoSet infoSet = new InfoSet();
 infoSet.setRoot(new NodeInfo(new Path(""), new Object(), null));
 EvalContext context = new EvalContext(infoSet);

 op.computeValue(context); // this should throw an exception
 }

 @Test(expected = IllegalStateException.class)
 public void testLessThanOrEqualWithNullArguments() {
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(null, new Const(2.0));

 assertNull(op.getSymbol());
 assertNull(op.args);

 try {
 op.computeValue(new EvalContext(new InfoSet()));
 fail("Expected an IllegalStateException");
 } catch (IllegalStateException e) {
 // expected
 }
 }
}