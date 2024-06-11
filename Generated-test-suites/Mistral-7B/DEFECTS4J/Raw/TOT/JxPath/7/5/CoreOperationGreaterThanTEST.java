import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.expressions.Expression;
import org.apache.commons.jxpath.ri.expressions.CoreOperationGreaterThan;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationGreaterThanTest {

 @Test
 public void testGreaterThan() {
 Expression arg1 = new LiteralExpression(new Double(5.0));
 Expression arg2 = new LiteralExpression(new Double(3.0));
 CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);

 EvaluationContext context = new EvaluationContext(new InfoSet(), new NodeInfo());
 Object result = operation.computeValue(context);

 assertTrue("Expected true for 5 > 3", result instanceof Boolean && ((Boolean) result).booleanValue());
 }

 @Test
 public void testLessThanOrEqual() {
 Expression arg1 = new LiteralExpression(new Double(5.0));
 Expression arg2 = new LiteralExpression(new Double(5.0));
 CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);

 EvaluationContext context = new EvaluationContext(new InfoSet(), new NodeInfo());
 Object result = operation.computeValue(context);

 assertFalse("Expected false for 5 <= 5", result instanceof Boolean && !((Boolean) result).booleanValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testinvalidType() {
 Expression arg1 = new LiteralExpression(new Integer(5));
 Expression arg2 = new LiteralExpression(new Double(3.0));
 CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);

 EvaluationContext context = new EvaluationContext(new InfoSet(), new NodeInfo());
 try {
 operation.computeValue(context);
 } catch (Exception e) {
 // Expected
 }

 assertTrue("Expected an IllegalArgumentException", e instanceof IllegalArgumentException);
 }
}