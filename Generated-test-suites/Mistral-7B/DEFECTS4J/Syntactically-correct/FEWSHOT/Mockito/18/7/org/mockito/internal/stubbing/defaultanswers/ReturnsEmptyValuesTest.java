package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.internal.util.Primitives.*;
import static org.mockito.mockitofy.Mockito.*;

import java.io.Serializable;
import java.util.*;
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsEmptyValuesTest {

 @Test
 public void testAnswer_InvocationOnMockWithToStringMethod_ShouldReturnDescriptionOfMock() {
 Mock <Integer> mock = mock(Integer.class);
 InvocationOnMock invocation = mock.getInvocationMock();

 ReturnsEmptyValues answer = new ReturnsEmptyValues();
 Object result = answer.answer(invocation);

 String expected = "Mock for java.lang.Integer, hashCode: " + mock.hashCode();
 String actual = (String) result;

 assertEquals(expected, actual);
 }

 @Test
 public void testAnswer_InvocationOnMockWithCompareToMethod_ShouldReturnZeroIfMocksAreEqual() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 invocation.setTarget(mock1);
 invocation.setArguments(new Object[]{mock2});

 ReturnsEmptyValues answer = new ReturnsEmptyValues();
 int result = (Integer) answer.answer(invocation);

 assertEquals(0, result);
 }

 @Test
 public void testAnswer_InvocationOnMockWithNullReturnType_ShouldReturnNull() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);

 ReturnsEmptyValues answer = new ReturnsEmptyValues();
 Object result = answer.answer(invocation);

 assertNull(result);
 }

 @Test
 public void testReturnValueFor_PrimitiveClass_ShouldReturnDefaultValue() {
 Class<Byte> byteType = Byte.TYPE;
 Object result = new ReturnsEmptyValues().returnValueFor(byteType);

 byte expected = (byte) 0;
 byte actual = (byte) result;

 assertEquals(expected, actual);
 }

 @Test
 public void testReturnValueFor_WrapperClass_ShouldReturnDefaultValue() {
 Class<Integer> intWrapperType = Integer.TYPE;
 Object result = new ReturnsEmptyValues().returnValueFor(intWrapperType);

 Integer expected = 0;
 Integer actual = (Integer) result;

 assertEquals(expected, actual);
 }

 @Test
 public void testReturnValueFor_CollectionTypes_ShouldReturnEmptyInstance() {
 Class<List<String>> listType = List.class.cast(new Class[] {String.class, Collection.class}[0]);
 Object result = new ReturnsEmptyValues().returnValueFor(listType);

 List<String> expected = new ArrayList<>();
 List<String> actual = (List<String>) result;

 assertTrue(expected.isEmpty());
 assertTrue(actual.isEmpty());
 }

 @Test
 public void testReturnValueFor_SetTypes_ShouldReturnEmptyInstance() {
 Class<Set<Integer>> setType = Set.class.cast(new Class[] {Integer.class, Collection.class}[0]);
 Object result = new ReturnsEmptyValues().returnValueFor(setType);

 Set<Integer> expected = new HashSet<>();
 Set<Integer> actual = (Set<Integer>) result;

 assertTrue(expected.isEmpty());
 assertTrue(actual.isEmpty());
 }

 @Test
 public void testReturnValueFor_MapTypes_ShouldReturnEmptyInstance() {
 Class<Map<String, Integer>> mapType = Map.class.cast(new Class[] {String.class, Object.class, Collection.class}[0]);
 Object result = new ReturnsEmptyValues().returnValueFor(mapType);

 Map<String, Integer> expected = new HashMap<>();
 Map<String, Integer> actual = (Map<String, Integer>) result;

 assertTrue(expected.isEmpty());
 assertTrue(actual.isEmpty());
 }
}