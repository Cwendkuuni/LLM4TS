import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

 @Test
 public void testGreaterThan() {
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);

 Expression arg1 = new LiteralExpression(1.5);
 Expression arg2 = new LiteralExpression(1.0);
 CoreOperationGreaterThan greaterThan = new CoreOperationGreaterThan(arg1, arg2);

 assertTrue(greaterThan.computeValue(context).booleanValue());
 }

 @Test
 public void testNotGreaterThan() {
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);

 Expression arg1 = new LiteralExpression(1.0);
 Expression arg2 = new LiteralExpression(1.5);
 CoreOperationGreaterThan greaterThan = new CoreOperationGreaterThan(arg1, arg2);

 assertFalse(greaterThan.computeValue(context).booleanValue());
 }

 @Test
 public void testEqual() {
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);

 Expression arg1 = new LiteralExpression(1.5);
 Expression arg2 = new LiteralExpression(1.5);
 CoreOperationGreaterThan greaterThan = new CoreOperationGreaterThan(arg1, arg2);

 assertFalse(greaterThan.computeValue(context).booleanValue());
 }

 @Test(expected = ClassCastException.class)
 public void testArg1NotNumeric() {
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);

 Expression arg1 = new LiteralExpression("abc");
 Expression arg2 = new LiteralExpression(1.0);
 CoreOperationGreaterThan greaterThan = new CoreOperationGreaterThan(arg1, arg2);
 }

 @Test(expected = ClassCastException.class)
 public void testArg2NotNumeric() {
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);

 Expression arg1 = new LiteralExpression(1.0);
 Expression arg2 = new LiteralExpression("abc");
 CoreOperationGreaterThan greaterThan = new CoreOperationGreaterThan(arg1, arg2);
 }
}