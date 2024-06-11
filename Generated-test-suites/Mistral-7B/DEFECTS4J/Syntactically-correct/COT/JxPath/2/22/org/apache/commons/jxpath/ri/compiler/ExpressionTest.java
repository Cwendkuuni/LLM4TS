import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ExpressionTest {

 private Expression expression;
 private EvalContext context;

 @Before
 public void setup() {
 expression = new Expression() {
 @Override
 public boolean computeContextDependent() {
 return false;
 }

 @Override
 public Object computeValue(EvalContext context) {
 return 42;
 }

 @Override
 public Object compute(EvalContext context) {
 return computeValue(context);
 }
 };
 context = new EvalContext(null, null, null, null, null, null, null, null, null, null, null);
 }

 @Test
 public void testIsContextDependent() {
 assertFalse(expression.isContextDependent());
 }

 @Test
 public void testComputeValue() {
 Object result = expression.computeValue(context);
 assertEquals(42, result);
 }

 @Test
 public void testCompute() {
 Object result = expression.compute(context);
 assertEquals(42, result);
 }

 @Test
 public void testIterate() {
 Iterator iterator = expression.iterate(context);
 assertTrue(iterator.hasNext());
 Object value = iterator.next();
 assertEquals(42, value);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratePointers() {
 Iterator iterator = expression.iteratePointers(context);
 assertNull(iterator.next());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveFromIterator() {
 Iterator iterator = expression.iteratePointers(context);
 iterator.next();
 iterator.remove();
 }

 @Test
 public void testNewNodePointer() {
 Object value = new Object();
 Pointer pointer = new NodePointer(new QName(null, "test"), value, Locale.ENGLISH);
 PointerIterator iterator = new PointerIterator(ValueUtils.iterate(pointer), new QName(null, "test"), Locale.ENGLISH);
 assertTrue(iterator.hasNext());
 Object next = iterator.next();
 assertSame(pointer, next);
 }

 @Test
 public void testValueIterator() {
 Object value = new Object();
 Iterator<Object> iterator = ValueUtils.iterate(value);
 ValueIterator valueIterator = new ValueIterator(iterator);
 assertTrue(valueIterator.hasNext());
 Object next = valueIterator.next();
 assertSame(value, next);
 }
}