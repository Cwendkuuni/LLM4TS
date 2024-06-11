import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

 @Test
 public void testGreaterThan() {
 Expression arg1 = new Literal(1.0);
 Expression arg2 = new Literal(2.0);
 CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);

 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);

 Object result = op.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testNotGreaterThan() {
 Expression arg1 = new Literal(1.0);
 Expression arg2 = new Literal(2.0);
 CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);

 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);

 Object result = op.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertFalse((Boolean) op.getSymbol().toString().equals(">"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArguments() {
 Expression arg1 = new CoreOperationGreaterThan(new Literal(1.0), new Literal(1.0));
 }

 @Test
 public void testEqualValues() {
 Expression arg1 = new Literal(1.0);
 Expression arg2 = new Literal(1.0);
 CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);

 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);

 Object result = op.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testNegativeValues() {
 Expression arg1 = new Literal(-1.0);
 Expression arg2 = new Literal(-2.0);
 CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);

 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);

 Object result = op.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testNaN() {
 Expression arg1 = new Literal(Double.NaN);
 Expression arg2 = new Literal(2.0);
 CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);

 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);

 Object result = op.computeValue(context);
 assertFalse(result instanceof Boolean);
 }

 @Test
 public void testNullArg() {
 Expression arg1 = new Literal((Object) null);
 Expression arg2 = new Literal(2.0);
 CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);

 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);

 Object result = op.computeValue(context);
 assertFalse(result instanceof Boolean);
 }
}