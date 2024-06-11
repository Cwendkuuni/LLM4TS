package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InternalMockHandler;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private InternalMockHandler<Object> mockHandler;
 private Object mockMap;
 private Object mockSet;

 @Before
 public void setUp() {
 mockHandler = Mockito.mock(InternalMockHandler.class);
 returnsDeepStubs = new ReturnsDeepStubs();
 when(mockHandler.getInvocationContainer()).thenReturn(new InvocationContainerImpl());
 mockMap = Mockito.mock(Map.class, withSettings().defaultAnswer(returnsDeepStubs));
 mockSet = Mockito.mock(Set.class, withSettings().defaultAnswer(returnsDeepStubs));
 }

 @Test
 public void shouldReturnDeepStubForMapEntrySet() throws Exception {
 Map<Integer, Set<Number>> map = mockMap;
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 InvocationOnMock iteratorInvocation = mock(InvocationOnMock.class);
 InvocationOnMock nextInvocation = mock(InvocationOnMock.class);
 when(iteratorInvocation.getTarget()).thenReturn(map);
 when(iteratorInvocation.getMethod()).thenReturn(Map.class.getMethod("entrySet"));
 when(nextInvocation.getTarget()).thenReturn(map);
 when(nextInvocation.getMethod()).thenReturn(Map.Entry.class.getMethod("getValue"));
 when(nextInvocation.getArgument(0)).thenReturn(iteratorInvocation);
 when(invocation.getMock()).thenReturn(map);
 when(invocation.getMethod()).thenReturn(Map.EntrySet.class.getMethod("iterator"));

 Object result = invocation.proceed();
 assertEquals(mockSet, result.get(0).getValue());

 verify(map, times(1)).entrySet();
 verify(map, times(1)).entrySet().iterator();
 verify(map, times(1)).entrySet().iterator().next();
 verify(map, times(1)).entrySet().iterator().next().getValue();
 verify(map, times(1)).entrySet().iterator().next().getValue().iterator();
 verify(map, times(1)).entrySet().iterator().next().getValue().iterator().next();
 }

 @Test
 public void shouldReturnDeepStubForSetIterator() throws Exception {
 Set<Number> set = mockSet;
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 InvocationOnMock iteratorInvocation = mock(InvocationOnMock.class);
 InvocationOnMock nextInvocation = mock(InvocationOnMock.class);
 when(iteratorInvocation.getTarget()).thenReturn(set);
 when(iteratorInvocation.getMethod()).thenReturn(Set.class.getMethod("iterator"));
 when(nextInvocation.getTarget()).thenReturn(set);
 when(nextInvocation.getMethod()).thenReturn(Number.class);
 when(nextInvocation.getArgument(0)).thenReturn(iteratorInvocation);
 when(invocation.getMock()).thenReturn(set);
 when(invocation.getMethod()).thenReturn(Set.class.getMethod("iterator"));

 Object result = invocation.proceed();
 assertEquals(nextInvocation.getArgument(0), result);

 verify(set, times(1)).iterator();
 verify(set, times(1)).iterator().next();
 }

 @Test(expected = IllegalStateException.class)
 public void shouldThrowExceptionWhenInvokingNonMockableType() throws Exception {
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Object nonMockableType = new Object() {};
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(invocation.getMock()).thenReturn(nonMockableType);
 when(invocation.getMethod()).thenReturn(Void.TYPE.getMethod("void"));

 Object result = invocation.proceed();
 fail("Should throw exception");
 }

 @Test(expected = IllegalStateException.class)
 public void shouldThrowExceptionWhenInvokingNonGenericReturnType() throws Exception {
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Object nonGenericReturnType = mock(String.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(invocation.getMock()).thenReturn(nonGenericReturnType);
 when(invocation.getMethod()).thenReturn(String.class.getMethod("length"));

 Object result = invocation.proceed();
 fail("Should throw exception");
 }

 @Test
 public void shouldReturnEmptyValueForNonGenericReturnType() throws Exception {
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Object emptyValue = new ReturnsEmptyValues().returnValueFor(String.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(invocation.getMock()).thenReturn("test");
 when(invocation.getMethod()).thenReturn(String.class.getMethod("length"));

 Object result = invocation.proceed();
 assertEquals(emptyValue, result);
 }
}