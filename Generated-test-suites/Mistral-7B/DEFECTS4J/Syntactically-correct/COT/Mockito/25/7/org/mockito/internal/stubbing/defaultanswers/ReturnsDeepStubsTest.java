package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

 @Test
 public void testAnswerForBasicType() {
 Object mock = mock(Object.class, Answers.RETURNS_DEEP_STUBS);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Object result = returnsDeepStubs.answer(invocation);
 assertEquals(mock, result);
 }

 @Test
 public void testAnswerForRawType() {
 Object mock = mock(Object[].class, Answers.RETURNS_DEEP_STUBS);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Object result = returnsDeepStubs.answer(invocation);
 assertEquals(mock, result);
 }

 @Test
 public void testAnswerForInterface() {
 interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
 }

 GenericsNest<Object> mock = mock(GenericsNest.class, Answers.RETURNS_DEEP_STUBS);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Object result = returnsDeepStubs.answer(invocation);
 assertEquals(mock, result);
 }

 @Test
 public void testGetMockForMatchingInvocation() {
 Object mockObject = mock(Object.class, Answers.RETURNS_DEEP_STUBS);
 Object mockList = mock(List.class, Answers.RETURNS_DEEP_STUBS);
 Object mockMap = mock(Map.class, Answers.RETURNS_DEEP_STUBS);
 Object mockSet = mock(Set.class, Answers.RETURNS_DEEP_STUBS);
 Object mockNumber = mock(Number.class, Answers.RETURNS_DEEP_STUBS);

 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.getMock().add(mockObject);
 invocation.getMethod().setReturnType(List.class);

 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Object result = returnsDeepStubs.getMock(invocation);
 assertEquals(mockList, result);

 verify(mockObject).add(ArgumentMatchers.eq(mockList));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAnswerForNonMockableClass() {
 Object mockString = mock(String.class, Answers.RETURNS_DEEP_STUBS);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Object result = returnsDeepStubs.answer(invocation);
 }

 @Test
 public void testAnswerForNullInput() {
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.getMethod().setParameterTypes(new Class[]{Object.class});
 invocation.getMethod().setReturnType(Void.TYPE);
 invocation.getMock().setInvocationCount(1);
 Object result = returnsDeepStubs.answer(invocation);
 assertEquals(null, result);
 }

 @Test
 public void testAnswerForMultipleInvocations() {
 Object mock = mock(Object.class, Answers.RETURNS_DEEP_STUBS);
 InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
 InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
 invocation1.getMethod().setReturnType(List.class);
 invocation2.getMethod().setReturnType(Map.class);

 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Object result1 = returnsDeepStubs.answer(invocation1);
 Object result2 = returnsDeepStubs.answer(invocation2);
 assertEquals(mock, result1);
 assertEquals(mock, result2);
 }

 @Test
 public void testRecordDeepStubMock() {
 Object mockObject = mock(Object.class, Answers.RETURNS_DEEP_STUBS);
 Object mockList = mock(List.class, Answers.RETURNS_DEEP_STUBS);
 Object mockMap = mock(Map.class, Answers.RETURNS_DEEP_STUBS);
 Object mockSet = mock(Set.class, Answers.RETURNS_DEEP_STUBS);
 Object mockNumber = mock(Number.class, Answers.RETURNS_DEEP_STUBS);
 Object mockDeepStub = mock(ReturnsDeepStubs.class, Answers.RETURNS_DEEP_STUBS);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);

 ReturnsDeepStubs returnsDeepStubs = Mockito.spy(new ReturnsDeepStubs());
 Object result = returnsDeepStubs.answer(invocation);
 assertEquals(mockObject, result);

 verify(mockDeepStub).recordDeepStubMock(invocation, Mockito.any(InvocationContainerImpl.class));

 invocation2.getMethod().setParameterTypes(new Class[]{InvocationContainerImpl.class});
 invocation2.getMethod().setReturnType(Object[].class);
 returnsDeepStubs.answer(invocation2);
 assertEquals(Arrays.asList(mockObject, mockList, mockMap, mockSet, mockNumber), returnsDeepStubs.getMock(invocation2));
 }

}