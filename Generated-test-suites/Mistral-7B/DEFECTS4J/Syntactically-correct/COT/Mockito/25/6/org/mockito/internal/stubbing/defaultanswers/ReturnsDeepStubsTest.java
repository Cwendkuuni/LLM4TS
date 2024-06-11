package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

 @Test
 public void testAnswerForPrimitiveType() {
  Object mock = Mockito.mock(int.class);
  Assert.assertSame(mock, returnsDeepStubs.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType() == int.class)));
 }

 @Test
 public void testAnswerForNonMockableType() {
  Assert.assertNotNull(returnsDeepStubs.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType() == String.class)));
 }

 @Test
 public void testGetMockForMatchingInvocation() {
  InterfaceNest mock = Mockito.mock(InterfaceNest.class, new ReturnsDeepStubs());
  Number number = Mockito.mock(Number.class);
  Mockito.doReturn(number).when(mock).entrySet().iterator().next().getValue().iterator().next();
  Object returnedNumber = returnsDeepStubs.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType() == Number.class &&
          Mockito.isA(InterfaceNest.class).and(Mockito.argThat(iMock -> Mockito.equals(mock, iMock)))));
  Assert.assertSame(number, returnedNumber);
 }

 @Test(expected = Throwable.class)
 public void testGetMockForNonMatchingInvocation() {
  InterfaceNest mock = Mockito.mock(InterfaceNest.class, new ReturnsDeepStubs());
  Object returnedObject = returnsDeepStubs.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType() == String.class));
  // No mock or answer should be created for this invocation
 }

 @Test
 public void testRecordDeepStubMock() {
  InterfaceNest mock = Mockito.mock(InterfaceNest.class, new ReturnsDeepStubs());
  List<Integer> list = Mockito.mock(List.class);
  Map<String, Set<Number>> map = Mockito.mock(Map.class);
  Set<Number> set = Mockito.mock(Set.class);

  Mockito.when(mock.entrySet().iterator().next()).thenReturn(Map.entry(map, list));
  Mockito.when(map.get("test")).thenReturn(set);
  Mockito.when(set.iterator().next()).thenReturn(Mockito.mock(Number.class));

  Object deepStubMock = returnsDeepStubs.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType() == Map.class &&
          Mockito.isA(InterfaceNest.class).and(Mockito.argThat(iMock -> Mockito.equals(mock, iMock)))));

  Assert.assertSame(map, deepStubMock);
 }

}

interface InterfaceNest<K extends Comparable<K> & Cloneable, V> extends Map<K, Set<V>> {
}