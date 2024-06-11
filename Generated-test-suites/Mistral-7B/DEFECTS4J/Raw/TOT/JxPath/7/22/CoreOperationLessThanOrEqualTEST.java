import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.expr.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

 @Test
 public void testLessThanOrEqualWithBothNumbersGreaterThan() {
 Expression arg1 = new Const(5.5);
 Expression arg2 = new Const(6.5);
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);
 InfoSet infoSet = new JXPathContext(new EvaluationContext(), new InfoSet()).setContextItem(new Object());
 Object result = op.computeValue(new EvaluationContext(), infoSet);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testLessThanOrEqualWithBothNumbersEqual() {
 Expression arg1 = new Const(5.5);
 Expression arg2 = new Const(5.5);
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);
 InfoSet infoSet = new JXPathContext(new EvaluationContext(), new InfoSet()).setContextItem(new Object());
 Object result = op.computeValue(new EvaluationContext(), infoSet);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testLessThanOrEqualWithFirstNumberGreaterThanSecond() {
 Expression arg1 = new Const(5.5);
 Expression arg2 = new Const(3.5);
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);
 InfoSet infoSet = new JXPathContext(new EvaluationContext(), new InfoSet()).setContextItem(new Object());
 Object result = op.computeValue(new EvaluationContext(), infoSet);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLessThanOrEqualWithNullArg1() {
 Expression arg1 = null;
 Expression arg2 = new Const(5.5);
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);
 // this test case should throw an IllegalArgumentException
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLessThanOrEqualWithNullArg2() {
 Expression arg1 = new Const(5.5);
 Expression arg2 = null;
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);
 // this test case should throw an IllegalArgumentException
 }
}