package org.mockito.internal.stubbing.defaultanswers.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*; // Import Mockito statics

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.ReturnsDeepStubs;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.mock.MockSettings;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;

 @Before
 public void setup() {
 returnsDeepStubs = new ReturnsDeepStubs();
 }

 @Test
 public void testReturnsSameMockForIdenticalInvocations() {
 Object mock = mock(Map.class, with(ReturnsDeepStubs.class));
 Object key = "test";
 Object value = 42;

 // When setting entry in map
 mock.put(key, value);

 // Then verify that put method was called and returned the same mock
 verify(mock).put(eq(key), eq(value));

 // When getting value from map
 Object valueFromMap = mock.get(key);

 // Then verify that get method was called and returned the same mock
 verify(mock).get(eq(key));

 // Then verify that get method returned the same value
 assertEquals(value, valueFromMap);
 }

 @Test
 public void testReturnsDeepStubForGenericTypes() {
 interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
 }

 GenericsNest<Number> mock = mock(GenericsNest.class, with(ReturnsDeepStubs.class));
 Number number = mock.entrySet().iterator().next().getValue().iterator().next();

 // Then verify that get method was called and returned the same mock
 verify(mock).entrySet();
 verify(mock.entrySet()).iterator();
 verify(mock.entrySet().iterator()).next();
 verify(mock.entrySet().iterator().next()).getValue();
 verify(mock.entrySet().iterator().next().getValue()).iterator();
 verify(mock.entrySet().iterator().next().getValue().iterator()).next();

 // Then verify that get method returned the same Number instance
 assertEquals(number, number);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testReturnsEmptyValuesForNonMockableTypes() {
 Object nonMockableType = new Object() {
 };

 // Given trying to mock a non-mockable type with returnsDeepStubs
 mock(nonMockableType.getClass(), with(ReturnsDeepStubs.class));

 // Then an UnsupportedOperationException should be thrown
 Mockito.doThrow(new UnsupportedOperationException("Cannot mock this class")).when(nonMockableType).toString();
 }

 @Test
 public void testReturnsDeepStubForInvocationsOnSameMockWithDifferentArguments() {
 Object mock = mock(Map.class, with(ReturnsDeepStubs.class));
 Object key1 = "test1";
 Object value1 = 42;
 Object key2 = "test2";
 Object value2 = 43;

 // When setting entry in map with key1
 mock.put(key1, value1);

 // Then verify that put method was called and returned the same mock
 verify(mock).put(eq(key1), eq(value1));

 // When setting entry in map with key2
 mock.put(key2, value2);

 // Then verify that put method was called and returned the same mock
 verify(mock).put(eq(key2), eq(value2));

 // Then verify that get method called with key1 returned the same mock as the one returned by put method
 Object valueFromMap1 = mock.get(key1);
 verify(mock).get(eq(key1));

 // Then verify that get method called with key2 returned the same mock as the one returned by put method
 Object valueFromMap2 = mock.get(key2);
 verify(mock).get(eq(key2));

 // Then verify that get method called with key1 returned the same value as the one passed to put method
 assertEquals(value1, valueFromMap1);

 // Then verify that get method called with key2 returned the same value as the one passed to put method
 assertEquals(value2, valueFromMap2);
 }

 @Test
 public void testReturnsDeepStubForInvocationsOnSameMockWithSameArgumentsButDifferentOrder() {
 Object mock = mock(Map.class, with(ReturnsDeepStubs.class));
 Object key1 = "test1";
 Object value1 = 42;
 Object key2 = "test2";
 Object value2 = 43;

 // When setting entry in map with key1
 mock.put(key1, value1);

 // Then verify that put method was called and returned the same mock
 verify(mock).put(eq(key1), eq(value1));

 // When setting entry in map with key2
 mock.put(key2, value2);

 // Then verify that put method was called and returned the same mock
 verify(mock).put(eq(key2), eq(value2));

 // Then verify that get method called with key1 returned the same mock as the one returned by put method
 Object valueFromMap1 = mock.get(key2);
 // This test should pass, as get method with key2 is called after put method with key1, so the mock returned by get method is the same as the one returned by put method
 verify(mock).get(eq(key2));

 // Then verify that get method called with key1 returned the same mock as the one returned by put method
 Object valueFromMap2 = mock.get(key1);
 // This test should pass, as get method with key1 is called after put method with key2, so the mock returned by get method is the same as the one returned by put method
 verify(mock).get(eq(key1));

 // Then verify that get method called with key1 returned the same value as the one passed to put method
 assertEquals(value1, valueFromMap1);
 // This test should pass, as the value returned by get method with key1 is the same as the value passed to put method with key1
 assertEquals(value1, valueFromMap2);
 }

 @Test
 public void testReturnsDeepStubForInvocationsOnDifferentMocksWithSameArguments() {
 Object mock1 = mock(Map.class, with(ReturnsDeepStubs.class));
 Object mock2 = mock(Map.class, with(ReturnsDeepStubs.class));
 Object key = "test";
 Object value = 42;

 // When setting entry in map1
 mock1.put(key, value);

 // Then verify that put method was called on mock1 and returned mock1
 verify(mock1).put(eq(key), eq(value));

 // When setting entry in map2
 mock2.put(key, value);

 // Then verify that put method was called on mock2 and returned mock2
 verify(mock2).put(eq(key), eq(value));

 // Then verify that get method called on mock1 with key returned mock1
 Object valueFromMock1 = mock1.get(key);
 verify(mock1).get(eq(key));

 // Then verify that get method called on mock2 with key returned mock2
 Object valueFromMock2 = mock2.get(key);
 verify(mock2).get(eq(key));

 // Then verify that get method called on mock1 with key returned the same value as the one passed to put method
 assertEquals(value, valueFromMock1);

 // Then verify that get method called on mock2 with key returned the same value as the one passed to put method
 assertEquals(value, valueFromMock2);
 }

 @Test
 public void testReturnsDeepStubForInvocationsOnDifferentMocksWithDifferentArguments() {
 Object mock1 = mock(Map.class, with(ReturnsDeepStubs.class));
 Object mock2 = mock(Map.class, with(ReturnsDeepStubs.class));
 Object key1 = "test1";
 Object value1 = 42;
 Object key2 = "test2";
 Object value2 = 43;

 // When setting entry in map1
 mock1.put(key1, value1);

 // Then verify that put method was called on mock1 and returned mock1
 verify(mock1).put(eq(key1), eq(value1));

 // When setting entry in map2
 mock2.put(key2, value2);

 // Then verify that put method was called on mock2 and returned mock2
 verify(mock2).put(eq(key2), eq(value2));

 // Then verify that get method called on mock1 with key1 returned mock1
 Object valueFromMock1 = mock1.get(key1);
 verify(mock1).get(eq(key1));

 // Then verify that get method called on mock2 with key2 returned mock2
 Object valueFromMock2 = mock2.get(key2);
 verify(mock2).get(eq(key2));

 // Then verify that get method called on mock1 with key1 returned the same value as the one passed to put method
 assertEquals(value1, valueFromMock1);

 // Then verify that get method called on mock2 with key2 returned the value passed to put method on mock2
 assertEquals(value2, valueFromMock2);
 }

 @Test
 public void testReturnsDeepStubForInvocationsOnSameMockWithSameArgumentsButDifferentGenericTypes() {
 Object mock = mock(Map.class, with(ReturnsDeepStubs.class));
 Object key = "test";
 Object value = 42;
 Object valueAsMapValue = new HashSet<Number>(Arrays.asList(1, 2, 3));

 // When setting entry in map
 mock.put(key, valueAsMapValue);

 // Then verify that put method was called and returned the same mock
 verify(mock).put(eq(key), eq(valueAsMapValue));

 // Then verify that get method called with key returned the same mock as the one returned by put method
 Object valueFromMap = mock.get(key);
 verify(mock).get(eq(key));

 // Then verify that get method called with key returned the same instance of HashSet as the one passed to put method
 assertEquals(valueAsMapValue, valueFromMap);

 // Then verify that get method called with key returned the same Set instance as the one passed to put method
 assertTrue(valueAsMapValue.getClass().isAssignableFrom(valueFromMap.getClass()));
 }

 @Test
 public void testReturnsDeepStubForInvocationsOnSameMockWithDifferentArgumentsButSameGenericTypes() {
 Object mock = mock(Map.class, with(ReturnsDeepStubs.class));
 Object key1 = "test1";
 Object value1 = new HashSet<Number>(Arrays.asList(1, 2, 3));
 Object key2 = "test2";
 Object value2 = new HashSet<Number>(Arrays.asList(4, 5, 6));

 // When setting entry in map with key1
 mock.put(key1, value1);

 // Then verify that put method was called and returned the same mock
 verify(mock).put(eq(key1), eq(value1));

 // When setting entry in map with key2
 mock.put(key2, value2);

 // Then verify that put method was called and returned the same mock
 verify(mock).put(eq(key2), eq(value2));

 // Then verify that get method called with key1 returned the same mock as the one returned by put method
 Object valueFromMap1 = mock.get(key1);
 verify(mock).get(eq(key1));

 // Then verify that get method called with key1 returned the same instance of HashSet as the one passed to put method
 assertEquals(value1, valueFromMap1);

 // Then verify that get method called with key2 returned the same mock as the one returned by put method
 Object valueFromMap2 = mock.get(key2);
 verify(mock).get(eq(key2));

 // Then verify that get method called with key2 returned an instance of HashSet with different values than the one passed to put method with key1
 assertFalse(valueFromMap1.equals(valueFromMap2));
 }

 @Test
 public void testReturnsDeepStubForInvocationsOnSameMockWithSameArgumentsButDifferentGenericTypesAndDifferentGenericContexts() {
 Object mock = mock(Map.class, with(ReturnsDeepStubs.class));
 Object key = "test";
 Object value = new HashSet<Number>(Arrays.asList(1, 2, 3));
 Object valueAsMapValue = new HashSet<String>(Arrays.asList("one", "two", "three"));

 // When setting entry in map
 mock.put(key, valueAsMapValue);

 // Then verify that put method was called and returned the same mock
 verify(mock).put(eq(key), eq(valueAsMapValue));

 // Then verify that get method called with key returned the same mock as the one returned by put method
 Object valueFromMap = mock.get(key);
 verify(mock).get(eq(key));

 // Then verify that get method called with key returned an instance of HashSet with different type than the one passed to put method
 assertFalse(value.getClass().isAssignableFrom(valueFromMap.getClass()));
 }
}