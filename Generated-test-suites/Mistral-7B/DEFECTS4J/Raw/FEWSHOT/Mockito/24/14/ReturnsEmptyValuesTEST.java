package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.mockito.internal.util.Primitives.isPrimitiveOrWrapper;

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

import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.Primitives;

public class ReturnsEmptyValuesTest {

 private static final ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
 private static final ObjectMethodsGuru methodsGuru = new ObjectMethodsGuru();

 @Test
 public void testAnswerToStringMethod() {
 String toString = "Mock for SomeClass, hashCode: 123456";
 String expected = returnsEmptyValues.answer(mockMethodWithToString(toString));
 assertEquals(toString, expected);
 }

 @Test
 public void testAnswerCompareToMethod() {
 int one = 1;
 int zero = 0;
 int expected = one;
 int actual = returnsEmptyValues.answer(mockMethodWithCompareTo(expected, one));
 assertEquals(expected, actual);
 }

 @Test
 public void testAnswerPrimitiveTypes() {
 byte b = 127;
 char c = 'A';
 short s = Short.MIN_VALUE;
 int i = 42;
 long l = Long.MIN_VALUE;
 float f = Float.MIN_VALUE;
 double d = Double.MIN_VALUE;
 boolean bool = true;

 byte expectedByte = Primitives.byteValue(b);
 char expectedChar = Primitives.charValue(c);
 short expectedShort = Primitives.shortValue(s);
 int expectedInt = Primitives.intValue(i);
 long expectedLong = Primitives.longValue(l);
 float expectedFloat = Primitives.floatValue(f);
 double expectedDouble = Primitives.doubleValue(d);
 boolean expectedBoolean = Primitives.booleanValue(bool);

 // check return values for primitives
 byte actualByte = (byte) returnsEmptyValues.answer(mockMethodWithReturnType(byte.class)).intValue();
 assertEquals(expectedByte, actualByte);

 char actualChar = (char) returnsEmptyValues.answer(mockMethodWithReturnType(char.class)).intValue();
 assertEquals(expectedChar, actualChar);

 short actualShort = (short) returnsEmptyValues.answer(mockMethodWithReturnType(short.class)).intValue();
 assertEquals(expectedShort, actualShort);

 int actualInt = returnsEmptyValues.answer(mockMethodWithReturnType(int.class)).intValue();
 assertEquals(expectedInt, actualInt);

 long actualLong = returnsEmptyValues.answer(mockMethodWithReturnType(long.class)).longValue();
 assertEquals(expectedLong, actualLong);

 float actualFloat = (float) returnsEmptyValues.answer(mockMethodWithReturnType(float.class)).floatValue();
 assertEquals(expectedFloat, actualFloat, 0.001f);

 double actualDouble = (double) returnsEmptyValues.answer(mockMethodWithReturnType(double.class)).doubleValue();
 assertEquals(expectedDouble, actualDouble, 0.001d);

 boolean actualBoolean = (boolean) returnsEmptyValues.answer(mockMethodWithReturnType(boolean.class)).booleanValue();
 assertEquals(expectedBoolean, actualBoolean);
 }

 @Test
 public void testAnswerEmptyCollection() {
 Class<?> collectionType = Collection.class;
 Collection<?> expected = new LinkedList<Object>();
 Collection<?> actual = (Collection<?>) returnsEmptyValues.answer(mockMethodWithReturnType(collectionType));
 assertEquals(expected, actual);
 }

 @Test
 public void testAnswerEmptySet() {
 Class<?> setType = Set.class;
 Set<?> expected = new HashSet<Object>();
 Set<?> actual = (Set<?>) returnsEmptyValues.answer(mockMethodWithReturnType(setType));
 assertEquals(expected, actual);
 }

 @Test
 public void testAnswerEmptyHashSet() {
 Class<?> hashSetType = HashSet.class;
 Set<?> expected = new HashSet<Object>();
 Set<?> actual = (Set<?>) returnsEmptyValues.answer(mockMethodWithReturnType(hashSetType));
 assertEquals(expected, actual);
 }

 @Test
 public void testAnswerEmptyTreeSet() {
 Class<?> treeSetType = TreeSet.class;
 Set<?> expected = new TreeSet<Object>();
 Set<?> actual = (Set<?>) returnsEmptyValues.answer(mockMethodWithReturnType(treeSetType));
 assertEquals(expected, actual);
 }

 @Test
 public void testAnswerEmptyLinkedHashSet() {
 Class<?> linkedHashSetType = LinkedHashSet.class;
 Set<?> expected = new LinkedHashSet<Object>();
 Set<?> actual = (Set<?>) returnsEmptyValues.answer(mockMethodWithReturnType(linkedHashSetType));
 assertEquals(expected, actual);
 }

 @Test
 public void testAnswerEmptyList() {
 Class<?> listType = List.class;
 List<?> expected = new LinkedList<Object>();
 List<?> actual = (List<?>) returnsEmptyValues.answer(mockMethodWithReturnType(listType));
 assertEquals(expected, actual);
 }

 @Test
 public void testAnswerEmptyLinkedList() {
 Class<?> linkedListType = LinkedList.class;
 List<?> expected = new LinkedList<Object>();
 List<?> actual = (List<?>) returnsEmptyValues.answer(mockMethodWithReturnType(linkedListType));
 assertEquals(expected, actual);
 }

 @Test
 public void testAnswerEmptyArrayList() {
 Class<?> arrayListType = ArrayList.class;
 List<?> expected = new ArrayList<Object>();
 List<?> actual = (List<?>) returnsEmptyValues.answer(mockMethodWithReturnType(arrayListType));
 assertEquals(expected, actual);
 }

 @Test
 public void testAnswerEmptyMap() {
 Class<?> mapType = Map.class;
 Map<String, Object> expected = new HashMap<String, Object>();
 Map<String, Object> actual = (Map<String, Object>) returnsEmptyValues.answer(mockMethodWithReturnType(mapType));
 assertEquals(expected, actual);
 }

 @Test
 public void testAnswerEmptyHashMap() {
 Class<?> hashMapType = HashMap.class;
 Map<String, Object> expected = new HashMap<String, Object>();
 Map<String, Object> actual = (Map<String, Object>) returnsEmptyValues.answer(mockMethodWithReturnType(hashMapType));
 assertEquals(expected, actual);
 }

 @Test
 public void testAnswerEmptyTreeMap() {
 Class<?> treeMapType = TreeMap.class;
 SortedMap<String, Object> expected = new TreeMap<String, Object>();
 SortedMap<String, Object> actual = (SortedMap<String, Object>) returnsEmptyValues.answer(mockMethodWithReturnType(treeMapType));
 assertEquals(expected, actual);
 }

 @Test
 public void testAnswerEmptyTreeSet() {
 Class<?> treeSetType = TreeSet.class;
 SortedSet<String> expected = new TreeSet<String>();
 SortedSet<String> actual = (SortedSet<String>) returnsEmptyValues.answer(mockMethodWithReturnType(treeSetType));
 assertEquals(expected, actual);
 }

 @Test
 public void testAnswerEmptyLinkedHashMap() {
 Class<?> linkedHashMapType = LinkedHashMap.class;
 Map<String, Object> expected = new LinkedHashMap<String, Object>();
 Map<String, Object> actual = (Map<String, Object>) returnsEmptyValues.answer(mockMethodWithReturnType(linkedHashMapType));
 assertEquals(expected, actual);
 }

 @Test
 public void testAnswerNull() {
 Class<?> nullType = null;
 Object actual = returnsEmptyValues.answer(mockMethodWithReturnType(nullType));
 assertNull(actual);
 }

 @Test
 public void testAnswerNullIfPrimitiveWrapperType() {
 byte b = 1;
 Class<?> byteWrapperType = Byte.class;
 Object actual = returnsEmptyValues.answer(mockMethodWithReturnType(byteWrapperType));
 assertNull(actual);
 }

 @Test
 public void testAnswerNullIfCustomType() {
 Class<?> customType = MyCustomClass.class;
 Object actual = returnsEmptyValues.answer(mockMethodWithReturnType(customType));
 assertNull(actual);
 }

 private InvocationOnMock mockMethodWithToString(String toString) {
 Object mock = mock(Object.class);
 when(mock.toString()).thenReturn(toString);
 return new InvocationOnMock(mock, mockUtil.getMockMethod(mock, "toString", new Class[0]));
 }

 private InvocationOnMock mockMethodWithCompareTo(int compareToValue, Class<?> compareToType) {
 Object mock = mock(Object.class);
 when(mock.compareTo((Object) any())).thenReturn(compareToValue);
 when(mock.getClass()).thenReturn(compareToType);
 return new InvocationOnMock(mock, mockUtil.getMockMethod(mock, "compareTo", Object.class));
 }

 private InvocationOnMock mockMethodWithReturnType(Class<?> returnType) {
 Object mock = mock(Object.class);
 when(mock.getClass()).thenReturn(returnType);
 return new InvocationOnMock(mock, mockUtil.getMockMethod(mock, "getClass", new Class[0]));
 }

 private boolean isMockitoMock(Object object) {
 return object != null && methodsGuru.isMockitoMock(object);
 }

}

class MyCustomClass {
 // empty custom class
}