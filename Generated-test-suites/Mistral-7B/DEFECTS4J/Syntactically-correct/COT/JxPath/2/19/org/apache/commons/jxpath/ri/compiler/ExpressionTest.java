import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

 private Expression expression;

 @Test
 public void testIsContextDependent() {
 expression = new Expression() {
 @Override
 public boolean computeContextDependent() {
 return true;
 }
 };
 assertTrue(expression.isContextDependent());

 expression = new Expression() {
 @Override
 public boolean computeContextDependent() {
 return false;
 }
 };
 assertFalse(expression.isContextDependent());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIterateWithNullContext() {
 expression = new Expression() {
 @Override
 public Object compute(EvalContext context) {
 return null;
 }
 };
 Iterator iterator = expression.iterate(null);
 iterator.next();
 }

 @Test
 public void testIterate() {
 expression = new Expression() {
 @Override
 public Object computeValue(EvalContext context) {
 return new Object[] {new NodePointer("test", 1), new NodePointer("test", 2)};
 }

 @Override
 public Iterator iterate(EvalContext context) {
 return new ValueIterator(new Object[] {new NodePointer("test", 1), new NodePointer("test", 2)}.iterator());
 }
 };
 Iterator iterator = expression.iterate(new EvalContext());
 assertTrue(iterator.hasNext());
 NodePointer nodePointer = (NodePointer) iterator.next();
 assertEquals("test", nodePointer.getNodeName());
 assertEquals(1, nodePointer.getNodeNumber());
 }

 @Test
 public void testIteratePointers() {
 expression = new Expression() {
 @Override
 public Object computeValue(EvalContext context) {
 return new Object[] {new NodePointer("test1", 1), new NodePointer("test2", 2), new NodePointer("test3", 3)};
 }

 @Override
 public Iterator iteratePointers(EvalContext context) {
 return new PointerIterator(ValueUtils.iterate(new Object[] {new NodePointer("test1", 1), new NodePointer("test2", 2), new NodePointer("test3", 3)}).iterator(),
 new QName(null, "value"), new EvalContext().getRootContext().getCurrentNodePointer().getLocale());
 }
 };
 List<NodePointer> pointers = Lists.newArrayList();
 Iterator iterator = expression.iteratePointers(new EvalContext());
 while (iterator.hasNext()) {
 pointers.add((NodePointer) iterator.next());
 }
 assertEquals(3, pointers.size());
 }

 @Test
 public void testComputeValue() {
 expression = new Expression() {
 @Override
 public Object computeValue(EvalContext context) {
 return "test";
 }
 };
 assertEquals("test", expression.computeValue(new EvalContext()));
 }

 @Test
 public void testCompute() {
 expression = new Expression() {
 @Override
 public Object compute(EvalContext context) {
 return "test";
 }
 };
 assertEquals("test", expression.compute(new EvalContext()));
 }
}