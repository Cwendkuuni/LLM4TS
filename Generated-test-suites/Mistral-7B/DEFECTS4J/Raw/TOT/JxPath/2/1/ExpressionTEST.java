import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class ExpressionTest {

 @Test
 public void testIsContextDependent() {
 Expression expression = new ConstantExpression(1);
 assertTrue(expression.isContextDependent());
 expression = new VariableExpression("name");
 assertTrue(expression.isContextDependent());
 expression = new NumberAddExpression(new NumberExpression(1), new NumberExpression(2));
 assertFalse(expression.isContextDependent());
 }

 @Test
 public void testComputeValue() {
 Expression expression = new ConstantExpression("Hello World");
 EvalContext context = new EvalContext(new NodePointer("/", "document"));
 Object result = expression.computeValue(context);
 assertEquals("Hello World", result);
 expression = new VariableExpression("name");
 context.setVariable("name", new NodePointer("/", "person/@name"));
 result = expression.computeValue(context);
 assertEquals("/person[@name]", result.toString());
 expression = new NumberAddExpression(new NumberExpression(1), new NumberExpression(2));
 result = expression.computeValue(context);
 assertEquals(3, result.doubleValue());
 }

 @Test(expected = IllegalStateException.class)
 public void testComputeValueWithNullContext() {
 Expression expression = new VariableExpression("name");
 EvalContext context = null;
 expression.computeValue(context);
 }

 @Test
 public void testIterate() {
 Expression expression = new NumberAddExpression(new NumberExpression(1), new NumberExpression(2));
 EvalContext context = new EvalContext(new NodePointer("/", "document"));
 Iterator<Object> iterator = expression.iterate(context);
 Object result = iterator.next();
 Assert.assertEquals(3.0, result.doubleValue());
 Assert.assertNull(iterator.next());
 }

 @Test
 public void testIteratePointers() {
 Expression expression = new NumberAddExpression(new NumberExpression(1), new NumberExpression(2));
 EvalContext context = new EvalContext(new NodePointer("/", "document"));
 Iterator<Pointer> pointerIterator = expression.iteratePointers(context);
 Pointer pointer = pointerIterator.next();
 Assert.assertEquals("/document", pointer.getPath());
 Assert.assertEquals("/document", pointer.getRoot().getPath());
 Assert.assertEquals(1.0, pointer.getValue().doubleValue());
 pointer = pointerIterator.next();
 Assert.assertEquals("/document", pointer.getPath());
 Assert.assertEquals("/document", pointer.getRoot().getPath());
 Assert.assertEquals(2.0, pointer.getValue().doubleValue());
 Assert.assertNull(pointerIterator.hasNext());
 }

 @Test
 public void testCompute() {
 Expression expression = new NumberAddExpression(new NumberExpression(1), new NumberExpression(2));
 EvalContext context = new EvalContext(new NodePointer("/", "document"));
 Object result = expression.compute(context);
 assertEquals(3.0, result.doubleValue());
 }

 @Test
 public void testComputeWithNullContext() {
 Expression expression = new NumberAddExpression(new NumberExpression(1), new NumberExpression(2));
 EvalContext context = null;
 Object result = expression.compute(context);
 assertNull(result);
 }
}