package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 @Test
 public void testAnswer_toStringMethod() {
 Mockito.when(mock(String.class)).thenReturn(returnsEmptyValues);
 String mock = mock(String.class);
 String result = mock.toString();

 String expected = "Mock for " + String.class.getSimpleName() + ", hashCode: " + mock.hashCode();

 verify(mock).toString();
 verify(mock).hashCode();

 assert(expected.equals(result.toString()));
 }

 @Test
 public void testAnswer_compareToMethod() {
 Object object1 = mock(Comparable.class);
 Object object2 = mock(Comparable.class);

 Mockito.when(object1.compareTo(object2)).thenReturn(returnsEmptyValues);

 int result = object1.compareTo(object2);

 verify(object1).compareTo(object2);

 assert(1 == result);
 }

 @Test
 public void testReturnValueFor_PrimitiveType() {
 Class<Byte> byteType = Byte.TYPE;
 Object result = returnsEmptyValues.returnValueFor(byteType);

 byte expected = 0;

 assert(expected.equals(result));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testReturnValueFor_CollectionType_WhenModifyingCollectionUnderTest() {
 List<String> list = mock(List.class);
 list.add("Test");
 Mockito.doReturn(returnsEmptyValues).when(list).get(0);

 List<String> result = list.subList(0, 1);
 }

 @Test
 public void testReturnValueFor_CollectionType() {
 Class<List<String>> listType = List.class;
 List<String> expected = new ArrayList<>();
 Object result = returnsEmptyValues.returnValueFor(listType);

 assert(expected.getClass().isAssignableFrom(result.getClass()));
 }

 @Test
 public void testReturnValueFor_SetType() {
 Class<Set<Integer>> setType = Set.class;
 Set<Integer> expected = new HashSet<>();
 Object result = returnsEmptyValues.returnValueFor(setType);

 assert(expected.getClass().isAssignableFrom(result.getClass()));
 }

 @Test
 public void testReturnValueFor_MapType() {
 Class<Map<String, Integer>> mapType = Map.class;
 Map<String, Integer> expected = new HashMap<>();
 Object result = returnsEmptyValues.returnValueFor(mapType);

 assert(expected.getClass().isAssignableFrom(result.getClass()));
 }

 // Add more test cases for other types here

}