import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testGreaterThanExpression() {
 CoreOperationRelationalExpression greaterThan = new CoreOperationRelationalExpression(new Expression[] { new ConstantExpression(1), new ConstantExpression(2) }) {
 @Override
 public String getOperatorSymbol() {
 return ">";
 }
 };
 assertTrue(greaterThan.evaluate(new Object[] { new Object[]{1, 2}, new Object[]{2, 1} }));
 assertFalse(greaterThan.evaluate(new Object[] { new Object[]{1, 1}, new Object[]{1, 2}, new Object[]{2, 1} }));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGreaterThanExpressionInvalidArgumentType() {
 CoreOperationRelationalExpression greaterThan = new CoreOperationRelationalExpression(new Expression[] { new CoreOperation(), new ConstantExpression(1) });
 greaterThan.evaluate(new Object[] { new Object[]{1, 2} });
 }

 @Test
 public void testGreaterThanOrEqualExpression() {
 CoreOperationRelationalExpression greaterThanOrEqual = new CoreOperationRelationalExpression(new Expression[] { new Expression[]{new ConstantExpression(1), new ConstantExpression(1)}, new Expression[]{new ConstantExpression(1), new ConstantExpression(2)}, new Expression[]{new ConstantExpression(2), new ConstantExpression(1)} }) {
 @Override
 public String getOperatorSymbol() {
 return ">=";
 }
 };
 assertTrue(greaterThanOrEqual.evaluate(new Object[] { new Object[]{1, 1}, new Object[]{1, 2}, new Object[]{2, 1}, new Object[]{2, 2} }));
 assertFalse(greaterThanOrEqual.evaluate(new Object[] { new Object[]{1, 1}, new Object[]{1, 1}, new Object[]{1, 2}, new Object[]{2, 1} }));
 }

 @Test
 public void testLessThanExpression() {
 CoreOperationRelationalExpression lessThan = new CoreOperationRelationalExpression(new Expression[] { new Expression[]{new ConstantExpression(1), new ConstantExpression(2)}, new Expression[]{new ConstantExpression(2), new ConstantExpression(1)} }) {
 @Override
 public String getOperatorSymbol() {
 return "<";
 }
 };
 assertFalse(lessThan.evaluate(new Object[] { new Object[]{1, 1}, new Object[]{1, 2}, new Object[]{2, 1}, new Object[]{2, 2} }));
 assertFalse(lessThan.evaluate(new Object[] { new Object[]{1, 2}, new Object[]{1, 1}, new Object[]{2, 1}, new Object[]{2, 2} }));
 assertTrue(lessThan.evaluate(new Object[] { new Object[]{1, 1}, new Object[]{1, 2}, new Object[]{2, 1} }));
 }

 @Test
 public void testLessThanOrEqualExpression() {
 CoreOperationRelationalExpression lessThanOrEqual = new CoreOperationRelationalExpression(new Expression[] { new Expression[]{new ConstantExpression(1), new ConstantExpression(1)}, new Expression[]{new ConstantExpression(1), new ConstantExpression(1)}, new Expression[]{new ConstantExpression(1), new ConstantExpression(2)}, new Expression[]{new ConstantExpression(2), new ConstantExpression(1)}, new Expression[]{new ConstantExpression(2), new ConstantExpression(2)} }) {
 @Override
 public String getOperatorSymbol() {
 return "<=";
 }
 };
 assertTrue(lessThanOrEqual.evaluate(new Object[] { new Object[]{1, 1}, new Object[]{1, 1}, new Object[]{1, 2}, new Object[]{2, 1}, new Object[]{2, 2}}));
 assertFalse(lessThanOrEqual.evaluate(new Object[] { new Object[]{1, 1}, new Object[]{1, 1}, new Object[]{1, 2}, new Object[]{2, 1}, new Object[]{2, 1}}));
 assertTrue(lessThanOrEqual.evaluate(new Object[] { new Object[]{1, 1}, new Object[]{1, 2}, new Object[]{2, 1}}));
 }

}