import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Parser;
import org.apache.commons.jxpath.ri.expressions.Expression;
import org.apache.commons.jxpath.ri.functions.FunctionCallExpression;
import org.apache.commons.jxpath.ri.functions.Functions;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationGreaterThanOrEqualTest {

 @Test
 public void testGreaterThanOrEqual() {
 Expression arg1 = Parser.parseExpression("1.5", null);
 Expression arg2 = Parser.parseExpression("1.5", null);
 CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);
 Object result = op.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);

 arg1 = Parser.parseExpression("1.5", null);
 arg2 = Parser.parseExpression("2.0", null);
 op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 result = op.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);

 arg1 = Parser.parseExpression("2.0", null);
 arg2 = Parser.parseExpression("1.5", null);
 op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 result = op.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArguments() {
 Expression arg1 = Parser.parseExpression("1.5", null);
 Expression arg2 = null;
 CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArguments2() {
 Expression arg1 = null;
 Expression arg2 = Parser.parseExpression("1.5", null);
 CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 }

 @Test
 public void testFunctionCall() {
 FunctionCallExpression func = new FunctionCallExpression(Functions.GT,
 new Expression[] { Parser.parseExpression("1.5", null), Parser.parseExpression("1.5", null) });
 CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(func, Parser.parseExpression("1.5", null));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);
 Object result = op.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);

 func = new FunctionCallExpression(Functions.GT,
 new Expression[] { Parser.parseExpression("2.0", null), Parser.parseExpression("1.5", null) });
 op = new CoreOperationGreaterThanOrEqual(Parser.parseExpression("1.5", null), func);
 result = op.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }
}