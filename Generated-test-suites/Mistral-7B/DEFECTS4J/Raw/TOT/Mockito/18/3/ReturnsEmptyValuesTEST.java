package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import static org.mockito.Mockito.*; // Import static methods from Mockito

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

import static org.mockito.Mockito.verify; // Verify method calls

public class ReturnsEmptyValuesTest {

 // Test for toString() method
 @Test
 public void testToString() {
 Mock<MyClass> mock = mock(MyClass.class);

 // Call toString method using the mock
 String result = mock.answer(new InvocationOnMock(mock, Mockito.method(MyClass.class, "toString"))).toString();

 // Verify that the toString method was called once
 verify(mock, times(1)).answer(ArgumentMatchers.isA(InvocationOnMock.class));

 // Verify that the returned string matches the expected format
 assertEquals("Mock for MyClass, hashCode: <some_hash_code>", result);
 }

 // Test for Comparable#compareTo(T other) method
 @Test
 public void testCompareTo() {
 MyClass mock1 = mock(MyClass.class);
 MyClass mock2 = mock(MyClass.class);

 // Call compareTo method with the same mock instance
 int result1 = mock1.compareTo(mock1);

 // Call compareTo method with different mock instances
 int result2 = mock1.compareTo(mock2);

 // Verify that compareTo methods were called twice
 verify(mock1, times(1)).compareTo(mock1);
 verify(mock1, times(1)).compareTo(mock2);

 // Verify that compareTo method returns 0 for the same instance and non-zero value for different instances
 assertEquals(0, result1);
 assertEquals(1, result2);
 }

 // Test for primitive types
 @Test
 public void testPrimitives() {
 Mock<MyIntType> mock = mock(MyIntType.class);

 // Test int
 int intResult = mock.answer(new InvocationOnMock(mock, Mockito.method(MyIntType.class, "getInt"))).intValue();
 assertEquals(0, intResult);

 // Test boolean
 boolean booleanResult = mock.answer(new InvocationOnMock(mock, Mockito.method(MyBooleanType.class, "getBoolean"))).booleanValue();
 assertEquals(false, booleanResult);
 }

 // Test for Collection-returning methods
 @Test
 public void testCollections() {
 Mock<MyCollectionType> mock = mock(MyCollectionType.class);

 // Test ArrayList
 List<String> listResult = mock.answer(new InvocationOnMock(mock, Mockito.method(MyCollectionType.class, "getList"))).<List<String>>getResult();
 assertEquals(Collections.emptyList(), listResult);

 // Test Set
 Set<String> setResult = mock.answer(new InvocationOnMock(mock, Mockito.method(MyCollectionType.class, "getSet"))).<Set<String>>getResult();
 assertEquals(Collections.emptySet(), setResult);

 // Test HashSet
 Set<String> hashSetResult = mock.answer(new InvocationOnMock(mock, Mockito.method(MyHashSetType.class, "getSet"))).<Set<String>>getResult();
 assertEquals(Collections.emptySet(), hashSetResult);

 // Test LinkedHashSet
 Set<String> linkedHashSetResult = mock.answer(new InvocationOnMock(mock, Mockito.method(MyLinkedHashSetType.class, "getSet"))).<Set<String>>getResult();
 assertEquals(Collections.emptySet(), linkedHashSetResult);

 // Test TreeSet
 Set<String> treeSetResult = mock.answer(new InvocationOnMock(mock, Mockito.method(MyTreeSetType.class, "getSet"))).<Set<String>>getResult();
 assertEquals(Collections.emptySet(), treeSetResult);
 }

 // Test for Map-returning methods
 @Test
 public void testMaps() {
 Mock<MyMapType> mock = mock(MyMapType.class);

 // Test HashMap
 Map<String, String> mapResult = mock.answer(new InvocationOnMock(mock, Mockito.method(MyMapType.class, "getMap"))).<Map<String, String>>getResult();
 assertEquals(Collections.emptyMap(), mapResult);

 // Test TreeMap
 Map<String, String> treeMapResult = mock.answer(new InvocationOnMock(mock, Mockito.method(MyTreeMapType.class, "getMap"))).<Map<String, String>>getResult();
 assertEquals(Collections.emptyMap(), treeMapResult);

 // Test LinkedHashMap
 Map<String, String> linkedHashMapResult = mock.answer(new InvocationOnMock(mock, Mockito.method(MyLinkedHashMapType.class, "getMap"))).<Map<String, String>>getResult();
 assertEquals(Collections.emptyMap(), linkedHashMapResult);
 }
}