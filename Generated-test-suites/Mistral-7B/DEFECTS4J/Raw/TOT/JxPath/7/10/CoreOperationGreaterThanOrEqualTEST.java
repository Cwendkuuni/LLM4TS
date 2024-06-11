import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

 @Test
 public void testGreaterThanOrEqual() {
 InfoSet infoSet = new InfoSet();
 EvaluationContext ctx = new EvaluationContext();
 ctx.setCurrentInfoSet(infoSet);

 Expression arg1 = new LiteralExpression(new Double(5.0), null);
 Expression arg2 = new LiteralExpression(new Double(5.0), null);

 CoreOperationGreaterThanOrEqual expr = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 Object result = expr.computeValue(ctx);

 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testGreaterThanButNotEqual() {
 InfoSet infoSet = new InfoSet();
 EvaluationContext ctx = new EvaluationContext();
 ctx.setCurrentInfoSet(infoSet);

 Expression arg1 = new LiteralExpression(new Double(5.0), null);
 Expression arg2 = new LiteralExpression(new Double(3.0), null);

 CoreOperationGreaterThanOrEqual expr = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 Object result = expr.computeValue(ctx);

 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testLessThanButNotEqualAndGreaterThanOrEqual() {
 InfoSet infoSet = new InfoSet();
 EvaluationContext ctx = new EvaluationContext();
 ctx.setCurrentInfoSet(infoSet);

 Expression arg1 = new LiteralExpression(new Double(-1.0), null);
 Expression arg2 = new LiteralExpression(new Double(5.0), null);

 CoreOperationGreaterThanOrEqual expr = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 Object result = expr.computeValue(ctx);

 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);

 Expression arg3 = new LiteralExpression(new Double(5.0), null);
 Expression arg4 = new LiteralExpression(new Double(5.0), null);

 expr = new CoreOperationGreaterThanOrEqual(arg3, arg4);
 result = expr.computeValue(ctx);

 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test(expected = ClassCastException.class)
 public void testNonNumericLeftOperand() {
 InfoSet infoSet = new InfoSet();
 EvaluationContext ctx = new EvaluationContext();
 ctx.setCurrentInfoSet(infoSet);

 Expression arg1 = new StringLiteralExpression("test", null);
 Expression arg2 = new LiteralExpression(new Double(5.0), null);

 CoreOperationGreaterThanOrEqual expr = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 expr.computeValue(ctx);
 }

 @Test(expected = ClassCastException.class)
 public void testNonNumericRightOperand() {
 InfoSet infoSet = new InfoSet();
 EvaluationContext ctx = new EvaluationContext();
 ctx.setCurrentInfoSet(infoSet);

 Expression arg1 = new LiteralExpression(new Double(5.0), null);
 Expression arg2 = new StringLiteralExpression("test", null);

 CoreOperationGreaterThanOrEqual expr = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 expr.computeValue(ctx);
 }
}