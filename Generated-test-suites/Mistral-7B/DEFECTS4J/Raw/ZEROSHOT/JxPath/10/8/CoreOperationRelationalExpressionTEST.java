import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.util.IteratorUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testComputeValueGreater() {
 CoreOperationRelationalExpression expression = new CoreOperationGreater(new CoreConstantExpression(new Integer(5)), new CoreConstantExpression(new Integer(3)));
 EvalContext context = new EvalContext(new InitialContext(new Object[]{new Object(){}}));
 Object result = expression.computeValue(context);
 assertTrue(result.equals(Boolean.TRUE));
 }

 @Test
 public void testComputeValueLess() {
 CoreOperationRelationalExpression expression = new CoreOperationLess(new CoreConstantExpression(new Integer(3)), new CoreConstantExpression(new Integer(5)));
 EvalContext context = new EvalContext(new InitialContext(new Object[]{new Object(){}}));
 Object result = expression.computeValue(context);
 assertTrue(result.equals(Boolean.FALSE));
 }

 @Test
 public void testComputeValueGreaterEqual() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterOrEqual(new CoreConstantExpression(new Integer(5)), new CoreConstantExpression(new Integer(5)));
 EvalContext context = new EvalContext(new InitialContext(new Object[]{new Object(){}}));
 Object result = expression.computeValue(context);
 assertTrue(result.equals(Boolean.TRUE));
 }

 @Test
 public void testComputeValueLessEqual() {
 CoreOperationRelationalExpression expression = new CoreOperationLessOrEqual(new CoreConstantExpression(new Integer(3)), new CoreConstantExpression(new Integer(3)));
 EvalContext context = new EvalContext(new InitialContext(new Object[]{new Object(){}}));
 Object result = expression.computeValue(context);
 assertTrue(result.equals(Boolean.TRUE));
 }

 @Test
 public void testComputeValueIterators() {
 CoreOperationRelationalExpression expression = new CoreOperationGreater(new CoreVariableExpression("var1"), new CoreVariableExpression("var2"));
 EvalContext context = new EvalContext(new InitialContext(new Object[]{new Object[]{new Object[]{1, 2, 3, 4, 5}, new Object[]{2, 3, 4, 5, 6}}}));
 context.setVariable("var1", IteratorUtil.createIterator(context, 0));
 context.setVariable("var2", IteratorUtil.createIterator(context, 1));
 Object result = expression.computeValue(context);
 assertFalse(result.equals(Boolean.TRUE));
 context.setVariable("var1", IteratorUtil.createIterator(context, 2));
 Object result1 = expression.computeValue(context);
 assertTrue(result1.equals(Boolean.TRUE));
 }

 @Test
 public void testComputeValueDouble() {
 CoreOperationRelationalExpression expression = new CoreOperationGreater(new CoreConstantExpression(new Double(5.5)), new CoreConstantExpression(new Double(3.5)));
 EvalContext context = new EvalContext(new InitialContext(new Object[]{new Object()}));
 Object result = expression.computeValue(context);
 assertTrue(result.equals(Boolean.TRUE));
 }

 @Test
 public void testComputeValueNaN() {
 CoreOperationRelationalExpression expression = new CoreOperationGreater(new CoreConstantExpression(new Double.NaN()), new CoreConstantExpression(new Double(3.5)));
 EvalContext context = new EvalContext(new InitialContext(new Object[]{new Object()}));
 Object result = expression.computeValue(context);
 assertFalse(result.equals(Boolean.TRUE));
 }

 @Test
 public void testComputeValueDoubleEqual() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterOrEqual(new CoreConstantExpression(new Double(5.5)), new CoreConstantExpression(new Double(5.5)));
 EvalContext context = new EvalContext(new InitialContext(new Object[]{new Object()}));
 Object result = expression.computeValue(context);
 assertTrue(result.equals(Boolean.TRUE));
 }

 @Test
 public void testGetPrecedence() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterOrEqual(new CoreConstantExpression(new Integer(5)), new CoreConstantExpression(new Integer(3)));
 int precedence = expression.getPrecedence();
 assertEquals(3, precedence);
 }

 @Test
 public void testIsSymmetric() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterOrEqual(new CoreConstantExpression(new Integer(5)), new CoreConstantExpression(new Integer(3)));
 boolean symmetric = expression.isSymmetric();
 assertFalse(symmetric);
 }
}