import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.junit.Test;
import static org.junit. Assert.assertEquals;

public class CoreOperationGreaterThanOrEqualTest {

 @Test
 public void testGreaterThanOrEqualWithBothNumbersGreaterOrEqual() {
 InfoSet is = createInfoSet(5.0, 6.0);
 CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
 new ConstantExpression(5.0), new ConstantExpression(6.0));

 EvalContext context = new EvalContext(is);
 Object result = expression.computeValue(context);

 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testGreaterThanOrEqualWithFirstNumberLessThanSecond() {
 InfoSet is = createInfoSet(4.5, 6.0);
 CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
 new ConstantExpression(4.5), new ConstantExpression(6.0));

 EvalContext context = new EvalContext(is);
 Object result = expression.computeValue(context);

 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testGreaterThanOrEqualWithBothNumbersEqual() {
 InfoSet is = createInfoSet(5.0, 5.0);
 CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
 new ConstantExpression(5.0), new ConstantExpression(5.0));

 EvalContext context = new EvalContext(is);
 Object result = expression.computeValue(context);

 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testGreaterThanOrEqualWithNull() {
 InfoSet is = createInfoSet(Double.NaN, 6.0);
 CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
 new ConstantExpression(Double.NaN), new ConstantExpression(6.0));

 EvalContext context = new EvalContext(is);
 Object result = expression.computeValue(context);

 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testGreaterThanOrEqualWithUndefined() {
 InfoSet is = createInfoSet(Double.NaN, Double.NaN);
 CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
 new ConstantExpression(Double.NaN), new ConstantExpression(Double.NaN));

 EvalContext context = new EvalContext(is);
 Object result = expression.computeValue(context);

 assertEquals(Boolean.FALSE, result);
 }

 @Test(expected = RuntimeException.class)
 public void testGreaterThanOrEqualWithInvalidNumberExpression() {
 CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
 new InvalidNumberExpression(), new ConstantExpression(6.0));
 }

 private InfoSet createInfoSet(double num1, double num2) {
 InfoSet is = new InfoSet();
 is.setItem(0, new JXPathContext(is, new Object[] { num1, num2 }));
 return is;
 }
}