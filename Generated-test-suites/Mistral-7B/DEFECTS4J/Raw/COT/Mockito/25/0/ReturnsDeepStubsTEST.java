package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.internal.util.reflection.GenericMetadataSupport;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ReturnsDeepStubsTest {

 private static final Class<ReturnsDeepStubs> RETURNS_DEEP_STUBS_CLASS = ReturnsDeepStubs.class;

 @Test
 public void testAnswerMethodWithNonMockableType() {
  ReturnsDeepStubs returnsDeepStubs = Mockito.spy(new ReturnsDeepStubs());
  InvocationOnMock invocation = Mockito.mock(Object.class);
  Assert.assertNotNull(returnsDeepStubs.answer(invocation));
 }

 @Test
 public void testGetMockMethodWithNoMatchingStubbedInvocations() {
  InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
  InternalMockHandler<Object> handler = Mockito.mock(InternalMockHandler.class);
  Mockito.when(handler.getInvocationContainer()).thenReturn(container);
  InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

  ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
  returnsDeepStubs.answer(invocation);

  Mockito.verifyNoMoreInteractions(container, handler);
 }

 @Test
 public void testGetMockMethodWithMatchingStubbedInvocations() {
  InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
  InternalMockHandler<Object> handler = Mockito.mock(InternalMockHandler.class);
  Mockito.when(handler.getInvocationContainer()).thenReturn(container);
  InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
  StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.mock(StubbedInvocationMatcher.class);
  Mockito.when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[] { stubbedInvocationMatcher });
  Mockito.when(stubbedInvocationMatcher.answer(invocation)).thenReturn("matching answer");

  ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
  Object result = returnsDeepStubs.answer(invocation);
  Mockito.verify(stubbedInvocationMatcher).answer(invocation);
  Assert.assertEquals("matching answer", result);
 }

 @Test(expected = Throwable.class)
 public void testGetMockMethodWithThrowableException() {
  InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
  InternalMockHandler<Object> handler = Mockito.mock(InternalMockHandler.class);
  Mockito.when(handler.getInvocationContainer()).thenReturn(container);
  InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
  Mockito.when(container.getInvocationForStubbing()).thenThrow(new RuntimeException("Exception message"));

  ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
  returnsDeepStubs.answer(invocation);
 }

 @Test
 public void testRecordDeepStubMockMethod() {
  Class<Map> mapClass = Map.class;
  Class<Set> setClass = Set.class;
  Class<Number> numberClass = Number.class;

  InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
  InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);

  ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
  Object mock = returnsDeepStubs.recordDeepStubMock(invocation, container);

  Mockito.verify(container).addAnswer(Mockito.isA(Answer.class), Mockito.eq(false));
  Mockito.verify(container, Mockito.atLeastOnce()).getMock();

  Mockito.when(container.getMock()).thenReturn(Mockito.mock(mapClass, Answers.RETURNS_DEEP_STUBS));
  Map<Object, Set<Number>> map = (Map<Object, Set<Number>>) mock;
  Set<Number> set = map.entrySet().iterator().next().getValue();
  Number number = set.iterator().next();

  Mockito.verify(map).put(Mockito.any(), Mockito.isA(HashSet.class));
  Mockito.verify(set).add(Mockito.any(Number.class));
 }

 @Test
 public void testRecordDeepStubMockMethodWithNestedGenerics() {
  Class<Map> mapClass = Map.class;
  Class<Set> setClass = Set.class;
  Class<Number> numberClass = Number.class;
  Class<List> listClass = List.class;
  Class<String> stringClass = String.class;

  InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
  InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);

  ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
  Object mock = returnsDeepStubs.recordDeepStubMock(invocation, container);

  Mockito.verify(container).addAnswer(Mockito.isA(Answer.class), Mockito.eq(false));
  Mockito.verify(container, Mockito.atLeastOnce()).getMock();

  Mockito.when(container.getMock()).thenReturn(Mockito.mock(mapClass, Answers.RETURNS_DEEP_STUBS));
  Mockito.when(container.getMock()).thenReturn(Mockito.mock(listClass, Answers.RETURNS_DEEP_STUBS));
  Mockito.when(container.getMock()).thenReturn(Mockito.mock(Map.class, Answers.RETURNS_DEEP_STUBS));

  Map<Object, Map<String, List<Number>>> map = (Map<Object, Map<String, List<Number>>>) mock;
  Map<String, List<Number>> innerMap = map.entrySet().iterator().next().getValue();
  List<Number> list = innerMap.entrySet().iterator().next().getValue();

  Mockito.verify(container).addAnswer(Mockito.isA(Answer.class), Mockito.eq(false));
  Mockito.verify(container, Mockito.atLeastOnce()).getMock();
  Mockito.verify(container, Mockito.atLeastOnce()).getMock();
  Mockito.verify(container, Mockito.atLeastOnce()).getMock();

  Mockito.verify(map).put(Mockito.any(), Mockito.isA(Map.class));
  Mockito.verify(innerMap).put(Mockito.any(), Mockito.isA(List.class));
  Mockito.verify(list).add(Mockito.any(Number.class));
 }

 @Test(expected = Throwable.class)
 public void testRecordDeepStubMockMethodWithThrowableException() {
  InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
  InternalMockHandler<Object> handler = Mockito.mock(InternalMockHandler.class);
  Mockito.when(handler.getInvocationContainer()).thenReturn(container);
  InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

  ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
  Mockito.doThrow(new RuntimeException("Exception message")).when(container).addAnswer(Mockito.isA(Answer.class), Mockito.eq(false));

  returnsDeepStubs.answer(invocation);
 }
}