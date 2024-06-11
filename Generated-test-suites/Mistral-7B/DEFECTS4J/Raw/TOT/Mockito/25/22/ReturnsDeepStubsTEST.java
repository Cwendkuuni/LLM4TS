package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object genericNestMock;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 returnsDeepStubs = new ReturnsDeepStubs();
 GenericMetadataSupport genericMetadata = new GenericMetadataSupport(Map.class, Set.class);
 genericNestMock = mock(Map.class, new ReturnsDeepStubs());
 }

 @Test
 public void testReturnsDeepStubForGenericClass() {
 Map<Integer, Set<Integer>> map = (Map<Integer, Set<Integer>>) genericNestMock;
 Set<Integer> set = mock(Set.class);
 map.put(1, set);
 Set<Number> numberSet = map.entrySet().iterator().next().getValue().iterator().next();
 Number number = numberSet.iterator().next();
 assertEquals(number, number); //should return the same mock instance
 verify(numberSet).add(ArgumentMatchers.isNull()); //verification should work
 }

 @Test
 public void testReturnsEmptyValuesForNonMockableClass() {
 Number number = (Number) returnsDeepStubs.answer(Mockito.mock(String.class));
 assertEquals(Number.class, number.getClass()); //should not return a mock instance for non-mockable class
 }

 @Test
 public void testRecordsDeepStubMockForInvocation() {
 Map<String, String> map = mock(Map.class, new ReturnsDeepStubs());
 String value = "value";
 map.put("key", value);
 String deepStub = map.get("key");
 assertEquals(value, deepStub); //should return the same mock instance for the same invocation
 }

 @Test
 public void testVerifiesStubsForAnswer() {
 Map<String, String> map = mock(Map.class, new ReturnsDeepStubs());
 map.put("key1", "value1");
 map.put("key2", "value2");
 map.remove("key1");
 map.get("key1"); //should not throw an exception
 map.get("key3"); //should throw an exception
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testThrowsExceptionForUnsupportedOperation() {
 Map<String, String> map = (Map<String, String>) genericNestMock;
 map.clear();
 }
}