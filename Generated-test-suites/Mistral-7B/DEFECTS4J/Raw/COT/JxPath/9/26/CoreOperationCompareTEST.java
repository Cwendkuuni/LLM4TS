import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.Pointer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CoreOperationCompareTest {

 private CoreOperationCompare coreOperationCompare;
 private Expression arg1;
 private Expression arg2;
 private EvalContext context;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 arg1 = mock(Expression.class);
 arg2 = mock(Expression.class);
 context = mock(EvalContext.class);
 coreOperationCompare = new CoreOperationCompare(arg1, arg2);
 }

 @Test
 public void testGetPrecedence() {
 assertTrue(coreOperationCompare.getPrecedence() == 2);
 }

 @Test
 public void testIsSymmetric() {
 assertTrue(coreOperationCompare.isSymmetric());
 }

 @Test
 public void testEqualWithNullArguments() {
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualWithSameInstance() {
 Object object = new Object();
 when(arg1.compute(context)).thenReturn(object);
 when(arg2.compute(context)).thenReturn(object);
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualWithBooleanValues() {
 when(arg1.compute(context)).thenReturn(Boolean.TRUE);
 when(arg2.compute(context)).thenReturn(Boolean.TRUE);
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));

 when(arg1.compute(context)).thenReturn(Boolean.FALSE);
 when(arg2.compute(context)).thenReturn(Boolean.FALSE);
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));

 when(arg1.compute(context)).thenReturn(Boolean.TRUE);
 when(arg2.compute(context)).thenReturn(Boolean.FALSE);
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualWithDoubles() {
 when(arg1.compute(context)).thenReturn(1.0);
 when(arg2.compute(context)).thenReturn(1.0);
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));

 when(arg1.compute(context)).thenReturn(1.0);
 when(arg2.compute(context)).thenReturn(2.0);
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));

 when(arg1.compute(context)).thenReturn(Double.NaN);
 when(arg2.compute(context)).thenReturn(Double.NaN);
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualWithStrings() {
 when(arg1.compute(context)).thenReturn("test1");
 when(arg2.compute(context)).thenReturn("test1");
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));

 when(arg1.compute(context)).thenReturn("test1");
 when(arg2.compute(context)).thenReturn("test2");
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualWithIterators() {
 List<Object> list1 = new LinkedList<>();
 list1.add("test1");
 list1.add("test2");
 when(arg1.compute(context)).thenReturn(list1.iterator());
 List<Object> list2 = new LinkedList<>();
 list2.add("test1");
 list2.add("test2");
 when(arg2.compute(context)).thenReturn(list2.iterator());
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));

 List<Object> list1_1 = new LinkedList<>();
 list1_1.add("test1");
 when(arg1.compute(context)).thenReturn(list1_1.iterator());
 List<Object> list2_1 = new LinkedList<>();
 when(arg2.compute(context)).thenReturn(Collections.singletonIterator("test3"));
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testContainsWithNullCollection() {
 assertFalse(coreOperationCompare.contains(ArgumentMatchers.any(Iterator.class), null));
 }

 @Test
 public void testFindMatchWithCollections() {
 List<Object> collection1 = new LinkedList<>();
 collection1.add("test1");
 collection1.add("test2");
 when(arg1.compute(context)).thenReturn(collection1.iterator());
 List<Object> collection2 = new LinkedList<>();
 collection2.add("test1");
 collection2.add("test3");
 when(arg2.compute(context)).thenReturn(collection2.iterator());
 assertTrue(coreOperationCompare.findMatch((Iterator) arg1.compute(context), (Iterator) arg2.compute(context)));

 List<Object> collection1_1 = new LinkedList<>();
 collection1_1.add("test1");
 when(arg1.compute(context)).thenReturn(collection1_1.iterator());
 List<Object> collection2_1 = new LinkedList<>();
 when(arg2.compute(context)).thenReturn(Collections.singletonIterator("test4"));
 assertFalse(coreOperationCompare.findMatch((Iterator) arg1.compute(context), (Iterator) arg2.compute(context)));
 }

}