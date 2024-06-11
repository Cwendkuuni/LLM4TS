import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

 @Test
 public void testIsContextDependent() {
 Expression expression = new FixedExpression(1);
 assertTrue(expression.isContextDependent());

 expression = new ContextDependentExpression();
 assertTrue(expression.isContextDependent());
 }

 @Test
 public void testComputeValue() {
 Expression expression = new FixedExpression(1);
 EvalContext context = new EvalContext(new NodePointer("root", null, null));
 Object result = expression.computeValue(context);
 assertEquals(1, result.doubleValue());

 expression = new ContextDependentExpression();
 context = new EvalContext(new NodePointer("root", null, null));
 result = expression.computeValue(context);
 assertNotNull(result);
 }

 @Test
 public void testCompute() {
 Expression expression = new FixedExpression(1);
 EvalContext context = new EvalContext(new NodePointer("root", null, null));
 Object result = expression.compute(context);
 assertEquals(1, result.doubleValue());

 expression = new ContextDependentExpression();
 context = new EvalContext(new NodePointer("root", null, null));
 result = expression.compute(context);
 assertNotNull(result);
 }

 @Test
 public void testIterate() {
 Expression expression = new FixedExpression(new NodePointer("node1", null, null), new NodePointer("node2", null, null));
 EvalContext context = new EvalContext(new NodePointer("root", null, null));
 Iterator iterator = expression.iterate(context);
 assertTrue(iterator.hasNext());
 Pointer node1 = (Pointer) iterator.next();
 assertEquals("node1", node1.getName().getLocalPart());
 assertTrue(iterator.hasNext());
 Pointer node2 = (Pointer) iterator.next();
 assertEquals("node2", node2.getName().getLocalPart());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratePointers() {
 Expression expression = new FixedExpression(new NodePointer("node1", null, null), new NodePointer("node2", null, null));
 EvalContext context = new EvalContext(new NodePointer("root", null, null));
 Iterator iterator = expression.iteratePointers(context);
 assertTrue(iterator.hasNext());
 Pointer node1 = (Pointer) iterator.next();
 assertEquals("node1", node1.getName().getLocalPart());
 assertTrue(iterator.hasNext());
 Pointer node2 = (Pointer) iterator.next();
 assertEquals("node2", node2.getName().getLocalPart());
 assertFalse(iterator.hasNext());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIterateRemove() {
 Expression expression = new FixedExpression(new NodePointer("node1", null, null), new NodePointer("node2", null, null));
 EvalContext context = new EvalContext(new NodePointer("root", null, null));
 Iterator iterator = expression.iterate(context);
 iterator.remove();
 }

 private static class FixedExpression extends Expression {
 private Object value;
 private Pointer[] pointers;

 public FixedExpression(Object value) {
 this(value, (Pointer[]) null);
 }

 public FixedExpression(Pointer... pointers) {
 this(null, pointers);
 }

 public FixedExpression(Object value, Pointer... pointers) {
 super();
 this.value = value;
 this.pointers = pointers;
 }

 @Override
 public boolean computeContextDependent() {
 return false;
 }

 @Override
 public Object computeValue(EvalContext context) {
 return value;
 }

 @Override
 public Object compute(EvalContext context) {
 return value;
 }

 @Override
 public Iterator iterate(EvalContext context) {
 if (pointers != null) {
 return new ValueIterator(Arrays.asList(pointers).iterator());
 }
 return new ValueIterator(Collections.singletonList(new NodePointer("value", null, null)).iterator());
 }

 @Override
 public Iterator iteratePointers(EvalContext context) {
 return new PointerIterator(Arrays.asList(pointers).iterator(), new QName(null, "value"), context.getRootContext().getCurrentNodePointer().getLocale());
 }
 }

 private static class ContextDependentExpression extends Expression {
 @Override
 public boolean computeContextDependent() {
 return true;
 }

 @Override
 public Object computeValue(EvalContext context) {
 return context.getRootContext().getCurrentNodePointer();
 }

 @Override
 public Object compute(EvalContext context) {
 return context.getRootContext().getCurrentNodePointer();
 }
 }
}