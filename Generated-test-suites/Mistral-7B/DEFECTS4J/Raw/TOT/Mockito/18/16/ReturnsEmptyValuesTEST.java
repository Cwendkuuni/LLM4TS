package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;

public class ReturnsEmptyValuesTest {

 @Test
 public void testToStringMethod() {
 Mock <MyClass> mock = Mockito.mock(MyClass.class);
 String expectedToString = "Mock for MyClass, hashCode: " + System.identityHashCode(mock.object());
 when(mock.toString()).thenAnswer(new ReturnsEmptyValues());
 String actualToString = mock.toString();
 verify(mock).toString();
 assertNotNull(actualToString);
 assertEquals(expectedToString, actualToString);
 }

 @Test
 public void testCompareToMethod() {
 Mock <ComparableClass> mock = Mockito.mock(ComparableClass.class);
 ComparableClass other = new ComparableClass();
 int expectedZero = 0;
 when(mock.compareTo(ArgumentMatchers.eq(other))).thenAnswer(new ReturnsEmptyValues());
 int actualCompareTo = mock.compareTo(other);
 verify(mock).compareTo(other);
 assertEquals(expectedZero, actualCompareTo);
 }

 @Test
 public void testEmptyListMethod() {
 Mock <ListInterface> mock = Mockito.mock(ListInterface.class);
 List<Integer> expectedEmptyList = new ArrayList<>();
 when(mock.getEmptyList()).thenAnswer(new ReturnsEmptyValues());
 List<Integer> actualEmptyList = mock.getEmptyList();
 verify(mock).getEmptyList();
 assertNotNull(actualEmptyList);
 assertTrue(actualEmptyList.isEmpty());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testModifyingList() {
 Mock <ListInterface> mock = Mockito.mock(ListInterface.class);
 List<Integer> expectedEmptyList = new ArrayList<>();
 when(mock.getEmptyList()).thenAnswer(new ReturnsEmptyValues());
 List<Integer> actualEmptyList = mock.getEmptyList();
 actualEmptyList.add(1);
 }

 @Test
 public void testEmptySetMethod() {
 Mock <SetInterface> mock = Mockito.mock(SetInterface.class);
 Set<Integer> expectedEmptySet = new HashSet<>();
 when(mock.getEmptySet()).thenAnswer(new ReturnsEmptyValues());
 Set<Integer> actualEmptySet = mock.getEmptySet();
 verify(mock).getEmptySet();
 assertNotNull(actualEmptySet);
 assertTrue(actualEmptySet.isEmpty());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testModifyingSet() {
 Mock <SetInterface> mock = Mockito.mock(SetInterface.class);
 Set<Integer> expectedEmptySet = new HashSet<>();
 when(mock.getEmptySet()).thenAnswer(new ReturnsEmptyValues());
 Set<Integer> actualEmptySet = mock.getEmptySet();
 actualEmptySet.add(1);
 }

 @Test
 public void testEmptyMapMethod() {
 Mock <MapInterface> mock = Mockito.mock(MapInterface.class);
 Map<String, Integer> expectedEmptyMap = new HashMap<>();
 when(mock.getEmptyMap()).thenAnswer(new ReturnsEmptyValues());
 Map<String, Integer> actualEmptyMap = mock.getEmptyMap();
 verify(mock).getEmptyMap();
 assertNotNull(actualEmptyMap);
 assertTrue(actualEmptyMap.isEmpty());
 }
}

interface ListInterface {
 List<Integer> getEmptyList();
}

interface SetInterface {
 Set<Integer> getEmptySet();
}

interface MapInterface {
 Map<String, Integer> getEmptyMap();
}

class ComparableClass implements Comparable<ComparableClass> {
 @Override
 public int compareTo(ComparableClass other) {
 return 0;
 }
}

class MyClass {
}