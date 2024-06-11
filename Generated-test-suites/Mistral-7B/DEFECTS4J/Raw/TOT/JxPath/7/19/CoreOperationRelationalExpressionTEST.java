import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationRelationalExpressionFactory;
import org.apache.commons.jxpath.ri.model.JXPathNodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testGreaterThan() {
 CoreOperationRelationalExpressionFactory factory = new CoreOperationRelationalExpressionFactory();
 CoreOperationRelationalExpression expr = (CoreOperationRelationalExpression) factory.createGreaterThanOperation();

 JXPathContext ctx = new JXPathContext(new JXPathNodes());
 Object result = expr.interpret(ctx, new Object[]{new Integer(5), new Integer(3)});
 assertTrue(result instanceof Boolean);
 assertEquals(true, result);
 }

 @Test
 public void testGreaterThan_False() {
 CoreOperationRelationalExpressionFactory factory = new CoreOperationRelationalExpressionFactory();
 CoreOperationRelationalExpression expr = (CoreOperationRelationalExpression) factory.createGreaterThanOperation();

 JXPathContext ctx = new JXPathContext(new JXPathNodes());
 Object result = expr.interpret(ctx, new Object[]{new Integer(3), new Integer(5)});
 assertTrue(result instanceof Boolean);
 assertEquals(false, result);
 }

 @Test
 public void testGreaterThanOrEqual() {
 CoreOperationRelationalExpressionFactory factory = new CoreOperationRelationalExpressionFactory();
 CoreOperationRelationalExpression expr = (CoreOperationRelationalExpression) factory.createGreaterThanOrEqualOperation();

 JXPathContext ctx = new JXPathContext(new JXPathNodes());
 Object result = expr.interpret(ctx, new Object[]{new Integer(5), new Integer(5)});
 assertTrue(result instanceof Boolean);
 assertEquals(true, result);
 }

 @Test
 public void testGreaterThanOrEqual_True() {
 CoreOperationRelationalExpressionFactory factory = new CoreOperationRelationalExpressionFactory();
 CoreOperationRelationalExpression expr = (CoreOperationRelationalExpression) factory.createGreaterThanOrEqualOperation();

 JXPathContext ctx = new JXPathContext(new JXPathNodes());
 Object result = expr.interpret(ctx, new Object[]{new Integer(5), new Integer(5)});
 assertTrue(result instanceof Boolean);
 assertEquals(true, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLessThan() {
 CoreOperationRelationalExpressionFactory factory = new CoreOperationRelationalExpressionFactory();
 CoreOperationRelationalExpression expr = (CoreOperationRelationalExpression) factory.createLessThanOperation();

 JXPathContext ctx = new JXPathContext(new JXPathNodes());
 Object result = expr.interpret(ctx, new Object[]{new Integer(3), new Integer(5)});
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLessThan_False() {
 CoreOperationRelationalExpressionFactory factory = new CoreOperationRelationalExpressionFactory();
 CoreOperationRelationalExpression expr = (CoreOperationRelationalExpression) factory.createLessThanOperation();

 JXPathContext ctx = new JXPathContext(new JXPathNodes());
 Object result = expr.interpret(ctx, new Object[]{new Integer(5), new Integer(3)});
 fail("Should throw an IllegalArgumentException");
 }

 @Test
 public void testLessThanOrEqual() {
 CoreOperationRelationalExpressionFactory factory = new CoreOperationRelationalExpressionFactory();
 CoreOperationRelationalExpression expr = (CoreOperationRelationalExpression) factory.createLessThanOrEqualOperation();

 JXPathContext ctx = new JXPathContext(new JXPathNodes());
 Object result = expr.interpret(ctx, new Object[]{new Integer(5), new Integer(5)});
 assertTrue(result instanceof Boolean);
 assertEquals(true, result);
 }

 @Test
 public void testLessThanOrEqual_True() {
 CoreOperationRelationalExpressionFactory factory = new CoreOperationRelationalExpressionFactory();
 CoreOperationRelationalExpression expr = (CoreOperationRelationalExpression) factory.createLessThanOrEqualOperation();

 JXPathContext ctx = new JXPathContext(new JXPathNodes());
 Object result = expr.interpret(ctx, new Object[]{new Integer(5), new Integer(5)});
 assertTrue(result instanceof Boolean);
 assertEquals(true, result);
 }

 @Test
 public void testLessThanOrEqual_False() {
 CoreOperationRelationalExpressionFactory factory = new CoreOperationRelationalExpressionFactory();
 CoreOperationRelationalExpression expr = (CoreOperationRelationalExpression) factory.createLessThanOrEqualOperation();

 JXPathContext ctx = new JXPathContext(new JXPathNodes());
 Object result = expr.interpret(ctx, new Object[]{new Integer(5), new Integer(6)});
 assertTrue(result instanceof Boolean);
 assertEquals(false, result);
 }
}