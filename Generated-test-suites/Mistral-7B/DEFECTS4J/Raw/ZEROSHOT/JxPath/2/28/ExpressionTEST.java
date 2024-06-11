import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

 @Test
 public void testContextDependency() {
 Expression expression1 = new ConstantExpression(1);
 Expression expression2 = new VariableExpression("$x");

 assertTrue(expression1.isContextDependent());
 assertFalse(expression2.isContextDependent());
 }

 @Test
 public void testComputeValue() {
 Expression contextDependentExpression = new AndExpression(
 new NumberExpression(1),
 new NumberExpression(2)
 );

 EvalContext context = new EvalContext(new NodePointer("/root", null), null);
 Object result = contextDependentExpression.computeValue(context);

 assertNotNull(result);
 assertFalse(result instanceof NodePointer);

 context = new EvalContext(new NodePointer("/root/child1", null), null);
 result = contextDependentExpression.computeValue(context);

 assertNotNull(result);
 assertFalse(result instanceof NodePointer);

 assertNotEquals(result, contextDependentExpression.computeValue(context));
 }

 @Test
 public void testCompute() {
 Expression contextDependentExpression = new AndExpression(
 new NumberExpression(1),
 new NumberExpression(2)
 );

 EvalContext context = new EvalContext(new NodePointer("/root", null), null);
 Object result = contextDependentExpression.compute(context);

 assertNotNull(result);
 assertFalse(result instanceof NodePointer);

 context = new EvalContext(new NodePointer("/root/child1", null), null);
 result = contextDependentExpression.compute(context);

 assertNotNull(result);
 assertFalse(result instanceof NodePointer);

 assertNotEquals(result, contextDependentExpression.compute(context));
 }

 @Test
 public void testIterate() {
 Expression expression = new ConstantExpression(Collections.singletonList("foo"));
 EvalContext context = new EvalContext(new NodePointer("/root", null), null);

 Iterator iterator = expression.iterate(context);
 assertTrue(iterator.hasNext());
 assertEquals("foo", iterator.next().getValue());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratePointers() {
 Expression expression = new ConstantExpression(Collections.singletonList(new NodePointer("/root/child1", null)));
 EvalContext context = new EvalContext(new NodePointer("/root", null), null);

 Iterator iterator = expression.iteratePointers(context);
 assertTrue(iterator.hasNext());
 assertTrue(iterator.next() instanceof NodePointer);
 assertFalse(iterator.hasNext());
 }
}