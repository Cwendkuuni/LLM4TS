package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

/**
 * Comprehensive JUnit 4 test file for the ReturnsEmptyValues class.
 */
public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues returnsEmptyValues;

 @Before
 public void setUp() {
 returnsEmptyValues = new ReturnsEmptyValues();
 }

 @Test
 public void testAnswer_toStringMethod() {
 // Given
 Object mock = Mockito.mock(Object.class);
 // When
 String result = (String) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getName().equals("toString")));
 // Then
 assertEquals("Mock for [mockito.mock.Mock$_mock0_State: mock], hashCode: 1532787547", result);
 }

 @Test
 public void testAnswer_compareToMethod() {
 // Given
 Object obj1 = Mockito.mock(Comparable.class);
 Object obj2 = Mockito.mock(Comparable.class);
 // When
 int result = (int) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getName().equals("compareTo")));
 // Then
 assertNotEquals(0, result);
 }

 @Test
 public void testReturnValueFor_CollectionType() {
 // Given
 Class<Collection<Integer>> collectionType = Collection.class;
 // When
 Collection<Integer> result = (Collection<Integer>) returnsEmptyValues.returnValueFor(collectionType);
 // Then
 assertTrue(result instanceof LinkedList);
 assertFalse(result.isEmpty());
 }

 @Test
 public void testReturnValueFor_SetType() {
 // Given
 Class<Set<Integer>> setType = Set.class;
 // When
 Set<Integer> result = (Set<Integer>) returnsEmptyValues.returnValueFor(setType);
 // Then
 assertTrue(result instanceof HashSet);
 assertFalse(result.isEmpty());
 }

 @Test
 public void testReturnValueFor_HashSetType() {
 // Given
 Class<HashSet<Integer>> hashSetType = HashSet.class;
 // When
 Set<Integer> result = (Set<Integer>) returnsEmptyValues.returnValueFor(hashSetType);
 // Then
 assertTrue(result instanceof HashSet);
 assertFalse(result.isEmpty());
 }

 @Test
 public void testReturnValueFor_SortedSetType() {
 // Given
 Class<SortedSet<Integer>> sortedSetType = SortedSet.class;
 // When
 Set<Integer> result = (Set<Integer>) returnsEmptyValues.returnValueFor(sortedSetType);
 // Then
 assertTrue(result instanceof TreeSet);
 assertFalse(result.isEmpty());
 }

 @Test
 public void testReturnValueFor_TreeSetType() {
 // Given
 Class<TreeSet<Integer>> treeSetType = TreeSet.class;
 // When
 Set<Integer> result = (Set<Integer>) returnsEmptyValues.returnValueFor(treeSetType);
 // Then
 assertTrue(result instanceof TreeSet);
 assertFalse(result.isEmpty());
 }

 @Test
 public void testReturnValueFor_LinkedHashSetType() {
 // Given
 Class<LinkedHashSet<Integer>> linkedHashSetType = LinkedHashSet.class;
 // When
 Set<Integer> result = (Set<Integer>) returnsEmptyValues.returnValueFor(linkedHashSetType);
 // Then
 assertTrue(result instanceof LinkedHashSet);
 assertFalse(result.isEmpty());
 }

 @Test
 public void testReturnValueFor_ListType() {
 // Given
 Class<List<Integer>> listType = List.class;
 // When
 List<Integer> result = (List<Integer>) returnsEmptyValues.returnValueFor(listType);
 // Then
 assertTrue(result instanceof LinkedList);
 assertFalse(result.isEmpty());
 }

 @Test
 public void testReturnValueFor_LinkedListType() {
 // Given
 Class<LinkedList<Integer>> linkedListType = LinkedList.class;
 // When
 List<Integer> result = (List<Integer>) returnsEmptyValues.returnValueFor(linkedListType);
 // Then
 assertTrue(result instanceof LinkedList);
 assertFalse(result.isEmpty());
 }

 @Test
 public void testReturnValueFor_ArrayListType() {
 // Given
 Class<ArrayList<Integer>> arrayListType = ArrayList.class;
 // When
 List<Integer> result = (List<Integer>) returnsEmptyValues.returnValueFor(arrayListType);
 // Then
 assertTrue(result instanceof ArrayList);
 assertFalse(result.isEmpty());
 }

 @Test
 public void testReturnValueFor_MapType() {
 // Given
 Class<Map<Integer,String>> mapType = Map.class;
 // When
 Map<Integer,String> result = (Map<Integer,String>) returnsEmptyValues.returnValueFor(mapType);
 // Then
 assertTrue(result instanceof HashMap);
 assertFalse(result.isEmpty());
 }

 @Test
 public void testReturnValueFor_HashMapType() {
 // Given
 Class<HashMap<Integer,String>> hashMapType = HashMap.class;
 // When
 Map<Integer,String> result = (Map<Integer,String>) returnsEmptyValues.returnValueFor(hashMapType);
 // Then
 assertTrue(result instanceof HashMap);
 assertFalse(result.isEmpty());
 }

 @Test
 public void testReturnValueFor_SortedMapType() {
 // Given
 Class<SortedMap<Integer,String>> sortedMapType = SortedMap.class;
 // When
 Map<Integer,String> result = (Map<Integer,String>) returnsEmptyValues.returnValueFor(sortedMapType);
 // Then
 assertTrue(result instanceof TreeMap);
 assertFalse(result.isEmpty());
 }

 @Test
 public void testReturnValueFor_TreeMapType() {
 // Given
 Class<TreeMap<Integer,String>> treeMapType = TreeMap.class;
 // When
 Map<Integer,String> result = (Map<Integer,String>) returnsEmptyValues.returnValueFor(treeMapType);
 // Then
 assertTrue(result instanceof TreeMap);
 assertFalse(result.isEmpty());
 }

 @Test
 public void testReturnValueFor_LinkedHashMapType() {
 // Given
 Class<LinkedHashMap<Integer,String>> linkedHashMapType = LinkedHashMap.class;
 // When
 Map<Integer,String> result = (Map<Integer,String>) returnsEmptyValues.returnValueFor(linkedHashMapType);
 // Then
 assertTrue(result instanceof LinkedHashMap);
 assertFalse(result.isEmpty());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testReturnValueFor_UnsupportedType() {
 // Given
 Class<String> stringType = String.class;
 // When
 String result = (String) returnsEmptyValues.returnValueFor(stringType);
 // Then
 fail("Returned an unexpected value: " + result);
 }
}