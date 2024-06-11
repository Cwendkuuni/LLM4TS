import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

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
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues returnsEmptyValues;

 @Before
 public void setup() {
 returnsEmptyValues = new ReturnsEmptyValues();
 }

 @Test
 public void testAnswerToStringMethod() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(invocation.getMethod()).thenReturn(Mockito.method(String.class.getName(), new Class[0]));
 Object mock = mock(Object.class);

 String result = (String) returnsEmptyValues.answer(invocation);

 assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), result);
 }

 @Test
 public void testAnswerCompareToMethod() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(invocation.getMethod()).thenReturn(Mockito.method(Object.class.getName(), new Class[0]));
 Mockito.when(invocation.getArguments()).thenReturn(new Object[]{mock1});

 int result = (int) returnsEmptyValues.answer(invocation);

 assertNull(result);
 Mockito.verify(mock1).equals(mock2);
 result = (int) returnsEmptyValues.answer(invocation);
 assertEquals(1, result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAnswerCollectionMethodWhenModify() {
 Collection<Object> collection = (Collection<Object>) returnsEmptyValues.returnValueFor(Collection.class);
 collection.add(new Object());
 }

 @Test
 public void testAnswerSetMethods() {
 Set<Object> set = (Set<Object>) returnsEmptyValues.returnValueFor(Set.class);
 Set<Object> hashSet = (Set<Object>) returnsEmptyValues.returnValueFor(HashSet.class);
 Set<Object> linkedHashSet = (Set<Object>) returnsEmptyValues.returnValueFor(LinkedHashSet.class);
 SortedSet<Object> treeSet = (SortedSet<Object>) returnsEmptyValues.returnValueFor(TreeSet.class);
 SortedSet<Object> linkedTreeSet = (SortedSet<Object>) returnsEmptyValues.returnValueFor(LinkedTreeSet.class);

 assertEquals(0, set.size());
 assertEquals(0, hashSet.size());
 assertEquals(0, linkedHashSet.size());
 assertEquals(0, treeSet.size());
 assertEquals(0, linkedTreeSet.size());
 }

 @Test
 public void testAnswerListMethods() {
 List<Object> list = (List<Object>) returnsEmptyValues.returnValueFor(List.class);
 List<Object> linkedList = (List<Object>) returnsEmptyValues.returnValueFor(LinkedList.class);
 List<Object> arrayList = (List<Object>) returnsEmptyValues.returnValueFor(ArrayList.class);

 assertEquals(0, list.size());
 assertEquals(0, linkedList.size());
 assertEquals(0, arrayList.size());
 }

 @Test
 public void testAnswerMapMethods() {
 Map<Object, Object> map = (Map<Object, Object>) returnsEmptyValues.returnValueFor(Map.class);
 Map<Object, Object> hashMap = (Map<Object, Object>) returnsEmptyValues.returnValueFor(HashMap.class);
 Map<Object, Object> linkedHashMap = (Map<Object, Object>) returnsEmptyValues.returnValueFor(LinkedHashMap.class);
 SortedMap<Object, Object> treeMap = (SortedMap<Object, Object>) returnsEmptyValues.returnValueFor(TreeMap.class);
 SortedMap<Object, Object> linkedTreeMap = (SortedMap<Object, Object>) returnsEmptyValues.returnValueFor(LinkedTreeMap.class);

 assertEquals(0, map.size());
 assertEquals(0, hashMap.size());
 assertEquals(0, linkedHashMap.size());
 assertEquals(0, treeMap.size());
 assertEquals(0, linkedTreeMap.size());
 }
}