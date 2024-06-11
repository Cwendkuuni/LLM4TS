package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Test;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues returnsEmptyValues;

 @Before
 public void setup() {
 returnsEmptyValues = new ReturnsEmptyValues();
 }

 @Test
 public void testToString() {
 Object mock = Mockito.mock(String.class);
 when(mock.toString()).thenAnswer(returnsEmptyValues);

 String actual = (String) mock.toString();
 String expected = "Mock for " + String.class.getSimpleName() + ", hashCode: " + mock.hashCode();

 assert expected.equals(actual);
 }

 @Test
 public void testCompareTo() {
 Object mock1 = Mockito.mock(Comparable.class);
 Object mock2 = Mockito.mock(Comparable.class);

 when(mock1.compareTo(ArgumentMatchers.any()))
 .thenAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return 1;
 }
 });

 when(mock2.compareTo(mock1)).thenCallRealMethod();

 int actual = mock2.compareTo(mock1);
 int expected = 1;

 assert expected == actual;
 }

 @Test
 public void testList() {
 Object mock = Mockito.mock(List.class);

 when(mock.getClass()).thenReturn(ArrayList.class);
 when(mock.toString()).thenAnswer(returnsEmptyValues);

 List<String> actual = (List<String>) mock;
 List<String> expected = new ArrayList<>();

 assert expected.isEmpty();
 }

 @Test
 public void testSet() {
 Object mock = Mockito.mock(Set.class);

 when(mock.getClass()).thenReturn(HashSet.class);
 when(mock.toString()).thenAnswer(returnsEmptyValues);

 Set<String> actual = (Set<String>) mock;
 Set<String> expected = new HashSet<>();

 assert expected.isEmpty();
 }

 @Test
 public void testMap() {
 Object mock = Mockito.mock(Map.class);

 when(mock.getClass()).thenReturn(HashMap.class);
 when(mock.toString()).thenAnswer(returnsEmptyValues);

 Map<String, String> actual = (Map<String, String>) mock;
 Map<String, String> expected = new HashMap<>();

 assert expected.isEmpty();
 }

 @Test
 public void testOtherTypes() {
 Object mock = Mockito.mock(Object.class);
 when(mock.toString()).thenAnswer(returnsEmptyValues);

 Object actual = mock;

 assert null == actual;
 }
}