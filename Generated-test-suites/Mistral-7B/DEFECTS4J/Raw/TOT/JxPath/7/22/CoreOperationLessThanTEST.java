import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

 @Test
 public void testLessThan() {
 InfoSet is = InfoSetUtil.createInfoSet(); // Empty InfoSet
 EvalContext context = new EvalContext(is);

 Expression arg1 = new LiteralExpression(1.0, context);
 Expression arg2 = new LiteralExpression(2.0, context);
 CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);

 Boolean result = (Boolean) op.computeValue(context);
 assertTrue(result.booleanValue());
 }

 @Test
 public void testLessThanTrueFalse() {
 InfoSet is = InfoSetUtil.createInfoSet(); // Empty InfoSet
 EvalContext context = new EvalContext(is);

 Expression arg1 = new LiteralExpression(1.0, context);
 Expression arg2 = new LiteralExpression(1.0, context);
 CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);

 Boolean result = (Boolean) op.computeValue(context);
 assertFalse(result.booleanValue());
 }

 @Test
 public void testLessThanZero() {
 InfoSet is = InfoSetUtil.createInfoSet(); // Empty InfoSet
 EvalContext context = new EvalContext(is);

 Expression arg1 = new LiteralExpression(-1.0, context);
 Expression arg2 = new LiteralExpression(1.0, context);
 CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);

 Boolean result = (Boolean) op.computeValue(context);
 assertTrue(result.booleanValue());
 }

 @Test
 public void testLessThanZeroZero() {
 InfoSet is = InfoSetUtil.createInfoSet(); // Empty InfoSet
 EvalContext context = new EvalContext(is);

 Expression arg1 = new LiteralExpression(-1.0, context);
 Expression arg2 = new LiteralExpression(-1.0, context);
 CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);

 Boolean result = (Boolean) op.computeValue(context);
 assertFalse(result.booleanValue());
 }

 @Test
 public void testLessThanNaN() {
 InfoSet is = InfoSetUtil.createInfoSet(); // Empty InfoSet
 EvalContext context = new EvalContext(is);

 Expression arg1 = new LiteralExpression(Double.NaN, context);
 Expression arg2 = new LiteralExpression(1.0, context);
 CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);

 try {
 Boolean result = (Boolean) op.computeValue(context);
 fail("Expected IllegalArgumentException for NaN");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testLessThanNull() {
 InfoSet is = InfoSetUtil.createInfoSet(); // Empty InfoSet
 EvalContext context = new EvalContext(is);

 Expression arg1 = new LiteralExpression(1.0, context);
 Expression arg2 = new LiteralExpression(null, context);
 CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);

 try {
 op.computeValue(context);
 fail("Expected IllegalArgumentException for null");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}