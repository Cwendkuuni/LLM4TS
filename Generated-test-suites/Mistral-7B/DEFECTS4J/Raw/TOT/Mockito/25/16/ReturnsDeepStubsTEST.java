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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs deepStubs;
 private Object mockMap;
 private Object mockNumber;
 private Object mockSet;
 private Object mockEntrySet;

 @Before
 public void setup() {
 deepStubs = Mockito.spy(new ReturnsDeepStubs());
 mockMap = mock(Map.class);
 mockNumber = mock(Number.class);
 mockSet = mock(Set.class);
 mockEntrySet = mock(Map.Entry.class);
 }

 @Test
 public void testDeepStubbingWithMap() throws Exception {
 // test deep stubbing with Map
 Map<String, Set<Number>> map = mockMap;
 Set<Number> set = mockSet;
 Number number = mockNumber;
 Map.Entry<String, Set<Number>> entry = mockEntrySet;

 when(mockMap.entrySet()).thenReturn((Map.Entry<?, ?>) entry);
 when(entry.getValue()).thenReturn(set);
 when(set.iterator()).thenReturn(Arrays.asList(number).iterator());

 assertEquals(number, deepStubs.answer(Mockito.argThat(invocation -> {
 try {
 return deepStubs.answer(new InvocationOnMock(mockMap, "entrySet", new Class[]{}, null, new Object[]{}, new Object[0]));
 } catch (Throwable e) {
 throw new RuntimeException(e);
 }
 })));

 verify(mockMap).entrySet();
 verify(entry).getValue();
 verify(set).iterator();
 verify(number).getClass();
 }

 @Test
 public void testDeepStubbingWithNestedGenerics() throws Exception {
 // test deep stubbing with nested generics
 GenericsNest<String> genericsNest = mock(GenericsNest.class);
 Number number = mockNumber;

 when(genericsNest.entrySet()).thenReturn(genericsNest.entrySet());
 when(genericsNest.entrySet().iterator()).thenReturn(Arrays.asList(genericsNest.entrySet().iterator().next()).iterator());
 when(genericsNest.entrySet().iterator().next().getValue()).thenReturn(number);

 assertEquals(number, deepStubs.answer(Mockito.argThat(invocation -> {
 try {
 return deepStubs.answer(new InvocationOnMock(genericsNest, "entrySet", new Class[]{}, null, new Object[]{}, new Object[0]));
 } catch (Throwable e) {
 throw new RuntimeException(e);
 }
 })));

 verify(genericsNest).entrySet();
 verify(genericsNest.entrySet()).iterator();
 verify(genericsNest.entrySet().iterator().next()).getValue();
 verify(number).getClass();
 }

 @Test
 public void testReturningMockForNonMockableType() throws Exception {
 // test returning mock for non-mockable type
 String string = "test";
 when(deepStubs.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType().equals(String.class)))).thenReturn(string);

 assertEquals(string, deepStubs.answer(Mockito.any(InvocationOnMock.class)));
 }

 @Test(expected = RuntimeException.class)
 public void testThrowingExceptionForNonMockableType() throws Exception {
 // test throwing exception for non-mockable type
 String string = "test";
 when(deepStubs.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType().equals(String.class)))).thenThrow(new RuntimeException());

 deepStubs.answer(Mockito.any(InvocationOnMock.class));
 }

 @Test
 public void testReturningEmptyValues() throws Exception {
 // test returning empty values
 when(deepStubs.delegate.returnValueFor(Object.class)).thenReturn(Collections.emptyList());
 assertEquals(Collections.emptyList(), deepStubs.answer(Mockito.any(InvocationOnMock.class)));
 }
}