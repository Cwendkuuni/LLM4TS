import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

 @Test
 public void testCoreOperationLessThan() {
 CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new ConstantExpression(1.0), new ConstantExpression(2.0));
 EvalContext context = new EvalContext();
 Object result = coreOperationLessThan.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);

 coreOperationLessThan = new CoreOperationLessThan(new ConstantExpression(1.0), new ConstantExpression(1.0));
 result = coreOperationLessThan.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);

 coreOperationLessThan = new CoreOperationLessThan(new ConstantExpression(1.0), new ConstantExpression(0.5));
 result = coreOperationLessThan.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCoreOperationLessThanNullArguments() {
 CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(null, new ConstantExpression(1.0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCoreOperationLessThanNullArg2() {
 CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new ConstantExpression(1.0), null);
 }

 @Test
 public void testCoreOperationLessThanSymbol() {
 CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new ConstantExpression(1.0), new ConstantExpression(2.0));
 assertEquals("<", coreOperationLessThan.getSymbol());
 }
}