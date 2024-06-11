package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mockGenericsNest;
 private Object mockNumber;
 private Object mockSet;

 @Before
 public void setUp() {
 returnsDeepStubs = new ReturnsDeepStubs();

 mockGenericsNest = mock(Map.class, new ReturnsDeepStubs());
 mockNumber = mock(Number.class, new ReturnsDeepStubs());
 mockSet = mock(Set.class, new ReturnsDeepStubs());
 }

 @Test
 public void testReturnsDeepStubsForMapInterface() {
 Map<String, Integer> map = (Map<String, Integer>) mockGenericsNest;
 when(map.entrySet().iterator().next().getValue()).thenReturn(mockNumber);

 Integer result = (Map<String, Integer>) mockGenericsNest.entrySet().iterator().next().getValue().iterator().next();

 verify(mockNumber).answer(ArgumentMatchers.any(InvocationOnMock.class));
 assertEquals(mockNumber, result);
 }

 @Test
 public void testReturnsDeepStubsForSetInterface() {
 Set<Number> set = (Set<Number>) mockGenericsNest;
 when(set.iterator().next()).thenReturn(mockNumber);

 Number result = (Set<Number>) mockGenericsNest.iterator().next();

 verify(mockNumber).answer(ArgumentMatchers.any(InvocationOnMock.class));
 assertEquals(mockNumber, result);
 }

 @Test
 public void testReturnsDeepStubsForNestedGenericType() {
 Map<String, Set<Number>> map = (Map<String, Set<Number>>) mockGenericsNest;
 Set<Number> set = mockSet;
 when(map.entrySet().iterator().next().getValue()).thenReturn(set);
 when(set.iterator().next()).thenReturn(mockNumber);

 Number result = (Map<String, Set<Number>>) mockGenericsNest.entrySet().iterator().next().getValue().iterator().next().getValue().iterator().next();

 verify(mockNumber).answer(ArgumentMatchers.any(InvocationOnMock.class));
 assertEquals(mockNumber, result);
 }

 @Test(expected = RuntimeException.class)
 public void testCreatesNewMockForNonMockableType() {
 when(mockGenericsNest.getClass()).thenReturn(new Object() {}.getClass());

 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Object result = returnsDeepStubs.answer(Mockito.mock(mockGenericsNest.getClass()));

 // This should throw a RuntimeException as the class is not mockable
 }
}