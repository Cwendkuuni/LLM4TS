import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testGreaterThan() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Constant(3), new Constant(2));
 JXPathContext context = new JXPathContext();
 context.setValues("a[1]", 2, new Object[] { new Integer(3), new Integer(2) });
 boolean result = (boolean) expression.evaluate(context);
 assertTrue(result);
 }

 @Test
 public void testGreaterThanWithInvalidInput() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Constant("a"), new Constant(3));
 JXPathContext context = new JXPathContext();
 boolean result = (boolean) expression.evaluate(context);
 assertFalse(result);
 }

 @Test
 public void testGreaterThanOrEqual() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Constant(3), new Constant(3));
 JXPathContext context = new JXPathContext();
 context.setValues("a[1]", 2, new Object[] { new Integer(3), new Integer(3) });
 boolean result = (boolean) expression.evaluate(context);
 assertTrue(result);
 }

 @Test
 public void testGreaterThanOrEqualWithInvalidInput() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Constant("a"), new Constant(2));
 JXPathContext context = new JXPathContext();
 boolean result = (boolean) expression.evaluate(context);
 assertFalse(result);
 }

 @Test
 public void testLessThan() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Constant(2), new Constant(3));
 JXPathContext context = new JXPathContext();
 context.setValues("a[1]", 2, new Object[] { new Integer(2), new Integer(3) });
 boolean result = (boolean) expression.evaluate(context);
 assertFalse(result);
 }

 @Test
 public void testLessThanWithInvalidInput() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Constant("a"), new Constant(2));
 JXPathContext context = new JXPathContext();
 boolean result = (boolean) expression.evaluate(context);
 assertFalse(result);
 }

 @Test
 public void testLessThanOrEqual() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Constant(3), new Constant(3));
 JXPathContext context = new JXPathContext();
 context.setValues("a[1]", 2, new Object[] { new Integer(3), new Integer(3) });
 boolean result = (boolean) expression.evaluate(context);
 assertTrue(result);
 }

 @Test
 public void testLessThanOrEqualWithInvalidInput() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Constant(2), new Constant("a"));
 JXPathContext context = new JXPathContext();
 boolean result = (boolean) expression.evaluate(context);
 assertFalse(result);
 }

}