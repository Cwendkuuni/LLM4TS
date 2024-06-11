import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExpressionTest {

 private Expression expression;

 @Test
 public void testIsContextDependent() {
 expression = mock(Expression.class);
 when(expression.computeContextDependent()).thenReturn(false);

 Assert.assertFalse(expression.isContextDependent());
 }

 @Test
 public void testIsContextDependentContextDependent() {
 expression = mock(Expression.class);
 when(expression.computeContextDependent()).thenReturn(true);

 Assert.assertTrue(expression.isContextDependent());
 }

 @Test
 public void testComputeValue() {
 expression = mock(Expression.class);
 EvalContext context = mock(EvalContext.class);
 Object result = new Object();
 when(expression.computeValue(context)).thenReturn(result);

 Assert.assertEquals(result, expression.computeValue(context));
 }

 @Test
 public void testComputeValueNodeSetFirstElement() {
 expression = mock(Expression.class);
 EvalContext context = mock(EvalContext.class);
 Object nodeSet = new Object[] {new NodePointer("testNode")};
 when(expression.computeValue(context)).thenReturn(nodeSet);

 Assert.assertEquals(nodeSet[0], expression.computeValue(context));
 }

 @Test
 public void testComputeValueNull() {
 expression = mock(Expression.class);
 EvalContext context = mock(EvalContext.class);
 when(expression.computeValue(context)).thenReturn(null);

 Assert.assertNull(expression.computeValue(context));
 }

 @Test
 public void testIterate() {
 expression = mock(Expression.class);
 EvalContext context = mock(EvalContext.class);
 Object result = new Object();
 when(expression.compute(context)).thenReturn(result);
 when(ValueUtils.iterate(result)).thenReturn(Collections.singletonList(result).iterator());

 Iterator<Object> iterator = expression.iterate(context);
 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals(result, iterator.next());
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testIterateEmpty() {
 expression = mock(Expression.class);
 EvalContext context = mock(EvalContext.class);
 when(expression.compute(context)).thenReturn(null);

 Iterator<Object> iterator = expression.iterate(context);
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratePointers() {
 expression = mock(Expression.class);
 EvalContext context = mock(EvalContext.class);
 Object pointer = mock(NodePointer.class);
 when(expression.compute(context)).thenReturn(pointer);
 when(pointer.getValue()).thenReturn("testValue");

 Iterator<Pointer> pointerIterator = expression.iteratePointers(context);
 Assert.assertTrue(pointerIterator.hasNext());
 Assert.assertEquals(pointer, pointerIterator.next());
 Assert.assertFalse(pointerIterator.hasNext());
 }

 @Test
 public void testIteratePointersEmpty() {
 expression = mock(Expression.class);
 EvalContext context = mock(EvalContext.class);
 when(expression.compute(context)).thenReturn(null);

 Iterator<Pointer> pointerIterator = expression.iteratePointers(context);
 Assert.assertFalse(pointerIterator.hasNext());
 }
}