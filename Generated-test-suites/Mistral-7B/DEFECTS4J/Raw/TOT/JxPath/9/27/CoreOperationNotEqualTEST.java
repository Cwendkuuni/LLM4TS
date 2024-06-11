import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual.CoreOperationNotEqualArg;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationNotEqualTest {

 @Test
 public void testNotEqualWithBooleanFalseAndFalse() {
 CoreOperationNotEqualArg arg1 = new CoreOperationNotEqualArg(Boolean.FALSE);
 CoreOperationNotEqualArg arg2 = new CoreOperationNotEqualArg(Boolean.FALSE);
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 Object result = notEqual.computeValue(context);
 assertTrue(result == Boolean.TRUE);
 }

 @Test
 public void testNotEqualWithBooleanFalseAndTrue() {
 CoreOperationNotEqualArg arg1 = new CoreOperationNotEqualArg(Boolean.FALSE);
 CoreOperationNotEqualArg arg2 = new CoreOperationNotEqualArg(Boolean.TRUE);
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 Object result = notEqual.computeValue(context);
 assertFalse(result == Boolean.FALSE);
 }

 @Test
 public void testNotEqualWithBooleanTrueAndFalse() {
 CoreOperationNotEqualArg arg1 = new CoreOperationNotEqualArg(Boolean.TRUE);
 CoreOperationNotEqualArg arg2 = new CoreOperationNotEqualArg(Boolean.FALSE);
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 Object result = notEqual.computeValue(context);
 assertTrue(result == Boolean.FALSE);
 }

 @Test
 public void testNotEqualWithBooleanTrueAndTrue() {
 CoreOperationNotEqualArg arg1 = new CoreOperationNotEqualArg(Boolean.TRUE);
 CoreOperationNotEqualArg arg2 = new CoreOperationNotEqualArg(Boolean.TRUE);
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 Object result = notEqual.computeValue(context);
 assertNull(result);
 }

 @Test(expected = NullPointerException.class)
 public void testNotEqualWithNullArg1() {
 CoreOperationNotEqualArg arg1 = null;
 CoreOperationNotEqualArg arg2 = new CoreOperationNotEqualArg(Boolean.FALSE);
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 notEqual.computeValue(context);
 }

 @Test(expected = NullPointerException.class)
 public void testNotEqualWithNullArg2() {
 CoreOperationNotEqualArg arg1 = new CoreOperationNotEqualArg(Boolean.FALSE);
 CoreOperationNotEqualArg arg2 = null;
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 notEqual.computeValue(context);
 }

 @Test
 public void testNotEqualWithNonBooleanArg1() {
 CoreOperationNotEqualArg arg1 = new CoreOperationNotEqualArg(new Integer(1));
 CoreOperationNotEqualArg arg2 = new CoreOperationNotEqualArg(Boolean.TRUE);
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 Object result = notEqual.computeValue(context);
 assertNull(result);
 }

 @Test
 public void testNotEqualWithNonBooleanArg2() {
 CoreOperationNotEqualArg arg1 = new CoreOperationNotEqualArg(Boolean.TRUE);
 CoreOperationNotEqualArg arg2 = new CoreOperationNotEqualArg(new Integer(1));
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 Object result = notEqual.computeValue(context);
 assertNull(result);
 }
}