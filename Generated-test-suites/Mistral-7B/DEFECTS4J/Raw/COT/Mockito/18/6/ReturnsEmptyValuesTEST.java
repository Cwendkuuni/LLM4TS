import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;

public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues answer = new ReturnsEmptyValues();

 @Test
 public void testToStringMethod() {
 MockitoMock mock = mock(Object.class);
 InvocationOnMock invocation = new InvocationOnMock(mock, MockitoMock.class.getMethod("toString"), new Object[0]);
 String result = (String) answer.answer(invocation);
 assertEquals("Mock for Object, hashCode: " + mock.hashCode(), result);
 }

 @Test
 public void testCompareToMethod() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 InvocationOnMock invocation1 = new InvocationOnMock(mock1, Object.class.getMethod("compareTo", Object.class), new Object[]{mock2});
 InvocationOnMock invocation2 = new InvocationOnMock(mock2, Object.class.getMethod("compareTo", Object.class), new Object[]{mock1});
 int result1 = (Integer) answer.answer(invocation1);
 int result2 = (Integer) answer.answer(invocation2);
 assertEquals(1, result1);
 assertEquals(0, result2);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testListModificationException() {
 List<Object> list = answer.returnValueFor(List.class);
 list.add(new Object());
 }

 @Test
 public void testCollectionEmptyValues() {
 Collection<Object> collection = answer.returnValueFor(Collection.class);
 assertEquals(true, collection.isEmpty());
 }

 @Test
 public void testSetEmptyValues() {
 Set<Object> set = answer.returnValueFor(Set.class);
 Set<Object> hashSet = answer.returnValueFor(HashSet.class);
 Set<Object> linkedHashSet = answer.returnValueFor(LinkedHashSet.class);
 assertEquals(true, set.isEmpty());
 assertEquals(true, hashSet.isEmpty());
 assertEquals(true, linkedHashSet.isEmpty());
 }

 @Test
 public void testMapEmptyValues() {
 Map<Object, Object> map = answer.returnValueFor(Map.class);
 Map<Object, Object> hashMap = answer.returnValueFor(HashMap.class);
 Map<Object, Object> linkedHashMap = answer.returnValueFor(LinkedHashMap.class);
 Map<Object, Object> treeMap = answer.returnValueFor(TreeMap.class);
 Map<Object, Object> linkedTreeMap = answer.returnValueFor(LinkedTreeMap.class);
 assertEquals(true, map.isEmpty());
 assertEquals(true, hashMap.isEmpty());
 assertEquals(true, linkedHashMap.isEmpty());
 assertEquals(true, treeMap.isEmpty());
 assertEquals(true, linkedTreeMap.isEmpty());
 }

 @Test
 public void testSortedSetEmptyValues() {
 SortedSet<Object> sortedSet = answer.returnValueFor(SortedSet.class);
 SortedSet<Object> treeSet = answer.returnValueFor(TreeSet.class);
 SortedSet<Object> linkedTreeSet = answer.returnValueFor(LinkedTreeSet.class);
 assertEquals(true, sortedSet.isEmpty());
 assertEquals(true, treeSet.isEmpty());
 assertEquals(true, linkedTreeSet.isEmpty());
 }

 @Test
 public void testSortedMapEmptyValues() {
 SortedMap<Object, Object> sortedMap = answer.returnValueFor(SortedMap.class);
 SortedMap<Object, Object> treeMap = answer.returnValueFor(TreeMap.class);
 SortedMap<Object, Object> linkedTreeMap = answer.returnValueFor(LinkedTreeMap.class);
 assertEquals(true, sortedMap.isEmpty());
 assertEquals(true, treeMap.isEmpty());
 assertEquals(true, linkedTreeMap.isEmpty());
 }

 @Test
 public void testArrayListEmptyValues() {
 List<Object> list = answer.returnValueFor(ArrayList.class);
 ArrayList<Object> linkedList = answer.returnValueFor(LinkedList.class);
 assertEquals(true, list.isEmpty());
 assertEquals(true, linkedList.isEmpty());
 }

 @Test
 public void testLinkedListEmptyValues() {
 List<Object> list = answer.returnValueFor(LinkedList.class);
 assertEquals(true, list.isEmpty());
 }

 @Test
 public void testArrayListModificationException() {
 List<Object> list = answer.returnValueFor(ArrayList.class);
 list.add(new Object());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testHashSetModificationException() {
 Set<Object> set = answer.returnValueFor(HashSet.class);
 set.add(new Object());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testLinkedHashSetModificationException() {
 Set<Object> set = answer.returnValueFor(LinkedHashSet.class);
 set.add(new Object());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testLinkedTreeSetModificationException() {
 SortedSet<Object> set = answer.returnValueFor(LinkedTreeSet.class);
 set.add(new Object());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testHashMapModificationException() {
 Map<Object, Object> map = answer.returnValueFor(HashMap.class);
 map.put(new Object(), new Object());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testLinkedHashMapModificationException() {
 Map<Object, Object> map = answer.returnValueFor(LinkedHashMap.class);
 map.put(new Object(), new Object());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testTreeMapModificationException() {
 SortedMap<Object, Object> map = answer.returnValueFor(TreeMap.class);
 map.put(new Object(), new Object());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testLinkedTreeMapModificationException() {
 SortedMap<Object, Object> map = answer.returnValueFor(LinkedTreeMap.class);
 map.put(new Object(), new Object());
 }
}