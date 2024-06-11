import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

 @Test
 public void testCoreOperationGreaterThanOrEqual() {
 CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(createExpression(5.0), createExpression(5.0));
 EvalContext context = new EvalContext();
 assertTrue(coreOperationGreaterThanOrEqual.computeValue(context).booleanValue());

 coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(createExpression(5.0), createExpression(6.0));
 assertTrue(coreOperationGreaterThanOrEqual.computeValue(context).booleanValue());

 coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(createExpression(5.0), createExpression(4.0));
 assertTrue(coreOperationGreaterThanOrEqual.computeValue(context).booleanValue());

 coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(createExpression(4.0), createExpression(5.0));
 assertFalse(coreOperationGreaterThanOrEqual.computeValue(context).booleanValue());
 }

 @Test
 public void testGetSymbol() {
 CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(createExpression(5.0), createExpression(6.0));
 assertEquals(">=", coreOperationGreaterThanOrEqual.getSymbol());
 }

 private Expression createExpression(double value) {
 return new ConstantExpression(value);
 }
}