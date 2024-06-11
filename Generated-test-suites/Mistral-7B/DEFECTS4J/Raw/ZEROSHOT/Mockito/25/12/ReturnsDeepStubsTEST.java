package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mockA;
 private Object mockB;
 private Object mockC;
 private Object mockD;

 @Before
 public void setup() {
 returnsDeepStubs = new ReturnsDeepStubs();

 mockA = mock(Map.class, returnsDeepStubs);
 mockB = mock(List.class, returnsDeepStubs);
 mockC = mock(Number.class, returnsDeepStubs);
 mockD = mock(Number.class, returnsDeepStubs);
 }

 @Test
 public void testGetMock_whenInvocationMatchesStubbedInvocation_thenReturnStubbedInvocationAnswer() throws Exception {
 InvocationOnMock invocation = ArgumentMatchers.any(Map.class).eq(mockB)
 .andReturn(mockD)
 .getMockInvocation();

 when(mockA.entrySet().iterator().next().getValue()).thenReturn(mockB);
 when(mockB.iterator().next()).thenReturn(mockC);

 assertEquals(mockD, returnsDeepStubs.answer(invocation));

 verify(mockB).iterator();
 verify(mockC).equals(mockD);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetMock_whenInvocationDoesNotMatchAnyStubbedInvocation_thenThrowException() throws Exception {
 InvocationOnMock invocation = ArgumentMatchers.any(Map.class).eq(mockB)
 .getMockInvocation();

 assertEquals(invocation.getMethod().getReturnType(), Integer.class);

 try {
 returnsDeepStubs.answer(invocation);
 } catch (Throwable e) {
 throw new IllegalStateException("Expected exception: " + e.getMessage(), e);
 }
 }

 @Test
 public void testRecordDeepStubMock() throws Exception {
 InvocationOnMock invocation = ArgumentMatchers.any(Map.class).eq(mockB)
 .andReturn(mockD)
 .getMockInvocation();

 verify(mockA, Mockito.times(1)).answer(ArgumentMatchers.any(InvocationOnMock.class), ArgumentMatchers.eq(false));

 Object deepStub = returnsDeepStubs.getMock(invocation);

 assertEquals(mockA, deepStub);
 }
}