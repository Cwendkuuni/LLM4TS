import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testGreaterThan() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new ConstantExpression(5), new ConstantExpression(3));
 JXPathContext context = new JXPathContext(new Object[] { new Integer(10) });
 assertTrue(expression.evaluate(context));

 context = new JXPathContext(new Object[] { new Integer(3) });
 assertFalse(expression.evaluate(context));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGreaterThanInvalidArguments() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperationNotExpression(), new CoreOperationNotExpression());
 }

 @Test
 public void testGreaterThanOrEqual() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new ConstantExpression(5), new ConstantExpression(5));
 JXPathContext context = new JXPathContext(new Object[] { new Integer(5) });
 assertTrue(expression.evaluate(context));

 context = new JXPathContext(new Object[] { new Integer(3) });
 assertTrue(expression.evaluate(context));
 }

 @Test
 public void testLessThan() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new ConstantExpression(3), new ConstantExpression(5));
 JXPathContext context = new JXPathContext(new Object[] { new Integer(5) });
 assertFalse(expression.evaluate(context));

 context = new JXPathContext(new Object[] { new Integer(3) });
 assertTrue(expression.evaluate(context));
 }

 @Test
 public void testLessThanOrEqual() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new ConstantExpression(5), new ConstantExpression(5));
 JXPathContext context = new JXPathContext(new Object[] { new Integer(5) });
 assertTrue(expression.evaluate(context));

 context = new JXPathContext(new Object[] { new Integer(6) });
 assertFalse(expression.evaluate(context));
 }
}