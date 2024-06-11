import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.Pointer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

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
 public void setUp() {
 arg1 = Mockito.mock(Expression.class);
 arg2 = Mockito.mock(Expression.class);
 context = Mockito.spy(EvalContext.class);
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
 public void testEqual_whenBothAreInitialContext_thenReturnFalse() {
 Mockito.when(arg1.getClass()).thenReturn(InitialContext.class);
 Mockito.when(arg2.getClass()).thenReturn(InitialContext.class);
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_whenBothAreSelfContext_thenReturnFalse() {
 Mockito.when(arg1.getClass()).thenReturn(SelfContext.class);
 Mockito.when(arg2.getClass()).thenReturn(SelfContext.class);
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_whenBothAreCollections_thenReturnTrueIfContainSameElements() {
 Object collection1 = new Object[] { 1, 2, 3 };
 Object collection2 = new Object[] { 1, 2, 3 };
 Mockito.when(arg1.compute(context)).thenReturn(collection1);
 Mockito.when(arg2.compute(context)).thenReturn(collection2);
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_whenBothAreCollections_thenReturnFalseIfNoMatchingElements() {
 Object collection1 = new Object[] { 1, 2, 3 };
 Object collection2 = new Object[] { 4, 5, 6 };
 Mockito.when(arg1.compute(context)).thenReturn(collection1);
 Mockito.when(arg2.compute(context)).thenReturn(collection2);
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_whenBothAreIterators_thenReturnTrueIfBothIteratorsHaveSameElements() {
 Iterator iterator1 = Mockito.mock(Iterator.class);
 Iterator iterator2 = Mockito.mock(Iterator.class);
 Mockito.when(arg1.compute(context)).thenReturn(iterator1);
 Mockito.when(arg2.compute(context)).thenReturn(iterator2);
 Mockito.when(iterator1.hasNext()).thenReturn(true).thenReturn(true).thenReturn(true);
 Mockito.when(iterator2.hasNext()).thenReturn(true).thenReturn(true).thenReturn(true);
 Mockito.when(iterator1.next()).thenReturn(1).thenReturn(2).thenReturn(3);
 Mockito.when(iterator2.next()).thenReturn(1).thenReturn(2).thenReturn(3);
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_whenBothAreIterators_thenReturnFalseIfNoMatchingElements() {
 Iterator iterator1 = Mockito.mock(Iterator.class);
 Iterator iterator2 = Mockito.mock(Iterator.class);
 Mockito.when(arg1.compute(context)).thenReturn(iterator1);
 Mockito.when(arg2.compute(context)).thenReturn(iterator2);
 Mockito.when(iterator1.hasNext()).thenReturn(true).thenReturn(true).thenReturn(true);
 Mockito.when(iterator2.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_whenBothAreObjects_thenReturnTrueIfObjectsAreEqual() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 Mockito.when(arg1.compute(context)).thenReturn(obj1);
 Mockito.when(arg2.compute(context)).thenReturn(obj2);
 Mockito.when(obj1.equals(obj2)).thenReturn(true);
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_whenBothAreObjects_thenReturnFalseIfObjectsAreNotEqual() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 Mockito.when(arg1.compute(context)).thenReturn(obj1);
 Mockito.when(arg2.compute(context)).thenReturn(obj2);
 Mockito.when(obj1.equals(obj2)).thenReturn(false);
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_whenOneIsPointerAndOtherIsObject_thenReturnFalse() {
 Object obj = new Object();
 Pointer pointer = Mockito.mock(Pointer.class);
 Mockito.when(arg1.compute(context)).thenReturn(pointer);
 Mockito.when(arg2.compute(context)).thenReturn(obj);
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_whenOneIsPointerAndOtherIsPointerWithDifferentValues_thenReturnFalse() {
 Pointer pointer1 = Mockito.mock(Pointer.class);
 Pointer pointer2 = Mockito.mock(Pointer.class);
 Object value1 = new Object();
 Object value2 = new Object();
 Mockito.when(arg1.compute(context)).thenReturn(pointer1);
 Mockito.when(arg2.compute(context)).thenReturn(pointer2);
 Mockito.when(pointer1.getValue()).thenReturn(value1);
 Mockito.when(pointer2.getValue()).thenReturn(value2);
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_whenOneIsPointerAndOtherIsPointerWithSameValues_thenReturnTrue() {
 Pointer pointer1 = Mockito.mock(Pointer.class);
 Pointer pointer2 = Mockito.mock(Pointer.class);
 Object value = new Object();
 Mockito.when(arg1.compute(context)).thenReturn(pointer1);
 Mockito.when(arg2.compute(context)).thenReturn(pointer2);
 Mockito.when(pointer1.getValue()).thenReturn(value);
 Mockito.when(pointer2.getValue()).thenReturn(value);
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_whenOneIsBooleanAndOtherIsBoolean_thenReturnTrueIfBothAreEqual() {
 Mockito.when(arg1.getClass()).thenReturn(Boolean.class);
 Mockito.when(arg2.getClass()).thenReturn(Boolean.class);
 Mockito.when(arg1.compute(context)).thenReturn(Boolean.TRUE);
 Mockito.when(arg2.compute(context)).thenReturn(Boolean.TRUE);
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_whenOneIsBooleanAndOtherIsBoolean_thenReturnFalseIfNotEqual() {
 Mockito.when(arg1.getClass()).thenReturn(Boolean.class);
 Mockito.when(arg2.getClass()).thenReturn(Boolean.class);
 Mockito.when(arg1.compute(context)).thenReturn(Boolean.TRUE);
 Mockito.when(arg2.compute(context)).thenReturn(Boolean.FALSE);
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_whenOneIsNumberAndOtherIsNumber_thenReturnTrueIfBothAreEqual() {
 Double number1 = 1.0;
 Double number2 = 1.0;
 Mockito.when(arg1.getClass()).thenReturn(Double.class);
 Mockito.when(arg2.getClass()).thenReturn(Double.class);
 Mockito.when(arg1.compute(context)).thenReturn(number1);
 Mockito.when(arg2.compute(context)).thenReturn(number2);
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_whenOneIsNumberAndOtherIsNumber_thenReturnFalseIfNotEqual() {
 Double number1 = 1.0;
 Double number2 = 2.0;
 Mockito.when(arg1.getClass()).thenReturn(Double.class);
 Mockito.when(arg2.getClass()).thenReturn(Double.class);
 Mockito.when(arg1.compute(context)).thenReturn(number1);
 Mockito.when(arg2.compute(context)).thenReturn(number2);
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_whenOneIsStringAndOtherIsString_thenReturnTrueIfBothAreEqual() {
 String string1 = "test";
 String string2 = "test";
 Mockito.when(arg1.getClass()).thenReturn(String.class);
 Mockito.when(arg2.getClass()).thenReturn(String.class);
 Mockito.when(arg1.compute(context)).thenReturn(string1);
 Mockito.when(arg2.compute(context)).thenReturn(string2);
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_whenOneIsStringAndOtherIsString_thenReturnFalseIfNotEqual() {
 String string1 = "test";
 String string2 = "test2";
 Mockito.when(arg1.getClass()).thenReturn(String.class);
 Mockito.when(arg2.getClass()).thenReturn(String.class);
 Mockito.when(arg1.compute(context)).thenReturn(string1);
 Mockito.when(arg2.compute(context)).thenReturn(string2);
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }
}