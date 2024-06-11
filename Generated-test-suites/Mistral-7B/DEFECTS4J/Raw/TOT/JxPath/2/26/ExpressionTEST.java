import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

 @Test
 public void testContextIndependentExpression() {
 Expression expression = new ConstantExpression(1);
 EvalContext context = new JXPathContext(new Object());
 assertTrue(expression.isContextDependent());
 assertEquals(1, expression.computeValue(context).doubleValue());
 }

 @Test
 public void testContextDependentExpression() {
 Expression expression = new VariableExpression();
 EvalContext context1 = new JXPathContext(new Object());
 EvalContext context2 = new JXPathContext(new Object() {
 @Override
 public Object getVariableValue(String name) {
 return "test";
 }
 });
 assertTrue(expression.isContextDependent());
 assertEquals("test", expression.computeValue(context2));
 assertEquals(null, expression.computeValue(context1));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIterateThrowsException() {
 Expression expression = new ConstantExpression(1);
 Iterator iterator = expression.iterate(new EvalContext(new Object()));
 iterator.next();
 iterator.remove();
 }

 @Test
 public void testIterateReturnsEmptyIterator() {
 Expression expression = new EmptyExpression();
 Iterator iterator = expression.iterate(new EvalContext(new Object()));
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIterateReturnsSingleValueIterator() {
 Expression expression = new ConstantExpression(1);
 Iterator iterator = expression.iterate(new EvalContext(new Object()));
 assertTrue(iterator.hasNext());
 assertEquals(1, iterator.next().doubleValue());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIterateReturnsNodeIterator() {
 Expression expression = new NodeSetExpression(new NodePointer[]{new NodePointer("root", null)});
 Iterator iterator = expression.iterate(new EvalContext(new Object()));
 assertTrue(iterator.hasNext());
 NodePointer node = (NodePointer) iterator.next();
 assertEquals("root", node.getLocalName());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIteratePointersThrowsException() {
 Expression expression = new ConstantExpression(1);
 expression.iteratePointers(new EvalContext(new Object()));
 }
}