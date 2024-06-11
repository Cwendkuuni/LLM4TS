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
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mockGenericsNest;
 private Object number;

 @Before
 public void setUp() {
 returnsDeepStubs = new ReturnsDeepStubs();
 mockGenericsNest = mock(Map.class, new ReturnsDeepStubs());
 number = mock(Number.class);
 }

 @Test
 public void testReturnsDeepStubsForNestedGenericTypes() {
 // given
 Map<Number, Set<Number>> genericsNest = (Map<Number, Set<Number>>) mockGenericsNest;
 Set<Number> set = mock(Set.class);
 given(genericsNest.entrySet()).willReturn(Arrays.asList(new Map.Entry<Number, Set<Number>>() {
 @Override
 public Number getKey() {
 return number;
 }

 @Override
 public Set<Number> getValue() {
 return set;
 }
 }).iterator());
 given(set.iterator()).willReturn(Arrays.asList(number).iterator());

 // when
 Number deepStubNumber = (Number) genericsNest.entrySet().iterator().next().getValue().iterator().next();

 // then
 assertNotNull(deepStubNumber);
 verify(set).iterator();
 verify(number).equals(number);
 }

 @Test(expected = IllegalStateException.class)
 public void testThrowsIllegalStateExceptionForNonMockableTypes() {
 // given
 Object nonMockableType = new Object() {};

 // when, then
 given(returnsDeepStubs.answer(ArgumentMatchers.any(InvocationOnMock.class))).willReturn(nonMockableType);
 Mockito.mock(String.class, new ReturnsDeepStubs());
 }

 @Test
 public void testGetMock() {
 // given
 Object mock = mock(Object.class, new ReturnsDeepStubs());
 InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
 Mockito.when(Mockito.mock(Object.class)).thenReturn(container);

 // when
 Object answer = returnsDeepStubs.getMock(Mockito.argThat(i -> i.getMock() == mock));

 // then
 assertNotNull(answer);
 verify(container).addAnswer(ArgumentMatchers.any(Answer.class), ArgumentMatchers.eq(false));
 }

 @Test
 public void testRecordDeepStubMock() {
 // given
 Object mock = mock(Object.class, new ReturnsDeepStubs());
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 InternalMockHandler<Object> handler = Mockito.mock(InternalMockHandler.class);
 Mockito.when(Mockito.mock(Object.class)).thenReturn(handler);
 InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
 Mockito.when(handler.getInvocationContainer()).thenReturn(container);

 // when
 Object answer = returnsDeepStubs.recordDeepStubMock(ArgumentMatchers.eq(invocation), ArgumentMatchers.eq(container));

 // then
 assertNotNull(answer);
 }
}