package org.mockito.internal.stubbing.defaultanswers.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.stubbing.answers.ReturnsDeepStubs;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

 @Test
 public void testAnswerMethodWithSimpleType() {
 Object mock = Mockito.mock(String.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setMethod(Mockito.method(String.class, "toString"));

 Object result = returnsDeepStubs.answer(invocation);

 Assertions.assertEquals(mock, result);
 }

 @Test
 public void testAnswerMethodWithGenericType() {
 interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
 }

 GenericsNest<Object> mock = Mockito.mock(GenericsNest.class, new ReturnsDeepStubs());
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setMethod(Mockito.method(GenericsNest.class, "entrySet"));

 Number number = Mockito.mock(Number.class, new ReturnsDeepStubs());
 Map.Entry<Object, Set<Number>> entry = Mockito.mock(Map.Entry.class, new ReturnsDeepStubs());
 Set<Number> set = Mockito.mock(Set.class, new ReturnsDeepStubs());

 Mockito.when(entry.getValue()).thenReturn(number);
 Mockito.when(number.iterator()).thenReturn(Mockito.mock(java.util.Iterator.class, new ReturnsDeepStubs()));
 Mockito.when(Mockito.iterator(Mockito.mock(java.util.Collection.class, new ReturnsDeepStubs()))).thenReturn(Mockito.mock(java.util.Iterator.class, new ReturnsDeepStubs()));

 Mockito.when(invocation.getArguments()).thenReturn(new Object[]{mock});
 Mockito.when(invocation.getMethod()).thenReturn(Mockito.method(GenericsNest.class, "entrySet"));

 Object result = returnsDeepStubs.answer(invocation);

 Assertions.assertEquals(mock, result);
 }

 @Test
 public void testGetMockMethodWithExistingMock() {
 Object mock = Mockito.mock(String.class);
 InternalMockHandler<Object> handler = Mockito.mock(InternalMockHandler.class);
 InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
 Mockito.when(handler.getInvocationContainer()).thenReturn(container);
 Mockito.when(container.getStubbedInvocations()).thenReturn(Mockito.mock(StubbedInvocationMatcher[].class));

 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setMethod(Mockito.method(String.class, "toString"));
 Mockito.when(container.getInvocationForStubbing()).thenReturn(invocation);

 Object result = returnsDeepStubs.getMock(invocation);

 Assertions.assertEquals(mock, result);
 }

 @Test
 public void testGetMockMethodWithNewMock() {
 Object mock = Mockito.mock(String.class);
 InternalMockHandler<Object> handler = Mockito.mock(InternalMockHandler.class);
 InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
 Mockito.when(handler.getInvocationContainer()).thenReturn(container);

 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setMethod(Mockito.method(String.class, "toString"));
 Object newMock = Mockito.mock(String.class, new ReturnsDeepStubs());

 Mockito.when(handler.getMockHandler(mock)).thenReturn(handler);
 Mockito.when(container.getInvocationForStubbing()).thenReturn(invocation);

 Object result = returnsDeepStubs.getMock(invocation);

 Assertions.assertEquals(newMock, result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAnswerMethodWithUnsupportedType() {
 Object mock = Mockito.mock(Number[].class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setMethod(Mockito.method(Number[].class, "[", Mockito.type(Integer.class), Mockito.anyInt()));

 Object result = returnsDeepStubs.answer(invocation);

 Assertions.fail("Expected UnsupportedOperationException but got: " + result.getClass().getCanonicalName());
 }

 @Test
 public void testRecordDeepStubMockMethodWithExistingMock() {
 Object mock = Mockito.mock(String.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setMethod(Mockito.method(String.class, "toString"));
 InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);

 Mockito.when(container.getInvocationForStubbing()).thenReturn(invocation);
 Mockito.when(container.getStubbedInvocations()).thenReturn(Mockito.mock(StubbedInvocationMatcher[].class));

 ReturnsDeepStubs returnsDeepStubs = Mockito.spy(new ReturnsDeepStubs());
 Object deepStub = returnsDeepStubs.recordDeepStubMock(invocation, container);

 Assertions.assertEquals(mock, deepStub);
 }

 @Test
 public void testRecordDeepStubMockMethodWithNewMock() {
 Object mock = Mockito.mock(String.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setMethod(Mockito.method(String.class, "toString"));
 InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);

 Object deepStub = returnsDeepStubs.recordDeepStubMock(invocation, container);

 Assertions.assertEquals(mock, deepStub);
 }
}