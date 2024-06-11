import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.Pointer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CoreOperationCompareTest {

 private Expression arg1;
 private Expression arg2;
 private CoreOperationCompare coreOperationCompare;
 private EvalContext context;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 arg1 = Mockito.mock(Expression.class);
 arg2 = Mockito.mock(Expression.class);
 context = Mockito.mock(EvalContext.class);
 coreOperationCompare = new CoreOperationCompare(arg1, arg2);
 }

 @Test
 public void testEqual_sameObjects() {
 Object l = new Object();
 Object r = l;
 Mockito.when(arg1.compute(context)).thenReturn(l);
 Mockito.when(arg2.compute(context)).thenReturn(r);
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_differentObjectsWithSameValue() {
 Object l = new Object();
 Object r = new Object();
 Mockito.when(arg1.compute(context)).thenReturn(l);
 Mockito.when(arg2.compute(context)).thenReturn(r);
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_differentObjectsWithDifferentValues() {
 Object l = new Object();
 Object r = new Object();
 Mockito.when(arg1.compute(context)).thenReturn(l);
 Mockito.when(arg2.compute(context)).thenReturn(r);
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_oneNull() {
 Object l = null;
 Object r = new Object();
 Mockito.when(arg1.compute(context)).thenReturn(l);
 Mockito.when(arg2.compute(context)).thenReturn(r);
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_bothNull() {
 Object l = null;
 Object r = null;
 Mockito.when(arg1.compute(context)).thenReturn(l);
 Mockito.when(arg2.compute(context)).thenReturn(r);
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_iterator_sameElements() {
 Object[] elements = new Object[]{new Integer(1), new Integer(2), new Integer(3)};
 Mockito.when(arg1.compute(context)).thenReturn(elements);
 Mockito.when(arg2.compute(context)).thenReturn(elements);
 Iterator<Object> it1 = Mockito.mock(Iterator.class);
 Iterator<Object> it2 = Mockito.mock(Iterator.class);
 Mockito.when(it1.hasNext()).thenReturn(true);
 Mockito.when(it1.next()).thenReturn(elements[0]);
 Mockito.when(it2.hasNext()).thenReturn(true);
 Mockito.when(it2.next()).thenReturn(elements[0]);
 Mockito.when(arg1.getIterator()).thenReturn(it1);
 Mockito.when(arg2.getIterator()).thenReturn(it2);
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_iterator_differentElements() {
 Object[] elements1 = new Object[]{new Integer(1), new Integer(2), new Integer(3)};
 Object[] elements2 = new Object[]{new Integer(1), new Integer(4), new Integer(3)};
 Mockito.when(arg1.compute(context)).thenReturn(elements1);
 Mockito.when(arg2.compute(context)).thenReturn(elements2);
 Iterator<Object> it1 = Mockito.mock(Iterator.class);
 Iterator<Object> it2 = Mockito.mock(Iterator.class);
 Mockito.when(it1.hasNext()).thenReturn(true);
 Mockito.when(it1.next()).thenReturn(elements1[0]);
 Mockito.when(it2.hasNext()).thenReturn(true);
 Mockito.when(it2.next()).thenReturn(elements2[0]);
 Mockito.when(arg1.getIterator()).thenReturn(it1);
 Mockito.when(arg2.getIterator()).thenReturn(it2);
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_iterator_sameIterator() {
 Object[] elements = new Object[]{new Integer(1), new Integer(2), new Integer(3)};
 Mockito.when(arg1.compute(context)).thenReturn(elements);
 Mockito.when(arg2.compute(context)).thenReturn(elements);
 Iterator<Object> it = Mockito.mock(Iterator.class);
 Mockito.when(it.hasNext()).thenReturn(true);
 Mockito.when(it.next()).thenReturn(elements[0]);
 Mockito.when(arg1.getIterator()).thenReturn(it);
 Mockito.when(arg2.getIterator()).thenReturn(it);
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_pointer_sameValue() {
 Object value = new Object();
 Pointer l = Mockito.mock(Pointer.class);
 Mockito.when(l.getValue()).thenReturn(value);
 Mockito.when(arg1.compute(context)).thenReturn(l);
 Mockito.when(arg2.compute(context)).thenReturn(l);
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_pointer_differentValues() {
 Object value1 = new Object();
 Object value2 = new Object();
 Pointer l1 = Mockito.mock(Pointer.class);
 Mockito.when(l1.getValue()).thenReturn(value1);
 Pointer l2 = Mockito.mock(Pointer.class);
 Mockito.when(l2.getValue()).thenReturn(value2);
 Mockito.when(arg1.compute(context)).thenReturn(l1);
 Mockito.when(arg2.compute(context)).thenReturn(l2);
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_pointer_samePointer() {
 Object value = new Object();
 Pointer l = Mockito.mock(Pointer.class);
 Mockito.when(l.getValue()).thenReturn(value);
 Mockito.when(arg1.compute(context)).thenReturn(l);
 Mockito.when(arg2.compute(context)).thenReturn(l);
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testIsSymmetric() {
 assertTrue(coreOperationCompare.isSymmetric());
 }

 @Test
 public void testGetPrecedence() {
 assertEquals(2, coreOperationCompare.getPrecedence());
 }
}