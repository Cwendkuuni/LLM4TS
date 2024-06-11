import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues returnsEmptyValues;
 private Object mock;

 @Before
 public void setup() {
 mock = mock(Object.class);
 returnsEmptyValues = new ReturnsEmptyValues();
 }

 @Test
 public void testToStringMethod() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethod("toString", (Class[]) null));
 Mockito.when(invocation.getMock()).thenReturn(mock);
 String result = (String) returnsEmptyValues.answer(invocation);
 Mockito.verify(invocation, Mockito.times(1)).getMock();
 assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), result);
 }

 @Test
 public void testCompareToMethod() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(invocation.getMethod()).thenReturn(Mockito.mock(Comparable.class.getDeclaredMethod("compareTo", Object.class)));
 Mockito.when(invocation.getMock()).thenReturn(mock);
 int result = (int) returnsEmptyValues.answer(invocation);
 Mockito.verify(invocation, Mockito.times(1)).getMock();
 assertEquals(1, result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCollectionMethodsWithModifiableCollection() {
 Collection<String> collection = Mockito.mock(ArrayList.class);
 Mockito.when(collect.add(ArgumentMatchers.anyString())).thenReturn(true);
 when(returnsEmptyValues.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenReturn(collection);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(invocation.getMethod()).thenReturn(ArrayList.class.getDeclaredMethod("add", String.class));
 invocation.proceed();
 collection.add("Test");
 }

 @Test
 public void testEmptyList() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(invocation.getMethod()).thenReturn(List.class.getDeclaredMethod("isEmpty", (Class[]) null));
 Mockito.when(invocation.getMock()).thenReturn(returnsEmptyValues);
 boolean result = (boolean) returnsEmptyValues.answer(invocation);
 Mockito.verify(invocation, Mockito.times(1)).getMock();
 assertEquals(true, result);
 }

 @Test
 public void testEmptySet() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(invocation.getMethod()).thenReturn(Set.class.getDeclaredMethod("isEmpty", (Class[]) null));
 Mockito.when(invocation.getMock()).thenReturn(returnsEmptyValues);
 boolean result = (boolean) returnsEmptyValues.answer(invocation);
 Mockito.verify(invocation, Mockito.times(1)).getMock();
 assertEquals(true, result);
 }

 @Test
 public void testEmptyHashSet() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(invocation.getMethod()).thenReturn(HashSet.class.getDeclaredMethod("isEmpty", (Class[]) null));
 Mockito.when(invocation.getMock()).thenReturn(returnsEmptyValues);
 boolean result = (boolean) returnsEmptyValues.answer(invocation);
 Mockito.verify(invocation, Mockito.times(1)).getMock();
 assertEquals(true, result);
 }

 @Test
 public void testEmptyTreeSet() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(invocation.getMethod()).thenReturn(TreeSet.class.getDeclaredMethod("isEmpty", (Class[]) null));
 Mockito.when(invocation.getMock()).thenReturn(returnsEmptyValues);
 boolean result = (boolean) returnsEmptyValues.answer(invocation);
 Mockito.verify(invocation, Mockito.times(1)).getMock();
 assertEquals(true, result);
 }

 @Test
 public void testEmptyArrayList() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(invocation.getMethod()).thenReturn(ArrayList.class.getDeclaredMethod("isEmpty", (Class[]) null));
 Mockito.when(invocation.getMock()).thenReturn(returnsEmptyValues);
 boolean result = (boolean) returnsEmptyValues.answer(invocation);
 Mockito.verify(invocation, Mockito.times(1)).getMock();
 assertEquals(true, result);
 }

 @Test
 public void testEmptyLinkedList() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(invocation.getMethod()).thenReturn(LinkedList.class.getDeclaredMethod("isEmpty", (Class[]) null));
 Mockito.when(invocation.getMock()).thenReturn(returnsEmptyValues);
 boolean result = (boolean) returnsEmptyValues.answer(invocation);
 Mockito.verify(invocation, Mockito.times(1)).getMock();
 assertEquals(true, result);
 }

 @Test
 public void testEmptyHashMap() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(invocation.getMethod()).thenReturn(HashMap.class.getDeclaredMethod("isEmpty", (Class[]) null));
 Mockito.when(invocation.getMock()).thenReturn(returnsEmptyValues);
 boolean result = (boolean) returnsEmptyValues.answer(invocation);
 Mockito.verify(invocation, Mockito.times(1)).getMock();
 assertEquals(true, result);
 }

 @Test
 public void testEmptyLinkedHashMap() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(invocation.getMethod()).thenReturn(LinkedHashMap.class.getDeclaredMethod("isEmpty", (Class[]) null));
 Mockito.when(invocation.getMock()).thenReturn(returnsEmptyValues);
 boolean result = (boolean) returnsEmptyValues.answer(invocation);
 Mockito.verify(invocation, Mockito.times(1)).getMock();
 assertEquals(true, result);
 }

 @Test
 public void testEmptyTreeMap() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(invocation.getMethod()).thenReturn(TreeMap.class.getDeclaredMethod("isEmpty", (Class[]) null));
 Mockito.when(invocation.getMock()).thenReturn(returnsEmptyValues);
 boolean result = (boolean) returnsEmptyValues.answer(invocation);
 Mockito.verify(invocation, Mockito.times(1)).getMock();
 assertEquals(true, result);
 }
}