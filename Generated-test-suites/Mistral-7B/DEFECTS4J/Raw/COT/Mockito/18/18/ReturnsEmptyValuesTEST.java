import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

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
import static org.mockito.Mockito.verify;

public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues returnsEmptyValues;

 @Before
 public void setUp() {
 returnsEmptyValues = new ReturnsEmptyValues();
 }

 @Test
 public void testToString() {
 InvocationOnMock invocation = Mockito.mock(Object.class);
 String result = (String) returnsEmptyValues.answer(invocation);
 assertEquals("Mock for [class org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues@" + System.identityHashCode(returnsEmptyValues) + "], hashCode: " + returnsEmptyValues.hashCode(), result);
 }

 @Test
 public void testCompareTo() {
 Object mock = Mockito.mock(Comparable.class);
 Object arg = Mockito.mock(Comparable.class);
 Mockito.when(mock.compareTo(arg)).then(returnsEmptyValues);
 int result = (int) Mockito.invokeMethod(mock, "compareTo", arg);
 assertEquals(1, result); // since mocks by default should return 0 if references are the same, otherwise some other value
 }

 @Test
 public void testEmptyCollection() {
 InvocationOnMock invocation = Mockito.mock(Collection.class);
 Collection<Object> result = (Collection<Object>) returnsEmptyValues.answer(invocation);
 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test
 public void testEmptySet() {
 InvocationOnMock invocation = Mockito.mock(Set.class);
 Set<Object> result = (Set<Object>) returnsEmptyValues.answer(invocation);
 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test
 public void testEmptyHashSet() {
 InvocationOnMock invocation = Mockito.mock(HashSet.class);
 Set<Object> result = (Set<Object>) returnsEmptyValues.answer(invocation);
 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test
 public void testEmptySortedSet() {
 InvocationOnMock invocation = Mockito.mock(SortedSet.class);
 SortedSet<Object> result = (SortedSet<Object>) returnsEmptyValues.answer(invocation);
 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test
 public void testEmptyTreeSet() {
 InvocationOnMock invocation = Mockito.mock(TreeSet.class);
 SortedSet<Object> result = (SortedSet<Object>) returnsEmptyValues.answer(invocation);
 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test
 public void testEmptyLinkedHashSet() {
 InvocationOnMock invocation = Mockito.mock(LinkedHashSet.class);
 Set<Object> result = (Set<Object>) returnsEmptyValues.answer(invocation);
 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test
 public void testEmptyList() {
 InvocationOnMock invocation = Mockito.mock(List.class);
 List<Object> result = (List<Object>) returnsEmptyValues.answer(invocation);
 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test
 public void testEmptyLinkedList() {
 InvocationOnMock invocation = Mockito.mock(LinkedList.class);
 List<Object> result = (List<Object>) returnsEmptyValues.answer(invocation);
 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test
 public void testEmptyArrayList() {
 InvocationOnMock invocation = Mockito.mock(ArrayList.class);
 List<Object> result = (List<Object>) returnsEmptyValues.answer(invocation);
 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test
 public void testEmptyMap() {
 InvocationOnMock invocation = Mockito.mock(Map.class);
 Map<Object, Object> result = (Map<Object, Object>) returnsEmptyValues.answer(invocation);
 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test
 public void testEmptyHashMap() {
 InvocationOnMock invocation = Mockito.mock(HashMap.class);
 Map<Object, Object> result = (Map<Object, Object>) returnsEmptyValues.answer(invocation);
 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test
 public void testEmptySortedMap() {
 InvocationOnMock invocation = Mockito.mock(SortedMap.class);
 SortedMap<Object, Object> result = (SortedMap<Object, Object>) returnsEmptyValues.answer(invocation);
 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test
 public void testEmptyTreeMap() {
 InvocationOnMock invocation = Mockito.mock(TreeMap.class);
 SortedMap<Object, Object> result = (SortedMap<Object, Object>) returnsEmptyValues.answer(invocation);
 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test
 public void testEmptyLinkedHashMap() {
 InvocationOnMock invocation = Mockito.mock(LinkedHashMap.class);
 Map<Object, Object> result = (Map<Object, Object>) returnsEmptyValues.answer(invocation);
 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test(expected = NullPointerException.class)
 public void testNullReturnType() {
 returnsEmptyValues.answer(Mockito.mock(Object.class));
 }
}