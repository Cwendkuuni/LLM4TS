package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object shallowMock;
 private Object deepMock;

 @Before
 public void setup() {
 shallowMock = mock(Object.class);
 deepMock = mock(Object.class, Answers.RETURNS_DEEP_STUBS);
 returnsDeepStubs = new ReturnsDeepStubs();
 }

 @Test
 public void testReturningShallowMock() {
 // given
 Object result = returnsDeepStubs.answer(new InvocationOnMock(shallowMock, new Object()[0], new Object[]{}));

 // then
 assertSame(shallowMock, result);
 }

 @Test
 public void testReturningDeepMockWithSameClass() {
 // given
 Object deepMock1 = mock(Object.class, Answers.RETURNS_DEEP_STUBS);
 Object deepMock2 = mock(Object.class, Answers.RETURNS_DEEP_STUBS);

 // when
 Object result = returnsDeepStubs.answer(new InvocationOnMock(deepMock1, new Object()[0], new Object[]{}));

 // then
 assertSame(deepMock1, result);
 // deep stub matches invocation for verification
 verify(deepMock1).something();
 }

 @Test
 public void testReturningDeepMockWithDifferentClasses() {
 // given
 Object deepMock1 = mock(List.class, Answers.RETURNS_DEEP_STUBS);
 Object deepMock2 = mock(Map.class, Answers.RETURNS_DEEP_STUBS);

 // when
 Object result = returnsDeepStubs.answer(new InvocationOnMock(deepMock1, new Object[0], new Object[]{"entrySet"}));

 // then
 Map<?, ?> resultMap = (Map<?, ?>) result;
 assertEquals(deepMock2, resultMap.values().iterator().next().iterator().next());
 // deep stub matches invocation for verification
 verify(deepMock1).something();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testReturningUnmockableType() {
 // given
 Object unmockableMock = mock(Number[].class, Answers.RETURNS_DEEP_STUBS);

 // when, then
 returnsDeepStubs.answer(new InvocationOnMock(unmockableMock, new Object[0], new Object[]{}));
 }

 @Test
 public void testRecordDeepStubMock() {
 // given
 Object deepMock = mock(Object.class, Answers.RETURNS_DEEP_STUBS);
 InvocationContainerImpl container = (InvocationContainerImpl) Mockito.mockHandler(deepMock).getInvocationContainer();

 // when
 Object recordedMock = returnsDeepStubs.recordDeepStubMock(deepMock, container);

 // then
 assertEquals(deepMock, recordedMock);
 }
}